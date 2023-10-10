<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>日記システム</title>
</head>
<body>
	<form action="/diary/AuthServlet" method="post">
		email：<input type="text" name="email"><br>
		パスワード：<input type="password" name="password"><br>
		<input type="hidden" name="action" value="login">
		<input type="submit" value="ログイン">
	</form>
</body>
</html>