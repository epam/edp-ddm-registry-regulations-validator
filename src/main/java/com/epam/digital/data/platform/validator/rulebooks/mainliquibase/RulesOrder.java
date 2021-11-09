package com.epam.digital.data.platform.validator.rulebooks.mainliquibase;

public class RulesOrder {

  public static final int emptyDataModelFolderRule = 1;

  public static final int changeLogFileMissingRule = 2;
  public static final int changeLogFileCannotBeParsedRule = 3;

  public static final int columnNameStartsFromDigitRule = 4;
  public static final int columnNameHasCyrillicSymbolRule = 5;
  public static final int columnNameIsTooLongRule = 6;

  private RulesOrder() {
  }
}
