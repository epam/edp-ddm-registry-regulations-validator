/*
 * Copyright 2021 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
