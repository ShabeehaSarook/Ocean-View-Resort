<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Guest Dashboard - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Guest Dashboard"/>
  <jsp:param name="includeChartJs" value="true"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-header">
    <h2>Guest Dashboard</h2>
    <p class="page-description">Welcome back, <strong><c:out value="${guestProfile.fullName}"/></strong>! Here's your activity overview.</p>
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
        <div class="stat-label">Total Amount</div>
        <div class="stat-value">$<c:out value="${totalBilledAmount}"/></div>
      </div>
    </article>
  </section>

  <section class="charts">
    <article class="card">
      <h3>My Reservation Status</h3>
      <canvas id="myReservationStatusChart"></canvas>
    </article>
    <article class="card">
      <h3>My Billing Status</h3>
      <canvas id="myBillingStatusChart"></canvas>
    </article>
  </section>
</main>

<script>
  (async function () {
    const contextPath = '${pageContext.request.contextPath}';

    try {
      const reservationStatus = await OceanCharts.fetchReport(contextPath, 'reservation-status');
      new Chart(document.getElementById('myReservationStatusChart'), {
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
      new Chart(document.getElementById('myBillingStatusChart'), {
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