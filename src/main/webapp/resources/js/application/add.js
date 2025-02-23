$(function () {

    // 获取当前用户的教师

    $.ajax({
        url : "/api/getClassMemberBySelf",
        dataType : "json",
        contentType : "application/json",
        data : sessionStorage.getItem("token"),
        type : "post",
        success : function (T) {
            if (T.statusCode == 200){
                // 获取成功
                for (var i = 0 ; i < T.data.length; i++){
                    if (T.data[i].identity == 1){
                        // 教师用户
                        $("#teacherID").val(T.data[i].name);
                        return;
                    }
                }
            } else {
                $.toast(T.message, "forbidden");
                if (T.message == "未登录"){
                    window.location = "/page/toLogin";
                }
            }
        }

    });

    $("#backButton").click(function () {
        window.location = "/page/toApplicationHome";
    });

    $("#submitButton").click(function () {
        var title = $("#title").val();
        var content = $("#content").val();
        if ($.trim(title) == ""){
            $.toast("标题必填","forbidden");
            return;
        }
        if ($.trim(content) == ""){
            $.toast("内容必填","forbidden");
            return;
        }
        title = "【"+sessionStorage.getItem("name")+"提交的审批】" + title;
        var req = {};
        req.token = sessionStorage.getItem("token");
        var data = {};
        data.title = title;
        data.content = content;
        req.data = data;
        $.ajax({
            url : "/api/insertApplcationByStudent",
            dataType : "json",
            contentType : "application/json",
            data : JSON.stringify(req),
            type : "post",
            success : function (T) {
                if (T.statusCode == 200){
                   $.toast("提交成功");
                   window.location = "/page/toApplicationHome";
                } else {
                    $.toast(T.message, "forbidden");
                    if (T.message == "未登录"){
                        window.location = "/page/toLogin";
                    }
                }
            }
        });
    });

});