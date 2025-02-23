var pageIndex = 1;
var pageSize = 5;
var pageMax = 0;

var isNeedSubmit = 0;
$(function () {
    $("#startTime").datetimePicker();
    $("#endTime").datetimePicker();
    $("#backButton").click(function () {
       window.location = "/page/toExamHome";
    });
    $("#submitButton").click(function () {
       var examName  = $("#examName").val();
       var subject  = $("#subject").val();
       var startTime  = $("#startTime").val();
       var endTime  = $("#endTime").val();
       var compareDate1 = new Date(startTime);
       var compareDate2 = new Date(endTime);
       if (compareDate1 > compareDate2){
           $.toast("开始时间大于结束时间","forbidden");
           return;
       }
       if ($.trim(examName) == ""){
           $.toast("考试名称必填","forbidden");
           return;
       }
        if ($.trim(subject) == ""){
            $.toast("科目必填","forbidden");
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
        if (startTime.length < 19){
            startTime += ":00";
        }
        if (endTime.length < 19){
            endTime += ":00";
        }
        var a = {};
        var b = {};
        a.token = sessionStorage.getItem("token");
        b.examName = examName;
        b.subject = subject;
        b.startTime = startTime;
        b.endTime = endTime;
        a.data = b;
        $.ajax({
            url : "/api/addExamByTeacher",
            data : JSON.stringify(a),
            dataType :"json",
            contentType : "application/json",
            success : function (data) {
                if (data.statusCode == 200){
                    $.toast("发布成功");
                    window.location = "/page/toExamHome";
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
