$(function () {

    getItem(1,10);


    $("#selectPageSize").change(function () {
        currentPageSize = $("#selectPageSize").val();
        currentPageIndex = 1;
        getItem(currentPageIndex,currentPageSize);
    });

    $("#skipPageButton").click(function () {
        skipPage();
    });

});

function getItem(pageIndex,pageSize) {
    var req = {};
    req.token =sessionStorage.getItem("admin_token");
    req.pageIndex = pageIndex;
    req.pageSize = pageSize;
    // 获取用户列表
    $.ajax({
        url : "/api/getAllUserInfo",
        contentType:"application/json",
        dataType :"json",
        type:"post",
        data : JSON.stringify(req),
        success : function (T) {
            if (T.statusCode == 200){
                $(".layui-laypage-count").html("共 "+T.data.totalCount+" 条");
                count = T.data.totalCount;
                var str = "";
                for (var i = 0; i < T.data.result.length; i++){

                    str+="<tr><td>"+T.data.result[i].auth.username+"</td><td>******</td>";
                    if (T.data.result[i].user.identity == 0){
                        str += "<td>学生/家长</td>"
                    } else if (T.data.result[i].user.identity == 1){
                        str += "<td>教师</td>"
                    } else{
                        str += "<td>管理员用户</td>"
                    }
                    str+="<td>"+T.data.result[i].user.name+"</td><td>"+T.data.result[i].user.nickname+"</td>";
                    str+="<td>"+T.data.result[i].user.email+"</td><td>"+T.data.result[i].user.phone+"</td>";
                    if (T.data.result[i].user.identity != 2){
                        if (T.data.result[i].auth.isLock == 0){
                        str += "<td>正  常</td>"
                        str+="<td><a style='cursor:pointer;color:blue' authId='"+T.data.result[i].auth.keyId+"' onclick='lockUser(this);'>锁定</a></td></tr>"
                        } else{
                            str += "<td>已锁定</td>"
                            str+="<td><a style='cursor:pointer;color:blue' authId='"+T.data.result[i].auth.keyId+"' onclick='unlockUser(this);'>解锁</a></td></tr>"
                        }
                    } else{
                        str += "<td>正  常</td>"
                        str+="<td>不允许操作</tr>"
                    }
                }
                $("#trContent").html(str);

            }
        }


    });


}

var currentPageIndex = 1;
var currentPageSize = 10;
var count = 0;
function toNextPage() {
    var maxPage = Math.ceil(count/currentPageSize);
    if (currentPageIndex == maxPage){
        return;
    }
    currentPageIndex = currentPageIndex + 1;
    if (currentPageIndex == maxPage){
        $("#toNextPageTag").attr("class","layui-laypage-prev layui-disabled");
    } else{
        $("#toNextPageTag").attr("class","layui-laypage-prev");
    }
    if (currentPageIndex < maxPage){
        getItem(currentPageIndex, currentPageSize);
    } else{
        getItem(maxPage, currentPageSize);
    }

    $("#toPrePageTag").attr("class","layui-laypage-prev");

}

function toPrePage() {
    if (currentPageIndex != 1){
        currentPageIndex = currentPageIndex - 1;
    } else {
        return;
    }
    getItem(currentPageIndex, currentPageSize);
    $("#toNextPageTag").attr("class","layui-laypage-next");
    if (currentPageIndex == 1){
        $("#toPrePageTag").attr("class","layui-laypage-prev layui-disabled");
    } else {
        $("#toPrePageTag").attr("class","layui-laypage-prev");

    }
}

function skipPage() {
    var page = $("#skipPageInput").val();
    var maxPageSize = Math.ceil(count / currentPageSize);
    if (page > maxPageSize){
        page = maxPageSize;
    }
    getItem(page,currentPageSize);
}

function lockUser(tag) {
    var authId = $(tag).attr("authId");
    var req = {};
    req.token = sessionStorage.getItem("admin_token");
    var data = {};
    data.keyId = authId;
    data.isLock = 1;
    req.data = data;

    $.ajax({
       url : "/api/updateAuth",
        contentType:"application/json",
        dataType :"json",
        type:"post",
        data : JSON.stringify(req),
        success : function (T) {
           if (T.statusCode == 200){
               alert("锁定成功!");
               window.location.reload();
           }else{
               alert(T.message);
           }

        }
    });
}
function unlockUser(tag) {
    var authId = $(tag).attr("authId");
    var req = {};
    req.token = sessionStorage.getItem("admin_token");
    var data = {};
    data.keyId = authId;
    data.isLock = 0;
    req.data = data;

    $.ajax({
        url : "/api/updateAuth",
        contentType:"application/json",
        dataType :"json",
        type:"post",
        data : JSON.stringify(req),
        success : function (T) {
            if (T.statusCode == 200){
                alert("解锁成功");
                window.location.reload();
            }else{
                alert(T.message);
            }

        }
    });
}