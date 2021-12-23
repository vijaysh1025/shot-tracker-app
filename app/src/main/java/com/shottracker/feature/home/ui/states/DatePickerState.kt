package com.shottracker.feature.home.ui.states

sealed class DatePickerState {
    object Hidden:DatePickerState()
    object Collapsed:DatePickerState()
    object Expanded:DatePickerState()
}