package com.github.willjgriff.playground.ethereum.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.will.Playground.R;

/**
 * Created by Will on 26/04/2016.
 */
public class LabelTextView extends LinearLayout {

    private TextView mLabel;
    private TextView mDescription;
    private Paint mPaint;

    int mWidth;
    int mHeight;

    public LabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttrs(context, attrs);
        setupPaint(context);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCheckers(canvas, 6, 40);

        Drawable backgroundGradient = ContextCompat.getDrawable(getContext(), R.drawable.label_text_view_gradient);
        backgroundGradient.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        backgroundGradient.draw(canvas);
    }

    // I'm sure I could create this shape and set it as a background drawable, but where's the fun in that...
    // Was suppose to look like blocks, it sort of does :)
    private void drawCheckers(Canvas canvas, int height, int width) {
        int checkerHeight = getMeasuredHeight() / height;
        int checkerWidth = getMeasuredWidth() / width;
        int checkerTop;
        int checkerLeft;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j+=2) {
                checkerTop = checkerHeight * i;
                checkerLeft = i % 2 == 0 ? checkerWidth * j : (checkerWidth * j) + checkerWidth;
                canvas.drawRect(checkerLeft, checkerTop, checkerLeft + checkerWidth, checkerTop + checkerHeight, mPaint);
            }
        }
    }

    private void setupPaint(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(context, R.color.accent_alpha26));
    }

    private void setupAttrs(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.view_label_text, this);
        mLabel = (TextView) findViewById(R.id.view_label_text_label);
        mDescription = (TextView) findViewById(R.id.view_label_text_description);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView, 0, 0);
        try {
            String labelText = typedArray.getString(R.styleable.LabelTextView_LabelTextLabel);
            mLabel.setText(labelText);
            String descriptionText = typedArray.getString(R.styleable.LabelTextView_LabelTextDescription);
            mDescription.setText(descriptionText);
        } finally {
            typedArray.recycle();
        }

        int padding = getResources().getDimensionPixelSize(R.dimen.xxsmall);
        setPadding(padding, padding, padding, padding);
        setBackground(ContextCompat.getDrawable(context, R.drawable.label_text_view_gradient));
    }

    public void setLabel(String label) {
        mLabel.setText(label);
        invalidate();
        requestLayout();
    }

    public void setDescription(String description) {
        mDescription.setText(description);
        invalidate();
        requestLayout();
    }
}