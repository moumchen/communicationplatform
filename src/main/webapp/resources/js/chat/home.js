var originalStudents = new Array();
var userId = sessionStorage.getItem("userId");
var groupName;
var checkBoxStr;
var checkedUserId = new Array();
var userInfos = new Array();
var selfGroupName;
var selftGroupId;
$(function () {
    // 获取会话列表
    getCurrentSessionList();

    setInterval(function(){
        getCurrentSessionList();//重新获取列表数据
    },5000);

    $(".addButton").click(function () {
        openDialog('callback','请选择您要发起会话的对象',checkBoxStr);
    });
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
                // var str ="";
                checkBoxStr = "";
                var index = 0;
                for (var i = 0; i < T.data.length; i++){
                        originalStudents[i] = T.data[i];
                        if (T.data[i].keyId != userId){
                            userInfos[index] = T.data[i];
                            index++;
                            // str += "<option style='cursor:pointer' oppoUserName='"+T.data[i].name+"' oppoUserHeadImg='"+T.data[i].headImg+"' oppoUserId='"+T.data[i].keyId+"' mineUserId='"+userId+"'>"+T.data[i].name+"</option>";
                            checkBoxStr += "<input class=\"aui-checkbox\" type=\"checkbox\" oppoUserId='"+T.data[i].keyId+"' onclick='clickCheckBox(this);'>"+T.data[i].name +"<br/>";
                        }
                }
                // $("#selectTag").append(str);
            }
        }
    });
});

// 获取当前存在的会话
function getCurrentSessionList() {
    $.ajax({
       url : "/api/getMessageSession",
       contentType : "application/json",
       dataType : "json",
       data : sessionStorage.getItem("token"),
       type : "post",
       success : function (T) {
           if (T.statusCode == 200){
               var str = "";
               for (var i=0; i < T.data.length; i++){
                   if (T.data[i].type == 1) {
                       str += "<li class=\"aui-list-item aui-list-item-middle\"  onclick=\"openChatSession(1,'"+T.data[i].user.keyId +"','"+T.data[i].user.headImg+"','"+T.data[i].user.name+"','li')\" style='cursor:pointer;'>";
                   } else if (T.data[i].type == 2){
                       groupName = T.data[i].groupName;
                       str += "<li class=\"aui-list-item aui-list-item-middle\"  onclick='openChatSessionGroup(2)' style='cursor:pointer;'>" ;
                   } else{
                       str += "<li class=\"aui-list-item aui-list-item-middle\" selfGroupId='"+T.data[i].groupId+"' selfGroupName='"+T.data[i].groupName+"' onclick='openSelfChatGroup(this)' style='cursor:pointer;'>" ;
                   }
                   str +=" <div class=\"aui-media-list-item-inner\">\n" +
                   "  <div class=\"aui-list-item-media wechat-avatar\" style='width:80px;height: 80px'>\n";
                if (T.data[i].noMarkNumber != 0){
                  str +=  "<div class=\"aui-badge\">"+T.data[i].noMarkNumber+"</div>\n";
                }
                if (T.data[i].type == 2 || T.data[i].type == 3) {
                    str += "<img src=\"/resources/images/headImg/defaultGroupHeadImg.jpg\" />";
                } else{
                    if (T.data[i].user.headImg == null){
                        str += "<img src=\"/resources/images/headImg/defaultHeadImg.png\" />"
                    } else {
                        str += "<img src=\"/resources/images/headImg/"+T.data[i].user.headImg.split('\\')[T.data[i].user.headImg.split('\\').length-1]+"\" />"
                    }
                }
                  str += " </div> <div class=\"aui-list-item-inner\"> <div class=\"aui-list-item-text\">";
                if (T.data[i].type != 1) {
                    str += "<div class=\"aui-list-item-title\">" + T.data[i].groupName + "</div>\n";
                    if (T.data[i].lastMessageTime == null){
                        str += "<div class=\"aui-list-item-right\"></div>";
                    }
                } else{
                    str += "<div class=\"aui-list-item-title\">"+T.data[i].user.name+"</div>\n"
                    str +=  "<div class=\"aui-list-item-right\">"+T.data[i].lastMessageTime.substring(0,19)+"</div>\n";

                }
               str+=    "</div><div class=\"aui-list-item-text aui-font-size-12\">\n";
                if (T.data[i].noMarkNumber == 0){
                    str+= "暂无未读消息";
                } else{
                    str+= "您有"+T.data[i].noMarkNumber +"条未读消息";
                }
                str+=   " </div> </div></div></li>";
               }
               $(".content").html(str);
           } else {
               $.toast(T.message,"forbidden");
               if (T.message == "未登录"){
                   window.location = "/page/toLogin";
               }
           }
       }
    });

}

// type为1是单聊 2为群聊
function openChatSession(type,oppoUserId,oppoUserHeadImg,oppoUserName,from) {
    if (from == "select"){
        sessionStorage.setItem("chat_isFromSelect","1");
    }else{
        sessionStorage.setItem("chat_isFromSelect","0");
    }
   if (type == 1){
       // 单聊
       sessionStorage.setItem("chat_type",1);
       sessionStorage.setItem("chat_oppoUserId",oppoUserId);
       if (oppoUserHeadImg == null) {
           sessionStorage.setItem("chat_oppoUserHeadImg", "defaultHeadImg.png");
       } else {
           sessionStorage.setItem("chat_oppoUserHeadImg", oppoUserHeadImg.split('\\')[oppoUserHeadImg.split('\\').length-1]);
       }
       sessionStorage.setItem("chat_oppoUserName",oppoUserName);

   } else if (type == 2){
       // 群聊
       sessionStorage.setItem("chat_type",2);
       sessionStorage.setItem("chat_groupName",groupName);
   } else {
      // 自定义群组聊天
       sessionStorage.setItem("chat_type",3);
       sessionStorage.setItem("chat_self_groupName",selfGroupName);
       sessionStorage.setItem("chat_self_groupId",selftGroupId);
   }
   window.location = "/page/toChatRoom";
}

// function openChatSessionBySelect(tag) {
//     var oppoUserId = $(tag).find("option:selected").attr("oppoUserId");
//     var oppoUserHeadImg = $(tag).find("option:selected").attr("oppoUserHeadImg");
//     var oppoUserName = $(tag).find("option:selected").attr("oppoUserName");
//     openChatSession(1, oppoUserId, oppoUserHeadImg,oppoUserName,"select");
// }

function openChatSessionGroup(type) {
    openChatSession(type,null,null,null,"li");
}

function openSelfChatGroup(obj) {
    selftGroupId = $(obj).attr("selfgroupid");
    selfGroupName = $(obj).attr("selfgroupname");
    openChatSession(3,null,null,null,"li");
}

var dialog = new auiDialog({});
function openDialog(type,title,content){
    switch (type) {
        case "text":
            dialog.alert({
                title:title,
                msg:content,
                buttons:['我已知悉']
            },function(ret){
                console.log(ret)
            });
            break;
        case "callback":
            dialog.alert({
                title:title,
                msg:content,
                buttons:['取消','确定']
            },function(ret){
                if (ret.buttonIndex == 2) {
                    if (checkedUserId.length == 1) {
                        // 发起单独的聊天 oppoUserId,oppoUserHeadImg,oppoUserName,from
                        for (var i = 0; i < userInfos.length; i++) {
                            if (userInfos[i].keyId == checkedUserId[0]) {
                                openChatSession(1, checkedUserId[0], userInfos[i].headImg, userInfos[i].name, "select");
                                return;
                            }
                        }
                    } else {
                        // 发起群聊
                        //1. 首先调用接口判断是否存在成员包括已选择的人员的群组，如果存在则直接返回群组信息，不存在创建后返回群组信息
                        var users = "";
                        for (var i = 0; i < checkedUserId.length; i++) {
                            users += checkedUserId[i] + "|";
                        }
                        var req1 = {};
                        req1.token = sessionStorage.getItem("token");
                        req1.data = users;
                        $.ajax({
                            url: "/api/createChatGroup",
                            type: "post",
                            async: false,
                            contentType: "application/json",
                            dataType: "json",
                            data: JSON.stringify(req1),
                            success: function (T) {
                                if (T.statusCode == 200) {
                                    // 根据结果
                                    selfGroupName = T.data.groupName;
                                    selftGroupId = T.data.keyId;
                                    // 打开会话
                                    openChatSession(3, null, null, null, "li");
                                }
                            }
                        });
                    }
                }
            });
            break;
        default:
            break;

    }
}

function clickCheckBox(obj) {
   var oppoUserId = $(obj).attr("oppouserid");
   for (var i = 0; i <checkedUserId.length; i++){
       if (oppoUserId == checkedUserId[i]){
           // 存在则移除
           checkedUserId.splice(checkedUserId[i],1);
           return;
       }
    }
   checkedUserId[checkedUserId.length] = oppoUserId;
}