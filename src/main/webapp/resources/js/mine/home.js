$(function () {
   var identity =  sessionStorage.getItem("identity");
    if (identity == 0){
        $("#classAdminTag").html("班级信息");
    }
    $("#mineInfo").click(function () {
       window.location = "/page/toMineInfo";
    });
    $("#classAdmin").click(function () {
        window.location = "/page/toClassAdmin";
    });
    $("#aboutUs").click(function () {
        window.location = "/page/toAboutUs";
    });
});