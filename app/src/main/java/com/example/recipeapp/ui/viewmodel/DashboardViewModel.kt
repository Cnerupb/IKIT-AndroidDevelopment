package com.example.recipeapp.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.WeatherData
import com.example.recipeapp.data.WeatherRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.suspendCancellableCoroutine

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherData) : WeatherUiState()
    object Error : WeatherUiState()
}

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepository()
    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    @SuppressLint("MissingPermission")
    fun loadWeather() {
        viewModelScope.launch {
            _weatherState.value = WeatherUiState.Loading
            try {
                val location = getLastLocationOrCurrent()
                val lat = location?.latitude ?: 53.9
                val lon = location?.longitude ?: 27.56
                val city = if (location != null) "Моё местоположение" else "Минск"
                Log.d(TAG, "Using coords: lat=$lat, lon=$lon, city=$city")
                _weatherState.value = WeatherUiState.Success(repository.getWeather(lat, lon, city))
            } catch (e: Exception) {
                Log.e(TAG, "loadWeather failed: ${e::class.simpleName}: ${e.message}", e)
                _weatherState.value = WeatherUiState.Error
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocationOrCurrent(): android.location.Location? {
        Log.d(TAG, "Trying lastLocation...")
        val last = suspendCoroutine { cont ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc ->
                    Log.d(TAG, "lastLocation result: $loc")
                    cont.resume(loc)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "lastLocation failed: ${e.message}")
                    cont.resume(null)
                }
        }
        if (last != null) return last

        Log.d(TAG, "lastLocation is null, trying getCurrentLocation...")
        val cts = CancellationTokenSource()
        return withTimeoutOrNull(10_000L) {
            suspendCancellableCoroutine { cont ->
                fusedLocationClient
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                    .addOnSuccessListener { loc ->
                        Log.d(TAG, "getCurrentLocation result: $loc")
                        cont.resume(loc)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "getCurrentLocation failed: ${e.message}")
                        cont.resume(null)
                    }
                cont.invokeOnCancellation {
                    Log.w(TAG, "getCurrentLocation cancelled (timeout)")
                    cts.cancel()
                }
            }
        }
    }

    companion object {
        private const val TAG = "DashboardViewModel"
    }
}
