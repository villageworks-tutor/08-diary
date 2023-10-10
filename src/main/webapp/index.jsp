<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 
	以下のような jsp:forward タグと jsp:param タグを使うことで「AuthServlet?action=top」と同じ処理が実現できる。
	回答のコード例では当該JSPは含まれていないが、当該index.jspを作成することで「http:8080/diary/」でアクセスすることができるようになる。
	当該JSPは
		当該サイトに「http://localhost:8080/diary」でアクセス可能にすること
	を目的としている。
	また、クエリー文字列のactionキー値「top」に対応する処理は新規追加である。
 -->
<jsp:forward page="LoginServlet">
	<jsp:param name="action" value="top"/>
</jsp:forward>