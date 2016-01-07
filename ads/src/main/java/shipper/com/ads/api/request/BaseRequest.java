package shipper.com.ads.api.request;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import shipper.com.ads.api.ConfigApi;

public abstract class BaseRequest {
    protected OkHttpClient okHttpClient = new OkHttpClient();
    protected RestAdapter restAdapter;

    public void execute() {
        okHttpClient.setConnectTimeout(ConfigApi.TIME_OUT_RETROFIT, TimeUnit.SECONDS);
        restAdapter = new RestAdapter.Builder()/*.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("x-ads-app-key", ConfigApi.HEADER_API);
            }
        })*/.setEndpoint(ConfigApi.SERVER_API).setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    protected abstract String getUrl();

    protected abstract String getStringSender();

}
