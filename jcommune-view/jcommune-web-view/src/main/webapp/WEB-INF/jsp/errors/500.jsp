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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<head>
    <title><spring:message code="label.500.title"/></title>
</head>
<body>
<div class="wrap main_page">
    <jsp:include page="../../template/topLine.jsp"/>
    <div class="all_forums">
        <div class="text_errorpage">
            <h1><span class="error_errorpage"><spring:message code="label.error"/></span> 500</h1>
            <spring:message code="label.500.detail"/>
            <br/>
            <spring:message code="label.500.refresh"/>&nbsp;
            <a href="${pageContext.request.contextPath}"><spring:message code="label.back2main"/></a>
        </div>
    </div>
    <div class="footer_buffer"></div>
</div>
</body>