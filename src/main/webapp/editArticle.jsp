<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>日記システム</title>
</head>
<body>
	<form action="/diary/ArticleServlet" method="post">
		タイトル：<input type="text" name="title" value="${article.title}"><br>
		内容：<input type="text" name="content" size="50" value="${article.content}"><br>
		<input type="hidden" name="id" value="${article.id}">
		<input type="hidden" name="userId" value="${profile.id}">
		<input type="hidden" name="action" value="update">
		<input type="submit" value="変更">
	</form>
</body>
</html>