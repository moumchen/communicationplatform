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

    $("#addButton").click(function () {
       sessionStorage.setItem("info_isChange","0");
       window.location = "/page/toAddAnnounce";
    });

});

function getItem(pageIndex,pageSize) {
    var req = {};
    req.token =sessionStorage.getItem("admin_token");
    req.pageIndex = pageIndex;
    req.pageSize = pageSize;
    // 获取用户列表
    $.ajax({
        url : "/api/getIndexInfoByPage",
        contentType:"application/json",
        dataType :"json",
        type:"post",
        data : JSON.stringify(req),
        success : function (T) {
            if (T.statusCode == 200){
                $(".layui-laypage-count").html("共 "+T.totalCount+" 条");
                count = T.totalCount;
                var str = "";
                for (var i = 0; i < T.data.length; i++){
                    str+="<tr><td>"+T.data[i].title+"</td>";
                    if (T.data[i].kind == 0){
                        str += "<td>/</td><td>轮播</td><td><img src='/resources/images/"+T.data[i].img+"'</td>"
                    } else{
                        str += "<td>"+T.data[i].content+"</td><td>公告</td><td>/</td>"
                    }
                    str += "<td>"+T.data[i].addTime.substring(0,19)+"</td>";
                    str += "<td>"+T.data[i].modifyTime.substring(0,19)+"</td>";
                    str += "<td>"+T.data[i].rank+"</td>";
                    str += "<td><a style='cursor:pointer;color:blue' onclick='changeInfo(this);' rank='"+T.data[i].rank+"' type='"+T.data[i].kind+"' content='"+T.data[i].content+"' img='"+T.data[i].img+"' title='"+T.data[i].title+"' infoId='"+T.data[i].keyId+"'>修改</a> <a onclick='deleteInfo(this);' style='cursor:pointer;color:blue' infoId='"+T.data[i].keyId+"'>删除</a></td>";
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


function changeInfo(obj) {
    var  infoId = $(obj).attr("infoId");
    var  title = $(obj).attr("title");
    var  content = $(obj).attr("content");
    var  kind = $(obj).attr("type");
    var  rank = $(obj).attr("rank");
    var  img = $(obj).attr("img");
    sessionStorage.setItem("info_id",infoId);
    sessionStorage.setItem("info_title",title);
    sessionStorage.setItem("info_content",content);
    sessionStorage.setItem("info_kind",kind);
    sessionStorage.setItem("info_rank",rank);
    sessionStorage.setItem("info_img",img);
    sessionStorage.setItem("info_isChange",1);
    window.location = "/page/toAddAnnounce";
}

function deleteInfo(obj) {
   var  infoId = $(obj).attr("infoId");
   var req = {};
   req.token =sessionStorage.getItem("admin_token");
   var ds = [];
   var data = {};
   data.isDelete = 1;
   data.keyId = infoId;
   ds[0] = data;
   req.data =ds;

   $.ajax({
      url : "/api/updateIndexInfos",
       contentType:"application/json",
       dataType :"json",
       type:"post",
       data : JSON.stringify(req),
       success : function (T) {
            if (T.statusCode == 200){
                alert("删除成功!");
                window.location.reload();
            } else{
                alert(T.message);
            }
       }
   });
}