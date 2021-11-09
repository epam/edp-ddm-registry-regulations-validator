package com.epam.digital.data.platform.validator.config;

import com.deliveredtechnologies.rulebook.spring.SpringAwareRuleBookRunner;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainLiquibaseConfig {

  @Bean
  public SpringAwareRuleBookRunner mainLiquibaseRuleBook() {
    var springAwareRuleBookRunner = new SpringAwareRuleBookRunner(
        "com.epam.digital.data.platform.validator.rulebooks.mainliquibase.rules");
    springAwareRuleBookRunner.setDefaultResult(new ValidationResult());
    return springAwareRuleBookRunner;
  }
}
