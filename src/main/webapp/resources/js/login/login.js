
$(function () {
    if (sessionStorage.errorTime == null){
       sessionStorage.errorTime = 0;
    }

    if (sessionStorage.errorTime <= 11){
       $(".checkCodeDiv").css("display","none");
    }
    if (sessionStorage.errorTime > 11){
        $(".checkCodeDiv").css("display","block");
        $(".weui-cell__hd").css("float","left");
        $("#checkCode").css("width","200px");
    }
    //注册
    $("#registerButton").click(function () {
        window.location = "/page/toRegister";
    });
    // 登录
    $("#loginButton").click(function () {
        var LOGIN_URL = "/api/login";
        var username = $("#username").val();
        var password = $("#password").val();
        var checkCode = "";
        if (sessionStorage.errorTime >= 3){
           checkCode = $("#checkCode").val();
        }
        $.ajaxSettings.async = false;

        $.post(
            LOGIN_URL,
            {"username":username,"password":password,"errorTimes":sessionStorage.errorTime,"checkCode":checkCode} ,
            function (data) {
                if(data.statusCode == 200){
                    $.toast(data.message);
                     // 判断用户是否为第一次登陆，根据教师用户class信息和学生用户class信息判断
                    var req = {};
                    req.token = data.data;
                    $.ajax({
                        url: "/api/getUserInfo",
                        dataType: "json",
                        type: "post",
                        contentType: "application/json",
                        data: JSON.stringify(req),
                        success: function (data2) {
                            if (data2.statusCode == 200) {
                                // 先存储用户必要信息
                                sessionStorage.setItem("nickname",data2.data.nickname);
                                sessionStorage.setItem("identity",data2.data.identity);
                                sessionStorage.setItem("name",data2.data.name);

                                if (data2.data.classId == null || data2.data.classId == "") {
                                    // 第一次登陆
                                    sessionStorage.setItem("class_limit_token", data.data);
                                    if (data2.data.identity == 0) {
                                        window.location = "/page/toJoinClass";
                                    } else {
                                        window.location = "/page/toCreateClass";
                                    }
                                } else {
                                    sessionStorage.setItem("token", data.data);
                                    window.location = "/page/toIndex";
                                }
                            }
                        }
                    });

                } else {
                    sessionStorage.setItem("errorTime",sessionStorage.getItem("errorTime")+1);
                   $.toast(data.message,"forbidden");
                }
            },
            "json"
        );

        if (sessionStorage.errorTime == 111){
            $(".checkCodeDiv").css("display","block");
            // $(".checkCodeDiv").css("display","");
        }
        $("#codeImg").attr("src", "/api/getVerificationCode?"+Math.random());
    });

});
