<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Manage Room Types - Admin"/>
  <jsp:param name="pageHeading" value="Room Type Management"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-title">
    <h2>Room Type Management</h2>
  </div>

  <c:if test="${not empty successMessage}"><p class="notice notice-success">${successMessage}</p></c:if>
  <c:if test="${not empty errorMessage}"><p class="notice notice-error">${errorMessage}</p></c:if>

  <section class="panel">
    <h3>Add New Room Type</h3>
    <form class="js-roomtype-form" method="post" action="${pageContext.request.contextPath}/admin/roomtypes">
      <input type="hidden" name="action" value="create">
      <div class="form-grid">
        <label class="required">Name
          <input type="text" name="name" required maxlength="100" 
                 placeholder="e.g., Deluxe Suite" aria-label="Room type name">
          <span class="label-hint">Maximum 100 characters</span>
        </label>
        <label class="required">Base Price
          <input type="number" step="0.01" min="0" max="100000" name="basePrice" required 
                 placeholder="0.00" aria-label="Base price in dollars">
          <span class="label-hint">Price per night ($0 - $100,000)</span>
        </label>
        <label class="required">Capacity
          <input type="number" min="1" max="50" name="capacity" required 
                 placeholder="2" aria-label="Maximum number of guests">
          <span class="label-hint">Maximum guests (1-50)</span>
        </label>
        <label>Description
          <input type="text" name="description" maxlength="500" 
                 placeholder="Optional description" aria-label="Room type description">
          <span class="label-hint">Optional, maximum 500 characters</span>
        </label>
      </div>
      <div class="form-actions"><button type="submit" class="btn-success">Create Room Type</button></div>
    </form>
  </section>

  <c:if test="${not empty editRoomType}">
    <section class="panel">
      <h3>Edit Room Type #${editRoomType.id}</h3>
      <form method="post" action="${pageContext.request.contextPath}/admin/roomtypes">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${editRoomType.id}">
        <div class="form-grid">
          <label>Name <input type="text" name="name" value="${editRoomType.name}" required></label>
          <label>Base Price <input type="number" step="0.01" min="0" name="basePrice" value="${editRoomType.basePrice}" required></label>
          <label>Capacity <input type="number" min="1" name="capacity" value="${editRoomType.capacity}" required></label>
          <label>Description <input type="text" name="description" value="${editRoomType.description}"></label>
        </div>
        <div class="form-actions">
          <button type="submit">Update</button>
          <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/roomtypes">Cancel</a>
        </div>
      </form>
    </section>
  </c:if>

  <div class="crud-toolbar">
    <input type="search"
           placeholder="Filter room types by name, capacity, description"
           data-crud-filter-input="#roomTypesTable"
           data-crud-count-target="#roomTypesVisibleCount"
           data-crud-clear-target="#clearRoomTypesFilter">
    <button type="button" class="btn btn-secondary" id="clearRoomTypesFilter">Clear Filter</button>
    <span class="crud-meta" id="roomTypesVisibleCount"></span>
  </div>

  <section class="table-wrap">
    <table id="roomTypesTable">
      <thead>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Base Price</th>
        <th>Capacity</th>
        <th>Description</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="roomType" items="${roomTypes}">
        <tr>
          <td>${roomType.id}</td>
          <td>${roomType.name}</td>
          <td>${roomType.basePrice}</td>
          <td>${roomType.capacity}</td>
          <td>${roomType.description}</td>
          <td>
            <div class="action-buttons">
              <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/roomtypes?editId=${roomType.id}">Edit</a>
              <form style="display: inline;"
                    method="post"
                    action="${pageContext.request.contextPath}/admin/roomtypes"
                    data-crud-confirm="Delete this room type?">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${roomType.id}">
                <button type="submit" class="btn-danger">Delete</button>
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
