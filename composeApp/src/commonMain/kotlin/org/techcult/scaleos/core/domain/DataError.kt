package com.techcult.salesman.core.domain

sealed interface DataError : Error {

    enum class NetworkError : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN
    }

    enum class Validation: DataError{
        INVALID_EMAIL,
        INVALID_PASSWORD,
        PASSWORD_TOO_SHORT,
        PASSWORD_MISMATCH,
        INVALID_NAME,
        INVALID_PHONE_NUMBER,
        INVALID_ADDRESS,
        INVALID_CITY,
        INVALID_STATE,
        INVALID_ZIP_CODE,
        INVALID_COUNTRY,
        USER_ALREADY_EXISTS,
        USER_NOT_FOUND,
    }
}