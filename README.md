# Backfill Task

## Description

This project is a Spring Boot application that runs as a console API and process xlsx file.

## Running Locally

This application only run locally pointing to a specific environment.

### Environmental Variables & VM Options

You will need to define the following process variables:

|Variable| Description                                                                                                                     |
|--------|---------------------------------------------------------------------------------------------------------------------------------|
|LOCAL_TEST| Used by vidispine sdk.  Should be TRUE so that it uses the extneral URL                                                         |

The following file is mandatory to set your parameters based on the env and file used:
### config.properties

|Variable| Example value            | Description                                                                |
|--------|--------------------------|----------------------------------------------------------------------------|
|location| D:\\Documents\\test.xlsx | The path and the file name location                                        |
|sheetNumber| 0                        | Choose sheet number in excel file                                          |
|columnNumber| 1                        | Column number to take the values to iterate                                |
|startInRowNumber| 1                        | Start in row number                                                        |
|shapeListGenerate| lowres,news-wtrmrk       | List of shapes to generate, each shape or tag should be separated by comma |
|adminPassword| password                 | Admin password                                                             |

### Executing

After set up all previous variables you can run the full process, and as a unique step, it will ask which environment you are pointing.
(for example if you want to search the items and create the shapes in qa, just enter -> qa)

```bash
$user-> Enter run env supported: {env}
```

NOTE: See application-debug.log when finish the process.