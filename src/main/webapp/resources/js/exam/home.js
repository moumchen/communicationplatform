var pageIndex = 1;
var pageSize = 5;
var pageMax = 0;
$(function () {
    // 教师用户显示新增按钮
    var identity = sessionStorage.getItem("identity");
    if (identity == "1"){
        $(".addButton").css("display","block");
        $("#analyseButton").css("display","none");
    }

    $(".addButton").click(function () {
        window.location = "/page/toAddExam";
    });
    var a = {};
    a.token = sessionStorage.getItem("token");
    a.pageIndex = pageIndex;
    a.pageSize = pageSize;
    // 获取当前用户的所在班级的全部考试！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
    $.ajax({
       url : "/api/getExamList",
        dataType :"json",
        contentType : "application/json",
        data : JSON.stringify(a),
        success : function (data) {
            if (data.statusCode == 200){
                if (data.data.length > 0){
                    $(".noneExams").css("display","none");
                }
                addItemToList(data.data);
            } else {
                $.toast(data.message,"forbidden");
                if (data.message == "未登录"){
                    window.location = "/page/toLogin";
                }
            }
        },
        type : "post"
    });

    $("#analyseButton").click(function () {
        if (choiceTagNumber <= 1){
            $.toast("请选择至少两个考试再进行区间分析","forbidden");
            return;
        }
        var tags = $(".choiceInput");
        var analysis_exam_ids = "";
        for (var i = 0; i < tags.length; i++){
            analysis_exam_ids += $(tags[i]).attr("examName");
            analysis_exam_ids +=",";
            analysis_exam_ids += $(tags[i]).attr("examId");
            analysis_exam_ids +=",";
            analysis_exam_ids += $(tags[i]).attr("maxScore");
            analysis_exam_ids +=","
            analysis_exam_ids += $(tags[i]).attr("averageScore");
            analysis_exam_ids +=","
            analysis_exam_ids += $(tags[i]).attr("score");
            analysis_exam_ids +="|";
        }
        sessionStorage.setItem("analysis_exam_ids",analysis_exam_ids.substring(0, analysis_exam_ids.length-1));
        window.location = "/page/toExamAnalysis";
    });
// <div class="weui-media-box weui-media-box_text content_item">
//         <h4 class="weui-media-box__title">标题一</h4>
//         <p class="weui-media-box__desc">由各种物质组成的巨型球状天体，叫做星球。星球有一定的形状，有自己的运行轨道。</p>
//     <ul class="weui-media-box__info">
//         <li class="weui-media-box__info__meta">文字来源</li>
//         <li class="weui-media-box__info__meta">时间</li>
//         <li class="weui-media-box__info__meta weui-media-box__info__meta_extra">其它信息</li>
//         </ul>
//         </div>

});

function addItemToList(data) {
    var str = $(".content").html();
    var identity = sessionStorage.getItem("identity");
    var date = new Date();
    for (var i=0; i<data.length; i++){
        var tempDate = new Date(data[i].exam.endTime);
        if (identity == 0) {
            str += "<div class=\"aui-card-list content_item\" style='width:95%;'>";
            if (data[i].score == null){
                str += "<input style=\"float: right;\" examId=\"" + data[i].exam.keyId + "\" class=\"aui-checkbox\" disabled=\"\" checked=\"\" type=\"radio\" name=\"checkbox\">";
            } else {
                str +=  "<div style=\"float: right;width:32px;height:32px;cursor:pointer;\" examName=\""+data[i].exam.examName+"\" averageScore=\""+data[i].exam.averageScore+"\" maxScore=\""+data[i].exam.maxScore+"\"  score=\""+data[i].score.score+"\" examId=\"" + data[i].exam.keyId + "\"  class=\"aui-checkbox\" onclick='choiceTag(this);'  type=\"radio\" name=\"checkbox\"></div>";
            }
            str += "  <div class=\"aui-card-list-header\">" + data[i].exam.examName + "</div>";
            str += " <div class=\"aui-card-list-content-padded\">";
            str += "科目：" + data[i].exam.subject;
            if (tempDate < date) {
                str += " 班平:" + data[i].exam.averageScore + " 最高分:" + data[i].exam.maxScore + " 参考人数:" + data[i].exam.num;
                if (data[i].score == null) {
                    str += " 暂无您的成绩信息!";
                } else {
                    str += " 您的分数是:" + data[i].score.score + " 排名:" + data[i].score.rank;
                }
                str += "</div><div class=\"aui-card-list-footer\"><div class=\"aui-label aui-label-warning\">已结束</div>";
            } else {
                str += "</div><div class=\"aui-card-list-footer\"><div class=\"aui-label aui-label-success\">未开始</div>"
            }
            str += "<br/><font>开始时间:" + data[i].exam.startTime.substring(0, 19) + "<br/> 结束时间:" + data[i].exam.endTime.substring(0, 19) + "</font>";
            str += "</div></div>";
        } else {
            // 教师用户
            str += "<div class=\"aui-card-list content_item_teacher\"  onclick=\"toModifyExam('"+data[i].exam.startTime+"','"+data[i].exam.endTime+"','"+data[i].exam.subject+"','"+data[i].exam.num+"','"+data[i].exam.examName+ "','" +data[i].exam.averageScore+ "','" +data[i].exam.maxScore+"','" +data[i].exam.keyId+"');\" style='width:95%;cursor:pointer;'>";
            str += "  <div class=\"aui-card-list-header\">" + data[i].exam.examName + "</div>";
            str += " <div class=\"aui-card-list-content-padded\">";
            str += "科目：" + data[i].exam.subject;
            if (tempDate < date) {
                str += " 班平:" + data[i].exam.averageScore + " 最高分:" + data[i].exam.maxScore + " 参考人数:" + data[i].exam.num;
                str += "</div><div class=\"aui-card-list-footer\"><div class=\"aui-label aui-label-warning\">已结束</div>";
            } else {
                str += "</div><div class=\"aui-card-list-footer\"><div class=\"aui-label aui-label-success\">未开始</div>";
            }
            str += "<br/><font>开始时间:" + data[i].exam.startTime.substring(0, 19) + "<br/> 结束时间:" + data[i].exam.endTime.substring(0, 19) + "</font>";
            str += "</div></div>";
        }
    }
    $(".content").html(str);

    

}
var choiceTagNumber = 0;
function choiceTag(obj) {
    var str1 = "aui-checkbox";
    var str2 = "aui-checkbox choiceInput aui-checked";
    if ($(obj).attr("isChoiced")== null || $(obj).attr("isChoiced") == "0") {
        choiceTagNumber++;
        $(obj).attr("isChoiced", "1");
        $(obj).attr("class",str2);
    } else {
        choiceTagNumber--;
        $(obj).attr("isChoiced", "0");
        $(obj).attr("class",str1);
    }

    if (choiceTagNumber > 0){
        $("#analyseButton").css("display","block");
    } else{
        $("#analyseButton").css("display","none");
    }
}

function toModifyExam(examStartTime,examEndTime,examSubject,examNum,examName,averageScore,maxScore,examId) {
    sessionStorage.setItem("examDetail_subject",examSubject);
    sessionStorage.setItem("examDetail_startTime",examStartTime);
    sessionStorage.setItem("examDetail_endTime",examEndTime);
    sessionStorage.setItem("examDetail_name",examName);
    sessionStorage.setItem("examDetail_num",examNum);
    sessionStorage.setItem("examDetail_averageScore",averageScore);
    sessionStorage.setItem("examDetail_maxScore",maxScore);
    sessionStorage.setItem("examDetail_id",examId);
    window.location = "/page/toExamDetail";
}