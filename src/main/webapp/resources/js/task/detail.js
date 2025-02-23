var isNeedSubmit = 0;
$(function () {
    $("#taskName").val(sessionStorage.getItem("taskName"));
    $("#content").val(sessionStorage.getItem("content"));
    $("#remark").val(sessionStorage.getItem("remark"));
    $("#startTime").val(sessionStorage.getItem("startTime").substring(0,19));
    $("#endTime").val(sessionStorage.getItem("endTime").substring(0,19));
    $("#submitCount").val(sessionStorage.getItem("submitCount"));
    isNeedSubmit = sessionStorage.getItem("isNeedSubmit");
    $("#endTime").datetimePicker();
    $("#startTime").datetimePicker();
    if (sessionStorage.getItem("isNeedSubmit") == "0"){
        // 当本作业为线下作业时候隐藏提交数量input 及分数
        $(".submitCountItem").css("display","none");
        $(".taskResultUL").css("display","none");
        // 隐藏上传作业 及提交按钮
        $(".uploadForm").css("display","none");
        $("#uploadButton").css("display","none");
        if (sessionStorage.getItem("identity") == 0){
            //学生
            $("#typeList").html("<input type=\"text\" value=\"线下作业\" disabled />");
        } else{
            $("#typeList").html("<label><input class=\"aui-radio\" id=\"type1\" checked onclick=\"setChecked(this);\" type=\"radio\" name=\"demo1\">线下作业</label><label><input class=\"aui-radio\" id=\"type2\" type=\"radio\" onclick=\"setChecked(this);\" name=\"demo1\" >线上作业</label>");
        }
    }else{
        // 获取成绩信息（当前学生用户的结果信息，如果没有相应数据表示当前用户未提交）
        // 获取成绩信息（当前任务下的所有结果信息，教师用户可以进行评阅打分操作）
        getTaskResult(sessionStorage.getItem("taskId"),sessionStorage.getItem("token"));
        // 线上作业 学生用户的相关操作
        if (sessionStorage.getItem("identity") == 0){
            $(".submitCountItem").css("display","none");
            $("#typeList").html("<input type=\"text\" value=\"线上作业\" disabled />");
            // 判断时间是否是进行中的状态，如果不是的话隐藏上传作业及提交按钮
            var date = new Date();
            if (new Date(sessionStorage.getItem("endTime")) < date){
                $(".uploadForm").css("display","none");
                $("#uploadButton").css("display","none");
            }
        } else{
            $("#typeList").html("<label><input class=\"aui-radio\" id=\"type1\"  onclick=\"setChecked(this);\" type=\"radio\"  name=\"demo1\">线下作业</label><label><input class=\"aui-radio\" id=\"type2\" checked type=\"radio\" onclick=\"setChecked(this);\" name=\"demo1\">线上作业</label>");
        }


    }



    var identity = sessionStorage.getItem("identity");
    if (identity == 0){
        // 学生用户只能查看，不能修改作业信息
        $("#taskName").attr("disabled","disabled");
        $("#content").attr("disabled","disabled");
        $("#remark").attr("disabled","disabled");
        $("#startTime").attr("disabled","disabled");
        $("#type").attr("disabled","disabled");
        $("#endTime").attr("disabled","disabled");
        $("#updateButton").css("display","none");
        $("#deleteButton").css("display","none");
    } else {
        // 教师用户
        $("#uploadButton").css("display","none");
        $(".uploadForm").css("display","none");
    }


    function packFile(){
        $('#bigImgGroup').removeClass('imageGroup')
        $('#exampleFormControlFile1').change(function(){
            $('#bigImgGroup').addClass('imageGroup')
            if($(this).get(0).files.length == 0){
                $('#bigImgGroup').removeClass('imageGroup');
            }
            var newGroup = '<div class="col-sm-10 col-sm-offset-2" id="imageGroup"></div>'
            $('#imageGroup').replaceWith(newGroup);
            $($(this).get(0).files).each(function(){
                var file = $(this)[0]
                console.log(file)
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload=function(e){
                    var img = e.target.result
                    var newImg ="<div class='col-sm-2 goodsSlImg' style='padding:0;'><img class='imgContent' src='"+img+"'></div>"
                    $('#imageGroup').append(newImg)
                }
            })
        })
    }
    //<li class="weui-uploader__file" style="background-image:url(./images/pic_160.png)"></li>
    $("#uploaderInput").on('change', function () {
        $("#uploadTip").html("您已经选择了文件，可点击加号重新选择。");
        if (typeof (FileReader) != "undefined") {
            var image_holder = $("#image-holder");
            image_holder.empty();
            var reader = new FileReader();
            reader.onload = function (e) {
                $("<img />", {
                    "style": "float:left;width:150px;height:150px;margin-right:15px",
                    "src": e.target.result,
                    "class": "thumb-image"
                }).appendTo(image_holder);
            }
            image_holder.show();
            reader.readAsDataURL($(this)[0].files[0]);
        } else {
            alert("This browser does not support FileReader.");
        }
    });
    $("#uploadButton").click(function () {
        var formData = new FormData();
        var img_file = document.getElementById("uploaderInput");
        var fileObj = img_file.files[0];
        formData.append("file",fileObj);
        $.ajax({
            url : "/api/uploadTaskResultFile",
            type : "post",
            data : formData,
            async : false,
            processData : false,
            contentType : false,
            success : function (T) {
                if (T.statusCode == 200){
                    // 保存提交的记录信息
                    var req = {};
                    var data = {};
                    data.taskId = sessionStorage.getItem("taskId");
                    data.file = T.data;
                    req.token = sessionStorage.getItem("token");
                    req.data = data;

                    $.ajax({
                       url : "/api/submitTaskResultByStudent",
                       dataType :"json",
                       contentType : "application/json",
                       data :JSON.stringify(req),
                       success : function (T2) {
                           if (T2.statusCode == 200){
                               $.toast("提交作业成功！");
                               window.location = "/page/toTaskDetail";
                           } else{
                               $.toast(T2.message, "forbidden");
                           }
                       } ,
                        type : "post"
                    });
                } else{
                    alert(T.message);
                }
            }
        })
    });
    $("#updateButton").click(function () {
        if (sessionStorage.getItem("identity") != 1){
            $.toast("你没权限","forbidden");
        }
        // 获取数据
        var keyId = sessionStorage.getItem("taskId");
        var taskName = $("#taskName").val();
        var content = $("#content").val();
        var remark = $("#remark").val();
        var startTime = $("#startTime").val();
        if (startTime.length <18){
            startTime += ":00";
        }
        var endTime = $("#endTime").val();
        if (endTime.length <18){
            endTime += ":00";
        }


        var req = {};
        var data = {};
        data.taskName = taskName;
        data.content = content;
        data.remark = remark;
        data.startTime = startTime.substring(0,19);
        data.endTime = endTime.substring(0,19);
        data.isNeedSubmit = isNeedSubmit;
        data.keyId = keyId;
        req.data = data;
        req.token = sessionStorage.getItem("token");

        $.ajax({
                url : "/api/updateTaskByTeacher",
                data : JSON.stringify(req),
                dataType : "json",
                contentType : "application/json",
                success : function (t) {
                    if (t.statusCode == 200) {
                        $.toast("更新成功!", "text");
                        sessionStorage.setItem("taskName",taskName);
                        sessionStorage.setItem("content",content);
                        sessionStorage.setItem("remark",remark);
                        sessionStorage.setItem("startTime",startTime);
                        sessionStorage.setItem("endTime",endTime);
                        sessionStorage.setItem("isNeedSubmit",isNeedSubmit);
                    } else{
                        $.toast(t.message,"forbidden");
                    }
                },
                type : "post"
        }
        );
    });

    $("#deleteButton").click(function () {
        var taskId = sessionStorage.getItem("taskId");
        var token = sessionStorage.getItem("token");
        var req = {};
        var data = {};
        data.isDelete = 1;
        data.keyId = taskId;
        req.token = token;
        req.data = data;

        $.ajax({
                url : "/api/updateTaskByTeacher",
                data : JSON.stringify(req),
                dataType : "json",
                contentType : "application/json",
                success : function (t) {
                    if (t.statusCode == 200) {
                        sessionStorage.removeItem("taskId")
                        sessionStorage.removeItem("taskName");
                        sessionStorage.removeItem("content");
                        sessionStorage.removeItem("remark");
                        sessionStorage.removeItem("startTime");
                        sessionStorage.removeItem("endTime");
                        sessionStorage.removeItem("isNeedSubmit");
                        window.location = "/page/toTaskDeleteSuccess";
                    } else{
                        $.toast(t.message,"forbidden");
                    }
                },
                type : "post"
            }
        );
    });



});

function getTaskResult(taskId, token) {
    var req= {};
    var data= {};
    data.taskId = taskId;
    req.token = token;
    req.data = data;
    $.ajax({
        url : "/api/getTaskResult",
        data : JSON.stringify(req),
        dataType : "json",
        contentType : "application/json",
        type : "post",
        success : function (T) {
            if (T.statusCode == 200){
                // 获取成功
                if (T.data.length == 0){
                        $(".taskResultUL").html($(".taskResultUL").html() + "   <li class=\"aui-list-item aui-list-item-middle\">" +
                            "<div class=\"aui-list-item-inner\">" + "暂无提交记录" + "</div>" + "</li>");
                } else{
                    // 如果是学生隐藏上传文件
                    if (sessionStorage.getItem("identity") == 0){
                        $(".uploadForm").css("display","none");
                        $("#uploadButton").css("display","none");
                    }
                    var htmlStr = $(".taskResultUL").html();
                    for (var i=0; i<T.data.length; i++){
                        htmlStr += "<div class=\"aui-list-item-inner\" onclick=\"goToResultDetailPage('"+T.data[i].keyId+"','"+T.data[i].file.split('\\')[T.data[i].file.split('\\').length-1]+"','"+T.data[i].userName+"','"+T.data[i].taskId+"','"+T.data[i].score+"','"+T.data[i].remark+"','"+T.data[i].addTime+"');\" style='cursor:pointer'><div class=\"aui-list-item-title\" style=\"margin-left:5px\">"+T.data[i].userName+"</div><div class=\"aui-list-item-right\">";
                        if (T.data[i].remark != null){
                            htmlStr += "<div class=\"aui-label aui-label-info\">已评阅</div></div></div>";
                        } else{
                            htmlStr += "<div class=\"aui-label aui-label-info\" style='background-color: red'>待评阅</div></div></div>";
                        }
                    }
                    $(".taskResultUL").html(htmlStr);
                }
            } else {
                $.toast(T.message,"forbidden");
                if (T.message == "未登录"){
                    window.location = "/page/toLogin";
                }
            }
        }
    })
}

function setChecked(obj) {
    if ($(obj).attr("id") == "type1"){
        // 线上作业
        isNeedSubmit = 0;
    } else {
        isNeedSubmit = 1;
    }
}

function  goToResultDetailPage(keyId,file,userName,taskId,score,remark,addTime) {
    sessionStorage.setItem("task_result_keyId",keyId);
    sessionStorage.setItem("task_result_file",file);
    sessionStorage.setItem("task_result_userName",userName);
    sessionStorage.setItem("task_result_taskId",taskId);
    sessionStorage.setItem("task_result_score",score);
    sessionStorage.setItem("task_result_remark",remark);
    sessionStorage.setItem("task_result_addTime",addTime);
    window.location = "/page/toTaskResultDetail";
}