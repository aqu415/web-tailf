/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('MyDesktop.PCConfig', {
    extend: 'Ext.ux.desktop.Module',
    requires: [],
    id: 'PCConfig_',
    init: function () {
        console.log('csdn：https://blog.csdn.net/Aqu415/article/details/114419320');
        console.log('gitee：https://gitee.com/aqu415/tailf');
    },
    createWindow: function () {
        let desktop = this.app.getDesktop();
        let win = desktop.getWindow('admin');

        if (!win) {
            let grid = this.buildGrid();
            win = desktop.createWindow({
                id: 'admin',
                title: '服务器管理',
                width: 1000,
                height: 500,
                animCollapse: false,
                constrainHeader: true,
                bodyBorder: true,
                tbar: {
                    xtype: 'toolbar',
                    ui: 'plain',
                    items: [{
                        tooltip: {title: 'Rich Tooltips', text: 'Let your users know what they can do!'},
                        iconCls: 'connect'
                    },
                        '-',
                        {
                            tooltip: 'Add a new user',
                            iconCls: 'user-add'
                        },
                        ' ',
                        {
                            tooltip: 'Remove the selected user',
                            iconCls: 'user-delete'
                        }]
                },
                layout: 'border',
                border: false,
                items: [grid]
            });
        }
        return win;
    },
    buildGrid() {
        // 建立一个store要使用的 model
        Ext.define('config_Model', {
            extend: 'Ext.data.Model',
            fields: [
                {name: 'idx', type: 'string'},
                {name: 'ip', type: 'string'},
                {name: 'bslj', type: 'string'},
                {name: 'jtlj', type: 'string'},
                {name: 'status', type: 'string'},
                {name: 'opt', type: 'string'}
            ]
        });

        let store_ = Ext.create('Ext.data.Store', {
            model: 'config_Model',
            proxy: {
                type: 'ajax',
                url: '/ci',
                reader: {
                    type: 'json'
                }
            },
            autoLoad: true
        });

        // create the grid
        let grid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            sortableColumns: false,
            store: store_,
            columns: [
                {text: "序号", dataIndex: 'idx'},
                {text: "IP", dataIndex: 'ip'},
                {text: "注册日期", dataIndex: 'zcrq'},
                {text: "部署路径", dataIndex: 'bslj'},
                {text: "监听路径", dataIndex: 'jtlj'},
                {text: "状态", dataIndex: 'status'},
                {text: "操作", dataIndex: 'opt'}
            ]
        });
        return grid;
    }
});
