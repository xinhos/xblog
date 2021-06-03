var simplemde = undefined;

function initSimpleMDE(editorId){
    if (simplemde == undefined) {
        let wrap = $("#"+editorId);
        wrap.append(`
            <div class="body">
                <div class="mkTitleWrap" class="">
                    <input type="text" id="mkTitle" placeholder="请输入标题" class="mkTitle">
                </div>
                <div class="editorWrap">
                    <textarea name="" id="editor" cols="30" rows="10"></textarea>
                </div>
            </div>
        `);
        simplemde = new SimpleMDE({ 
            element: $("#editor")[0],
            autoDownloadFontAwesome: false,
            autosave: {
                enabled: true,
                uniqueId: "uniqueid",
                delay: 100,
            },
            previewRender: function(plainText) {
                $(".editor-preview").addClass("mkWrap");
                return renderMarkDown(plainText);
            },
            spellChecker: false,
        });
    }
}

function openSidePreview(){
    $(".editor-preview-side").addClass("mkWrap");
}
function openPreview(){
    $(".editor-preview").addClass("mkWrap");
}
function renderMarkDown(plainText){
    let rendererMD = new marked.Renderer();
        marked.setOptions({
            renderer: rendererMD,
            gfm: true,
            tables: true,
            breaks: true,
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
        return marked(plainText);
}
function getMkContent(){
    return simplemde.value();
}