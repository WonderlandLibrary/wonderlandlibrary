/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.player;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "FastEat", description = "Eat like Dream, who lives at 2710 Engli-.", category = Category.PLAYER)
public final class FastEat extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Ghostly");
    private final NumberSetting speed = new NumberSetting("Speed", this, 20, 1, 100, 1);

    @Override
    public void onUpdateAlwaysInGui() {
        speed.hidden = mode.is("Hypixel");
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.isEating() && !this.getModule(Scaffold.class).isEnabled()) {
            for (int i = 0; i < (int) (speed.getValue() / 2); i++) {
                switch (mode.getMode()) {
                    case "Normal":
                        PacketUtil.sendPacket(new C03PacketPlayer(mc.thePlayer.onGround));
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
                        break;

                    case "Ghostly":
                        PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1E-9, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        break;
                }
            }
        }
    }
}
