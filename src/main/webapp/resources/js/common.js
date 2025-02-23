$(function () {
    var getUserReq = {};
    getUserReq.token = sessionStorage.getItem("token");
    $.ajax({
        url : "/api/getUserInfo",
        type: "post",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(getUserReq),
        success: function (T) {
            if (T.statusCode == 200) {
                sessionStorage.setItem("userId",T.data.keyId);
                sessionStorage.setItem("classId",T.data.classId);
                sessionStorage.setItem("headImg",T.data.headImg);
                if (T.data.classId == ""){
                    $.toast("用户信息有变更，请重新登录!","forbidden");
                    window.location = "/page/toLogin";
                }
            } else {
                if (T.message == "未登录"){
                    window.location = "/page/toLogin";
                }
            }
        }
    });



    if (sessionStorage.token == null){
        window.location = "/page/toLogin";
    }

    $("#indexButton").click(function () {
        window.location = "/page/toIndex";
    });
    $("#taskButton").click(function () {
        window.location = "/page/toTaskHome";
    });
    $("#examButton").click(function () {
        window.location = "/page/toExamHome";
    });
    $("#applicationButton").click(function () {
        window.location = "/page/toApplicationHome";
    });
    $("#chatButton").click(function () {
        window.location = "/page/toChatHome";
    });
    $("#mineButton").click(function () {
        window.location = "/page/toMineHome";
    });

})