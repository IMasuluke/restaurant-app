package com.example.mrd.debug

enum class ApiIssueScenario(
    val title: String,
    val message: String
) {
    ApiTimeout(
        title = "API timeout",
        message = "The restaurant service took too long to respond. Please retry."
    ),
    ServerError(
        title = "HTTP 500",
        message = "The restaurant service returned an unexpected server error."
    ),
    Offline(
        title = "Network unavailable",
        message = "No connection is available. Check your connection and try again."
    ),
    MalformedResponse(
        title = "Malformed response",
        message = "The restaurant service returned data the app could not parse."
    )
}
