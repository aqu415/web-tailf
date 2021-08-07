let TF_SOCK = function (onopen, onmessage, onclose) {
    this.tf_sock = null;
    this.onopen = onopen;
    this.onmessage = onmessage;
    this.onclose = onclose;
}
TF_SOCK.prototype = {
    init_socket: function () {
        if ('WebSocket' in window) {
            let url = 'ws://' + window.location.host + '/websocket01';
            /**打开WebSocket*/
            this.tf_sock = new WebSocket(url);
            /**处理连接开启事件*/
            if (this.callable(this.onopen)) {
                this.tf_sock.onopen = this.onopen;
            }
            /**处理server发送来的消息*/
            this.tf_sock.onmessage = this.onmessage;
            /**处理连接关闭事件*/
            this.tf_sock.onclose = this.onclose;
        } else {
            alert("你的浏览器不支持WebSocket");
        }
    },
    send: function (msg) {
        this.tf_sock.send(msg);
    },
    callable: function (obj) {
        return typeof obj === "function"
    },
    destroy: function () {
        if (this.tf_sock) {
            this.tf_sock.close();
        }
    }
}