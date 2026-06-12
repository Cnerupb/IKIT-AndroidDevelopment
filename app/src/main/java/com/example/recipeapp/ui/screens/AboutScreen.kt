package com.example.recipeapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Color as AndroidColor
import androidx.core.graphics.toColorInt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.recipeapp.R
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import androidx.core.graphics.createBitmap

data class Office(val name: String, val point: Point)

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
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .nestedScroll(NoOverscroll)
                .verticalScroll(rememberScrollState())
        ) {
            AboutCompanyCard()
        }

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
    
    val offices = remember {
        listOf(
            Office("Главный офис (Минск)", Point(53.9022, 27.5619)),
            Office("Филиал Центр", Point(53.9100, 27.5800)),
            Office("IT-департамент", Point(53.9200, 27.6000))
        )
    }

    // Создаем Material-точку (красный круг с белой обводкой)
    val dotMarkerProvider = remember {
        val size = 100 // Увеличили размер для удобства нажатия
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // 1. Мягкая тень под точкой
        paint.color = AndroidColor.argb(70, 0, 0, 0)
        canvas.drawCircle(size / 2f, size / 2f + 4f, size / 2.2f, paint)

        // 2. Основной красный круг (Material Red)
        paint.color = "#F44336".toColorInt()
        paint.style = Paint.Style.FILL
        canvas.drawCircle(size / 2f, size / 2f, size / 2.6f, paint)

        // 3. Белая обводка (делает точку четкой)
        paint.color = AndroidColor.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6f
        canvas.drawCircle(size / 2f, size / 2f, size / 2.6f, paint)

        ImageProvider.fromBitmap(bitmap)
    }

    val mapView = remember {
        MapView(context).apply {
            val map = mapWindow.map

            // Устанавливаем общий слушатель кликов на все объекты карты
            map.mapObjects.addTapListener { mapObject, _ ->
                val officeName = mapObject.userData as? String
                if (officeName != null) {
                    Toast.makeText(context.applicationContext, officeName, Toast.LENGTH_SHORT).show()
                }
                true // Событие обработано
            }

            // Наносим метки
            offices.forEach { office ->
                map.mapObjects.addPlacemark().apply {
                    geometry = office.point
                    setIcon(
                        dotMarkerProvider, 
                        IconStyle().apply { 
                            anchor = PointF(0.5f, 0.5f)
                            scale = 1.0f 
                        }
                    )
                    userData = office.name
                }
            }

            // Перемещаем камеру на метку
            map.move(CameraPosition(offices[0].point, 13.5f, 0f, 0f))
        }
    }

    DisposableEffect(lifecycleOwner, mapView) {
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AndroidView(
                    factory = { mapView },
                    modifier = Modifier.fillMaxSize()
                )
                
                // Кнопки масштабирования
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            val pos = mapView.mapWindow.map.cameraPosition
                            mapView.mapWindow.map.move(
                                CameraPosition(pos.target, pos.zoom + 1, pos.azimuth, pos.tilt)
                            )
                        },
                        modifier = Modifier.size(40.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Zoom In")
                    }
                    IconButton(
                        onClick = {
                            val pos = mapView.mapWindow.map.cameraPosition
                            mapView.mapWindow.map.move(
                                CameraPosition(pos.target, pos.zoom - 1, pos.azimuth, pos.tilt)
                            )
                        },
                        modifier = Modifier.size(40.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                        )
                    ) {
                        Text("-", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
        }
    }
}
