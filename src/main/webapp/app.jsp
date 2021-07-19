<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme()+"://"+
            request.getServerName()+":"+request.getServerPort()+
            request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script>
        $.ajax({
            url:"",
            data:{

            },
            type:"",
            dataType:"json",
            success:function (data) {

            }
        })
    </script>
</head>
<body>
        <form action="login.do" method="post">
            <input type="text" name="loginAct"><br>
            <input type="password" name="loginPwd"><br>
            <input type="submit" value="Go">
        </form>
<p>${msg}</p>
</body>
</html>
