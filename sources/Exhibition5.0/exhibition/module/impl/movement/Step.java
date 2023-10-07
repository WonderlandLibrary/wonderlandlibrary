// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import exhibition.event.RegisterEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import exhibition.event.impl.EventStep;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Step extends Module
{
    public Step(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventStep.class })
    @Override
    public void onEvent(final Event event) {
        final EventStep es = (EventStep)event;
        if (es.isPre()) {
            if (!Step.mc.thePlayer.movementInput.jump && Step.mc.thePlayer.isCollidedVertically) {
                es.setStepHeight(1.0);
                es.setActive(true);
            }
        }
        else if (es.getRealHeight() == 1.0) {
            final double realHeight = es.getRealHeight();
            final double height1 = realHeight * 0.42;
            final double height2 = realHeight * 0.75;
            Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + height1, Step.mc.thePlayer.posZ, Step.mc.thePlayer.onGround));
            Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + height2, Step.mc.thePlayer.posZ, Step.mc.thePlayer.onGround));
            Bhop.stage = -3;
        }
    }
}
