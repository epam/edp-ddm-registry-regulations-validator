package com.epam.digital.data.platform.validator.rulebooks.mainliquibase;

import com.deliveredtechnologies.rulebook.NameValueReferableTypeConvertibleMap;
import com.deliveredtechnologies.rulebook.lang.RuleBuilder;
import com.deliveredtechnologies.rulebook.model.rulechain.cor.CoRRuleBook;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import java.io.File;
import java.nio.file.Path;

public class MainLiquibaseRules extends CoRRuleBook<ValidationResult> {

  @Override
  public void defineRules() {
    addRule(RuleBuilder.create()
        .withFactType(String.class)
        .withResultType(ValidationResult.class)
        .when(this::isFolderEmpty)
        .then((fact, result) -> result.getValue()
            .addWarning("Папка '" + Path.of(fact.getOne()).getParent() + "' порожня"))
        .stop()
        .build());
    addRule(RuleBuilder.create()
        .withFactType(String.class)
        .withResultType(ValidationResult.class)
        .when(fact -> !new File(fact.getOne()).exists())
        .then((fact, result) -> result.getValue().addError("Файл '" + fact.getOne() + "' не існує"))
        .build());
  }

  private boolean isFolderEmpty(NameValueReferableTypeConvertibleMap<String> fact) {
    var files =
        Path.of(fact.getOne())
            .getParent()
            .toFile()
            .listFiles(file -> !file.getName().startsWith("."));

    return files == null || files.length == 0;
  }
}
