/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.movement;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventStep;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Step
extends Module {
    private boolean resetNextTick;

    public Step(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventStep.class, EventTick.class})
    public void onEvent(Event event) {
        if (event instanceof EventStep) {
            EventStep es = (EventStep)event;
            if (es.isPre() && !Step.mc.thePlayer.movementInput.jump && Step.mc.thePlayer.isCollidedVertically) {
                es.setStepHeight(1.0);
                es.setActive(true);
            } else if (!es.isPre() && es.isActive() && es.getRealHeight() >= 0.9 && es.getStepHeight() > 0.0) {
                double realHeight = es.getRealHeight();
                double height1 = realHeight * 0.42;
                double height2 = realHeight * 0.75;
                Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + height1, Step.mc.thePlayer.posZ, Step.mc.thePlayer.onGround));
                Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + height2, Step.mc.thePlayer.posZ, Step.mc.thePlayer.onGround));
                Step.mc.timer.timerSpeed = 0.37f;
                new Thread(() -> {
                    try {
                        Thread.sleep(125);
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                    Step.mc.timer.timerSpeed = 1.0f;
                }
                ).start();
            }
        }
    }
}

