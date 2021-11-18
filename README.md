# registry-regulations-validator

This console application is used for validation of the registry regulation.

### Usage
The application is supposed to be run from the root of the registry regulation folder, otherwise configuration changes are needed.

### Local development:
###### Prerequisites:
* `setting.yaml` - registry settings file
* `data-model/main-liquibase.xml` - registry regulation file

###### Configuration:
1. Check `src/main/resources/application.yaml` and customize the relative paths if running not from the root of the registry regulation folder

###### Steps:
1. (Optional) Package application into jar file with `mvn clean package`
2. Run application with your favourite IDE or via `java -jar ...` with jar file, created above
