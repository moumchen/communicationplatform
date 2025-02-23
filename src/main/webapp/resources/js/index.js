 $(function () {
        var GET_INDEX_INFO_URL = "/api/getIndexInfo";

        $.post(
            GET_INDEX_INFO_URL,
            {"token":""},
            function (data) {
                if(data.statusCode == 200){
                    parseImageAndInfo(data.data);
                }
            }
        );



    });
// <a href="javascript:void(0);" class="weui-media-box weui-media-box_appmsg">
//        <div class="weui-media-box__bd">
//                <h4 class="weui-media-box__title">欢迎使用</h4>
//                <p class="weui-media-box__desc">CUIT家校沟通平台，疫情期间为您坚守！</p>
//            </div>
//            </a>

//        <div class="swiper-slide"><img src="/resources/images/index_image_01.png" alt=""></div>

function parseImageAndInfo(data) {
    var imgAddStr="";
    var wordAddStr="";
    for (i=0; i<data.length;i++){
        if (data[i].kind == 0){
            imgAddStr += "<div class=\"swiper-slide\"><img src=\"/resources/images/"+data[i].img+"\" alt=\"\"></div>";
        }
        if (data[i].kind == 1){
            wordAddStr += "<a href=\"javascript:void(0);\" onclick='openDialog(\"text\",\""+data[i].title+"\",\""+data[i].content+"\")' class=\"weui-media-box weui-media-box_appmsg \"><div class=\"weui-media-box__bd\">";
            if (i==0){
            wordAddStr += "<h4 class=\"weui-media-box__title\" style='color:red'>[热]"+data[i].title+"</h4>";
            } else{
                wordAddStr += "<h4 class=\"weui-media-box__title\">"+data[i].title+"</h4>";
            }
            wordAddStr += "<p class=\"weui-media-box__desc \">"+data[i].content+"</p>";
            wordAddStr += "</div></a>";
        }
    }
    $(".swiper-wrapper").html(imgAddStr);
    $(".weui-panel__bd").html(wordAddStr);
    load();
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
             })
             break;

         default:
             break;

     }
 }

function load() {
    var mySwiper = new Swiper('.swiper-container',
        {
            speed:5000,//播放速度
            autoHeight:true,
            loop:true,//是否循环播放
            setWrapperSize:true,
            autoplay:
                {
                    delay: 5000,
                    disableOnInteraction: false,
                },
            pagination:  '.swiper-pagination',//分页
            effect : 'slide',//动画效果
        }
    );
}
