package shimmer.developer.dw.shimmer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * @author WeiDeng
 * @date 16/8/25
 * @description
 */
public class ShimmerView extends TextView {

    private static final int ANIM_TIME = 1500;
    private static final int ANIM_INTERVAL = 1000;
    private static final int[] colors = new int[3];
    private static final float[] positions = new float[]{
            .0f, .5f, 0.9f
    };

    private LinearGradient mLinearGradient;
    private Scroller mScroller;
    private int mTransX;
    private Matrix mMatrix;

    private Runnable r1 = new Runnable() {
        @Override
        public void run() {
            mScroller.startScroll(-getWidth(), 0, getWidth() * 2, 0, ANIM_TIME);
            post(r2);
        }
    };

    private Runnable r2 = new Runnable() {
        @Override
        public void run() {
            if(mScroller.computeScrollOffset()) {
                mTransX = mScroller.getCurrX();
                invalidate();
                post(this);
            }else {
                postDelayed(r1, ANIM_INTERVAL);
            }
        }
    };

    public ShimmerView(Context context) {
        this(context, null);
    }

    public ShimmerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShimmerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        internalInit(context, attrs);
    }

    private void internalInit(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShimmerView);
            int tvColor = typedArray.getColor(R.styleable.ShimmerView_shimmer_text_color, getResources().getColor(R.color.default_color));
            int shadeColor = typedArray.getColor(R.styleable.ShimmerView_shimmer_color, Color.WHITE);
            colors[0] = tvColor;
            colors[1] = shadeColor;
            colors[2] = tvColor;
            typedArray.recycle();
        }
        mScroller = new Scroller(context, new AccelerateInterpolator());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if(mLinearGradient == null) {
            mLinearGradient = new LinearGradient(0, 0, getWidth(), getHeight(), colors, positions, Shader.TileMode.CLAMP);
            getPaint().setShader(mLinearGradient);
            mTransX = -getWidth();
            mMatrix = new Matrix();
            post(r1);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mMatrix.setTranslate(mTransX, 0);
        mLinearGradient.setLocalMatrix(mMatrix);
        super.onDraw(canvas);
    }
}
