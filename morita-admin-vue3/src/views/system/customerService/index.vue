<template>
    <div class="customer-service-container">
        <el-container style="height: calc(100vh - 120px)">
            <el-aside width="300px" class="online-users">
                <div class="online-users-header">
                    <h3>在线用户</h3>
                    <el-tag type="success">在线: {{ onlineCount }}</el-tag>
                </div>
                <el-menu
                  :default-active="activeUser"
                  class="user-list"
                  @select="handleUserSelect"
                >
                    <el-menu-item
                      v-for="user in onlineUsers"
                      :key="user.userId"
                      :index="user.userId"
                    >
                        <el-badge :is-dot="user.unread > 0">
                            <i class="el-icon-user"></i>
                        </el-badge>
                        <span>{{ user.nickName || user.userName || user.userId }}</span>
                        <el-tag v-if="user.unread > 0" type="danger" size="small">{{ user.unread }}</el-tag>
                    </el-menu-item>
                </el-menu>
            </el-aside>

            <el-main class="chat-main">
                <div v-if="activeUser" class="chat-container">
                    <div class="chat-header">
                        <h3>与 {{ activeUserNickName || activeUserUserName || activeUserName || activeUser }} 的对话</h3>
                    </div>

                    <div class="chat-messages" ref="messagesContainer">
                        <div
                          v-for="(msg, index) in currentMessages"
                          :key="index"
                          :class="['message', msg.from === 'service' ? 'service-message' : 'user-message']"
                        >
                            <div class="message-content">
                                <div class="message-text">{{ msg.content }}</div>
                                <div class="message-time">{{ formatTime(msg.time) }}</div>
                            </div>
                        </div>
                    </div>

                    <div class="chat-input">
                        <el-input
                          v-model="inputMessage"
                          placeholder="请输入回复内容..."
                          @keyup.enter="sendMessage"
                          :disabled="!isConnected"
                          ref="messageInput"
                        />
                        <el-button
                          @click="sendMessage"
                          type="primary"
                          :disabled="!isConnected || !inputMessage.trim()"
                        >
                            发送
                        </el-button>
                    </div>
                </div>

                <div v-else class="no-user-selected">
                    <el-empty description="请选择一个用户开始对话" />
                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { connectWebsocket, closeWebsocket, getWebsocket } from '@/api/websocket'
import useUserStore from "@/store/modules/user"

const onlineUsers = ref([])
const activeUser = ref('')
const activeUserName = ref('')
const activeUserUserName = ref('') // 添加userName字段
const activeUserNickName = ref('') // 添加nickName字段
const currentMessages = ref([])
const inputMessage = ref('')
const isConnected = ref(false)
const messagesContainer = ref(null)
const messageInput = ref(null)
const onlineCount = ref(0)
const userStore = useUserStore()

const WSUrl = import.meta.env.VITE_APP_WS_URL || 'ws://localhost:8080/websocket/'

// 格式化时间
const formatTime = (time) => {
    const date = new Date(time)
    return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`
}

// 滚动到底部
const scrollToBottom = () => {
    nextTick(() => {
        if (messagesContainer.value) {
            messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
        }
    })
}

// 处理用户选择
const handleUserSelect = (userId) => {
    activeUser.value = userId
    // 找到用户名称
    const user = onlineUsers.value.find(u => u.userId === userId)
    if (user) {
        activeUserName.value = user.name || userId
        activeUserUserName.value = user.userName || userId // 设置userName
        activeUserNickName.value = user.nickName || user.userName || userId // 设置nickName
        // 创建当前消息的副本，而不是直接引用用户消息数组
        currentMessages.value = user.messages.map(msg => ({...msg})) || []
        // 清除未读标记
        user.unread = 0
    } else {
        currentMessages.value = []
    }
    scrollToBottom()

    // 确保输入框获得焦点
    nextTick(() => {
        if (messageInput.value && messageInput.value.focus) {
            messageInput.value.focus()
        }
    })
}

// 发送消息
const sendMessage = () => {
    if (!inputMessage.value.trim() || !activeUser.value) {
        return
    }

    const msgObj = {
        type: 'customer_service',
        toUserId: activeUser.value,
        content: inputMessage.value,
        fromUserId: 'customer_service'
    }

    const socket = getWebsocket()
    if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify(msgObj))

        // 添加到本地消息列表
        const message = {
            from: 'service',
            content: inputMessage.value,
            time: new Date()
        }

        currentMessages.value.push({...message})

        // 同时保存到用户消息历史
        const user = onlineUsers.value.find(u => u.userId === activeUser.value)
        if (user) {
            // 检查是否已存在相同消息
            const isMessageExists = user.messages.some(msg =>
              msg.content === message.content &&
              msg.from === message.from &&
              Math.abs(new Date(msg.time).getTime() - new Date(message.time).getTime()) < 1000
            )

            if (!isMessageExists) {
                user.messages.push({...message})
            }
        }

        inputMessage.value = ''
        scrollToBottom()

        // 发送消息后确保输入框获得焦点
        nextTick(() => {
            if (messageInput.value && messageInput.value.focus) {
                messageInput.value.focus()
            }
        })
    } else {
        ElMessage.error('WebSocket连接未建立，消息发送失败')
    }
}

// 建立WebSocket连接
const initWebSocket = () => {
    // 客服ID固定为customer_service
    const serviceId = 'customer_service'

    connectWebsocket(
      `${WSUrl}${serviceId}`,
      (data) => {
          try {
              const messageData = JSON.parse(data)
              // 处理在线用户列表更新
              if (messageData.type === 'online_users') {
                  onlineUsers.value = messageData.users.map(user => ({
                      userId: user.userId,
                      userName: user.userName, // 添加userName字段
                      nickName: user.nickName, // 添加nickName字段
                      name: user.name || user.userId,
                      unread: 0,
                      messages: []
                  }))
                  onlineCount.value = messageData.users.length
              }
              // 处理新消息
              else if (messageData.type === 'customer_service') {
                  // 如果消息来自客服（即客服回复），避免重复显示
                  if (messageData.fromUserId === 'customer_service') {
                      // 不再重复添加到当前消息列表，因为客服发送的消息已经在sendMessage方法中添加了
                      return
                  }

                  const fromUserId = messageData.fromUserId
                  // 添加到消息列表
                  const message = {
                      from: 'user',
                      content: messageData.content,
                      time: new Date()
                  }

                  // 查找用户或创建新用户
                  let user = onlineUsers.value.find(u => u.userId === fromUserId)
                  if (!user) {
                      user = {
                          userId: fromUserId,
                          name: fromUserId,
                          unread: 0,
                          messages: []
                      }
                      onlineUsers.value.push(user)
                      onlineCount.value = onlineUsers.value.length
                  }

                  // 检查消息是否已存在，避免重复添加（基于内容和时间）
                  const isMessageExists = user.messages.some(msg =>
                    msg.content === message.content &&
                    Math.abs(new Date(msg.time).getTime() - new Date(message.time).getTime()) < 1000
                  )

                  if (!isMessageExists) {
                      // 将消息添加到用户历史记录
                      user.messages.push({...message})

                      // 如果是当前活跃用户，添加到当前消息列表
                      if (activeUser.value === fromUserId) {
                          // 再次检查当前显示列表中是否已存在该消息
                          const isCurrentMessageExists = currentMessages.value.some(msg =>
                            msg.content === message.content &&
                            Math.abs(new Date(msg.time).getTime() - new Date(message.time).getTime()) < 1000
                          )

                          if (!isCurrentMessageExists) {
                              currentMessages.value.push({...message})
                              scrollToBottom()
                          }
                      } else {
                          // 否则增加未读计数
                          user.unread++
                      }
                  }
              }
              // 处理用户上线通知
              else if (messageData.type === 'user_online') {
                  const userId = messageData.userId
                  // 检查用户是否已存在
                  let user = onlineUsers.value.find(u => u.userId === userId)
                  if (!user) {
                      // 添加新用户
                      user = {
                          userId: userId,
                          userName: messageData.userName, // 添加userName字段
                          nickName: messageData.nickName, // 添加nickName字段
                          name: messageData.nickName || messageData.userName || userId,
                          unread: 0,
                          messages: []
                      }
                      onlineUsers.value.push(user)
                      onlineCount.value = onlineUsers.value.length
                  }
              }
          } catch (e) {
              console.log('Received non-JSON message:', data)
          }
      },
      (err) => {
          console.error('WebSocket错误:', err)
          isConnected.value = false
      }
    )

    // 定期检查连接状态
    const checkConnection = () => {
        const socket = getWebsocket()
        if (socket) {
            isConnected.value = socket.readyState === WebSocket.OPEN
        }
    }

    // 定期检查连接状态以确保界面更新
    const connectionInterval = setInterval(checkConnection, 1000)

    // 初始检查
    setTimeout(checkConnection, 100)

    onBeforeUnmount(() => {
        clearInterval(connectionInterval)
    })
}

onMounted(() => {
    initWebSocket()
})

onBeforeUnmount(() => {
    closeWebsocket()
})
</script>

<style scoped>
.customer-service-container {
    padding: 20px;
    background: #fff;
}

.online-users {
    border-right: 1px solid #e4e7ed;
    background: #f5f7fa;
}

.online-users-header {
    padding: 15px;
    border-bottom: 1px solid #e4e7ed;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.user-list {
    border: none;
    height: calc(100% - 60px);
    overflow-y: auto;
}

.user-list .el-menu-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.chat-header {
    padding: 15px 0;
    border-bottom: 1px solid #e4e7ed;
}

.chat-messages {
    height: calc(100% - 120px);
    overflow-y: auto;
    padding: 15px;
    background: #f5f7fa;
}

.message {
    margin-bottom: 15px;
    display: flex;
}

.user-message {
    justify-content: flex-start;
}

.service-message {
    justify-content: flex-end;
}

.message-content {
    max-width: 80%;
}

.user-message .message-content {
    background: #fff;
    color: #333;
    border-radius: 5px 15px 15px 15px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.service-message .message-content {
    background: #409eff;
    color: white;
    border-radius: 15px 5px 15px 15px;
}

.message-text {
    padding: 10px;
}

.message-time {
    font-size: 12px;
    color: #999;
    text-align: right;
    padding: 0 10px 5px;
}

.chat-input {
    display: flex;
    padding: 15px 0;
    border-top: 1px solid #e4e7ed;
}

.chat-input .el-input {
    margin-right: 10px;
}

.no-user-selected {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
}
</style>
