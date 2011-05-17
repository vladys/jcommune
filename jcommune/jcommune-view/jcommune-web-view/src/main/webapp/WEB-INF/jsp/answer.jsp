<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
</head>
<body>
	<spring:message code="label.answer" />
	<form:form action="${pageContext.request.contextPath}/answer.html"
		method="POST">
		<spring:message code="label.topic" />
		<input type="hidden" name="topicId" value="${topicId}" />
		<table border="2" width="100%">
			<tr>
				<td height="200"><spring:message code="label.text" /> <textarea
						name="bodytext" cols="40" rows="10"></textarea>
				</td>
			</tr>
		</table>
		<br>
		<input type="submit" value="<spring:message code="label.answer"/>" />
	</form:form>
</body>
</html>