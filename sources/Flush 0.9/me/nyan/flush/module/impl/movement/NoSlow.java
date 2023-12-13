package me.nyan.flush.module.impl.movement;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventSlowdown;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.player.PlayerUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "NCP", "Vanilla", "NCP");

    public NoSlow() {
        super("NoSlowdown", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onSlowdown(EventSlowdown e) {
        e.cancel();
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (mode.is("NCP") && PlayerUtils.isBlocking() && MovementUtils.isMoving() && mc.thePlayer.onGround) {
            if (e.isPre()) {
                mc.playerController.syncCurrentPlayItem();
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            } else {
                mc.playerController.syncCurrentPlayItem();
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}
