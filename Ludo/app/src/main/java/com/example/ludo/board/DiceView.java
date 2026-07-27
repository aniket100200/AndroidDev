package com.example.ludo.board;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import java.util.Random;

public class DiceView extends View {

    private Paint bgPaint;
    private Paint dotPaint;
    private Paint borderPaint;
    private RectF rectF;
    private int currentValue = 6;
    private boolean isRolling = false;
    private Random random;

    public DiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        random = new Random();
        rectF = new RectF();

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.WHITE);
        bgPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(Color.parseColor("#B32722")); // action_red_dark
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8f);

        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setColor(Color.parseColor("#4A4A4A")); // game_text_dark
        dotPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int padding = 10;

        rectF.set(padding, padding, width - padding, height - padding);
        float cornerRadius = width * 0.2f;

        // Draw dice background and border
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, bgPaint);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, borderPaint);

        drawDots(canvas, width, height);
    }

    private void drawDots(Canvas canvas, int width, int height) {
        float cx = width / 2f;
        float cy = height / 2f;
        float offset = width * 0.25f;
        float radius = width * 0.08f;

        // Center dot (1, 3, 5)
        if (currentValue == 1 || currentValue == 3 || currentValue == 5) {
            canvas.drawCircle(cx, cy, radius, dotPaint);
        }
        // Top-left and Bottom-right dots (2, 3, 4, 5, 6)
        if (currentValue >= 2) {
            canvas.drawCircle(cx - offset, cy - offset, radius, dotPaint);
            canvas.drawCircle(cx + offset, cy + offset, radius, dotPaint);
        }
        // Top-right and Bottom-left dots (4, 5, 6)
        if (currentValue >= 4) {
            canvas.drawCircle(cx + offset, cy - offset, radius, dotPaint);
            canvas.drawCircle(cx - offset, cy + offset, radius, dotPaint);
        }
        // Middle-left and Middle-right dots (6)
        if (currentValue == 6) {
            canvas.drawCircle(cx - offset, cy, radius, dotPaint);
            canvas.drawCircle(cx + offset, cy, radius, dotPaint);
        }
    }

    public void rollDice(OnRollCompleteListener listener) {
        if (isRolling) return;
        isRolling = true;

        // 1. Rapidly change the value to simulate rolling
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 10);
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(animation -> {
            currentValue = random.nextInt(6) + 1;
            invalidate();
        });

        // 2. Physical Rotation and Bounce Animation
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        rotateAnim.setDuration(600);
        rotateAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.2f, 1f);
        scaleUpX.setDuration(600);
        scaleUpY.setDuration(600);
        scaleUpX.setInterpolator(new OvershootInterpolator());
        scaleUpY.setInterpolator(new OvershootInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimator, rotateAnim, scaleUpX, scaleUpY);
        animatorSet.start();

        // Finish callback
        postDelayed(() -> {
            isRolling = false;
            // The final result
            currentValue = random.nextInt(6) + 1;
            invalidate();
            if (listener != null) listener.onRollComplete(currentValue);
        }, 600);
    }

    public interface OnRollCompleteListener {
        void onRollComplete(int result);
    }
}