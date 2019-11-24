# Display a list of crypto currencies for a given currency eg USD, EUR, GBP


## Technologies used

* Language: Kotlin 1.3.31
* Core framework: Spring Boot 2.1 with Spring Framework 5 Kotlin support
* Web framework: Spring MVC
* JQuery 2.2.4
* Google Cloud Jib Tool 1.3.0 (for building Docker image)
* Templates: Thymeleaf and Bootstrap
* Build: Gradle Script with the Kotlin DSL
* Testing: Junit 5, Mockito
* json2kotlin for generating Kotlin data model classes from a JSON object

### To build the project on gradle command line

```
cd kotlin-gig-finder
./gradlew clean build
```

### To run on gradle command line

```
./gradlew bootRun
```

### To create and push Docker image

```
./gradlew jibDockerBuild
```




