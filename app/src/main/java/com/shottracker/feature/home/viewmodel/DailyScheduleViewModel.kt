package com.shottracker.feature.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shottracker.feature.home.data.DailyScheduleRepository
import com.shottracker.feature.home.ui.states.DatePickerState
import com.shottracker.feature.home.ui.states.DsUiState
import com.shottracker.network.Result
import com.shottracker.utils.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DailyScheduleViewModel @Inject constructor(
    private val dailyScheduleRepository: DailyScheduleRepository
) : ViewModel() {

    /**
     * Manage screen state
     */
    private val _dsUiState: LiveEvent<DsUiState> = LiveEvent()
    val dsUiState: LiveData<DsUiState> = _dsUiState

    /**
     * Manage state of date picker
     */
    private val _datePickerState: LiveEvent<DatePickerState> = LiveEvent()
    val datePickerState: LiveData<DatePickerState> = _datePickerState

    /**
     * Hold current date picked
     */
    private val _datePicked: LiveEvent<LocalDate> = LiveEvent()
    val datePicked: LiveData<LocalDate> = _datePicked

    init {
        _dsUiState.addSource(_datePicked) {
            fetchGames(it)
        }
        dispatchDatePicked(DEFAULT_DATE)
    }

    private fun fetchGames(date: LocalDate) {
        dispatchDsUiState(DsUiState.Loading)
        viewModelScope.launch {
            when (val result = dailyScheduleRepository.getGamesForDate(date)) {
                is Result.Success -> result.data?.games?.let {
                    dispatchDsUiState(DsUiState.Games(it.filterNotNull()))
                }
                is Result.Failure -> {
                    dispatchDsUiState(DsUiState.ErrorRetry)
                    //TODO: Add Error logs
                }
                else -> {

                }
            }
        }
    }

    private fun dispatchDsUiState(state: DsUiState) {
        _dsUiState.postValue(state)
    }

    fun dispatchDatePicked(date:LocalDate) {
        _datePicked.postValue(date)
    }

    fun dispatchDatePickerState(state: DatePickerState) {
        _datePickerState.postValue(state)
    }

    companion object {
        val DEFAULT_DATE: LocalDate
            get() {
                return LocalDate.now()
            }
    }
}