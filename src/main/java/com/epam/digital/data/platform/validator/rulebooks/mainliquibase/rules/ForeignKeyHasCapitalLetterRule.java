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

package com.epam.digital.data.platform.validator.rulebooks.mainliquibase.rules;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.Given;
import com.deliveredtechnologies.rulebook.annotation.Result;
import com.deliveredtechnologies.rulebook.annotation.Rule;
import com.deliveredtechnologies.rulebook.annotation.Then;
import com.deliveredtechnologies.rulebook.annotation.When;
import com.deliveredtechnologies.rulebook.spring.RuleBean;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.FactNames;
import com.epam.digital.data.platform.validator.rulebooks.mainliquibase.MainLiquibaseFacade;
import com.epam.digital.data.platform.validator.rulebooks.mainliquibase.RulesOrder;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.CreateTableChange;

@RuleBean
@Rule(order = RulesOrder.foreignKeyHasCapitalLetterRule)
public class ForeignKeyHasCapitalLetterRule {

  private static final String HAS_UPPER_CASE_SYMBOL_PATTERN = "^.*\\p{javaUpperCase}.*$";

  private List<String> foreignKeyIdentifiers;

  @Given(FactNames.MAIN_LIQUIBASE_FACADE)
  protected MainLiquibaseFacade facade;

  @Result
  protected ValidationResult result;

  @When
  public boolean checkForeignKeys() {
    foreignKeyIdentifiers = getForeignKeys(
        facade.getChangesByType(CreateTableChange.class))
        .stream()
        .filter(fk -> fk.matches(HAS_UPPER_CASE_SYMBOL_PATTERN))
        .collect(Collectors.toList());

    return !foreignKeyIdentifiers.isEmpty();
  }

  @Then
  public RuleState then() {
    result.addError(
        String.format("Наступні foreign keys містить символи у верхньому регістрі, "
            + "що неприпустимо: %s", foreignKeyIdentifiers));
    return RuleState.NEXT;
  }


  protected Set<String> getForeignKeys(List<CreateTableChange> changes) {
    return changes.stream()
        .flatMap(x -> x.getColumns().stream())
        .map(ColumnConfig::getConstraints)
        .filter(Objects::nonNull)
        .map(ConstraintsConfig::getForeignKeyName)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }
}
