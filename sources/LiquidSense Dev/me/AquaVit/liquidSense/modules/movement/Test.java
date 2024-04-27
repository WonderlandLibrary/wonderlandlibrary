package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "Nope", description = ":/", category = ModuleCategory.MOVEMENT)
public class Test extends Module {

    private List<Packet<?>> packets = new ArrayList<>();
    private boolean fakelag = false;
    private boolean packetmodify = false;

    @Override
    public void onEnable() {
        fakelag = false;
        packetmodify = false;
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer && fakelag) {
            event.cancelEvent();
            if (packetmodify) {
                ((C03PacketPlayer) packet).onGround = true;
                packetmodify = false;
            }
            packets.add(packet);
        }
    }
    @EventTarget
    public void onMotion(MotionEvent event) {
        final EventState eventState = event.getEventState();
        if (eventState == EventState.PRE) {
            if (!inVoid()) {
                if (fakelag) {
                    fakelag = false;
                    if (packets.size() > 0) {
                        for (Packet<?> packet : packets) {
                            mc.thePlayer.sendQueue.addToSendQueue(packet);
                        }
                        packets.clear();
                    }
                }
                return;
            }
            if (mc.thePlayer.onGround && fakelag) {
                fakelag = false;
                if (packets.size() > 0) {
                    for (Packet<?> packet : packets) {
                        mc.thePlayer.sendQueue.addToSendQueue(packet);
                    }
                    packets.clear();
                }
                return;
            }
            if (mc.thePlayer.fallDistance > 3 && fakelag) {
                packetmodify = true;
                mc.thePlayer.fallDistance = 0;
            }
            if (inAir(4.0, 1.0)) {
                return;
            }
            if (!fakelag) {
                fakelag = true;
            }
        }


    }

    public boolean inVoid() {
        if (mc.thePlayer.posY < 0) {
            return false;
        }

        for (int off = 0; off < mc.thePlayer.posY + 2; off += 2) {
            AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX, off, mc.thePlayer.posZ);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean inAir(double height, double plus) {
        if (mc.thePlayer.posY < 0)
            return false;
        for (int off = 0; off < height; off += plus) {
            AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX, mc.thePlayer.posY - off, mc.thePlayer.posZ);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
