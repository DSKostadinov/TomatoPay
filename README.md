# TomatoPay

In order to run locally do the following:
1. Run 'gradle clean build' from within the root folder if using Windows or './gradle clean build' if using Linux
2. Run 'java -jar build/libs/demo-0.0.1-SNAPSHOT.jar' from the root folder or import the project in an IDE and run it from there
3. Go to http://localhost:8080/swagger-ui.html# and run any endpoint

Example JSON body for creating a Transaction (note accountId should be UUID formatted String and amount should be a positive number):
```json
{
"id": "cd9d2265-b9ba-4d5e-a5dc-8f1cbe4476c1",
"accountId": "d783f984-7efe-4639-a846-ac70ca340a89",
"currency": "string",
"amount": 0.5,
"description": "string",
"type": "DEBIT"
}
```
