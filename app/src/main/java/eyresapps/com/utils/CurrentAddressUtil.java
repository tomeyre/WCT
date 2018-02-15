package eyresapps.com.utils;

/**
 * Created by Tom on 04/11/2017.
 */
public class CurrentAddressUtil {

    String address;

    private static CurrentAddressUtil ourInstance = new CurrentAddressUtil();

    public static CurrentAddressUtil getInstance() {
        return ourInstance;
    }

    private CurrentAddressUtil() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
