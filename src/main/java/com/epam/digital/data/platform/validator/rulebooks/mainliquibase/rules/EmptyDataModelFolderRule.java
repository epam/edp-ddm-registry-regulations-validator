package com.epam.digital.data.platform.validator.rulebooks.mainliquibase.rules;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.Given;
import com.deliveredtechnologies.rulebook.annotation.Result;
import com.deliveredtechnologies.rulebook.annotation.Rule;
import com.deliveredtechnologies.rulebook.annotation.Then;
import com.deliveredtechnologies.rulebook.annotation.When;
import com.deliveredtechnologies.rulebook.spring.RuleBean;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.FactNames;
import com.epam.digital.data.platform.validator.rulebooks.mainliquibase.RulesOrder;
import java.nio.file.Path;

@RuleBean
@Rule(order = RulesOrder.emptyDataModelFolderRule)
public class EmptyDataModelFolderRule {

  @Given(FactNames.MAIN_LIQUIBASE_PATH)
  private String path;

  @When
  public boolean isFolderEmpty() {
    var files = Path.of(path)
        .getParent()
        .toFile()
        .listFiles(file -> !file.getName().startsWith("."));

    return files == null || files.length == 0;
  }

  @Result
  private ValidationResult result;

  @Then
  public RuleState then() {
    result.addWarning("Папка '" + Path.of(path).getParent() + "' порожня");
    return RuleState.BREAK;
  }
}
