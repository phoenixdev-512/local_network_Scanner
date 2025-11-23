package com.example.local_network_scanner.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.local_network_scanner.ui.animation.GoogleMotionSpecs
import com.example.local_network_scanner.ui.feedback.HapticFeedbackManager
import com.example.local_network_scanner.ui.feedback.SoundFeedbackManager

@Composable
fun GoogleCard(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    } else {
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun GoogleFAB(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    text: String? = null
) {
    val animatedModifier = modifier.animateContentSize(
        animationSpec = tween(
            durationMillis = GoogleMotionSpecs.Duration.MEDIUM2,
            easing = GoogleMotionSpecs.standardEasing
        )
    )
    
    if (expanded && text != null) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            icon = { Icon(icon, contentDescription) },
            text = { Text(text) },
            modifier = animatedModifier
        )
    } else {
        FloatingActionButton(
            onClick = onClick,
            modifier = animatedModifier
        ) {
            Icon(icon, contentDescription)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleTopAppBar(
    title: String,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(navigationIcon, contentDescription = "Navigate back")
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Composable
fun GoogleBottomNavigation(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    items: List<BottomNavItem>
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

data class BottomNavItem(
    val icon: ImageVector,
    val label: String
)

@Composable
fun GoogleSnackbar(
    message: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    Snackbar(
        action = {
            if (actionLabel != null && onActionClick != null) {
                TextButton(onClick = onActionClick) {
                    Text(actionLabel)
                }
            }
        },
        dismissAction = {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Dismiss")
            }
        }
    ) {
        Text(message)
    }
}

@Composable
fun GoogleStyleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    soundManager: SoundFeedbackManager? = null,
    hapticManager: HapticFeedbackManager? = null
) {
    Button(
        onClick = {
            soundManager?.playTap()
            hapticManager?.lightTap()
            onClick()
        },
        modifier = modifier,
        colors = ButtonDefaults.filledTonalButtonColors()
    ) {
        Text(text)
    }
}

@Composable
fun GoogleStyleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    soundManager: SoundFeedbackManager? = null,
    hapticManager: HapticFeedbackManager? = null
) {
    Switch(
        checked = checked,
        onCheckedChange = { newValue ->
            soundManager?.playToggle(newValue)
            hapticManager?.mediumTap()
            onCheckedChange(newValue)
        },
        modifier = modifier
    )
}
