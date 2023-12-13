package info.sigmaclient.util;

import net.minecraft.entity.player.EntityPlayer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arithmo on 1/19/2017.
 */
public class TeamUtils {

    public static boolean isTeam(final EntityPlayer e, final EntityPlayer e2) {
       // ChatUtil.printChat(e2.getDisplayName().getFormattedText().contains("§" + isTeam(e)) + " " + isTeam(e));
        return e.getDisplayName().getFormattedText().contains("§" + isTeam(e)) && e2.getDisplayName().getFormattedText().contains("§" + isTeam(e));
    }

    private static String isTeam(EntityPlayer player) {
        final Matcher m = Pattern.compile("§(.).*§r").matcher(player.getDisplayName().getFormattedText());
        if (m.find()) {
            return m.group(1);
        }
        return "f";
    }

}
