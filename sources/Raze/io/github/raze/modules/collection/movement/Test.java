package io.github.raze.modules.collection.movement;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.collection.network.EventPacketReceive;
import io.github.raze.events.collection.network.EventPacketSend;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.utilities.collection.math.TimeUtil;
import io.github.raze.utilities.collection.visual.ChatUtil;
import io.github.raze.utilities.collection.world.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;

public class Test extends AbstractModule {

    public Test() {
        super("Test", "Test module to test bypasses", ModuleCategory.MOVEMENT);
    }

    private int ticks;
    private double y;
    private TimeUtil timer;
    @Listen
    public void onEventMotion(EventMotion event) {
        if(event.getState() == Event.State.PRE) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 9.9 + Math.random() / 500, mc.thePlayer.posZ);
            } else {
                if(mc.thePlayer.ticksExisted % 3 == 0) {
                    mc.thePlayer.motionY = -0.155;
                    mc.timer.timerSpeed = 1.07F;
                } else {
                    mc.timer.timerSpeed = 1F;
                    mc.thePlayer.motionY = -0.1;
                }
            }

            }
        }

    @Listen
    public void onPacketSend(EventPacketSend event) {

    }

    @Listen
    public void onPacketRecive(EventPacketReceive event) {

    }

     @Listen
     public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
     }
}