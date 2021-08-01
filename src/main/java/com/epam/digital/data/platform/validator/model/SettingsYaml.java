package com.epam.digital.data.platform.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private Kafka kafka;

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

    @JsonProperty("retention-policy-in-days")
    private RetentionPolicyInDays retentionPolicyInDays;

    public RetentionPolicyInDays getRetentionPolicyInDays() {
      return retentionPolicyInDays;
    }

    public void setRetentionPolicyInDays(RetentionPolicyInDays retentionPolicyInDays) {
      this.retentionPolicyInDays = retentionPolicyInDays;
    }
  }

  public static class RetentionPolicyInDays {

    private Integer read;
    private Integer write;

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
