/**
 * Time: 6:59:38 PM
 * Date: Jan 1, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventStep;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step extends Module {

    public Step(ModuleData data) {
        super(data);

    }

    private boolean resetNextTick;

    @Override
    @RegisterEvent(events = {EventStep.class, EventTick.class})
    public void onEvent(Event event) {

        if (event instanceof EventStep) {
            EventStep es = (EventStep) event;
            if (es.isPre() && (!mc.thePlayer.movementInput.jump) && (mc.thePlayer.isCollidedVertically)) {
                es.setStepHeight(1D);
                es.setActive(true);
            } else if (!es.isPre() && es.isActive() && es.getRealHeight() >= 0.87 && es.getStepHeight() > 0) {
                Bhop.stage = -4;
                double realHeight = es.getRealHeight();
                double height1 = realHeight * 0.42D;
                double height2 = realHeight * 0.75D;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height1, mc.thePlayer.posZ, mc.thePlayer.onGround));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height2, mc.thePlayer.posZ, mc.thePlayer.onGround));
                mc.timer.timerSpeed = 0.37F;
                new Thread(() -> {
                    try
                    {
                        Thread.sleep(150L);
                    }
                    catch (InterruptedException localInterruptedException) {}
                    mc.timer.timerSpeed = 1.0F;
                }).start();
            }

        }
    }
}
