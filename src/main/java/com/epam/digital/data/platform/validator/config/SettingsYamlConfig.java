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

package com.epam.digital.data.platform.validator.config;

import com.deliveredtechnologies.rulebook.spring.SpringAwareRuleBookRunner;
import com.epam.digital.data.platform.validator.model.SettingsYaml;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class SettingsYamlConfig {

  @Bean
  public SettingsYaml settingsYaml(@Value("file:${app.settings-yaml-path}") Resource file,
      ObjectMapper mapper) throws IOException {
    return mapper.readValue(new FileInputStream(file.getFile()), SettingsYaml.class);
  }

  @Bean
  public ObjectMapper yamlMapper() {
    return new ObjectMapper(new YAMLFactory());
  }

  @Bean
  public SpringAwareRuleBookRunner settingsYamlRuleBook() {
    var springAwareRuleBookRunner = new SpringAwareRuleBookRunner(
        "com.epam.digital.data.platform.validator.rulebooks.settings.rules");
    springAwareRuleBookRunner.setDefaultResult(new ValidationResult());
    return springAwareRuleBookRunner;
  }
}
