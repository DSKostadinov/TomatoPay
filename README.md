# TomatoPay

## Architecture
I have used a layered approach where the application classes are loosely coupled.
This enables the application to be maintanable, extendable and easier to test. Also it provides readability.

I am using in-memory H2 database to store the Transactions and org.springframework.boot:spring-boot-starter-data-jpa 
which provides the CRUD queries for me to reduce boilerplate code.

I am leveraging the ThreadPoolTaskExecutor with custom properties defined in the application.yml.
It is being used together with Spring's @Async annotation to enable asynchronous creations of Transactions.

For better experience Swagger UI and ControllerAdvice are provided.

## Running locally
In order to run locally do the following:
1. Run 'gradlew clean build' from within the root folder
2. Run 'java -jar build/libs/demo-0.0.1-SNAPSHOT.jar' from the root folder or import the project in an IDE and run it from there
3. Go to http://localhost:8080/swagger-ui.html# and run any endpoint

Example JSON body for creating a Transaction (note accountId should be UUID formatted String and amount should be a positive number and we don't provide id as it is auto-generated):
```json
{
"accountId": "d783f984-7efe-4639-a846-ac70ca340a89",
"currency": "string",
"amount": 0.5,
"description": "string",
"type": "DEBIT"
}
```

Once you have created a Transaction you can copy the id from the response body and use it to get/update/delete a transaction by id.
