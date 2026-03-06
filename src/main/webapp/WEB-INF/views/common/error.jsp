<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
  Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
  String message = (String) request.getAttribute("jakarta.servlet.error.message");
  String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");
  Throwable exception = (Throwable) request.getAttribute("jakarta.servlet.error.exception");

  if (statusCode == null) {
    statusCode = 500;
  }

  if (message == null || message.isBlank()) {
    if (statusCode == 403) {
      message = "You do not have permission to access this resource.";
    } else if (statusCode == 404) {
      message = "The requested resource could not be found.";
    } else {
      message = "An unexpected error occurred while processing your request.";
    }
  }

  request.setAttribute("friendlyStatus", statusCode);
  request.setAttribute("friendlyMessage", message);
  request.setAttribute("friendlyRequestUri", requestUri == null ? "N/A" : requestUri);
  request.setAttribute("friendlyException", exception == null ? "" : exception.getClass().getSimpleName());
%>
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
  <jsp:param name="pageTitle" value="Error - Ocean View Resort"/>
  <jsp:param name="pageHeading" value="Request Error"/>
</jsp:include>
<style>
  .error-actions {
    margin-top: 10px;
  }

  .error-meta {
    margin-top: 10px;
    padding: 10px;
    border: 1px dashed var(--border);
    border-radius: 8px;
    background: var(--surface-alt);
    color: var(--muted);
    font-size: 0.9rem;
  }
</style>

<main class="content auth-layout">
  <section class="panel">
    <h2>Error ${friendlyStatus}</h2>
    <p class="notice notice-error" id="errorMessageText">${friendlyMessage}</p>
    <p><strong>Request:</strong> <span id="errorRequestText">${friendlyRequestUri}</span></p>
    <c:if test="${not empty friendlyException}">
      <p><strong>Details:</strong> <span id="errorExceptionText">${friendlyException}</span></p>
    </c:if>
    <div class="form-actions error-actions">
      <a class="btn" href="${pageContext.request.contextPath}/">Home</a>
      <a class="btn btn-secondary" href="${pageContext.request.contextPath}/login">Login</a>
      <button type="button" class="btn btn-secondary" id="errorBackBtn">Back</button>
      <button type="button" class="btn btn-secondary" id="errorRetryBtn">Retry</button>
      <button type="button" class="btn btn-secondary" id="errorCopyBtn">Copy Details</button>
      <button type="button" class="btn btn-secondary" id="errorClearLogsBtn">Clear Logs</button>
    </div>
    <div class="error-meta" id="errorMetaBox">Stored error logs: 0</div>
  </section>
</main>
<script>
  (function () {
    var STORAGE_KEY = 'ovr_error_logs';

    function readLogs() {
      try {
        var raw = localStorage.getItem(STORAGE_KEY);
        return raw ? JSON.parse(raw) : [];
      } catch (error) {
        return [];
      }
    }

    function createLog(item) {
      var logs = readLogs();
      logs.unshift(item);
      if (logs.length > 25) {
        logs = logs.slice(0, 25);
      }
      localStorage.setItem(STORAGE_KEY, JSON.stringify(logs));
    }

    function updateLog(index, item) {
      var logs = readLogs();
      if (index < 0 || index >= logs.length) {
        return;
      }
      logs[index] = item;
      localStorage.setItem(STORAGE_KEY, JSON.stringify(logs));
    }

    function deleteLogs() {
      localStorage.removeItem(STORAGE_KEY);
    }

    function updateMetaView() {
      var box = document.getElementById('errorMetaBox');
      if (!box) {
        return;
      }
      var logs = readLogs();
      box.textContent = 'Stored error logs: ' + logs.length;
    }

    function getErrorSnapshot() {
      var statusText = '${friendlyStatus}';
      var messageText = (document.getElementById('errorMessageText') || {}).textContent || '';
      var requestText = (document.getElementById('errorRequestText') || {}).textContent || '';
      var exceptionText = (document.getElementById('errorExceptionText') || {}).textContent || '';
      return {
        timestamp: new Date().toISOString(),
        status: statusText,
        message: messageText.trim(),
        request: requestText.trim(),
        exception: exceptionText.trim()
      };
    }

    var snapshot = getErrorSnapshot();
    createLog(snapshot);
    updateMetaView();

    var existingLogs = readLogs();
    if (existingLogs.length > 0) {
      updateLog(0, snapshot);
    }

    document.getElementById('errorBackBtn').addEventListener('click', function () {
      window.history.back();
    });

    document.getElementById('errorRetryBtn').addEventListener('click', function () {
      window.location.reload();
    });

    document.getElementById('errorCopyBtn').addEventListener('click', function () {
      var summary = [
        'Status: ' + snapshot.status,
        'Message: ' + snapshot.message,
        'Request: ' + snapshot.request,
        'Details: ' + snapshot.exception
      ].join('\n');

      if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(summary);
      }
    });

    document.getElementById('errorClearLogsBtn').addEventListener('click', function () {
      deleteLogs();
      updateMetaView();
    });

    window.errorCrud = {
      create: createLog,
      read: readLogs,
      update: updateLog,
      delete: deleteLogs
    };
  })();
</script>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
