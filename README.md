# Room Occupancy Manager

Tool for hotels to optimize room occupancy and calculate profit.

### Build status
[![CircleCI](https://circleci.com/gh/zonkil/RoomOccupancyManager.svg?style=svg)](https://circleci.com/gh/zonkil/RoomOccupancyManager)

### Minimal requirements

To run project you need:

* Java 11

### Used technologies and libraries

* Spring boot
* Gradle 
* H2 Database
* Spock 
* Swagger

## Run project
To run project execute
```shell
./gradlew bootRun
```

Once application is working you can access swagger ui
> http://localhost:8080/swagger-ui.html
 
### Run test
Tu run test execute
```shell
./gradlew test
```

## Description

Service expose one endpoint to calculate room occupancy. There are tree parameters:
* numberOfPremiumRooms (required)
* numberOfEconomyRooms (required)
* guests (optional)

If parameter guests is present then calculation is done based on provided list of guests
If parameter guests is not provided then calculation is done based on data in database (there is data from assignment)
