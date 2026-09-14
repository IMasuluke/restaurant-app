package com.example.mrd.ui.simulation

import com.example.mrd.debug.ApiIssueScenario

enum class SimulationScenario(
    val title: String,
    val message: String,
    val apiIssue: ApiIssueScenario
) {
    ApiTimeout(
        title = ApiIssueScenario.ApiTimeout.title,
        message = ApiIssueScenario.ApiTimeout.message,
        apiIssue = ApiIssueScenario.ApiTimeout
    ),
    ServerError(
        title = ApiIssueScenario.ServerError.title,
        message = ApiIssueScenario.ServerError.message,
        apiIssue = ApiIssueScenario.ServerError
    ),
    Offline(
        title = ApiIssueScenario.Offline.title,
        message = ApiIssueScenario.Offline.message,
        apiIssue = ApiIssueScenario.Offline
    ),
    MalformedResponse(
        title = ApiIssueScenario.MalformedResponse.title,
        message = ApiIssueScenario.MalformedResponse.message,
        apiIssue = ApiIssueScenario.MalformedResponse
    )
}
