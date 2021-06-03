var publicElem = undefined;
layui.use('tree', function() {
    let tree = layui.tree;
    tree.render({
        elem: "#toolBar",
        showCheckbox: false,  //是否显示复选框
        showLine: false,
        click: function(obj) { // obj.data --> 节点数据，obj.state --> 节点状态，obj.elem --> 节点元素
            let elem = obj.elem;
            let content = $("#content");
            let pageId = obj.data['id'];
            let table = new Map()
            table.set("sysBlogAdd", "component/sysBlogAdd.html");
            table.set("sysBlogList", "component/sysBlogList.html");
            table.set("sysCategory", "component/sysCategory.html");
            table.set("sysComment", "component/sysComment.html");
            table.set("sysUser", "component/sysUser.html");
            table.set("sysSite", "component/sysSite.html");
            if (pageId == "mainPage") {
                window.location.href = ""
            } else if (table.get(pageId) != undefined) {
                oppenLoading();
                content.on("load", () => { closeLoading() });
                refreshSubPage(table.get(pageId), elem); 
            }
        },
        // accordion: true,
        data: [{
            title: "首页",
            id: "mainPage"
        }, {
            title: "博客管理",
            id: "sysBlog",
            children: [{
                title: "编辑博客",
                id: "sysBlogAdd"
            }, {
                title: "博客列表",
                id: "sysBlogList"
            }]
        }, {
            title: "标签与分类",
            id: "sysCategory"
        }, {
            title: "账号管理",
            id: "sysUser"
        }, {
            title: "网站管理",
            id: "sysSite"
        }, {
            title: "评论管理",
            id: "sysComment"
        }]
    })
})

function refreshSubPage(url, elem){
    let content = $("#content");
    let colorClass = "whiteFont";
    content.attr("src", url);
    if(publicElem != undefined) $(publicElem).removeClass(colorClass);
    $(elem).addClass(colorClass);
    publicElem = elem;
}

function oppenLoading(){
    $("#categoryLoadingIcon").css("display", "flex")
    $(content).css("display", "none");
}

function closeLoading(){
    $("#content").css("display", "block");
    $("#categoryLoadingIcon").css("display", "none")
}