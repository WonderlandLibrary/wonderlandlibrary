package vip.radium.utils;

public class StringUtils {

    private StringUtils() {
    }

    public static String upperSnakeCaseToPascal(String s) {
        if (s == null) return null;
        if (s.length() == 1) return Character.toString(s.charAt(0));
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    public static String replaceUserSymbols(String str) {
        return str.replace('&', '\247').replace("<3", "\u2764");
    }

    public static String getTrimmedClipboardContents() {
        String data = ClipboardUtils.getClipboardContents();

        if (data != null) {
            data = data.trim();

            if (data.indexOf('\n') != -1)
                data = data.replace("\n", "");
        }

        return data;
    }


    public static String fromCharCodes(int[] codes) {
        StringBuilder builder = new StringBuilder();
        for (int cc : codes) {
            builder.append((char) cc);

        }
        return builder.toString();
    }

}
