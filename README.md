# Simple Payment Application API

## Solution Approach

### Stack:
* Java 
* MySQL
* Spring Boot
* Spring JPA
* Spring Retry
* ModelMapper
* Docker

### Highlights

The solution contains 3 APIs:

#### Payment
The payment API post saves the Payment object and it triggers the webhooks list.

#### Webhook
Webhook contains the URL, event type (PAYMENT) and the list of headers to guarantee the webhook is dynamic.

#### Webhooks that failed.
When a triggered webhook, fails after retry, the recover method saves a object represented with the Payment and Webhook to be sent in the future.
This part was not implement, but it could be @Scheduled job or any other approach to collect all failed webhook and post them again.

### Swagger
The API can be used via Swagger on:

http://localhost:8080/swagger-ui/index.html#/

and Open API Specification on:

http://localhost:8080/v3/api-docs

## Run the application

Go to the root folder and type:

**docker compose up**

## Application Usage

It was not implemented the front-end part.
All the tests could be executed on Swagger front-end, postman tools like or via CURL command.
Below, some CURL commands to use the application.

### Payments
**List the payments:**

curl -X 'GET' \
'http://localhost:8080/payments' \
-H 'accept: */*'

**Save a payment**

curl -X 'POST' \
'http://localhost:8080/payments' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"firstName": "Arda",
"lastName": "Guller",
"zipCode": "28001",
"cardNumber": "124456896688"
}'

### Webhooks

**List all webhooks**

curl -X 'GET' \
'http://localhost:8080/webhooks' \
-H 'accept: */*'


**Save a webhook** for a specific url, for example: 
http://localhost:3030/moreonetest

curl -X 'POST' \
'http://localhost:8080/webhooks' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"url": "http://localhost:3030/moreonetest",
"type": "PAYMENT",
"params": [
{
"name": "Content-Type",
"value": "json"
},
{
"name": "anything",
"value": "ok"
}
]
}'

### Failed Webhooks
**List the failed webhooks to be processed later:**

curl -X 'GET' \
'http://localhost:8080/webhooks/reprocess' \
-H 'accept: */*'


### Reference Documentation

* [Encrypt and Decrypt data](https://smattme.com/posts/how-to-encrypt-decrypt-rsa-in-java/)

