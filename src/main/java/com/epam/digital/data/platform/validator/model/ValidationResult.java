/*
 * Copyright 2021 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  public void addResult(ValidationResult other) {
    errors.addAll(other.getErrors());
    warnings.addAll(other.getWarnings());
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
