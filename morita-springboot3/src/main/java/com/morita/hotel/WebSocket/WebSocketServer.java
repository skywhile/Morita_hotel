package com.morita.hotel.WebSocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.morita.system.domain.SysUser;
import com.morita.system.general.utils.StringUtils;
import com.morita.system.service.ISysUserService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

// 添加AI对话服务相关导入
import com.morita.hotel.service.DialogueService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketServer implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    // 静态变量，用来记录当前在线连接数
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    // concurrent包的线程安全Map，用来存放每个客户端对应的WebSocket对象
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    // 记录已转人工的用户，key为userId，value为true表示已转人工
    private static ConcurrentHashMap<String, Boolean> transferredToHumanMap = new ConcurrentHashMap<>();

    // 注入用户服务
    private static ISysUserService userService;
    
    // 注入对话服务
    private static DialogueService dialogueService;

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 接收userId
    private String userId = "";
    
    // Spring应用上下文
    private static ApplicationContext applicationContext;
    
    @Autowired
    public void setUserService(ISysUserService userService) {
        WebSocketServer.userService = userService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketServer.applicationContext = applicationContext;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else {
            webSocketMap.put(userId, this);
            addOnlineCount();
        }
        
        // 初始化对话服务
        if (dialogueService == null) {
            // 修复NoUniqueBeanDefinitionException错误，明确指定Bean名称
            dialogueService = applicationContext.getBean("dialogueService", DialogueService.class);
        }
        
        log.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());

        // 如果是客服连接，发送在线用户列表
        if ("customer_service".equals(userId)) {
            sendOnlineUsersList();
        } else {
            // 如果是普通用户连接，通知客服有新用户上线
            notifyCustomerServiceUserOnline(userId);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineCount();
            // 移除转人工状态记录
            transferredToHumanMap.remove(userId);
            log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());

            // 如果是普通用户下线，通知客服
            if (!"customer_service".equals(userId)) {
                notifyCustomerServiceUserOffline(userId);
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + userId + ",报文:" + message);
        // 可以群发消息
        // 消息处理
        if (StringUtils.isNotBlank(message)) {
            try {
                // 解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                // 追加发送人(防止串改)
                jsonObject.put("fromUserId", this.userId);
                String toUserId = jsonObject.getString("toUserId");
                String messageType = jsonObject.getString("type");
                String content = jsonObject.getString("content");

                // 处理AI助手请求
                if ("ai_assistant".equals(messageType)) {
                    handleAiAssistantRequest(jsonObject);
                    return;
                }

                // 特殊处理客服相关消息
                if ("customer_service".equals(toUserId)) {
                    // 处理用户问题
                    if (StringUtils.isNotBlank(content)) {
                        // 不再保存用户问题到对话历史
                    }
                    
                    // 检查是否已经转人工或者需要转人工客服
                    boolean isTransferred = isUserTransferredToHuman();
                    boolean needTransfer = needTransferToHuman(content);
                    
                    if (isTransferred || needTransfer) {
                        // 如果已经转人工或者需要转人工，则直接转发给客服
                        
                        // 如果是首次转人工，需要执行转人工流程
                        if (needTransfer && !isTransferred) {
                            // 标记用户已转人工
                            transferredToHumanMap.put(userId, true);
                            
                            // 先给用户发送转接提示
                            sendTransferPromptToUser();
                        }
                        
                        // 转人工客服（无论客服是否在线都发送）
                        WebSocketServer customerService = webSocketMap.get("customer_service");
                        if (customerService != null) {
                            // 发送给客服
                            customerService.sendMessage(jsonObject.toJSONString());
                        }
                    } else {
                        // 默认由AI处理
                        handleAiAssistantAutoReply(content, userId);
                    }
                } else if ("customer_service".equals(this.userId)) {
                    // 客服发送给用户
                    if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
                        webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                        // 不再保存客服回复到对话历史
                    } else {
                        log.error("请求的userId:" + toUserId + "不在该服务器上");
                        // 否则不在这个服务器上
                    }
                } else {
                    // 普通用户之间的消息
                    if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
                        webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                    } else {
                        log.error("请求的userId:" + toUserId + "不在该服务器上");
                        // 否则不在这个服务器上
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送转接提示给用户
     */
    private void sendTransferPromptToUser() {
        try {
            JSONObject response = new JSONObject();
            response.put("type", "customer_service");
            response.put("fromUserId", "customer_service");
            response.put("toUserId", this.userId);
            response.put("content", "好的，正在为您转接人工客服，请稍候...");
            response.put("timestamp", System.currentTimeMillis());
            this.sendMessage(response.toJSONString());
        } catch (IOException e) {
            log.error("发送转接提示失败", e);
        }
    }

    /**
     * 判断用户是否已经转人工
     * @return 是否已经转人工
     */
    private boolean isUserTransferredToHuman() {
        return transferredToHumanMap.getOrDefault(userId, false);
    }

    /**
     * 判断是否需要转人工客服
     * @param content 用户消息内容
     * @return 是否需要转人工
     */
    private boolean needTransferToHuman(String content) {
        if (StringUtils.isBlank(content)) {
            return false;
        }
        
        // 定义需要转人工的关键词
        String[] keywords = {"转人工", "人工客服", "人工服务", "找客服", "联系客服", "人工", "客服"};
        
        for (String keyword : keywords) {
            if (content.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 处理AI助手请求
     * @param jsonObject 请求数据
     */
    private void handleAiAssistantRequest(JSONObject jsonObject) {
        try {
            String userMessage = jsonObject.getString("message");
            String response = dialogueService.chat(userMessage);
            
            // 构造返回给用户的AI响应
            JSONObject aiResponse = new JSONObject();
            aiResponse.put("type", "ai_assistant_response");
            aiResponse.put("message", response);
            aiResponse.put("timestamp", System.currentTimeMillis());
            
            // 发送AI响应给用户
            this.sendMessage(aiResponse.toJSONString());
        } catch (Exception e) {
            log.error("处理AI助手请求失败", e);
            try {
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("type", "ai_assistant_error");
                errorResponse.put("message", "抱歉，AI助手当前不可用，请稍后再试。");
                this.sendMessage(errorResponse.toJSONString());
            } catch (IOException ioException) {
                log.error("发送AI助手错误响应失败", ioException);
            }
        }
    }

    /**
     * 处理AI助手自动回复（默认处理方式）
     * @param content 用户消息内容
     * @param userId 用户ID
     */
    private void handleAiAssistantAutoReply(String content, String userId) {
        try {
            String response = dialogueService.chat(content);
            
            // 构造返回给用户的AI响应
            JSONObject aiResponse = new JSONObject();
            aiResponse.put("type", "customer_service");
            aiResponse.put("fromUserId", "customer_service");
            aiResponse.put("toUserId", userId);
            aiResponse.put("content", response);
            aiResponse.put("timestamp", System.currentTimeMillis());
            
            // 发送AI响应给用户
            this.sendMessage(aiResponse.toJSONString());
            
            // 不再保存AI回复到对话历史
        } catch (Exception e) {
            log.error("处理AI助手自动回复失败", e);
            try {
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("type", "customer_service");
                errorResponse.put("fromUserId", "customer_service");
                errorResponse.put("toUserId", userId);
                errorResponse.put("content", "抱歉，AI助手当前不可用，请稍后再试。");
                this.sendMessage(errorResponse.toJSONString());
            } catch (IOException ioException) {
                log.error("发送AI助手自动回复失败", ioException);
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送在线用户列表给客服
     */
    private void sendOnlineUsersList() {
        try {
            WebSocketServer customerService = webSocketMap.get("customer_service");
            if (customerService != null) {
                List<Map<String, String>> users = new ArrayList<>();
                for (String userId : webSocketMap.keySet()) {
                    if (!"customer_service".equals(userId)) {
                        Map<String, String> user = new HashMap<>();
                        user.put("userId", userId);

                        // 获取用户名信息
                        try {
                            long userIdLong = Long.parseLong(userId);
                            SysUser sysUser = userService.selectUserById(userIdLong);
                            if (sysUser != null) {
                                user.put("userName", sysUser.getUserName());
                                user.put("nickName", sysUser.getNickName());
                            }
                        } catch (NumberFormatException e) {
                            log.warn("无法解析用户ID为数字: " + userId);
                        }

                        users.add(user);
                    }
                }

                JSONObject message = new JSONObject();
                message.put("type", "online_users");
                message.put("users", users);
                customerService.sendMessage(message.toJSONString());
            }
        } catch (Exception e) {
            log.error("发送在线用户列表失败", e);
        }
    }

    /**
     * 通知客服有用户上线
     */
    private void notifyCustomerServiceUserOnline(String userId) {
        try {
            WebSocketServer customerService = webSocketMap.get("customer_service");
            if (customerService != null) {
                JSONObject message = new JSONObject();
                message.put("type", "user_online");
                message.put("userId", userId);

                // 添加用户详细信息
                try {
                    long userIdLong = Long.parseLong(userId);
                    SysUser sysUser = userService.selectUserById(userIdLong);
                    if (sysUser != null) {
                        message.put("userName", sysUser.getUserName());
                        message.put("nickName", sysUser.getNickName());
                    }
                } catch (NumberFormatException e) {
                    log.warn("无法解析用户ID为数字: " + userId);
                }

                customerService.sendMessage(message.toJSONString());
                // 重新发送用户列表
                sendOnlineUsersList();
            }
        } catch (Exception e) {
            log.error("通知客服用户上线失败", e);
        }
    }

    /**
     * 通知客服有用户下线
     */
    private void notifyCustomerServiceUserOffline(String userId) {
        try {
            WebSocketServer customerService = webSocketMap.get("customer_service");
            if (customerService != null) {
                JSONObject message = new JSONObject();
                message.put("type", "user_offline");
                message.put("userId", userId);
                customerService.sendMessage(message.toJSONString());
                // 重新发送用户列表
                sendOnlineUsersList();
            }
        } catch (Exception e) {
            log.error("通知客服用户下线失败", e);
        }
    }

    public static int getOnlineCount() {
        return onlineCount.get();
    }

    public static void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineCount.decrementAndGet();
    }

}