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

import com.epam.digital.data.platform.validator.service.ChangelogParser;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MainLiquibaseFacade {

  private final Logger log = LoggerFactory.getLogger(MainLiquibaseFacade.class);

  private final ChangelogParser changelogParser;
  private final String changeLogPath;

  private DatabaseChangeLog databaseChangeLog;

  public MainLiquibaseFacade(ChangelogParser changelogParser,
      @Value("${app.main-liquibase-path}") String changeLogPath) {
    this.changelogParser = changelogParser;
    this.changeLogPath = changeLogPath;
  }

  @PostConstruct
  private void postConstruct() {
    try {
      databaseChangeLog = changelogParser.parseChangeLog(changeLogPath);
    } catch (LiquibaseException e) {
      log.info("It is not possible to parse the file {}", changeLogPath);
      databaseChangeLog = null;
    }
  }

  public List<ChangeSet> getChangeSets() {
    return databaseChangeLog.getChangeSets();
  }

  public List<Change> getAllChanges() {
    return databaseChangeLog.getChangeSets()
        .stream()
        .flatMap(x -> x.getChanges().stream())
        .collect(Collectors.toList());
  }

  public <T> List<T> getChangesByType(Class<T> type) {
    return getAllChanges().stream()
        .filter(c -> type.isAssignableFrom(c.getClass()))
        .map(x -> (T)x)
        .collect(Collectors.toList());
  }

  public String getChangeLogPath() {
    return changeLogPath;
  }

  public boolean changeLogCanBeParsed() {
    return databaseChangeLog != null;
  }
}
