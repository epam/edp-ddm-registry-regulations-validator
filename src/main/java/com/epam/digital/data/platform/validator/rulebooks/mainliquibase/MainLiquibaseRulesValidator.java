package com.epam.digital.data.platform.validator.rulebooks.mainliquibase;

import com.deliveredtechnologies.rulebook.Fact;
import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.lang.RuleBookBuilder;
import com.deliveredtechnologies.rulebook.model.RuleBook;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.RulesValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MainLiquibaseRulesValidator implements RulesValidator {

  private final NameValueReferableMap<String> mainLiquibaseFacts = new FactMap<>();

  public MainLiquibaseRulesValidator(
      @Value("${app.main-liquibase-path}") String mainLiquibasePath) {
    mainLiquibaseFacts.put(new Fact<>(mainLiquibasePath));
  }

  @Override
  public ValidationResult validate(ValidationResult result) {
    RuleBook<ValidationResult> ruleBook =
        RuleBookBuilder.create(MainLiquibaseRules.class)
            .withResultType(ValidationResult.class)
            .withDefaultResult(result)
            .build();

    ruleBook.run(mainLiquibaseFacts);
    return result;
  }
}
