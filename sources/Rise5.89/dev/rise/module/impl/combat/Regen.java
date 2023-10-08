package dev.rise.module.impl.combat;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Regen", description = "Regenerates your health super fast by spamming position packets", category = Category.COMBAT)
public class Regen extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Ghostly");
    private final NumberSetting health = new NumberSetting("Health", this, 15, 0, 20, 1);
    private final NumberSetting speed = new NumberSetting("Speed", this, 20, 1, 300, 1);
    private final NumberSetting tick = new NumberSetting("Tick", this, 1, 1, 20, 1);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.ticksExisted > 10 && mc.thePlayer.getHealth() < health.getValue() && mc.thePlayer.ticksExisted % tick.getValue() == 0) {
            for (int i = 0; i < speed.getValue(); i++) {
                switch (mode.getMode()) {
                    case "Normal":
                        PacketUtil.sendPacket(new C03PacketPlayer());
                        break;

                    case "Ghostly":
                        PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1E-9, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        break;
                }
            }
        }
    }
}
