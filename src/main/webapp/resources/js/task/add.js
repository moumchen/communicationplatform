var pageIndex = 1;
var pageSize = 5;
var pageMax = 0;

var isNeedSubmit = 0;
$(function () {
    $("#startTime").datetimePicker();
    $("#endTime").datetimePicker();
    $("#backButton").click(function () {
       window.location = "/page/toTaskHome";
    });
    $("#submitButton").click(function () {
       var taskname  = $("#taskname").val();
       var content  = $("#content").val();
       var remark  = $("#remark").val();
       var startTime  = $("#startTime").val();
       var endTime  = $("#endTime").val();
       var compareDate1 = new Date(startTime);
       var compareDate2 = new Date(endTime);
       if (compareDate1 > compareDate2){
           $.toast("开始时间大于结束时间","forbidden");
           return;
       }
       if ($.trim(taskname) == ""){
           $.toast("作业名称必填","forbidden");
           return;
       }
        if ($.trim(content) == ""){
            $.toast("内容必填","forbidden");
            return;
        }
        if ($.trim(startTime) == ""){
            $.toast("开始时间必填","forbidden");
            return;
        }
        if ($.trim(endTime) == ""){
            $.toast("结束时间必填","forbidden");
            return;
        }
        if ($.trim(remark) == ""){
            remark = "无备注";
        }
        var a = {};
        var b = {};
        a.token = sessionStorage.getItem("token");
        b.taskName = taskname;
        b.content = content;
        b.startTime = startTime;
        b.remark = remark;
        b.endTime = endTime;
        b.isNeedSubmit = isNeedSubmit;
        b.submitCount = 0;
        a.data = b;
        $.ajax({
            url : "/api/addTaskByTeacher",
            data : JSON.stringify(a),
            dataType :"json",
            contentType : "application/json",
            success : function (data) {
                if (data.statusCode == 200){
                    $.toast("发布成功");
                    window.location = "/page/toTaskAddSuccess";
                } else {
                    $.toast(data.message,"forbidden");
                    if(data.message == "未登录"){
                        window.location = "/page/toLogin";
                    }
                }
            },
            type : "post"
        });
    });

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

function setChecked(obj) {
    if ($(obj).html() == "x11"){
        isNeedSubmit = 0;
    } else {
        isNeedSubmit = 1;
    }
}