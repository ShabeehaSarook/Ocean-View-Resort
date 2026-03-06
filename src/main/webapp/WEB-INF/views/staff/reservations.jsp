<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Staff Reservations - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Reservation Management"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-header">
    <h2>📅 Reservations Management</h2>
    <p class="page-description">Create, view, and manage guest reservations.</p>
  </div>

  <c:if test="${not empty successMessage}"><p class="notice notice-success">${successMessage}</p></c:if>
  <c:if test="${not empty errorMessage}"><p class="notice notice-error">${errorMessage}</p></c:if>

  <section class="panel">
    <h3>➕ Create New Reservation</h3>
    <form class="js-reservation-form" method="post" action="${pageContext.request.contextPath}/staff/reservations">
      <input type="hidden" name="action" value="create">
      <div class="form-grid">
        <label>Guest
          <select name="guestId" required>
            <c:forEach var="guest" items="${guests}">
              <option value="${guest.id}">${guest.fullName} (Guest #${guest.id})</option>
            </c:forEach>
          </select>
        </label>

        <label>Room
          <select name="roomId" required>
            <c:forEach var="room" items="${rooms}">
              <option value="${room.id}">Room ${room.roomNumber} (${room.status})</option>
            </c:forEach>
          </select>
        </label>

        <label>Check In <input type="date" name="checkIn" required></label>
        <label>Check Out <input type="date" name="checkOut" required></label>
        <label>Adults <input type="number" name="adults" min="1" value="1" required></label>
        <label>Children <input type="number" name="children" min="0" value="0" required></label>

        <label>Status
          <select name="status">
            <option value="CONFIRMED">CONFIRMED</option>
            <option value="PENDING">PENDING</option>
            <option value="CANCELLED">CANCELLED</option>
          </select>
        </label>
      </div>
      <div class="form-actions"><button type="submit">Create</button></div>
    </form>
  </section>

  <c:if test="${not empty editReservation}">
    <section class="panel">
      <h3>Edit Reservation #${editReservation.id}</h3>
      <form class="js-reservation-form" method="post" action="${pageContext.request.contextPath}/staff/reservations">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${editReservation.id}">

        <div class="form-grid">
          <label>Guest
            <select name="guestId" required>
              <c:forEach var="guest" items="${guests}">
                <option value="${guest.id}" ${editReservation.guestId == guest.id ? 'selected' : ''}>${guest.fullName} (Guest #${guest.id})</option>
              </c:forEach>
            </select>
          </label>

          <label>Room
            <select name="roomId" required>
              <c:forEach var="room" items="${rooms}">
                <option value="${room.id}" ${editReservation.roomId == room.id ? 'selected' : ''}>Room ${room.roomNumber} (${room.status})</option>
              </c:forEach>
            </select>
          </label>

          <label>Check In <input type="date" name="checkIn" value="${editReservation.checkIn}" required></label>
          <label>Check Out <input type="date" name="checkOut" value="${editReservation.checkOut}" required></label>
          <label>Adults <input type="number" name="adults" min="1" value="${editReservation.adults}" required></label>
          <label>Children <input type="number" name="children" min="0" value="${editReservation.children}" required></label>

          <label>Status
            <select name="status">
              <option value="PENDING" ${editReservation.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
              <option value="CONFIRMED" ${editReservation.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
              <option value="CHECKED_IN" ${editReservation.status == 'CHECKED_IN' ? 'selected' : ''}>CHECKED_IN</option>
              <option value="CHECKED_OUT" ${editReservation.status == 'CHECKED_OUT' ? 'selected' : ''}>CHECKED_OUT</option>
              <option value="CANCELLED" ${editReservation.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
              <option value="NO_SHOW" ${editReservation.status == 'NO_SHOW' ? 'selected' : ''}>NO_SHOW</option>
            </select>
          </label>
        </div>
        <div class="form-actions">
          <button type="submit">Update</button>
          <a class="btn btn-secondary" href="${pageContext.request.contextPath}/staff/reservations">Cancel</a>
        </div>
      </form>
    </section>
  </c:if>

  <div class="crud-toolbar">
    <input type="search"
           placeholder="Filter reservations by guest, room, dates, status"
           data-crud-filter-input="#staffReservationsTable"
           data-crud-count-target="#staffReservationsVisibleCount"
           data-crud-clear-target="#clearStaffReservationsFilter">
    <button type="button" class="btn btn-secondary" id="clearStaffReservationsFilter">Clear Filter</button>
    <span class="crud-meta" id="staffReservationsVisibleCount"></span>
  </div>

  <section class="table-wrap">
    <table id="staffReservationsTable">
      <thead>
      <tr>
        <th>ID</th>
        <th>Guest</th>
        <th>Room</th>
        <th>Check In</th>
        <th>Check Out</th>
        <th>Adults</th>
        <th>Children</th>
        <th>Status</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="reservation" items="${reservations}">
        <tr>
          <td>${reservation.id}</td>
          <td>${guestNameMap[reservation.guestId]}</td>
          <td>${roomNumberMap[reservation.roomId]}</td>
          <td>${reservation.checkIn}</td>
          <td>${reservation.checkOut}</td>
          <td>${reservation.adults}</td>
          <td>${reservation.children}</td>
          <td><span class="status-badge status-${fn:toLowerCase(reservation.status)}">${reservation.status}</span></td>
          <td>
            <div class="action-buttons">
              <a class="btn btn-secondary" href="${pageContext.request.contextPath}/staff/reservations?editId=${reservation.id}">✏️ Edit</a>

              <form style="display: inline;"
                    method="post"
                    action="${pageContext.request.contextPath}/staff/reservations"
                    data-crud-confirm="Cancel this reservation?">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${reservation.id}">
                <button type="submit" class="btn-danger">❌ Cancel</button>
              </form>

              <c:if test="${reservation.status == 'PENDING' || reservation.status == 'CONFIRMED'}">
                <form style="display: inline;"
                      method="post"
                      action="${pageContext.request.contextPath}/staff/reservations"
                      data-crud-confirm="Check in this reservation?">
                  <input type="hidden" name="action" value="checkin">
                  <input type="hidden" name="id" value="${reservation.id}">
                  <button type="submit" class="btn-success">✅ Check In</button>
                </form>
              </c:if>

              <c:if test="${reservation.status == 'CHECKED_IN'}">
                <form style="display: inline;"
                      method="post"
                      action="${pageContext.request.contextPath}/staff/reservations"
                      data-crud-confirm="Check out this reservation?">
                  <input type="hidden" name="action" value="checkout">
                  <input type="hidden" name="id" value="${reservation.id}">
                  <button type="submit" class="btn-success">🏁 Check Out</button>
                </form>
              </c:if>
            </div>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </section>
</main>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
