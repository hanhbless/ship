package com.shipper.ship.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.shipper.ship.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class OSUtils {
    public static boolean IsSupportVersionOS(int verSupport) {
        if (android.os.Build.VERSION.SDK_INT >= verSupport) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     *
     * @param context The application's environment.
     * @param intent  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and
     * responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }

        return false;
    }

    public static boolean hasPhoneFeature(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    public static boolean isNetworkAvailable(Context appContext) {
        if (appContext == null)
            return false;
        Context context = appContext.getApplicationContext();
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager
                        .getActiveNetworkInfo();
                return activeNetworkInfo != null
                        && activeNetworkInfo.isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean isNetworkAvailable() {
        return isNetworkAvailable(MyApplication.getContext());
    }


    public static boolean hasActiveInternetConnection(Context appContext) {
        if (appContext == null)
            return false;
        Context context = appContext.getApplicationContext();
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(
                        "http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.obj("Error checking internet connection", e);
            }
        } else {
            Log.obj("No network available!");
        }
        return false;
    }


    /**
     * @return
     */
    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    /**
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static void hideKeyBoard(Activity activity) {
        if (activity == null) {
            return;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager.isActive()) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
                Log.d("Keyboard hide called");
            }
        } catch (Exception e) {
        }
    }

    public static void hideKeyBoard(Dialog dialog) {
        if (dialog == null)
            return;
        try {
            InputMethodManager inputManager = (InputMethodManager) dialog.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(0, 0);
        } catch (Exception e) {
        }
    }

    public static boolean keyBoardActive(Activity activity) {
        if (activity == null)
            return false;
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputManager.isActive();
        } catch (Exception e) {
        }
        return false;
    }

    public static void hideKeyBoard(Context appContext, EditText view) {
        if (appContext == null)
            return;

        try {
            Context context = appContext.getApplicationContext();
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showKeyboard(Activity activity, EditText edt) {
        if (activity == null)
            return;

        try {
            edt.requestFocus();
            InputMethodManager mgr = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);
            mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showKeyboard(Activity activity) {
        if (activity == null)
            return;

        try {
            InputMethodManager mgr = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            //mgr.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);
            mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Dialog dialog) {
        if (dialog == null)
            return;
        try {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } catch (Exception e) {

        }
    }

    public static void writeFileToSDcard(String fileName, String body) {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            FileOutputStream fos = null;
            try {
                final File dir = new File(Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + "/DOCTELA/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                final File myFile = new File(dir, fileName + ".txt");
                if (!myFile.exists()) {
                    myFile.createNewFile();
                }
                fos = new FileOutputStream(myFile);
                fos.write(body.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    public static boolean checkCameraDevice() {
        PackageManager pckManager = MyApplication.getContext().getPackageManager();
        return pckManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * check location is enable
     */
    public static boolean checkLocationService(Context context) {
        boolean result;
        LocationManager lm = null;
        boolean gps_enabled = false, network_enabled = false;
        if (lm == null)
            lm = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    public static String getDeviceID() {
        TelephonyManager manager = (TelephonyManager) MyApplication.getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }

    public static int getExifOrientation(String filePath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }

        return degree;
    }

    public static void setWindowSoftInputMode(Activity act, boolean isAdjustResize) {
        act.getWindow().setSoftInputMode(isAdjustResize ?
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE :
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /**
     * Check if application is running on background;
     *
     * @param context
     * @return
     */
    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static void setTouchToHideKeyboard(View view, final Activity activity) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard(activity);
                return false;
            }
        });
    }

    public static boolean isForeground(Context context, String className) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = manager.getRunningTasks(Integer.MAX_VALUE);

        if (taskList != null && taskList.size() > 0) {
            ActivityManager.RunningTaskInfo taskInfo = taskList.get(0);
            if ((taskInfo.topActivity != null && taskInfo.topActivity.getClassName().equals(className)) ||
                    (taskInfo.baseActivity != null && taskInfo.baseActivity.getClassName().equals(className))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForeground(Context context, List<String> classNameList) {
        if (classNameList != null && classNameList.size() > 0) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = manager.getRunningTasks(Integer.MAX_VALUE);

            if (taskList != null && taskList.size() > 0) {
                ActivityManager.RunningTaskInfo taskInfo = taskList.get(0);
                if (taskInfo != null) {
                    Log.i("OSUtils taskInfo:[" + taskInfo.topActivity + "," + taskInfo.baseActivity);
                    if ((taskInfo.topActivity != null && classNameList.contains(taskInfo.topActivity.getClassName())) ||
                            (taskInfo.baseActivity != null && classNameList.contains(taskInfo.baseActivity.getClassName()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
