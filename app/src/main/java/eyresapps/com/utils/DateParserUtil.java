package eyresapps.com.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by thomaseyre on 15/03/2018.
 */

public class DateParserUtil {

    CurrentAddressUtil currentAddress = CurrentAddressUtil.getInstance();

    public SimpleDateFormat getParser(){
        if(currentAddress.getAddress().toLowerCase().contains("uk")) {
            return new SimpleDateFormat("yyyy-MM");
        }
        if(currentAddress.getAddress().toLowerCase().contains("chicago")) {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
        if(currentAddress.getAddress().toLowerCase().contains("los angeles")) {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
        if(currentAddress.getAddress().toLowerCase().contains("durham, nc")) {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
        return new SimpleDateFormat("dd-MM-yyyy");
    }

    public DateFormat getFormat(){
        int style = DateFormat.LONG;
        return DateFormat.getDateInstance(style, Locale.getDefault());
    }
}
