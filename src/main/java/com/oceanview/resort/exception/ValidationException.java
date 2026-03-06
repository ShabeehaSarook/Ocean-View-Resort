package com.oceanview.resort.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationException extends RuntimeException {
  private final List<String> errors;

  public ValidationException(String message) {
    super(message);
    this.errors = Collections.singletonList(message);
  }

  public ValidationException(List<String> errors) {
    super(errors == null || errors.isEmpty() ? "Validation failed." : errors.get(0));
    if (errors == null || errors.isEmpty()) {
      this.errors = Collections.singletonList("Validation failed.");
    } else {
      this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
    }
  }

  public List<String> getErrors() {
    return errors;
  }
}
