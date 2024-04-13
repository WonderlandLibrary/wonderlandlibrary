package dev.tenacity.module.impl.combat;


import dev.tenacity.module.ModuleCategory;
import dev.tenacity.module.Module;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.network.PacketUtils;
import dev.tenacity.util.misc.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C18PacketSpectate;

import java.util.UUID;

@SuppressWarnings("unused")
public final class Criticals extends Module {

    private final ModeSetting modeSetting = new ModeSetting("Mode", "Watchdog", "Packet", "Dev");
    private final NumberSetting delay = new NumberSetting("Delay", 1,0, 20, 1);
    private final TimerUtil timer = new TimerUtil();

    public Criticals() {
        super("Criticals", "Crit attacks", ModuleCategory.COMBAT);
        initializeSettings(modeSetting, delay);
    }

    private final IEventListener<MotionEvent> onMotion = e -> {
        switch (modeSetting.getCurrentMode()) {
            case "Watchdog":
                if (e.isOnGround()) {
                    for (double offset : new double[]{0.06f, 0.01f}) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset + (Math.random() * 0.001), mc.thePlayer.posZ, false));
                    }
                }
                break;

            case "Packet":
                if (mc.objectMouseOver.entityHit != null && mc.thePlayer.onGround) {
                    if (mc.objectMouseOver.entityHit.hurtResistantTime > (int) delay.getCurrentValue()) {
                        for (double offset : new double[]{0.006253453, 0.002253453, 0.001253453}) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                        }
                    }
                }
                break;
//            case "Dev":
//                if (mc.objectMouseOver.entityHit != null && mc.thePlayer.onGround) {
//                    if (mc.objectMouseOver.entityHit.hurtResistantTime > (int) delay.getCurrentValue()) {
//                        for (double offset : new double[]{0.06253453, 0.02253453, 0.001253453, 0.0001135346}) {
//                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
//                            PacketUtils.sendPacketNoEvent(new C18PacketSpectate(UUID.randomUUID()));
//                        }
//                    }
//                }
//                break;
        }
    };

}
