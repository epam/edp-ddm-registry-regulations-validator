package com.epam.digital.data.platform.validator.rulebooks.settings;

import com.epam.digital.data.platform.validator.model.SettingsYaml;
import com.epam.digital.data.platform.validator.model.SettingsYaml.General;
import com.epam.digital.data.platform.validator.model.SettingsYaml.Kafka;
import com.epam.digital.data.platform.validator.model.SettingsYaml.RetentionPolicyInDays;
import com.epam.digital.data.platform.validator.model.SettingsYaml.Settings;

public class MockSettings {

  public SettingsYaml instance;

  public MockSettings() {
    instance = baseMock();
  }

  public MockSettings set(Field field, Object value) {
    switch (field) {
      case PACKAGE:
        instance.getSettings().getGeneral().setBasePackageName((String) value);
        break;
      case REGISTER:
        instance.getSettings().getGeneral().setRegister((String) value);
        break;
      case VERSION:
        instance.getSettings().getGeneral().setVersion((String) value);
        break;
      case RETENTION_READ:
        instance.getSettings().getKafka().getRetentionPolicyInDays().setRead((int) value);
        break;
      case RETENTION_WRITE:
        instance.getSettings().getKafka().getRetentionPolicyInDays().setWrite((int) value);
        break;
    }
    return this;
  }

  private SettingsYaml baseMock() {
    var settingsYaml = new SettingsYaml();
    var settings = new Settings();
    var general = new General();
    var kafka = new Kafka();
    var retentionPolicy = new RetentionPolicyInDays();

    settingsYaml.setSettings(settings);
    settings.setGeneral(general);
    settings.setKafka(kafka);
    kafka.setRetentionPolicyInDays(retentionPolicy);

    general.setRegister("registry");
    general.setBasePackageName("base.pkg");
    general.setVersion("1.2.3");

    retentionPolicy.setRead(5000);
    retentionPolicy.setWrite(5000);
    return settingsYaml;
  }

  public enum Field {
    PACKAGE, REGISTER, VERSION, RETENTION_READ, RETENTION_WRITE
  }
}
