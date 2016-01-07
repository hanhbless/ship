package shipper.com.ads.common;

public class GlobalSingleton {
    private static GlobalSingleton ourInstance = new GlobalSingleton();

    public static GlobalSingleton getInstance() {
        return ourInstance;
    }

    private GlobalSingleton() {
    }
}
