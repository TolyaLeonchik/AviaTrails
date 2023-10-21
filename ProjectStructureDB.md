classDiagram
direction BT
class airlines {
   varchar airline_name
   varchar country
   bigint port_id
   bigint id
}
class airports {
   varchar port_name
   varchar city
   varchar country
   varchar airport_code
   bigint id
}
class flight_table {
   bigint airline_id
   bigint from_airport_id
   bigint to_airport_id
   timestamp departure_time
   timestamp arrival_time
   integer flight_price
   integer number_of_free_seats
   bigint id
}
class payment_history {
   bigint ticket_id
   bigint passenger_id
   varchar card_number
   bigint id
}
class security_credentials {
   varchar user_login
   varchar user_password
   varchar user_role
   bigint user_id
   bigint id
}
class ticket_table {
   bigint flight_id
   bigint passenger_id
   integer seat_number
   integer ticket_price
   integer number_of_tickets
   boolean active_status
   timestamp booking_expiration_time
   bigint id
}
class user_info {
   varchar first_name
   varchar last_name
   varchar phone_number
   varchar email
   bigint id
}

airlines  -->  airports : port_id:id
flight_table  -->  airlines : airline_id:id
flight_table  -->  airports : from_airport_id:id
flight_table  -->  airports : to_airport_id:id
payment_history  -->  ticket_table : ticket_id:id
payment_history  -->  user_info : passenger_id:id
security_credentials  -->  user_info : user_id:id
ticket_table  -->  flight_table : flight_id:id
ticket_table  -->  user_info : passenger_id:id
