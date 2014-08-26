package nl.returntothesource.wereldontdooien.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        final int roundCornerHor = bitmap.getWidth()/10;
        final int roundCornerVer = bitmap.getHeight()/10;
        final float borderHor = (int) (roundCornerHor * 0.05);
        final float borderVer = (int) (roundCornerVer * 0.05);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth()+roundCornerHor, bitmap
                .getHeight()+roundCornerVer, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect dstRect = new Rect(0, 0, bitmap.getWidth()+roundCornerHor, bitmap.getHeight()+roundCornerVer);
        final RectF rectF = new RectF(dstRect);
        final Rect innerRect = new Rect((int) borderHor, (int) borderVer,
                (int) (bitmap.getWidth() + roundCornerHor - borderHor),
                (int) (bitmap.getHeight() + roundCornerVer - borderVer));
        final RectF innerRectF = new RectF(innerRect);
        final Rect bitmapRect = new Rect(roundCornerHor/2, roundCornerVer/2,
                bitmap.getWidth()+(roundCornerHor/2),
                bitmap.getHeight()+(roundCornerVer/2));
        final RectF bitmapRectF = new RectF(bitmapRect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff321e18);
        canvas.drawRoundRect(rectF, roundCornerHor, roundCornerVer, paint);
        paint.setColor(0xffffffff);
        canvas.drawRoundRect(innerRectF, roundCornerHor-borderHor, roundCornerVer-borderVer, paint);

        paint.setColor(0x00000000);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRoundRect(bitmapRectF, roundCornerHor/2, roundCornerVer/2, paint);

        paint.setColor(0xffffffff);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        canvas.drawBitmap(bitmap, new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()), bitmapRect, paint);

        bitmap.recycle();

        return output;
    }
}
