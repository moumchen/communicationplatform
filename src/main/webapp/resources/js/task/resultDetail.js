$(function () {
   var userName = sessionStorage.getItem("task_result_userName");
   var remark = sessionStorage.getItem("task_result_remark");
   var score = sessionStorage.getItem("task_result_score");
   var addTime = sessionStorage.getItem("task_result_addTime");
   var taskId = sessionStorage.getItem("task_result_taskId");
   var file = sessionStorage.getItem("task_result_file");
   var keyId = sessionStorage.getItem("task_result_keyId");
   var identity = sessionStorage.getItem("identity");
   $("#ImgFile").attr("src","/resources/images/taskResult/"+file);
   $("#userNameDiv").html(userName);
   $("#addTimeDiv").html(addTime.substring(0,19));

   if (remark == "null"){
       $("#isChecked").val("待评阅");
       $("#score").val("待评阅");
       $("#remark").val("(无评语)");
   } else{
       $("#isChecked").val("已评阅");
       $("#score").val(score);
       $("#remark").val(remark);
   }

   if (identity == 0){
       //学生
       $("#remark").attr("disabled","disabled");
       $("#markLi").css("display","none");
       $("#submitButton").css("display","none");
   }

   $("#backButton").click(function () {
       window.location = "/page/toTaskDetail";
   });

   $("#submitButton").click(function () {
       remark = $("#remark").val();
       score = $("#mark").val();

       var req = {};
       var data = {};
       req.token = sessionStorage.getItem("token");
       data.remark = remark;
       data.score = score;
       data.keyId = keyId;
       req.data =data;
       $.ajax({
           url : "/api/updateTaskResult",
           dataType :"json",
           contentType : "application/json",
           data : JSON.stringify(req),
           success : function (T) {
               if (T.statusCode == 200){
                   $.toast("评阅成功");
                   sessionStorage.setItem("task_result_score",score);
                   sessionStorage.setItem("task_result_remark",remark);
               } else {
                   $.toast(T.message,"forbidden");
                   if (T.message == "未登录"){
                       window.location = "/page/toLogin";
                   }
               }
           },
           type : "post"
       });
   });
});

function addNumber() {
    $("#mark").val(Number($("#mark").val()) + 1);
}
function reduceNumber() {
    if (Number($("#mark").val()) >= 1) {
        $("#mark").val(Number($("#mark").val()) - 1);
    } else {
        $.toast("不能为负数","forbidden");
    }
}

