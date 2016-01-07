package shipper.com.ads.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
    private static final String CRYPT_KEY = "nmtien92@outlook.com";

    public static String hashMac(String paramString, String secretKey)
            throws SignatureException {
        try {
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(
                    secretKey.getBytes(), "hmacSHA256");
            Mac localMac = Mac.getInstance(localSecretKeySpec.getAlgorithm());
            localMac.init(localSecretKeySpec);

            return toHexString(localMac.doFinal(paramString.getBytes()));
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            throw new SignatureException(
                    "error building signature, no such algorithm in device hmacSHA256");
        } catch (InvalidKeyException localInvalidKeyException) {
        }
        throw new SignatureException(
                "error building signature, invalid key hmacSHA256");
    }

    public static String hashMac(String paramString) throws SignatureException {
        return hashMac(paramString, CRYPT_KEY);
    }

    private static String toHexString(byte[] paramArrayOfByte) {
        StringBuilder localStringBuilder = new StringBuilder(
                2 * paramArrayOfByte.length);
        Formatter localFormatter = new Formatter(localStringBuilder);
        int i = paramArrayOfByte.length;
        for (int j = 0; ; j++) {
            if (j >= i) {
                localFormatter.close();
                return localStringBuilder.toString();
            }
            byte b = paramArrayOfByte[j];
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Byte.valueOf(b);
            localFormatter.format("%02x", arrayOfObject);
        }
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static int randInt(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static void openAppFromPlayStore(Activity activity, String packageApp) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageApp)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageApp)));
        }
    }

    public static String getLocaleCurrently(Context context) {
        return context.getResources().getConfiguration().locale.getCountry();
    }

    public static String getTimeStamp() {
        return String.valueOf(SystemClock.currentThreadTimeMillis());
    }
}
