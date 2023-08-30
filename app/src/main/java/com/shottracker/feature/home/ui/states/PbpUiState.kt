package com.shottracker.feature.home.ui.states

import com.shottracker.feature.home.ui.models.TeamUi

/**
 * Various states for UI
 */
sealed class PbpUiState {
    /**
     * Loading screen state
     */
    object Loading:PbpUiState()

    /**
     * Games state
     */
    class Success(val homeUi:TeamUi, val awayUi:TeamUi ) : PbpUiState()

    /**
     * Retry loading
     */
    object ErrorRetry:PbpUiState()
}
