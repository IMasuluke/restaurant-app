package com.example.mrd.debug

import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IssueSimulationController {
    private val nextRefreshIssue = AtomicReference<ApiIssueScenario?>(null)
    private val _armedIssue = MutableStateFlow<ApiIssueScenario?>(null)

    val armedIssue: StateFlow<ApiIssueScenario?> = _armedIssue

    fun armNextRefresh(issue: ApiIssueScenario) {
        nextRefreshIssue.set(issue)
        _armedIssue.value = issue
    }

    fun clear() {
        nextRefreshIssue.set(null)
        _armedIssue.value = null
    }

    fun consumeNextRefreshIssue(): ApiIssueScenario? {
        return nextRefreshIssue.getAndSet(null)
            ?.also { _armedIssue.value = null }
    }
}
