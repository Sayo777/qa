$(function(){
    $("#btn").click(publish);
});

function publish() {
    // $("#publishModal").modal("hide");
    //获取标题和内容，异步请求
    var enterpriseName = $("#enterpriseName").val();
     var legalPerson = $("#legalPerson").val();
     var commercialNumber = $("#commercialNumber").val();
     var creditCode = $("#creditCode").val();
     // var type = $("#type").val();
    var type = $("input[name=type]:checked").val();
     var scope = $("#scope").val();
     var status = $("#status").val();
     var organCode = $("#organCode").val();
     var enterpriseEmail = $("#enterpriseEmail").val();
     var website = $("#website").val();
     var auditDate = $("#hello").val();
     var address = $("#address").val();
    // var content=$("#message-text").val();
    var province = myForm.s_province.value;
    var city = myForm.s_city.value;
    var county = myForm.s_county.value;
    var license ="http://q5qcypm7u.bkt.clouddn.com/"+$("#key").val();
    //发送异步请求
    $.post(
        "/qa/customer/eApply",
        {"enterpriseName":enterpriseName,"province":province,"city":city,"county":county,"auditDate":auditDate,
            "legalPerson":legalPerson,"commercialNumber":commercialNumber,
        "creditCode":creditCode,"type":type,"scope":scope,"status":status,
            "organCode":organCode,"enterpriseEmail":enterpriseEmail,
        "website":website,"address":address,"license":license},
        function (data) {
            data = $.parseJSON(data);
            alert(data.msg);
            //在提示框当中显示返回的消息
            $("#hintBody").text(data.msg);
            // 提示框显示
            $("#hintModal").modal("show");
            //2s后自动隐藏提示框
            setTimeout(function(){
                $("#hintModal").modal("hide");
                //刷新页面
                if (data.code == 0){
                    window.location.reload();
                }
            }, 2000);
        }
    );


}