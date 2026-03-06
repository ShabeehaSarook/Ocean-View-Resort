<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Resort Management System"/>
</jsp:include>

<main class="content auth-layout">
  <section class="panel">
    <h2>Ocean View Resort</h2>
    <p>Application foundation is active.</p>
    <p>DB health endpoint: <a href="${pageContext.request.contextPath}/health/db">/health/db</a></p>
    <div class="form-actions">
      <a class="btn" href="${pageContext.request.contextPath}/login">Login</a>
      <a class="btn btn-secondary" href="${pageContext.request.contextPath}/register">Register</a>
    </div>
  </section>
</main>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>