package com.epam.digital.data.platform.validator.rulebooks.settings;

import com.deliveredtechnologies.rulebook.Fact;
import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.lang.RuleBookBuilder;
import com.deliveredtechnologies.rulebook.model.RuleBook;
import com.epam.digital.data.platform.validator.model.SettingsYaml;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.RulesValidator;
import org.springframework.stereotype.Component;

@Component
public class SettingsRulesValidator implements RulesValidator {

  private final NameValueReferableMap<SettingsYaml> settingsYamlFacts = new FactMap<>();

  public SettingsRulesValidator(SettingsYaml settingsYaml) {
    settingsYamlFacts.put(new Fact<>(settingsYaml));
  }

  @Override
  public ValidationResult validate(ValidationResult result) {
    RuleBook<ValidationResult> ruleBook =
        RuleBookBuilder.create(SettingsRules.class)
            .withResultType(ValidationResult.class)
            .withDefaultResult(result)
            .build();

    ruleBook.run(settingsYamlFacts);
    return result;
  }
}
