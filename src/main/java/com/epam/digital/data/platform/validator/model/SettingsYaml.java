package com.epam.digital.data.platform.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

public class SettingsYaml {

  private Settings settings;

  public Settings getSettings() {
    return settings;
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
  }

  public static class Settings {

    private General general;

    @JsonSetter(nulls = Nulls.SKIP)
    private Kafka kafka = new Kafka();

    public General getGeneral() {
      return general;
    }

    public void setGeneral(General general) {
      this.general = general;
    }

    public Kafka getKafka() {
      return kafka;
    }

    public void setKafka(Kafka kafka) {
      this.kafka = kafka;
    }
  }

  public static class General {

    @JsonProperty("package")
    private String basePackageName;
    private String register;
    private String version;

    public String getBasePackageName() {
      return basePackageName;
    }

    public void setBasePackageName(String basePackageName) {
      this.basePackageName = basePackageName;
    }

    public String getRegister() {
      return register;
    }

    public void setRegister(String register) {
      this.register = register;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }
  }

  public static class Kafka {

    @JsonSetter(nulls = Nulls.SKIP)
    @JsonProperty("retention-policy-in-days")
    private RetentionPolicyInDays retentionPolicyInDays = new RetentionPolicyInDays();

    public RetentionPolicyInDays getRetentionPolicyInDays() {
      return retentionPolicyInDays;
    }

    public void setRetentionPolicyInDays(RetentionPolicyInDays retentionPolicyInDays) {
      this.retentionPolicyInDays = retentionPolicyInDays;
    }
  }

  public static class RetentionPolicyInDays {

    @JsonSetter(nulls = Nulls.SKIP)
    private Integer read = 3 * 365;

    @JsonSetter(nulls = Nulls.SKIP)
    private Integer write = 3 * 365;

    public Integer getRead() {
      return read;
    }

    public void setRead(Integer read) {
      this.read = read;
    }

    public Integer getWrite() {
      return write;
    }

    public void setWrite(Integer write) {
      this.write = write;
    }
  }
}
