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

  private static final String PLATFORM_VERSION_PATTERN = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

  @Given(FactNames.SETTINGS_YAML)
  private SettingsYaml settingsYaml;

  @When
  public boolean isVersionIncorrect() {
    return !settingsYaml
        .getSettings()
        .getGeneral()
        .getVersion()
        .matches(PLATFORM_VERSION_PATTERN);
  }

  @Result
  private ValidationResult result;

  @Then
  public RuleState then() {
    result.addError("Версія має бути у вигляді x.х.х");
    return RuleState.NEXT;
  }
}
