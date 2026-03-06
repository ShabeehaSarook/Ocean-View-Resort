<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Login - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Secure Sign In"/>
</jsp:include>
<style>
  .auth-login .password-wrap {
    display: flex;
    gap: 8px;
    align-items: center;
  }

  .auth-login .password-wrap input {
    flex: 1;
  }

  .auth-login .remember-row {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-top: 6px;
    color: var(--muted);
  }
</style>

<main class="content auth-layout auth-login">
  <section class="panel">
    <h2>Login</h2>

    <c:if test="${not empty param.registered}">
      <p class="notice notice-success">Registration successful. Please log in.</p>
    </c:if>
    <c:if test="${not empty param.logout}">
      <p class="notice notice-success">You have been logged out.</p>
    </c:if>
    <c:if test="${not empty param.authRequired}">
      <p class="notice notice-error">Please log in to continue.</p>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <p class="notice notice-error">${errorMessage}</p>
    </c:if>

    <form id="loginForm" method="post" action="${pageContext.request.contextPath}/login">
      <div class="form-grid">
        <label>Username
          <input type="text" id="loginUsername" name="username" value="${username}" required autocomplete="username">
        </label>
        <label>Password
          <div class="password-wrap">
            <input type="password" id="loginPassword" name="password" required autocomplete="current-password">
            <button type="button" class="btn btn-secondary" id="toggleLoginPasswordBtn">Show</button>
          </div>
        </label>
      </div>
      <label class="remember-row">
        <input type="checkbox" id="rememberUsernameChk">
        Remember username on this browser
      </label>
      <div class="form-actions">
        <button type="submit">Login</button>
        <button type="button" class="btn btn-secondary" id="clearLoginBtn">Clear</button>
      </div>
    </form>

    <p>No account? <a href="${pageContext.request.contextPath}/register">Register as Guest</a></p>
  </section>
</main>
<script>
  (function () {
    var form = document.getElementById('loginForm');
    if (!form) {
      return;
    }

    var STORAGE_KEY = 'ovr_login_username';
    var usernameInput = document.getElementById('loginUsername');
    var passwordInput = document.getElementById('loginPassword');
    var rememberChk = document.getElementById('rememberUsernameChk');
    var toggleBtn = document.getElementById('toggleLoginPasswordBtn');

    function createRememberedUsername(value) {
      localStorage.setItem(STORAGE_KEY, value);
    }

    function readRememberedUsername() {
      return localStorage.getItem(STORAGE_KEY) || '';
    }

    function updateRememberedUsername(value) {
      createRememberedUsername(value);
    }

    function deleteRememberedUsername() {
      localStorage.removeItem(STORAGE_KEY);
    }

    function validateRead() {
      if (!(usernameInput.value || '').trim()) {
        alert('Username is required.');
        usernameInput.focus();
        return false;
      }
      if (!(passwordInput.value || '').trim()) {
        alert('Password is required.');
        passwordInput.focus();
        return false;
      }
      return true;
    }

    function clearCredentials() {
      usernameInput.value = '';
      passwordInput.value = '';
      usernameInput.focus();
    }

    function initRememberedValue() {
      if ((usernameInput.value || '').trim()) {
        return;
      }
      var remembered = readRememberedUsername();
      if (remembered) {
        usernameInput.value = remembered;
        rememberChk.checked = true;
      }
    }

    toggleBtn.addEventListener('click', function () {
      var show = passwordInput.type === 'password';
      passwordInput.type = show ? 'text' : 'password';
      toggleBtn.textContent = show ? 'Hide' : 'Show';
    });

    form.addEventListener('submit', function (event) {
      if (!validateRead()) {
        event.preventDefault();
        return;
      }
      if (rememberChk.checked) {
        if (readRememberedUsername()) {
          updateRememberedUsername(usernameInput.value.trim());
        } else {
          createRememberedUsername(usernameInput.value.trim());
        }
      } else {
        deleteRememberedUsername();
      }
    });

    document.getElementById('clearLoginBtn').addEventListener('click', function () {
      clearCredentials();
      if (!rememberChk.checked) {
        deleteRememberedUsername();
      }
    });

    window.loginCrud = {
      create: createRememberedUsername,
      read: readRememberedUsername,
      update: updateRememberedUsername,
      delete: deleteRememberedUsername
    };

    initRememberedValue();
  })();
</script>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
