package com.orange.jiachen.landlords.print;

public class FormatPrinter {

    private FormatPrinter() {
    }

    public static void printNotice(String format, Object... args) {
        System.out.printf(format, args);
    }
}
