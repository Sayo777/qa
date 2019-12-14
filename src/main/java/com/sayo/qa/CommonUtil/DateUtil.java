package com.sayo.qa.CommonUtil;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date stringToDate(String string){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
       
}
