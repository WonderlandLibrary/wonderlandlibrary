package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@ModuleInfo(name = "HYTRun", description = "HYTRun Bypass:/", category = ModuleCategory.MOVEMENT)
public class HYTRun extends Module {

    private final List<Packet> packets = new ArrayList<>();
    private final LinkedList<double[]> positions = new LinkedList<>();
    private boolean disableLogger;
    Aura killAura = (Aura) LiquidBounce.moduleManager.getModule(Aura.class);
    private final TimeUtils timer = new TimeUtils();

    private final FloatValue MotionF = new FloatValue("MotionSpike", 5.0F, 0.1F, 30F);
    private final FloatValue MotionY = new FloatValue("MotionY", 1.0F, 0.1F, 10F);

    @Override
    public void onDisable() {
        if(mc.thePlayer == null)
            return;

        blink();
    }

    @Override
    public void onEnable() {
        synchronized (positions) {
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() / 2), mc.thePlayer.posZ});
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }
        mc.thePlayer.motionY = MotionY.get();
        mc.thePlayer.motionX *= MotionF.get();
        mc.thePlayer.motionZ *= MotionF.get();
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
    public void onUpdate(UpdateEvent event) {
        synchronized (positions) {
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }
        if(mc.thePlayer.onGround || MovementUtils.isOnGround(0.5))
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        if(mc.thePlayer.isBlocking() && MovementUtils.isMoving() && MovementUtils.isOnGround(0.42) && ( killAura.getTarget() == null)){
            if(timer.delay(65)){
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                timer.reset();
            }
        }
    }

    private void blink() {
        try {
            disableLogger = true;

            final Iterator<Packet> packetIterator = packets.iterator();
            for(; packetIterator.hasNext(); ) {
                mc.getNetHandler().addToSendQueue(packetIterator.next());
                packetIterator.remove();
            }

            disableLogger = false;
        }catch(final Exception e) {
            e.printStackTrace();
            disableLogger = false;
        }

        synchronized(positions) {
            positions.clear();
        }
    }
}