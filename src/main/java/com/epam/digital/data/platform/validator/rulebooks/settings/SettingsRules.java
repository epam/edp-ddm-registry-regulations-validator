package com.epam.digital.data.platform.validator.rulebooks.settings;

import com.deliveredtechnologies.rulebook.NameValueReferableTypeConvertibleMap;
import com.deliveredtechnologies.rulebook.lang.RuleBuilder;
import com.deliveredtechnologies.rulebook.model.rulechain.cor.CoRRuleBook;
import com.epam.digital.data.platform.validator.model.SettingsYaml;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import java.util.Set;

public class SettingsRules extends CoRRuleBook<ValidationResult> {

  private static final String VERSION_PATTERN = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
  private static final String PACKAGE_PATTERN = "^[a-z_][a-z0-9_]*(\\.[a-z_][a-z0-9_]*){0,100}$";
  private static final String DATABASE_PATTERN = "^[a-zA-Z_]\\w{0,30}$";

  private static final Set<String> JAVA_RESERVED_WORDS = Set
      .of("byte", "short", "int", "long", "char", "float", "double", "boolean", "if", "else",
          "switch", "case", "default", "while", "do", "break", "continue", "for", "try", "catch",
          "finally", "throw", "throws", "private", "protected", "public", "import", "package",
          "class", "interface", "extends", "implements", "static", "final", "void", "abstract",
          "native", "new", "return", "this", "super", "synchronized", "volatile", "const", "goto",
          "instanceof", "enum", "assert", "transient", "strictfp");

  @Override
  public void defineRules() {

    // version: 4.5.445
    addRule(RuleBuilder.create()
        .withFactType(SettingsYaml.class)
        .withResultType(ValidationResult.class)
        .when(f -> !f.getOne().getSettings().getGeneral().getVersion().matches(VERSION_PATTERN))
        .then((fact, result) -> result.getValue().addError("Версія має бути у вигляді x.х.х"))
        .build());

    // package: abc.de_f.ghi._123jkl
    addRule(RuleBuilder.create()
        .withFactType(SettingsYaml.class)
        .withResultType(ValidationResult.class)
        .when(f -> !f.getOne().getSettings().getGeneral().getBasePackageName()
            .matches(PACKAGE_PATTERN))
        .then((fact, result) -> result.getValue().addError("Назва пакету повинна мати вигляд:"
            + " abc.de_1.ghi._123jkl. Може містити маленькі латинські літери, цифри, та знаки"
            + " підкреслення. Назва підпакету не повинна починатися з цифри"))
        .build());

    // subpackage must not be equivalent to the Java reserved word
    addRule(RuleBuilder.create()
        .withFactType(SettingsYaml.class)
        .withResultType(ValidationResult.class)
        .when(this::doesPackageContainReservedWord)
        .then((fact, result) -> result.getValue().addError(
            "Назва підпакету не повинна відповідати зарезервованому у Java слову"))
        .build());

    // database valid characters: [0-9a-zA-Z_], length <= 31, must not start with a digit
    addRule(RuleBuilder.create()
        .withFactType(SettingsYaml.class)
        .withResultType(ValidationResult.class)
        .when(fact ->
            !fact.getOne().getSettings().getGeneral().getRegister().matches(DATABASE_PATTERN))
        .then((fact, result) -> result.getValue().addError(
            "Назва бази даних повинна містити не більше ніж 31 символ, не повинна починатися "
                + "з цифри та може містити літери, цифри та знак підкреслення"))
        .build());

    // kafka.retention-policy-in-days.read < 100 - warning
    addRule(RuleBuilder.create()
        .withFactType(SettingsYaml.class)
        .withResultType(ValidationResult.class)
        .when(f -> f.getOne().getSettings().getKafka().getRetentionPolicyInDays().getRead() < 100)
        .then((fact, result) -> result.getValue().addWarning(
            "Ви впевнені, що хочете зберігати дані у топіку READ меньше 100 днів?"))
        .build());

    // kafka.retention-policy-in-days.write < 100 - warning
    addRule(RuleBuilder.create()
        .withFactType(SettingsYaml.class)
        .withResultType(ValidationResult.class)
        .when(f -> f.getOne().getSettings().getKafka().getRetentionPolicyInDays().getWrite() < 100)
        .then((fact, result) -> result.getValue().addWarning(
            "Ви впевнені, що хочете зберігати дані у топіку WRITE меньше 100 днів?"))
        .build());
  }

  private boolean doesPackageContainReservedWord(
      NameValueReferableTypeConvertibleMap<SettingsYaml> fact) {
    String pkg = fact.getOne().getSettings().getGeneral().getBasePackageName();
    for (String subPackage : pkg.split("\\.")) {
      if (JAVA_RESERVED_WORDS.contains(subPackage)) {
        return true;
      }
    }
    return false;
  }
}
