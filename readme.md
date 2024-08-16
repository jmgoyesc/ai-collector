# collector

This project collects the network traffic from Kubeshark and store in mongoDB.

## Build

```bash
./gradlew jib --image=jmgoyesc/ai-collector:0.0.6
```

## MongoDB

```shell
docker run --name local_mongo -d -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=example -p 27017:27017 mongo
```