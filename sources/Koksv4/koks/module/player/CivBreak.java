package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.UpdateEvent;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "CivBreak", category = Module.Category.PLAYER, description = "It continues to break down the block for you")
public class CivBreak extends Module {

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof UpdateEvent) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                if (getGameSettings().keyBindAttack.pressed) {
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
