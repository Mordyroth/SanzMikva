package com.example.android.miveh2.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.widget.TextView;


public class CustomTextView extends TextView {
    private Bitmap _bitmap;
    private NonClippableCanvas _canvas;

    public CustomTextView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(final int width, final int height,
                             final int oldwidth, final int oldheight) {
        if (width != oldwidth || height != oldheight) {
            _bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            _canvas = new NonClippableCanvas(_bitmap);
        }

        super.onSizeChanged(width, height, oldwidth, oldheight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        super.onDraw(_canvas);

        canvas.drawBitmap(_bitmap, 0, 0, null);
    }
    class NonClippableCanvas extends Canvas {

        public NonClippableCanvas(@NonNull Bitmap bitmap) {
            super(bitmap);
        }

        @Override
        public boolean clipRect(float left, float top, float right, float bottom) {
            return true;
        }
    }
}

