package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

import java.util.Random;

public class NoAFK
        extends Module {
    private final Setting<Boolean> swing = this.register(new Setting<Boolean>("Swing", true));
    private final Setting<Boolean> turn = this.register(new Setting<Boolean>("Turn", true));
    private final Random random = new Random();

    public NoAFK() {
        super("NoAFK", "Prevents you from getting kicked for afk.", Module.Category.MISC, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (NoAFK.mc.playerController.getIsHittingBlock()) {
            return;
        }
        if (NoAFK.mc.player.ticksExisted % 40 == 0 && this.swing.getValue().booleanValue()) {
            NoAFK.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        if (NoAFK.mc.player.ticksExisted % 15 == 0 && this.turn.getValue().booleanValue()) {
            NoAFK.mc.player.rotationYaw = this.random.nextInt(360) - 180;
        }
        if (!this.swing.getValue().booleanValue() && !this.turn.getValue().booleanValue() && NoAFK.mc.player.ticksExisted % 80 == 0) {
            NoAFK.mc.player.jump();
        }
    }
}

