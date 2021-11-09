package com.epam.digital.data.platform.validator.rulebooks.config;

import com.deliveredtechnologies.rulebook.spring.SpringAwareRuleBookRunner;
import com.epam.digital.data.platform.validator.config.SettingsYamlConfig;
import com.epam.digital.data.platform.validator.model.SettingsYaml;
import com.epam.digital.data.platform.validator.rulebooks.settings.MockSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SettingsYamlTestConfig {

  private SettingsYamlConfig settingsYamlConfig = new SettingsYamlConfig();

  @Bean
  public SpringAwareRuleBookRunner settingsYamlRuleBook() {
    return settingsYamlConfig.settingsYamlRuleBook();
  }

  @Bean
  public SettingsYaml settingsYaml() {
    return new MockSettings().instance;
  }

  @Bean
  public ObjectMapper yamlMapper() {
    return settingsYamlConfig.yamlMapper();
  }
}
