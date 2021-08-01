package com.epam.digital.data.platform.validator.rulebooks.settings;

import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.PACKAGE;
import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.REGISTER;
import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.RETENTION_READ;
import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.RETENTION_WRITE;
import static com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings.Field.VERSION;
import static org.assertj.core.api.Assertions.assertThat;

import com.epam.digital.data.platform.validator.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SettingsRulesValidatorTest {

  private ValidationResult result;

  @BeforeEach
  void init() {
    result = new ValidationResult();
  }

  @ParameterizedTest
  @ValueSource(strings = {"1.2.3.", "1.2..3", "1.2", "1.2.3.4", "a.2.3", "_.2.3", "1111.2.3"})
  void shouldFailOnEveryInvalidVersionFormat(String version) {
    var validator = new SettingsRulesValidator(new MockSettings().set(VERSION, version).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).hasSize(1);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1.2.3", "000.555.999"})
  void shouldNotFailOnEveryValidVersionFormat(String version) {
    var validator = new SettingsRulesValidator(new MockSettings().set(VERSION, version).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"1aa.bb", "aa.1bb", "Aa.bb", "aA.bb", "aa.bb.", "aa/bb", "aa.b+b",
      "if", "aa.default", "aa.strictfp"})
  void shouldFailOnEveryInvalidPackageFormat(String pkg) {
    var validator = new SettingsRulesValidator(new MockSettings().set(PACKAGE, pkg).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).hasSize(1);
  }

  @ParameterizedTest
  @ValueSource(strings = {"a1a.bb", "aa._1bb", "a.bb._", "a", "aa.bb", "aa_bb", "___.____", "when"})
  void shouldNotFailOnEveryValidPackageFormat(String pkg) {
    var validator = new SettingsRulesValidator(new MockSettings().set(PACKAGE, pkg).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"db.name", "dbNameMoreThan31symbol3456789012", "db-name", "5dbName"})
  void shouldFailOnEveryInvalidDatabaseName(String dbName) {
    var validator = new SettingsRulesValidator(new MockSettings().set(REGISTER, dbName).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).hasSize(1);
  }

  @ParameterizedTest
  @ValueSource(strings = {"DB_Name", "dbNameLessThan32symbol345678901", "_d_b_N_a_m_e_"})
  void shouldNotFailOnEveryValidDatabaseName(String dbName) {
    var validator = new SettingsRulesValidator(new MockSettings().set(REGISTER, dbName).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }

  @Test
  void shouldGenerateWarningWhenRetentionPolicyReadLessThen100() {
    var validator = new SettingsRulesValidator(new MockSettings().set(RETENTION_READ, 99).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).hasSize(1);
    assertThat(result.getErrors()).isEmpty();
  }

  @Test
  void shouldNotGenerateWarningWhenRetentionPolicyReadMoreThen99() {
    var validator = new SettingsRulesValidator(new MockSettings().set(RETENTION_READ, 100).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }

  @Test
  void shouldGenerateWarningWhenRetentionPolicyWriteLessThen100() {
    var validator = new SettingsRulesValidator(new MockSettings().set(RETENTION_WRITE, 99).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).hasSize(1);
    assertThat(result.getErrors()).isEmpty();
  }

  @Test
  void shouldNotGenerateWarningWhenRetentionPolicyWriteMoreThen99() {
    var validator = new SettingsRulesValidator(new MockSettings().set(RETENTION_WRITE, 100).instance);

    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }
}