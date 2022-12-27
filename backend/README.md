# Rezerwacja Stolików - Backend

### Project created using:
- ![favicon](https://www.google.com/s2/favicons?domain=https://gradle.org/) Gradle ([🗃️ docs](https://docs.gradle.org/current/userguide/userguide.html))
- ![favicon](https://www.google.com/s2/favicons?domain=https://kotlinlang.org/) Kotlin ([🗃️ docs](https://kotlinlang.org/docs/home.html))
- ![favicon](https://www.google.com/s2/favicons?domain=https://ktor.io/) Ktor ([🗃️ docs](https://ktor.io/docs/welcome.html))
- ![favicon](https://www.google.com/s2/favicons?domain=https://www.jetbrains.com/lp/mono/) Exposed ([🗃️ docs](https://github.com/JetBrains/Exposed/wiki))

## Available Tasks

### `gradle run`

Builds and runs the server.\
The endpoints will be available at [http://localhost:42069](http://localhost:42069).

For the list of available endpoints, their inputs and outputs schemas, and side-effects, see the next section below.

### `gradle test` 🚧 Maybe someday

Launches the unit tests.

## Available Endpoints

#### Common errors:
- `401 Unauthorized` - The endpoint requires the authentication token
- `403 Forbidden` - The endpoint contains the resource, which the token's user isn't owner of
- `404 Not found` - The request contains parameter pointing to the list of static data, but its value is not found
- `408 Request timeout` - The endpoint has locked some data via session-like token, and token timed-out
- `409 Conflict` - The endpoint should point to data, that is taken by someone else
- `422 Unprocessable entity` The parameters provided to the request fail validation 
- `423 Locked` - The endpoint should point to data, that is currently locked by (reserved for) someone else

### `##TODO##`

