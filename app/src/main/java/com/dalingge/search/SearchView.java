package com.dalingge.search;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * 搜素伸缩动画
 * Created by dingboyang on 2016/12/24.
 */
public class SearchView extends View {

    private static final int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_TEXT_COLOR = 0xFF8A8A8A;

    private Paint mBackgroundRectPaint;
    private Paint mSearchBitmapPaint;
    private Paint mTitleTextPaint;
    private Bitmap mBitmapSearch;

    private Path mBackgroundRectPath;
    private int mWidth = 0;
    private int mHeight = 0;
    private int MIN_WIDTH = dip2px(80);
    private int MIN_HEIGHT = dip2px(30);
    private int MAX_WITH ;
    private final int MAX_BACKGROUND_ROUND_RECT_RADIUS = dip2px(30);
    private String mTitleText;
    private CharSequence shortText;
    private CharSequence longText;
    private float textSize=14;

    private int backgroundColor;
    private int shortBackgroundColor;
    private int longBackgroundColor;
    private int mBackgroundRectWidth;
    private int mBackgroundRectHeight;
    private int mBgRoundRectRadius;

    private RectF mBackgroundRectF = new RectF();


    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.searchViewAnim, 0, 0);
            shortBackgroundColor = a.getColor(R.styleable.searchViewAnim_sva_shortBackground, DEFAULT_BACKGROUND_COLOR);
            longBackgroundColor = a.getColor(R.styleable.searchViewAnim_sva_longBackground, DEFAULT_BACKGROUND_COLOR);
            int resourceIdShortText =
                    a.getResourceId(R.styleable.searchViewAnim_sva_shortText, R.string.search_short_text);
            int resourceIdLongText =
                    a.getResourceId(R.styleable.searchViewAnim_sva_longText, R.string.search_long_text);
            this.shortText = getResources().getString(resourceIdShortText);
            this.longText = getResources().getString(resourceIdLongText);
            this.textSize = a.getDimension(R.styleable.searchViewAnim_sva_textSize, textSize);
            a.recycle();
        }

        backgroundColor=shortBackgroundColor;
        mBitmapSearch = getBitmap(context, R.drawable.ic_search);

        mTitleText=shortText.toString();

        initPaint();

        mBackgroundRectPath = new Path();
        mBackgroundRectPath.reset();

        initValues();
    }
    private void initPaint(){
        mBackgroundRectPaint = new Paint();
        mBackgroundRectPaint.setColor(backgroundColor);
        mBackgroundRectPaint.setAntiAlias(true);
        mBackgroundRectPaint.setStrokeWidth(5);

        mSearchBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSearchBitmapPaint.setFilterBitmap(true);
        mSearchBitmapPaint.setDither(true);

        mTitleTextPaint = new Paint();
        mTitleTextPaint.setColor(DEFAULT_TEXT_COLOR);
        mTitleTextPaint.setAntiAlias(true);
        mTitleTextPaint.setStyle(Paint.Style.FILL);
        mTitleTextPaint.setTextSize(sp2px(textSize));

    }


    private void initValues() {
        mBackgroundRectWidth = MIN_WIDTH;
        mBackgroundRectHeight = MIN_HEIGHT;
        mBgRoundRectRadius = MAX_BACKGROUND_ROUND_RECT_RADIUS;
    }


    private static Bitmap getBitmap(Context context, int vectorDrawableId) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result;
        int widthMode = MeasureSpec.getMode(measureSpec);
        int widthSize = MeasureSpec.getSize(measureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            result = widthSize;
            MAX_WITH=widthSize;
        } else {
            result = MIN_WIDTH;
            if (widthMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, widthSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result;
        int heightMode = MeasureSpec.getMode(measureSpec);
        int heightSize = MeasureSpec.getSize(measureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            result = heightSize;
        } else {
            result = MIN_HEIGHT;
            if (heightMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, heightSize);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int rectLeft = mWidth - mBackgroundRectWidth;
        int rectRight = mWidth;
        mBackgroundRectF.set(rectLeft, 0, rectRight, mBackgroundRectHeight);
        mBackgroundRectPaint.setColor(backgroundColor);
        canvas.drawRoundRect(mBackgroundRectF, mBgRoundRectRadius, mBgRoundRectRadius, mBackgroundRectPaint);
        canvas.drawBitmap(mBitmapSearch, rectLeft, (mHeight - mBitmapSearch.getHeight()) / 2, mSearchBitmapPaint);
        Paint.FontMetricsInt fm = mTitleTextPaint.getFontMetricsInt();
        float startY = mHeight / 2 - fm.descent + (fm.bottom - fm.top) / 2;
        canvas.drawText(mTitleText, rectLeft + mBitmapSearch.getWidth(), startY, mTitleTextPaint);
    }

    private boolean isAnim = true;

    public void startAnim() {
        if (!isAnim) return;
        AnimatorSet backgroundAnimSet = new AnimatorSet();
        ValueAnimator widthAnim = ValueAnimator.ofInt(MIN_WIDTH, MAX_WITH);
        widthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBackgroundRectWidth = (int) animation.getAnimatedValue();
                mTitleText = longText.toString();
                backgroundColor=longBackgroundColor;
                invalidate();
                isAnim = false;
            }
        });
        ValueAnimator alphaAnim = ValueAnimator.ofFloat(0f,1f);
        backgroundAnimSet.playTogether(widthAnim,alphaAnim);
        backgroundAnimSet.setDuration(500);
        backgroundAnimSet.start();
    }

    public void resetAnim() {
        if (isAnim) return;
        AnimatorSet backgroundAnimSet = new AnimatorSet();
        ValueAnimator widthAnim = ValueAnimator.ofInt(MAX_WITH, MIN_WIDTH);
        widthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBackgroundRectWidth = (int) animation.getAnimatedValue();
                invalidate();
                isAnim = true;
            }
        });
        backgroundAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTitleText = shortText.toString();
                backgroundColor=shortBackgroundColor;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        backgroundAnimSet.playTogether(widthAnim);
        backgroundAnimSet.setDuration(500);
        backgroundAnimSet.start();
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
