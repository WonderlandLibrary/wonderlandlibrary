package io.github.raze.modules.collection.movement.step.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.step.ModeStep;
import io.github.raze.utilities.collection.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketStep extends ModeStep {

    public PacketStep() { super("Jump"); }

    private boolean hasStepped;

    @Listen
    public void onMotionEvent(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            if(mc.thePlayer.onGround) {
                hasStepped = false;
            }

            mc.thePlayer.stepHeight = 1;
            if(mc.thePlayer.isCollidedHorizontally && mc.thePlayer.fallDistance < 1.4 && !hasStepped) {
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.13 + Math.random() / 1000, mc.thePlayer.posZ, true));
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.27 + Math.random() / 1000, mc.thePlayer.posZ, true));
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.34 + Math.random() / 1000, mc.thePlayer.posZ, true));
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.19 + Math.random() / 1000, mc.thePlayer.posZ, true));
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2 + Math.random() / 1000, mc.thePlayer.posZ, true));

                hasStepped = true;
            }
        }
    }


    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
        super.onDisable();
    }

}
