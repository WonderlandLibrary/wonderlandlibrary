package dev.tenacity.module.impl.misc;

import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.hackerdetector.Detection;
import dev.tenacity.hackerdetector.DetectionManager;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class HackerDetector extends Module {

    private final DetectionManager detectionManager = new DetectionManager();
    private final TimerUtil timer = new TimerUtil();
    private final MultipleBoolSetting detections = new MultipleBoolSetting("Detections",
            new BooleanSetting("Flight A", true),
            new BooleanSetting("Flight B", true),
            new BooleanSetting("Reach A", true));

    public HackerDetector() {
        super("HackerDetector", Category.MISC, "Detects people using cheats inside your game");
        this.addSettings(detections);
    }

    @Override
    public void onTickEvent(TickEvent event) {
        if(mc.theWorld == null || mc.thePlayer == null) return;
        for(Entity entity : mc.theWorld.getLoadedEntityList()) {
            if(entity instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                if(entityPlayer != mc.thePlayer) {
                    for(Detection d : detectionManager.getDetections()) {
                        if(detections.getSetting(d.getName()).isEnabled()) {
                            if(d.runCheck(entityPlayer) && System.currentTimeMillis() > d.getLastViolated() + 500) {
                                NotificationManager.post(NotificationType.WARNING, entityPlayer.getName(), "has flagged " + d.getName() + " | " + EnumChatFormatting.BOLD + entityPlayer.VL);
                                entityPlayer.VL++;
                                d.setLastViolated(System.currentTimeMillis());
                            }
                        }
                    }
                }
            }
        }
    }

}
