package by.banking.currency.util;

import by.banking.currency.ui.model.response.Dinamics;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStringHelper {
    public static String removeTimeFromDateString(String value) {
        if (value != null) {
            value = value.replace("T00:00:00", "");
        }
        return value;
    }

    public static String removeBeginingZerosFromDateString(String value) {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-M-d");
        Date parseDate;
        try {
            parseDate = formatter1.parse(value);
        } catch (ParseException e) {
            return value;
        }
        return formatter2.format(parseDate);
    }

    public static String findRateDinamics(String value1, String value2) {
        BigDecimal rateOne = new BigDecimal(value1);
        BigDecimal rateTwo = new BigDecimal(value2);
        switch (rateOne.compareTo(rateTwo)) {
            case -1:
                return Dinamics.UP.name();
            case 1:
                return Dinamics.DOWN.name();
            default:
                return Dinamics.EQUAL.name();
        }
    }
}
