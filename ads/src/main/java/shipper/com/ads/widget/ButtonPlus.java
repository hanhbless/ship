/*
 * *
 *  * All software created will be owned by
 *  * Patient Doctor Technologies, Inc. in USA
 *
 */

package shipper.com.ads.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonPlus extends Button {
    public ButtonPlus(Context context) {
        super(context);
        isInEditMode();
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        isInEditMode();
        //setCustomFont(context, attrs);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isInEditMode();
        //setCustomFont(context, attrs);
    }

    /*private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs,
                R.styleable.TextViewPlus);
        String customFont = a.getString(R.styleable.TextViewPlus_customFont);
        if (customFont == null || customFont.length() == 0) {
            customFont = ctx.getString(R.string.font_normal);
        }
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            Log.d("RadioButton Locale: " + GlobalSingleton.INSTANCE.getStrLocaleLanguage());
            tf = Typeface.createFromAsset(ctx.getAssets(),
                    "fonts/" + GlobalSingleton.INSTANCE.getStrLocaleLanguage() + "/" + asset);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        setTypeface(tf);
        return true;
    }

    public void setCustomFont(Context ctx, String languageLocale, String fontName) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(),
                    "fonts/" + languageLocale + "/" + fontName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTypeface(tf);
    }*/
}
