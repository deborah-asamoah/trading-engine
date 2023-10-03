# trading-engine
This trading engine is a multi-module springboot project. The database used for this project is postgres. It has four(4) services which are:
  - Client service
  - Market data service
  - Order processing service
  - Reporting Service
    
## Setting up postgres on docker
Run this command in your terminal
<pre>docker pull postgres</pre>
Run this command to set your postgres password
<pre>docker run -d -i --name postgres3 -p 5432:5432 -e POSTGRES_PASSWORD="SET_YOUR_POSTGRES_PASSWORD" postgres:latest</pre>

## Running the various microservices
### Client Service
  - Create a database in postgres
    <pre>client_order_db</pre>
  - Run the main application
    <pre>ClientServiceApplication</pre>


### Market Data Service
  - Setup a redis container in docker
    <pre>https://hub.docker.com/_/redis</pre>
  - Run the main application
    <pre>MarketDataService</pre>


### Order Processing Service
  - Uncomment the first line of code in the data.sql file
    <pre>--drop table if exists order_view;</pre>
  - Run the main application
    <pre>OrderProcessingServiceApplication</pre>

### Reporting Service
  - Setup a kafka and zookeeper container in docker
    <pre>
      version: '2'
      services:
        zookeeper:
          image: confluentinc/cp-zookeeper:latest
          environment:
            ZOOKEEPER_CLIENT_PORT: 2181
            ZOOKEEPER_TICK_TIME: 2000
          ports:
            - 22181:2181
  
        kafka:
          image: confluentinc/cp-kafka:latest
          depends_on:
            - zookeeper
          ports:
            - 29092:29092
          environment:
            KAFKA_BROKER_ID: 1
            KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
            KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
            KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
            KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
            KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    </pre>
    - Create a database in postgres
    <pre>reporting_service</pre>
  - Run the main application
    <pre>ReportingServiceApplication</pre>






