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
@Rule(order = RulesOrder.packageDoesNotMatchPatternRule)
public class PackageDoesNotMatchPatternRule {

  private static final String PACKAGE_PATTERN = "^[a-z_][a-z0-9_]*(\\.[a-z_][a-z0-9_]*){0,100}$";

  @Given(FactNames.SETTINGS_YAML)
  private SettingsYaml settingsYaml;

  @When
  public boolean isPackageIncorrect() {
    return !settingsYaml
        .getSettings()
        .getGeneral()
        .getBasePackageName()
        .matches(PACKAGE_PATTERN);
  }

  @Result
  private ValidationResult result;

  @Then
  public RuleState then() {
    result.addError("Назва пакету повинна мати вигляд: abc.de_1.ghi._123jkl."
        + " Може містити маленькі латинські літери, цифри, та знаки"
        + " підкреслення. Назва підпакету не повинна починатися з цифри");
    return RuleState.NEXT;
  }
}
