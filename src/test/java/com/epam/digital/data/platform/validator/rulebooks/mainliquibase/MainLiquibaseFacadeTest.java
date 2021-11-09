package com.epam.digital.data.platform.validator.rulebooks.mainliquibase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.epam.digital.data.platform.validator.service.ChangelogParser;
import java.util.List;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.DropColumnChange;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MainLiquibaseFacadeTest {

  @Mock
  ChangelogParser parser;
  @Mock
  DatabaseChangeLog databaseChangeLog;

  private MainLiquibaseFacade facade;

  @BeforeEach
  void init() {
    facade = new MainLiquibaseFacade(parser, "abc");
  }

  @Test
  void shouldSetNullToDatabaseChangeLogWhenParseException() throws LiquibaseException {
    when(parser.parseChangeLog(any())).thenThrow(LiquibaseException.class);

    ReflectionTestUtils.invokeMethod(facade, "postConstruct");

    assertThat(facade.changeLogCanBeParsed()).isFalse();
  }

  @Test
  void shouldReturnMainLiquibasePath() {
    assertThat(facade.getChangeLogPath()).isEqualTo("abc");
  }

  @Test
  void shouldReturnAllChanges() throws LiquibaseException {
    when(parser.parseChangeLog(any())).thenReturn(databaseChangeLog);
    ReflectionTestUtils.invokeMethod(facade, "postConstruct");

    var change1 = new CreateTableChange();
    var change2 = new DropColumnChange();
    var change3 = new UpdateDataChange();

    ChangeSet changeSet = new ChangeSet(databaseChangeLog);
    changeSet.addChange(change1);
    changeSet.addChange(change2);

    ChangeSet changeSet2 = new ChangeSet(databaseChangeLog);
    changeSet2.addChange(change3);

    when(databaseChangeLog.getChangeSets()).thenReturn(List.of(changeSet, changeSet2));

    var result = facade.getAllChanges();

    assertThat(result).containsExactly(change1, change2, change3);
  }

  @Test
  void shouldGetAllChanges() throws LiquibaseException {
    when(parser.parseChangeLog(any())).thenReturn(databaseChangeLog);
    ReflectionTestUtils.invokeMethod(facade, "postConstruct");

    var change1 = new CreateTableChange();
    var change2 = new CreateTableChange();
    var change3 = new UpdateDataChange();

    ChangeSet changeSet = new ChangeSet(databaseChangeLog);
    changeSet.addChange(change1);

    ChangeSet changeSet2 = new ChangeSet(databaseChangeLog);
    changeSet.addChange(change2);
    changeSet2.addChange(change3);

    when(databaseChangeLog.getChangeSets()).thenReturn(List.of(changeSet, changeSet2));

    var result = facade.getChangesByType(CreateTableChange.class);

    assertThat(result).containsExactly(change1, change2);
  }
}