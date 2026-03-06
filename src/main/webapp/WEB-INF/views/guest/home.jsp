<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Guest Home - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Guest Workspace"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <c:if test="${not empty param.loginSuccess}">
    <p class="notice notice-success">Login successful.</p>
  </c:if>

  <div class="welcome-banner">
    <h3>🏖️ Welcome to Ocean View Resort!</h3>
    <p>Enjoy your stay with us, <strong><c:out value="${sessionScope.authUser.username}"/></strong>. Manage your reservations, view bills, and explore our beautiful resort facilities.</p>
  </div>

  <div class="info-card">
    <div class="info-card-title">
      💡 Quick Start Guide
    </div>
    <div class="info-card-content">
      <strong>New here?</strong> Start by creating a reservation to book your room. You can view all your reservations, check billing information, and manage your bookings easily from your dashboard.
    </div>
  </div>

  <h3>🚀 Quick Actions</h3>
  <div class="quick-actions">
    <a href="${pageContext.request.contextPath}/guest/dashboard" class="quick-action-card">
      <div class="quick-action-icon">📊</div>
      <div class="quick-action-title">Dashboard</div>
      <div class="quick-action-desc">View your activity overview</div>
    </a>
    <a href="${pageContext.request.contextPath}/guest/reservations" class="quick-action-card">
      <div class="quick-action-icon">📅</div>
      <div class="quick-action-title">My Reservations</div>
      <div class="quick-action-desc">Book and manage reservations</div>
    </a>
    <a href="${pageContext.request.contextPath}/guest/bills" class="quick-action-card">
      <div class="quick-action-icon">💳</div>
      <div class="quick-action-title">My Bills</div>
      <div class="quick-action-desc">View billing history</div>
    </a>
  </div>
</main>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
