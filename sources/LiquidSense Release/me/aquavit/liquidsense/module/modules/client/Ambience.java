package me.aquavit.liquidsense.module.modules.client;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@ModuleInfo(name = "Ambience", description = "Ambience", category = ModuleCategory.CLIENT)
public class Ambience extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[] {"Day", "Dusk", "Night", "Dynamic", "Custom"}, "Dynamic");
    private final IntegerValue customTime = new IntegerValue("Custom", 12000, 0, 24000);
    private final IntegerValue dynamicSpeed = new IntegerValue("DynamicSpeed", 20, 1, 50);

    private long currentTime;
    private final MSTimer msTimer = new MSTimer();

    @Override
    public void onEnable() {
        currentTime = mc.theWorld.getWorldTime();
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S03PacketTimeUpdate) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        switch (modeValue.get()) {
            case "Day":
                currentTime = 2000;
                break;
            case "Dusk":
                currentTime = 13050;
                break;
            case "Night":
                currentTime = 16000;
                break;
            case "Dynamic":
                if (msTimer.hasTimePassed(1L)) {
                    if (currentTime < 24000L) {
                        currentTime += dynamicSpeed.get();
                    }

                    msTimer.reset();
                }

                if (currentTime >= 24000L) {
                    currentTime = 1L;
                }

                break;
            case "Custom":
                currentTime = customTime.get();
                break;
        }

        mc.theWorld.setWorldTime(currentTime);
    }
}
