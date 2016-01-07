package shipper.com.ads.api;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import shipper.com.ads.api.response.ClickAdsResponse;
import shipper.com.ads.api.response.GetAdsResponse;
import shipper.com.ads.api.response.InstallAdsResponse;

public interface IServiceApi {

    @GET(ConfigApi.URL_GET_ADS)
    void getAds(@Query("os") String os,
                @Query("package") String packageApp,
                @Query("country") String country,
                @Query("category_ad") String category_ad,
                @Query("device_id") String device_id,
                @Query("country_ad") String country_ad,
                @Query("timestamp") String timestamp,
                @Query("token") String token,
                Callback<GetAdsResponse> callback
    );

    @GET(ConfigApi.URL_INSTALL_APP)
    void installAds(@Query("os") String os,
                    @Query("package") String packageApp,
                    @Query("country") String country,
                    @Query("device") String device,
                    @Query("timestamp") String timestamp,
                    @Query("token") String token,
                    Callback<InstallAdsResponse> callback

    );

    @GET(ConfigApi.URL_CLICK_AD)
    void clickAds(@Query("os") String os,
                  @Query("m_package") String m_package,
                  @Query("country") String country,
                  @Query("device") String device,
                  @Query("ad_type") String ad_type,
                  @Query("ad_package") String ad_package,
                  @Query("timestamp") String timestamp,
                  @Query("token") String token,
                  Callback<ClickAdsResponse> callback

    );
}
