package io.github.raze.modules.collection.movement.noslow.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.noslow.ModeNoSlow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPosition;
import net.minecraft.util.EnumFacing;

public class NCPNoSlow extends ModeNoSlow {

    public NCPNoSlow() { super("NCP"); }

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            if (mc.thePlayer.isBlocking()) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPosition.ORIGIN, EnumFacing.DOWN));
            }
        }

        if(event.getState() == Event.State.POST) {
            if (mc.thePlayer.isBlocking()) {
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }
        }
    }

}
