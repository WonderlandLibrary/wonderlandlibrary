package me.AquaVit.liquidSense.modules.movement;

import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.*;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@ModuleInfo(name = "HYTHighJump", description = "HYT HighJump Bypass:/", category = ModuleCategory.MOVEMENT)
public class HYTHighJump extends Module {

    private final List<Packet> packets = new ArrayList<>();
    private final LinkedList<double[]> positions = new LinkedList<>();
    private boolean disableLogger;

    private boolean jumped;


    private final ListValue modeValue = new ListValue("Mode", new String[] {"Bug","Lag"}, "Bug");
    private final IntegerValue bugPowerValue = new IntegerValue("BugPower", 5, 1, 5);
    private final IntegerValue lagPowerValue = new IntegerValue("FlagPower", 5, 3, 10);

    @Override
    public void onDisable() {
        if(modeValue.get().equalsIgnoreCase("lag")){
            if(mc.thePlayer == null)
                return;

            blink();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if(modeValue.get().equalsIgnoreCase("lag")){
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

    }

    @Override
    public void onEnable() {
        if(modeValue.get().equalsIgnoreCase("lag")){
            synchronized (positions) {
                positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() / 2), mc.thePlayer.posZ});
                positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
            }
        }

        if(modeValue.get().equalsIgnoreCase("bug")){
            mc.thePlayer.addChatMessage(new ChatComponentText("§c请按下空格执行高跳"));
        }
    }

    @EventTarget
    public void onJump(JumpEvent e){
        if(modeValue.get().equalsIgnoreCase("lag")){
            e.setMotion(e.getMotion() * lagPowerValue.get());

        }
    }

    @EventTarget
    public void onKey(KeyEvent e) {
        if(modeValue.get().equalsIgnoreCase("bug")){
            if (e.getKey() == 57) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + bugPowerValue.get(), mc.thePlayer.posZ, true));
                MovementUtils.forward(0.04D);
            }
        }

    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(modeValue.get().equalsIgnoreCase("lag")){
            synchronized(positions) {
                positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
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
    @Override
    public String getTag() {
        return modeValue.get();
    }
}



