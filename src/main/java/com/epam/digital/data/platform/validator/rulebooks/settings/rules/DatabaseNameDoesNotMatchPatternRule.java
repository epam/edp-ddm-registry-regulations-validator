package com.epam.digital.data.platform.validator.rulebooks.settings.rules;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.Given;
import com.deliveredtechnologies.rulebook.annotation.Result;
import com.deliveredtechnologies.rulebook.annotation.Rule;
import com.deliveredtechnologies.rulebook.annotation.Then;
import com.deliveredtechnologies.rulebook.annotation.When;
import com.deliveredtechnologies.rulebook.spring.RuleBean;
import com.epam.digital.data.platform.validator.model.SettingsYaml;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.FactNames;
import com.epam.digital.data.platform.validator.rulebooks.settings.RulesOrder;

@RuleBean
@Rule(order = RulesOrder.databaseNameDoesNotMatchPatternRule)
public class DatabaseNameDoesNotMatchPatternRule {

  private static final String DATABASE_PATTERN = "^[a-zA-Z_]\\w{0,30}$";

  @Given(FactNames.SETTINGS_YAML)
  private SettingsYaml settingsYaml;

  @When
  public boolean isDatabaseNameIncorrect() {
    return !settingsYaml
        .getSettings()
        .getGeneral()
        .getRegister()
        .matches(DATABASE_PATTERN);
  }

  @Result
  private ValidationResult result;

  @Then
  public RuleState then() {
    result.addError("Назва бази даних повинна містити не більше ніж 31 символ, не повинна "
        + "починатися з цифри та може містити літери, цифри та знак підкреслення");
    return RuleState.NEXT;
  }
}
