package memebattle.aayushf.memebattle

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by aayushf on 3/11/17.
 */
class FontedTextView(c: Context, a: AttributeSet) : TextView(c, a) {
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


}