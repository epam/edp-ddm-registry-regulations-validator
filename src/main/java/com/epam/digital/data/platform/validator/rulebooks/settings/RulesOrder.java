package com.epam.digital.data.platform.validator.rulebooks.settings;

public class RulesOrder {

  public static final int versionDoesNotMatchPatternRule = 1;
  public static final int packageDoesNotMatchPatternRule = 2;
  public static final int databaseNameDoesNotMatchPatternRule = 3;
  public static final int subpackageIsJavaReservedWordRule = 4;
  public static final int retentionPolicyReadRule = 5;
  public static final int retentionPolicyWriteRule = 6;

  private RulesOrder() {
  }
}
