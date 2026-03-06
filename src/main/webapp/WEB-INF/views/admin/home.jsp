<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Admin Home - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Administrator Workspace"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content admin-home">
  <c:if test="${not empty param.loginSuccess}">
    <p class="notice notice-success">Login successful.</p>
  </c:if>

  <div class="admin-header">
    <div>
      <h1>Welcome back, <c:out value="${sessionScope.authUser.username}"/></h1>
      <p>Here's what's happening with your resort today</p>
    </div>
  </div>

  <div class="admin-grid">
    <!-- Main Actions -->
    <div class="admin-card">
      <div class="card-header">
        <h3>System Management</h3>
      </div>
      <div class="card-body">
        <ul class="action-list">
          <li>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="action-link">
              <span class="action-title">Dashboard</span>
              <span class="action-desc">View analytics and metrics</span>
            </a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/admin/users" class="action-link">
              <span class="action-title">User Management</span>
              <span class="action-desc">Manage admin, staff, and guest accounts</span>
            </a>
          </li>
        </ul>
      </div>
    </div>

    <!-- Room Management -->
    <div class="admin-card">
      <div class="card-header">
        <h3>Room Management</h3>
      </div>
      <div class="card-body">
        <ul class="action-list">
          <li>
            <a href="${pageContext.request.contextPath}/admin/rooms" class="action-link">
              <span class="action-title">Rooms</span>
              <span class="action-desc">Manage room inventory and status</span>
            </a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/admin/roomtypes" class="action-link">
              <span class="action-title">Room Types</span>
              <span class="action-desc">Configure categories and pricing</span>
            </a>
          </li>
        </ul>
      </div>
    </div>

    <!-- Quick Stats -->
    <div class="admin-card stats-card">
      <div class="card-header">
        <h3>Quick Overview</h3>
      </div>
      <div class="card-body">
        <p class="stats-note">Access the <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a> to view detailed analytics, charts, and system reports.</p>
      </div>
    </div>
  </div>
</main>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
