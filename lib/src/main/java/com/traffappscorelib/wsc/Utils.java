package com.traffappscorelib.wsc;

public class Utils {

    public static boolean useAdditionalUrl(int count, double percent) {
        int additionalUrlKoef = (int) (100 / percent);
        return count % additionalUrlKoef == 0;

    }
}
