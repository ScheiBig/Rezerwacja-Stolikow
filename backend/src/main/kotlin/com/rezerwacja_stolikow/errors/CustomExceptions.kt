package com.rezerwacja_stolikow.errors


class AuthenticationException(message: String? = null): RuntimeException(message)
class AuthorizationException(message: String? = null): RuntimeException(message)

class LockTimeoutException(message: String? = null): RuntimeException(message)
class DataLockedException(message: String? = null): RuntimeException(message)
class DataTakenException(message: String? = null): RuntimeException(message)

class DataSpoiledException(message: String? = null): RuntimeException(message)
