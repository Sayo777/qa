function previewImage(file) {
    $.ajax({
        url: "http://upload-z0.qiniup.com",
        method: "post",
        processData: false,
        contentType: false,
        data: new FormData($("#form1")[0]),
        success: function(data) {
            if(data && data.code == 0) {
            } else {
                alert("fail");
            }
        }
    });
    var MAXWIDTH = 260;
    var MAXHEIGHT = 180;
    var div = document.getElementById('preview');
    if (file.files && file.files[0]) {
        var img = document.getElementById('img_notification');
        img.onload = function () {
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.width = rect.width;
            img.height = rect.height;
            img.style.marginTop = rect.top + 'px';
        }
        var reader = new FileReader();
        reader.onload = function (evt) {
            img.src = evt.target.result;
        }
        reader.readAsDataURL(file.files[0]);
    }
}
function previewImage2(file) {
    var formData = new FormData($("#form1")[0])
    formData.delete('file');
    formData.delete('token');
    formData.delete('key');
    formData.set('file',document.querySelector("#form1 input[id=undertaking]").files[0]);
    formData.set('token',$(" #token2").val());
    formData.set('key',$(" #key2").val());
    $.ajax({
        url: "http://upload-z0.qiniup.com",
        method: "post",
        processData: false,
        contentType: false,
        data:formData,
        success: function(data) {
            if(data && data.code == 0) {
            } else {
                alert("上传失败!");
            }
        }
    });
    var MAXWIDTH = 260;
    var MAXHEIGHT = 180;
    var div = document.getElementById('preview2');
    if (file.files && file.files[0]) {
        var img = document.getElementById('img_undertaking');
        img.onload = function () {
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.width = rect.width;
            img.height = rect.height;
            img.style.marginTop = rect.top + 'px';
        }
        var reader = new FileReader();
        reader.onload = function (evt) {
            img.src = evt.target.result;
        }
        reader.readAsDataURL(file.files[0]);
    }
}
function previewImage3(file) {
    var formData = new FormData($("#form1")[0])
    formData.delete('file2');
    formData.delete('token2');
    formData.delete('key2');
    formData.set('file',document.querySelector("#form1 input[id=inspectorsPhoto]").files[0]);
    formData.set('token',$(" #token3").val());
    formData.set('key',$(" #key3").val());
    $.ajax({
        url: "http://upload-z0.qiniup.com",
        method: "post",
        processData: false,
        contentType: false,
        data:formData,
        success: function(data) {
            if(data && data.code == 0) {
            } else {
                alert("上传失败!");
            }
        }
    });

    var MAXWIDTH = 260;
    var MAXHEIGHT = 180;
    var div = document.getElementById('preview3');
    if (file.files && file.files[0]) {
        var img = document.getElementById('img_inspectorsPhoto');
        img.onload = function () {
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.width = rect.width;
            img.height = rect.height;
            img.style.marginTop = rect.top + 'px';
        }
        var reader = new FileReader();
        reader.onload = function (evt) {
            img.src = evt.target.result;
        }
        reader.readAsDataURL(file.files[0]);
    }
}
function previewImage4(file) {
    var formData = new FormData($("#form1")[0])
    formData.delete('file3');
    formData.delete('token3');
    formData.delete('key3');
    formData.set('file',document.querySelector("#form1 input[id=environment1]").files[0]);
    formData.set('token',$(" #token4").val());
    formData.set('key',$(" #key4").val());
    $.ajax({
        url: "http://upload-z0.qiniup.com",
        method: "post",
        processData: false,
        contentType: false,
        data:formData,
        success: function(data) {
            if(data && data.code == 0) {
            } else {
                alert("上传失败!");
            }
        }
    });

    var MAXWIDTH = 260;
    var MAXHEIGHT = 180;
    var div = document.getElementById('preview4');
    if (file.files && file.files[0]) {
        var img = document.getElementById('img_environment1');
        img.onload = function () {
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.width = rect.width;
            img.height = rect.height;
            img.style.marginTop = rect.top + 'px';
        }
        var reader = new FileReader();
        reader.onload = function (evt) {
            img.src = evt.target.result;
        }
        reader.readAsDataURL(file.files[0]);
    }
}
function previewImage5(file) {
    var formData = new FormData($("#form1")[0])
    formData.delete('file4');
    formData.delete('token4');
    formData.delete('key4');
    formData.set('file',document.querySelector("#form1 input[id=environment2]").files[0]);
    formData.set('token',$(" #token5").val());
    formData.set('key',$(" #key5").val());
    $.ajax({
        url: "http://upload-z0.qiniup.com",
        method: "post",
        processData: false,
        contentType: false,
        data:formData,
        success: function(data) {
            if(data && data.code == 0) {
            } else {
                alert("上传失败!");
            }
        }
    });
    var MAXWIDTH = 260;
    var MAXHEIGHT = 180;
    var div = document.getElementById('preview5');
    if (file.files && file.files[0]) {
        var img = document.getElementById('img_environment2');
        img.onload = function () {
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.width = rect.width;
            img.height = rect.height;
            img.style.marginTop = rect.top + 'px';
        }
        var reader = new FileReader();
        reader.onload = function (evt) {
            img.src = evt.target.result;
        }
        reader.readAsDataURL(file.files[0]);
    }
}
function previewImage6(file) {
    var formData = new FormData($("#form1")[0])
    formData.delete('file5');
    formData.delete('token5');
    formData.delete('key5');
    formData.set('file',document.querySelector("#form1 input[id=license]").files[0]);
    formData.set('token',$(" #token6").val());
    formData.set('key',$(" #key6").val());
    $.ajax({
        url: "http://upload-z0.qiniup.com",
        method: "post",
        processData: false,
        contentType: false,
        data:formData,
        success: function(data) {
            if(data && data.code == 0) {
            } else {
                alert("上传失败!");
            }
        }
    });
    var MAXWIDTH = 260;
    var MAXHEIGHT = 180;
    var div = document.getElementById('preview6');
    if (file.files && file.files[0]) {
        var img = document.getElementById('img_license');
        img.onload = function () {
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.width = rect.width;
            img.height = rect.height;
            img.style.marginTop = rect.top + 'px';
        }
        var reader = new FileReader();
        reader.onload = function (evt) {
            img.src = evt.target.result;
        }
        reader.readAsDataURL(file.files[0]);
    }
}

function clacImgZoomParam(maxWidth, maxHeight, width, height) {
    var param = {top: 0, left: 0, width: width, height: height};
    if (width > maxWidth || height > maxHeight) {
        rateWidth = width / maxWidth;
        rateHeight = height / maxHeight;

        if (rateWidth > rateHeight) {
            param.width = maxWidth;
            param.height = Math.round(height / rateWidth);
        } else {
            param.width = Math.round(width / rateHeight);
            param.height = maxHeight;
        }
    }

    param.left = Math.round((maxWidth - param.width) / 2);
    param.top = Math.round((maxHeight - param.height) / 2);
    return param;
}

