$(function () {
    var admin_token = sessionStorage.getItem("admin_token");
    if (admin_token == null || admin_token == "null"){
        window.location = "/page/toAdminLogin";
    }
    var getUserReq = {};
    getUserReq.token = admin_token;
    $.ajax({
        url : "/api/getUserInfo",
        type: "post",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(getUserReq),
        success: function (T) {
            if (T.statusCode == 200) {
                if (T.data.identity != 2){
                    $("#adminUserNameTip").html("您不是管理员身份，请输入正确的管理员账号密码登录！");
                    return;
                }
                sessionStorage.setItem("adminId",T.data.keyId);
                sessionStorage.setItem("adminName",T.data.name);
                $("#adminUserNameTip").html(T.data.name);
            } else {
                if (T.message == "未登录"){
                    window.location = "/page/toAdminLogin";
                }
            }
        }
    });

    $("#logout").click(function () {
        sessionStorage.setItem("admin_token",null);
        window.location.reload();
    });

})