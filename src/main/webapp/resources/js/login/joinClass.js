
$(function () {

    $("#backButton").click(function () {
        window.location = "/page/toLogin";
    });
    $("#submitButton").click(function () {
       var code = $("#code").val();
       if (code == null || code == ""){
           $.toast("请输入邀请码","forbidden");
           return;
       }
       if (sessionStorage.getItem("class_limit_token") == null || sessionStorage.getItem("class_limit_token") == ""){
           window.location = "/page/toLogin";
       }
       var a = {};
       a.token = sessionStorage.getItem("class_limit_token");
       a.data = code;
       $.ajax({
           url : "/api/joinClass",
           type : "post",
           contentType : "application/json",
           dataType :"json",
           data : JSON.stringify(a),
           success: function (data) {
               if (data.statusCode == 200){
                   $.toast("加入班级成功","text");
                   sessionStorage.setItem("token",sessionStorage.getItem("class_limit_token"));
                   window.location = "/page/toIndex";
               } else {
                   $.toast("失败!"+data.message,"text");
               }
           }
       });
   });

});
