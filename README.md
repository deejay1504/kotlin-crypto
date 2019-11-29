# Display a list of crypto currencies by their total volume across all
# markets in the last 24 hours for a given currency eg USD, EUR, GBP


## Technologies used

* Language: Kotlin 1.3.31
* Core framework: Spring Boot 2.1 with Spring Framework 5 Kotlin support
* Web framework: Spring MVC
* JQuery 2.2.4
* Web Ticker JQuery Plugin (https://maze.digital/webticker/)
* Bootstrap-Select JQuery Plugin for customizable drop down lists (https://developer.snapappointments.com/bootstrap-select/)
* Crypto Compare Public API (https://min-api.cryptocompare.com/documentation)
* Google Cloud Jib Tool 1.3.0 (for building Docker image)
* Templates: Thymeleaf and Bootstrap
* Build: Gradle Script with the Kotlin DSL
* Testing: Junit 5, Mockito
* json2kotlin for generating Kotlin data model classes from a JSON object

### To build the project on gradle command line

```
cd kotlin-crypto
./gradlew clean build
```

### To run on gradle command line

```
./gradlew bootRun
(you can then access the app on http//localhost:8080/)
```

### To create and push Docker image

```
./gradlew jibDockerBuild
```




