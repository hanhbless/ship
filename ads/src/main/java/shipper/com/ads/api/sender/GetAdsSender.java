package shipper.com.ads.api.sender;

public class GetAdsSender extends BaseSender {
    public String os = "android";
    public String packageApp = "all";
    public String country = "all";
    public String category_ad = "all";
    public String device_id;
    public String country_ad = "all";
    public String timestamp;
    public String token;

    @Override
    public String getParamsString() {
        return "os=" + os +
                "&package=" + packageApp +
                "&country=" + country +
                "&category_ad=" + category_ad +
                "&device_id=" + device_id +
                "&country_ad=" + country_ad +
                "&timestamp=" + timestamp;
    }

    @Override
    public String toString() {
        return "GetAdsSender{" +
                "os='" + os + '\'' +
                ", packageapp='" + packageApp + '\'' +
                ", country='" + country + '\'' +
                ", category_ad='" + category_ad + '\'' +
                ", device_id='" + device_id + '\'' +
                ", country_ad='" + country_ad + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
