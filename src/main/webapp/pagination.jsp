<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ol class="pagination" style="margin-top: 0.25rem; margin-bottom: 0.25rem; list-style: none;">
	<c:forEach begin="1" end="${requestScope.totalPage}" step="1" varStatus="status">
		<li style="display: inline; width: 0.75rem; text-align: center;">
		<a href="ArticleServlet?action=pagination&total=${requestScope.totalPage}&page=${status.count}">&nbsp;${status.count}&nbsp;</a>
		</li>
	</c:forEach>
</ol>
