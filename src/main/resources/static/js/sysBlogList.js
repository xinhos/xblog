// =========================================================================================
var categoryInfo = [
    {"id": "1001", "value": "技术"},
    {"id": "1002", "value": "生活"},
    {"id": "1003", "value": "分享"}
]
var topicInfo = {
    "1001": [
        {"id": "2001", "value": "Java学习"},
        {"id": "2002", "value": "项目记录"},
        {"id": "2003", "value": "刷题学习"}
    ],
    "1002": [
        {"id": "2011", "value": "读书笔记"},
        {"id": "2012", "value": "随想札记"},
        {"id": "2013", "value": "晨间笔记"}
    ],
    "1003": [],
}
function renderBlog(data){
    let markListDom = "";
    let markList = data['mark'];
    for (let i = 0; i < markList.length; i++) {
        markListDom += `
            <div class="blogMark">
                <input type="hidden" value="${markList[i]['id']}">
                #${markList[i]['value']}
                <i onclick=deleteMark(${markList[i]['id']}) class="blogMarkClose">×</i>
            </div>`;
    }

    let blogCategoryId = data['categoryId'];
    let blogTopicId = data['topicId'];
    let categoryOptionDom = "";
    let topicOptionDom = "";
    let categoryStr = "未发布";
    let topicStr = "未发布";
    for (let i = 0; i < categoryInfo.length; i++) {
        let categoryId = categoryInfo[i]['id'];
        let categoryValue = categoryInfo[i]['value'];
        let selected = "";
        if (categoryInfo[i]['id'] == blogCategoryId){
            selected = "selected";
            categoryStr = categoryValue;
        } 
        categoryOptionDom += `
            <option ${selected} onclick=changeTopicOptions(${categoryId}) value="${categoryId}">
                ${categoryValue}
            </option>\n`;
    }
    if (blogCategoryId == "") {
        categoryOptionDom += `<option selected>未发布</option>\n`;
        topicOptionDom += `<option selected>未发布</option>\n`;
    } else {
        let topicList = topicInfo[blogCategoryId];
        for (let i = 0; i < topicList.length; i++) {
            let topicId = topicList[i]['id'];
            let topicValue = topicList[i]['value'];
            let selected = "";
            if (topicId == blogTopicId) {
                selected = "selected"
                topicStr = topicValue;
            }
            topicOptionDom += `<option ${selected} value="${topicId}">${topicValue}</option>`;
        }
    }

    let row = `
        <tr class="tableRow">
            <td class="moreIconWrap">
                <span onclick="toggleBlogInfo(this, ${data['id']})" class="fa fa-angle-right moreIcon"></span>
            </td>
            <td>${data['title']}</td><td>${data['createTime']}</td><td>${categoryStr}</td><td>Java</td><td>${categoryStr}</td>
            <td>-</td>
        </tr>
        <tr class="blogInfoTR">
            <td colspan="100">
                <div class="blogInfoWrap" style="display: none;">
                    <ul class="blogInfoContent">
                        <li><span class="blogInfoTitle">标题</span><input type="text" value="${data['title']}" class="blogTitle blogInfoValue"></li>
                        <li><span class="blogInfoTitle">标签</span><div class="blogInfoMarkList">${markListDom}
                            <div class="addMark"><i class="addIcon" onclick="showAddBox()">+</i></div></div>
                        </li>
                        <li><span class="blogInfoTitle">分栏</span><select class="blogInfoSelect">${categoryOptionDom}</select></li>
                        <li><span class="blogInfoTitle">专栏</span><select class="blogInfoSelect">${topicOptionDom}</select></li>
                        <li><span class="blogInfoTitle">发布日期</span><input type="text" value="${data['createTime']}" readonly class="blogInfoText"></li>
                        <li><span class="blogInfoTitle">修改日期</span><input type="text" value="${data['updateTime']}" readonly class="blogInfoText"></li>
                        <li><span class="blogInfoTitle">浏览次数</span><input type="text" value="${data['visited']}" readonly class="blogInfoText"></li>
                        <li><span class="blogInfoTitle">评论数</span><input type="text" value="${data['comment']}" readonly class="blogInfoText"></li>
                        <li style="align-items: flex-start;"><span class="blogInfoTitle">封面</span><img src="${data['headImgUrl']}" alt="" class="blogHeadImg"></li>
                        <li style="align-items: flex-start;"><span class="blogInfoTitle">摘要</span><textarea class="blogAbstruct xScrollBar">${data['abstract']}</textarea></li>
                    </ul>
                    <span class="saveBlogBtn" onclick="updataBlog()">保存</span>
                </div>
            </td>
        </tr>
    `;
    return row;
}
// ========================================================================================

$(function(){
    $("#doSearchIcon").mouseover(()=>{
        $("#searchIconSVG").attr("fill", "#2196F3")
    })
    $("#doSearchIcon").mouseout(()=>{
        $("#searchIconSVG").attr("fill", "#777777")
    })
})