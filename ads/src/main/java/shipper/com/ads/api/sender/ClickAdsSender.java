package shipper.com.ads.api.sender;

public class ClickAdsSender extends BaseSender {
    public String os = "android";
    public String m_package;
    public String country;
    public String device;
    public String ad_type;
    public String ad_package;
    public String timestamp;
    public String token;

    @Override
    public String getParamsString() {
        return "os=" + os +
                "&m_package=" + m_package +
                "&country=" + country +
                "&device=" + device +
                "&ad_type=" + ad_type +
                "&ad_package=" + ad_package +
                "&timestamp=" + timestamp;
    }

    @Override
    public String toString() {
        return "ClickAdsSender{" +
                "os='" + os + '\'' +
                ", m_package='" + m_package + '\'' +
                ", country='" + country + '\'' +
                ", device='" + device + '\'' +
                ", ad_type='" + ad_type + '\'' +
                ", ad_package='" + ad_package + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
