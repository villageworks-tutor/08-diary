<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>日記システム</title>
</head>
<body>
	<table border="1">
		<tr>
			<th>記事番号</th>
			<td>${requestScope.article.id}</td>
		</tr>
		<tr>
			<th>タイトル</th>
			<td>${requestScope.article.title}</td>
		</tr>
		<tr>
			<th>内容</th>
			<td>${requestScope.article.content}</td>
		</tr>
		<tr>
			<th>作成時間</th>
			<td>${requestScope.article.createdAt}</td>
		</tr>
	</table>
	<br>
	<!--
	 TODO: ここではjavaScriptのhistory.back()関数を利用しているが、Servletを呼び出して処理しても構わない。
	       余力がある場合は、servletで書き換えてみよう。
	 -->
	<button type="button" onclick="history.back()">戻る</button>
</body>
</html>