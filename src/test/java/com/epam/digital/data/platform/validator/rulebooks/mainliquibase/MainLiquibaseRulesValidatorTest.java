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

package com.epam.digital.data.platform.validator.rulebooks.mainliquibase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.deliveredtechnologies.rulebook.model.RuleBook;
import com.epam.digital.data.platform.validator.config.MainLiquibaseConfig;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import java.util.List;
import liquibase.change.ColumnConfig;
import liquibase.change.core.CreateTableChange;
import liquibase.structure.core.Column;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(classes = {MainLiquibaseRulesValidator.class})
@ContextConfiguration(classes = {MainLiquibaseConfig.class})
class MainLiquibaseRulesValidatorTest {

  @Autowired
  MainLiquibaseRulesValidator validator;
  @Autowired
  RuleBook<ValidationResult> mainLiquibaseRuleBook;

  @MockBean
  MainLiquibaseFacade service;

  private ValidationResult result;
  private static final String EMPTY_FOLDER_PATH = "src/test/resources/data-model/test-main-liquibase.xml";
  private static final String CORRECT_PATH = "src/test/resources/test-main-liquibase.xml";
  private static final String INCORRECT_PATH = "src/test/resources/test-main-liquibase.json";

  @BeforeEach
  void init() {
    result = new ValidationResult();
    mainLiquibaseRuleBook.setDefaultResult(new ValidationResult());
  }

  @Nested
  class EmptyDataModelFolder {

    @Test
    void shouldWriteWarningWhenEmptyFolderOrContainsOnlyHiddenFiles() {
      ReflectionTestUtils.setField(validator, "mainLiquibasePath", EMPTY_FOLDER_PATH);

      assertErrorsWarnings(0, 1);
    }
  }

  @Nested
  class ChangeLogFileMissing {

    @Test
    void shouldWriteErrorWhenFileNotFound() {
      ReflectionTestUtils.setField(validator, "mainLiquibasePath", INCORRECT_PATH);

      assertErrorsWarnings(1, 0);
    }

    @Test
    void noErrorsWhenFileCanBeFound() {
      ReflectionTestUtils.setField(validator, "mainLiquibasePath", CORRECT_PATH);
      when(service.changeLogCanBeParsed()).thenReturn(true);

      assertErrorsWarnings(0, 0);
    }
  }

  @Nested
  class CannotParseChangeLog {

    @Test
    void noErrorsWhenChangeLogFileCanBeParsed() {
      ReflectionTestUtils.setField(validator, "mainLiquibasePath", CORRECT_PATH);
      when(service.changeLogCanBeParsed()).thenReturn(true);

      assertErrorsWarnings(0, 0);
    }

    @Test
    void shouldWriteErrorWhenCannotParseChangeLogFile() {
      ReflectionTestUtils.setField(validator, "mainLiquibasePath", CORRECT_PATH);
      when(service.changeLogCanBeParsed()).thenReturn(false);

      assertErrorsWarnings(1, 0);
    }
  }

  @Nested
  class IncorrectColumnName {

    @Test
    void noErrorsWhenCorrectColumnName() {
      List<ColumnConfig> columns = List.of(new ColumnConfig(new Column("_2bc")));

      assertErrors(columns, 0);
    }

    @Test
    void shouldWriteErrorWhenColumnNameStartsFromDigit() {
      List<ColumnConfig> columns = List.of(new ColumnConfig(new Column("2bc")));

      assertErrors(columns, 1);
    }

    @Test
    void shouldWriteErrorWhenColumnNameHasCyrillic() {
      List<ColumnConfig> columns = List.of(new ColumnConfig(new Column("Бbc")));

      assertErrors(columns, 1);
    }

    @Test
    void shouldWriteErrorWhenColumnNameIsTooLong() {
      List<ColumnConfig> columns = List.of(
          new ColumnConfig(
            new Column(
              "ten_chars1ten_chars2ten_chars3ten_chars4ten_chars5ten_chars6____")));

      assertErrors(columns, 1);
    }

    private void assertErrors(List<ColumnConfig> columns, int errors) {
      ReflectionTestUtils.setField(validator, "mainLiquibasePath", CORRECT_PATH);
      CreateTableChange change = mock(CreateTableChange.class);

      when(change.getColumns()).thenReturn(columns);
      when(service.changeLogCanBeParsed()).thenReturn(true);
      when(service.getChangesByType(CreateTableChange.class)).thenReturn(List.of(change));

      assertErrorsWarnings(errors, 0);
    }
  }

  private void assertErrorsWarnings(int errors, int warnings) {
    validator.validate(result);

    assertThat(result.getWarnings()).hasSize(warnings);
    assertThat(result.getErrors()).hasSize(errors);
  }
}