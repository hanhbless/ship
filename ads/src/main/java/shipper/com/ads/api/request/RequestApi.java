package shipper.com.ads.api.request;

import retrofit.Callback;
import shipper.com.ads.api.IServiceApi;
import shipper.com.ads.api.response.ClickAdsResponse;
import shipper.com.ads.api.response.GetAdsResponse;
import shipper.com.ads.api.response.InstallAdsResponse;
import shipper.com.ads.api.sender.ClickAdsSender;
import shipper.com.ads.api.sender.GetAdsSender;
import shipper.com.ads.api.sender.InstallAdsSender;

public class RequestApi extends BaseRequest {

    public void getAds(GetAdsSender sender, Callback<GetAdsResponse> callback) {
        super.execute();
        IServiceApi serviceApi = restAdapter.create(IServiceApi.class);
        serviceApi.getAds(sender.os, sender.packageApp, sender.country, sender.category_ad,
                sender.device_id, sender.country_ad, sender.timestamp, sender.token, callback);
    }

    public void installAds(InstallAdsSender sender, Callback<InstallAdsResponse> callback) {
        super.execute();
        IServiceApi serviceApi = restAdapter.create(IServiceApi.class);
        serviceApi.installAds(sender.os, sender.packageApp, sender.country, sender.device, sender.timestamp,
                sender.token, callback);
    }

    public void clickAds(ClickAdsSender sender, Callback<ClickAdsResponse> callback) {
        super.execute();
        IServiceApi serviceApi = restAdapter.create(IServiceApi.class);
        serviceApi.clickAds(sender.os, sender.m_package, sender.country, sender.device, sender.ad_type,
                sender.ad_package, sender.timestamp, sender.token, callback);
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected String getStringSender() {
        return null;
    }
}
