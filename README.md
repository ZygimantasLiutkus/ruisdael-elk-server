# ruisdael-elk-server

A scalable high-availability monitoring system that aims to provide real-time metrics.

#### Main Branch
![Main Pipeline](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/main/pipeline.svg?ignore_skipped=true)
JUnit
![JUnit Coverage](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/main/coverage.svg?job=junit-jacoco)
PITest
![JUnit Coverage](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/main/coverage.svg?job=pitest)


#### Development
![Dev Pipeline](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/dev/pipeline.svg?ignore_skipped=true)
JUnit
![JUnit Coverage](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/dev/coverage.svg?job=junit-jacoco)
PITest
![JUnit Coverage](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2022-2023-q4/cluster-12/ruisdael-automatic-network-monitoring-system/ruisdael-elk-server/badges/dev/coverage.svg?job=pitest)


### About

This application shows a real time dashboard depicting the status of multiple monitored nodes over a network. It provides real time metrics such as CPU and storage usage.\
In addition to standard metrics, custom fields can also be displayed that are directly sent by monitored nodes.\
The system is equipped to show warnings when problems arise with certain metrics, such as storage nearing capacity.

#### System Overview

The ruisdael-elk-server repository only contains code for the Monitoring WebUI shown in the diagram below.  
The WebUI is connected with both the Kibana server for graphing functionality and the ElasticSearch server for node data.  
The below diagram shows connections between different parts of the stack with arrows, dictating dataflows.

![Ruisdael overview.png](docs/readme/Ruisdael%20overview.png)

## Authors

| Name               | Email                                |
|--------------------|--------------------------------------|
| Dean Polimac       | D.Polimac@student.tudelft.nl         |
| Cas Ebels          | C.N.Ebels@student.tudelft.nl         |
| Erfan Mozafari     | E.Mozafari@student.tudelft.nl        |
| Quinten Van Opstal | Q.M.F.VanOpstal-1@student.tudelft.nl |
| Žygis Liutkus      | Liutkus@student.tudelft.nl           |

## Installation

This guide assumes you have already cloned the git repository to your local machine and that you are using a **linux** server.
Support for windows is not guaranteed.
Any path listed with a `~` is the project root folder, not your home directory.

It is recommended to install the software using the provided installation scripts from Ansible if you are familiar with this process.
If you are unfamiliar with Ansible, do not have Ansible set up on your environment or application configuration options must be changed it is strongly recommended to manually install the software.  

Before you proceed with the installation, an ElasticSearch server must be set up.
See [Elastic Installation](docs/readme/elastic_install.md) for installation instructions.

### Configuring the application

The main configuration file is found under `~/src/main/resources/application.properties`.  
This file contains options for the following, and should be set to the correct values:
- Webserver Port
- ElasticSearch Configuration
- Database Configuration

It should not be necessary to change the database configuration.

**Warning: These credentials will be hardcoded into the application. Do not upload any resulting build files to outside locations.**  
If you do not wish to hardcode credentials into the resulting build, do not change this file. Instead, copy it to another location and change it there. This file will be used later in the install process.

A secondary configuration file is available at `~/src/main/java/tudelft/ewi/cse2000/ruisdael/monitoring/configurations/ApplicationConfig`.
This file contains configuration options for the application itself. The default values should work for most installations, and shouldn't need to be changed.

### Ansible Installation

TODO

### Manual Installation

**Warning: Do not continue with this section if you followed the Ansible Installation**

#### Compiling from source & Installing

To compile this project you need JDK17 and an internet connection.

The installed Java version can be checked with the following command.

```shell
java -version
```

If any version `17.x.x` is reported, the installation is good. In case of an error or another version reported, install JDK17 with the following command.

```shell
apt update && apt install openjdk-17-jdk-headless -y
```

With Java installed, the gradle file must be made executable and ran to build the application. Run each line separately.

```shell
chmod +x gradlew
./gradlew :bootJar
```

The resulting jar will be stored in the folder `~/build/libs/`.\
*If you wish to only run the services from your IDE, run the `gradle bootRun` task or start them in the Services tab in IntelliJ.*

The jar file should be moved from the `~/build/libs` folder to any destination folder. A recommended example is by creating a folder under `/bin/` and copying it there.  
If you did not hardcode credentials during configuration, the `application.properties` file should be moved to the same folder containing the jar.

## Usage

The application can be started manually with the following command:

```shell
java -jar ruisdael-monitoring-webui.jar
```

### Default Port Mapping

The application by default listens on port 8080, but this can be changed with a configuration file. (See below)

#### Note for the ruisdael network

The WebUI is running on port 8081 (Configuration file) and behind the provided NGINX reverse proxy to serve `https://ruisdael-kibana.citg.tudelft.nl/`.
The Kibana service can be accessed from `https://ruisdael-kibana.citg.tudelft.nl/kibana`.

## Contributing

In order to run this project on your local machine you will need a functioning Java 17 installation.
It is recommended to set on up locally. Your credentials should be entered in the `~/src/main/resources/application.properties` file.\
When making Merge Requests, please check that you do not commit the application.properties file, as this could possibly expose your H2 credentials and resets the file for other developers. Your MR might get closed if this file is overwritten.
To avoid commits that only fix checkstyle or PMD issues, it is recommended to run these locally before commiting. If running gradle, they are the following tasks: `gradle checkstyleMain checkstyleTest pmdMain pmdTest test pitest`

## License

This project is licensed under the Apache 2.0 license.  
See [License](LICENSE.txt) for more information.