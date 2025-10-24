<template>
    <div class="customer-service-chat">
        <div class="chat-window" v-if="showChat">
            <div class="chat-header">
                <span>在线客服</span>
                <el-button @click="closeChat" type="danger" size="small" circle icon="Close" />
            </div>
            <div class="chat-messages" ref="messagesContainer">
                <div
                  v-for="(msg, index) in messages"
                  :key="index"
                  :class="['message', msg.from === 'user' ? 'user-message' : 'service-message']"
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
                  placeholder="请输入消息..."
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
        <div class="chat-toggle" v-else @click="openChat">
            <el-badge :is-dot="unreadCount > 0">
                <el-button type="primary" circle icon="Service" />
            </el-badge>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { connectWebsocket, closeWebsocket, getWebsocket } from '@/api/websocket'
import useUserStore from "@/store/modules/user"

const showChat = ref(false)
const messages = ref([])
const inputMessage = ref('')
const isConnected = ref(false)
const unreadCount = ref(0)
const messagesContainer = ref(null)
const messageInput = ref(null)
const userStore = useUserStore()

const WSUrl = import.meta.env.VITE_APP_WS_URL || 'ws://localhost:8080/websocket/'

// 格式化时间
const formatTime = (time) => {
    const date = new Date(time)
    return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

// 打开聊天窗口
const openChat = () => {
    showChat.value = true
    unreadCount.value = 0
    scrollToBottom()

    // 确保输入框获得焦点
    nextTick(() => {
        if (messageInput.value && messageInput.value.focus) {
            messageInput.value.focus()
        }
    })
}

// 关闭聊天窗口
const closeChat = () => {
    showChat.value = false
}

// 滚动到底部
const scrollToBottom = () => {
    nextTick(() => {
        if (messagesContainer.value) {
            messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
        }
    })
}

// 建立WebSocket连接
const initWebSocket = () => {
    const userId = userStore.id
    if (!userId) {
        ElMessage.error('用户未登录')
        return
    }

    connectWebsocket(
      `${WSUrl}${userId}`,
      (data) => {
          // 接收到消息的回调
          try {
              const messageData = JSON.parse(data)
              if (messageData.type === 'customer_service') {
                  messages.value.push({
                      from: 'service',
                      content: messageData.content,
                      time: new Date()
                  })

                  // 如果聊天窗口未打开，增加未读计数
                  if (!showChat.value) {
                      unreadCount.value++
                  }

                  scrollToBottom()
              }
          } catch (e) {
              console.log('Received non-JSON message:', data)
          }
      },
      (err) => {
          // 错误处理
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

// 发送消息
const sendMessage = () => {
    if (!inputMessage.value.trim()) {
        return
    }

    const msgObj = {
        type: 'customer_service',
        toUserId: 'customer_service', // 固定发送给客服
        content: inputMessage.value,
        fromUserId: userStore.id
    }

    const socket = getWebsocket()
    if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify(msgObj))
        // 添加到本地消息列表
        messages.value.push({
            from: 'user',
            content: inputMessage.value,
            time: new Date()
        })
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

onMounted(() => {
    initWebSocket()
})

onBeforeUnmount(() => {
    closeWebsocket()
})
</script>

<style scoped>
.customer-service-chat {
    position: fixed;
    bottom: 20px;
    right: 20px;
    z-index: 1000;
}

.chat-toggle {
    font-size: 24px;
}

.chat-toggle .el-button {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s;
}

.chat-toggle .el-button:hover {
    transform: scale(1.1);
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.chat-toggle .el-button i {
    font-size: 24px;
}

.chat-window {
    width: 350px;
    height: 500px;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
}

.chat-header {
    padding: 15px;
    border-bottom: 1px solid #e4e7ed;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #409eff;
    color: white;
    border-radius: 8px 8px 0 0;
}

.chat-messages {
    flex: 1;
    padding: 15px;
    overflow-y: auto;
    background: #f5f7fa;
}

.message {
    margin-bottom: 15px;
    display: flex;
}

.user-message {
    justify-content: flex-end;
}

.service-message {
    justify-content: flex-start;
}

.message-content {
    max-width: 80%;
}

.user-message .message-content {
    background: #409eff;
    color: white;
    border-radius: 15px 5px 15px 15px;
}

.service-message .message-content {
    background: #fff;
    color: #333;
    border-radius: 5px 15px 15px 15px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
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
    padding: 15px;
    border-top: 1px solid #e4e7ed;
    background: #fff;
}

.chat-input .el-input {
    margin-right: 10px;
}

.chat-toggle .el-badge {
    display: flex;
}
</style>
