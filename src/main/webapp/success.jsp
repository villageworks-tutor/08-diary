<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>日記システム</title>
</head>
<body>
	${sessionScope.profile.name}さんの日記
	<hr>
	<table border="1">
		<tr>
			<th>記事番号</th>
			<th>タイトル</th>
			<th colspan="2">操作</th>
		</tr>
		<c:forEach items="${requestScope.articleList}" var="article">
			<tr>
				<td>
					<a href="/diary/LoginServlet?action=showOne&id=${article.id}">${article.id}</a>
			    </td>
			    <td>${article.title}</td>
			    <td>
				    <form action="/diary/LoginServlet" method="post">
				    	<input type="submit" value="削除">
				    	<input type="hidden" name="action" value="delete">
				    	<input type="hidden" name="id" value="${article.id}">
				    	<input type="hidden" name="userId" value="${sessionScope.profile.id}">
				    </form>
			    </td>
			    <td>
				    <form action="/diary/LoginServlet" method="post">
				    	<input type="submit" value="編集">
				    	<input type="hidden" name="action" value="edit">
				    	<input type="hidden" name="id" value="${article.id}">
				    </form>
			    </td>
			</tr>
		</c:forEach>
	</table>
	<hr>
	<form action="/diary/LoginServlet" method="post">
		タイトル：<input type="text" name="title"><br>
		内容：<input type="text" name="content" size="50"><br>
		<input type="hidden" name="action" value="create">
		<input type="hidden" name="userId" value="${sessionScope.profile.id}">
		<input type="submit" value="記事投稿">
	</form>
	<hr>
	<a href="/diary/LoginServlet?action=logout">ログアウト</a>
</body>
</html>