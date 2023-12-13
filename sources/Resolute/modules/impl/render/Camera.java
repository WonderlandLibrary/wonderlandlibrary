// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.modules.Module;

public class Camera extends Module
{
    public static boolean noHurtEnabled;
    public static boolean viewclipEnabled;
    public BooleanSetting nohurt;
    public BooleanSetting viewclip;
    
    public Camera() {
        super("Camera", 0, "Adjusts player camera", Category.RENDER);
        this.nohurt = new BooleanSetting("NoHurtCamera", true);
        this.viewclip = new BooleanSetting("View Clip", false);
        this.addSettings(this.nohurt, this.viewclip);
    }
    
    @Override
    public void onDisable() {
        Camera.noHurtEnabled = false;
        Camera.viewclipEnabled = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventUpdate) {
            if (this.nohurt.isEnabled()) {
                Camera.noHurtEnabled = true;
            }
            else {
                Camera.noHurtEnabled = false;
            }
            if (this.viewclip.isEnabled()) {
                Camera.viewclipEnabled = true;
            }
            else {
                Camera.viewclipEnabled = false;
            }
        }
    }
}
