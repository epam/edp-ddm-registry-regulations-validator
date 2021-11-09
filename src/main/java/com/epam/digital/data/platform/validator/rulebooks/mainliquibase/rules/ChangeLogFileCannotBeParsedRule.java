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

@RuleBean
@Rule(order = RulesOrder.changeLogFileCannotBeParsedRule)
public class ChangeLogFileCannotBeParsedRule {

  @Given(FactNames.MAIN_LIQUIBASE_FACADE)
  private MainLiquibaseFacade facade;

  @When
  public boolean cannotParseChangeLog() {
    return !facade.changeLogCanBeParsed();
  }

  @Result
  private ValidationResult result;

  @Then
  public RuleState then() {
    result.addError("Неможливо розпарсити " + facade.getChangeLogPath());
    return RuleState.BREAK;
  }
}
