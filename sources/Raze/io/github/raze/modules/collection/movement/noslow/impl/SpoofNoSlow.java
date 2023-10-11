package io.github.raze.modules.collection.movement.noslow.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.noslow.ModeNoSlow;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class SpoofNoSlow extends ModeNoSlow {

    public SpoofNoSlow() { super("Spoof"); }

    private int spoofSlot;

    @Listen
    public void onMotionEvent(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            spoofSlot += 1;

            if(1 > spoofSlot || spoofSlot > 8) {
                spoofSlot = 1;
            }

            if(mc.thePlayer.inventory.currentItem == spoofSlot) {
                spoofSlot += 1;
            }

            if(mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking() || mc.thePlayer.isEating()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(spoofSlot));
            }
        }
    }
}
