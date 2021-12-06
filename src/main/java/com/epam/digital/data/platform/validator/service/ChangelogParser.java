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

package com.epam.digital.data.platform.validator.service;

import java.nio.file.Paths;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class ChangelogParser {

  public DatabaseChangeLog parseChangeLog(String changeLogFile) throws LiquibaseException {

    CompositeResourceAccessor fileOpener = new CompositeResourceAccessor(
        new FileSystemResourceAccessor(Paths.get(".").toAbsolutePath().toFile()),
        new CommandLineResourceAccessor(this.getClass().getClassLoader()));

    ChangeLogParser parser = ChangeLogParserFactory.getInstance()
        .getParser(changeLogFile, fileOpener);

    return parser.parse(changeLogFile, new ChangeLogParameters(), fileOpener);
  }
}
