package utils;

public class StringUtils {


    public static boolean isSpace(int c) { return c == 32; }

    public static boolean isEOL(int c) { return c == 10; }

    public static String getHexFromDecimal(int number, int padding, boolean hasXPrefix) {
        return String.format((hasXPrefix ? "0x%0" : "%0") + padding + "X", number);
    }

    public static String getCustomFormat(int number, Object str) {
        return String.format("%1$-" + number + "s", str.toString());
    }
}
