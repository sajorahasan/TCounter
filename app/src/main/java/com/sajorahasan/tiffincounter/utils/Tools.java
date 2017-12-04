package com.sajorahasan.tiffincounter.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class Tools {

    public static void rateAction(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

    /**
     * For device info parameters
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE + "";
    }

    public static int getVersionCode(Context ctx) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public static String showDeviceDetails(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "Device OS version: " + Build.VERSION.RELEASE + "\n App Version: " + body +
                    "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return body;
    }

//    public static String getVersionNamePlain(Context ctx) {
//        try {
//            PackageManager manager = ctx.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
//            return info.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            return ctx.getString(R.string.version_unknown);
//        }
//    }

    public static String getFormattedDate() {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM, hh:mm a   E");
        return newFormat.format(new Date());
    }

    public static String getFormattedDateSimple() {
        SimpleDateFormat newFormat = new SimpleDateFormat(Constant.DOB_FORMAT);
        return newFormat.format(new Date());
    }

    public static String getFormattedDateSimple(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat(Constant.DOB_FORMAT);
        return newFormat.format(date);
    }

//    public static void displayImageOriginal(Context ctx, ImageView img, String url) {
//        try {
//            Glide.with(ctx).load(url)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(img);
//        } catch (Exception e) {
//        }
//    }
//
//    public static void displayImageThumbnail(Context ctx, ImageView img, String url, float thumb) {
//        try {
//            Glide.with(ctx).load(url)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .thumbnail(thumb)
//                    .into(img);
//        } catch (Exception e) {
//
//        }
//
//    }


    public static int colorDarker(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f; // value component
        return Color.HSVToColor(hsv);
    }

    public static int colorBrighter(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] /= 0.8f; // value component
        return Color.HSVToColor(hsv);
    }

//    public static int getGridSpanCount(Activity activity) {
//        Display display = activity.getWindowManager().getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        display.getMetrics(displayMetrics);
//        float screenWidth = displayMetrics.widthPixels;
//        float cellWidth = activity.getResources().getDimension(R.dimen.item_product_width);
//        return Math.round(screenWidth / cellWidth);
//    }

    public static int getFeaturedNewsImageHeight(Activity activity) {
        float w_ratio = 2, h_ratio = 1; // we use 2:1 ratio
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels - 10;
        float resHeight = (screenWidth * h_ratio) / w_ratio;
        return Math.round(resHeight);
    }

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static Bitmap getBitmap(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

//    public static String getVersionName(Context ctx) {
//        try {
//            PackageManager manager = ctx.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
//            return ctx.getString(R.string.app_version) + " " + info.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            return ctx.getString(R.string.version_unknown);
//        }
//    }

    public static void copyToClipboard(Context context, String data) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("clipboard", data);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
    }

    public static void rippleBlack(View view) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleHover(true)
                .rippleAlpha(0.2F)
                .rippleColor(0xFF585858)
                .create();
    }

    public static void rippleBlackAdapter(View view) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleHover(true)
                .rippleInAdapter(true)
                .rippleAlpha(0.2F)
                .rippleColor(Color.BLACK)
                .create();
    }

    public static void rippleWhite(View view) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleHover(true)
                .rippleAlpha(0.2F)
                .rippleColor(Color.WHITE)
                .create();
    }

    public static void toastNoInternet(Context context) {
        Toasty.warning(context, "No Internet Connection!", Toast.LENGTH_SHORT, true).show();
    }

    public static void toastSuccess(Context context, String message) {
        Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void toastError(Context context, String message) {
        try {
            Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toastWarn(Context context, String message) {
        try {
            Toasty.warning(context, message, Toast.LENGTH_SHORT, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareImage(Context context, Bitmap image, String message) {
        File pictureFile = getOutputMediaFile(context);
        if (pictureFile == null) {
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpg");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pictureFile));
            share.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(Intent.createChooser(share, "Share Image"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shareImage(Context context, Bitmap image) {
        File pictureFile = getOutputMediaFile(context);
        if (pictureFile == null) {
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpg");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pictureFile));
            share.putExtra(Intent.EXTRA_TEXT, "Shared via Ingrid Shaadi App.");
            context.startActivity(Intent.createChooser(share, "Share Image"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static File getOutputMediaFile(Context context) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/" + context.getPackageName() + "/Files");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        File mediaFile;
        Random generator = new Random();
        int n = 1000;
        n = generator.nextInt(n);
        String mImageName = "Image-" + n + ".jpg";

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

}
