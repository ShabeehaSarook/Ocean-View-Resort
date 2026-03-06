<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Staff Billing - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Billing Management"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-header">
    <h2>💳 Billing Management</h2>
    <p class="page-description">Generate bills and manage payment status.</p>
  </div>

  <c:if test="${not empty successMessage}"><p class="notice notice-success">${successMessage}</p></c:if>
  <c:if test="${not empty errorMessage}"><p class="notice notice-error">${errorMessage}</p></c:if>

  <section class="panel">
    <h3>➕ Generate New Bill</h3>
    <form class="js-bill-form" method="post" action="${pageContext.request.contextPath}/staff/billing">
      <input type="hidden" name="action" value="create">
      <div class="form-grid">
        <label class="required">Reservation
          <select name="reservationId" required aria-label="Select reservation">
            <option value="">-- Select Reservation --</option>
            <c:forEach var="reservation" items="${reservations}">
              <option value="${reservation.id}">${reservationLabelMap[reservation.id]}</option>
            </c:forEach>
          </select>
          <span class="label-hint">Select a reservation to bill</span>
        </label>

        <label class="required">Subtotal
          <input type="number" step="0.01" min="0" max="1000000" name="subtotal" required 
                 placeholder="0.00" aria-label="Subtotal amount">
          <span class="label-hint">Base amount before tax/discount</span>
        </label>
        <label class="required">Tax
          <input type="number" step="0.01" min="0" name="tax" value="0.00" required 
                 placeholder="0.00" aria-label="Tax amount">
          <span class="label-hint">Tax amount to add</span>
        </label>
        <label class="required">Discount
          <input type="number" step="0.01" min="0" name="discount" value="0.00" required 
                 placeholder="0.00" aria-label="Discount amount">
          <span class="label-hint">Discount amount to subtract</span>
        </label>

        <label class="required">Payment Status
          <select name="paymentStatus" aria-label="Payment status">
            <option value="UNPAID">UNPAID</option>
            <option value="PARTIAL">PARTIAL</option>
            <option value="PAID">PAID</option>
            <option value="REFUNDED">REFUNDED</option>
            <option value="VOID">VOID</option>
          </select>
          <span class="label-hint">Current payment status</span>
        </label>
      </div>
      <p class="crud-total-preview">Total Preview: $<span data-bill-total>0.00</span></p>
      <div class="form-actions"><button type="submit" class="btn-success">Generate Bill</button></div>
    </form>
  </section>

  <c:if test="${not empty editBill}">
    <section class="panel">
      <h3>Edit Bill #${editBill.id}</h3>
      <form class="js-bill-form" method="post" action="${pageContext.request.contextPath}/staff/billing">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${editBill.id}">

        <p>Reservation: ${reservationLabelMap[editBill.reservationId]}</p>
        <div class="form-grid">
          <label>Subtotal <input type="number" step="0.01" min="0" name="subtotal" value="${editBill.subtotal}" required></label>
          <label>Tax <input type="number" step="0.01" min="0" name="tax" value="${editBill.tax}" required></label>
          <label>Discount <input type="number" step="0.01" min="0" name="discount" value="${editBill.discount}" required></label>

          <label>Payment Status
            <select name="paymentStatus">
              <option value="UNPAID" ${editBill.paymentStatus == 'UNPAID' ? 'selected' : ''}>UNPAID</option>
              <option value="PARTIAL" ${editBill.paymentStatus == 'PARTIAL' ? 'selected' : ''}>PARTIAL</option>
              <option value="PAID" ${editBill.paymentStatus == 'PAID' ? 'selected' : ''}>PAID</option>
              <option value="REFUNDED" ${editBill.paymentStatus == 'REFUNDED' ? 'selected' : ''}>REFUNDED</option>
              <option value="VOID" ${editBill.paymentStatus == 'VOID' ? 'selected' : ''}>VOID</option>
            </select>
          </label>
        </div>
        <p class="crud-total-preview">Total Preview: <span data-bill-total>0.00</span></p>

        <div class="form-actions">
          <button type="submit">Update Bill</button>
          <a class="btn btn-secondary" href="${pageContext.request.contextPath}/staff/billing">Cancel</a>
        </div>
      </form>
    </section>
  </c:if>

  <div class="crud-toolbar">
    <input type="search"
           placeholder="Filter bills by reservation, amount, payment status"
           data-crud-filter-input="#staffBillsTable"
           data-crud-count-target="#staffBillsVisibleCount"
           data-crud-clear-target="#clearStaffBillsFilter">
    <button type="button" class="btn btn-secondary" id="clearStaffBillsFilter">Clear Filter</button>
    <span class="crud-meta" id="staffBillsVisibleCount"></span>
  </div>

  <section class="table-wrap">
    <table id="staffBillsTable">
      <thead>
      <tr>
        <th>ID</th>
        <th>Reservation</th>
        <th>Subtotal</th>
        <th>Tax</th>
        <th>Discount</th>
        <th>Total</th>
        <th>Payment Status</th>
        <th>Issued At</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="bill" items="${bills}">
        <tr>
          <td>${bill.id}</td>
          <td>${reservationLabelMap[bill.reservationId]}</td>
          <td>${bill.subtotal}</td>
          <td>${bill.tax}</td>
          <td>${bill.discount}</td>
          <td>${bill.total}</td>
          <td><span class="status-badge status-${fn:toLowerCase(bill.paymentStatus)}">${bill.paymentStatus}</span></td>
          <td>${bill.issuedAt}</td>
          <td>
            <div class="action-buttons">
              <a class="btn btn-secondary" href="${pageContext.request.contextPath}/staff/billing?editId=${bill.id}">✏️ Edit</a>
              <form style="display: inline;"
                    method="post"
                    action="${pageContext.request.contextPath}/staff/billing"
                    data-crud-confirm="Void this bill?">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${bill.id}">
                <button type="submit" class="btn-danger">🚫 Void</button>
              </form>
            </div>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </section>
</main>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
