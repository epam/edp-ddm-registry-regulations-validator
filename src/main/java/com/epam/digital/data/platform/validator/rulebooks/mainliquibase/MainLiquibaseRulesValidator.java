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

package com.epam.digital.data.platform.validator.rulebooks.mainliquibase;

import com.deliveredtechnologies.rulebook.Fact;
import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.model.RuleBook;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.FactNames;
import com.epam.digital.data.platform.validator.rulebooks.RulesValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MainLiquibaseRulesValidator implements RulesValidator {

  private final String mainLiquibasePath;
  private final MainLiquibaseFacade mainLiquibaseFacade;
  private final RuleBook<ValidationResult> mainLiquibaseRuleBook;

  public MainLiquibaseRulesValidator(
      @Value("${app.main-liquibase-path}") String mainLiquibasePath,
      MainLiquibaseFacade mainLiquibaseFacade,
      RuleBook<ValidationResult> mainLiquibaseRuleBook) {
    this.mainLiquibasePath = mainLiquibasePath;
    this.mainLiquibaseFacade = mainLiquibaseFacade;
    this.mainLiquibaseRuleBook = mainLiquibaseRuleBook;
  }

  @Override
  public ValidationResult validate(ValidationResult result) {
    NameValueReferableMap<Object> mainLiquibaseFacts = new FactMap<>();
    mainLiquibaseFacts.put(new Fact<>(FactNames.MAIN_LIQUIBASE_PATH, mainLiquibasePath));
    mainLiquibaseFacts.put(new Fact<>(FactNames.MAIN_LIQUIBASE_FACADE, mainLiquibaseFacade));

    mainLiquibaseRuleBook.run(mainLiquibaseFacts);

    result.addResult(mainLiquibaseRuleBook.getResult().get().getValue());
    return result;
  }
}
