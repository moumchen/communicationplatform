
$(function () {

    $("#backButton").click(function () {
        window.location = "/page/toLogin";
    });
    $("#createButton").click(function () {
       var schoolName = $("#schoolName").val();
       var className = $("#className").val();
       if (schoolName == null || schoolName == ""){
           $.toast("请输入学校名称","forbidden");
           return;
       }
        if (className == null || className == ""){
            $.toast("请输入班级名称","forbidden");
            return;
        }
       if (sessionStorage.getItem("class_limit_token") == null || sessionStorage.getItem("class_limit_token") == ""){
           window.location = "/page/toLogin";
       }
       var a = {};
       var b = {};
       a.token = sessionStorage.getItem("class_limit_token");
       b.className = className;
       b.schoolName = schoolName;
       a.data = b;
       $.ajax({
           url : "/api/addNewClassByTeacher",
           type : "post",
           contentType : "application/json",
           dataType :"json",
           data : JSON.stringify(a),
           success: function (data) {
               if (data.statusCode == 200){
                   sessionStorage.setItem("inviteCode",data.data.inviteCode);
                   sessionStorage.setItem("token",sessionStorage.getItem("class_limit_token"));
                   window.location = "/page/toCreateSuccess";
               } else {
                   $.toast("失败!"+data.message,"text");
               }
           }
       });
   });

});