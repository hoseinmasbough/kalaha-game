# Seyyed Hossein Masbough - KALAHA GAME


## Running game 
Use the following instructions for building and running game:

```
cd project_dir
mvn clean install -Pprod
cd target
java -jar kalaha-game-0.1.jar

```

***
## Improvable Notes


1- Security
- Adding JWT security utils file
- Changing controller for handling jwt requests

Estimation: 2d

2- Docker
- creating 2 seperated dockers for front-app and back-app
- managing apps in a docker-compose

Estimation: 1d

3- Persistent DB
- Make a decision about sql/nosql
- Adding required libraries
- Changing current entities if it needed

Estimation: 3d

4- Microservice
- Spring Cloud
- Zipkin
- Spring Sleuth
- Ribbon
- Eureka
- Hystrix (Circuit Breakers)
- Zuul

Estimation: 1w

5- Centralized configuration
- Apache Zookeeper
***

## Rest API Doc

- [ ] [Swagger URL](http://localhost:8080/api/swagger-ui/index.html)
