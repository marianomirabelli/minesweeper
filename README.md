# API-Minesweeper


## Architecture

This API is made up of two layers: api and service. The service is a RESTFul API that manages the game flows and
the user registry. On the other hand, api is a flat module that only contains DTO objects. Therefore, if Minesweeper-API
would be consumed by another JVM based application (Groovy, Scala, etc.) our consumer can import the dependency directly without
replicate our model through json or other types of objects.

In terms of persistence, this API saves the data in Mongo DB. The database is available through [Atlas] https://www.mongodb.com/cloud/atlas) 
and is deployed as a three-node cluster that is mounted on AWS in the US -East region.

Additionally, the API-Minesweeper is deployed in Heroku as a Docker container.

Finally, there are an http client called MinesweeperConnector.

## Technological Stack

* api-minesweeper
    * Java 14
    * Spring Boot 2
    * Docker
    
* minesweeper-client
    * Python 3.6
    
## Testing

The api-minesweeper has 45 automated test taking into account the unit and the integration test. 
These test were developed making use of:

* Junit 5
* Mockito
* Spring Boot test tooling

Additionally, an embedded Mongo is used for some test scenarios.

On the other hand, the main.py file in the client folder, simulates
a minesweeper game using the minesweeper client.

## API Accessing

The api is accessible in the following URL : 

* https://mmariano-minesweeper.herokuapp.com/api/minesweeper/swagger-ui/

