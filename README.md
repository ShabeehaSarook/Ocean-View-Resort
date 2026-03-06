# Ocean View Resort

Role-based resort reservation and billing system built with Java (Jakarta EE Servlets/JSP), JDBC, MySQL, and Chart.js.

## Tech Stack
- Java 17+
- Jakarta Servlet/JSP (Tomcat 10+)
- JDBC (MySQL Connector/J)
- JSP + HTML/CSS/JavaScript
- Chart.js
- Maven Wrapper (`mvnw`)

## Roles
- Admin
- Staff
- Guest

## Prerequisites
- JDK 17 (or 21 if using the `java21` profile)
- MySQL 8+
- Apache Tomcat 10+

## Setup
1. Configure database connection:
- Edit [db.properties](D:/Intellij Projects/Ocean.View_Resort/src/main/resources/db.properties)
2. Create schema and seed data:
- Run [schema.sql](D:/Intellij Projects/Ocean.View_Resort/sql/schema.sql)
- Run [seed.sql](D:/Intellij Projects/Ocean.View_Resort/sql/seed.sql)
3. Build:
```powershell
.\mvnw.cmd clean package
```

## Deploy WAR to Tomcat
1. Build WAR:
```powershell
.\mvnw.cmd clean package
```
2. Deploy generated WAR:
- Copy [Ocean.View_Resort.war](D:/Intellij Projects/Ocean.View_Resort/target/Ocean.View_Resort.war) to Tomcat `webapps/`
3. Start Tomcat and open:
- `http://localhost:8080/Ocean.View_Resort/`

## Run Tests
```powershell
.\mvnw.cmd -s .mvn/local-settings.xml test
```

## Key Docs

### Development & Implementation
- Implementation plan: [IMPLEMENTATION_PLAN.md](IMPLEMENTATION_PLAN.md)
- DoD status: [DOD_STATUS.md](docs/DOD_STATUS.md)
- **Task D - Git/GitHub Version Control Report**: [TASK_D_GIT_GITHUB_REPORT.md](docs/TASK_D_GIT_GITHUB_REPORT.md) **NEW**

### Testing & Quality Assurance
- **Task C - Test Plan & TDD**: [TASK_C_TEST_PLAN_AND_TDD.md](docs/TASK_C_TEST_PLAN_AND_TDD.md) ⭐ **NEW**
- **Task C - Test Results**: [TEST_RESULTS_SUMMARY.md](docs/TEST_RESULTS_SUMMARY.md) ⭐ **NEW**
- **Task C - TDD Workflow**: [TDD_WORKFLOW_DIAGRAM.md](docs/TDD_WORKFLOW_DIAGRAM.md) ⭐ **NEW**
- **Task C - Quick Reference**: [TASK_C_QUICK_REFERENCE.md](docs/TASK_C_QUICK_REFERENCE.md) ⭐ **NEW**
- Testing plan: [TESTING_PLAN.md](docs/TESTING_PLAN.md)
- Manual UAT script: [MANUAL_UAT_SCRIPT.md](docs/MANUAL_UAT_SCRIPT.md)
- Manual checklist: [MANUAL_TEST_CHECKLIST.md](docs/MANUAL_TEST_CHECKLIST.md)
