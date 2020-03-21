/**
 * 提交回复
 */
function post() {
    var questionId = $("#questionId").val();
    var content = $("#content").val();
    comment2target(questionId,1,content)
}
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId,2,content)
}
function comment2target(targetId,type,content) {
    if (!content){
        alert("您回复的内容为空嗷");
        return;
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 4000) {
                window.location.reload();
            } else if(response.code == 4003){
                var message = response.message;
                if (message) {
                    alert(response.message);
                    window.open("https://github.com/login/oauth/authorize?client_id=a40fc1f721f375da254f&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                    window.localStorage.setItem("closable", true);
                }
            }
            alert(response.message);
        },
        dataType: "json"
    });
}

function like(e) {
    var id = e.getAttribute("data-id")

    if (e.getAttribute("msg")==1){
       $.ajax({
           type: "POST",
           contentType: "application/json",
           url: "/comment",
           data: JSON.stringify({
               "id": id,
               "msg":0
           }),
           success: function (response) {
               var c = response.t
               $("#count").html(c)
           },
           dataType: "json"
       });
        e.setAttribute("msg",0)
        e.classList.remove("active");
   }else {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/comment",
        data: JSON.stringify({
            "id": id,
            "msg":1
        }),
        success: function (response) {
            var c = response.t
            $("#count").html(c)
        },
        dataType: "json"
    });
    e.setAttribute("msg",1)
    e.classList.add("active")
    }
}

//展开二级回复
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $('#commentDao-'+id);
    // comments.toggleClass("in")
    // $("#active").toggleClass("active")
    //获取
   var collapse = e.getAttribute("data-collapse");
   if (collapse){
       //折叠二级评论
       comments.removeClass("in");
       e.removeAttribute("data-collapse");
       e.classList.remove("active");
   }else {
       var subCommentContainer = $("#commentDao-"+id);
       if (subCommentContainer.children().length!==1){
           //展开二级评论
           comments.addClass("in");
           //标记二级评论的展开状态
           e.setAttribute("data-collapse","in");
           e.classList.add("active");
       }else {
           $.getJSON("/comment/"+id,function (data) {
               $.each(data.t.reverse(), function (index, commentDto) {
                   var mediaLeftElement = $("<div/>", {
                       "class": "media-left"
                   }).append($("<img/>", {
                       "class": "media-object img-rounded",
                       "src": commentDto.user.avatarUrl
                   }));
                   var mediaBodyElement = $("<div/>", {
                       "class": "media-body"
                   }).append($("<h5/>", {
                       "class": "media-heading",
                       "html": commentDto.user.name
                   })).append($("<div/>", {
                       "style":"margin-top: 14px",
                       "html": commentDto.content
                   })).append($("<span/>", {
                       "class": "pull-right",
                       "style": "color: #999",
                       "html": moment(commentDto.gmtCreate).format('YYYY-MM-DD h:mm')
                   }));
                   var mediaElement = $("<div/>", {
                       "class": "media"
                   }).append(mediaLeftElement)
                       .append(mediaBodyElement);
                   var commentElement = $("<div/>", {
                       "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comment"
                   }).append(mediaElement);
                   subCommentContainer.prepend(commentElement);
                    });
               //展开二级评论
               comments.addClass("in");
               //标记二级评论的展开状态
               e.setAttribute("data-collapse", "in");
               e.classList.add("active");

           })

   }
}
}