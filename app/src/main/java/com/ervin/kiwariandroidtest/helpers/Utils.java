package com.ervin.kiwariandroidtest.helpers;

import java.text.DateFormat;
import java.util.Date;

public class Utils {
    public static String produceId(String senderId, String receiverId) {
        String result;
        if (senderId.compareTo(receiverId)>receiverId.compareTo(senderId)){
            result = senderId + "_" + receiverId;
        } else {
            result = receiverId + "_" + senderId;
        }
        return result;
    }

    public static String dateFormat(long timestamp) {
        Date date = new Date(timestamp);
        DateFormat formatter = DateFormat.getDateTimeInstance();
        return formatter.format(date);
    }

}
