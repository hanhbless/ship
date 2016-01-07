package shipper.com.ads.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAdsResponse extends BaseResponse {
    @SerializedName("vip_ads")
    public ArrayList<VipAdsResponse> vipAdsList;

    @SerializedName("normal_ads")
    public ArrayList<NormalAdsResponse> normalAdsList;
}
