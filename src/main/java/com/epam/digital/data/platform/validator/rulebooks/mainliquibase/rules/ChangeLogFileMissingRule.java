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
import com.epam.digital.data.platform.validator.rulebooks.mainliquibase.RulesOrder;
import java.io.File;

@RuleBean
@Rule(order = RulesOrder.changeLogFileMissingRule)
public class ChangeLogFileMissingRule {

  @Given(FactNames.MAIN_LIQUIBASE_PATH)
  private String path;

  @When
  public boolean isFileMissing() {
    return !new File(path).exists();
  }

  @Result
  private ValidationResult result;

  @Then
  public RuleState then() {
    result.addError("Файл '" + path + "' не існує");
    return RuleState.BREAK;
  }
}
