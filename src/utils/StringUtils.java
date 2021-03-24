package utils;

public class StringUtils {

    public static boolean isIgnoredCharacter(int c) {
        return isReturn(c) || isSpace(c);
    }

    public static boolean isReturn(int c) {
        return c == 13;
    }
    public static boolean isSpace(int c) {
        return c == 32;
    }

    public static boolean isEOL(int c) {
        return c == 10;
    }

    public static boolean isEOF(int c) {return c == 65535 || c == '\0';}

    public static String getHexFromDecimal(int number, int padding, boolean hasXPrefix) {
        return String.format((hasXPrefix ? "0x%0" : "%0") + padding + "X", number);
    }

    public static String getCustomFormat(int number, Object str) {
        return String.format("%1$-" + number + "s", str.toString());
    }

    public static boolean isSemicolon(int c) {
        return c == 59;
    }

    public static boolean isMinusSign(int c) {
        return c == 45;
    }

    public static boolean isPeriod(char c) {
        return c == 46;
    }

    public static boolean isQuote(char c) {
        return c == 34;
    }
}
