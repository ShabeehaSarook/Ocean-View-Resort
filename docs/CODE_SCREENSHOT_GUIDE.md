# Code Screenshot Guide for TASK_B_INTERACTIVE_SYSTEM.md

This guide provides step-by-step instructions for capturing professional code screenshots to include in your Task B documentation.

---

## Method 1: Using Visual Studio Code (Recommended)

### Step 1: Install Polacode Extension

1. Open Visual Studio Code
2. Go to Extensions (Ctrl+Shift+X or Cmd+Shift+X on Mac)
3. Search for "Polacode-2022" or "CodeSnap"
4. Click Install

### Step 2: Take Screenshots with Polacode

1. Open the Java file you want to screenshot
2. Select the code you want to capture (highlight it)
3. Press `Ctrl+Shift+P` (or `Cmd+Shift+P` on Mac)
4. Type "Polacode" or "CodeSnap"
5. Select "Polacode: Activate" or "CodeSnap"
6. A preview window will open
7. Click the camera icon or press the save button
8. Save the image to `docs/screenshots/` folder

### Benefits:
- Professional appearance with syntax highlighting
- Clean, gradient background
- Includes file name and line numbers
- High quality PNG output

---

## Method 2: Using Carbon (Online Tool)

### Step 1: Access Carbon

1. Go to https://carbon.now.sh
2. Or use https://ray.so (alternative with more themes)

### Step 2: Prepare Your Code

1. Copy the code from your Java file
2. Paste it into the Carbon text area

### Step 3: Customize Appearance

**Theme:** Select a professional theme
- Monokai (recommended)
- Dracula
- One Dark
- Solarized

**Language:** Select "Java"

**Font:** 
- Fira Code (recommended)
- JetBrains Mono
- Source Code Pro

**Background:**
- Enable gradient background
- Or use solid color

**Padding:** Adjust to 64px

**Window Controls:** Enable for professional look

### Step 4: Export

1. Click "Export" button
2. Choose PNG format
3. Save to `docs/screenshots/` folder

### Benefits:
- No software installation needed
- Multiple theme options
- Customizable appearance
- Professional gradient backgrounds

---

## Method 3: Using IntelliJ IDEA Screenshots

### Step 1: Configure IntelliJ

1. Open your project in IntelliJ IDEA
2. Go to Settings → Editor → Color Scheme
3. Select a professional theme (Darcula or IntelliJ Light)

### Step 2: Take Screenshot

1. Open the Java file
2. Select the code you want to capture
3. Use Windows Snipping Tool or:
   - Windows: Win+Shift+S
   - Mac: Cmd+Shift+4
4. Crop to show only the code area
5. Save to `docs/screenshots/` folder

### Step 3: Enhance (Optional)

Use an image editor to:
- Add subtle shadow
- Adjust borders
- Crop precisely

---

## Method 4: Using Browser Developer Tools (for JSP/HTML/CSS)

### For JSP Files:

1. Open the JSP file in VS Code
2. Use Polacode or CodeSnap extension
3. Follow Method 1 steps above

### For HTML in Browser:

1. Open your running application
2. Right-click → Inspect Element
3. Use browser screenshot tools:
   - Firefox: Right-click element → Screenshot Node
   - Chrome: Elements tab → Right-click element → Capture node screenshot

---

## Recommended Screenshots for Your Document

### Section 2: Design Patterns Implementation

#### 2.1 Repository Pattern
**File:** `src/main/java/com/oceanview/resort/dao/UserDAO.java`
- Capture the interface definition (lines 7-19)

**File:** `src/main/java/com/oceanview/resort/dao/impl/UserJdbcDAO.java`
- Capture the `create()` method showing PreparedStatement usage
- Capture the `findByUsername()` method

#### 2.2 Service Layer Pattern
**File:** `src/main/java/com/oceanview/resort/service/RegisterService.java`
- Capture entire class (constructor injection + methods)

**File:** `src/main/java/com/oceanview/resort/service/ReservationService.java`
- Capture business rule methods (ensureCheckInAllowed, etc.)

#### 2.3 Front Controller Pattern
**File:** `src/main/java/com/oceanview/resort/controller/AuthServlet.java`
- Capture the @WebServlet annotation and doPost method

#### 2.5 Strategy Pattern
**File:** `src/main/java/com/oceanview/resort/validation/RegisterValidator.java`
- Capture the validateForGuestRegistration method

#### 2.7 Filter Chain Pattern
**File:** `src/main/java/com/oceanview/resort/filter/AuthFilter.java`
- Capture the doFilter method

**File:** `src/main/java/com/oceanview/resort/filter/RoleFilter.java`
- Capture the doFilter method and isRoleAllowed method

---

### Section 3: Three-Tier Architecture

#### Presentation Layer
**File:** `src/main/java/com/oceanview/resort/controller/RegisterServlet.java`
- Capture the doPost method showing delegation to service layer

#### Business Logic Layer
**File:** `src/main/java/com/oceanview/resort/service/BillingService.java`
- Capture calculateTotal and validateForCreateOrUpdate methods

#### Data Access Layer
**File:** `src/main/java/com/oceanview/resort/dao/impl/ReservationJdbcDAO.java`
- Capture the existsOverlappingReservation method

---

### Section 4: Validation Mechanisms

#### Client-Side Validation
**File:** `src/main/webapp/assets/js/validation.js`
- Capture the validateRegistrationForm function

#### Server-Side Validation
**File:** `src/main/java/com/oceanview/resort/validation/ReservationValidator.java`
- Capture the validateReservation method

#### Database Constraints
**File:** `sql/schema.sql`
- Capture CHECK constraints for reservations table
- Capture CHECK constraints for bills table

---

### Section 5: Database Implementation

**File:** `sql/schema.sql`
- Screenshot of users table with constraints
- Screenshot of reservations table with foreign keys
- Screenshot of bills table with CHECK constraints
- Screenshot of indexes

---

### Section 6: Reports for Decision-Making

**File:** `src/main/java/com/oceanview/resort/api/ReportApiServlet.java`
- Capture the doGet method showing endpoint routing
- Capture one report method (e.g., usersByRoleData)

**File:** `src/main/webapp/assets/js/charts.js`
- Capture the loadChart function

---

### Section 7: User Interface

**File:** `src/main/webapp/WEB-INF/views/fragments/nav.jspf`
- Capture role-based navigation JSP code

**File:** `src/main/webapp/assets/css/app.css`
- Capture responsive design CSS

**Browser Screenshots:**
- Screenshot of Admin Dashboard showing charts
- Screenshot of Guest Dashboard
- Screenshot of Staff Reservations page
- Screenshot of login page with validation

---

### Section 8: Web Services

**File:** `src/main/java/com/oceanview/resort/api/ReportApiServlet.java`
- Capture RESTful endpoint implementation

**Browser Developer Tools:**
- Screenshot of Network tab showing API call
- Screenshot of JSON response

---

### Section 9: Session Management

**File:** `src/main/java/com/oceanview/resort/controller/AuthServlet.java`
- Capture login method with session creation

**File:** `src/main/java/com/oceanview/resort/filter/AuthFilter.java`
- Capture session validation code

**Browser Developer Tools:**
- Screenshot of Application tab showing session storage
- Screenshot of Cookies showing JSESSIONID

---

## Creating Screenshot Folder Structure

```
docs/
├── TASK_B_INTERACTIVE_SYSTEM.md
└── screenshots/
    ├── design-patterns/
    │   ├── repository-dao-interface.png
    │   ├── repository-jdbc-impl.png
    │   ├── service-layer-register.png
    │   ├── front-controller-auth.png
    │   ├── filter-chain-auth.png
    │   └── filter-chain-role.png
    ├── architecture/
    │   ├── presentation-layer.png
    │   ├── business-layer.png
    │   └── data-layer.png
    ├── validation/
    │   ├── client-side-js.png
    │   ├── server-side-validator.png
    │   └── database-constraints.png
    ├── database/
    │   ├── schema-users.png
    │   ├── schema-reservations.png
    │   ├── foreign-keys.png
    │   └── indexes.png
    ├── reports/
    │   ├── api-servlet.png
    │   ├── chart-js.png
    │   └── dashboard-screenshot.png
    ├── ui/
    │   ├── admin-dashboard.png
    │   ├── guest-dashboard.png
    │   ├── login-page.png
    │   └── navigation.png
    └── session/
        ├── session-creation.png
        ├── session-filter.png
        └── cookies-screenshot.png
```

---

## Step-by-Step: Creating Professional Screenshots

### Quick Start Guide

1. **Create folder structure:**
   ```bash
   mkdir -p docs/screenshots/{design-patterns,architecture,validation,database,reports,ui,session}
   ```

2. **Install VS Code extension:**
   - Open VS Code
   - Install "Polacode-2022" or "CodeSnap"

3. **For each code section:**
   - Open the file
   - Select the relevant code
   - Ctrl+Shift+P → "Polacode" or "CodeSnap"
   - Save with descriptive name in appropriate folder

4. **For running application:**
   - Start your application
   - Open in browser
   - Use browser screenshot tools or Snipping Tool
   - Capture UI elements and dashboards

5. **For database:**
   - Open MySQL Workbench or your DB tool
   - Navigate to schema
   - Capture table definitions
   - Or screenshot the SQL file in VS Code

---

## Best Practices

### Code Screenshots

✅ **DO:**
- Use syntax highlighting
- Include line numbers
- Show file name/path
- Keep consistent theme
- Use high resolution (300 DPI minimum)
- Crop tightly to relevant code
- Use professional themes (Monokai, Dracula, One Dark)

❌ **DON'T:**
- Use phone camera to photograph screen
- Include unnecessary whitespace
- Mix different color schemes
- Use low resolution
- Include sensitive information (passwords, API keys)

### UI Screenshots

✅ **DO:**
- Use full application in browser
- Show realistic data (not "test123")
- Capture complete features
- Use consistent browser
- Show responsive design on different sizes

❌ **DON'T:**
- Include browser bookmarks/tabs
- Show personal information
- Use development console unless needed
- Capture broken layouts

### File Naming

Use descriptive names:
- `repository-pattern-dao-interface.png`
- `service-layer-billing-service.png`
- `validation-client-side-javascript.png`
- `database-foreign-key-constraints.png`
- `ui-admin-dashboard-with-charts.png`

---

## Inserting Screenshots into Markdown

### Method 1: Relative Path (Recommended)

```markdown
![Repository Pattern - DAO Interface](screenshots/design-patterns/repository-dao-interface.png)

*Figure 1: UserDAO interface showing Repository Pattern implementation*
```

### Method 2: With Width Control

```markdown
<img src="screenshots/design-patterns/repository-dao-interface.png" width="600" alt="Repository Pattern">

*Figure 1: UserDAO interface showing Repository Pattern implementation*
```

### Method 3: Side-by-Side Images

```markdown
<table>
<tr>
<td><img src="screenshots/design-patterns/dao-interface.png" width="400"></td>
<td><img src="screenshots/design-patterns/dao-impl.png" width="400"></td>
</tr>
<tr>
<td align="center"><em>DAO Interface</em></td>
<td align="center"><em>JDBC Implementation</em></td>
</tr>
</table>
```

---

## Tools Download Links

1. **Visual Studio Code:** https://code.visualstudio.com/
2. **Polacode Extension:** Search in VS Code Extensions
3. **CodeSnap Extension:** Search in VS Code Extensions
4. **Carbon:** https://carbon.now.sh
5. **Ray.so:** https://ray.so
6. **Snipping Tool:** Built into Windows
7. **Greenshot (Windows):** https://getgreenshot.org/
8. **Lightshot:** https://app.prntscr.com/
9. **Snagit (Paid):** https://www.techsmith.com/screen-capture.html

---

## Example: Complete Screenshot Workflow

### Taking Screenshot of RegisterValidator.java

1. **Open File:**
   - Open `src/main/java/com/oceanview/resort/validation/RegisterValidator.java`

2. **Select Code:**
   - Select the `validateForGuestRegistration` method (entire method)

3. **Activate Polacode:**
   - Press `Ctrl+Shift+P`
   - Type "Polacode"
   - Press Enter

4. **Customize (if needed):**
   - Adjust zoom if text is too small
   - Ensure entire method is visible

5. **Save:**
   - Click save icon
   - Navigate to `docs/screenshots/validation/`
   - Name: `server-side-register-validator.png`
   - Save

6. **Insert in Document:**
   ```markdown
   ![Server-Side Validation](screenshots/validation/server-side-register-validator.png)
   
   *Figure 4.2: RegisterValidator showing comprehensive validation rules*
   ```

---

## Troubleshooting

### Issue: Extension not working
**Solution:** Restart VS Code after installation

### Issue: Screenshot too large
**Solution:** Use image compression tool like TinyPNG.com

### Issue: Code text too small
**Solution:** Increase editor font size before screenshot (Ctrl++ or Cmd++)

### Issue: Wrong color scheme
**Solution:** Change VS Code theme: File → Preferences → Color Theme

### Issue: Can't capture long code
**Solution:** Break into multiple screenshots or use Carbon.now.sh with scrolling

---

## Quick Reference Commands

### VS Code Screenshot
```
Ctrl+Shift+P → "Polacode" → Select code → Save
```

### Windows Screenshot
```
Win+Shift+S → Select area → Ctrl+V to paste
```

### Mac Screenshot
```
Cmd+Shift+4 → Select area → Auto-saved to Desktop
```

### Browser Element Screenshot (Firefox)
```
F12 → Right-click element → Screenshot Node
```

---

## Checklist Before Submission

- [ ] All code screenshots have syntax highlighting
- [ ] File names are visible in screenshots
- [ ] Screenshots are high resolution
- [ ] Consistent theme across all code screenshots
- [ ] UI screenshots show professional data
- [ ] All screenshots properly labeled in document
- [ ] Screenshots folder organized by category
- [ ] Image file sizes optimized (< 500KB each)
- [ ] All images referenced in markdown work correctly

---

Good luck with your documentation! The screenshots will make your Task B submission much more professional and easier to evaluate.
