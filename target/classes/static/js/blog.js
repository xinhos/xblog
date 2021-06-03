function toggleS2() {
    let s2 = $("#s2");
    let s2Flag = $("#s2Flag");
    let blogWrap = $("#contentWrap")
    let wrapWidth = blogWrap.width();
    let targetWidth = 340;
    if (s2Flag.val() == 'close') {
        s2.css({"margin-left": "0px"});
        blogWrap.width(wrapWidth);
        s2Flag.val('open');
    } else {
        s2.width(0);
        s2.css({"margin-left": -targetWidth});
        s2Flag.val('close');
        setTimeout(() => {
            $("#contentWrap").css("width", "100%");
            switchContentState(true, false, false, "");
        }, 250);
    }
}

function selectShortBlogList(topicId, topicTitle) {
    $("#curTopic").html(topicTitle);

    $.ajax({
        url: `/category/${topicId}/page/1/t`,
        type: "GET",
        dataType: "json",
        success: (data) => {
            if (data['code'] == 'GET_DATA_OK') {
                renderShortBlogList(data);
                $("#curTopic").val(topicId);
            }
        }
    })
}

function renderShortBlogList(data) {
    let shortBlogWrap = $("#shortBlogWrap");
    let blogs = data['shortBlogList']
    shortBlogWrap.empty();

    if (blogs.length == 0) {
        shortBlogWrap.append(`
            <p class="emptyLine"><i class="line"></i><span>end</span><i class="line"></i></p>
        `);
        return;
    }

    for (let i = 0; i < blogs.length; i++) {
        let title = blogs[i]['title'];
        let date = blogs[i]['publishDate'];
        let labelHtml = "";
        let labelArray = blogs[i]['marks'];
        for (let j = 0; j < labelArray.length; j++) {
            labelHtml += `<li>#${labelArray[j]}</li>`;
        }

        let pattern = `
            <li class="shortBlog">
                <div class="shortBlogTitleWrap">
                    <span class="fa fa-quote-left shortBlogTitleIcon"></span>
                    <span class="shortBlogTitle">${title}</span>
                </div>
                <div class="shortBlogBodyWrap">
                    <span class="shortBlogData">
                        <span class="fa fa-calendar"></span>
                        <span class="date">${date}</span>
                    </span>
                    <ul class="shortBlogMarkWrap">
                        <li><i class="fa fa-tags"></i></li>${labelHtml}
                    </ul>
                </div>
            </li>`

        shortBlogWrap.append(pattern);
    }

    let isMoreBlog = data['pageInfo']['curPage'] < data['pageInfo']['pageNum'];
    if (isMoreBlog) {
        shortBlogWrap.append(`
            <div class="getMoreBtn" onclick="getMore()">点击加载更多</div>
        `)
    }
}

function getMore(selectCondition) {
    
}

function toggleCategory(categoryId, title) {
    let s2Flag = $("#s2Flag");
    let categoryFlag = $("#curCategoryID");
    // 关闭——>打开，打开（点击同一个category两次）——>关闭
    if (s2Flag.val() == 'close' || categoryFlag.val() == categoryId) {
        toggleS2();
        // 点开上一次打开的目录，无需请求后台，直接打开即可
        if (s2Flag.val() == 'open' && categoryFlag.val() == categoryId) {
            let lastPage = "#" + $("#lastPage").val();
            $(lastPage).css("display", "flex");
            $("#categoryLoadingIcon").css("display", "none")
            return;
        }
    }
    // 必须发起请求
    if (s2Flag.val() != 'close' && categoryFlag.val() != categoryId) {
        $.ajax({
            url: `category/${categoryId}/topic`,
            type: 'GET',
            dataType: 'json',
            success: (data) => {
                $("#categoryLoadingIcon").css("display", "none");
                switch (data['code']) {
                    case "GET_DATA_ERROR": {
                        let content = $("#categoryAlertMsg");
                        content.html(data['info']);
                        switchContentState(false, true, false, data['categoryId']);
                        break;
                    }
                    case "GET_DATA_OK": {
                        openCatagory(data, categoryId, title);
                    }
                }
            },
            error: (data) => {  }
        })
    } 
}

function openCatagory(data, categoryId, categoryTitle) {
    $("#categoryTitle").html(categoryTitle);
    $("#curTopic").empty();

    let topicWrap = $("#topicWrap");
    topicWrap.empty();
    for (let i = 0; i < data['topicList'].length; i++) {
        let topicId = data['topicList'][i]['id'];
        let topicTitle = data['topicList'][i]['name'];
        let articleNum = data['topicList'][i]['articleNum'];
        let pattern = `
            <li class="topicInfo" onclick="selectShortBlogList(${topicId}, '${topicTitle}')">
                <span class="topicTitle">${topicTitle}</span>
                <span class="topicArticleNum">${articleNum}</span>
            </li>`
        topicWrap.append(pattern);
    }

    renderShortBlogList(data)
    switchContentState(false, false, true, categoryId);
}

function switchContentState(loadingState, msgState, contentState, curCategoryId) {
    // 重置s2子版块的状态
    let loading = (loadingState)? "flex": "none";
    let msg = (msgState)? "flex": "none";
    let content = (contentState)? "flex": "none";
    let lastPageId = msgState? "categoryAlertMsgWrap":
                        contentState? "categoryContent": "";
    $("#categoryLoadingIcon").css("display", loading);
    $("#categoryAlertMsgWrap").css("display", msg);
    $("#categoryContent").css("display", content);
    // 置空ID标记
    if (curCategoryId != "") {
        let categoryFlag = $("#curCategoryID");
        categoryFlag.val(curCategoryId);
    }
    if (lastPageId != "") $("#lastPage").val(lastPageId);

    // 点击笔记名后上色
    $("#notebookList li span").removeClass("curNotebook");
    if (curCategoryId != "") {
        $(`#notebook_${curCategoryId}`).addClass("curNotebook");
    }
}

function toggleTopic() {
    let switchFlag = $("#switchTopic");
    let topicWrap = $("#topicWrap");
    if (switchFlag.val() == 1) {
        topicWrap.slideUp();
        switchFlag.val(0);
    } else {
        topicWrap.slideDown();
        switchFlag.val(1);
    }
}

function showloadingIcon() {
    let shortBlogWrap = $("#shortBlogWrap");
    shortBlogWrap.html(`
        <div class="spinner">
            <div class="rect1"></div>
            <div class="rect2"></div>
            <div class="rect3"></div>
            <div class="rect4"></div>
            <div class="rect5"></div>
        </div>
    `);
}

function search() {
    let keyWord = $("#searchInput").val().trim();
    if (keyWord.length <= 0) return;
    showloadingIcon();
    $.ajax({
        url: "mock/searchResult.json",
        type: "GET",
        dataType: "json",
        success: (data) => {
            if (data['code'] == 'GET_DATA_OK') {
                renderShortBlogList(data);
            }
        }
    })
}

function refreshCatagory(rank) {
    let catagoryId = $("#curTopic").val();
    let type = 't';
    if (rank == "catagory") {
        $("#curTopic").empty();
        catagoryId = $("#curCategoryID").val();
        type = 'c';
    }

    showloadingIcon();

    $.ajax({
        url: `/category/${catagoryId}/page/1/${type}`,
        type: "GET",
        dataType: "json",
        success: (data) => {
            if (data['shortBlogList'].length > 0) {
                renderShortBlogList(data)
            } 
        }
    })
}

$(function() {
    let rendererMD = new marked.Renderer();
    marked.setOptions({
        renderer: rendererMD,
        gfm: true,
        tables: true,
        breaks: false,
        pedantic: false,
        sanitize: false,
        smartLists: true,
        smartypants: false
    });

    hljs.initHighlightingOnLoad();

    marked.setOptions({
        highlight: function (code) {
            return hljs.highlightAuto(code).value;
        }
    })

    let blogBodys = $("article.blogBody");
    for (let i = 0; i < blogBodys.length; i++) {
        let curBlogDOM = blogBodys[i];
        let articleHTML = marked(curBlogDOM.innerText.replace(/\\n/g, '\n'));
        $(curBlogDOM).html(articleHTML);
    }
    // $("#notebook_1001").click();
    $("#doSearchIcon").mouseover(()=>{
        $("#searchIconSVG").attr("fill", "#2196F3")
    })
    $("#doSearchIcon").mouseout(()=>{
        $("#searchIconSVG").attr("fill", "#777777")
    })
})