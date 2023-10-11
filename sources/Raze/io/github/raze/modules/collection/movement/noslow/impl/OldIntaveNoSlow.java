package io.github.raze.modules.collection.movement.noslow.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.noslow.ModeNoSlow;
import io.github.raze.utilities.collection.packet.PacketUtil;
import io.github.raze.utilities.collection.world.PlayerUtil;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class OldIntaveNoSlow extends ModeNoSlow {

    public OldIntaveNoSlow() { super("Old Intave"); }

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            if (mc.thePlayer.isUsingItem()) {
                PacketUtil.sendPacket(new C09PacketHeldItemChange(PlayerUtil.getIndexOfItem() % 8 + 1));
                PacketUtil.sendPacket(new C09PacketHeldItemChange(PlayerUtil.getIndexOfItem()));
            }
        }

        if(event.getState() == Event.State.POST) {
            if (mc.thePlayer.isUsingItem()) {
                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(PlayerUtil.getItemStack()));
            }
        }
    }

}
