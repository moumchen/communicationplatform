$(function () {

    $("#backButton").click(function () {
        window.location = "/page/toLogin";
    });
    $("#password2").focusout(function () {
        var password = $("#password").val();
        var password2 = $("#password2").val();
        if (password != password2){
            $.toast("两次密码输入不一致！","text");
        }
    });
    $("#email").focusout(function () {
        if (!$("#email").val().match(/^[\w\-\.]+@[\w\-\.]+(\.\w+)+$/)){
            $.toast("邮箱格式不正确！","text");
        }
    });
    $("#phone").focusout(function () {
        if (!$("#phone").val().match(/^(((13[0-9]{1})|(15[0-9]{1})|177|149)+\d{8})$/)){
            $.toast("手机号格式不正确！","text");
        }
    });


    $("#registerButton").click(function () {
        var REGISTER_URL = "/api/register";
        var password = $("#password").val();
        var password2 = $("#password2").val();
        if (password != password2){
            $.toast("两次密码输入不一致！","text");
            return;
        }
        if (!$("#email").val().match(/^[\w\-\.]+@[\w\-\.]+(\.\w+)+$/)){
            $.toast("邮箱格式不正确！","text");
            return;
        }
        if (!$("#phone").val().match(/^(((13[0-9]{1})|(15[0-9]{1})|177|149)+\d{8})$/)){
            $.toast("手机号格式不正确！","text");
            return;
        }
        var username = $("#username").val();
        var identity = 0;
        if ($("#x12").is(":checked")){
            identity = 1;
        }
        var nickname = $("#nickname").val();
        var name = $("#name").val();
        var phone = $("#phone").val();
        var email = $("#email").val();


        var param = {};
        var user = {};
        var auth = {};
        auth.username = username;
        auth.password = password;
        auth.source = 0;
        user.nickname = nickname;
        user.phone = phone;
        user.identity = identity;
        user.email = email;
        user.name = name;
        param.user = user;
        param.auth = auth;
        $.ajax({
            url: REGISTER_URL,
            type: "post",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.statusCode == 200) {
                    $.toast("注册成功！");
                    window.location = "/page/toLogin";
                } else {
                    $.toast("错误！" + data.message, "forbidden");
                }
            }

        });

    });
});