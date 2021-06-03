function openMenu(target){

}
$(() => {
    $(".categoryItem").bind("contextmenu", function(e){
        if (e.which == 3) {
            let curElem = $(this);
            let x = e.pageX;
            let y = e.pageY;
            let id = curElem.attr("xId");
            let type = curElem.attr("xType");
            let menu = $("#menu");
            menu.css({"top": `${y}px`,"left": `${x}px`}).show();
            let dom = "";
            // 如果是mark，则渲染'修改'、'删除'
            switch (type){
                case 'mark': 
                    dom += `
                        <li onclick="doAction('${id}', 'edit', 'mark')">修改</li>
                        <li onclick="doAction('${id}', 'delete', 'mark')">删除</li>
                        `;    
                    break;
                // 如果是cate，则渲染'修改'、'删除'、'详细'
                case 'cate': 
                    dom += `
                        <li onclick="doAction('${id}', 'edit', 'cate')">修改</li>
                        <li onclick="doAction('${id}', 'delete', 'cate')">删除</li>
                        <li onclick="doAction('${id}', 'detail', 'cate')">详情</li>
                    `;    
                    break;
                case 'topic': 
                    dom += `
                        <li onclick="doAction('${id}', 'edit', 'topic')">修改</li>
                        <li onclick="doAction('${id}', 'delete', 'topic')">删除</li>
                        <li onclick="doAction('${id}', 'detail', 'topic')">详情</li>
                        <li onclick="doAction('${id}', 'move', 'topic')">移动</li>
                    `;    
                    break;
            }
            menu.empty().append(dom);
            return false;
        }
        menu.hide();
        return false;
    })
    
    $(document).bind("click", function(e){
        let menu = $("#menu");
        menu.hide();
    })
})

var cateInfo = {
    "1001": {
        "value": "技术",
        "topicNum": 4,
        "parentId": "",
        "createTime": "2020-03-11",
        "topics": ["2001", "2002", "2003", "2004", "2005"]
    }
}
var topicInfo = {
    "2001": { 'id': "2001", 'parentId': '1001', "value": "Java学习", "articleNum": 102, 'createTime': '2020-03-24', 'visitNum':1100 },
    "2002": { 'id': "2002", 'parentId': '1001', "value": "Spring开发", "articleNum": 102, 'createTime': '2020-03-24', 'visitNum':1100 },
    "2003": { 'id': "2003", 'parentId': '1001', "value": "数据库", "articleNum": 102, 'createTime': '2020-03-24', 'visitNum':1100 },
    "2004": { 'id': "2004", 'parentId': '1001', "value": "计算机网络", "articleNum": 102, 'createTime': '2020-03-24', 'visitNum':1100 },
    "2005": { 'id': "2005", 'parentId': '1001', "value": "算法刷题", "articleNum": 102, 'createTime': '2020-03-24', 'visitNum':1100 },
}

var markInfo = {
    "1001": { "id": "1001", "value": "java", "articleNum": 65, "createTime": "2021-03-25"},
    "1002": { "id": "1001", "value": "spring", "articleNum": 65, "createTime": "2021-03-25"},
    "1003": { "id": "1001", "value": "前端", "articleNum": 65, "createTime": "2021-03-25"},
    "1004": { "id": "1001", "value": "项目", "articleNum": 65, "createTime": "2021-03-25"}
}

function doAction(id, actionType, callType){
    let inputTitle = (callType == 'cate')? "分区": (callType == 'topic')? "专题": "标签";
    let info = (callType == 'cate')? cateInfo: (callType == 'topic')? topicInfo: markInfo;
    let value = info[id]['value'];
    let infoBox = $("#infoBox");
    let wrap = $("#insertContent");
    let btnList = $("#btnList");
    infoBox.show();

    let dom = ""
    let btnDom = ``
    if (actionType == 'edit') {
        dom += `
            <div class="display:flex;">
                <span class="x-input-title">${inputTitle}名</span>
                <input type="text" class="x-input-line" value="${value}" name="newName">
            </div>`;
        btnDom = `
            <span class="x-btn" style="margin-right: 10px;">修改</span>
            <span class="x-btn" onclick='cancelBtn()'>取消</span>
        `;
    } else if (actionType == 'delete') {
        if (callType == 'mark') {
            dom += `
                <p style="line-height: 1.7; text-align: center;">是否删除标签 #${value}？</p>
            `;
            btnDom = `
                <span class="x-btn" style="margin-right: 10px;">删除</span>
                <span class="x-btn" onclick='cancelBtn()'>取消</span>`
        } else {
            dom += `
                <p style="line-height: 1.7; text-align: center;">是否将改${inputTitle}下所有文章移入草稿箱？</p>
            `;
            btnDom = `
                <span class="x-btn" style="margin-right: 10px;">移入草稿箱</span>
                <span class="x-btn" style="margin-right: 10px;">直接删除</span>
                <span class="x-btn" onclick='cancelBtn()'>取消</span>`
        }
    } else if (actionType == 'detail') {
        dom += `
            
        `;
        btnDom = `
            <span class="x-btn" onclick='cancelBtn()'>关闭</span>`
    } else if (actionType == 'move') {
        dom += `
            
        `;
        btnDom = `
            <span class="x-btn" style="margin-right: 10px;">确认</span>
            <span class="x-btn" onclick='cancelBtn()'>取消</span>`
    }
    wrap.html(dom);
    btnList.html(btnDom);
}

function modifyValue(id, type, value){
    // cate、topic调用一个接口（因为是一张表），mark调用一个接口 

    // 调用结束后根据返回值关闭窗口并弹出消息提示
}

function remove(id, type){
    // cate、topic调用一个接口（因为是一张表），mark调用一个接口 

    // 调用结束后根据返回值关闭窗口并弹出消息提示
}

function move(id, newParentId){
    // 只有topic能移动
}

function cancelBtn(){
    $("#infoBox").hide();
}