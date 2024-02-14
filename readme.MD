# Customer orders processing

## Description
This is a Java demo app to process customer orders using 2 different services:
- **orders_api**
- **orders_processing**

## Tech stack
- [Spring 5 WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html) for the main App Framework.
- [MongoDB](https://www.mongodb.com).
- [RabbitMQ](https://rabbitmq.com/).
- [Docker](https://www.docker.com/).
- [Lombok](https://projectlombok.org/).
- [MapStruct](https://mapstruct.org/).
- [Maven3](https://maven.apache.org/).

## How to use
1. Using Docker (required).

Navigate to the /docker directory and run the command
```shell
docker % sh up.sh
```
or run manually the docker compose command
```shell
docker % docker-compose up -d
```
This will download the required docker images, create and start the required containers.
Finally issue the REST api commands against the localhost using the port 8080.

To stop, use the shell command
```shell
docker % sh down.sh
```
or run manually the docker compose command
```shell
docker % docker-compose down --rmi all -v --remove-orphans
```
2. Run the Java tests. (Required Java 17+ and maven3+ installed)

Navigate to the /orders_api or to the /orders_processor directory and run the command
```shell
orders_api % mvn test
```

## orders_processing
Receives the new created orders and calculates its totals. Deployed by default to the port 8088.

## orders_api
Exposes the REST endpoints to submit orders. Deployed by default to the port 8080.

Endpoints:
```json lines
Create a new order
POST /orders
JSON BODY 
{
  "customer": {
    "id": "<customer_id>"
  },
  "items": [
    {
      "id": "<item_id>"
    } 
  ]
}
```
**Example**: localhost:8080/orders
```json
{
  "customer": {
    "id": "65c30003e6e2b520c5064d58"
  },
  "items": [
    {
      "id": "65c303d0e6e2b520c5064ff0"
    },
    {
      "id": "65c303d0e6e2b520c5064ff3"
    }
  ]
}
```
**response**: 201 CREATED
```json
{
  "id": "65cc1d3d6edbfd292b84bc70",
  "customer": {
    "id": "65c30003e6e2b520c5064d58"
  },
  "items": [
    {
      "id": "65c303d0e6e2b520c5064ff0"
    },
    {
      "id": "65c303d0e6e2b520c5064ff3"
    }
  ],
  "status": "CREATED"
}
```
```
Cancel an order
PUT /orders/{order_id}?operation=CANCEL
returns: 200 OK or 404 NOT FOUND
```
**Example**: localhost:8080/orders/65cc1d3d6edbfd292b84bc70?operation=CANCEL

**response**: 200 OK
```json
{
  "id": "65cc1d3d6edbfd292b84bc70",
  "customer": {
    "id": "65c30003e6e2b520c5064d58",
    "name": "John Doe",
    "email": "doe.john@fakemail.net"
  },
  "items": [
    {
      "id": "65c303d0e6e2b520c5064ff0",
      "name": "Bag of mangos",
      "category": "Produce",
      "price": 5.0
    },
    {
      "id": "65c303d0e6e2b520c5064ff3",
      "name": "Pack of bagels",
      "category": "Bakery",
      "price": 9.25
    }
  ],
  "status": "CANCELED"
}
```
```
Retrieve all orders
GET /orders
```
**Example**: localhost:8080/orders

**response**: 200 OK
```json
[
  {
    "id": "65cc1d3d6edbfd292b84bc70",
    "customer": {
      "id": "65c30003e6e2b520c5064d58",
      "name": "John Doe",
      "email": "doe.john@fakemail.net"
    },
    "items": [
      {
        "id": "65c303d0e6e2b520c5064ff0",
        "name": "Bag of mangos",
        "category": "Produce",
        "price": 5.0
      },
      {
        "id": "65c303d0e6e2b520c5064ff3",
        "name": "Pack of bagels",
        "category": "Bakery",
        "price": 9.25
      }
    ],
    "status": "CANCELED"
  },
  {
    "id": "65cc1e426edbfd292b84bc71",
    "customer": {
      "id": "65c30003e6e2b520c5064d58",
      "name": "John Doe",
      "email": "doe.john@fakemail.net"
    },
    "items": [
      {
        "id": "65c303d0e6e2b520c5064ff0",
        "name": "Bag of mangos",
        "category": "Produce",
        "price": 5.0
      },
      {
        "id": "65c303d0e6e2b520c5064ff3",
        "name": "Pack of bagels",
        "category": "Bakery",
        "price": 9.25
      }
    ],
    "status": "CREATED"
  }
]
```
```
Retrieve an order by id
GET /orders/{order_id}
returns: 200 OK or 404 NOT FOUND
```
**Example**: localhost:8080/orders/65cc1d3d6edbfd292b84bc70

**response**: 200 OK
```json
{
  "id": "65cc1d3d6edbfd292b84bc70",
  "customer": {
    "id": "65c30003e6e2b520c5064d58",
    "name": "John Doe",
    "email": "doe.john@fakemail.net"
  },
  "items": [
    {
      "id": "65c303d0e6e2b520c5064ff0",
      "name": "Bag of mangos",
      "category": "Produce",
      "price": 5.0
    },
    {
      "id": "65c303d0e6e2b520c5064ff3",
      "name": "Pack of bagels",
      "category": "Bakery",
      "price": 9.25
    }
  ],
  "status": "CANCELED"
}
```
```
Retrieve all orders that a customer placed
GET /customers/{customer_id}/orders
```
**Example**: localhost:8080/customers/65c30003e6e2b520c5064d58/orders

**response**: 200 OK
```json
[
  {
    "id": "65cc1d3d6edbfd292b84bc70",
    "customer": {
      "id": "65c30003e6e2b520c5064d58",
      "name": "John Doe",
      "email": "doe.john@fakemail.net"
    },
    "items": [
      {
        "id": "65c303d0e6e2b520c5064ff0",
        "name": "Bag of mangos",
        "category": "Produce",
        "price": 5.0
      },
      {
        "id": "65c303d0e6e2b520c5064ff3",
        "name": "Pack of bagels",
        "category": "Bakery",
        "price": 9.25
      }
    ],
    "status": "CANCELED"
  },
  {
    "id": "65cc1e426edbfd292b84bc71",
    "customer": {
      "id": "65c30003e6e2b520c5064d58",
      "name": "John Doe",
      "email": "doe.john@fakemail.net"
    },
    "items": [
      {
        "id": "65c303d0e6e2b520c5064ff0",
        "name": "Bag of mangos",
        "category": "Produce",
        "price": 5.0
      },
      {
        "id": "65c303d0e6e2b520c5064ff3",
        "name": "Pack of bagels",
        "category": "Bakery",
        "price": 9.25
      }
    ],
    "status": "CREATED"
  }
]
```
```
Retrieve total of items and amount spend per customer per categories
GET /customers/{customer_id}/totals
```
**Example**: localhost:8080/orders/65cc1d3d6edbfd292b84bc70

**response**: 200 OK
```json
{
  "id": "65cc1d3e0bd3d36b8f148d78",
  "customer": {
    "id": "65c30003e6e2b520c5064d58",
    "name": "John Doe",
    "email": "doe.john@fakemail.net"
  },
  "totals": {
    "Produce": {
      "quantity": 1,
      "amount": 5.0
    },
    "Bakery": {
      "quantity": 1,
      "amount": 9.25
    }
  }
}
```
## Notes
1. **Comments**: only a few of the code is documented with javadoc, this is intentional due the best practice of preventing
redundant javacode in favor of self documented code.

## Pending and TODOS
1. Validate data on **create** operations, currently is possible to create orders using non-existing customers and items.
2. Create CRUD endpoints on items and finish operations on customers
3. Add logging and metrics

## Test data
```js
// customers
[
  {
    "_id": ObjectId("65c30003e6e2b520c5064d58"),
    "name": "John Doe",
    "email": "doe.john@fakemail.net"
  },
  {
    "_id": ObjectId("65c302dae6e2b520c5064fdc"),
    "name": "Jane Roe",
    "email": "emily.johnson@emailfake.com"
  }
]

// items
[
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff0"),
    "name": "Bag of mangos",
    "category": "Produce",
    "price": 5.0
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff1"),
    "name": "Basket of avocados",
    "category": "Produce",
    "price": 7.50
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff2"),
    "name": "Half dozen of donuts",
    "category": "Bakery",
    "price": 8.0
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff3"),
    "name": "Pack of bagels",
    "category": "Bakery",
    "price": 9.25
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff4"),
    "name": "Bag of rice",
    "category": "Pantry staples",
    "price": 6.50
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff5"),
    "name": "Extra virgin olive oil",
    "category": "Pantry staples",
    "price": 12.0
  }  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff0"),
    "name": "Bag of mangos",
    "category": "Produce",
    "price": 5.0
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff1"),
    "name": "Basket of avocados",
    "category": "Produce",
    "price": 7.50
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff2"),
    "name": "Half dozen of donuts",
    "category": "Bakery",
    "price": 8.0
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff3"),
    "name": "Pack of bagels",
    "category": "Bakery",
    "price": 9.25
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff4"),
    "name": "Bag of rice",
    "category": "Pantry staples",
    "price": 6.50
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff5"),
    "name": "Extra virgin olive oil",
    "category": "Pantry staples",
    "price": 12.0
  }
]
```
