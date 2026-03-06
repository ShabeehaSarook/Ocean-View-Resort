<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><c:out value="${empty param.pageTitle ? 'Ocean View Resort' : param.pageTitle}"/></title>
  <link id="ovrAppCss" rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=20260303">
  <script>
    (function () {
      var link = document.getElementById('ovrAppCss');
      if (!link) {
        return;
      }

      var href = link.getAttribute('href') || '';
      if (href.indexOf('pageContext.request.contextPath') === -1) {
        return;
      }

      var path = window.location.pathname || '/';
      var segments = path.split('/').filter(function (part) {
        return part.length > 0;
      });

      var reservedRoots = ['admin', 'staff', 'guest', 'login', 'logout', 'register', 'health', 'assets'];
      var first = segments.length > 0 ? segments[0].toLowerCase() : '';
      var contextPath = '';
      if (first && reservedRoots.indexOf(first) === -1) {
        contextPath = '/' + segments[0];
      }

      link.setAttribute('href', contextPath + '/assets/css/app.css?v=20260303');
    })();
  </script>
  <c:if test="${param.includeChartJs == 'true'}">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  </c:if>
  <style>
    :root {
      /* Enhanced Colorful Palette */
      --bg: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      --bg-light: #f8f9ff;
      --bg-gradient-start: #e0e7ff;
      --bg-gradient-end: #fef3c7;
      --surface: #ffffff;
      --surface-alt: #f9fafb;
      
      /* Text Colors */
      --text: #1f2937;
      --text-light: #6b7280;
      --text-white: #ffffff;
      --muted: #9ca3af;
      
      /* Primary Ocean Blue */
      --primary: #0891b2;
      --primary-light: #06b6d4;
      --primary-dark: #0e7490;
      --primary-gradient: linear-gradient(135deg, #06b6d4 0%, #0891b2 50%, #0e7490 100%);
      
      /* Secondary Purple */
      --secondary: #7c3aed;
      --secondary-light: #a78bfa;
      --secondary-dark: #5b21b6;
      --secondary-gradient: linear-gradient(135deg, #a78bfa 0%, #7c3aed 50%, #5b21b6 100%);
      
      /* Accent Colors */
      --accent-orange: #f97316;
      --accent-pink: #ec4899;
      --accent-green: #10b981;
      --accent-blue: #3b82f6;
      --accent-yellow: #fbbf24;
      
      /* Semantic Colors */
      --danger: #ef4444;
      --danger-light: #fef2f2;
      --danger-dark: #b91c1c;
      --danger-gradient: linear-gradient(135deg, #fca5a5 0%, #ef4444 50%, #b91c1c 100%);
      
      --success: #10b981;
      --success-light: #ecfdf5;
      --success-dark: #047857;
      --success-gradient: linear-gradient(135deg, #6ee7b7 0%, #10b981 50%, #047857 100%);
      
      --warning: #f59e0b;
      --warning-light: #fffbeb;
      --warning-dark: #d97706;
      --warning-gradient: linear-gradient(135deg, #fcd34d 0%, #f59e0b 50%, #d97706 100%);
      
      --info: #3b82f6;
      --info-light: #eff6ff;
      --info-dark: #1d4ed8;
      --info-gradient: linear-gradient(135deg, #93c5fd 0%, #3b82f6 50%, #1d4ed8 100%);
      
      /* Borders & Shadows */
      --border: #e5e7eb;
      --border-light: #f3f4f6;
      --border-dark: #d1d5db;
      --border-colorful: linear-gradient(90deg, #06b6d4, #7c3aed, #ec4899);
      
      --shadow: 0 10px 30px rgba(0, 0, 0, 0.12);
      --shadow-md: 0 4px 15px rgba(0, 0, 0, 0.1);
      --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.06);
      --shadow-lg: 0 20px 50px rgba(0, 0, 0, 0.15);
      --shadow-colorful: 0 8px 20px rgba(6, 182, 212, 0.3);
      --shadow-purple: 0 8px 20px rgba(124, 58, 237, 0.3);
      --shadow-pink: 0 8px 20px rgba(236, 72, 153, 0.3);
      
      --focus-ring: 0 0 0 3px rgba(6, 182, 212, 0.3);
      
      /* Navbar with Vibrant Gradient */
      --navbar-bg: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
      --navbar-text: #ffffff;
      --navbar-hover: rgba(255, 255, 255, 0.2);
      --navbar-active: rgba(255, 255, 255, 0.3);
    }

    body {
      margin: 0;
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
      color: var(--text);
      background: #f6f8fa;
      min-height: 100vh;
    }

    .page-shell {
      max-width: 1180px;
      margin: 0 auto;
      padding: 20px 16px 36px;
    }

    .site-header {
      background: #24292e;
      border: none;
      border-radius: 0;
      box-shadow: 0 1px 0 rgba(27, 31, 35, 0.04);
      padding: 16px 24px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 14px;
      margin-bottom: 0;
    }

    .panel,
    .table-wrap {
      background: var(--surface);
      border: 1px solid var(--border);
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 20px;
    }

    .top-nav {
      background: var(--surface);
      border: 1px solid var(--border);
      border-radius: 8px;
      padding: 12px 16px;
      margin-bottom: 8px;
    }

    .brand-link {
      color: var(--navbar-text);
      text-decoration: none;
      font-size: 1.3rem;
      font-weight: 700;
      display: flex;
      align-items: center;
      gap: 10px;
    }

    .brand-link:hover {
      text-decoration: none;
    }


    .brand-subtitle {
      margin: 4px 0 0;
      color: rgba(255, 255, 255, 0.9);
      font-size: 0.9rem;
      font-weight: 400;
    }

    .content {
      margin-top: 16px;
    }

    .auth-layout {
      max-width: 760px;
      margin: 18px auto 0;
    }

    .form-actions {
      margin-top: 10px;
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }

    .btn,
    button {
      display: inline-block;
      border: 1px solid var(--primary);
      background: var(--primary);
      color: #fff;
      border-radius: 8px;
      padding: 8px 12px;
      cursor: pointer;
      font-weight: 600;
      text-decoration: none;
      font: inherit;
    }

    .btn-secondary {
      background: #fff;
      color: var(--text);
      border-color: var(--border);
    }

    .notice {
      margin: 10px 0;
      padding: 10px 12px;
      border: 1px solid;
      border-radius: 8px;
      font-size: 0.95rem;
    }

    .notice-error {
      color: var(--danger);
      background: #fff4f3;
      border-color: #fecaca;
    }

    .notice-success {
      color: var(--success);
      background: #ecfdf3;
      border-color: #b7ebcf;
    }

    .crud-toolbar {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      align-items: center;
      margin: 12px 0;
    }

    .crud-toolbar input[type="search"] {
      max-width: 340px;
    }

    .crud-meta {
      color: var(--muted);
      font-size: 0.9rem;
    }

    .crud-total-preview {
      margin-top: 8px;
      font-weight: 600;
      color: var(--primary);
    }

    .crud-hidden {
      display: none;
    }

    .session-pill {
      background: rgba(255, 255, 255, 0.25);
      backdrop-filter: blur(15px);
      border: 2px solid rgba(255, 255, 255, 0.4);
      border-radius: 999px;
      padding: 10px 20px;
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 0.9rem;
      color: var(--navbar-text);
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
    }

    .header-brand {
      display: flex;
      align-items: center;
      gap: 12px;
    }


    .user-info {
      display: flex;
      flex-direction: column;
      gap: 2px;
    }

    .username {
      font-weight: 600;
      font-size: 0.95rem;
    }

    .role-badge {
      display: inline-block;
      padding: 3px 8px;
      border-radius: 999px;
      font-size: 0.7rem;
      font-weight: 700;
      letter-spacing: 0.05em;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .role-admin {
      background: #ef4444;
      color: #ffffff;
      border: 1px solid #dc2626;
    }

    .role-staff {
      background: #3b82f6;
      color: #ffffff;
      border: 1px solid #2563eb;
    }

    .role-guest {
      background: #10b981;
      color: #ffffff;
      border: 1px solid #059669;
    }

    .top-nav-link {
      display: inline-block;
      padding: 8px 14px;
      border-radius: 6px;
      border: 1px solid transparent;
      color: var(--text);
      font-weight: 500;
      font-size: 0.9rem;
      transition: all 0.15s ease;
      text-decoration: none;
    }

    .top-nav-link:hover {
      background: #f6f8fa;
      text-decoration: none;
    }

    .top-nav-link.active {
      background: var(--primary);
      color: white;
      border-color: var(--primary);
    }

    .nav-logout {
      margin-left: 10px;
      background: transparent;
      color: var(--danger);
      border: 1px solid var(--border);
    }

    .nav-logout:hover {
      background: var(--danger);
      color: white;
      border-color: var(--danger);
    }

    button,
    .btn {
      display: inline-block;
      border: 1px solid var(--primary);
      background: var(--primary);
      color: #fff;
      border-radius: 6px;
      padding: 8px 16px;
      cursor: pointer;
      font-weight: 500;
      font-size: 0.9rem;
      transition: all 0.15s ease;
      text-decoration: none;
    }

    button:hover,
    .btn:hover {
      background: var(--primary-dark);
      border-color: var(--primary-dark);
      text-decoration: none;
    }

    .btn-secondary {
      background: white;
      color: var(--text);
      border-color: var(--border-dark);
    }

    .btn-secondary:hover {
      background: #f6f8fa;
      border-color: var(--text-light);
    }

    .btn-success {
      background: var(--success);
      border-color: var(--success);
    }

    .btn-success:hover {
      background: var(--success-dark);
      border-color: var(--success-dark);
    }

    .btn-danger {
      background: var(--danger);
      border-color: var(--danger);
    }

    .btn-danger:hover {
      background: var(--danger-dark);
      border-color: var(--danger-dark);
    }

    .btn-warning {
      background: var(--warning);
      border-color: var(--warning);
    }

    .btn-warning:hover {
      background: var(--warning-dark);
      border-color: var(--warning-dark);
    }

    .site-footer {
      margin-top: 32px;
      padding: 24px 20px;
      background: var(--surface);
      border: 1px solid var(--border);
      border-radius: 16px;
      box-shadow: var(--shadow-md);
      color: var(--muted);
      font-size: 0.86rem;
      text-align: center;
    }

    h2 {
      margin: 0 0 16px;
      font-size: 1.4rem;
      color: var(--text);
      font-weight: 600;
    }

    h3 {
      margin: 0 0 12px;
      font-size: 1.1rem;
      color: var(--text);
      font-weight: 600;
    }

    input,
    select,
    textarea {
      width: 100%;
      border: 2px solid var(--border);
      border-radius: 12px;
      padding: 12px 16px;
      background: linear-gradient(to right, #ffffff, #f9fafb);
      color: var(--text);
      transition: all 0.3s ease;
      font-size: 0.95rem;
    }

    input:focus,
    select:focus,
    textarea:focus {
      outline: none;
      border: 2px solid transparent;
      background: linear-gradient(white, white) padding-box,
                  linear-gradient(135deg, #06b6d4, #7c3aed) border-box;
      box-shadow: var(--shadow-colorful);
    }

    .notice {
      margin: 12px 0;
      padding: 12px 16px;
      border: 1px solid;
      border-radius: 12px;
      font-size: 0.95rem;
      display: flex;
      align-items: center;
      gap: 10px;
      box-shadow: var(--shadow-sm);
    }

    .notice-error {
      color: var(--danger-dark);
      background: var(--danger-light);
      border-color: #fecaca;
    }

    .notice-error::before {
      content: "ERROR: ";
      font-weight: 700;
      font-size: 0.95rem;
    }

    .notice-success {
      color: var(--success-dark);
      background: var(--success-light);
      border-color: #86efac;
    }

    .notice-success::before {
      content: "SUCCESS: ";
      font-weight: 700;
      font-size: 0.95rem;
    }

    .card:hover,
    .panel:hover {
      box-shadow: var(--shadow-lg);
      transform: translateY(-2px);
    }

    table th {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      font-weight: 700;
      color: #ffffff;
      text-transform: uppercase;
      font-size: 0.85rem;
      letter-spacing: 0.05em;
      border-bottom: 3px solid #f093fb;
    }

    table tr:hover {
      background: linear-gradient(to right, #f9fafb, #ffffff);
      transform: scale(1.01);
      box-shadow: var(--shadow-sm);
    }

    /* Professional Admin Panel Styles */
    .admin-home {
      max-width: 1200px;
    }

    .admin-header {
      margin-bottom: 32px;
      padding-bottom: 20px;
      border-bottom: 1px solid var(--border);
    }

    .admin-header h1 {
      font-size: 1.8rem;
      font-weight: 600;
      margin: 0 0 8px 0;
      color: var(--text);
      background: none;
      -webkit-text-fill-color: var(--text);
    }

    .admin-header p {
      margin: 0;
      color: var(--muted);
      font-size: 1rem;
    }

    .admin-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 24px;
    }

    .admin-card {
      background: white;
      border: 1px solid var(--border);
      border-radius: 8px;
      overflow: hidden;
    }

    .card-header {
      padding: 16px 20px;
      border-bottom: 1px solid var(--border);
      background: #fafbfc;
    }

    .card-header h3 {
      margin: 0;
      font-size: 1.1rem;
      font-weight: 600;
      color: var(--text);
      background: none;
      -webkit-text-fill-color: var(--text);
    }

    .card-body {
      padding: 0;
    }

    .action-list {
      list-style: none;
      margin: 0;
      padding: 0;
    }

    .action-list li {
      border-bottom: 1px solid var(--border-light);
    }

    .action-list li:last-child {
      border-bottom: none;
    }

    .action-link {
      display: block;
      padding: 16px 20px;
      text-decoration: none;
      color: var(--text);
      transition: background 0.15s ease;
    }

    .action-link:hover {
      background: #f6f8fa;
      text-decoration: none;
    }

    .action-title {
      display: block;
      font-weight: 600;
      font-size: 0.95rem;
      color: var(--primary);
      margin-bottom: 4px;
    }

    .action-desc {
      display: block;
      font-size: 0.85rem;
      color: var(--muted);
    }

    .stats-note {
      padding: 20px;
      margin: 0;
      color: var(--text-light);
      line-height: 1.6;
    }

    .stats-note a {
      color: var(--primary);
      text-decoration: none;
      font-weight: 500;
    }

    .stats-note a:hover {
      text-decoration: underline;
    }

    .page-title {
      margin-bottom: 24px;
      padding-bottom: 12px;
      border-bottom: 1px solid var(--border);
    }

    .page-title h2 {
      margin: 0;
      font-size: 1.5rem;
      font-weight: 600;
      color: var(--text);
      background: none;
      -webkit-text-fill-color: var(--text);
    }

    .stat-card {
      padding: 20px;
      border-radius: 16px;
      background: white;
      border: 2px solid var(--border);
      transition: all 0.3s ease;
    }

    .stat-card:hover {
      transform: translateY(-3px);
      box-shadow: var(--shadow-md);
    }

    .stat-icon {
      font-size: 1.1rem;
      font-weight: 700;
      color: var(--primary);
      margin-bottom: 8px;
      padding: 8px 12px;
      background: linear-gradient(135deg, var(--primary-light), var(--primary));
      color: white;
      border-radius: 8px;
      display: inline-block;
    }

    .stat-content {
      margin-top: 12px;
    }

    .stat-label {
      font-size: 0.9rem;
      color: var(--muted);
      text-transform: uppercase;
      letter-spacing: 0.05em;
      margin-bottom: 8px;
    }

    .stat-value {
      font-size: 2rem;
      font-weight: 800;
      background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    .stat-card-highlight {
      border-color: var(--warning);
      background: linear-gradient(to bottom right, #fffbeb, #ffffff);
    }

    .stat-card-highlight .stat-icon {
      background: linear-gradient(135deg, var(--warning), var(--warning-dark));
    }

    .grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 20px;
      margin-bottom: 32px;
    }

    .action-buttons {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }

    .status-badge {
      padding: 4px 12px;
      border-radius: 999px;
      font-size: 0.8rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.03em;
    }

    .status-active {
      background: linear-gradient(135deg, #d1fae5, #a7f3d0);
      color: #065f46;
      border: 1px solid #6ee7b7;
    }

    .status-inactive {
      background: linear-gradient(135deg, #f3f4f6, #e5e7eb);
      color: #374151;
      border: 1px solid #d1d5db;
    }

    .status-locked {
      background: linear-gradient(135deg, #fee2e2, #fecaca);
      color: #991b1b;
      border: 1px solid #fca5a5;
    }

    .status-available {
      background: linear-gradient(135deg, #d1fae5, #a7f3d0);
      color: #065f46;
      border: 1px solid #6ee7b7;
    }

    .status-occupied {
      background: linear-gradient(135deg, #fef3c7, #fde68a);
      color: #92400e;
      border: 1px solid #fcd34d;
    }

    .status-maintenance {
      background: linear-gradient(135deg, #fed7aa, #fdba74);
      color: #9a3412;
      border: 1px solid #fb923c;
    }

    .status-pending {
      background: linear-gradient(135deg, #dbeafe, #bfdbfe);
      color: #1e40af;
      border: 1px solid #93c5fd;
    }

    .status-confirmed {
      background: linear-gradient(135deg, #d1fae5, #a7f3d0);
      color: #065f46;
      border: 1px solid #6ee7b7;
    }

    .status-checked_in {
      background: linear-gradient(135deg, #e0e7ff, #c7d2fe);
      color: #3730a3;
      border: 1px solid #a5b4fc;
    }

    .status-checked_out {
      background: linear-gradient(135deg, #f3f4f6, #e5e7eb);
      color: #374151;
      border: 1px solid #d1d5db;
    }

    .status-cancelled {
      background: linear-gradient(135deg, #fee2e2, #fecaca);
      color: #991b1b;
      border: 1px solid #fca5a5;
    }
  </style>
  <script defer src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
  <script defer src="${pageContext.request.contextPath}/assets/js/charts.js"></script>
</head>
<body>
<div class="page-shell">
  <header class="site-header">
    <div class="header-brand">
      <div>
        <a class="brand-link" href="${pageContext.request.contextPath}/">Ocean View Resort</a>
        <p class="brand-subtitle"><c:out value="${empty param.pageHeading ? 'Resort Management System' : param.pageHeading}"/></p>
      </div>
    </div>
    <c:if test="${not empty sessionScope.authUser}">
      <div class="session-pill">
        <div class="user-info">
          <span class="username"><c:out value="${sessionScope.authUser.username}"/></span>
          <span class="role-badge role-${fn:toLowerCase(sessionScope.authUser.role)}">
            <c:out value="${sessionScope.authUser.role}"/>
          </span>
        </div>
      </div>
    </c:if>
  </header>
