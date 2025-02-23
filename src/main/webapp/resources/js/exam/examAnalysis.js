$(function () {

    $("#backButton").click(function () {
       window.location = "/page/toExamHome";
    });

    var myChart = echarts.init(document.getElementById('showDiv'));
    var analysis_exam_ids = sessionStorage.getItem("analysis_exam_ids");
    var list = analysis_exam_ids.split("|");
    var types = new Array();
    var averages = new Array();
    var yours = new Array();
    var maxs = new Array();
    for (var i = 0; i < list.length; i++){
        var currentData = list[i].split(",");
        types[i] = currentData[0];
        maxs[i] = currentData[2];
        averages[i] = currentData[3];
        yours[i] = currentData[4];
    }



    option = {
        title: {
            text: '成绩区间分析'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['班级平均分', '您的分数', '最高分']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: types
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '班级平均分',
                type: 'line',
                data: averages
            },
            {
                name: '您的分数',
                type: 'line',
                data: yours
            },
            {
                name: '最高分',
                type: 'line',
                data: maxs
            }
        ]
    };
    myChart.setOption(option);
});