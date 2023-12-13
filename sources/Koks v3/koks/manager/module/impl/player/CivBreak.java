package koks.manager.module.impl.player;

import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

/**
 * @author kroko
 * @created on 05.10.2020 : 21:56
 */

@ModuleInfo(name = "CivBreak", description = "It continues to break down the blocks for you", category = Module.Category.PLAYER)
public class CivBreak extends Module {

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                if (getGameSettings().keyBindAttack.pressed) {
                    getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
                    getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
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
