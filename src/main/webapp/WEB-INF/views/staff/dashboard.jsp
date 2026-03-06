<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Staff Dashboard - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Staff Dashboard"/>
  <jsp:param name="includeChartJs" value="true"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-header">
    <h2>Staff Dashboard</h2>
    <p class="page-description">Monitor reservations, billing, and guest services.</p>
  </div>

  <c:if test="${not empty errorMessage}">
    <p class="notice notice-error"><c:out value="${errorMessage}"/></p>
  </c:if>

  <section class="grid">
    <article class="card stat-card">
      <div class="stat-icon">Reservations</div>
      <div class="stat-content">
        <div class="stat-label">Total Reservations</div>
        <div class="stat-value"><c:out value="${totalReservations}"/></div>
      </div>
    </article>
    <article class="card stat-card">
      <div class="stat-icon">Checked In</div>
      <div class="stat-content">
        <div class="stat-label">Checked In</div>
        <div class="stat-value"><c:out value="${checkedInReservations}"/></div>
      </div>
    </article>
    <article class="card stat-card">
      <div class="stat-icon">Checked Out</div>
      <div class="stat-content">
        <div class="stat-label">Checked Out</div>
        <div class="stat-value"><c:out value="${checkedOutReservations}"/></div>
      </div>
    </article>
    <article class="card stat-card">
      <div class="stat-icon">Bills</div>
      <div class="stat-content">
        <div class="stat-label">Total Bills</div>
        <div class="stat-value"><c:out value="${totalBills}"/></div>
      </div>
    </article>
    <article class="card stat-card stat-card-highlight">
      <div class="stat-icon">Revenue</div>
      <div class="stat-content">
        <div class="stat-label">Total Billed</div>
        <div class="stat-value">$<c:out value="${totalBilledAmount}"/></div>
      </div>
    </article>
  </section>

  <section class="charts">
    <article class="card">
      <h3>Reservation Status</h3>
      <canvas id="reservationStatusChart"></canvas>
    </article>
    <article class="card">
      <h3>Billing Status</h3>
      <canvas id="billingStatusChart"></canvas>
    </article>
  </section>
</main>

<script>
  (async function () {
    const contextPath = '${pageContext.request.contextPath}';

    try {
      const reservationStatus = await OceanCharts.fetchReport(contextPath, 'reservation-status');
      new Chart(document.getElementById('reservationStatusChart'), {
        type: 'bar',
        data: {
          labels: reservationStatus.labels,
          datasets: [{
            label: 'Reservations',
            data: reservationStatus.values,
            backgroundColor: ['#fab005', '#1c7ed6', '#2f9e44', '#94d82d', '#f03e3e', '#868e96']
          }]
        },
        options: {responsive: true}
      });

      const billingStatus = await OceanCharts.fetchReport(contextPath, 'billing-status');
      new Chart(document.getElementById('billingStatusChart'), {
        type: 'doughnut',
        data: {
          labels: billingStatus.labels,
          datasets: [{
            label: 'Bills',
            data: billingStatus.values,
            backgroundColor: ['#f03e3e', '#f08c00', '#2f9e44', '#1c7ed6', '#868e96']
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