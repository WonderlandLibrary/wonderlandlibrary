// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.module.impl.render.NotificationsMod;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class Phase extends Module
{
    private final ModeSetting mode;
    private boolean canClip;
    
    public Phase() {
        super("Phase", "Phase", Category.MOVEMENT, "Allows you to phase out from cages");
        this.mode = new ModeSetting("Mode", "Mospixel", new String[] { "Mospixel" });
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEnable() {
        this.setSuffix(this.mode.getMode());
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Phase.mc.thePlayer.noClip = false;
        super.onDisable();
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (!event.isPre()) {
            return;
        }
        if (this.mode.is("Mospixel") && this.canClip) {
            Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY - 4.0, Phase.mc.thePlayer.posZ);
            this.canClip = false;
        }
    }
    
    @Override
    public void onChatReceivedEvent(final ChatReceivedEvent event) {
        if (Phase.mc.thePlayer == null) {
            return;
        }
        final String messageStr = event.message.getUnformattedText();
        if (messageStr.startsWith(Phase.mc.thePlayer.getName() + " has joined")) {
            this.canClip = true;
            if (NotificationsMod.toggleNotifications.isEnabled()) {
                NotificationManager.post(NotificationType.SUCCESS, "Phase", "Phased you out of the cage!");
            }
        }
    }
}
