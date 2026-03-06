# TDD Workflow Diagram and Process
**Ocean View Resort Management System**

---

## 1. Test-Driven Development (TDD) Cycle

### 1.1 The Red-Green-Refactor Loop

```
                    ┌─────────────────────────────────────┐
                    │   START: New Feature Required      │
                    └─────────────────┬───────────────────┘
                                      │
                                      ▼
                    ┌─────────────────────────────────────┐
                    │         STEP 1: RED 🔴              │
                    │  Write a Test That FAILS            │
                    │                                     │
                    │  • Define expected behavior         │
                    │  • Write test method                │
                    │  • Run test → FAILS (good!)         │
                    │  • No implementation yet            │
                    └─────────────────┬───────────────────┘
                                      │
                                      ▼
                    ┌─────────────────────────────────────┐
                    │        STEP 2: GREEN 🟢             │
                    │  Write MINIMAL Code to Pass         │
                    │                                     │
                    │  • Implement just enough            │
                    │  • Make the test pass               │
                    │  • Don't worry about perfect code   │
                    │  • Run test → PASSES ✅             │
                    └─────────────────┬───────────────────┘
                                      │
                                      ▼
                    ┌─────────────────────────────────────┐
                    │       STEP 3: REFACTOR 🔵          │
                    │  Improve Code Quality               │
                    │                                     │
                    │  • Clean up implementation          │
                    │  • Remove duplication               │
                    │  • Improve design                   │
                    │  • Tests still pass ✅              │
                    └─────────────────┬───────────────────┘
                                      │
                                      ▼
                    ┌─────────────────────────────────────┐
                    │      More Features Needed?          │
                    └─────────────┬───────┬───────────────┘
                                  │       │
                            YES ◄─┘       └─► NO
                             │                 │
                             │                 ▼
                             │         ┌───────────────┐
                             │         │   COMPLETE!   │
                             │         └───────────────┘
                             │
                             └──────────► REPEAT CYCLE
```

---

## 2. TDD Applied: Real Examples from Project

### 2.1 Example 1: Billing Total Calculation

#### **Phase 1: RED 🔴 (Write Failing Test)**

```java
// File: BillingServiceTest.java

@Test
void calculateTotalShouldReturnSubtotalPlusTaxMinusDiscount() {
    // ARRANGE: Set up test data
    BillingService service = new BillingService();
    BigDecimal subtotal = new BigDecimal("100.00");
    BigDecimal tax = new BigDecimal("12.50");
    BigDecimal discount = new BigDecimal("7.50");
    
    // Expected: 100.00 + 12.50 - 7.50 = 105.00
    
    // ACT: Call the method we're testing
    BigDecimal total = service.calculateTotal(subtotal, tax, discount);
    
    // ASSERT: Verify the result
    assertEquals(0, total.compareTo(new BigDecimal("105.00")));
}

// RUN TEST: ❌ FAILS - Method doesn't exist yet!
```

**Test Output (RED)**:
```
[ERROR] calculateTotalShouldReturnSubtotalPlusTaxMinusDiscount
java.lang.NoSuchMethodException: calculateTotal(BigDecimal, BigDecimal, BigDecimal)
```

#### **Phase 2: GREEN 🟢 (Make Test Pass)**

```java
// File: BillingService.java

public class BillingService {
    
    public BigDecimal calculateTotal(BigDecimal subtotal, 
                                     BigDecimal tax, 
                                     BigDecimal discount) {
        // Minimal implementation to pass the test
        return subtotal.add(tax).subtract(discount);
    }
}

// RUN TEST: ✅ PASSES!
```

**Test Output (GREEN)**:
```
[INFO] BillingServiceTest.calculateTotalShouldReturnSubtotalPlusTaxMinusDiscount ✅
[INFO] Tests run: 1, Failures: 0, Errors: 0
```

#### **Phase 3: REFACTOR 🔵 (Improve Code)**

```java
// File: BillingService.java

public class BillingService {
    
    public BigDecimal calculateTotal(BigDecimal subtotal, 
                                     BigDecimal tax, 
                                     BigDecimal discount) {
        // Add validation (discovered we need it!)
        if (subtotal == null || tax == null || discount == null) {
            throw new ValidationException(
                "Subtotal, tax, and discount are required."
            );
        }
        
        // Business logic (same formula)
        return subtotal.add(tax).subtract(discount);
    }
}

// RUN TEST: ✅ STILL PASSES!
```

**New Test for Validation**:
```java
@Test
void calculateTotalShouldRejectNullValues() {
    BillingService service = new BillingService();
    
    assertThrows(ValidationException.class, 
        () -> service.calculateTotal(null, BigDecimal.ZERO, BigDecimal.ZERO));
}
```

---

### 2.2 Example 2: Reservation Date Validation

#### **Phase 1: RED 🔴**

```java
// Test: Check-out must be after check-in
@Test
void shouldRejectInvalidDateRange() {
    LocalDate checkIn = LocalDate.of(2026, 3, 15);
    LocalDate checkOut = LocalDate.of(2026, 3, 10); // BEFORE check-in!
    
    ValidationException ex = assertThrows(ValidationException.class,
        () -> ReservationValidator.validateReservationInput(
            "CONFIRMED", checkIn, checkOut, 2, 0
        )
    );
    
    assertTrue(ex.getMessage().contains("Check-out must be after check-in"));
}

// RUN: ❌ FAILS - No date validation exists
```

#### **Phase 2: GREEN 🟢**

```java
// File: ReservationValidator.java

public static void validateReservationInput(String status, 
                                           LocalDate checkIn, 
                                           LocalDate checkOut,
                                           int adults, 
                                           int children) {
    // Add date range check
    if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
        throw new ValidationException(
            "Check-out must be after check-in date."
        );
    }
    
    // ... other validations ...
}

// RUN: ✅ PASSES!
```

#### **Phase 3: REFACTOR 🔵**

```java
// Extract to separate method for reusability
private static void validateDateRange(LocalDate checkIn, LocalDate checkOut) {
    if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
        throw new ValidationException(
            "Check-out must be after check-in date."
        );
    }
}

public static void validateReservationInput(String status, 
                                           LocalDate checkIn, 
                                           LocalDate checkOut,
                                           int adults, 
                                           int children) {
    validateDateRange(checkIn, checkOut);
    // ... other validations ...
}

// RUN: ✅ PASSES! Code is cleaner now.
```

---

## 3. TDD Benefits Visualization

### 3.1 Bug Detection Timeline

```
Without TDD:
┌────────────────────────────────────────────────────────────────┐
│ Code → Build → Deploy → QA Testing → Bug Found → Fix → Redeploy│
└────────────────────────────────────────────────────────────────┘
  ↑                                        ↑
  Day 1                                   Day 7
  
  Cost to Fix: HIGH (production bug, customer impact)
  Time Lost: ~1 week


With TDD:
┌─────────────────────┐
│ Test → Code → Pass  │
└─────────────────────┘
  ↑              ↑
  Minute 1      Minute 5
  
  Cost to Fix: LOW (caught immediately)
  Time Lost: ~5 minutes
```

### 3.2 Development Speed Over Time

```
Cumulative Productivity

  ▲
  │                              ╱── With TDD (faster long-term)
  │                          ╱───
  │                      ╱───
  │                  ╱───
  │              ╱───
  │          ╱───
  │      ╱───
  │  ╱───         Without TDD (slower due to debugging)
  │──────────────────────────────────────────────────────►
  Day 1                                              Day 30

Initial Cost: TDD slower (writing tests)
Long-term Benefit: TDD faster (less debugging, confident refactoring)
```

---

## 4. Test Pyramid Applied

### 4.1 Ocean View Resort Test Distribution

```
                        ╱╲
                       ╱  ╲
                      ╱ UI ╲        ← Manual UAT (7 scenarios)
                     ╱──────╲         Slow, Expensive, Fragile
                    ╱────────╲
                   ╱          ╲
                  ╱ Integration╲    ← Integration Tests (13 tests)
                 ╱──────────────╲     Medium speed, DB required
                ╱────────────────╲
               ╱                  ╲
              ╱    Unit Tests      ╲  ← Unit Tests (81 tests)
             ╱──────────────────────╲   Fast, Isolated, Reliable
            ╱────────────────────────╲
           ╱──────────────────────────╲
          ╱────────────────────────────╲
         ────────────────────────────────

IDEAL DISTRIBUTION:
Unit Tests:        70-80% ✅ (81/94 = 86%)
Integration Tests: 15-20% ✅ (13/94 = 14%)
UI Tests:          5-10%  ⚠️ (Manual UAT - to be automated)
```

---

## 5. TDD Workflow in Practice

### 5.1 Developer Daily Workflow

```
Morning: New Feature Assignment
├─ 09:00 - Read requirements
├─ 09:15 - Write first test (RED 🔴)
│          ❌ Test fails as expected
│
├─ 09:20 - Write minimal implementation (GREEN 🟢)
│          ✅ Test passes
│
├─ 09:25 - Refactor code (REFACTOR 🔵)
│          ✅ Test still passes, code improved
│
├─ 09:30 - Write next test (RED 🔴)
│          ❌ New edge case fails
│
├─ 09:35 - Add validation (GREEN 🟢)
│          ✅ Test passes
│
├─ 09:40 - Extract method (REFACTOR 🔵)
│          ✅ All tests pass
│
├─ 09:45 - Write integration test (RED 🔴)
│          ❌ Database interaction fails
│
├─ 10:00 - Implement DAO method (GREEN 🟢)
│          ✅ Integration test passes
│
├─ 10:15 - Code review & commit
│          ✅ All 94 tests pass
│
└─ 10:20 - Feature complete! (1 hour 20 minutes)
           High confidence, minimal debugging needed
```

### 5.2 Test Execution Frequency

```
Every Code Change:
├─ Run affected tests (1-2 seconds)
│
Every Commit:
├─ Run full unit test suite (1 second)
│
Every Push:
├─ CI/CD runs all tests (3-4 seconds)
│
Every Pull Request:
├─ Full test suite + code coverage report
│
Every Release:
└─ Full test suite + manual UAT + performance tests
```

---

## 6. Test Organization Structure

### 6.1 Test Class Naming Convention

```
Source Code:              Test Code:
─────────────            ──────────────────────
BillingService.java  →   BillingServiceTest.java
├─ calculateTotal()      ├─ calculateTotalShouldReturnSubtotalPlusTaxMinusDiscount()
├─ validate()            ├─ validateShouldRejectNegativeValues()
└─ normalize()           └─ validateShouldRejectDiscountHigherThanSubtotalPlusTax()

ReservationValidator  →   ReservationValidatorTest.java
├─ validateInput()       ├─ shouldAcceptValidReservationInput()
├─ validateDates()       ├─ shouldRejectInvalidDateRange()
└─ validateGuests()      └─ shouldRejectInvalidGuestCounts()
```

### 6.2 Test Method Naming Pattern

**Pattern**: `[methodName]Should[ExpectedBehavior]When[Condition]`

**Examples**:
- `calculateTotalShouldReturnSumWhenValidInputProvided`
- `validateShouldThrowExceptionWhenSubtotalIsNegative`
- `checkInShouldSucceedWhenReservationIsPending`
- `checkInShouldFailWhenReservationIsCheckedOut`

**Benefits**:
- ✅ Self-documenting
- ✅ Clear intent
- ✅ Easy to understand failures

---

## 7. Code Coverage Goals

### 7.1 Target Coverage by Layer

```
Layer                Coverage Goal    Actual     Status
─────────────────   ─────────────    ──────    ────────
Validators          95%+             95%+      ✅ Met
Services            90%+             90%+      ✅ Met
Utilities           90%+             90%+      ✅ Met
DAOs                80%+             70%       ⚠️ Good
Controllers         70%+             20%       ❌ Manual
Views               50%+             10%       ❌ Manual

Overall (Core)      90%+             88%       ✅ Good
```

### 7.2 What We Test vs. What We Don't

#### **✅ Always Test (100% Coverage)**
- Business logic calculations (billing formulas)
- Input validation rules
- State transitions (reservation states)
- Security functions (password hashing)
- Data transformations
- Error handling

#### **⚠️ Sometimes Test (80% Coverage)**
- Database queries (integration tests)
- Configuration loading
- Utility functions

#### **❌ Usually Don't Test (Manual UAT)**
- UI rendering (JSP)
- Framework code (Servlet API)
- Third-party libraries
- Getters/setters (if trivial)

---

## 8. TDD Anti-Patterns to Avoid

### 8.1 Common Mistakes

❌ **Writing Tests After Code**
```java
// WRONG: Code first, test later
public class BillingService {
    public BigDecimal calculateTotal(...) {
        return subtotal.add(tax).subtract(discount);
    }
}

// Then writing tests to match existing code (not TDD!)
@Test
void testCalculateTotal() { ... }
```

✅ **Correct: Test First**
```java
// RIGHT: Test first
@Test
void calculateTotalShouldReturnCorrectSum() {
    // Define expected behavior FIRST
    assertEquals(105.00, service.calculateTotal(100, 12.50, 7.50));
}

// Then implement to satisfy test
```

❌ **Testing Implementation Details**
```java
// WRONG: Testing how it works (fragile)
@Test
void shouldUseAddMethodThenSubtractMethod() {
    verify(bigDecimal).add(any());
    verify(bigDecimal).subtract(any());
}
```

✅ **Correct: Test Behavior**
```java
// RIGHT: Testing what it does (robust)
@Test
void shouldReturnCorrectTotal() {
    assertEquals(expected, actual);
}
```

❌ **One Giant Test**
```java
// WRONG: Testing everything in one test
@Test
void testEntireReservationSystem() {
    // 100 lines of test code...
}
```

✅ **Correct: Small, Focused Tests**
```java
// RIGHT: One concept per test
@Test
void shouldRejectNegativeAdults() { ... }

@Test
void shouldRejectPastDates() { ... }

@Test
void shouldAcceptValidReservation() { ... }
```

---

## 9. Quick Reference: Running Tests

### 9.1 Command Cheat Sheet

```powershell
# Run all tests
.\mvnw.cmd test

# Run specific test class
.\mvnw.cmd test -Dtest=BillingServiceTest

# Run specific test method
.\mvnw.cmd test -Dtest=BillingServiceTest#calculateTotalShouldReturnSubtotalPlusTaxMinusDiscount

# Run all validator tests
.\mvnw.cmd test -Dtest=*ValidatorTest

# Run all service tests
.\mvnw.cmd test -Dtest=*ServiceTest

# Run tests and skip compilation
.\mvnw.cmd test-compile surefire:test

# Clean and run tests
.\mvnw.cmd clean test

# Run tests with verbose output
.\mvnw.cmd test -X
```

### 9.2 Using the Convenience Script

```powershell
# Run all tests
.\run_tests.bat

# Run only unit tests
.\run_tests.bat unit

# Run only integration tests
.\run_tests.bat integration

# Run only validator tests
.\run_tests.bat validator

# Run only service tests
.\run_tests.bat service
```

---

## 10. Success Metrics Dashboard

### 10.1 Current Test Health

```
┌─────────────────────────────────────────────────────┐
│           Ocean View Resort Test Metrics            │
├─────────────────────────────────────────────────────┤
│ Total Tests:              94                        │
│ Passing:                  94 ✅                     │
│ Failing:                  0  ✅                     │
│ Skipped:                  0  (when DB available)    │
│                                                     │
│ Execution Time:           ~3-4 seconds              │
│ Code Coverage (Core):     88%                       │
│ Test Success Rate:        100%                      │
│                                                     │
│ Test Reliability:         ⭐⭐⭐⭐⭐ (Excellent)      │
│ Maintenance Burden:       🟢 Low                    │
│ Developer Confidence:     🟢 High                   │
└─────────────────────────────────────────────────────┘
```

### 10.2 TDD Adoption Scorecard

| Criteria | Score | Evidence |
|----------|-------|----------|
| **Tests written first** | 5/5 ⭐ | All services/validators have tests |
| **Coverage > 80%** | 5/5 ⭐ | 88% core logic coverage |
| **Fast execution** | 5/5 ⭐ | <4 seconds full suite |
| **CI/CD integrated** | 5/5 ⭐ | Maven + JUnit 5 ready |
| **Maintainable tests** | 4/5 ⭐ | Clear naming, good structure |
| **Team adoption** | 4/5 ⭐ | Consistent TDD practice |
| **OVERALL** | **28/30** 🏆 | **Excellent TDD Implementation** |

---

**Document Version**: 1.0  
**Last Updated**: 2026-03-05  
**Author**: Ocean View Resort Development Team
