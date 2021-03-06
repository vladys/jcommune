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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="jtalks" uri="http://www.jtalks.org/tags" %>
<head>
    <title><spring:message code="label.postListOfUser"/> <c:out value="${user.username}"/></title>
</head>
<body>
<c:set var="authenticated" value="${false}"/>
<div class="wrap user_posts_page topic_page">
    <jsp:include page="../template/topLine.jsp"/>
    <jsp:include page="../template/logo.jsp"/>
    <div class="all_forums">
        <div class="forum_info_top">
            <div>
                <div> <!-- top left -->
                    <h2 class="heading"><spring:message code="label.postListOfUser"/> ${user.username}</h2>
                </div>
                <div> <!-- top right -->

                </div>
            </div>
            <div class="info_top_lower">
                <div> <!-- bottom left -->
                    <sec:authorize access="hasAnyRole('ROLE_USER','ROLE_ADMIN')">
                        <c:set var="authenticated" value="${true}"/>
                    </sec:authorize>
                    <c:if test="${authenticated==true}">
                        <a class="button"
                           href="${pageContext.request.contextPath}/users/${user.encodedUsername}">
                            <spring:message code="label.backToProfile"/>
                        </a>
                    </c:if>
                </div>
                <div> <!-- bottom right -->
                    <span class="nav_top">
                        <jtalks:pagination uri="" pagination="${pag}" numberLink="3" list="${posts}"/>
                    <span>
                </div>
            </div>
        </div>
        <div class="forum_header_table">
            <div class="forum_header">
                <span class="forum_header_userinfo"><spring:message code="label.info"/></span>
                <span class="forum_header_topic"><spring:message code="label.topic.header.message"/></span>
            </div>
        </div>

        <c:choose>
            <c:when test="${!(empty posts)}">
                <ul class="forum_table">
                    <c:forEach var="post" items="${list}" varStatus="i">
                        <li class="forum_row">
                            <div class="forum_userinfo">
                                <div class="user_info">Branch</div>
                                <a class="forum_message_cell_text"
                                   href="${pageContext.request.contextPath}/branches/${post.topic.branch.id}">
                                    <c:out value="${post.topic.branch.name}"/></a>
                                <br>

                                <div class="user_info">Topic</div>
                                <a class="forum_message_cell_text"
                                   href="${pageContext.request.contextPath}/topics/${post.topic.id}">
                                    <c:out value="${post.topic.title}"/></a>
                            </div>
                            <div class="forum_message_cell">
                                <div class="post_details">
                                    <a class="button"
                                       href="${pageContext.request.contextPath}/posts/${post.id}">
                                        <spring:message code="label.goToPost"/>
                                    </a>
                                    <spring:message code="label.added"/>&nbsp;
                                    <jtalks:format value="${post.creationDate}"/>
                                </div>
                                <p class="forum_message_cell_text">
                                    <span class="truncated"><jtalks:bb2html bbCode="${post.postContent}"/></span>
                                    <br/><br/><br/>
                                    <c:if test="${post.modificationDate!=null}">
                                        <spring:message code="label.modify"/>
                                        <jtalks:format value="${post.modificationDate}"/>
                                    </c:if>
                                </p>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <ul class="forum_table">
                    <li class="forum_row empty_container">
                        <div>
                            <span class="empty">
                                <spring:message code="label.postListOfUser.empty"/>
                            </span>
                        </div>
                    </li>
                </ul>
            </c:otherwise>
        </c:choose>
        <div class="forum_info_bottom">
            <div>
                <div>
                    <a class="button"  href="${pageContext.request.contextPath}/users/${user.encodedUsername}">
                        <spring:message code="label.backToProfile"/>
                    </a>
                    <c:if test="${pag.maxPages>1}">
                        <c:if test="${pag.pagingEnabled==true}">
                            <a class="button"
                               href="?pagingEnabled=false"><spring:message code="label.showAll"/></a>
                        </c:if>
                    </c:if>
                    <c:if test="${pag.pagingEnabled == false}">
                        <a class="button" href="?pagingEnabled=true"><spring:message code="label.showPages"/></a>
                    </c:if>
                </div>
                <div>
                    <span class="nav_bottom">
                        <jtalks:pagination uri="${topicId}" pagination="${pag}" numberLink="3" list="${posts}" />
                    </span>
                </div>
            </div>
        </div>
    </div>
    <div class="footer_buffer"></div>
</div>
</body>