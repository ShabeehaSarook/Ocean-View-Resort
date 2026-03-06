<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Admin Dashboard - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Administrator Dashboard"/>
  <jsp:param name="includeChartJs" value="true"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-header">
    <h2>Admin Dashboard</h2>
    <p class="page-description">Manage system, users, rooms, and view analytics.</p>
  </div>

  <c:if test="${not empty errorMessage}">
    <p class="notice notice-error"><c:out value="${errorMessage}"/></p>
  </c:if>

  <section class="grid">
    <article class="card stat-card">
      <div class="stat-icon">Users</div>
      <div class="stat-content">
        <div class="stat-label">Total Users</div>
        <div class="stat-value"><c:out value="${totalUsers}"/></div>
      </div>
    </article>
    <article class="card stat-card">
      <div class="stat-icon">Rooms</div>
      <div class="stat-content">
        <div class="stat-label">Total Rooms</div>
        <div class="stat-value"><c:out value="${totalRooms}"/></div>
      </div>
    </article>
    <article class="card stat-card">
      <div class="stat-icon">Reservations</div>
      <div class="stat-content">
        <div class="stat-label">Total Reservations</div>
        <div class="stat-value"><c:out value="${totalReservations}"/></div>
      </div>
    </article>
    <article class="card stat-card stat-card-highlight">
      <div class="stat-icon">Revenue</div>
      <div class="stat-content">
        <div class="stat-label">Total Revenue</div>
        <div class="stat-value">$<c:out value="${totalRevenue}"/></div>
      </div>
    </article>
  </section>

  <section class="charts">
    <article class="card">
      <h3>Users by Role</h3>
      <canvas id="usersByRoleChart"></canvas>
    </article>
    <article class="card">
      <h3>Rooms by Status</h3>
      <canvas id="roomsByStatusChart"></canvas>
    </article>
    <article class="card">
      <h3>Revenue Trend (Last 6 Months)</h3>
      <canvas id="revenueTrendChart"></canvas>
    </article>
    <article class="card">
      <h3>Reservation Trend (Last 6 Months)</h3>
      <canvas id="reservationTrendChart"></canvas>
    </article>
  </section>
</main>

<script>
  (async function () {
    const contextPath = '${pageContext.request.contextPath}';

    try {
      const usersByRole = await OceanCharts.fetchReport(contextPath, 'users-by-role');
      new Chart(document.getElementById('usersByRoleChart'), {
        type: 'bar',
        data: {
          labels: usersByRole.labels,
          datasets: [{
            label: 'User Count',
            data: usersByRole.values,
            backgroundColor: ['#0b7285', '#2f9e44', '#d9480f']
          }]
        },
        options: {responsive: true}
      });

      const occupancy = await OceanCharts.fetchReport(contextPath, 'occupancy');
      new Chart(document.getElementById('roomsByStatusChart'), {
        type: 'doughnut',
        data: {
          labels: occupancy.labels,
          datasets: [{
            label: 'Rooms',
            data: occupancy.values,
            backgroundColor: ['#2f9e44', '#1c7ed6', '#f08c00', '#868e96']
          }]
        },
        options: {responsive: true}
      });

      const revenueTrend = await OceanCharts.fetchReport(contextPath, 'revenue');
      new Chart(document.getElementById('revenueTrendChart'), {
        type: 'line',
        data: {
          labels: revenueTrend.labels,
          datasets: [{
            label: 'Revenue',
            data: revenueTrend.values,
            borderColor: '#0b7285',
            backgroundColor: 'rgba(11, 114, 133, 0.15)',
            fill: true,
            tension: 0.25
          }]
        },
        options: {responsive: true}
      });

      const reservationTrend = await OceanCharts.fetchReport(contextPath, 'reservation-trend');
      new Chart(document.getElementById('reservationTrendChart'), {
        type: 'line',
        data: {
          labels: reservationTrend.labels,
          datasets: [{
            label: 'Reservations',
            data: reservationTrend.values,
            borderColor: '#2f9e44',
            backgroundColor: 'rgba(47, 158, 68, 0.15)',
            fill: true,
            tension: 0.25
          }]
        },
        options: {responsive: true}
      });
    } catch (error) {
      OceanCharts.renderError('.content', 'Unable to load chart data.');
      console.error(error);
    }
  })();
</script>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>