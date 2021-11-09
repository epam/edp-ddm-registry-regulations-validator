package com.epam.digital.data.platform.validator.rulebooks.settings;

import com.deliveredtechnologies.rulebook.Fact;
import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.model.RuleBook;
import com.epam.digital.data.platform.validator.model.SettingsYaml;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.FactNames;
import com.epam.digital.data.platform.validator.rulebooks.RulesValidator;
import org.springframework.stereotype.Component;

@Component
public class SettingsYamlRulesValidator implements RulesValidator {

  private final RuleBook<ValidationResult> settingsYamlRuleBook;

  SettingsYaml settingsYaml;

  public SettingsYamlRulesValidator(SettingsYaml settingsYaml,
      RuleBook<ValidationResult> settingsYamlRuleBook) {
    this.settingsYaml = settingsYaml;
    this.settingsYamlRuleBook = settingsYamlRuleBook;
  }

  @Override
  public ValidationResult validate(ValidationResult result) {
    NameValueReferableMap<SettingsYaml> settingsYamlFacts = new FactMap<>();
    settingsYamlFacts.put(new Fact<>(FactNames.SETTINGS_YAML, settingsYaml));

    settingsYamlRuleBook.run(settingsYamlFacts);

    result.addResult(settingsYamlRuleBook.getResult().get().getValue());
    return result;
  }
}
