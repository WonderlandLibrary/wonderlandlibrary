package de.tired.base.module.implementation.movement.speed.list;

import de.tired.util.hook.PlayerHook;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.implementation.movement.speed.SpeedExtension;
import de.tired.base.module.implementation.movement.speed.SpeedModeAnnotation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

@SpeedModeAnnotation(name = "VerusYPort")
public class VerusYPort extends SpeedExtension {

    @Override
    public void onPre(EventPreMotion eventPreMotion) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        MC.thePlayer.ticksExisted = 0;
        MC.timer.timerSpeed = 1;
    }

    @Override
    public void onPacket(PacketEvent eventPacket) {

    }

    @Override
    public void onUpdate(UpdateEvent eventUpdate) {
        if (!PlayerHook.isMoving()) return;

        if (MC.thePlayer.onGround) {
            MC.thePlayer.jump();
            MC.thePlayer.motionX *= .83F;
            MC.thePlayer.motionZ *= .83F;
        } else {
            if (MC.thePlayer.motionY >= .2) {
                sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(getX(), getY() - 1.5, getZ()), 1, new ItemStack(Blocks.stone.getItem(getWorld(), new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                MC.thePlayer.motionY = -2;
                PlayerHook.increaseSpeedWithStrafe(PlayerHook.getSpeed() * 1.01);
            }
        }
    }
}
