package devit951.github.magictip

import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference

open class MagicTip(private val anchorView: View) {

    private val weakAnchorView = WeakReference<View>(anchorView)
    private val padding = dp2Px(8)
    private var onMagicTipView: (MagicTipView.() -> Unit)? = null

    internal val decorView: ViewGroup
    internal val magicTipView: MagicTipView

    init {
        val activity = weakAnchorView.get()!!.activity()
        decorView = activity.window.decorView as ViewGroup
        magicTipView = MagicTipView(activity)
    }

    fun settings(magicTipView: MagicTipView.() -> Unit): MagicTip{
        onMagicTipView = magicTipView
        return this
    }

    open fun show(){
        decorView.addView(magicTipView.apply {
            visibility = View.INVISIBLE
            onMagicTipView?.invoke(this)
            anchorView.post{
                val location = intArrayOf(0, 0)
                anchorView.getLocationOnScreen(location)
                post {
                    x = location[0].toFloat() + ((anchorView.width - width) / 2f)
                    y = location[1].toFloat() - height - padding
                    visibility = View.VISIBLE
                    startEnterAnimation()
                }
            }
            setOnClickListener {
                clickDelegate.onClick(this, decorView)
            }
        })
    }
}