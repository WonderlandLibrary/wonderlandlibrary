// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Animation extends Module
{
    public static boolean enabled;
    public static ModeSetting mode;
    public static NumberSetting speed;
    public static NumberSetting scale;
    public static NumberSetting xPos;
    public static NumberSetting yPos;
    public static NumberSetting zPos;
    public static BooleanSetting onItem;
    public static BooleanSetting smoothHitting;
    
    public Animation() {
        super("Animation", 0, "Renders different block animations", Category.RENDER);
        this.addSettings(Animation.mode, Animation.speed, Animation.scale, Animation.onItem, Animation.xPos, Animation.yPos, Animation.zPos, Animation.smoothHitting);
        this.toggled = true;
    }
    
    @Override
    public void onEnable() {
        Animation.enabled = true;
    }
    
    @Override
    public void onDisable() {
        Animation.enabled = false;
        this.toggle();
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventUpdate) {
            this.setSuffix("");
        }
    }
    
    static {
        Animation.enabled = false;
        Animation.mode = new ModeSetting("Mode", "Resolute", new String[] { "Resolute", "Swing", "Swang", "Swong", "Swank", "Slide", "Old", "Astro", "Exhibition", "Exhibobo" });
        Animation.speed = new NumberSetting("Slowdown", 1.0, 0.4, 5.0, 0.1);
        Animation.scale = new NumberSetting("Scale", 0.4, 0.1, 1.0, 0.1);
        Animation.xPos = new NumberSetting("X", 0.0, -1.0, 1.0, 0.05);
        Animation.yPos = new NumberSetting("Y", 0.0, -1.0, 1.0, 0.05);
        Animation.zPos = new NumberSetting("Z", 0.0, -1.0, 1.0, 0.05);
        Animation.onItem = new BooleanSetting("Position on Item", true);
        Animation.smoothHitting = new BooleanSetting("Smooth Hitting", false);
    }
}
