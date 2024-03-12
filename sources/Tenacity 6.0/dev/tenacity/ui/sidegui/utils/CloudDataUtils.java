// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.utils;

import dev.tenacity.ui.sidegui.panels.scriptpanel.ScriptPanel;
import dev.tenacity.ui.sidegui.panels.configpanel.ConfigPanel;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.misc.MathUtils;

public class CloudDataUtils
{
    public static String getLastEditedTime(final String epoch) {
        final long epochTime = Long.parseLong(epoch);
        long timeSince = System.currentTimeMillis() / 1000L - epochTime;
        if (timeSince < 60L) {
            return "Just now";
        }
        timeSince = (long)MathUtils.round(timeSince / 60.0f, 0);
        if (timeSince < 60L) {
            return timeSince + ((timeSince > 1L) ? " minutes " : " minute ") + "ago";
        }
        timeSince = (long)MathUtils.round(timeSince / 60.0f, 0);
        if (timeSince < 24L) {
            return timeSince + ((timeSince > 1L) ? " hours " : " hour ") + "ago";
        }
        timeSince = (long)MathUtils.round(timeSince / 24.0f, 0);
        return timeSince + ((timeSince > 1L) ? " days " : " day ") + "ago";
    }
    
    public static void refreshCloud() {
        if (Tenacity.INSTANCE.getSideGui().getPanels() != null) {
            final ConfigPanel configPanel = Tenacity.INSTANCE.getSideGui().getPanels().get("Configs");
            configPanel.refresh();
            final ScriptPanel scriptPanel = Tenacity.INSTANCE.getSideGui().getPanels().get("Scripts");
            scriptPanel.refresh();
        }
    }
}
