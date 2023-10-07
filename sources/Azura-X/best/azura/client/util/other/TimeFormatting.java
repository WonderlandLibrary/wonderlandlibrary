package best.azura.client.util.other;

public class TimeFormatting {

    public static String convertMillisToString(final long totalMillis) {
        final long totalSecs = totalMillis / 1000, secs = totalSecs % 60, minutes = (totalSecs / 60) % 60, hours = (totalSecs / 60 / 60) % 24, days = (totalSecs / 60 / 60) / 24;
        StringBuilder text = new StringBuilder();
        if (days > 0) text.append(days).append("d ");
        if (hours > 0) text.append(hours).append("h ");
        if (minutes > 0) text.append(minutes).append("m ");
        if (secs > 0) text.append(secs).append("s ");
        if (text.toString().isEmpty()) text.append("0s");
        if (text.toString().endsWith(" ")) text = new StringBuilder(text.substring(0, text.length() - 1));
        return text.toString();
    }

}