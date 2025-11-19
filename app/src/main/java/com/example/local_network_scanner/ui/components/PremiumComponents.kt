package com.example.local_network_scanner.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.local_network_scanner.ui.theme.SenetColors

/**
 * Premium Card Component
 * 
 * Elevated card with subtle shadows and premium feel
 */
@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = SenetColors.NavyDark.copy(alpha = 0.25f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick ?: {}
    ) {
        content()
    }
}

/**
 * Premium Button Component
 * 
 * Button with navy blue base and loading state support
 */
@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SenetColors.NavyBase,
            contentColor = SenetColors.WhitePure,
            disabledContainerColor = SenetColors.MediumGray,
            disabledContentColor = SenetColors.LightGray
        ),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = SenetColors.WhitePure,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Premium FAB Component
 * 
 * Animated floating action button with navy blue base
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumFAB(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    isExpanded: Boolean = false,
    expandedText: String = "Action"
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = SenetColors.NavyBase,
        contentColor = SenetColors.WhitePure,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 12.dp,
            pressedElevation = 16.dp,
            hoveredElevation = 14.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = contentDescription)
            if (isExpanded) {
                Text(expandedText, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

/**
 * Premium Bottom Sheet Component
 * 
 * Smooth bottom sheet with rounded corners
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = SenetColors.Black.copy(alpha = 0.32f),
            sheetState = rememberModalBottomSheetState()
        ) {
            content()
        }
    }
}

/**
 * Navy Ripple Button Component
 * 
 * Button with enhanced ripple effects
 */
@Composable
fun NavyRippleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = SenetColors.NavyBase,
            contentColor = SenetColors.WhitePure,
            disabledContainerColor = SenetColors.MediumGray,
            disabledContentColor = SenetColors.LightGray
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp,
            hoveredElevation = 10.dp
        )
    ) {
        content()
    }
}

/**
 * Morphing Icon Component
 * 
 * Animates between two icons
 */
@Composable
fun MorphingIcon(
    sourceIcon: ImageVector,
    targetIcon: ImageVector,
    isTransformed: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedAlpha = animateFloatAsState(
        targetValue = if (isTransformed) 1f else 0f,
        animationSpec = tween(150),
        label = "icon_morph"
    ).value
    
    Box(modifier = modifier) {
        Icon(
            imageVector = sourceIcon,
            contentDescription = null,
            modifier = Modifier.alpha(1 - animatedAlpha),
            tint = SenetColors.NavyLight
        )
        Icon(
            imageVector = targetIcon,
            contentDescription = null,
            modifier = Modifier.alpha(animatedAlpha),
            tint = SenetColors.NavyLight
        )
    }
}

/**
 * Confirmation Ripple Component
 * 
 * Animated ripple effect for confirmations
 */
@Composable
fun ConfirmationRipple(
    isConfirmed: Boolean,
    modifier: Modifier = Modifier
) {
    if (isConfirmed) {
        val infiniteTransition = rememberInfiniteTransition(label = "confirmation_ripple")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 3f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "ripple_scale"
        )
        val alpha by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(600),
                repeatMode = RepeatMode.Restart
            ),
            label = "ripple_alpha"
        )
        
        Canvas(modifier = modifier.size(48.dp)) {
            drawCircle(
                color = SenetColors.NavyLight.copy(alpha = alpha),
                radius = 24.dp.toPx() * scale
            )
        }
    }
}

/**
 * SENET Logo Component
 * 
 * Displays the SENET branding logo at various sizes
 */
@Composable
fun SenetLogo(size: Dp = 120.dp) {
    // Note: Using placeholder since actual logo file doesn't exist yet
    // Replace R.drawable.ic_senet_logo with actual logo resource when available
    Box(
        modifier = Modifier
            .size(size)
            .shadow(8.dp, RoundedCornerShape(size / 2)),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder circle for logo
        Surface(
            modifier = Modifier.size(size),
            shape = RoundedCornerShape(size / 2),
            color = SenetColors.NavyBase
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "S",
                    style = MaterialTheme.typography.displayLarge,
                    color = SenetColors.WhitePure,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
