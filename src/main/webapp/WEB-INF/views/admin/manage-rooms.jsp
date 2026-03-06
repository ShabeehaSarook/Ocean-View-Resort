<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Manage Rooms - Admin"/>
  <jsp:param name="pageHeading" value="Room Management"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-title">
    <h2>Room Management</h2>
  </div>

  <c:if test="${not empty successMessage}"><p class="notice notice-success">${successMessage}</p></c:if>
  <c:if test="${not empty errorMessage}"><p class="notice notice-error">${errorMessage}</p></c:if>

  <section class="panel">
    <h3>Add New Room</h3>
    <form class="js-room-form" method="post" action="${pageContext.request.contextPath}/admin/rooms">
      <input type="hidden" name="action" value="create">
      <div class="form-grid">
        <label class="required">Room Number
          <input type="text" name="roomNumber" required placeholder="e.g., 101, A-204" 
                 pattern="[A-Za-z0-9-]{1,20}" maxlength="20"
                 aria-label="Room number">
          <span class="label-hint">1-20 characters, letters/numbers/hyphens only</span>
        </label>
        <label class="required">Room Type
          <select name="roomTypeId" required aria-label="Select room type">
            <option value="">-- Select Room Type --</option>
            <c:forEach var="type" items="${roomTypes}">
              <option value="${type.id}">${type.name}</option>
            </c:forEach>
          </select>
        </label>
        <label class="required">Status
          <select name="status" required aria-label="Select room status">
            <option value="AVAILABLE">AVAILABLE</option>
            <option value="OCCUPIED">OCCUPIED</option>
            <option value="MAINTENANCE">MAINTENANCE</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>
          <span class="label-hint">Current room availability status</span>
        </label>
        <label class="required">Floor
          <input type="number" name="floor" value="1" required min="-5" max="200"
                 aria-label="Floor number">
          <span class="label-hint">Between -5 (basement) and 200</span>
        </label>
      </div>
      <div class="form-actions"><button type="submit" class="btn-success">Create Room</button></div>
    </form>
  </section>

  <c:if test="${not empty editRoom}">
    <section class="panel">
      <h3>Edit Room #${editRoom.id}</h3>
      <form method="post" action="${pageContext.request.contextPath}/admin/rooms">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${editRoom.id}">
        <div class="form-grid">
          <label>Room Number <input type="text" name="roomNumber" value="${editRoom.roomNumber}" required></label>
          <label>Room Type
            <select name="roomTypeId" required>
              <c:forEach var="type" items="${roomTypes}">
                <option value="${type.id}" ${editRoom.roomTypeId == type.id ? 'selected' : ''}>${type.name}</option>
              </c:forEach>
            </select>
          </label>
          <label>Status
            <select name="status" required>
              <option value="AVAILABLE" ${editRoom.status == 'AVAILABLE' ? 'selected' : ''}>AVAILABLE</option>
              <option value="OCCUPIED" ${editRoom.status == 'OCCUPIED' ? 'selected' : ''}>OCCUPIED</option>
              <option value="MAINTENANCE" ${editRoom.status == 'MAINTENANCE' ? 'selected' : ''}>MAINTENANCE</option>
              <option value="INACTIVE" ${editRoom.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
            </select>
          </label>
          <label>Floor <input type="number" name="floor" value="${editRoom.floor}" required></label>
        </div>
        <div class="form-actions">
          <button type="submit">Update</button>
          <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/rooms">Cancel</a>
        </div>
      </form>
    </section>
  </c:if>

  <div class="crud-toolbar">
    <input type="search"
           placeholder="Filter rooms by number, type, status, floor"
           data-crud-filter-input="#roomsTable"
           data-crud-count-target="#roomsVisibleCount"
           data-crud-clear-target="#clearRoomsFilter">
    <button type="button" class="btn btn-secondary" id="clearRoomsFilter">Clear Filter</button>
    <span class="crud-meta" id="roomsVisibleCount"></span>
  </div>

  <section class="table-wrap">
    <table id="roomsTable">
      <thead>
      <tr>
        <th>ID</th>
        <th>Room Number</th>
        <th>Room Type</th>
        <th>Status</th>
        <th>Floor</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="room" items="${rooms}">
        <tr>
          <td>${room.id}</td>
          <td>${room.roomNumber}</td>
          <td>${roomTypeNameMap[room.roomTypeId]}</td>
          <td><span class="status-badge status-${fn:toLowerCase(room.status)}">${room.status}</span></td>
          <td>${room.floor}</td>
          <td>
            <div class="action-buttons">
              <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/rooms?editId=${room.id}">Edit</a>
              <form style="display: inline;"
                    method="post"
                    action="${pageContext.request.contextPath}/admin/rooms"
                    data-crud-confirm="Mark this room as inactive?">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${room.id}">
                <button type="submit" class="btn-danger">Inactivate</button>
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
