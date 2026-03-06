package com.oceanview.resort.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonValidatorTest {

  @Test
  void testIsBlank() {
    assertTrue(CommonValidator.isBlank(null));
    assertTrue(CommonValidator.isBlank(""));
    assertTrue(CommonValidator.isBlank("   "));
    assertTrue(CommonValidator.isBlank("\t\n"));
    
    assertFalse(CommonValidator.isBlank("text"));
    assertFalse(CommonValidator.isBlank(" text "));
  }

  @Test
  void testNormalizeUpper() {
    assertEquals("", CommonValidator.normalizeUpper(null));
    assertEquals("", CommonValidator.normalizeUpper(""));
    assertEquals("HELLO", CommonValidator.normalizeUpper("hello"));
    assertEquals("HELLO", CommonValidator.normalizeUpper("HELLO"));
    assertEquals("HELLO", CommonValidator.normalizeUpper(" hello "));
    assertEquals("HELLO WORLD", CommonValidator.normalizeUpper("hello world"));
  }
}
