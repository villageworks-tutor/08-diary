<%@page import="java.sql.Timestamp"%>
<%@page import="diary.bean.ArticleBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
List<ArticleBean> articleList = new ArrayList<ArticleBean>();
articleList.add(new ArticleBean(1, "研修開始1", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-04-01 00:00:00"), 1));
articleList.add(new ArticleBean(2, "IT 基礎1", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-04-02 00:00:00"), 1));
articleList.add(new ArticleBean(3, "プログラミング1", "条件分岐や繰り返し処理によって幅が広がってきました", Timestamp.valueOf("2022-04-12 00:00:00"), 1));
articleList.add(new ArticleBean(6, "研修開始2", "これから技術を習得してエンジニアを目指します！", Timestamp.valueOf("2022-05-01 00:00:00"), 1));
articleList.add(new ArticleBean(7, "IT 基礎2", "インターネットの仕組みとデータの流れを勉強しました", Timestamp.valueOf("2022-05-02 00:00:00"), 1));
request.setAttribute("articleList", articleList);
request.setAttribute("totalPage", 4);
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>日記システム</title>
</head>
<body>
	${sessionScope.profile.name}さんの日記
	<hr>
	<form action="ArticleServlet" method="post">
	<table border="0">
		<tr>
			<td>キーワード</td>
			<td>
				<input type="text" name="keyword" value="${requestScope.condition.keyword}">
				<input type="hidden" name="userId" value="${sessionScope.profile.id}">
				<button type="submit" name="action" value="search">検索</button>
			</td>
		</tr>
	</table>
	</form>
	<hr>
	<table border="1">
		<tr>
			<th>記事番号</th>
			<th>タイトル</th>
		    <c:if test="${!empty requestScope.condition}">
		    <th>内容</th>
		    </c:if>
			<th colspan="2">操作</th>
		</tr>
		<c:forEach items="${requestScope.articleList}" var="article">
			<tr>
				<td>
					<a href="/diary/ArticleServlet?action=detail&id=${article.id}">${article.id}</a>
			    </td>
			    <td>${article.title}</td>
			    <c:if test="${!empty requestScope.condition}">
			    <td>${article.content}</td>
			    </c:if>
			    <td>
				    <form action="/diary/ArticleServlet" method="post">
				    	<input type="submit" value="削除">
				    	<input type="hidden" name="action" value="delete">
				    	<input type="hidden" name="id" value="${article.id}">
				    	<input type="hidden" name="userId" value="${sessionScope.profile.id}">
				    </form>
			    </td>
			    <td>
				    <form action="/diary/ArticleServlet" method="post">
				    	<input type="submit" value="編集">
				    	<input type="hidden" name="action" value="edit">
				    	<input type="hidden" name="id" value="${article.id}">
				    </form>
			    </td>
			</tr>
		</c:forEach>
		<caption style="text-align: left">
			<jsp:include page="pagination.jsp" />
		</caption>
	</table>
	<jsp:include page="pagination.jsp" />
	<hr>
	<form action="/diary/ArticleServlet" method="post">
		タイトル：<input type="text" name="title"><br>
		内容：<input type="text" name="content" size="50"><br>
		<input type="hidden" name="action" value="post">
		<input type="hidden" name="userId" value="${sessionScope.profile.id}">
		<input type="submit" value="記事投稿">
	</form>
	<hr>
	<a href="/diary/AuthServlet?action=logout">ログアウト</a>
</body>
</html>