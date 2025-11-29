package org.techcult.scaleos.feature.settings.presentation.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.ktor.client.request.invoke
import org.techcult.scaleos.core.utils.DeviceConfiguration

@Composable
fun SettingsHomeScreen(onNavigate: (String) -> Unit) {
    SettingsHomeContent(onNavigate = onNavigate)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsHomeContent(
    onNavigate: (String) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    Scaffold(topBar = {

    }, containerColor = Color(0xFFF9FAFB)
    ) { padding ->

        if (deviceConfiguration == DeviceConfiguration.DESKTOP || deviceConfiguration == DeviceConfiguration.TABLET_LANDSCAPE) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp
            ) {
                items(settingsCategories) { category ->
                    SettingsCard(category = category, onNavigate = onNavigate)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier,verticalArrangement = Arrangement.spacedBy(16.dp),contentPadding = PaddingValues(16.dp)
            ) {
                items(settingsCategories) { category ->
                    SettingsCard(category = category, onNavigate = onNavigate)
                }
            }

        }
    }
}


@Composable
fun SettingsCard(
    category: SettingsCategory,
    onNavigate: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 2.dp,
        color = Color.White,



    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SettingsHeader(
                icon = category.icon,
                title = category.title,
                subtitle = category.subtitle
            )
            Spacer(modifier = Modifier.size(16.dp))

            category.items.forEach { item ->
                SettingsItem(item = item, onClick = { onNavigate(item.route) })
            }
        }
    }
}


@Composable
fun SettingsHeader(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEFF6FF))
                .padding(8.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(16.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
fun SettingsItem(item: SettingsItemData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(all = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = item.title, style = MaterialTheme.typography.bodyMedium)
                item.tag?.let {
                    Spacer(modifier = Modifier.size(8.dp))
                    SettingsTagPill(tag = it)
                }
            }
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun SettingsTagPill(tag: SettingsTag) {
    val (backgroundColor, textColor) = when (tag) {
        SettingsTag.REQUIRED -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.error
        SettingsTag.NEW -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimary
    }
    Text(
        text = tag.text,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        color = textColor,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium
    )
}
