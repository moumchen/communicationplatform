var originalStudents = new Array();
var oppoUserName = sessionStorage.getItem("chat_oppoUserName");
var userId = sessionStorage.getItem("userId");
var oppoUserId = sessionStorage.getItem("chat_oppoUserId");
var oppoUserHeadImg = sessionStorage.getItem("chat_oppoUserHeadImg");
var groupName = sessionStorage.getItem("chat_groupName");
var chat_type = sessionStorage.getItem("chat_type");
var chat_isFromSelect = sessionStorage.getItem("chat_isFromSelect");
var name = sessionStorage.getItem("name");
var headImg = sessionStorage.getItem("headImg");
var selfGroupName = sessionStorage.getItem("chat_self_groupName");
var selfGroupId = sessionStorage.getItem("chat_self_groupId");
$(function () {
     if (chat_type == 1){
         //单聊
         $(".aui-title").html(oppoUserName);
     } else if (chat_type == 2){
         // 班群聊天
         $(".aui-title").html(groupName);
         // 获取当前用户所在班级所有成员
         var req2 = sessionStorage.getItem("token");
         $.ajax({
             url : "/api/getClassMemberBySelf",
             type : "post",
             async : false,
             contentType : "application/json",
             dataType : "json",
             data : req2,
             success : function (T) {
                 if (T.statusCode == 200){
                     var str ="";
                     for (var i = 0; i < T.data.length; i++){
                         originalStudents[i] = T.data[i];
                     }
                 }
             }
         });
     } else{
         // 自定义群组聊天
         $(".aui-title").html(selfGroupName);
         // 获取该群组的成员
         var req = {};
         req.token = sessionStorage.getItem("token");
         req.data = selfGroupId;
         $.ajax({
             url : "/api/getGroupMembers",
             type : "post",
             async : false,
             contentType : "application/json",
             dataType : "json",
             data : JSON.stringify(req),
             success : function (T) {
                 if (T.statusCode == 200){
                     var str ="";
                     for (var i = 0; i < T.data.length; i++){
                         originalStudents[i] = T.data[i];
                     }
                 }
             }
         });

     }

     // 获取默认数据：存在未读消息即拉去所有未读消息
    if (chat_isFromSelect == null || chat_isFromSelect != 1){
         var req1 = {};
         req1.token = sessionStorage.getItem("token");
         var data = {};
         data.queryType = chat_type;
         if (chat_type == 1){
             data.oppoUserId = oppoUserId;
         }
         if (chat_type == 3){
             data.groupId = selfGroupId;
         }
         data.mineUserId = userId;
         req1.data = data;
         $.ajax({
             url : "/api/getNoMarkMessage",
             type : "post",
             async : false,
             contentType : "application/json",
             dataType : "json",
             data : JSON.stringify(req1),
             success : function (T) {
                 if (T.statusCode == 200) {
                     // 组装
                     var flag = 0;
                     for (var i = 0; i < T.data.length; i++) {
                         flag = 1;
                        msgToTag(T.data[i]);
                     }
                     // 调用已读接口
                     if (flag == 1) {
                         $.ajax({
                             url: "/api/updateNoMarkMessage",
                             type: "post",
                             async: false,
                             contentType: "application/json",
                             dataType: "json",
                             data: JSON.stringify(req1),
                             success: function (T) {
                                 if (T.statusCode == 200) {

                                 } else {
                                     $.toast(T.message, "forbidden");
                                     if (T.message == "未登录") {
                                         window.location = "/page/toLogin";
                                     }
                                 }
                             }
                         });
                     }
                 } else {
                     $.toast(T.message, "forbidden");
                     if (T.message == "未登录") {
                         window.location = "/page/toLogin";
                     }
                 }

             }
         });

    }

    // WebSocket连接
    var websocket = null;
    if("WebSocket" in window){
        websocket = new WebSocket("ws://localhost:8081/ws/chat/"+userId);
    }else{
        alert("您的浏览器不支持WEBSOCKET");
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        alert("连接错误,请重试！");
    }
    //连接成功建立的回调方法
    websocket.onopen = function () {

    }
    //接收到消息的回调方法
    websocket.onmessage = function (data) {
        msgToTag(JSON.parse(data.data));
    }
    //连接关闭的回调
    websocket.onclose = function () {

    }
    //监听窗口关闭事件，当窗口关闭时，主动关闭websocket
    window.onbeforeunload = function () {
        websocket.close();
    }


   $("#commitButton").click(function () {
       var message = $("#typeContent").val();
       $("#typeContent").val("");


       var req = {};
       req.content = message;
       req.fromUser = userId;
       if (chat_type == 1){
           req.toUser = oppoUserId;
           req.type = 1;
       } else if(chat_type == 2){
           req.groupId = sessionStorage.getItem("classId");
           req.type = 2;
       } else{
           req.groupId = selfGroupId;
           req.type = 3;
       }
       // 组装自己的信息
       mineMsgToTag(req);
       websocket.send(JSON.stringify(req));
   });




});
function mineMsgToTag(data) {
    var str = "<div class=\"aui-chat-item aui-chat-right\"><div class=\"aui-chat-media\">";
    if (headImg == "null") {
        str += "<img src=\"/resources/images/headImg/defaultHeadImg.png\" />";
    } else {
        str += "<img src=\"/resources/images/headImg/"+headImg+"\" />"
    }
    str += "  </div> <div class=\"aui-chat-inner\"><div class=\"aui-chat-name\">" + name + " <span class=\"aui-label aui-label-info\">"+dateFtt(new Date(),"yyyy-MM-dd hh:mm:ss")+"</span></div>";
    str += "  <div class=\"aui-chat-content\"><div class=\"aui-chat-arrow\"></div>" +
        data.content +
        "</div><div class=\"aui-chat-status aui-chat-status-refresh\"><i class=\"aui-iconfont aui-icon-correct aui-text-success\"></i></div></div></div>";
    $(".content").append(str);
}
function msgToTag(data) {
    var str = "<div class=\"aui-chat-item aui-chat-left\"><div class=\"aui-chat-media\">";
    if (chat_type == 1) {
        if (oppoUserHeadImg == "null") {
            str += "<img src=\"/resources/images/headImg/defaultHeadImg.png\" />";
        } else {
            str += "<img src=\"/resources/images/headImg/"+oppoUserHeadImg+"\" />";
        }
        str += "  </div> <div class=\"aui-chat-inner\"><div class=\"aui-chat-name\">" + oppoUserName+ " <span class=\"aui-label aui-label-info\">"+data.addTime.substring(0,19)+"</span></div>";
    } else {
        var userName = "";
        var headImg = "";
        for (var j = 0; j < originalStudents.length; j++){
            if (originalStudents[j].keyId == data.fromUser){
                userName = originalStudents[j].name;
                headImg = originalStudents[j].headImg;
                break;
            }
        }
        if (headImg == null) {
            str += "<img src=\"/resources/images/headImg/defaultHeadImg.png\" />";
        } else {
            str += "<img src=\"/resources/images/headImg/"+ headImg.split("\\")[headImg.split("\\").length - 1] +"\" />";
        }
        str += "  </div> <div class=\"aui-chat-inner\"><div class=\"aui-chat-name\">" + userName + " <span class=\"aui-label aui-label-info\">"+data.addTime.substring(0,19)+"</span></div>";
    }
    str += "  <div class=\"aui-chat-content\"><div class=\"aui-chat-arrow\"></div>" +
        data.content +
        "</div><div class=\"aui-chat-status aui-chat-status-refresh\"><i class=\"aui-iconfont aui-icon-correct aui-text-success\"></i></div></div></div>";
    $(".content").append(str);
}

function dateFtt(date, fmt) { //author: meizz
    var o = {
        "M+" : date.getMonth() + 1, //月份
        "d+" : date.getDate(), //日
        "h+" : date.getHours(), //小时
        "m+" : date.getMinutes(), //分
        "s+" : date.getSeconds(), //秒
        "q+" : Math.floor((date.getMonth() + 3) / 3), //季度
        "S" : date.getMilliseconds()//毫秒
    };
    if (arguments.length == 1) {
        fmt = 'yyyy-MM-dd hh:mm:ss';
    }
    if (/(y+)/.test(fmt)){
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for ( var k in o){
        if (new RegExp("(" + k + ")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]): (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}