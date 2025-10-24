let websocket = null; // 将websocket声明为模块变量
let messageCallback = null;
let errorCallback = null;
let wsUrl = '';

// 接收ws后端返回的数据
function websocketonmessage(e) {
    if (messageCallback) {
        messageCallback(e.data);
    }
}

/**
 * 发起websocket连接
 * @param {Object} agentData 需要向后台传递的参数数据
 */
function websocketOpen(e) {
    console.log('连接成功');
}

// 连接建立失败重连
function websocketonerror(e) {
    console.error('WebSocket连接错误:', e);
    if (errorCallback) {
        errorCallback(e);
    }
    reconnect(wsUrl);
}

// ws连接关闭
function websocketclose(e) {
    console.log('连接关闭');
}

// 关闭ws连接
export function closeWebsocket() {
    if (websocket) {
        websocket.close();
        websocket = null;
    }
}

// 发起ws连接
function initWebSocket(url) {
    // 检查浏览器是否支持WebSocket
    if (typeof (WebSocket) === 'undefined') {
        console.log('您的浏览器不支持WebSocket');
        if (errorCallback) {
            errorCallback(new Error('您的浏览器不支持WebSocket'));
        }
        return false;
    }

    // 如果已存在连接且处于开启状态，先关闭
    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.close();
    }

    wsUrl = url;
    websocket = new WebSocket(url);

    websocket.onmessage = function(e) {
        websocketonmessage(e);
    };
    websocket.onopen = function() {
        websocketOpen();
    };
    websocket.onerror = function(e) {
        websocketonerror(e);
    };
    websocket.onclose = function() {
        websocketclose();
    };

    return websocket;
}

// 重新连接
function reconnect(url) {
    // 如果当前连接已经打开，则不重连
    if (websocket && websocket.readyState === WebSocket.OPEN) {
        return;
    }

    setTimeout(function() {
        initWebSocket(url);
    }, 3000);
}

/**
 * 初始化websocket
 * @param {string} url ws连接地址
 * @param {function} successCallback 接收到ws消息回调
 * @param {function} errCallback ws连接错误的回调
 */
export function connectWebsocket(url, successCallback, errCallback) {
    messageCallback = successCallback;
    errorCallback = errCallback;
    return initWebSocket(url);
}

export function getWebsocket() {
    return websocket;
}

export default {
    connectWebsocket,
    closeWebsocket,
    getWebsocket
};
