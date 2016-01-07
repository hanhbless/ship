package shipper.com.ads.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;

public class BitmapUtils {
    private static ImageLoader imageLoader = ImageLoader.getInstance();
    private static DisplayImageOptions options;
    private static final int IMAGE_NO_ROTATE = -1;

    public static void setImageWithUILoader(final String url, final ImageView imageView, int defaultRes) {
        try {
            if (url == null)
                return;
            SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    super.onLoadingComplete(imageUri, view, bitmap);
                    Matrix matrix = new Matrix();
                    Bitmap bitmapRotate;
                    boolean isRotate = false;
                    if (rotation(matrix, Uri.parse(url)) != IMAGE_NO_ROTATE)
                        isRotate = true;

                    if (isRotate) /* Image rotated */ {
                        bitmapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    } else
                        bitmapRotate = bitmap;
                    imageView.setImageBitmap(bitmapRotate);
                }
            };
            options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
                    .showImageForEmptyUri(defaultRes)
                    .showImageOnFail(defaultRes)
                    .showImageOnLoading(defaultRes).cacheInMemory(true)
                    .cacheOnDisc(true)
                    .resetViewBeforeLoading(true).build();
            imageLoader.displayImage(url, imageView, options, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int rotation(Matrix matrix, Uri uri) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(uri.getPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int orientation = -1;
        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        String str = "Image Rotate: ";
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                str += 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                str += 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                str += 270;
                break;

            default:
                return IMAGE_NO_ROTATE; /* Image not rotate */
        }

        return 1;
    }
}
