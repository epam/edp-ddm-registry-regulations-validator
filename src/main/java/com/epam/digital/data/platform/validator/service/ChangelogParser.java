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
