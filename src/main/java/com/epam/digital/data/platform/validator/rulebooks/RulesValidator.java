package com.epam.digital.data.platform.validator.rulebooks;

import com.epam.digital.data.platform.validator.model.ValidationResult;

public interface RulesValidator {

  ValidationResult validate(ValidationResult validationResult);
}
