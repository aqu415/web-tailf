/**管理界面代码*/
let admin_window_;
let deploy_tf_sock;

/**显示管理界面*/
function showAdmin() {
    if (!admin_window_) {

        let admin_west = {
            region: 'west',
            stateId: 'admin_west',
            id: 'admin_west', // see Ext.getCmp() below
            split: true,      // 可拖拽改变宽度
            width: 270,
            layout: 'fit',    //指定容器内部布局
            stateful: true,
            html: "000x"
        };

        let btn = Ext.create('Ext.Button', {
            x: 400, y: 70,
            text: '添加',
            listeners: {
                click: function () {
                    // 打开一个页面和后台进行websocket连接,返回一个唯一标记ID
                    $.get('/deployKey', function (uuid) {
                        if (deploy_tf_sock) {
                            deploy_tf_sock.close();
                        }
                        deploy_tf_sock = new TF_SOCK(function () {
                            console.log('uuid:' + uuid);
                            let obj = {};
                            obj.cmd = 'deploy';
                            obj.uuid = uuid;
                            obj.ip = Ext.getCmp('ip').value;    //Ext.getCmp('id').value
                            obj.userName = Ext.getCmp('userName').value;
                            obj.pw = Ext.getCmp('pw').value;
                            obj.remoteDir = Ext.getCmp('remoteDir').value;
                            deploy_tf_sock.send(JSON.stringify(obj));
                        }, function (e) {
                            e = e || event;  //获取事件，这样写是为了兼容IE浏览器
                            let data = e.data;
                            let old = Ext.getCmp('msg_').getValue();
                            Ext.getCmp('msg_').setValue(old + '\n' + data);
                        });
                        deploy_tf_sock.init_socket();
                    });
                }
            }
        });

        let admin_center = Ext.create('Ext.form.Panel', {
            region: "center",
            width: 800,
            height: 500,
            layout: 'absolute',      //指定Panel内部布局，X/Y配置选项对子组件进行定位
            defaultType: 'textfield',
            items: [
                {x: 10, y: 10, xtype: 'label', text: 'Slave IP:'}, {x: 70, y: 10, id: 'ip'}, {x: 260, y: 10, xtype: 'label', text: '用户名:'}, {x: 330, y: 10, id: 'userName'},
                {x: 10, y: 40, xtype: 'label', text: '密码：'}, {x: 70, y: 40, id: 'pw'}, {x: 260, y: 40, xtype: 'label', text: '部署路径:'}, {x: 330, y: 40, id: 'remoteDir'},
                {x: 10, y: 70, xtype: 'label', text: '日志路径：'}, {x: 70, y: 70, id: 'logPath', width: 260}, btn,
                {x: 0, y: 120, xtype: 'textareafield', anchor: '100% 100%', id: 'msg_'}/*控件从居左0px处拉长100%，空间从居上80px处，拉长至余下控件高度的80%处*/
            ],
        });

        admin_window_ = new Ext.Window({
            layout: 'border',  //设置window里面的布局(子元素将自动填满整个父容器)
            width: 800,
            height: 500,
            resizable: false,
            //关闭时执行隐藏命令,如果是close就不能再show出来了
            closeAction: 'hide',
            items: [admin_west, admin_center]
        });
    }
    admin_window_.show();
}