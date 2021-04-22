/**获得根元素*/
function getRootNodes(){
    let arr = [];
    let cur = $('#path_config').val();
    if (cur) {
        let all = cur.split(';');
        for (let i in all) {
            let item = all[i];
            let node = {};
            node.name = item;
            node.path = item;
            node.open = false;
            node.isParent = true;
            node.id = 'P_' + Math.random() * 100;
            arr.push(node);
        }
    }
    return arr;
}

/**展开第一个节点*/
function openTree(){
    setTimeout(function(){
        let treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        let nodes = treeObj.getNodes();
        if (nodes.length>0) {
            getNodeByParam(nodes[0]);
            treeObj.expandNode(nodes[0], true, true, true);
        }
    },300);
}

/**根据参数获得路径*/
function getNodeByParam(node){
    $.get('/path?key=' + node.path, function(data){
        let data_ = JSON.parse(data);
        for(let i in data_){
            let item = data_[i];
            item.id = 'P_' + Math.random() * 100;
            zTreeObj.addNodes(node, item, true);
        }
    });
}

/**是否可以展示内容*/
function enableView(path){
    return (endWith(path,'.log') || endWith(path,'.txt'));
}

function endWith(src,str){
    let reg = new RegExp(str + "$");
    return reg.test(src);
}