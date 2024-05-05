package club.async.module.impl.player;

import club.async.event.impl.EventPacket;
import club.async.event.impl.EventPreUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.ModeSetting;
import club.async.util.ChatUtil;
import club.async.util.TimeUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import rip.hippo.lwjeb.annotation.Handler;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@ModuleInfo(name = "Disabler", description = "Disable", category = Category.PLAYER)
public class Disabler extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, new String[] {"Verus", "VerusCombat"}, "Verus");

    private final Queue <Packet<?>> packetQueue = new ConcurrentLinkedDeque <> ();
    private final TimeUtil timer = new TimeUtil();
    private boolean expectedTeleport;
    public ArrayList<Packet<?>> packets = new ArrayList<>();
    @Handler
    public void update(EventPreUpdate event) {
        setExtraTag(mode.getCurrMode());
        if(mode.is("Verus")) {
            if (!this.shouldRun()) {
                this.expectedTeleport = false;
                this.timer.reset();
                this.packetQueue.clear();
                return;
            }

            if (this.timer.hasTimePassed (260L)) {
                this.timer.reset();
                ChatUtil.addChatMessage( String.valueOf ( expectedTeleport + " --> " + packetQueue.size ()  ) );
                if (!this.packetQueue.isEmpty()) {
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacketSilent (this.packetQueue.poll());
                }
            }
        }
        if(mode.is("veruscombat")) {
            if(mc.thePlayer.ticksExisted % 180 == 0) {
                while(packets.size() > 22) {
                    mc.thePlayer.sendQueue.addToSendQueueSilent(packets.get(0));
                    packets.remove(0);
                }
            }
        }

    }

    @Handler
    public void packet(EventPacket event) {
        if(mode.is("Verus")){
            if (!this.shouldRun()) return;

            if (event.getPacket() instanceof C0FPacketConfirmTransaction
                    || event.getPacket() instanceof C00PacketKeepAlive) {

                short action = -1;

                if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                    action = ((C0FPacketConfirmTransaction) event.getPacket()).getUid();
                }

                if (action != -1 && this.isInventory(action)) return;

                event.setCancelled(true);
                this.packetQueue.add(event.getPacket());
            }

            if (event.getPacket() instanceof C03PacketPlayer) {

                C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) event.getPacket();


                if (mc.thePlayer.ticksExisted % 25 == 0) {
                    this.expectedTeleport = true;

                    c03PacketPlayer.setMoving(false);
                    c03PacketPlayer.setY(-0.015625);
                    c03PacketPlayer.setOnGround(false);
                }
            }

            if (event.getPacket() instanceof S08PacketPlayerPosLook && this.expectedTeleport) {
                S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook) event.getPacket();
                this.expectedTeleport = false;

                event.setCancelled(true);

                mc.thePlayer.sendQueue.getNetworkManager().sendPacketSilent (new C03PacketPlayer
                        .C06PacketPlayerPosLook(s08PacketPlayerPosLook.getX(),
                        s08PacketPlayerPosLook.getY(),
                        s08PacketPlayerPosLook.getZ(),
                        s08PacketPlayerPosLook.getYaw(),
                        s08PacketPlayerPosLook.getPitch(), true));
            }
        }
        if( mode.is("veruscombat")) {
            if (event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive) {
                event.setCancelled(true);
                packets.add(event.getPacket());
            }
            if( mode.is("veruscombat") && mode.is ( "Verus" )) {
                if (event.getPacket () instanceof C0BPacketEntityAction) {
                  
                }
            }
        }
    }
    boolean shouldRun() {
        return mc.thePlayer != null && mc.thePlayer.ticksExisted > 5;
    }

    boolean isInventory(short action) {
        return action > 0 && action < 100;
    }
}
