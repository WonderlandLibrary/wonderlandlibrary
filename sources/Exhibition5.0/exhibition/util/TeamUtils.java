// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.entity.player.EntityPlayer;

public class TeamUtils
{
    public static boolean isTeam(final EntityPlayer e, final EntityPlayer e2) {
        return e.getDisplayName().getFormattedText().contains("§" + isTeam(e)) && e2.getDisplayName().getFormattedText().contains("§" + isTeam(e));
    }
    
    private static String isTeam(final EntityPlayer player) {
        final Matcher m = Pattern.compile("§(.).*§r").matcher(player.getDisplayName().getFormattedText());
        if (m.find()) {
            return m.group(1);
        }
        return "f";
    }
}
