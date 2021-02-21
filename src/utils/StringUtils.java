package utils;

public class StringUtils {

    public static boolean isWhiteSpace(Character ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }

    public static String getHexFromDecimal(int number, int padding, boolean hasXPrefix) {
        return String.format((hasXPrefix ? "0x%0" : "%0") + padding + "X", number);
    }

    public static String getCustomFormat(int number, Object str) {
        return String.format("%1$-" + number + "s", str.toString());
    }
}
