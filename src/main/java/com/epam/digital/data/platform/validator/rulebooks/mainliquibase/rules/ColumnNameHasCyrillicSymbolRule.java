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
import liquibase.change.ColumnConfig;
import liquibase.change.core.CreateTableChange;

@RuleBean
@Rule(order = RulesOrder.columnNameHasCyrillicSymbolRule)
public class ColumnNameHasCyrillicSymbolRule {

  private static final String HAS_CYRILLIC_PATTERN = "^.*\\p{IsCyrillic}.*$";

  @Given(FactNames.MAIN_LIQUIBASE_FACADE)
  private MainLiquibaseFacade facade;

  @When
  public boolean checkColumnNames() {
    List<CreateTableChange> changes = facade.getChangesByType(CreateTableChange.class);

    var num = changes.stream()
        .flatMap(x -> x.getColumns().stream())
        .map(ColumnConfig::getName)
        .filter(x -> x.matches(HAS_CYRILLIC_PATTERN))
        .count();

    return num != 0;
  }

  @Result
  private ValidationResult result;

  @Then
  public RuleState then() {
    result.addError("Назва колонки містить символи кирилиці, що недопустимо");
    return RuleState.NEXT;
  }
}
