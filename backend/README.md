# Rezerwacja Stolików — Backend

### Project created using:
- ![favicon](https://www.google.com/s2/favicons?domain=gradle.org)
  Gradle ([🗃️ docs](https://docs.gradle.org/current/userguide/userguide.html))
- ![favicon](https://www.google.com/s2/favicons?domain=play.kotlinlang.org)
  Kotlin ([🗃️ docs](https://kotlinlang.org/docs/home.html))
- ![favicon](https://www.google.com/s2/favicons?domain=ktor.io) Ktor ([🗃️ docs](https://ktor.io/docs/welcome.html))
- ![favicon](https://www.google.com/s2/favicons?domain=www.jetbrains.com/lp/mono/)
  Exposed ([🗃️ docs](https://github.com/JetBrains/Exposed/wiki))

## Available Tasks

### `gradle run`

Builds and runs the server.\
The endpoints will be available at [http://localhost:42069](http://localhost:42069) (the address will later be mentioned
as "⚓").

For the list of available endpoints, their inputs and outputs schemas, and side effects, see the next section below.

Adding `--args='-P:shouldPurge=true'` will reset persistence for PendingLocks and Reservations.

### `gradle test` 🚧 Maybe someday

Launches the unit tests.

## Available Endpoints

---

#### Common errors:

- `400 Bad request` — The request URL parameters might be missing,
- `401 Unauthorized` — The endpoint requires the authentication token,
- `403 Forbidden` — The endpoint contains the resource, which the token's user isn't owner of,
- `404 Not found` — The request contains parameter pointing to the list of static data, but its value is not found,
- `405 Method not allowed` — The endpoint doesn't exist, or allow such `HttpMethod` of access,
- `408 Request timeout` — The endpoint has locked some data via session-like token, and token timed-out,
- `409 Conflict` — The endpoint should point to data, that is taken by someone else,
- `410 Gone` — The request carries data, that is expired,
- `412 Precondition failed` — static-structure data (usually JWT) is malformed — this can indicate either server internal-API error, or token signature secret being compromised,
- `422 Unprocessable entity` — The parameters provided to the request fail validation,
- `423 Locked` — The endpoint should point to data, that is currently locked by (reserved for) someone else.

---



### [`⚓/restaurants :: 🎁GET`](http://localhost:42069/restaurants)

###### Retrieves list of all restaurants. Never fails.

#### Responses:

- `200 OK`: array of Restaurant objects:
  ```ts
  [
    {
      ID: Long,
      name: String,
      openingHours: {
          day("monday" - "sunday"): {
              from: @ISO_8601("HH:mm") String,
              to: @ISO_8601("HH:mm") String
          }
      },
      image: @URLRelative String,
      map: @URLRelative String
    }, …
  ]
  ```



### [`⚓/dining_tables/search/{restaurant_ID} :: 📨POST`](http://localhost:42069/dining_tables/search/…)

###### Finds list of dining tables matching criteria

#### URL parameters:
- `restaurantID: Integer` — `number` of restaurant.

#### Request body:

```ts
{
  byWindow: Boolean?,
  outside: Boolean?,
  smokingAllowed: Boolean?,
}?
```
filters response based on present fields.

#### Responses:

- `200 OK`: array of DiningTable objects:
  ```ts
  {
    restaurantID: Long,
    number: Integer,
    byWindow: Boolean,
    outside: Boolean,
    smokingAllowed: Boolean,
    $chairs: Integer,
    $mapLocation: {
      x: Integer,
      y: Integer,
      w: Integer,
      h: Integer
    }
  }[]
  ```
  empty array might be returned if none are found.\
  Combination of `restaurantID` and `number` properties is guaranteed to be unique for each object and thus
  can be used as `key`.\
  Properties `chairs` and `mapLocation` are only present in unfiltered request.
- `400 Bad request`: `restaurantID` is missing.
- `404 Not found`: `restaurantID` does not represent available restaurant
- `422 Unprocessable entity`: `restaurantID` is not an `Integer`.

---



### [`⚓/dining_tables/locks :: PUT`](http://localhost:42069/dining_tables/locks)
###### Locks selected reservation slot, so reservation can be finalized

#### Request body:

```ts
{
  diningTable: {
    restaurantID: Long,
    number: Integer
  },
  bounds: {
    from: @ISO_8601("uuuu-MM-dd'T'HH:mm") String,
    durationH: @Hours Long
  }
}
```
`from` property should be representing beginning of hour, if not its value will be floored to full hour.

#### Responses:

- `200 OK`: JWT of type `lock`, that expires after 2 minutes.
- `404 Not found`: `diningTable` doesn't represent existing table.
- `409 Conflict`: there is already a reservation made on colliding reservation slot.
- `410 Gone`: `from` property represents past date.
- `412 Precondition failed`: `bounds` are at least partially outside of opening hours of given restaurant.
- `422 Unprocessable entity`: body is missing or malformed.
- `423 Locked`: there is already a lock put on colliding reservation slot.



### [`⚓/dining_tables/locks :: 🗑️DELETE [🔐lock]`](http://localhost:42069/dining_tables/locks)
###### Removes previously created lock, if one hasn't already expired

#### Responses:

- `202 Accepted`: 
  ```ts
  `Unlocked ${n} reservation${n == 1 ? "" : "s"}`
  ```
  lock has been found and released.
- `401 Unauthorized`: `bearer_token` missing.
- `403 Forbidden`: `bearer_token` type is not `lock`.
- `404 Not found`: `bearer_token` doesn't represent valid, not expired lock. 

---



### [`⚓/sms_checking/reservations :: 🛒PUT`](http://localhost:42069/sms_checking/reservations)
###### Returns sms verification code (mocks sending code via sms)

#### Request body:

```ts
{
  phoneNumber: @PhoneNumber Long
}
```

#### Responses:

- `200 OK`: 6-digit, random code that expires after 2 minutes.
- `422 Unprocessable entity`: body is missing or malformed, or `phoneNumber` doesn't represent valid 9-digit polish phone number.



### [`⚓/sms_checking/reservations :: 📨POST`](http://localhost:42069/sms_checking/reservations)
###### Generates access token

#### Request body:

- 6-digit, random verification code from [`🛒PUT`](./README.md#sms_checkingreservations--put) route.

#### Responses:

- `200 OK`: JWT of type `access`, that expires after 2 minutes.
- `401 Unauthorized`: verification code is missing.
- `403 Forbidden`: verification code is not a valid 6-digit number, or doesn't exist.
- `410 Gone`: verification code has expired (client should automatically resend code request). number.

---



### [`⚓/dining_tables/reservations :: 🛒PUT [🔐lock]`](http://localhost:42069/dining_tables/reservations)
###### Finalizes reservation and returns its details

#### Request body:

```ts
{
    firstName: String,
    lastName: String,
    phoneNumber: @PhoneNumber Long
}
```

#### Responses:

- `201 Created`:
  ```ts
  {
    personDetails: {
        firstName: String,
        lastName: String,
        phoneNumber: @PhoneNumber Long
    },
    diningTable: {
        restaurantID: Long,
        number: Integer,
        byWindow: Boolean,
        outside: Boolean,
        smokingAllowed: Boolean
    },
    bounds: {
      from: @ISO_8601("uuuu-MM-dd'T'HH:mm") String,
      durationH: @Hours Long
    },
    removalToken: @JWT<"access">(2.minutes) String
  }
  ```
  reservation may be cancelled within 2 minutes using `removalToken`, cancellation in later time is possible using tokens generated in `🎁GET` route.
- `401 Unauthorized`: `bearer_token` missing.
- `403 Forbidden`: `bearer_token` type is not `cancel`.
- `404 Not found`: `bearer_token` doesn't represent existing reservation.



### [`⚓/dining_tables/reservations :: 🗑️DELETE [🔐cancel]`](http://localhost:42069/dining_tables/reservations)
###### Cancels reservation

#### Responses:

- `202 Accepted`: 
  ```ts
  `Cancelled ${n} reservation${n == 1 ? "" : "s"}`
  ```
  reservation has been found and cancelled.
- `401 Unauthorized`: `bearer_token` missing.
- `403 Forbidden`: `bearer_token` type is not `lock`.
- `404 Not found`: `bearer_token` doesn't represent valid, not expired lock. 



### [`⚓/dining_tables/reservations :: 🎁GET [🔐access]`](http://localhost:42069/dining_tables/reservations)
###### Lists all reservations made for phone number

#### Responses:
- `200 OK`:
  ```ts
  {
    personDetails: {
        firstName: String,
        lastName: String,
        phoneNumber: @PhoneNumber Long
    },
    diningTable: {
        restaurantID: Long,
        number: Integer,
        byWindow: Boolean,
        outside: Boolean,
        smokingAllowed: Boolean
    },
    bounds: {
      from: @ISO_8601("uuuu-MM-dd'T'HH:mm") String,
      durationH: @Hours Long
    },
    removalToken: @JWT<"access">(2.minutes) String
  }[]
  ```
  empty array might be returned if none are found.
- `401 Unauthorized`: `bearer_token` missing.
- `403 Forbidden`: `bearer_token` type is not `access`.



### [`⚓/restaurants/{restaurant_ID}/reservations :: 📨POST`](http://localhost:42069/restaurants/…/reservations)
###### Lists occupancy rates for specified restaurant and month & year

#### URL parameters:
- `restaurantID: Integer` — `number` of restaurant.
 
#### Request body:

```ts
{
  date: @ISO_8601("uuuu-MM-dd'T'HH:mm") String
}
```
only year and month from `date` are used, but for compatibility reasons full date is required. Passing values of beginning of the month is recommended, as is easiest.

#### Responses:

- `200 OK`: 
  ```ts
  Float[]
  ```
  each value in array represents % occupancy rate of restaurant in day number indicated by index. Occupancy rate for each day is calculated using formula:
  ```kotlin
  val occupancyAtDay = reservationsAtDay.fold(0.0) { acc, res -> 
    acc + res.bounds.durationH
  } / (diningTablesAtRestaurant.count() * 24)
  ```
  additionally, each day that has already ended will have its value increased by 1.
- `400 Bad request`: `restaurantID` is missing.
- `404 Not found`: `restaurantID` does not represent available restaurant
- `422 Unprocessable entity`: `restaurantID` is not an `Integer`.



### [`⚓/restaurants/{restaurant_ID}/reservations/search :: 📨POST`](http://localhost:42069/restaurants/…/reservations)
###### Lists dining tables matching criteria, that are available in given bounds

#### URL parameters:
- `restaurantID: Integer` — `number` of restaurant.
 
#### Request body:

```ts
{
  date: @ISO_8601("uuuu-MM-dd'T'HH:mm") String
  durationH: @Hours Long,
  filter: {
    byWindow: Boolean?,
    outside: Boolean?,
    smokingAllowed: Boolean?,
  }?
}
```
`filter` property, if present, filters response based on present fields.

#### Responses:

- `200 OK`: 
  ```ts
  {
    restaurantID: Long,
    number: Integer,
    byWindow: Boolean,
    outside: Boolean,
    smokingAllowed: Boolean
  }[]
  ```
  empty array might be returned if none are found.\
  Only immediately available tables are listed — only [locking call](./README.md#dining_tablesreservations--put-lock) is guaranteed to show actual availability of given dining table.
- `400 Bad request`: `restaurantID` is missing.
- `404 Not found`: `restaurantID` does not represent available restaurant.
- `410 Gone`: `date` has already passed.
- `422 Unprocessable entity`: body is missing or malformed.

---



### [`⚓/image/restaurant_thumb/{hash_ID} :: 🎁GET`](http://localhost:42069/image/restaurant_thumb/…)
###### Sends *.jpg image of thumbnail of the restaurant

#### URL parameters:

- `hash_ID: String` — hashed ID of the thumbnail

#### Responses:

- `200 OK`: *.jpg image with the thumbnail.\
  Neither images dimensions, nor aspect ratio is normalized.
- `400 Bad request`: `hash_ID` is missing.
- `404 Not found`: `hash_ID` doesn't represent existing resource.



### [`⚓/image/restaurant_map/{hash_ID} :: 🎁GET`](http://localhost:42069/image/restaurant_map/…)
###### Sends *.svg image of top view map of the restaurant

#### URL parameters:

- `hash_ID: String` — hashed ID of the map

#### Responses:

- `200 OK`: *.svg image with the map.\
  Map image should be overlayed with controls using `DinningTable.mapLocation` data.
- `400 Bad request`: `hash_ID` is missing.
- `404 Not found`: `hash_ID` doesn't represent existing resource.
