package shipper.com.ads.types;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.SignatureException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shipper.com.ads.R;
import shipper.com.ads.api.request.RequestApi;
import shipper.com.ads.api.response.ClickAdsResponse;
import shipper.com.ads.api.response.GetAdsResponse;
import shipper.com.ads.api.response.InstallAdsResponse;
import shipper.com.ads.api.response.VipAdsResponse;
import shipper.com.ads.api.sender.ClickAdsSender;
import shipper.com.ads.api.sender.GetAdsSender;
import shipper.com.ads.api.sender.InstallAdsSender;
import shipper.com.ads.utils.BitmapUtils;
import shipper.com.ads.utils.Utils;

public class DialogAds extends Dialog implements View.OnClickListener {
    private Activity mAct;

    private View container;
    private ImageView icon, iconClose;
    private TextView tvHeader, tvDes;
    private Button btnGetApp;

    private VipAdsResponse.AppAds appAds;
    private VipAdsResponse.AppAds.LanguageAds languageAds;

    public DialogAds(Activity activity) {
        super(activity, R.style.FullHeightDialog);
        mAct = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ads_layout);
        setCancelable(false);

        container = findViewById(R.id.container);
        icon = (ImageView) findViewById(R.id.icon);
        iconClose = (ImageView) findViewById(R.id.icon_close);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvDes = (TextView) findViewById(R.id.tvDes);
        btnGetApp = (Button) findViewById(R.id.btnGetApp);

        container.setOnClickListener(this);
        iconClose.setOnClickListener(this);
        btnGetApp.setOnClickListener(this);

    }

    public void show(VipAdsResponse.AppAds appAds, VipAdsResponse.AppAds.LanguageAds languageAds) {
        super.show();
        this.appAds = appAds;
        this.languageAds = languageAds;
        BitmapUtils.setImageWithUILoader(languageAds.icon, icon, R.mipmap.ic_launcher);
        tvHeader.setText(languageAds.name);
        tvDes.setText(languageAds.fullDescription);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon_close) {
            dismiss();
        } else if (v.getId() == R.id.btnGetApp) {
            installApp();
        } else if (v.getId() == R.id.container) {
            clickAds();
        }
    }

    private void installApp() {
        if (appAds != null) {
            Utils.openAppFromPlayStore(mAct, appAds.packageApp);

            InstallAdsSender sender = new InstallAdsSender();
            sender.country = Utils.getLocaleCurrently(mAct.getApplicationContext());
            sender.device = Utils.getDeviceId(mAct.getApplicationContext());
            sender.packageApp = appAds.packageApp;
            sender.timestamp = Utils.getTimeStamp();
            try {
                sender.token = Utils.hashMac(sender.getParamsString());
            } catch (SignatureException e) {
                e.printStackTrace();
            }

            RequestApi requestApi = new RequestApi();
            requestApi.installAds(sender, new Callback<InstallAdsResponse>() {
                @Override
                public void success(InstallAdsResponse installAdsResponse, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
        dismiss();
    }

    private void clickAds() {
        if (appAds != null) {
            ClickAdsSender sender = new ClickAdsSender();
            sender.country = Utils.getLocaleCurrently(mAct.getApplicationContext());
            sender.m_package = mAct.getPackageName();
            sender.ad_package = appAds.packageApp;
            sender.ad_type = appAds.category;
            sender.device = Utils.getDeviceId(mAct.getApplicationContext());
            sender.timestamp = Utils.getTimeStamp();

            try {
                sender.token = Utils.hashMac(sender.getParamsString());
            } catch (SignatureException e) {
                e.printStackTrace();
            }

            RequestApi requestApi = new RequestApi();
            requestApi.clickAds(sender, new Callback<ClickAdsResponse>() {
                @Override
                public void success(ClickAdsResponse clickAdsResponse, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
        dismiss();
    }
}
