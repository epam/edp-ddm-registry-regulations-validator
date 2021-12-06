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

package com.epam.digital.data.platform.validator;

import com.epam.digital.data.platform.validator.exception.ValidationException;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.RulesValidator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ValidatorApplication implements CommandLineRunner {

  private final Logger log = LoggerFactory.getLogger(ValidatorApplication.class);
  private final List<RulesValidator> rulesValidators;

  public ValidatorApplication(List<RulesValidator> rulesValidators) {
    this.rulesValidators = rulesValidators;
  }

  public static void main(String[] args) {
    SpringApplication.run(ValidatorApplication.class, args);
  }

  @Override
  public void run(String... args) {
    ValidationResult result = new ValidationResult();

    for (RulesValidator rulesValidator : rulesValidators) {
      rulesValidator.validate(result);
    }

    printResults(result);
  }

  private void printResults(ValidationResult result) {
    System.err.println(result);

    if (!result.getWarnings().isEmpty()) {
      log.warn(String.join(",", result.getWarnings()));
    }

    if (!result.getErrors().isEmpty()) {
      log.error(String.join(",", result.getErrors()));
      throw new ValidationException("Регламент не пройшов валідацію");
    } else {
      log.info("Регламент пройшов валідацію");
    }
  }
}
