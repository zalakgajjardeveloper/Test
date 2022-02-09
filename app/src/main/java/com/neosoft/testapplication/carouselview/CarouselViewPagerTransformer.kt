package com.neosoft.testapplication.carouselview

import android.view.View
import androidx.annotation.IntDef
import androidx.viewpager.widget.ViewPager
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/*
 * ViewPager transformation animation invoked when a visible/attached page is scrolled - before
 * changing this, first see https://code.google.com/p/android/issues/detail?id=58918#c5
 * tl;dr make sure to remove X translation when a page is no longer fully visible
 *
 * Usage: viewPager.setPageTransformer(false, new ReaderViewPagerTransformer(TransformType.FLOW));
 */
class CarouselViewPagerTransformer internal constructor(private val mTransformType: Int) :
    ViewPager.PageTransformer {
    /** @hide
     */
    @IntDef(FLOW, SLIDE_OVER, DEPTH, ZOOM, DEFAULT)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Transformer

    override fun transformPage(page: View, position: Float) {
        val alpha: Float
        val scale: Float
        val translationX: Float
        when (mTransformType) {
            FLOW -> {
                page.rotationY = position * -30f
                return
            }
            SLIDE_OVER -> if (position < 0 && position > -1) {
                // this is the page to the left
                scale =
                    Math.abs(Math.abs(position) - 1) * (1.0f - SCALE_FACTOR_SLIDE) + SCALE_FACTOR_SLIDE
                alpha = Math.max(MIN_ALPHA_SLIDE, 1 - Math.abs(position))
                val pageWidth = page.width
                val translateValue = position * -pageWidth
                translationX = if (translateValue > -pageWidth) {
                    translateValue
                } else {
                    0f
                }
            } else {
                alpha = 1f
                scale = 1f
                translationX = 0f
            }
            DEPTH -> if (position > 0 && position < 1) {
                // moving to the right
                alpha = 1 - position
                scale = MIN_SCALE_DEPTH + (1 - MIN_SCALE_DEPTH) * (1 - Math.abs(position))
                translationX = page.width * -position
            } else {
                // use default for all other cases
                alpha = 1f
                scale = 1f
                translationX = 0f
            }
            ZOOM -> if (position >= -1 && position <= 1) {
                scale = Math.max(MIN_SCALE_ZOOM, 1 - Math.abs(position))
                alpha = MIN_ALPHA_ZOOM +
                        (scale - MIN_SCALE_ZOOM) / (1 - MIN_SCALE_ZOOM) * (1 - MIN_ALPHA_ZOOM)
                val vMargin = page.height * (1 - scale) / 2
                val hMargin = page.width * (1 - scale) / 2
                translationX = if (position < 0) {
                    hMargin - vMargin / 2
                } else {
                    -hMargin + vMargin / 2
                }
            } else {
                alpha = 1f
                scale = 1f
                translationX = 0f
            }
            else -> return
        }
        page.alpha = alpha
        page.translationX = translationX
        page.scaleX = scale
        page.scaleY = scale
    }

    companion object {
        const val FLOW = 0
        const val SLIDE_OVER = 1
        const val DEPTH = 2
        const val ZOOM = 3
        const val DEFAULT = -1
        private const val MIN_SCALE_DEPTH = 0.75f
        private const val MIN_SCALE_ZOOM = 0.85f
        private const val MIN_ALPHA_ZOOM = 0.5f
        private const val SCALE_FACTOR_SLIDE = 0.85f
        private const val MIN_ALPHA_SLIDE = 0.35f
    }
}