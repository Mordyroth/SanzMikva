package com.example.android.miveh2.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;

import com.example.android.miveh2.R;

public class NewTextView  extends AppCompatTextView {

    public NewTextView(Context context) {
        super(context);
    }

    public NewTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NewTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        if (attrs != null) {

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.textViewStyle);


            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final Paint paint = getPaint();
        final int color = paint.getColor();
        // Draw what you have to in transparent
        // This has to be drawn, otherwise getting values from layout throws exceptions
        setTextColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        // setTextColor invalidates the view and causes an endless cycle
        paint.setColor(color);

        System.out.println("Drawing text info:");

        Layout layout = getLayout();
        String text = getText().toString();

        for (int i = 0; i < layout.getLineCount(); i++) {
            final int start = layout.getLineStart(i);
            final int end = layout.getLineEnd(i);

            String line = text.substring(start, end);

            System.out.println("Line:\t" + line);

            final float left = layout.getLineLeft(i);
            final int baseLine = layout.getLineBaseline(i);

            canvas.drawText(line,
                    left + getTotalPaddingLeft(),
                    // The text will not be clipped anymore
                    // You can add a padding here too, faster than string string concatenation
                    baseLine + getTotalPaddingTop(),
                    getPaint());
        }
    }

}
