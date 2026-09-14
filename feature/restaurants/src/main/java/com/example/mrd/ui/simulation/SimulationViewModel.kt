package com.example.mrd.ui.simulation

import androidx.lifecycle.ViewModel
import com.example.mrd.debug.ApiIssueScenario
import com.example.mrd.debug.IssueSimulationController
import kotlinx.coroutines.flow.StateFlow

class SimulationViewModel(
    private val issueSimulationController: IssueSimulationController
) : ViewModel() {
    val armedIssue: StateFlow<ApiIssueScenario?> = issueSimulationController.armedIssue

    fun armNextRefresh(issue: ApiIssueScenario) {
        issueSimulationController.armNextRefresh(issue)
    }

    fun clear() {
        issueSimulationController.clear()
    }
}
