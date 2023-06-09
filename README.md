# ruisdael-elk-server

A scalable high-availability monitoring system that aims to provide real-time metrics.

#### Main Branch
Pipeline
![Main Pipeline](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/main/pipeline.svg?ignore_skipped=true)
JUnit Coverage
![JUnit Coverage](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/main/coverage.svg?job=junit-jacoco)
PITest Coverage
![JUnit Coverage](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/main/coverage.svg?job=pitest)


#### Development
Pipeline
![Dev Pipeline](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/dev/pipeline.svg?ignore_skipped=true)
JUnit Coverage
![JUnit Coverage](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/dev/coverage.svg?job=junit-jacoco)
PITest Coverage
![JUnit Coverage](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/dev/coverage.svg?job=pitest)


### About

This application shows a real time dashboard depicting the status of multiple monitored nodes over a network. It provides real time metrics such as CPU and storage usage.\
In addition to standard metrics, custom fields can also be displayed that are directly sent by monitored nodes.\
The system is equipped to show warnings when problems arise with certain metrics, such as storage nearing capacity.

## Authors

| Name               | Email                                |
|--------------------|--------------------------------------|
| Dean Polimac       | D.Polimac@student.tudelft.nl         |
| Cas Ebels          | C.N.Ebels@student.tudelft.nl         |
| Erfan Mozafari     | E.Mozafari@student.tudelft.nl        |
| Quinten Van Opstal | Q.M.F.VanOpstal-1@student.tudelft.nl |
| Žygis Liutkus      | Liutkus@student.tudelft.nl           |

## Compiling from source

To compile this project you need JDK17, gradle and an internet connection.

You may edit the included application.properties file and enter your elastic credentials here to hardcode these into the application.
Additional configuration options are available in this file.

If you choose to not hardcode the credentials, you will need to add the `application.properties` with elastic credentials to the same folder when running the generated jar. (Continue reading on how to generate this).

Run the `gradle bootJar` gradle command to compile the source code and build an executable jar file.\
The jar will be stored in the folder `/build/libs/`.\
*If you wish to only run the services from your IDE, run the `gradle bootRun` task or start them in the Services tab in IntelliJ.*


## Installation

Only an executable jar has to be copied to any folder within the operating system and executed using Java 17. There is no installation process. Please see [usage](#usage) on how to run the application.

## Usage

When using pre-built binaries there is no configuration required for the database, but an elasticsearch server must be provided. See the [configuration](#configuration) section below.\
A H2 file database is created upon run. However, custom configurations can be set here as well.

### Default Port Mapping

The application by default listens on port 8080, but this can be changed with a configuration file. (See below)

### Configuration

Copy over the [applications.properties](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/-/blob/main/src/main/resources/application.properties) file next to the .jar when running from a bootJar, or edit the file directly when running from source within an IDE.
Any settings in this file will overwrite the default ones provided by the bootJar.

The ElasticSearch configuration should be provided to you by either a server administrator, or have been documented during installation of the elastic server.\
Input those values without any brackets after the '=' sign.\
It is not required to change other values, including the password to the H2 database, as it does not store any data other than what is publicly accessible. Passwords are encrypted and unreadable.


## Contributing

In order to run this project on your local machine you will need a functioning Java 17 installation.
It is recommended to set on up locally. Your credentials should be entered in the `~/src/main/resources/application.properties` file.\
When making Merge Requests, please check that you do not commit the application.properties file, as this could possibly expose your H2 credentials and resets the file for other developers. Your MR might get closed if this file is overwritten.
To avoid commits that only fix checkstyle or PMD issues, it is recommended to run these locally before commiting. If running gradle, they are the following tasks: `gradle checkstyleMain checkstyleTest pmdMain pmdTest test pitest`
