
Command to run the Postgres Docker container

```
docker run -d -e POSTGRES_PASSWORD=postgres -e POSTGRES_USERNAME=postgres -e POSTGRES_DATABASE=postgres -p 5432:5432 postgres
```

Command to run the RabbitMQ Docker container

```
docker run -d -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```

Build Account Service image using Buildpacks 

```
./mvnw clean spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=account-svc
```

To Run all container using `docker compose`

```
docker compose -d up
```

`-d` option is to run as detached mode