package codes.karo.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Utils {
    private static final int CONNECTION_TIMEOUT = 5000; // 5 seconds

    public static int getAveragePixelsColor(int[] pixels) {
        final int pixelsCount = pixels.length;
        int redPixelsCount = 0;
        int greenPixelsCount = 0;
        int bluePixelsCount = 0;

        for (@ColorInt int pixel : pixels) {
            redPixelsCount += Color.red(pixel);
            greenPixelsCount += Color.green(pixel);
            bluePixelsCount += Color.blue(pixel);
        }

        return Color.rgb(
                redPixelsCount/pixelsCount,
                greenPixelsCount/pixelsCount,
                bluePixelsCount/pixelsCount);
    }

    public static Bitmap getBitmapFromURL(final String imageUrl) throws IOException {
        URLConnection connection = new URL(imageUrl).openConnection();
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        InputStream input = connection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
        input.close();

        return bitmap;
    }

    public static String colorIntToHex(@ColorInt int color) {
        return String.format("%06X", (0xFFFFFF & color));
    }
}
