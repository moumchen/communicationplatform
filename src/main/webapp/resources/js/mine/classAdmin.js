$(function () {
   var identity = sessionStorage.getItem("identity");
   if (identity == 0){
      $("#operateButtons").css("display","none");
      $("#className").attr("disabled","disabled");
      $("#schoolName").attr("disabled","disabled");
   } else{
      $("#exitClass").css("display","none");
   }

   getClassInfo();
    getUserInfoAndSetTag();

    $("#exitClass").click(function () {
        var dialog = new auiDialog({});
        dialog.alert({
            title:"退出确认",
            msg:"是否退出该班级？请注意退出后您必须重新加入一个班级后才能使用系统功能！",
            buttons:['确认','取消']
        },function(ret){
            if(ret){
                if(ret.buttonIndex == 1){
                    $.ajax({
                        url : "/api/updateStudentClassInfoByStudent",
                        type : "post",
                        contentType : "application/json",
                        dataType : "json",
                        data : sessionStorage.getItem("token"),
                        success : function (T) {
                            if (T.statusCode == 200){
                                $.toast("退出班级成功!请重新登录!");
                                window.location = "/page/toLogin";
                            } else {
                                $.toast(T.message, "forbidden");
                            }
                        }
                    });
                }
            }
        })


    });
   $("#updateButton").click(function () {
      var className =  $("#className").val();
      var schoolName =  $("#schoolName").val();
      var req = {};
      req.token =sessionStorage.getItem("token");
      var data = {};
      data.className = className;
      data.schoolName = schoolName;
      data.keyId = classId;
      req.data = data;

      $.ajax({
          url : "/api/updateClassInfoByTeacher",
          type : "post",
          contentType : "application/json",
          dataType : "json",
          data : JSON.stringify(req),
          success : function (T) {
            if (T.statusCode == 200){
               $.toast("更新成功!");
               getClassInfo();
            } else{
               $.toast(T.message, "forbidden");
            }
          }
      });
   });
   $("#updateInviteCodeButton").click(function () {

       var req = {};
       req.token =sessionStorage.getItem("token");
       var data = {};
       data.inviteCode = "1";
       data.keyId = classId;
       req.data = data;

       $.ajax({
           url : "/api/updateClassInfoByTeacher",
           type : "post",
           contentType : "application/json",
           dataType : "json",
           data : JSON.stringify(req),
           success : function (T) {
               if (T.statusCode == 200){
                   $.toast("更新成功!");
                   getClassInfo();
               } else{
                   $.toast(T.message, "forbidden");
               }
           }
       });
   });
});
var classId ;
function getClassInfo() {
    $.ajax({
        url : "/api/getClassInfoBySelf",
        type : "post",
        contentType : "application/json",
        dataType : "json",
        data : sessionStorage.getItem("token"),
        success : function (T) {
            if (T.statusCode == 200){
               var className = T.data.className;
               var schoolName = T.data.schoolName;
               var count = T.data.count;
               var inviteCode = T.data.inviteCode;
               classId = T.data.keyId;
               $("#className").val(className);
               $("#schoolName").val(schoolName);
               $("#count").val(count);
               $("#inviteCode").val(inviteCode);
            } else {
               $.toast(T.message,"forbidden");
               if ("未登录"== T.message){
                  window.location = "/page/toLogin";
               }
            }
        }
    });
}

function getUserInfoAndSetTag() {

    $.ajax({
        url : "/api/getClassMemberBySelf",
        type : "post",
        contentType : "application/json",
        dataType : "json",
        data : sessionStorage.getItem("token"),
        success:function (T) {
            if (T.statusCode == 200){
               var identity = sessionStorage.getItem("identity");
                var htmlStr = $(".userList").html();
                for (var i =0 ; i< T.data.length; i++){
                   if (identity == 1 && T.data[i].identity == 1){
                      continue;
                   }
                   htmlStr += "<li  class=\"aui-list-item\"> <div class=\"aui-list-item-inner\">"+T.data[i].name;
                   if (T.data[i].identity == 0){
                      htmlStr += "【学生】";
                   }else{
                      htmlStr += "【教师】";
                   }
                   if (identity == 0){
                           htmlStr += "</div></li>";

                   } else {
                      //教师
                           htmlStr += "</div> <div style=\"margin-top:8px;margin-right:5px;cursor:pointer\" onclick=\"removeUser('"+T.data[i].keyId+"')\" class=\"aui-btn aui-btn-danger\">移除</div></li>";
                   }
                }
                $(".userList").html(htmlStr);

            }
        }
    });

}

function removeUser(userId) {
    var dialog = new auiDialog({});
    dialog.alert({
        title:"移除确认",
        msg:"是否移除该学生？该学生的相关作业、考试信息将会保留，如需加会该学生，请将邀请码告知学生重新加入班级！",
        buttons:['确认','取消']
    },function(ret) {
        if (ret) {
            if (ret.buttonIndex == 1) {
                var req = {};
                req.token = sessionStorage.getItem("token");
                req.data = userId;
                $.ajax({
                    url: "/api/deleteClassMemberByTeacher",
                    type: "post",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(req),
                    success: function (T) {
                        if (T.statusCode == 200) {
                            $.toast("移除成功!");
                            window.location.reload();
                        } else {
                            $.toast(T.message, "forbidden");
                        }

                    }
                });
            }
        }
        });
}