package com.example.local_network_scanner.ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

/**
 * SENET Motion Specifications
 * 
 * Implements Material Design 3 motion system with physics-based animations
 * for a premium, responsive user experience.
 */
object SenetMotionSpecs {
    // Standard animations for all transitions
    val standardEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    val emphasizedEasing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
    
    // Duration specs (in milliseconds)
    object Durations {
        const val SHORT = 150
        const val MEDIUM = 300
        const val LONG = 500
        const val EXTRA_LONG = 700
    }
    
    /**
     * Shared Axis Transition - X axis (horizontal slide + fade)
     */
    fun sharedAxisTransitionX(
        initialOffsetX: (fullWidth: Int) -> Int = { it },
        targetOffsetX: (fullWidth: Int) -> Int = { -it },
        durationMillis: Int = Durations.MEDIUM
    ): ContentTransform {
        return ContentTransform(
            targetContentEnter = slideInHorizontally(
                initialOffsetX = initialOffsetX,
                animationSpec = tween(durationMillis, easing = standardEasing)
            ) + fadeIn(animationSpec = tween(durationMillis)),
            initialContentExit = slideOutHorizontally(
                targetOffsetX = targetOffsetX,
                animationSpec = tween(durationMillis, easing = standardEasing)
            ) + fadeOut(animationSpec = tween(durationMillis))
        )
    }
    
    /**
     * Shared Axis Transition - Y axis (vertical slide + fade)
     */
    fun sharedAxisTransitionY(
        durationMillis: Int = Durations.MEDIUM
    ): ContentTransform {
        return ContentTransform(
            targetContentEnter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis, easing = standardEasing)
            ) + fadeIn(animationSpec = tween(durationMillis)),
            initialContentExit = slideOutVertically(
                targetOffsetY = { -it / 2 },
                animationSpec = tween(durationMillis, easing = standardEasing)
            ) + fadeOut(animationSpec = tween(durationMillis))
        )
    }
    
    /**
     * Container Transform - Element expansion animation
     */
    fun containerTransform(
        durationMillis: Int = Durations.LONG
    ): ContentTransform {
        return ContentTransform(
            targetContentEnter = fadeIn(
                animationSpec = tween(
                    durationMillis = durationMillis,
                    delayMillis = 100,
                    easing = LinearEasing
                )
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = emphasizedEasing
                )
            ),
            initialContentExit = fadeOut(
                animationSpec = tween(durationMillis = durationMillis / 2)
            ) + scaleOut(
                targetScale = 0.9f,
                animationSpec = tween(durationMillis = durationMillis / 2)
            )
        )
    }
}

/**
 * Reusable animated container with automatic size transitions
 */
@Composable
fun AnimatedContainer(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    animationDurationMillis: Int = SenetMotionSpecs.Durations.MEDIUM
) {
    val animatedModifier = modifier
        .animateContentSize(
            animationSpec = tween(
                durationMillis = animationDurationMillis,
                easing = SenetMotionSpecs.standardEasing
            )
        )
    
    Box(modifier = animatedModifier) {
        content()
    }
}

/**
 * Spring physics for tactile interactions
 */
@Composable
fun SpringAnimatedValue(
    targetValue: Float,
    modifier: Modifier = Modifier
): Float {
    return animateFloatAsState(
        targetValue = targetValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "spring_animation"
    ).value
}
