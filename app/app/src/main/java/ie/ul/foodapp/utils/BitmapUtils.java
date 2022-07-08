package ie.ul.foodapp.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class BitmapUtils {

    /**
     * Crop a Bitmap to respect a given aspect ratio
     * @param source The Bitmap to crop
     * @param relativeWidth Relative width of the cropped Bitmap
     * @param relativeHeight Relative height of the cropped Bitmap
     * @return A new bitmap of the source bitmap if it was in the proper aspect ratio
     */
    public static Bitmap cropToAspectRatio(Bitmap source, float relativeWidth, float relativeHeight) {
        /* Source image is null */
        if (source == null) { return null; }

        final float targetFormat = relativeWidth/relativeHeight;
        final float bannerFormat = source.getWidth() / (float) source.getHeight();

        /* Source image is already in the proper aspect ratio, return */
        if (Math.abs(bannerFormat/targetFormat - 1) < 0.01) { return source; }

        RectF targetRect;
        int width = source.getWidth();
        int height = source.getHeight();

        if (bannerFormat > targetFormat) { // too wide!
            width = (int) (source.getHeight() * targetFormat);
            targetRect = new RectF(
                    - (source.getWidth() - width) / 2,
                    0,
                    width + (source.getWidth() - width) / 2,
                    height
            );
        } else { // too tall!
            height = (int) (source.getWidth() / targetFormat);
            targetRect = new RectF(
                    0,
                    - (source.getHeight() - height) / 2,
                    width,
                    height + (source.getHeight() - height) / 2
            );
        }

        Bitmap croppedBitmap = Bitmap.createBitmap(width, height, source.getConfig());
        Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(source, null, targetRect, null);
        return croppedBitmap;
    }

}
