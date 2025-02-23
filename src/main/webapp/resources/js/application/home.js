$(function () {
   var identity =  sessionStorage.getItem("identity");
   if (identity == 0){
       // 学生/家长用户可以提审批申请
       $(".addButton").css("display","block");
       $("#tips-1").css("display","none");
   } else {
       $(".addButton").css("display","none");
   }

   $(".addButton").click(function () {
       window.location = "/page/toAddApplication";
   });

   // 查询当前用户的审批,学生即为我发起的，老师即为我审批的。
    $.ajax({
       url : "/api/getApplication",
        contentType : "application/json",
        dataType : "json",
        type : "post",
        data : sessionStorage.getItem("token"),
        success : function (T) {
            if (T.statusCode == 200){
                if (T.data.length > 0){
                    $(".noneApplications").css("display","none");
                    var str = $(".content").html();
                    if (identity == 0){
                        for (var i=0; i<T.data.length; i++){
                            str += "<div class=\"aui-card-list content_item\" style='width:95%;cursor:pointer'>";
                            str += "  <div class=\"aui-card-list-header\">" + T.data[i].title + "</div>";
                            str += " <div class=\"aui-card-list-content-padded\">";
                            str +=  T.data[i].content;
                            str += "</div><div class=\"aui-card-list-footer\">";
                            if (T.data[i].result == 1){
                            str += "<div class=\"aui-label aui-label-success\">状态:通过;审批意见:"+T.data[i].remark+"</div> 申请时间:"+T.data[i].addTime.substring(0,19) +"<br/>审批时间:" + T.data[i].resultTime.substring(0,19);
                                str += "<div style='float:right' onclick=\"cannotCallBack();\"  class=\"aui-btn \">撤销</div></div></div>";
                            } else if (T.data[i].result == 0){
                                str += "<div class=\"aui-label aui-label-warning\">状态:待审批</div> 申请时间:"+T.data[i].addTime.substring(0,19);
                                str += "<div style='float:right' onclick=\"callBack('"+T.data[i].keyId+"');\"  class=\"aui-btn  aui-btn-danger\">撤销</div></div></div>";
                            } else{
                                str += "<div class=\"aui-label aui-label-danger\">状态:拒绝;审批意见:"+T.data[i].remark+"</div> 申请时间:"+T.data[i].addTime.substring(0,19) +"<br/>审批时间:" + T.data[i].resultTime.substring(0,19);
                                str += "<div style='float:right'   class=\"aui-btn \">撤销</div></div></div>";
                            }
                        }
                    } else {
                        for (var i=0; i<T.data.length; i++){
                            str += "<div class=\"aui-card-list content_item\" style='width:95%;cursor:pointer'";
                            if (T.data[i].result == 0){
                                str += "onclick=\"checkApplication('"+T.data[i].keyId+"','"+T.data[i].title+"','"+T.data[i].content+"');\"";
                            }
                            str += "> <div class=\"aui-card-list-header\">" + T.data[i].title + "</div>";
                            str += " <div class=\"aui-card-list-content-padded\">";
                            str +=  T.data[i].content;
                            str += "</div><div class=\"aui-card-list-footer\">";
                            if (T.data[i].result == 1){
                                str += "<div class=\"aui-label aui-label-success\">我已审批:通过;审批意见:"+T.data[i].remark+"</div> 申请时间:"+T.data[i].addTime.substring(0,19) +"<br/>审批时间:" + T.data[i].resultTime.substring(0,19);
                            } else if (T.data[i].result == 0){
                                str += "<div class=\"aui-label aui-label-warning\">待我审批</div> 申请时间:"+T.data[i].addTime.substring(0,19);
                            } else{
                                str += "<div class=\"aui-label aui-label-danger\">我已审批:拒绝;审批意见:"+T.data[i].remark+"</div> 申请时间:"+T.data[i].addTime.substring(0,19) +"<br/>审批时间:" + T.data[i].resultTime.substring(0,19);
                            }
                            str += "</div></div>";
                        }

                    }
                    $(".content").html(str);


                }
            }else{
                $.toast(T.message, "forbidden");
                if (T.message == "未登录"){
                    window.location = "/page/toLogin";
                }
            }
        }
    });
});

function closeTips() {
    $("#tips-1").css("display","none");
}

function checkApplication(applicationId,title,content) {
    var dialog = new auiDialog({});
    dialog.alert({
        title:"请您审批:"+title,
        msg:content,
        buttons:['同意','拒绝','取消']
    },function(ret){
        if(ret){
            if(ret.buttonIndex == 1){
                dialog.prompt({
                    title:"请输入审批备注",
                    text:'请填写审批意见，不填默认无',
                    buttons:['提交','取消']
                },function(ret2){
                    if(ret2.buttonIndex == 1){
                        var req = {};
                        req.token = sessionStorage.getItem("token");
                        var data = {};
                        data.result = 1;
                        data.keyId = applicationId;
                        if(ret2.text == "请填写审批意见，不填默认无"){
                            data.remark ="无";
                        } else {
                            data.remark = ret2.text;
                        }
                        req.data = data;

                        $.ajax({
                            url : "/api/updateApplication",
                            contentType : "application/json",
                            dataType : "json",
                            type : "post",
                            data : JSON.stringify(req),
                            success : function (T) {
                                if (T.statusCode == 200){
                                    $.toast("审批成功!");
                                    window.location.reload();
                                } else{
                                    $.toast(T.message, "forbidden");
                                    if (T.message == "未登录"){
                                        window.location = "/page/toLogin";
                                    }
                                }
                            }
                        });
                    }else{

                    }
                });

            } else if (ret.buttonIndex == 2){
                dialog.prompt({
                    title:"请输入审批备注",
                    text:'请填写审批意见，不填默认无',
                    buttons:['提交','取消']
                },function(ret2) {
                    if (ret2.buttonIndex == 1) {
                        var req = {};
                        req.token = sessionStorage.getItem("token");
                        var data = {};
                        data.result = 2;
                        data.keyId = applicationId;
                        if(ret2.text == "请填写审批意见，不填默认无"){
                            data.remark ="无";
                        } else {
                            data.remark = ret2.text;
                        }
                        req.data = data;
                        $.ajax({
                            url : "/api/updateApplication",
                            contentType : "application/json",
                            dataType : "json",
                            type : "post",
                            data : JSON.stringify(req),
                            success : function (T) {
                                if (T.statusCode == 200){
                                    $.toast("审批成功!");
                                    window.location.reload();
                                } else{
                                    $.toast(T.message, "forbidden");
                                    if (T.message == "未登录"){
                                        window.location = "/page/toLogin";
                                    }
                                }
                            }
                        });
                    }else{

                    }
                });
            } else{

            }
            }
    })
}

function callBack(applicationId) {
    var dialog = new auiDialog({});
    dialog.alert({
        title:"撤销确认",
        msg:"是否撤销该审批申请？",
        buttons:['撤销','取消']
    },function(ret){
        if(ret){
            if(ret.buttonIndex == 1){
                var req = {};
                req.token = sessionStorage.getItem("token");
                var data = {};
                data.isDelete = 1;
                data.keyId = applicationId;
                req.data = data;
                $.ajax({
                    url : "/api/updateApplication",
                    contentType : "application/json",
                    dataType : "json",
                    type : "post",
                    data : JSON.stringify(req),
                    success : function (T) {
                        if (T.statusCode == 200){
                            $.toast("审批成功!");
                            window.location.reload();
                        } else{
                            $.toast(T.message, "forbidden");
                            if (T.message == "未登录"){
                                window.location = "/page/toLogin";
                            }
                        }
                    }
                });
            }
        }
    })
}
function cannotCallBack() {
    $.toast("已经经过审批，不允许撤销！","forbidden");
}