package com.example.mrd.debug

class SimulatedApiException(
    issue: ApiIssueScenario
) : RuntimeException(issue.message)
