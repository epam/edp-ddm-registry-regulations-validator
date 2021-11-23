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
@Rule(order = RulesOrder.retentionPolicyReadRule)
public class RetentionPolicyReadRule {

  private static final int RETENTION_POLICY_READ_DAYS = 100;

  @Given(FactNames.SETTINGS_YAML)
  private SettingsYaml settingsYaml;

  @When
  public boolean isRetentionPolicyLessThenN() {
    return settingsYaml
        .getSettings()
        .getKafka()
        .getRetentionPolicyInDays()
        .getRead() < RETENTION_POLICY_READ_DAYS;
  }

  @Result
  private ValidationResult result;

  @Then
  public RuleState then() {
    result.addWarning(
        String.format(
            "Ви впевнені, що хочете зберігати дані у топіку READ меньше %d днів?",
            RETENTION_POLICY_READ_DAYS));
    return RuleState.NEXT;
  }
}
