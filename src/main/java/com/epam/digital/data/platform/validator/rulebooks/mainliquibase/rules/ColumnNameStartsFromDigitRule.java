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
@Rule(order = RulesOrder.columnNameStartsFromDigitRule)
public class ColumnNameStartsFromDigitRule extends AbstractColumnNamesRule {

  private static final String FIRST_SYMBOL_IS_DIGIT_PATTERN = "^\\d.*";
  
  private List<String> columnIdentifiers;

  @When
  public boolean checkColumnNames() {

    columnIdentifiers = getCreatedColumnIdentifiers().stream()
        .filter(x -> x.matches(FIRST_SYMBOL_IS_DIGIT_PATTERN))
        .collect(Collectors.toList());

    return !columnIdentifiers.isEmpty();
  }

  @Then
  public RuleState then() {
    result.addError(
        "Назви наступних колонок починаються з цифри, що неприпустимо: " + columnIdentifiers);
    return RuleState.NEXT;
  }
}
