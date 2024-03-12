// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.server.ban;

import java.util.List;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import dev.tenacity.ui.altmanager.helpers.Alt;

public class BanUtils
{
    public static void processDisconnect(final Alt alt, final IChatComponent message) {
        final List<IChatComponent> siblings = message.getSiblings();
        if (siblings.size() > 1 && siblings.get(0) instanceof ChatComponentText) {
            final String firstLine = siblings.get(0).getUnformattedText().trim();
            final String msg = message.toString().toLowerCase();
            HypixelBan.Reason reason = null;
            String duration = null;
            if (firstLine.equals("You are permanently banned from this server!")) {
                if (msg.contains("suspicious activity")) {
                    reason = HypixelBan.Reason.SECURITY_ALERT;
                }
                else if (msg.contains("cheat")) {
                    reason = HypixelBan.Reason.CHEATING;
                }
                else {
                    reason = HypixelBan.Reason.MISC;
                }
            }
            else if (firstLine.equals("You are temporarily banned for")) {
                duration = siblings.get(1).getUnformattedText();
                if (msg.contains("security appeal was processed")) {
                    reason = HypixelBan.Reason.SECURITY_ALERT_PROCCESSED;
                }
                else if (msg.contains("cheat")) {
                    reason = HypixelBan.Reason.CHEATING;
                }
                else {
                    reason = HypixelBan.Reason.MISC;
                }
            }
            alt.hypixelBan = new HypixelBan(reason, duration);
            NotificationManager.post(NotificationType.INFO, "Ban Tracker", "Alt marked as banned for " + ((duration == null) ? "PERMANENT" : duration), 5.0f);
        }
    }
}
