// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import vip.Resolute.auth.Authentication;
import vip.Resolute.events.impl.EventRenderNametag;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.modules.Module;

public class StreamerMode extends Module
{
    public BooleanSetting hideNames;
    public static BooleanSetting hideScoreboard;
    public static String name;
    public static boolean enabled;
    
    public StreamerMode() {
        super("StreamerMode", 0, "Protects your name", Category.PLAYER);
        this.hideNames = new BooleanSetting("Hide Names", true);
        this.addSettings(this.hideNames);
    }
    
    @Override
    public void onEnable() {
        StreamerMode.enabled = true;
    }
    
    @Override
    public void onDisable() {
        StreamerMode.enabled = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRenderNametag && this.hideNames.isEnabled()) {
            e.setCancelled(true);
        }
    }
    
    static {
        StreamerMode.hideScoreboard = new BooleanSetting("Hide Scoreboard", true);
        StreamerMode.name = Authentication.username;
        StreamerMode.enabled = false;
    }
}
