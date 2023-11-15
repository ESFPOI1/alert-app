# Implementing a Spring Cloud Producer and Consumer that use Avro and Schema Registry
we’ll demonstrate the creation of two Spring Cloud Stream applications: Alert Producer and Alert Consumer. The Alert Producer will emit alerts through an Apache Kafka broker, while the Alert Consumer will be responsible for receiving and processing these alerts. We’ll use Apache Avro as the serialization format for the alert, and the alert Schema will be managed by a Schema Registry.

A brief explanation of Spring Cloud Stream, Apache Kafka, Apache Avro and Schema Registry:

- Spring Cloud Stream: It is a framework for building event-driven microservices applications. It simplifies the development of messaging-based applications by abstracting away the complexities of the messaging middleware, such as Apache Kafka, RabbitMQ, or others. Spring Cloud Stream introduces concepts like “binders” to connect to messaging systems and provides a programming model based on Spring Boot, making it easier to develop message-driven microservices;
- Apache Kafka: It is an open-source distributed streaming platform used for building real-time data pipelines and streaming applications. It is designed to handle high volumes of data and provides a durable, fault-tolerant, and scalable messaging system. Kafka allows producers to publish data to topics, and consumers can subscribe to these topics to process the data;
- Apache Avro: It is a data serialization framework used for efficient data exchange between different systems. It defines a compact and efficient data format and provides tools for data serialization and deserialization.
- Schema Registry: It is a component used to manage and store the schemas of data in a distributed system. It helps ensure that different services can communicate with one another using compatible data schemas, promoting consistency and interoperability.

## Start Docker Compose services
In a terminal and inside alert-app folder, run the following command to start the Docker Compose services:

```docker
docker compose up -d
```

## Start Alert Producer and Consumer apps
In a terminal and inside the alert-producer root folder, run the command below:

```cmd
mvn clean spring-boot:run
```

In another terminal and inside the alert-consumer root folder, run the following command:

```cmd
mvn clean spring-boot:run
```

## Emitting an Alert
In a terminal, run the following cURL command to send an alert:

```curl
curl -i -X POST localhost:8080/api/alerts \
  -H 'Content-Type: application/json' \
  -d '{"description": "Hurricane Norma makes landfall in Mexico as Category 1 storm", "color": "ORANGE"}'
```

In Alert Producer application logs, we should see:

```log
INFO 79147 --- [nio-8080-exec-1] c.e.a.emitter.AlertEmitter           : Alert emitted! {"description": "Hurricane Norma makes landfall in Mexico as Category 1 storm", "AlertColor": "ORANGE", "createdOn": "2023-11-06T13:04:48.354Z"}
```

The Alert Consumer should listen to the alert and log the following:

```log
INFO 79148 --- [container-0-C-1] c.e.a.listener.AlertListener             : Received Alert! "Hurricane Norma makes landfall in Mexico as Category 1 storm" with color 'ORANGE' created on '2023-11-06T13:04:48.354Z'
```

Nice, the alert was transmitted successfully! Once the first alert was emitted, the alert Schema was submitted to the Schema Registry.

## Checking Alert Schema in Schema Registry
For it, in a terminal, run the following command to all subjects present in the Schema Registry:

```curl
curl -i localhost:8081/subjects
```

It should return:

```http
HTTP/1.1 200 OK
...
["com.example.alertproducer.avro.Alert"]
```

We can check the versions of the com.example.alertproducer.avro.Alert subject:

```curl
curl -i localhost:8081/subjects/com.example.alertproducer.avro.Alert/versions
```

We should get:

```http
HTTP/1.1 200 OK
...
[1]
```

Now, let’s check all schemas:

```curl
curl -i localhost:8081/schemas
```

The response should be:

```http
HTTP/1.1 200 OK
...
[{"subject":"com.example.alertproducer.avro.Alert","version":1,"id":1,"schema":"{\"type\":\"record\",\"name\":\"Alert\",\"namespace\":\"com.example.alertproducer.avro\",\"fields\":[{\"name\":\"description\",\"type\":\"string\"},{\"name\":\"AlertColor\",\"type\":{\"type\":\"enum\",\"name\":\"AlertColor\",\"symbols\":[\"YELLOW\",\"ORANGE\",\"RED\"]}},{\"name\":\"createdOn\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}}]}"}]
```

We can retrieve a specific schema by id:

```curl
curl -i localhost:8081/schemas/ids/1
```

The response should be:

```http
HTTP/1.1 200 OK
...
{"schema":"{\"type\":\"record\",\"name\":\"Alert\",\"namespace\":\"com.example.alertproducer.avro\",\"fields\":[{\"name\":\"description\",\"type\":\"string\"},{\"name\":\"AlertColor\",\"type\":{\"type\":\"enum\",\"name\":\"AlertColor\",\"symbols\":[\"YELLOW\",\"ORANGE\",\"RED\"]}},{\"name\":\"createdOn\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}}]}"}
```

## Shutdown
In the terminals where the Alert Producer and Consumer are running, press Ctrl+C to stop them.

In order to stop the Docker Compose services, in a terminal, make sure you are inside alert-app root folder and run the following command:

```docker
docker compose down -v
```