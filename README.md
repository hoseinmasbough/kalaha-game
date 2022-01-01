# Seyyed Hossein Masbough - KALAHA GAME


## Building Game 
Use the following instructions for building and running the game:

```
cd project_dir
mvn clean install -Pprod
cd target
java -jar kalaha-game-0.1.jar

```

***
## Game Page Address
- [ ] [Game URL](http://localhost:8080/)

***
## Rest API Doc

- [ ] [Swagger URL](http://localhost:8080/swagger-ui/index.html)

***
## Improvable Notes
Some features that could be added

1- Security
- Adding JWT security util files
- Changing controller for handling jwt requests

Estimation: 2d

2- Docker
- creating 2 seperated dockers for front-end and back-end apps
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

Estimation: 2d
