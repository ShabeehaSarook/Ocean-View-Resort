<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Register - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Guest Registration"/>
</jsp:include>
<style>
  .auth-register {
    max-width: 780px;
  }

  .auth-register .panel {
    border-top: 4px solid var(--primary);
  }

  .auth-register h2 {
    margin-bottom: 6px;
  }

  .auth-register .notice {
    margin-bottom: 14px;
  }

  .auth-register .form-grid {
    gap: 12px;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  }

  .auth-register label {
    font-weight: 600;
    color: var(--text);
  }

  .auth-register input,
  .auth-register textarea {
    border: 1px solid #c9d6e5;
    background: #fff;
    transition: border-color 0.2s ease, box-shadow 0.2s ease;
  }

  .auth-register input:focus,
  .auth-register textarea:focus {
    outline: none;
    border-color: var(--primary);
    box-shadow: 0 0 0 3px rgba(11, 114, 133, 0.16);
  }

  .auth-register .password-wrap {
    display: flex;
    gap: 8px;
    align-items: center;
  }

  .auth-register .password-wrap input {
    flex: 1;
  }

  .auth-register .toggle-btn {
    white-space: nowrap;
    min-width: 72px;
  }

  .auth-register .strength-text {
    margin-top: 10px;
    padding: 8px 10px;
    border-radius: 8px;
    background: var(--surface-alt);
    border: 1px solid var(--border);
    font-size: 0.9rem;
    color: var(--muted);
  }

  .auth-register .strength-text[data-strength="weak"] {
    color: #b42318;
    border-color: #f1b5b0;
    background: #fff5f4;
  }

  .auth-register .strength-text[data-strength="medium"] {
    color: #9a6700;
    border-color: #f8d39a;
    background: #fffbf3;
  }

  .auth-register .strength-text[data-strength="strong"] {
    color: #067647;
    border-color: #9dd9bb;
    background: #effcf6;
  }

  .auth-register .form-actions {
    margin-top: 12px;
  }

  @media (max-width: 640px) {
    .auth-register {
      max-width: 100%;
    }

    .auth-register .password-wrap {
      flex-direction: column;
      align-items: stretch;
    }

    .auth-register .toggle-btn {
      width: 100%;
    }
  }
</style>

<main class="content auth-layout auth-register">
  <section class="panel">
    <h2>Create Guest Account</h2>

    <c:if test="${not empty errorMessage}">
      <p class="notice notice-error">${errorMessage}</p>
    </c:if>

    <form class="js-register-form" id="registerForm" method="post" action="${pageContext.request.contextPath}/register" novalidate>
      <div class="form-grid">
        <label>Username
          <input type="text" name="username" value="${username}" required pattern="[A-Za-z0-9._-]{4,50}" autocomplete="username">
        </label>
        <label>Password
          <div class="password-wrap">
            <input type="password" id="registerPassword" name="password" required autocomplete="new-password">
            <button type="button" class="btn btn-secondary toggle-btn" data-toggle-target="registerPassword">Show</button>
          </div>
        </label>
        <label>Confirm Password
          <div class="password-wrap">
            <input type="password" id="registerConfirmPassword" name="confirmPassword" required autocomplete="new-password">
            <button type="button" class="btn btn-secondary toggle-btn" data-toggle-target="registerConfirmPassword">Show</button>
          </div>
        </label>
        <label>Full Name
          <input type="text" name="fullName" value="${fullName}" required>
        </label>
        <label>Email
          <input type="email" name="email" value="${email}" required>
        </label>
        <label>Phone
          <input type="text" name="phone" value="${phone}" required>
        </label>
        <label>NIC/Passport
          <input type="text" name="nicPassport" value="${nicPassport}" maxlength="40" required>
        </label>
        <label>Address
          <textarea name="address" rows="3">${address}</textarea>
        </label>
      </div>
      <p class="strength-text" id="registerStrengthText">Password strength: not set.</p>
      <div class="form-actions">
        <button type="submit">Create Account</button>
        <button type="button" class="btn btn-secondary" id="registerResetBtn">Reset</button>
      </div>
    </form>

    <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
  </section>
</main>
<script>
  (function () {
    var form = document.getElementById('registerForm');
    if (!form) {
      return;
    }

    var STORAGE_KEY = 'ovr_register_draft';
    var strengthText = document.getElementById('registerStrengthText');
    var passwordInput = document.getElementById('registerPassword');
    var confirmInput = document.getElementById('registerConfirmPassword');

    function createDraft(data) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
    }

    function readDraft() {
      try {
        var raw = localStorage.getItem(STORAGE_KEY);
        return raw ? JSON.parse(raw) : null;
      } catch (error) {
        return null;
      }
    }

    function updateDraft() {
      var data = {};
      form.querySelectorAll('input[name], textarea[name]').forEach(function (field) {
        if (field.type === 'password') {
          return;
        }
        data[field.name] = field.value;
      });
      createDraft(data);
    }

    function deleteDraft() {
      localStorage.removeItem(STORAGE_KEY);
    }

    function evaluatePasswordStrength(value) {
      var score = 0;
      if (value.length >= 8) {
        score += 1;
      }
      if (/[A-Z]/.test(value)) {
        score += 1;
      }
      if (/[a-z]/.test(value)) {
        score += 1;
      }
      if (/\d/.test(value)) {
        score += 1;
      }
      if (/[^A-Za-z0-9]/.test(value)) {
        score += 1;
      }

      if (value.length === 0) {
        return 'not set';
      }
      if (score <= 2) {
        return 'weak';
      }
      if (score <= 4) {
        return 'medium';
      }
      return 'strong';
    }

    function renderStrength() {
      var label = evaluatePasswordStrength(passwordInput.value || '');
      strengthText.setAttribute('data-strength', label === 'not set' ? '' : label);
      strengthText.textContent = 'Password strength: ' + label + '.';
    }

    function validateCreate() {
      var username = form.querySelector('input[name="username"]');
      var email = form.querySelector('input[name="email"]');
      var phone = form.querySelector('input[name="phone"]');
      var nic = form.querySelector('input[name="nicPassport"]');

      var usernamePattern = /^[A-Za-z0-9._-]{4,50}$/;
      var emailPattern = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
      var phonePattern = /^[+0-9()\-\s]{7,20}$/;

      if (!usernamePattern.test(username.value || '')) {
        alert('Username must be 4-50 chars and use letters, numbers, dot, underscore, or hyphen.');
        username.focus();
        return false;
      }
      if (!emailPattern.test(email.value || '')) {
        alert('Email format is invalid.');
        email.focus();
        return false;
      }
      if (!phonePattern.test(phone.value || '')) {
        alert('Phone format is invalid.');
        phone.focus();
        return false;
      }
      if ((nic.value || '').length > 40) {
        alert('NIC/Passport must be 40 characters or less.');
        nic.focus();
        return false;
      }
      if ((passwordInput.value || '') !== (confirmInput.value || '')) {
        alert('Password and confirmation do not match.');
        confirmInput.focus();
        return false;
      }
      return true;
    }

    function resetForm() {
      form.reset();
      renderStrength();
      deleteDraft();
    }

    function applyDraftIfNeeded() {
      var draft = readDraft();
      if (!draft) {
        return;
      }
      form.querySelectorAll('input[name], textarea[name]').forEach(function (field) {
        if (field.type === 'password') {
          return;
        }
        if (!field.value && Object.prototype.hasOwnProperty.call(draft, field.name)) {
          field.value = draft[field.name];
        }
      });
    }

    form.querySelectorAll('input[name], textarea[name]').forEach(function (field) {
      field.addEventListener('input', function () {
        if (field.type !== 'password') {
          updateDraft();
        }
      });
    });

    form.querySelectorAll('[data-toggle-target]').forEach(function (button) {
      button.addEventListener('click', function () {
        var targetId = button.getAttribute('data-toggle-target');
        var target = document.getElementById(targetId);
        if (!target) {
          return;
        }
        var show = target.type === 'password';
        target.type = show ? 'text' : 'password';
        button.textContent = show ? 'Hide' : 'Show';
      });
    });

    passwordInput.addEventListener('input', renderStrength);
    form.addEventListener('submit', function (event) {
      if (!validateCreate()) {
        event.preventDefault();
        return;
      }
      deleteDraft();
    });

    document.getElementById('registerResetBtn').addEventListener('click', resetForm);

    window.registerCrud = {
      create: validateCreate,
      readDraft: readDraft,
      updateDraft: updateDraft,
      deleteDraft: deleteDraft
    };

    applyDraftIfNeeded();
    renderStrength();
  })();
</script>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
