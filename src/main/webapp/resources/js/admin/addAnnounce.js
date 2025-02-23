$(function () {
    var isChange = sessionStorage.getItem("info_isChange");
    if (isChange == "1"){
        // 修改
        $(".typeChoice").css("display","none");
        $("#title").val(sessionStorage.getItem("info_title"));
        if (sessionStorage.getItem("info_kind") == "0"){
            $(".content").css("display","none");
        } else{
            $(".img").css("display","none");
            $("#content").val(sessionStorage.getItem("info_content"));
        }
        $("#rank").val(sessionStorage.getItem("info_rank"));
    } else{
        layui.use('form', function(){
            var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
            form.render();
        });

    }

    $("#submitButton").click(function () {
        var title = $("#title").val();
        var rank = $("#rank").val();
        if (isChange == 1){
            var keyId = sessionStorage.getItem("info_id");
            // 修改
            var req = {};
            req.token = sessionStorage.getItem("admin_token");
            var data = [];
            var item = {};
            item.keyId = keyId;
            item.title = title;
            item.rank = rank;
            if(sessionStorage.getItem("info_kind") == 0){
                // 轮播
                // 更新内容 再上传图片更新
                data[0] = item;
                req.data =data;
                $.ajax({
                    url : "/api/updateIndexInfos",
                    contentType:"application/json",
                    dataType :"json",
                    type:"post",
                    data : JSON.stringify(req),
                    success : function (T) {
                        if (T.statusCode == 200){
                            if ($("#file").val() != null){
                                var formData = new FormData();
                                var img_file = document.getElementById("file");
                                var fileObj = img_file.files[0];
                                formData.append("file",fileObj);
                                $.ajax({
                                    url: "/api/uploadInfoImg",
                                    type: "post",
                                    data: formData,
                                    async: false,
                                    processData: false,
                                    contentType: false,
                                    success: function (T) {
                                        if (T.statusCode == 200) {
                                            var req2 = {};
                                            req2.token = sessionStorage.getItem("admin_token");
                                            var data2 = [];
                                            var item2 = {};
                                            item2.keyId = keyId;
                                            item2.img = T.data;
                                            data2[0] = item2;
                                            req2.data = data2;
                                            $.ajax({
                                                url: "/api/updateIndexInfos",
                                                contentType: "application/json",
                                                dataType: "json",
                                                type: "post",
                                                data: JSON.stringify(req2),
                                                success: function (T) {
                                                    alert("更新成功!");
                                                    window.location = "/page/toAnnouncementAdmin";
                                                }
                                            });
                                        }
                                    }
                                });
                            }else{
                                alert(T.message);
                            }
                        }
                    }
                });
             } else{
                // 公告
                var content = $("#content").val();
                item.content = content;
                data[0] = item;
                req.data = data;
                $.ajax({
                    url: "/api/updateIndexInfos",
                    contentType: "application/json",
                    dataType: "json",
                    type: "post",
                    data: JSON.stringify(req),
                    success: function (T) {
                        if (T.statusCode == 200) {
                            alert("更新成功");
                            window.location = "/page/toAnnouncementAdmin";
                        }else{
                            alert(T.message);
                        }
                    }

                });
            }
        } else{
            // 【添加】
            var title = $("#title").val();
            var rank = $("#rank").val();
            var req = {};
            req.token = sessionStorage.getItem("admin_token");
            var data = [];
            var item = {};
            item.title = title;
            item.rank = rank;
              if ( $($(".layui-this")[1]).html() == "轮播（不需输入内容）"){
                    item.kind = 0;
                    data[0] = item;
                    req.data = data;
                    $.ajax({
                        url: "/api/addIndexInfos",
                        contentType: "application/json",
                        dataType: "json",
                        type: "post",
                        data: JSON.stringify(req),
                        success: function (T) {
                            var currentKeyid = T.data;
                            if ($("#file").val() != null) {
                                var formData = new FormData();
                                var img_file = document.getElementById("file");
                                var fileObj = img_file.files[0];
                                formData.append("file", fileObj);
                                $.ajax({
                                    url: "/api/uploadInfoImg",
                                    type: "post",
                                    data: formData,
                                    async: false,
                                    processData: false,
                                    contentType: false,
                                    success: function (T) {
                                        if (T.statusCode == 200) {
                                            var req2 = {};
                                            req2.token = sessionStorage.getItem("admin_token");
                                            var data2 = [];
                                            var item2 = {};
                                            item2.keyId = currentKeyid;
                                            item2.img = T.data;
                                            data2[0] = item2;
                                            req2.data = data2;
                                            $.ajax({
                                                url: "/api/updateIndexInfos",
                                                contentType: "application/json",
                                                dataType: "json",
                                                type: "post",
                                                data: JSON.stringify(req2),
                                                success: function (T) {
                                                    alert("更新成功!");
                                                    window.location = "/page/toAnnouncementAdmin";
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
              }else{
                // 公告
                  var content = $("#content").val();
                  item.content = content;
                  item.kind= 1;
                  data[0] = item;
                  req.data = data;
                  $.ajax({
                      url: "/api/addIndexInfos",
                      contentType: "application/json",
                      dataType: "json",
                      type: "post",
                      data: JSON.stringify(req),
                      success: function (T) {
                          if (T.statusCode == 200) {
                              alert("添加成功");
                              window.location = "/page/toAnnouncementAdmin";
                          }else{
                              alert(T.message);
                          }
                      }
                  });
              }

        }
    });

});
