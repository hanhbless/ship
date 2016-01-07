package shipper.com.ads.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */

public class TextViewTitle extends TextViewPlus {
    private boolean isRun = true;


    public TextViewTitle(Context context) {
        super(context);
        isInEditMode();
        setCustomEllipsize();
    }

    public TextViewTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        isInEditMode();
        setCustomEllipsize();
    }

    public TextViewTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isInEditMode();
        setCustomEllipsize();
    }

    private void setCustomEllipsize() {
        runEllipsize(isRun);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isRun = !isRun;
                runEllipsize(isRun);
            }
        });
    }

    private void runEllipsize(boolean run) {
        TextViewTitle.this.setSelected(false);
        TextViewTitle.this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        if (run) {
            TextViewTitle.this.setSelected(true);
        } else {
            TextViewTitle.this.setSelected(false);
        }
    }

}
