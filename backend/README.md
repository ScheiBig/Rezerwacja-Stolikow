# Rezerwacja Stolików - Backend

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

### `gradle test` 🚧 Maybe someday

Launches the unit tests.

## Available Endpoints

---

#### Common errors:

- `400 Bad request` - The request parameters might be missing,
- `401 Unauthorized` - The endpoint requires the authentication token,
- `403 Forbidden` - The endpoint contains the resource, which the token's user isn't owner of,
- `404 Not found` - The request contains parameter pointing to the list of static data, but its value is not found,
- `408 Request timeout` - The endpoint has locked some data via session-like token, and token timed-out,
- `409 Conflict` - The endpoint should point to data, that is taken by someone else,
- `422 Unprocessable entity` The parameters provided to the request fail validation,
- `423 Locked` - The endpoint should point to data, that is currently locked by (reserved for) someone else.

---

### [`⚓/restaurants`](http://localhost:42069/restaurants)

###### Retrieves list of all restaurants. Never fails.

#### Responses:

- `200 OK`: array of Restaurant objects
  ```ts
  [
    {
      number: Integer,
      name: String,
      openingHours: {
          day("monday" - "sunday"): {
              from: NumberEncodedTime,
              to: NumberEncodedTime
          }
      }
    }, …
  ]
  ```
  where `NumberEncodedTime` for 21:37 will be `2137`.
  `number` property is guaranteed to be unique and thus can be used as `key`.

### [`⚓/dining_tables/search`](http://localhost:42069/dining_tables/search)

###### Finds list of dining tables matching criteria

#### Query parameters:

| Parameter | Type | Description |
| --- | --- | --- |
| restaurantId | Integer | `number` property of restaurant |
| smokingAllowed | Boolean ? | if table is in smoking-allowed zone - if not specified filtering is skipped |
| byWindow | Boolean ? | if table should be directly by window - if not specified filtering is skipped |

#### Responses:

- `200 OK`: array of DiningTable objects
  ```ts
  [
    {
      restaurant: Integer,
      number: Integer,
      smokingAllowed: Boolean,
      byWindow: Boolean
    }, …
  ]
  ```
  empty array migth be returned if none are found.
- `400 Bad request`: `restaurantId` is missing
- `404 Not found`: `restaurantId` does not represent available restaurant
- `422 Unprocessable entity` - any of parameters is wrong type

### ##TODO add images##
