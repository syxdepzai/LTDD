package com.example.bt6.decoration;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LinePagerIndicatorDecoration extends RecyclerView.ItemDecoration {
    private int colorActive = 0xFFFF6347;
    private int colorInactive = 0xFFBBBBBB;

    private static final float DP = Resources.getSystem().getDisplayMetrics().density;

    private static final float INDICATOR_HEIGHT = (DP * 4);
    private static final float INDICATOR_STROKE_WIDTH = DP * 2;
    private static final float INDICATOR_ITEM_LENGTH = DP * 16;
    private static final float INDICATOR_ITEM_PADDING = DP * 4;

    private final Paint mPaint = new Paint();
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public LinePagerIndicatorDecoration() {
        mPaint.setStrokeWidth(INDICATOR_STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int itemCount = parent.getAdapter().getItemCount();

        float totalLength = INDICATOR_ITEM_LENGTH * itemCount;
        float paddingBetweenItems = Math.max(0, itemCount - 1) * INDICATOR_ITEM_PADDING;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

        float indicatorPosY = parent.getHeight() - INDICATOR_HEIGHT / 2F;

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);

        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        final View activeChild = layoutManager.findViewByPosition(activePosition);
        int left = activeChild.getLeft();
        int width = activeChild.getWidth();

        float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);
    }

    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        mPaint.setColor(colorInactive);

        final float itemWidth = INDICATOR_ITEM_LENGTH + INDICATOR_ITEM_PADDING;

        float start = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {
            c.drawLine(start, indicatorPosY, start + INDICATOR_ITEM_LENGTH, indicatorPosY, mPaint);
            start += itemWidth;
        }
    }

    private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY,
                                int highlightPosition, float progress, int itemCount) {
        mPaint.setColor(colorActive);

        final float itemWidth = INDICATOR_ITEM_LENGTH + INDICATOR_ITEM_PADDING;

        if (progress == 0F) {
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            c.drawLine(highlightStart, indicatorPosY,
                    highlightStart + INDICATOR_ITEM_LENGTH, indicatorPosY, mPaint);
        } else {
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            float partialLength = INDICATOR_ITEM_LENGTH * progress;

            c.drawLine(highlightStart + partialLength, indicatorPosY,
                    highlightStart + INDICATOR_ITEM_LENGTH, indicatorPosY, mPaint);

            if (highlightPosition < itemCount - 1) {
                highlightStart += itemWidth;
                c.drawLine(highlightStart, indicatorPosY,
                        highlightStart + partialLength, indicatorPosY, mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = (int) INDICATOR_HEIGHT;
    }
}
