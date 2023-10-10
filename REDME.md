# AviaTrails

The service provides an opportunity to book and pay for air tickets, search flights by parameters and detailed information about flights.

## Database

The avia database in memory is connected to the project.  It contains 7 tables.
UserInfo - stores information about users. At the time of creation, it has 3 users.
Airpors - contains information about airporsts. At the time of creation, it has 6 airports.
Ailines - contains information about airlines, including their identifiers, names,  cities and countries.
Flight - contains flight information including date and time of departure, place of departure, destination, flight duration and the airline operating the flight.
Payment history - contains information about the history of payments related to flight ticket bookings. It stores data on the payment amount, bank card number and the associated booking.
Ticket - contains information about tickets booked by users. It stores data about users, the number of tickets booked, their price and related flight information.
Markdown is a lightweight markup language based on the formatting conventions
that people naturally use in email.

## Registration
To register, you must go to "http://localhost:8080/registration".
We must pass json format
 ```sh
 ( POST method: {"login": "exampleLogin", "password": "examplePassword"} )
 ```
to "http://localhost:8080/registration" after which the user will be created and placed in the database.

## Authentication
Get access to endpoints we can only pass authentication.
After passing the authentication, the user has the role USER, MODERATOR or ADMIN.
The database contains 3 users with different roles:
- ADMIN: (Login: admin, Password: password),
- MODERATOR: (Login: moderator, Password: password),
- USER:(Login: user, Password: password);
 ```sh
EXCEPTIONS(no authentication required):
"http://localhost:8080/registration",
"http://localhost:8080/authentication",
"http://localhost:8080/flights/{flightId}",
"http://localhost:8080/flights/search"
 ```

# User capabilities
 ___
- no authentication required.

| Available endpoints | Description |
| ------ | ------ |
| http://localhost:8080/flights/{flightId} | GET method: Get data about flight with a specific id.|
| http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd} | GET method: Get data about flight with a specific parameters.|
`Examples: `
- For GET method: "http://localhost:8080/flights/{flightId}":
 ```sh
"http://localhost:8080/flights/4"
 ```
- For GET method: "http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd}":
 ```sh
"http://localhost:8080/flights/search?cityOfDeparture=Minsk&cityOfArrival=Amsterdam&date=2023-09-07"
 ```
___

- ROLE_USER - a user who's logged in.

| Available endpoints | Description |
| ------ | ------ |
| http://localhost:8080/user/{userId} | GET method: Get data about user with a specific id.|
| http://localhost:8080/user | PUT method: Update user.|
| http://localhost:8080/booking | POST method: Сreate ticket. |
| http://localhost:8080/booking/user/{userId} | GET method: Get data about all tickets of a user with a specific id.|
| http://localhost:8080/booking/pay/{userId} | POST method: Payment for tickets booked by the user. |
| http://localhost:8080/booking/refund/{ticketId} | DELETE method: Refund of a ticket with a specific ID. |
| http://localhost:8080/flights/{flightId} | GET method: Get data about flight with a specific id.|
| http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd} | GET method: Get data about flight with a specific parameters.|
`Examples: `
- For GET method: "http://localhost:8080/user/{userId}":
 ```sh
"http://localhost:8080/user/4"
 ```
- For POST method: "http://localhost:8080/booking", you need to pass json format:
 ```sh
{
        "firstName": "testName",
        "lastName": "testLastNamea",
        "airlane": "nameAirline",
        "portCityFrom": "namePortCityDeparture",
        "portCityTo": "namePortCityArrival",
        "portNameFrom": "namePortDeparture",
        "portNameTo": "namePortArrival",
        "departureTime": "yyyy-MM-dd'T'HH:mm:ss",
        "countOfTickets": 2
        "returnTicket": true,
        "airlane": "nameAirline",
        "portCityFrom": "namePortCityDeparture",
        "portCityTo": "namePortCityArrival",
        "portNameFrom": "namePortDeparture",
        "portNameTo": "namePortArrival",
        "departureTime": "yyyy-MM-dd'T'HH:mm:ss",
        "countOfTickets": 2
    }
 ```
- For GET method: "http://localhost:8080/booking/user/{userId}":
 ```sh
"http://localhost:8080/booking/user/4"
 ```
- For POST method: "http://localhost:8080/booking/pay/{userId}", you need to pass json format:
 ```sh
 "http://localhost:8080/booking/pay/4
{
        "firstName": "testName",
        "lastName": "testLastName",
        "numberCard": "0000000000000000",
        "balance": 2000
    }
 ```
- For DELETE method: "http://localhost:8080/booking/refund/{ticketId}":
 ```sh
"http://localhost:8080/booking/refund/14"
 ```
 ___

- ROLE_MODERATOR - grants extended rights over the basic ROLE_USER role.

| Available endpoints | Description |
| ------ | ------ |
| http://localhost:8080/user/{userId} | GET method: Get data about user with a specific id.|
| http://localhost:8080/user | POST method: Сreate user.|
| http://localhost:8080/user | PUT method: Update user.|
| http://localhost:8080/booking | POST method: Сreate ticket. |
| http://localhost:8080/booking/user/{userId} | GET method: Get data about all tickets of a user with a specific id.|
| http://localhost:8080/booking/pay/{userId} | POST method: Payment for tickets booked by the user. |
| http://localhost:8080/booking/refund/{ticketId} | DELETE method: Refund of a ticket with a specific ID. |
| http://localhost:8080/flights/{flightId} | GET method: Get data about flight with a specific id.|
| http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd} | GET method: Get data about flight with a specific parameters.|
`IMPORTANT!!! `
  ```sh
Unlike ROLE_USER, ROLE_MODERATOR has access to all users, not just its own account.
 ```

 ___

-  ROLE_ADMIN - has the highest privilege level in the system.

ROLE_ADMIN has unlimited rights: add, edit, delete users, customize their roles and access differentiation and access to all information.

| Available endpoints | Description |
| ------ | ------ |
| http://localhost:8080/user| GET method: Get data about ALL users.|
| http://localhost:8080/user/{userId} | GET method: Get data about user with a specific id.|
| http://localhost:8080/user | POST method: Сreate user.|
| http://localhost:8080/user | PUT method: Update user.|
| http://localhost:8080/user/{userId} | DELETE method: Deleting a user with a specific ID. |
| http://localhost:8080/booking/allTickets | GET method: Get data about all users tickets. |
| http://localhost:8080/booking | POST method: Сreate ticket. |
| http://localhost:8080/booking/user/{userId} | GET method: Get data about all tickets of a user with a specific id.|
| http://localhost:8080/booking/pay/{userId} | POST method: Payment for tickets booked by the user. |
| http://localhost:8080/booking/refund/{ticketId} | DELETE method: Refund of a ticket with a specific ID. |
| http://localhost:8080/flights/{flightId} | GET method: Get data about flight with a specific id.|
| http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd} | GET method: Get data about flight with a specific parameters.|
`Examples:`
- For POST method: "http://localhost:8080/user", you need to pass json format:
 ```sh
{
"firstName": "testName",
"lastName": "testLastName",
“email”:“testMail@example.com”,
“phoneNumber”:“+000000000000”
}
 ```
- For PUT method: "http://localhost:8080/user", you need to pass json format:
 ```sh
{
        "id": 11,
        "firstName": "testing",
        "lastName": "test",
        "email": "test@gmail.com",
        "phoneNumber": "+375297777777"
    }
 ```
- For DELETE method: "http://localhost:8080/user/{userId}":
 ```sh
"http://localhost:8080/user/14"
 ```