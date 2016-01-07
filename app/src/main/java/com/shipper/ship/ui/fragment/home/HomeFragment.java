package com.shipper.ship.ui.fragment.home;

import android.view.View;
import android.widget.Button;

import com.shipper.ship.R;
import com.shipper.ship.ui.eventbus.DialogAdsEvent;
import com.shipper.ship.ui.fragment.BaseFragment;
import com.shipper.ship.utils.Log;

import java.security.SignatureException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shipper.com.ads.api.request.RequestApi;
import shipper.com.ads.api.response.GetAdsResponse;
import shipper.com.ads.api.response.VipAdsResponse;
import shipper.com.ads.api.sender.GetAdsSender;
import shipper.com.ads.types.DialogAds;
import shipper.com.ads.utils.Utils;

public class HomeFragment extends BaseFragment {
    private EventBus bus = EventBus.getDefault();
    private DialogAds dialogAds;

    @Override
    protected int getContentView() {
        return R.layout.home_fragment_layout;
    }

    @Override
    protected void initView() {
        Button btnOpenChildScreen = (Button) findViewById(R.id.btn_open_child);
        btnOpenChildScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openChild();
            }
        });

        dialogAds = new DialogAds(getActivity());
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    //-- On receiver when child fragment closed
    //-- Show dialog ads in here
    public void onEvent(DialogAdsEvent event) {
        showAds();
    }

    private void showAds() {
        GetAdsSender sender = new GetAdsSender();
        sender.country = Utils.getLocaleCurrently(getActivity().getApplicationContext());
        sender.device_id = Utils.getDeviceId(getActivity().getApplicationContext());
        sender.timestamp = Utils.getTimeStamp();
        try {
            sender.token = Utils.hashMac(sender.getParamsString());
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        RequestApi requestApi = new RequestApi();
        requestApi.getAds(sender, new Callback<GetAdsResponse>() {
            @Override
            public void success(GetAdsResponse getAdsResponse, Response response) {
                if (getAdsResponse != null && 0 == getAdsResponse.code &&
                        getAdsResponse.vipAdsList != null &&
                        getAdsResponse.vipAdsList.size() > 0) {
                    VipAdsResponse item = getAdsResponse.vipAdsList.get(Utils.randInt(0, getAdsResponse.vipAdsList.size()));
                    VipAdsResponse.AppAds appAds = item.app;
                    int index = item.app.languageAdsList.indexOf(new VipAdsResponse.AppAds.LanguageAds(item.app.defaultLanguage));
                    if (index >= 0) {
                        VipAdsResponse.AppAds.LanguageAds languageAds = item.app.languageAdsList.get(index);
                        dialogAds.show(appAds, languageAds);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.toString());
            }
        });
    }
}
