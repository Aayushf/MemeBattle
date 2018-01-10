package memebattle.aayushf.memebattle

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView

/**
 * Created by aayushf on 3/11/17.
 */
open class FontedOutlineTextView(c: Context, a: AttributeSet) : IconicsTextView(c, a) {
    var fontStyle = ""

    init {
        val a: TypedArray = c.theme.obtainStyledAttributes(a, R.styleable.FontedTextView, 0, 0)
        try {
            fontStyle = a.getString(R.styleable.FontedTextView_textfont)

        } finally {
            a.recycle()
            val tf = Typeface.createFromAsset(context.assets, "font/OpenSans-$fontStyle.ttf")
            setTypeface(tf, 1)
        }


    }

    override fun onDraw(canvas: Canvas?) {
        paint.style = Paint.Style.FILL
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4.5f
        setTextColor(Color.BLACK)
        super.onDraw(canvas)
        setTextColor(Color.WHITE)
    }


}