<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Manage Users - Admin"/>
  <jsp:param name="pageHeading" value="User Management"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/fragments/nav.jsp"/>

<main class="content">
  <div class="page-title">
    <h2>User Management</h2>
  </div>

  <c:if test="${not empty successMessage}"><p class="notice notice-success">${successMessage}</p></c:if>
  <c:if test="${not empty errorMessage}"><p class="notice notice-error">${errorMessage}</p></c:if>

  <section class="panel">
    <h3>Add New User</h3>
    <form method="post" action="${pageContext.request.contextPath}/admin/users">
      <input type="hidden" name="action" value="create">
      <div class="form-grid">
        <label>Username <input type="text" name="username" required></label>
        <label>Password <input type="password" name="password" required></label>
        <label>Role
          <select name="role" required>
            <option value="ADMIN">ADMIN</option>
            <option value="STAFF">STAFF</option>
            <option value="GUEST">GUEST</option>
          </select>
        </label>
        <label>Status
          <select name="status" required>
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
            <option value="LOCKED">LOCKED</option>
          </select>
        </label>
      </div>
      <div class="form-actions"><button type="submit">Create</button></div>
    </form>
  </section>

  <c:if test="${not empty editUser}">
    <section class="panel">
      <h3>Edit User #${editUser.id}</h3>
      <form method="post" action="${pageContext.request.contextPath}/admin/users">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${editUser.id}">
        <div class="form-grid">
          <label>Username <input type="text" name="username" value="${editUser.username}" required></label>
          <label>New Password (optional) <input type="password" name="password"></label>
          <label>Role
            <select name="role" required>
              <option value="ADMIN" ${editUser.role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
              <option value="STAFF" ${editUser.role == 'STAFF' ? 'selected' : ''}>STAFF</option>
              <option value="GUEST" ${editUser.role == 'GUEST' ? 'selected' : ''}>GUEST</option>
            </select>
          </label>
          <label>Status
            <select name="status" required>
              <option value="ACTIVE" ${editUser.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
              <option value="INACTIVE" ${editUser.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
              <option value="LOCKED" ${editUser.status == 'LOCKED' ? 'selected' : ''}>LOCKED</option>
            </select>
          </label>
        </div>
        <div class="form-actions">
          <button type="submit">Update</button>
          <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/users">Cancel</a>
        </div>
      </form>
    </section>
  </c:if>

  <div class="crud-toolbar">
    <input type="search"
           placeholder="Filter users by username, role, or status"
           data-crud-filter-input="#usersTable"
           data-crud-count-target="#usersVisibleCount"
           data-crud-clear-target="#clearUsersFilter">
    <button type="button" class="btn btn-secondary" id="clearUsersFilter">Clear Filter</button>
    <span class="crud-meta" id="usersVisibleCount"></span>
  </div>

  <section class="table-wrap">
    <table id="usersTable">
      <thead>
      <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Role</th>
        <th>Status</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="user" items="${users}">
        <tr>
          <td>${user.id}</td>
          <td>${user.username}</td>
          <td><span class="status-badge role-${fn:toLowerCase(user.role)}">${user.role}</span></td>
          <td><span class="status-badge status-${fn:toLowerCase(user.status)}">${user.status}</span></td>
          <td>
            <div class="action-buttons">
              <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/users?editId=${user.id}">Edit</a>
              <c:if test="${sessionScope.authUser.id != user.id}">
                <form style="display: inline;"
                      method="post"
                      action="${pageContext.request.contextPath}/admin/users"
                      data-crud-confirm="Deactivate this user?">
                  <input type="hidden" name="action" value="delete">
                  <input type="hidden" name="id" value="${user.id}">
                  <button type="submit" class="btn-danger">Deactivate</button>
                </form>
              </c:if>
              <c:if test="${sessionScope.authUser.id == user.id}">
                <span style="color: var(--muted); font-size: 0.85rem;">Current user</span>
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
