package com.example.collegepal.util;

import java.text.DecimalFormat;

public class NumberingFormat {
    public static String ThousandSeparator(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###");
        decimalFormat.setDecimalSeparatorAlwaysShown(false);

        return decimalFormat.format(number).replace(",", ".");
    }
}
