package nl.returntothesource.wereldontdooien;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

        final int roundCornerHor = bitmap.getWidth()/10;
        final int roundCornerVer = bitmap.getHeight()/10;
        final int paddingHor = roundCornerHor;
        final int paddingVer = roundCornerVer;
        final float borderHor = (int) (roundCornerHor * 0.05);
        final float borderVer = (int) (roundCornerVer * 0.05);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth()+paddingHor, bitmap
                .getHeight()+paddingVer, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect dstRect = new Rect(0, 0, bitmap.getWidth()+paddingHor, bitmap.getHeight()+paddingVer);
        final RectF rectF = new RectF(dstRect);
        final Rect innerRect = new Rect((int) borderHor, (int) borderVer,
                (int) (bitmap.getWidth() + paddingHor - borderHor),
                (int) (bitmap.getHeight() + paddingVer - borderVer));
        final RectF innerRectF = new RectF(innerRect);
        final Rect bitmapRect = new Rect(paddingHor/2, paddingVer/2,
                bitmap.getWidth()+(paddingHor/2),
                bitmap.getHeight()+(paddingVer/2));
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

        return output;
    }
}
