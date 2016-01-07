package shipper.com.ads.api.sender;

public class InstallAdsSender extends BaseSender {
    public String os = "android";
    public String packageApp;
    public String country;
    public String device;
    public String timestamp;
    public String token;

    @Override
    public String getParamsString() {
        return "os=" + os +
                "&package=" + packageApp +
                "&country=" + country +
                "&device=" + device +
                "&timestamp=" + timestamp;
    }

    @Override
    public String toString() {
        return "InstallAdsSender{" +
                "os='" + os + '\'' +
                ", packageapp='" + packageApp + '\'' +
                ", country='" + country + '\'' +
                ", device='" + device + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
