package best.azura.client.util.other;

public class PlayTimeUtil {
    public static String format(long time) {
        String playTime = "";
        final long totalSecs = time / 1000, secs = totalSecs % 60,
                minutes = (totalSecs / 60) % 60, hours = (totalSecs / 60 / 60) % 24, days = (totalSecs / 60 / 60) / 24;
        if (days != 0) playTime += days + "d ";
        if (hours != 0) playTime += hours + "h ";
        if (minutes != 0) playTime += minutes + "m ";
        if (secs != 0) playTime += secs + "s ";
        if (playTime.endsWith(" ")) playTime = playTime.substring(0, playTime.length() - 1);
        return playTime;
    }
}
