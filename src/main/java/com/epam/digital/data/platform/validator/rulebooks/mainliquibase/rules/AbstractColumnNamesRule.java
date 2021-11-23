package com.epam.digital.data.platform.validator.rulebooks.mainliquibase.rules;

import com.deliveredtechnologies.rulebook.annotation.Given;
import com.deliveredtechnologies.rulebook.annotation.Result;
import com.epam.digital.data.platform.liquibase.extension.change.DdmColumnConfig;
import com.epam.digital.data.platform.liquibase.extension.change.DdmFunctionConfig;
import com.epam.digital.data.platform.liquibase.extension.change.core.DdmCreateSearchConditionChange;
import com.epam.digital.data.platform.validator.model.ValidationResult;
import com.epam.digital.data.platform.validator.rulebooks.FactNames;
import com.epam.digital.data.platform.validator.rulebooks.mainliquibase.MainLiquibaseFacade;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import liquibase.change.ColumnConfig;
import liquibase.change.core.CreateTableChange;

public abstract class AbstractColumnNamesRule {
  
  @Given(FactNames.MAIN_LIQUIBASE_FACADE)
  protected MainLiquibaseFacade facade;

  @Result
  protected ValidationResult result;

  protected Set<String> getColumnNamesFromTables(List<CreateTableChange> changes) {
    return changes.stream()
        .flatMap(x -> x.getColumns().stream())
        .map(ColumnConfig::getName)
        .collect(Collectors.toSet());
  }

  protected Set<String> getColumnNamesFromSearchConditions(
      List<DdmCreateSearchConditionChange> changes) {
    
    Set<String> names = changes.stream()
        .flatMap(x -> x.getTables().stream())
        .flatMap(x -> x.getColumns().stream())
        .map(DdmColumnConfig::getName)
        .collect(Collectors.toSet());

    names.addAll(changes.stream()
        .flatMap(x -> x.getCtes().stream())
        .flatMap(x -> x.getTables().stream())
        .flatMap(x -> x.getColumns().stream())
        .map(DdmColumnConfig::getName)
        .collect(Collectors.toSet()));

    names.addAll(changes.stream()
        .flatMap(x -> x.getCtes().stream())
        .flatMap(x -> x.getTables().stream())
        .flatMap(x -> x.getFunctions().stream())
        .map(DdmFunctionConfig::getColumnName)
        .collect(Collectors.toSet()));

    return names;
  }

  protected Set<String> getColumnAliasesFromSearchConditions(
      List<DdmCreateSearchConditionChange> changes) {
    
    Set<String> aliases = changes.stream()
        .flatMap(x -> x.getTables().stream())
        .flatMap(x -> x.getColumns().stream())
        .map(DdmColumnConfig::getAlias)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    aliases.addAll(changes.stream()
        .flatMap(x -> x.getCtes().stream())
        .flatMap(x -> x.getTables().stream())
        .flatMap(x -> x.getColumns().stream())
        .map(DdmColumnConfig::getAlias)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet()));

    aliases.addAll(changes.stream()
        .flatMap(x -> x.getCtes().stream())
        .flatMap(x -> x.getTables().stream())
        .flatMap(x -> x.getFunctions().stream())
        .map(DdmFunctionConfig::getAlias)
        .collect(Collectors.toSet()));

    return aliases;
  }
  
  protected Set<String> getCreatedColumnIdentifiers() {
    List<CreateTableChange> tables = facade.getChangesByType(CreateTableChange.class);
    List<DdmCreateSearchConditionChange> searchConditions = facade.getChangesByType(DdmCreateSearchConditionChange.class);

    Set<String> columnIdentifiers = new HashSet<>();
    columnIdentifiers.addAll(getColumnNamesFromTables(tables));
    columnIdentifiers.addAll(getColumnNamesFromSearchConditions(searchConditions));
    columnIdentifiers.addAll(getColumnAliasesFromSearchConditions(searchConditions));
    return columnIdentifiers;
  }
}
