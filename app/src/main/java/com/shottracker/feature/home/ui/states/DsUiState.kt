package com.shottracker.feature.home.ui.states

import com.shottracker.feature.home.models.GamesItem

/**
 * Various states for UI
 */
sealed class DsUiState {
    /**
     * Loading screen state
     */
    object Loading:DsUiState()

    /**
     * Games state
     */
    class Games(val games:List<GamesItem>) : DsUiState()

    /**
     * Retry loading
     */
    object ErrorRetry:DsUiState()
}
