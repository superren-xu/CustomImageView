package qianfeng.com.customimaviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class CustomImagView extends ImageView {

    private int shape;
    private static final int CIRCLE = 1;
    private static final int ROUNDRECT = 2;
    private static final int RECTANGLE = 3;
    private static final int OVAL = 4;
    private static final int RHOMBUS = 5;
    private static final int HEXAGON = 6;
    private Paint mPaint;

    public CustomImagView(Context context) {
        this(context, null);
    }

    public CustomImagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取设置的shape的值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImagView);
        shape = typedArray.getInt(R.styleable.CustomImagView_shape, CIRCLE);
        //资源回收
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取用户设置的图片
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        //将图片转换成对应的bitmap
        Bitmap srcBitmap = ((BitmapDrawable) drawable).getBitmap();
        //获取图片的宽和高
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        //获取控件的宽和高
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        //计算缩放比例
        float scale = Math.max(measuredHeight * 1f / height, measuredWidth * 1f / width);
        //矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        //创建一个缩放的bitmap
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);
        //创建一个与控件一样大小的空白bitmap
        Bitmap blankBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(blankBitmap);
        //重置画笔属性
        mPaint.reset();
        switch (shape) {
            case CIRCLE:
                canvas1.drawCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2, mPaint);
                break;
            case ROUNDRECT:
                canvas1.drawRoundRect(new RectF(0,0,measuredWidth,measuredHeight),10,10,mPaint);
                break;
            case RECTANGLE:
                canvas1.drawRect(0,0,measuredWidth,measuredHeight,mPaint);
                break;
            case OVAL:
                canvas1.drawOval(new RectF(0,0,measuredWidth,measuredHeight),mPaint);
                break;
            case RHOMBUS:
            {
                Path path = new Path();
                path.moveTo(measuredWidth/2,0);
                path.lineTo(measuredWidth,measuredHeight/2);
                path.lineTo(measuredWidth/2,measuredHeight);
                path.lineTo(0,measuredHeight/2);
                path.close();
                canvas1.drawPath(path,mPaint);
            }
                break;
            case HEXAGON:
                break;
        }
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas1.drawBitmap(bitmap,0,0,mPaint);
        //如果最后一个参数不为null，会将阴影显示出来
        canvas.drawBitmap(blankBitmap,0,0,null);
        if (blankBitmap != null) {
            blankBitmap.recycle();
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
