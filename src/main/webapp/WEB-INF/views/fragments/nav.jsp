<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${not empty sessionScope.authUser}">
  <c:set var="currentPath" value="${pageContext.request.requestURI}" />
  <nav class="top-nav" aria-label="Main navigation">
    <ul class="top-nav-list">
      <c:choose>
        <c:when test="${sessionScope.authUser.role == 'ADMIN'}">
          <li><a class="top-nav-link ${currentPath.contains('/admin/dashboard') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
          <li><a class="top-nav-link ${currentPath.contains('/admin/users') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/users">Users</a></li>
          <li><a class="top-nav-link ${currentPath.contains('/admin/roomtypes') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/roomtypes">Room Types</a></li>
          <li><a class="top-nav-link ${currentPath.contains('/admin/rooms') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/rooms">Rooms</a></li>
        </c:when>
        <c:when test="${sessionScope.authUser.role == 'STAFF'}">
          <li><a class="top-nav-link ${currentPath.contains('/staff/dashboard') ? 'active' : ''}" href="${pageContext.request.contextPath}/staff/dashboard">Dashboard</a></li>
          <li><a class="top-nav-link ${currentPath.contains('/staff/reservations') ? 'active' : ''}" href="${pageContext.request.contextPath}/staff/reservations">Reservations</a></li>
          <li><a class="top-nav-link ${currentPath.contains('/staff/billing') ? 'active' : ''}" href="${pageContext.request.contextPath}/staff/billing">Billing</a></li>
        </c:when>
        <c:when test="${sessionScope.authUser.role == 'GUEST'}">
          <li><a class="top-nav-link ${currentPath.contains('/guest/dashboard') ? 'active' : ''}" href="${pageContext.request.contextPath}/guest/dashboard">Dashboard</a></li>
          <li><a class="top-nav-link ${currentPath.contains('/guest/reservations') ? 'active' : ''}" href="${pageContext.request.contextPath}/guest/reservations">My Reservations</a></li>
          <li><a class="top-nav-link ${currentPath.contains('/guest/bills') ? 'active' : ''}" href="${pageContext.request.contextPath}/guest/bills">My Bills</a></li>
        </c:when>
      </c:choose>
      <li><a class="top-nav-link nav-logout" href="${pageContext.request.contextPath}/logout">Logout</a></li>
    </ul>
  </nav>
</c:if>