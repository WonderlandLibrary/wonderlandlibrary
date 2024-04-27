package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@ModuleInfo(name = "HYTLongJump", description = "HYT LongJump Bypass:/", category = ModuleCategory.MOVEMENT)
public class HYTLongJump extends Module {

    private final ListValue modeValue = new ListValue("Mode", new String[] {"Lag","NewLag"}, "Lag");
    private final IntegerValue lagPowerValue = new IntegerValue("LagPower", 8, 1, 8);
    private final IntegerValue newLagPowerValue = new IntegerValue("NewLagPower", 20, 10, 50);

    private final List<Packet> packets = new ArrayList<>();
    private final LinkedList<double[]> positions = new LinkedList<>();
    private boolean disableLogger;

    private boolean jumped;
    private boolean canBoost;

    @Override
    public void onEnable() {

        synchronized (positions) {
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() / 2), mc.thePlayer.posZ});
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }


    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        if (mc.thePlayer == null)
            return;
        blink();
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        if(modeValue.get().equalsIgnoreCase("newlag")){
            if (!MovementUtils.isMoving() && jumped) {
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                e.zeroXZ();
            }
        }
    }

    @EventTarget
    public void onJump(JumpEvent e) {
        if(modeValue.get().equalsIgnoreCase("newlag")){
            jumped = true;
            canBoost = true;
        }

    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if (mc.thePlayer == null || disableLogger)
            return;

        if (packet instanceof C03PacketPlayer) // Cancel all movement stuff
            event.cancelEvent();

        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook ||
                packet instanceof C08PacketPlayerBlockPlacement ||
                packet instanceof C0APacketAnimation ||
                packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
            event.cancelEvent();

            packets.add(packet);
        }
    }

    @EventTarget
    public void onMotion(MotionEvent e) {

        if(modeValue.get().equalsIgnoreCase("lag")){
            if (MovementUtils.isMoving()) {
                mc.timer.timerSpeed = 1.0F;

                if (mc.thePlayer.onGround) {
                    MovementUtils.strafe(lagPowerValue.get());
                    mc.thePlayer.motionY = 0.42F;
                }
                MovementUtils.strafe(lagPowerValue.get());
            } else {
                mc.thePlayer.motionX = mc.thePlayer.motionZ = 0D;
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        synchronized (positions) {
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }

        if(modeValue.get().equalsIgnoreCase("newlag")){
            if (jumped) {
                if (mc.thePlayer.onGround || mc.thePlayer.capabilities.isFlying) {
                    jumped = false;

                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                    return;
                }
                MovementUtils.strafe(MovementUtils.getSpeed() * (canBoost ? newLagPowerValue.get() : 1F));
                canBoost = false;
            }

            if(mc.thePlayer.onGround && MovementUtils.isMoving()) {
                jumped = true;
                mc.thePlayer.jump();
            }
        }

    }

    private void blink() {
        try {
            disableLogger = true;

            final Iterator<Packet> packetIterator = packets.iterator();
            for (; packetIterator.hasNext(); ) {
                mc.getNetHandler().addToSendQueue(packetIterator.next());
                packetIterator.remove();
            }

            disableLogger = false;
        } catch (final Exception e) {
            e.printStackTrace();
            disableLogger = false;
        }

        synchronized (positions) {
            positions.clear();
        }
    }
    @Override
    public String getTag() {
        return modeValue.get();
    }
}
