var pageIndex = 1;
var pageSize = 5;
var pageMax = 0;
$(function () {
    // 教师用户显示新增按钮
    var identity = sessionStorage.getItem("identity");
    if (identity == "1"){
        $(".addButton").css("display","block");
    }

    $(".addButton").click(function () {
        window.location = "/page/toAddTask";
    });
    var a = {};
    a.token = sessionStorage.getItem("token");
    a.pageIndex = pageIndex;
    a.pageSize = pageSize;
    // 根据当前用户id获取作业信息
    $.ajax({
       url : "/api/getTasks",
        dataType :"json",
        contentType : "application/json",
        data : JSON.stringify(a),
        success : function (data) {
            if (data.statusCode == 200){
                var list = data.data.list.length;
                pageMax = Math.ceil(data.data.totalCount / pageSize);
                if (Object.keys(data.data.list).length > 0){
                    addItemToList(data.data.list);
                } else {
                    $(".noneTasks").css("display","block");
                }
            } else {
                $.toast(data.message,"forbidden");
                if (data.message == "未登录"){
                    window.location = "/page/toLogin";
                }}
        },
        type : "post"
    });


// <div class="weui-media-box weui-media-box_text content_item">
//         <h4 class="weui-media-box__title">标题一</h4>
//         <p class="weui-media-box__desc">由各种物质组成的巨型球状天体，叫做星球。星球有一定的形状，有自己的运行轨道。</p>
//     <ul class="weui-media-box__info">
//         <li class="weui-media-box__info__meta">文字来源</li>
//         <li class="weui-media-box__info__meta">时间</li>
//         <li class="weui-media-box__info__meta weui-media-box__info__meta_extra">其它信息</li>
//         </ul>
//         </div>

});

function addItemToList(data) {
    var str = $(".content").html();
    var date = new Date();
    for (var i=0; i<data.length; i++){
        var tempDate = new Date(data[i].endTime);
        str += "<div class=\"aui-card-list content_item\" submitCount=\""+data[i].submitCount+"\" taskId=\""+data[i].keyId+"\" isNeedSubmit=\""+data[i].isNeedSubmit+"\" startTime=\""+data[i].startTime+"\" remark=\""+data[i].remark+"\" endTime=\""+data[i].endTime+"\" addTime=\""+data[i].addTime+"\" taskName=\""+data[i].taskName+"\" content=\""+data[i].content+"\" style='width:95%;cursor:pointer'>";
        str += "  <div class=\"aui-card-list-header\">"+data[i].taskName+"</div>";
        str += " <div class=\"aui-card-list-content-padded\">";
        str += ""+data[i].content+"</div>";
        str += "<div class=\"aui-card-list-footer\">";

        if (tempDate < date){
            str += "<div class=\"aui-label aui-label-danger\">已完结</div>";
        } else {
            str += "<div class=\"aui-label aui-label-info\">进行中</div>"
        }
        if (data[i].isNeedSubmit == 1){
            str += "<div class=\"aui-label aui-label-success\">线上作业</div>";
        } else{
            str += "<div class=\"aui-label aui-label-warning\">线下作业</div>";
        }
        str += "<br/><font>开始时间:"+data[i].startTime.substring(0,19)+"<br/> 结束时间:"+data[i].endTime.substring(0,19)+"</font>";
        str += "</div></div>";

    }
    $(".content").html(str);


    $(".content_item").click(function () {
        sessionStorage.setItem("taskName",$(this).attr("taskName"));
        sessionStorage.setItem("taskId",$(this).attr("taskId"));
        sessionStorage.setItem("content",$(this).attr("content"));
        sessionStorage.setItem("addTime",$(this).attr("addTime"));
        sessionStorage.setItem("endTime",$(this).attr("endTime"));
        sessionStorage.setItem("remark",$(this).attr("remark"));
        sessionStorage.setItem("startTime",$(this).attr("startTime"));
        sessionStorage.setItem("submitCount",$(this).attr("submitCount"));
        sessionStorage.setItem("isNeedSubmit",$(this).attr("isNeedSubmit"));
        window.location = "/page/toTaskDetail";
    });

}