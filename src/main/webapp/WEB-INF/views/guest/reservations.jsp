<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="My Reservations - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Guest Reservations"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-header">
    <h2>My Reservations</h2>
    <p class="page-description">View and manage your room reservations.</p>
  </div>

  <c:if test="${not empty successMessage}"><p class="notice notice-success">${successMessage}</p></c:if>
  <c:if test="${not empty errorMessage}"><p class="notice notice-error">${errorMessage}</p></c:if>

  <section class="panel">
    <h3>➕ Create New Reservation</h3>
    <form class="js-reservation-form" method="post" action="${pageContext.request.contextPath}/guest/reservations">
      <input type="hidden" name="action" value="create">

      <div class="form-grid">
        <label class="required">Room
          <select name="roomId" required aria-label="Select a room">
            <option value="">-- Select a Room --</option>
            <c:forEach var="room" items="${rooms}">
              <option value="${room.id}">Room ${room.roomNumber} (${room.status})</option>
            </c:forEach>
          </select>
          <span class="label-hint">Choose from available rooms</span>
        </label>

        <label class="required">Check In
          <input type="date" name="checkIn" required aria-label="Check-in date">
          <span class="label-hint">Arrival date (cannot be in the past)</span>
        </label>
        <label class="required">Check Out
          <input type="date" name="checkOut" required aria-label="Check-out date">
          <span class="label-hint">Departure date (must be after check-in)</span>
        </label>
        <label class="required">Adults
          <input type="number" name="adults" min="1" max="20" value="1" required 
                 aria-label="Number of adults">
          <span class="label-hint">1-20 adults</span>
        </label>
        <label class="required">Children
          <input type="number" name="children" min="0" max="20" value="0" required 
                 aria-label="Number of children">
          <span class="label-hint">0-20 children (max 30 total guests)</span>
        </label>
      </div>

      <div class="form-actions"><button type="submit" class="btn-success">Create Reservation</button></div>
    </form>
  </section>

  <c:if test="${not empty editReservation}">
    <section class="panel">
      <h3>Edit Reservation #${editReservation.id}</h3>
      <form class="js-reservation-form" method="post" action="${pageContext.request.contextPath}/guest/reservations">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${editReservation.id}">

        <div class="form-grid">
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
        </div>

        <div class="form-actions">
          <button type="submit">Update</button>
          <a class="btn btn-secondary" href="${pageContext.request.contextPath}/guest/reservations">Cancel</a>
        </div>
      </form>
    </section>
  </c:if>

  <div class="crud-toolbar">
    <input type="search"
           placeholder="Filter my reservations by room, date, or status"
           data-crud-filter-input="#guestReservationsTable"
           data-crud-count-target="#guestReservationsVisibleCount"
           data-crud-clear-target="#clearGuestReservationsFilter">
    <button type="button" class="btn btn-secondary" id="clearGuestReservationsFilter">Clear Filter</button>
    <span class="crud-meta" id="guestReservationsVisibleCount"></span>
  </div>

  <section class="table-wrap">
    <table id="guestReservationsTable">
      <thead>
      <tr>
        <th>ID</th>
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
          <td>${roomNumberMap[reservation.roomId]}</td>
          <td>${reservation.checkIn}</td>
          <td>${reservation.checkOut}</td>
          <td>${reservation.adults}</td>
          <td>${reservation.children}</td>
          <td><span class="status-badge status-${fn:toLowerCase(reservation.status)}">${reservation.status}</span></td>
          <td>
            <c:if test="${reservation.status == 'PENDING' || reservation.status == 'CONFIRMED'}">
              <div class="action-buttons">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/guest/reservations?editId=${reservation.id}">Edit</a>

                <form style="display: inline;"
                      method="post"
                      action="${pageContext.request.contextPath}/guest/reservations"
                      data-crud-confirm="Cancel this reservation?">
                  <input type="hidden" name="action" value="cancel">
                  <input type="hidden" name="id" value="${reservation.id}">
                  <button type="submit" class="btn-danger">Cancel</button>
                </form>
              </div>
            </c:if>
            <c:if test="${reservation.status != 'PENDING' && reservation.status != 'CONFIRMED'}">
              <span style="color: var(--muted); font-size: 0.85rem;">No actions available</span>
            </c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </section>
</main>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
