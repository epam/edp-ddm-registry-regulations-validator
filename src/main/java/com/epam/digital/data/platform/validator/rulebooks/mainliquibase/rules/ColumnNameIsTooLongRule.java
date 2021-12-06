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
import com.deliveredtechnologies.rulebook.annotation.Rule;
import com.deliveredtechnologies.rulebook.annotation.Then;
import com.deliveredtechnologies.rulebook.annotation.When;
import com.deliveredtechnologies.rulebook.spring.RuleBean;
import com.epam.digital.data.platform.validator.rulebooks.mainliquibase.RulesOrder;
import java.util.List;
import java.util.stream.Collectors;

@RuleBean
@Rule(order = RulesOrder.columnNameIsTooLongRule)
public class ColumnNameIsTooLongRule extends AbstractColumnNamesRule {

  private static final int MAX_COLUMN_NAME_LENGTH = 63;
  
  private List<String> columnIdentifiers;

  @When
  public boolean checkColumnNames() {

    columnIdentifiers = getCreatedColumnIdentifiers().stream()
        .filter(x -> x.length() > MAX_COLUMN_NAME_LENGTH)
        .collect(Collectors.toList());

    return !columnIdentifiers.isEmpty();
  }

  @Then
  public RuleState then() {
    result.addError(
        String.format("Наступні колонки мають назву довше %d символів, що неприпустимо: %s",
            MAX_COLUMN_NAME_LENGTH, columnIdentifiers));
    return RuleState.NEXT;
  }
}
