package com.neosoft.testapplication.carouselview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager

class CarouselViewPager : ViewPager {
    private var imageClickListener: ImageClickListener? = null
    private var oldX = 0f
    private var newX = 0f
    private val sens = 5f
    fun setImageClickListener(imageClickListener: ImageClickListener?) {
        this.imageClickListener = imageClickListener
    }

    constructor(context: Context?) : super(context!!) {
        postInitViewPager()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        postInitViewPager()
    }

    private var mScroller: CarouselViewPagerScroller? = null

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private fun postInitViewPager() {
        try {
            val viewpager: Class<*> = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val interpolator = viewpager.getDeclaredField("sInterpolator")
            interpolator.isAccessible = true
            mScroller = CarouselViewPagerScroller(
                context,
                interpolator[null] as Interpolator
            )
            scroller[this] = mScroller
        } catch (e: Exception) {
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    fun setTransitionVelocity(scrollFactor: Int) {
        mScroller!!.setmScrollDuration(scrollFactor)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> oldX = ev.x
            MotionEvent.ACTION_UP -> {
                newX = ev.x
                if (Math.abs(oldX - newX) < sens) {
                    if (imageClickListener != null) imageClickListener!!.onClick(currentItem)
                    return true
                }
                oldX = 0f
                newX = 0f
            }
        }
        return super.onTouchEvent(ev)
    }
}