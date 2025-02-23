$(function () {
   $(".submtdiv").click(function () {
      var username = $(".usern").val();
      var password = $(".passw").val();
      if ($.trim(username) == ""){
          $.toast("用户名不能为空");
          return;
      }
       if ($.trim(password) == ""){
           $.toast("密码不能为空");
           return;
       }
       $.post(
           "/api/login",
           {"username":username,"password":password,"errorTimes":0,"checkCode":"noCheckCode"} ,
           function (data) {
               if (data.statusCode == 200){
                   sessionStorage.setItem("admin_token",data.data);
                   window.location = "/page/toUserAdmin";
               } else{
                    $(".HtToolset").html("<font style='color:red'>"+ data.message + "</font>");
               }
           }
       );
   });


});