package dev.tenacity.module.impl.misc;

import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.hackerdetector.Detection;
import dev.tenacity.hackerdetector.DetectionManager;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.module.Module;
import dev.tenacity.event.IEventListener;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.MultipleBoolSetting;
import dev.tenacity.util.misc.ChatUtil;
import dev.tenacity.util.misc.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class HackerDetectorModule extends Module {

    private final DetectionManager detectionManager = new DetectionManager();
    private final TimerUtil timer = new TimerUtil();

    public HackerDetectorModule() {
        super("HackerDetector", "Detects people using cheats inside your game", ModuleCategory.MISC);
        MultipleBoolSetting detections = new MultipleBoolSetting("Detections",
                new BooleanSetting("Flight A", true),
                new BooleanSetting("Flight B", true),
                new BooleanSetting("Reach A", true));
        initializeSettings(detections);
    }
    private final IEventListener<TickEvent> onTick = e -> {
        ChatUtil.hacker(String.valueOf(detectionManager.getDetections()));
        if (mc.theWorld == null || mc.thePlayer == null) return;
        ChatUtil.hacker(String.valueOf(detectionManager.getDetections()));
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            ChatUtil.hacker(String.valueOf(detectionManager.getDetections()));
            if (entity instanceof EntityPlayer) {
                ChatUtil.hacker(String.valueOf(detectionManager.getDetections()));
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                ChatUtil.hacker(String.valueOf(detectionManager.getDetections()));
                for (Detection d : detectionManager.getDetections()) {
                    ChatUtil.hacker(String.valueOf(detectionManager.getDetections()));
                }
            }
        }
    };
}