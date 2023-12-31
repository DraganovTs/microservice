![generalArchitecture](https://github.com/DraganovTs/microservice/assets/104751714/11418523-6549-42a4-a653-47824c595f7d)

**Services**        [🔗&#xFE0E;](https://github.com/DraganovTs/microservice#services)

- **Config-server:** Spring-cloud-config, retrieves the configs in config-server-repository and each service uses them.

- **Twitter-to-kafka-service:** Simulates a stream of tweets from twitter and send them to a kafka topic called "twitter-topic".

- **Kafka-to-elastic-service:** Consumes the events from "twitter-topic" and sends them to Elasticsearch.

- **Kafka-streams-service:** Consumes the events from "twitter-topic" and groups them by (key, word) and after count (word-count):

- [ ] Save the result in the kafka streams state store.
- [ ] It sends the new event (word, count) to "twitter-analytics-topic".

This service offers a RESTful api to retrieve the word count from the kafka streams state store.

- **Analytics-service:** Consumes the events from "twitter-analytics-topic" and persists them in PostgreSQL. It offers as well a RESTful API to retrieve the word count from the PostgreSQL DB.

- **Elastic-query-Service:** Query Elasticsearch based on client interactions. Returns how many times a word appears in each document. To accomplish that it can make a REST request to Analytics-service or Elastic-query-service (to choose to go in docker-compose/services.yml and for elastic-query-service-1 or elastic-query-service-2 change: "ELASTIC-QUERY-SERVICE_WEBCLIENT_QUERY-TYPE" to "ANALYTICS_DATABASE" or "KAFKA_STATE_STORE").

- **Elastic-query-web-client:** Client that allows the user to log in and search for a tweet. It asks for an elastic-query-service to retrieve the tweets based on the user input in the search bar. It as well returns the words count to see in how many documents each word appears.

- **Elastic-query-web-client-2:** Same but created to show how to implement SSO.

- **reactive-elastic-query-web-client:** Same but created to show how to implement reactive web.

- **Discovery-service:** Server for service registration and discovery.

Gateway-service: API Gateway with rate limiting using Redis as datastore for rate limiter. We defined 3 properties for the rate limiter.



**Config:**

Redis-rate-limiter.replenishRate: Rate at which the token basket is filled. It defines the request/second a user is allowed to do.
Redis-rate-limiter.burstCapacity: Max. number of requests a user is allowed to do in a single second. Can be used to allow for a temporary burst up to this number. Also sets the number of tokens a token can hold. Setting this to zero blocks all requests.
Redis-rate-limiter.requestedTokens: Defines how many tokens a request costs. Defaults to 1.

**E.G.:**

- replenishRate=5, burstCapacity=10, requestedTokens=1: Token bucket will be filled by 5 in each second, but with max. a number of allowed tokens as 10. Users can send 10 requests at the first second, as burst 10 is allowed, however, cannot send another request until the second 3 as the allowance of 2 seconds is already spent in the first second.
- replenishRate=1, burstCapacity=60, requestedTokens=60: Token bucket will be filled by 1 in each second, with max. a number of 60 tokens. Since requestedTokens is 60, any request will cost 60 tokens, and a request that comes between second 1 and second 60 will be allowed and then it will be blocked until 61 seconds. That implies 1 request/min.

Any request that goes beyond the rate limit will result in HTTP 429 - Too Many Requests as responses.

We used Resilience4j defining a filter named CircuitBreaker inside the gateway and for each service a fallback uri. Resilience4j CircuitBreaker is implemented using a finite state machine with 3 normal states: CLOSED, OPEN and HALF_OPEN.

**Basic Transitions:**

- CLOSED -> OPEN: Occurs when the failure rate is equal or greater than a configurable threshold, for example when more than 25% of calls fail.
- CLOSED -> OPEN: This also occurs if slow call percentage is equal or greater than a configurable threshold, for instance, when 25% of calls took longer than 5 seconds.
- OPEN -> HALF_OPEN: After the configured wait duration passes, the state can switch to HALF_OPEN where it has a configured amount of calls allowed.
- HALF_OPEN -> OPEN: If the failure rate or slow call rate is still equal or greater than a configurable threshold.
- HALF_OPEN -> CLOSED If the failure rate and slow call rate still is less than a configurable threshold.

**Note:** We use spring sleuth to send trance/ span data from different services to kafka, after zipkin consumes directly to kafka for distributed tracing and persist everything in a MySQL db. We could have sent from each service through HTTP to zipkin, but with the first implementation, we have decoupled.

**Modules**[🔗&#xFE0E;](https://github.com/DraganovTs/microservice#modules)

**App-config-data:** Objects representing the configs retrieved from spring-cloud-config.

**Common config:** common configs.

**Common util:** common utils.

**Elastic Folders**

- Elastic-config: Get the configs to communicate correctly with Elasticsearch.
- Elastic-index-client: Index correctly the documents in Elasticsearch.
- elastic-model: define the index model and the tweet model for Elasticsearch.
- Elastic-query-client: define how to query Elasticsearch to get documents.

**Kafka Folders**

**Kafka-model:** Create and hold java object for kafka in Avro format.
Kafka-admin: Create and verify Kafka topics programmatically.

**Kafka-producer:** Use spring-kafka to write Kafka producer implementation.
**Kafka-consumer:** Use spring-kafka to write Kafka consumer implementation.

**HOW TO RUN IT**[🔗&#xFE0E;](https://github.com/DraganovTs/microservice#how-to-run-it)
Launch docker.

From the root folder to create the docker images type in the terminal:


```
mvn clean install -DskipTests
```


Make all the scripts runnable (needed for the services in docker-compose):


```
chmod +x docker-compose/scripts/*.sh
```



To access the web client on browser, we need to update the DNS with hostnames, because when web-client, for instance, is redirecting to the user login (keycloack), it will do it to http://keycloack-authorization-server:9091/ hostname, that is the container running in docker-compose and externally is accessible on localhost:9091. So from the terminal let's type:



```
vi /etc/hosts
```


and after add:

```
127.0.0.1       keycloak-authorization-server
127.0.0.1       elastic-query-service-1
127.0.0.1       elastic-query-service-2
127.0.0.1       elastic-query-web-client-1
127.0.0.1       elastic-query-web-client-2
```
And save.

Now we can run everything from the folder docker-compose by typing:


```
docker-compose up
```


Wait for a while, till the services are up and running (can take long).

You can access the elastic-query-web-client from a browser at:

http://elastic-query-web-client-1:8184/elastic-query-web-client/

To login:

Username: spring_cloud_user

Password: 1234
