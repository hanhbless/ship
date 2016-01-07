package shipper.com.ads.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NormalAdsResponse {

    @SerializedName("package")
    public String packageApp;

    @SerializedName("developer")
    public String developer;

    @SerializedName("os")
    public String os;

    @SerializedName("category")
    public String category;

    @SerializedName("supported_ad")
    public ArrayList<String> supportedAdsList;

    @SerializedName("default_language")
    public String defaultLanguage = "";

    @SerializedName("languages")
    public ArrayList<LanguageAds> languageAdsList;

    public static class LanguageAds {
        @SerializedName("name")
        public String name;

        @SerializedName("icon")
        public String icon;

        @SerializedName("banner")
        public String banner;

        @SerializedName("sort_description")
        public String sortDescription;

        @SerializedName("full_description")
        public String fullDescription;

        @SerializedName("language")
        public String language = "";

        public LanguageAds(String language) {
            this.language = language;
        }

        @Override
        public boolean equals(Object o) {
            return language.equals(((LanguageAds) o).language);
        }
    }
}
