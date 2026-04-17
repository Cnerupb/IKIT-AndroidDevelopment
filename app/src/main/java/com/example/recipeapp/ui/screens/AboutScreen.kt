package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

// Поглощает остаточный скролл, не давая overscroll-эффекту срабатывать
private val NoOverscroll = object : NestedScrollConnection {
    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = available
}

@Composable
fun AboutScreen(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Текстовый блок прокручивается независимо.
        // weight(1f, fill = false) даёт ему ровно столько высоты, сколько нужно
        // контенту, но не более оставшегося места — тогда карта всегда видна снизу.
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .nestedScroll(NoOverscroll)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AboutCompanyCard()
        }

        // Карта находится вне scrollable-контейнера — жесты ей принадлежат целиком.
        OfficesCard()
    }
}

@Composable
private fun AboutCompanyCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "О нашей компании",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Мы — команда энтузиастов кулинарного искусства, создавшая приложение " +
                        "«Книга Рецептов» для всех любителей готовить. Наша миссия — сделать " +
                        "процесс готовки проще и приятнее, предоставляя удобный инструмент для " +
                        "хранения и организации ваших любимых рецептов.\n\n" +
                        "Приложение разработано в 2026 году и включает все необходимые функции: " +
                        "от создания собственных рецептов до расчёта КБЖУ. Мы постоянно работаем " +
                        "над улучшением функционала и добавлением новых возможностей.\n\n" +
                        "Наша команда состоит из разработчиков, дизайнеров и профессиональных " +
                        "поваров, которые вместе создают лучший опыт для наших пользователей.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun OfficesCard() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val officePoint = remember { Point(53.9, 27.56) }

    val mapView = remember {
        MapView(context).apply {
            mapWindow.map.move(CameraPosition(officePoint, 13f, 0f, 0f))
            mapWindow.map.mapObjects.addPlacemark().apply {
                geometry = officePoint
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                MapKitFactory.getInstance().onStart()
                mapView.onStart()
            }
            override fun onStop(owner: LifecycleOwner) {
                mapView.onStop()
                MapKitFactory.getInstance().onStop()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            MapKitFactory.getInstance().onStart()
            mapView.onStart()
        }
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Наши офисы",
                style = MaterialTheme.typography.titleMedium
            )
            AndroidView(
                factory = { mapView },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}
