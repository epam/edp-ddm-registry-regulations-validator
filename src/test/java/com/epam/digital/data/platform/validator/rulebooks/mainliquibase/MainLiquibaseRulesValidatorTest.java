package com.epam.digital.data.platform.validator.rulebooks.mainliquibase;

import static org.assertj.core.api.Assertions.assertThat;

import com.epam.digital.data.platform.validator.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainLiquibaseRulesValidatorTest {

  private ValidationResult result;

  @BeforeEach
  void init() {
    result = new ValidationResult();
  }

  @Test
  void shouldFindFile() {
    MainLiquibaseRulesValidator validator =
        new MainLiquibaseRulesValidator("src/test/resources/test-main-liquibase.xml");
    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).isEmpty();
  }

  @Test
  void shouldNotFindFile() {
    MainLiquibaseRulesValidator validator =
        new MainLiquibaseRulesValidator("src/test/resources/test-main-liquibase.json");
    validator.validate(result);

    assertThat(result.getWarnings()).isEmpty();
    assertThat(result.getErrors()).hasSize(1);
  }

  @Test
  void shouldWriteWarningWhenEmptyFolderOrContainsOnlyHiddenFiles() {
    MainLiquibaseRulesValidator validator =
        new MainLiquibaseRulesValidator("src/test/resources/data-model/test-main-liquibase.xml");
    validator.validate(result);

    assertThat(result.getWarnings()).hasSize(1);
    assertThat(result.getErrors()).isEmpty();
  }
}