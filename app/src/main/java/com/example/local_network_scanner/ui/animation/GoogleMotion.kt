package com.example.local_network_scanner.ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween

object GoogleMotionSpecs {
    // Duration tokens (Google standard)
    object Duration {
        const val SHORT1 = 50
        const val SHORT2 = 100
        const val SHORT3 = 150
        const val SHORT4 = 200
        const val MEDIUM1 = 250
        const val MEDIUM2 = 300
        const val MEDIUM3 = 350
        const val MEDIUM4 = 400
        const val LONG1 = 450
        const val LONG2 = 500
        const val LONG3 = 550
        const val LONG4 = 600
        const val EXTRA_LONG1 = 700
        const val EXTRA_LONG2 = 800
        const val EXTRA_LONG3 = 900
        const val EXTRA_LONG4 = 1000
    }
    
    // Easing curves (Google standard)
    val standardEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val standardAccelerate = CubicBezierEasing(0.3f, 0.0f, 1.0f, 1.0f)
    val standardDecelerate = CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)
    val emphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val emphasizedAccelerate = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
    val emphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
    
    // Shared Axis - Z (depth)
    fun sharedAxisZForward(): ContentTransform {
        return ContentTransform(
            targetContentEnter = fadeIn(
                animationSpec = tween(Duration.LONG2, easing = standardDecelerate)
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(Duration.LONG2, easing = emphasizedDecelerate)
            ),
            initialContentExit = fadeOut(
                animationSpec = tween(Duration.SHORT4, easing = standardAccelerate)
            ) + scaleOut(
                targetScale = 1.1f,
                animationSpec = tween(Duration.SHORT4, easing = emphasizedAccelerate)
            )
        )
    }
    
    fun sharedAxisZBackward(): ContentTransform {
        return ContentTransform(
            targetContentEnter = fadeIn(
                animationSpec = tween(Duration.LONG2, easing = standardDecelerate)
            ) + scaleIn(
                initialScale = 1.1f,
                animationSpec = tween(Duration.LONG2, easing = emphasizedDecelerate)
            ),
            initialContentExit = fadeOut(
                animationSpec = tween(Duration.SHORT4, easing = standardAccelerate)
            ) + scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(Duration.SHORT4, easing = emphasizedAccelerate)
            )
        )
    }
    
    // Shared Axis - X (horizontal)
    fun sharedAxisXForward(): ContentTransform {
        return ContentTransform(
            targetContentEnter = slideInHorizontally(
                initialOffsetX = { it / 3 },
                animationSpec = tween(Duration.LONG2, easing = emphasizedDecelerate)
            ) + fadeIn(animationSpec = tween(Duration.LONG2)),
            initialContentExit = slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(Duration.LONG2, easing = emphasizedAccelerate)
            ) + fadeOut(animationSpec = tween(Duration.LONG2))
        )
    }
    
    // Container Transform (card to detail)
    fun containerTransform(): ContentTransform {
        return ContentTransform(
            targetContentEnter = fadeIn(
                animationSpec = tween(Duration.LONG2, delayMillis = 60, easing = standardDecelerate)
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(Duration.LONG2, easing = emphasizedDecelerate)
            ),
            initialContentExit = fadeOut(
                animationSpec = tween(Duration.SHORT4, easing = standardAccelerate)
            ) + scaleOut(
                targetScale = 1.1f,
                animationSpec = tween(Duration.SHORT4, easing = emphasizedAccelerate)
            )
        )
    }
    
    // Fade Through (switching tabs)
    fun fadeThrough(): ContentTransform {
        return ContentTransform(
            targetContentEnter = fadeIn(
                animationSpec = tween(Duration.SHORT4, delayMillis = Duration.SHORT4)
            ) + scaleIn(
                initialScale = 0.92f,
                animationSpec = tween(Duration.SHORT4, delayMillis = Duration.SHORT4)
            ),
            initialContentExit = fadeOut(
                animationSpec = tween(Duration.SHORT4)
            ) + scaleOut(
                targetScale = 0.92f,
                animationSpec = tween(Duration.SHORT4)
            )
        )
    }
}
