
$(function () {

    $("#backButton").click(function () {
        window.location = "/page/toIndex";
    });
   $("#content").html("班级创建成功!您的班级邀请码是<font style=\"color:red\">"+sessionStorage.getItem("inviteCode")+"</font>");
});