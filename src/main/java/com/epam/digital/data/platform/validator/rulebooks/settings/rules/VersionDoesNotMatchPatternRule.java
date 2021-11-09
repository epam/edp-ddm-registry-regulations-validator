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
@Rule(order = RulesOrder.versionDoesNotMatchPatternRule)
public class VersionDoesNotMatchPatternRule {

  private static final String VERSION_PATTERN = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

  @Given(FactNames.SETTINGS_YAML)
  private SettingsYaml settingsYaml;

  @When
  public boolean isVersionIncorrect() {
    return !settingsYaml
        .getSettings()
        .getGeneral()
        .getVersion()
        .matches(VERSION_PATTERN);
  }

  @Result
  private ValidationResult result;

  @Then
  public RuleState then() {
    result.addError("Версія має бути у вигляді x.х.х");
    return RuleState.NEXT;
  }
}
