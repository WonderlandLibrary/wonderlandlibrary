// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import exhibition.util.misc.ChatUtil;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class DeathClip extends Module
{
    public static String DIST;
    public static String CLIP;
    public static String MESSAGE;
    boolean dead;
    public int waitTicks;
    
    public DeathClip(final ModuleData data) {
        super(data);
        this.waitTicks = 0;
        ((HashMap<String, Setting<Boolean>>)this.settings).put(DeathClip.CLIP, new Setting<Boolean>(DeathClip.CLIP, true, "Vertical Clip."));
        ((HashMap<String, Setting<Double>>)this.settings).put(DeathClip.DIST, new Setting<Double>(DeathClip.DIST, 2.0, "Distance to clip.", 1.0, -10.0, 10.0));
        ((HashMap<String, Setting<String>>)this.settings).put(DeathClip.MESSAGE, new Setting<String>(DeathClip.MESSAGE, "/sethome", "Command to execute after clipping."));
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        final EventMotion em = (EventMotion)event;
        final boolean vclip = ((HashMap<K, Setting<Boolean>>)this.settings).get(DeathClip.CLIP).getValue();
        final float distance = ((HashMap<K, Setting<Number>>)this.settings).get(DeathClip.DIST).getValue().floatValue();
        if (em.isPre()) {
            if (vclip && DeathClip.mc.thePlayer.getHealth() == 0.0f && !this.dead) {
                DeathClip.mc.thePlayer.setPosition(DeathClip.mc.thePlayer.posX, DeathClip.mc.thePlayer.posY + distance, DeathClip.mc.thePlayer.posZ);
                ++this.waitTicks;
                this.dead = true;
            }
            else if (DeathClip.mc.thePlayer.getHealth() == 0.0f && !this.dead) {
                final float yaw = DeathClip.mc.thePlayer.rotationYaw;
                DeathClip.mc.thePlayer.setPosition(DeathClip.mc.thePlayer.posX + (distance * 2.0f * Math.cos(Math.toRadians(yaw + 90.0f)) + 0.0 * Math.sin(Math.toRadians(yaw + 90.0f))), DeathClip.mc.thePlayer.posY + 0.0010000000474974513, DeathClip.mc.thePlayer.posZ + (distance * 2.0f * Math.sin(Math.toRadians(yaw + 90.0f)) - 0.0 * Math.cos(Math.toRadians(yaw + 90.0f))));
                ++this.waitTicks;
                this.dead = true;
            }
            if (this.waitTicks > 0) {
                ++this.waitTicks;
                if (this.waitTicks >= 4) {
                    ChatUtil.sendChat(((HashMap<K, Setting<String>>)this.settings).get(DeathClip.MESSAGE).getValue().toString());
                    this.waitTicks = 0;
                }
            }
            if (DeathClip.mc.thePlayer.getHealth() > 0.0f) {
                this.dead = false;
            }
        }
    }
    
    static {
        DeathClip.DIST = "DIST";
        DeathClip.CLIP = "CLIP";
        DeathClip.MESSAGE = "MESSAGE";
    }
}
