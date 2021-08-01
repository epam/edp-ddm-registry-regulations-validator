package com.epam.digital.data.platform.validator.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
  private List<String> errors = new ArrayList<>();
  private List<String> warnings = new ArrayList<>();

  public void addError(String error) {
    errors.add("[ERROR] " + error);
  }

  public void addWarning(String warning) {
    warnings.add("[WARNING] " + warning);
  }
  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    this.errors = errors;
  }

  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }

  @Override
  public String toString() {
    String message = "";
    if(!warnings.isEmpty()) {
      message += "\nValidationResult.WARNINGS:\n" + String.join("\n", warnings);
    }
    if(!errors.isEmpty()) {
      message += "\nValidationResult.ERRORS:\n" + String.join("\n", errors);
    }
    return message;
  }
}
