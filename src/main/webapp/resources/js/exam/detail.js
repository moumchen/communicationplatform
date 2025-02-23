// 从sessionStorage取出相关考试信息
var examName = sessionStorage.getItem("examDetail_name");
var examId = sessionStorage.getItem("examDetail_id");
var averageScore = sessionStorage.getItem("examDetail_averageScore");
var maxScore = sessionStorage.getItem("examDetail_maxScore");
var examNum = sessionStorage.getItem("examDetail_num");
var subject = sessionStorage.getItem("examDetail_subject");
var examStartTime = sessionStorage.getItem("examDetail_startTime");
var examEndTime = sessionStorage.getItem("examDetail_endTime");
var addFlag = 0;
var students = new Array();
var originalStudents = new Array();
$(function () {

    $("#examName").val(examName);
    $("#subject").val(subject);
    $("#averageScore").val(averageScore);
    $("#maxScore").val(maxScore);
    $("#num").val(examNum);

    $("#startTime").datetimePicker();
    $("#endTime").datetimePicker();
    $("#startTime").val(examStartTime.substring(0,19));
    $("#endTime").val(examEndTime.substring(0,19));


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
                if (T.data.length == 0){
                    $("#tips-2").css("display","block");
                    $("#addItemButton").attr("disabled","disabled");
                } else {
                    for (var i = 0; i < T.data.length; i++){
                        students[i] = T.data[i];
                        originalStudents[i] = T.data[i];
                    }
                }
            }
        }
    });

    // 获取当前考试的学生成绩
    var req = {};
    var data =  examId;
    req.token = sessionStorage.getItem("token");
    req.data = data;

    $.ajax({
        url : "/api/getScoresByTeacher",
        type : "post",
        contentType : "application/json",
        dataType : "json",
        async: false,
        data : JSON.stringify(req),
        success :function (T) {
            if (T.statusCode == 200){
                // 获取成功
                if (T.data.length == 0){
                    $(".scoreUL").html($(".scoreUL").html() + "   <li id=\"noScoreTip\" class=\"aui-list-item aui-list-item-middle\">" +
                        "<div class=\"aui-list-item-inner\">" + "暂无成绩记录，点击下方添加按钮添加成绩吧！" + "</div>" + "</li>");
                } else {
                    var htmlStr = $(".scoreUL").html();
                    for (var i=0; i<T.data.length; i++){
                        var oldStr = "<li userId='"+T.data[i].student.keyId+"' score='"+T.data[i].score.score+"' class=\"aui-list-item\"> <div class=\"aui-list-item-inner\"><span style='width: 130px;'>学生:</span><input type='text' width='80px' disabled value='"+T.data[i].student.name+"'/>  <span style='width: 130px;'>分数:</span><input type='text' onchange='updateInfoToParentTag(this,2);' width='50px' placeholder='输入分数' value="+T.data[i].score.score+"></div><div onclick='updateScore(this);'  style='margin-top:8px;margin-right:5px;cursor:pointer' class=\"aui-btn aui-btn-info\">更新</div><div style='margin-top:8px;margin-right:5px;cursor:pointer' onclick=\"deleteOld(this,'"+T.data[i].student.name+"');\"   class=\"aui-btn aui-btn-danger\">删除</div></li>";
                        $(".scoreUL").append(oldStr);
                        removeFromArray(T.data[i].student.name);
                    }

                }
            } else {
                $.toast(T.message,"forbidden");
                if (T.message == "未登录"){
                    window.location = "/page/toLogin";
                }
            }
        }
    });
    $("#addItemButton").click(function () {
        if (students.length == 1){
            $.toast("所有学生已录入成绩！","forbidden");
            return;
        }
        if (addFlag == 1){
            $.toast("请先保存新添加的元素后再新增!","forbidden");
            return;
        }
        addFlag = 1;
        $("#noScoreTip").css("display","none");
        if ($("#tips-1").attr("count") == 0){
            $("#tips-1").css("display","block");
            $("#tips-1").attr("count",1);
        }
        var newInput = "<li class=\"aui-list-item\"> <div class=\"aui-list-item-inner\"><span style='width: 130px;'>学生:</span><select pre='0' onchange='updateInfoToParentTag(this,1);'  >";
        newInput += "<option>请选择学生</option>";
        for (var j = 0; j < students.length; j++){
            if (students[j].identity == 0){
                newInput += "<option>"+students[j].name+"</option>";
            }
        }
           newInput += "</select>  <span style='width: 130px;'>分数:</span><input type='text' onchange='updateInfoToParentTag(this,2);' width='80px' placeholder='输入分数'></div><div style='cursor:pointer;margin-top:8px;margin-right:5px' onclick='save(this);' class=\"aui-btn aui-btn-warning\">保存</div><div id='scoreInput' style='margin-top:8px;margin-right:5px;cursor:pointer'  onclick='deleteNew(this);'    class=\"aui-btn aui-btn-danger\">删除</div></li>";
        $(".scoreUL").append(newInput);

    });

    $("#updateButton").click(function () {
        var req = {};
        var data = {};
        data.keyId = sessionStorage.getItem("examDetail_id");
        data.examName = $("#examName").val();
        data.subject = $("#subject").val();
        req.data = data;
        req.token = sessionStorage.getItem("token");
        $.ajax({
            url: "/api/updateExamInfoByTeacher",
            type: "post",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(req),
            success: function (T) {
                if (T.statusCode == 200) {
                    $.toast("更新成功");
                    sessionStorage.setItem("examDetail_name", $("#examName").val());
                    sessionStorage.setItem("examDetail_subject", $("#subject").val());
                } else {
                    $.toast(T.message, "forbidden");
                    if (T.message == "未登录") {
                        window.location = "/page/toLogin";
                    }
                }
            }
        });
    });

    $("#deleteButton").click(function () {
        var dialog = new auiDialog({});
        dialog.alert({
            title:"请您确认",
            msg:'是否要删除本考试信息？',
            buttons:['取消','确定']
        },function(ret){
            if(ret){
                if(ret.buttonIndex == 2){
                    var req = {};
                    var data = {};
                    data.keyId = sessionStorage.getItem("examDetail_id");
                    data.isDelete = 1;
                    req.data = data;
                    req.token = sessionStorage.getItem("token");
                    $.ajax({
                       url : "/api/updateExamInfoByTeacher",
                        type : "post",
                        contentType : "application/json",
                        dataType : "json",
                        data : JSON.stringify(req),
                        success : function (T) {
                            if (T.statusCode == 200){
                                $.toast("删除成功");
                                window.location = "/page/toExamHome";
                            } else {
                                $.toast(T.message,"forbidden");
                                if (T.message == "未登录"){
                                    window.location = "/page/toLogin";
                                }
                            }
                        }
                    });
                }
            }
        })

    });

});

function save(tag){
    var req = {};
    var userId = $($(tag).parent()).attr("userid");
    var score = $(tag).parent().attr("score");
    if (userId == null){
        $.toast("请选择学生用户!","forbidden");
        return;
    }
    if (score == null){
        $.toast("请输入成绩","forbidden");
        return;
    }
    var data = new Array();
    var item = {};
    item.userId = userId;
    item.examId = examId;
    item.score = score;
    data[0] = item;
    req.data = data;
    req.token = sessionStorage.getItem("token");
    $.ajax({
       url : "/api/addScoresByTeacher",
       type : "post",
       contentType : "application/json",
       dataType : "json",
        data : JSON.stringify(req),
        success : function (T) {
            if (T.statusCode == 200){
                $.toast("保存成功！");
                addFlag = 0;
                updateInfo();
            } else {
                $.toast(T.message,"forbidden");
            }
        }
    });
}
function deleteNew(tag) {
    addFlag = 0;
    var dialog = new auiDialog({});
    dialog.alert({
        title:"请您确认",
        msg:'是否删除本条成绩信息？',
        buttons:['取消','确定']
    },function(ret){
        if(ret){
            if(ret.buttonIndex == 2){
                var username = $(tag).find("select").val();
                var div = $(tag).parent();
                $(div).remove();
                // 将该用户放回数组中
                putBackToArray(username);
            }
        }
    })
}

function updateInfoToParentTag(tag,type) {
     if (type == 1){
         // 学生信息
        var userName = $(tag).val();
         removeFromArray(userName);
         for (var i = 0; i < originalStudents.length; i++){
            if (userName == originalStudents[i].name){
                $(tag).parent().parent().attr("userId",originalStudents[i].keyId);
                return;
            }
        }
     } else {
         // 分数
         $(tag).parent().parent().attr("score",$(tag).val());
     }
}

function deleteOld(tag,username) {
    var dialog = new auiDialog({});
    dialog.alert({
        title:"请您确认",
        msg:'是否删除本条成绩信息？',
        buttons:['取消','确定']
    },function(ret){
        if(ret){
            if(ret.buttonIndex != 1){
                var req = {};
                req.token = sessionStorage.getItem("token");
                var data = {};
                data.examId = examId;
                data.userId = $(tag).parent().attr("userid");
                req.data = data;
                $.ajax({
                   url : "/api/deleteScoreByTeacher",
                   type : "post",
                   contentType : "application/json",
                   dataType : "json",
                   data : JSON.stringify(req) ,
                   success : function (T) {
                       if (T.statusCode == 200){
                           $.toast("删除成功!");
                           var div = $(tag).parent();
                           $(div).remove();
                           updateInfo();
                       } else {
                           $.toast(T.message, "forbidden");
                           if (T.message == "未登录"){
                               window.location = "/page/toLogin";
                           }
                       }
                   }
                });
                // 将该用户重新放回数组中
                for (var i = 0; i <originalStudents.length; i++){
                    if (originalStudents[i].name == username){
                        students[students.length] = originalStudents[i];
                    }
                }

            }
        }
    })
}

function updateInfo() {
    // 更新当前考试信息
    var req=  {};
    req.token = sessionStorage.getItem("token");
    req.data = examId;

    $.ajax({
       url : "/api/getExamByExamId",
        type : "post",
        contentType : "application/json",
        dataType : "json",
        data : JSON.stringify(req),
        success : function (T) {
            if (T.statusCode == 200) {
                examName = T.data.examName;
                averageScore = T.data.averageScore;
                maxScore = T.data.maxScore;
                examNum = T.data.num;
                subject = T.data.subject;
                examStartTime = T.data.startTime;
                examEndTime = T.data.endTime;
                sessionStorage.setItem("examDetail_name",examName);
                sessionStorage.setItem("examDetail_num",examNum);
                sessionStorage.setItem("examDetail_maxScore",maxScore);
                sessionStorage.setItem("examDetail_averageScore",averageScore);
                sessionStorage.setItem("examDetail_startTime",examStartTime);
                sessionStorage.setItem("examDetail_endTime",examEndTime);
                sessionStorage.setItem("examDetail_subject",subject);
                $("#examName").val(examName);
                $("#subject").val(subject);
                $("#averageScore").val(averageScore);
                $("#maxScore").val(maxScore);
                $("#num").val(examNum);
                $("#startTime").datetimePicker();
                $("#endTime").datetimePicker();
                $("#startTime").val(examStartTime.substring(0,19));
                $("#endTime").val(examEndTime.substring(0,19));
                window.location.reload();
            } else {
                if (T.message == "未登录"){
                    $.toast("您的登录已过期，请注意保存您现在录入的信息后重新登录","forbidden");
                }else {
                    $.toast(T.meesage, "forbidden");
                }
            }
        }
    });
}

function closeTips() {
    $("#tips-1").css("display","none");
}

function removeFromArray(name) {
    for (var i = 0; i < students.length; i++){
        if (name == students[i].name){
            students.splice(i,1);
            return;
        }
    }
}

function putBackToArray(username) {
    for (var i = 0; i <originalStudents.length; i++){
        if (originalStudents[i].name == username){
            students[students.length] = originalStudents[i];
        }
    }
}

function updateScore(tag) {
    var req = {};
    var userId = $(tag).parent().attr("userId");
    var examId = sessionStorage.getItem("examDetail_id");
    var score = $(tag).parent().attr("score");
    var data = new Array();
    var item = {};
    item.userId = userId;
    item.score = score;
    item.examId = examId;
    data[0] = item;
    req.data = data;
    req.token = sessionStorage.getItem("token");

    $.ajax({
        url : "/api/updateScoresByTeacher",
        contentType : "application/json",
        dataType : "json",
        type : "post",
        data : JSON.stringify(req),
        success : function (T) {
            if (T.statusCode == 200){
                $.toast("更新成功!");
                updateInfo();
            } else {
                if (T.message == "未登录"){
                    $.toast("您的登录已过期，请注意保存您现在录入的信息后重新登录","forbidden");
                }else {
                    $.toast(T.meesage, "forbidden");
                }
            }
        }
    })
}