$(function () {
    getUserInfo();
   $("#backButton").click(function () {
      window.location = "/page/toMineHome";
   });
    $("#updateButton").click(function () {
       var name=  $("#name").val();
       var nickname= $("#nickname").val();
       var phone =$("#phone").val();
       var email = $("#email").val();
       returnFlag = 0;
       isNull("姓名",name);
       if(returnFlag==1){return;}
       isNull("昵称",nickname);
        if(returnFlag==1){return;}
        isNull("电话",phone);
        if(returnFlag==1){return;}
        isNull("邮箱",email);
        if(returnFlag==1){return;}
        var req = {};
        req.token = sessionStorage.getItem("token");
        var data = {};
        data.keyId = keyId;
        data.name = name;
        data.nickname = nickname;
        data.phone = phone;
        data.email = email;
        req.data = data;
        $.ajax({
            url : "/api/updateUserInfo",
            contentType : "application/json",
            dataType : "json",
            type : "post",
            data : JSON.stringify(req),
            success : function (T) {
                if (T.statusCode==200){
                   $.toast("更新成功");
                   getUserInfo();
                } else{
                   $.toast(T.message,"forbidden");
                }
            }

        });
    });


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
            url : "/api/uploadHeadImg",
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
                    req.token = sessionStorage.getItem("token");
                    data.keyId = sessionStorage.getItem("userId");
                    data.headImg = T.data;
                    req.data = data;
                    $.ajax({
                        url : "/api/updateUserInfo",
                        dataType :"json",
                        contentType : "application/json",
                        data :JSON.stringify(req),
                        success : function (T2) {
                            if (T2.statusCode == 200){
                                $.toast("头像更新成功！");
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
});
var returnFlag = 0;
var keyId;
function isNull(title,str) {
    if ($.trim(str)==""){
       $.toast(title+"不能为空","forbidden");
       returnFlag = 1;
    }
}
function getUserInfo() {
    var req = {};
    req.token = sessionStorage.getItem("token");

    $.ajax({
       url : "/api/getUserInfo",
        contentType : "application/json",
        dataType : "json",
        type : "post",
        data : JSON.stringify(req),
        success : function (T) {
            if (T.statusCode==200 && T.data != null){
               var name = T.data.name;
               var nickname = T.data.nickname;
               var phone = T.data.phone;
               var email = T.data.email;
               var identity = T.data.identity;
               keyId = T.data.keyId;
               $("#name").val(name);
               $("#nickname").val(nickname);
               $("#phone").val(phone);
               $("#email").val(email);
               if (identity == 0){
                   $("#identity").val("家长/学生");
               } else{
                   $("#identity").val("教师");
               }
            } else{
               window.location = "/page/toLogin";
            }
        }
    });
}