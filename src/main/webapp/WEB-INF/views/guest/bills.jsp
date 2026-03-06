<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="My Bills - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Guest Billing"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-header">
    <h2>💳 My Bills</h2>
    <p class="page-description">View your billing history and payment status.</p>
  </div>

  <c:if test="${not empty errorMessage}"><p class="notice notice-error">${errorMessage}</p></c:if>

  <div class="crud-toolbar">
    <input type="search"
           placeholder="Filter my bills by reservation, total, or payment status"
           data-crud-filter-input="#guestBillsTable"
           data-crud-count-target="#guestBillsVisibleCount"
           data-crud-clear-target="#clearGuestBillsFilter">
    <button type="button" class="btn btn-secondary" id="clearGuestBillsFilter">Clear Filter</button>
    <span class="crud-meta" id="guestBillsVisibleCount"></span>
  </div>

  <section class="table-wrap">
    <table id="guestBillsTable">
      <thead>
      <tr>
        <th>Bill ID</th>
        <th>Reservation</th>
        <th>Subtotal</th>
        <th>Tax</th>
        <th>Discount</th>
        <th>Total</th>
        <th>Payment Status</th>
        <th>Issued At</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="bill" items="${bills}">
        <tr>
          <td>${bill.id}</td>
          <td>${billReservationLabelMap[bill.id]}</td>
          <td>${bill.subtotal}</td>
          <td>${bill.tax}</td>
          <td>${bill.discount}</td>
          <td>${bill.total}</td>
          <td><span class="status-badge status-${fn:toLowerCase(bill.paymentStatus)}">${bill.paymentStatus}</span></td>
          <td>${bill.issuedAt}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </section>
</main>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
