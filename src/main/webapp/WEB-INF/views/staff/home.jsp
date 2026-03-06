<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Staff Home - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Staff Workspace"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <c:if test="${not empty param.loginSuccess}">
    <p class="notice notice-success">Login successful.</p>
  </c:if>

  <div class="welcome-banner">
    <h3>👔 Staff Portal - Ocean View Resort</h3>
    <p>Welcome, <strong><c:out value="${sessionScope.authUser.username}"/></strong>. Manage guest reservations, process billing, and provide exceptional service to our guests.</p>
  </div>

  <div class="info-card">
    <div class="info-card-title">
      💡 Staff Responsibilities
    </div>
    <div class="info-card-content">
      As a staff member, you can create and manage guest reservations, process check-ins and check-outs, generate bills, and update payment statuses. Use the dashboard to monitor daily activities.
    </div>
  </div>

  <h3>🚀 Quick Actions</h3>
  <div class="quick-actions">
    <a href="${pageContext.request.contextPath}/staff/dashboard" class="quick-action-card">
      <div class="quick-action-icon">📊</div>
      <div class="quick-action-title">Dashboard</div>
      <div class="quick-action-desc">View operations overview</div>
    </a>
    <a href="${pageContext.request.contextPath}/staff/reservations" class="quick-action-card">
      <div class="quick-action-icon">📅</div>
      <div class="quick-action-title">Reservations</div>
      <div class="quick-action-desc">Manage guest bookings</div>
    </a>
    <a href="${pageContext.request.contextPath}/staff/billing" class="quick-action-card">
      <div class="quick-action-icon">💳</div>
      <div class="quick-action-title">Billing</div>
      <div class="quick-action-desc">Generate and manage bills</div>
    </a>
  </div>
</main>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
