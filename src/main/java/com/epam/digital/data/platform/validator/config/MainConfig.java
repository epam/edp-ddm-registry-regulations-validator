package com.epam.digital.data.platform.validator.config;

import com.epam.digital.data.platform.validator.model.SettingsYaml;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class MainConfig {

  @Bean
  public SettingsYaml settings(@Value("file:${app.settings-yaml-path}") Resource file,
      ObjectMapper mapper) throws IOException {
    return mapper.readValue(new FileInputStream(file.getFile()), SettingsYaml.class);
  }

  @Bean
  public ObjectMapper yamlMapper() {
    return new ObjectMapper(new YAMLFactory());
  }
}
