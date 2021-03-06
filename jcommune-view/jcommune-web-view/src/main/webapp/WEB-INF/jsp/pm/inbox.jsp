<%--

    Copyright (C) 2011  JTalks.org Team
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="jtalks" uri="http://www.jtalks.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<head>
        <script language="javascript"
            src="${pageContext.request.contextPath}/resources/javascript/custom/tableStylish.js"></script>
        <script language="javascript"
          src="${pageContext.request.contextPath}/resources/javascript/custom/privateMessages.js"></script>
    <title><spring:message code="label.inbox"/></title>
</head>
<body>

<div class="wrap pm_page">
    <jsp:include page="../../template/topLine.jsp"/>
    <jsp:include page="../../template/logo.jsp"/>

    <div class="all_forums">
        <h2><a class="heading" href="#"><spring:message code="label.inbox"/></a></h2>
        <jsp:include page="../../template/pmNavigationMenu.jsp"/>
        <table class="messages">
          <tr class="head">
                  <th class="pm_header_check"><input type = "checkbox" class="check_all"/></th>
                  <th class="pm_header_info"><spring:message code="label.sender"/></th>
                  <th class="pm_header_title"><spring:message code="label.pm.title"/></th>
                  <th class="pm_header_info"><spring:message code="label.sending_date"/></th>
          </tr>
          <c:choose>
            <c:when test="${!(empty pmList)}">
                <c:forEach var="pm" items="${pmList}">
                    <c:choose>
                        <c:when test="${pm.read}">
                          <tr id="${pm.id}" class="mess read">
                        </c:when>
                        <c:otherwise>
                          <tr id="${pm.id}" class="mess">
                        </c:otherwise>
                    </c:choose>
                    <td><input type = "checkbox" id = "${pm.id}" class="checker"/></td>
                    <td>
                      <a href="${pageContext.request.contextPath}/users/${pm.userFrom.encodedUsername}">
                        <c:out value="${pm.userFrom.username}"/>
                      </a>
                    </td>
                    <td class="title">
                      <a href="${pageContext.request.contextPath}/pm/${pm.id}">
                        <c:out value="${pm.title}"/></a>
                    </td>
                    <td>
                       <jtalks:format value="${pm.creationDate}"/>
                    </td>
                </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
              <tr class="forum_row">
                <td colspan="4"><spring:message code="label.inbox.empty"/></td>
              </tr>
            </c:otherwise>
          </c:choose>
        </table>
        <div class="del">
          <a id="deleteCheckedPM" class="button delete" href="${pageContext.request.contextPath}/pm"
            rel="<spring:message code="label.deletePMConfirmation"/>">
            <spring:message code="label.delete"/>
          </a>
          <form:form id="deleteForm" method="DELETE"/>
        </div>
    </div>
    <div class="footer_buffer"></div>
</div>
</body>
