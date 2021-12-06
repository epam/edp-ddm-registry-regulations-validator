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

import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.PACKAGE;
import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.REGISTER;
import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.RETENTION_READ;
import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.RETENTION_WRITE;
import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.VERSION;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliveredtechnologies.rulebook.model.RuleBook;
import com.epam.digital.data.platform.validator.model.SettingsYaml;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.config.SettingsYamlTestConfig;
import com.epam.digital.data.platform.validator.rulebooks.mainliquibase.MainLiquibaseFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(classes = {SettingsYamlRulesValidator.class})
@Import(SettingsYamlTestConfig.class)
class SettingsRulesValidatorTest {

  @Autowired
  SettingsYamlRulesValidator validator;
  @Autowired
  RuleBook<ValidationResult> settingsYamlRuleBook;

  @MockBean
  MainLiquibaseFacade service;

  private ValidationResult result;

  @BeforeEach
  void init() {
    result = new ValidationResult();
    settingsYamlRuleBook.setDefaultResult(new ValidationResult());
  }

  @ParameterizedTest
  @ValueSource(strings = {"1.2.3.", "1.2..3", "1.2", "1.2.3.4", "a.2.3", "_.2.3", "1111.2.3"})
  void shouldWriteErrorOnEveryInvalidVersionFormat(String version) {
    SettingsYaml settingsYaml = new MockSettings().set(VERSION, version).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).hasSize(1);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1.2.3", "000.555.999"})
  void shouldNotFailOnEveryValidVersionFormat(String version) {
    SettingsYaml settingsYaml = new MockSettings().set(VERSION, version).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"1aa.bb", "aa.1bb", "Aa.bb", "aA.bb", "aa.bb.", "aa/bb", "aa.b+b",
      "if", "aa.default", "aa.strictfp", "aa.bb._"})
  void shouldFailOnEveryInvalidPackageFormat(String pkg) {
    SettingsYaml settingsYaml = new MockSettings().set(PACKAGE, pkg).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).hasSize(1);
  }

  @ParameterizedTest
  @ValueSource(strings = {"a1a.bb", "aa._1bb", "a.bb.__", "a", "aa.bb", "aa_bb", "___.____",
      "when"})
  void shouldNotFailOnEveryValidPackageFormat(String pkg) {
    SettingsYaml settingsYaml = new MockSettings().set(PACKAGE, pkg).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"db.name", "dbNameMoreThan31symbol3456789012", "db-name", "5dbName"})
  void shouldFailOnEveryInvalidDatabaseName(String dbName) {
    SettingsYaml settingsYaml = new MockSettings().set(REGISTER, dbName).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).hasSize(1);
  }

  @ParameterizedTest
  @ValueSource(strings = {"DB_Name", "dbNameLessThan32symbol345678901", "_d_b_N_a_m_e_"})
  void shouldNotFailOnEveryValidDatabaseName(String dbName) {
    SettingsYaml settingsYaml = new MockSettings().set(REGISTER, dbName).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }

  @Test
  void shouldGenerateWarningWhenRetentionPolicyReadLessThen100() {
    SettingsYaml settingsYaml = new MockSettings().set(RETENTION_READ, 99).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).hasSize(1);
    assertThat(result.getErrors()).isEmpty();
  }

  @Test
  void shouldNotGenerateWarningWhenRetentionPolicyReadMoreThen99() {
    SettingsYaml settingsYaml = new MockSettings().set(RETENTION_READ, 100).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }

  @Test
  void shouldGenerateWarningWhenRetentionPolicyWriteLessThen100() {
    SettingsYaml settingsYaml = new MockSettings().set(RETENTION_WRITE, 99).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).hasSize(1);
    assertThat(result.getErrors()).isEmpty();
  }

  @Test
  void shouldNotGenerateWarningWhenRetentionPolicyWriteMoreThen99() {
    SettingsYaml settingsYaml = new MockSettings().set(RETENTION_WRITE, 100).instance;
    ReflectionTestUtils.setField(validator, "settingsYaml", settingsYaml);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }
}
