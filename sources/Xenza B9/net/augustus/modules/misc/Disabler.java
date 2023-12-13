// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.misc;

import net.augustus.utils.PlayerUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.augustus.events.EventReadPacket;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.status.client.C01PacketPing;
import net.augustus.utils.RandomUtil;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.augustus.events.EventSendPacket;
import java.util.Iterator;
import java.util.function.Predicate;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.augustus.events.EventRender3D;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventUpdate;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.utils.custompackets.CustomC00PacketKeepAlive;
import java.util.ArrayList;
import net.augustus.modules.Module;

public class Disabler extends Module
{
    private final ArrayList<CustomC00PacketKeepAlive> keepAlivePackets;
    private final int counter = 0;
    public BooleanValue pingSpoof;
    public BooleanValue royalPixels;
    public BooleanValue minemenStrafe;
    public BooleanValue universoCraft;
    public BooleanValue spectate;
    public BooleanValue keepAlive;
    public BooleanValue ping;
    public BooleanValue entityAction;
    public BooleanValue playerAbilities;
    public BooleanValue confirmTransaction;
    public BooleanValue ncpTimer;
    public BooleanValue hac;
    public BooleanValue noSprint;
    public BooleanValue blcSpoof;
    public BooleanValue aac5;
    public BooleanValue antiVanillaEumel;
    public DoubleValue delay;
    private boolean disabling;
    
    public Disabler() {
        super("Disabler", new Color(73, 127, 163), Categorys.MISC);
        this.keepAlivePackets = new ArrayList<CustomC00PacketKeepAlive>();
        this.pingSpoof = new BooleanValue(0, "PingSpoof", this, false);
        this.royalPixels = new BooleanValue(8, "RoyalPixels", this, false);
        this.minemenStrafe = new BooleanValue(9, "MinemenStrafe", this, false);
        this.universoCraft = new BooleanValue(7, "UniversoCraft", this, false);
        this.spectate = new BooleanValue(0, "Spectate", this, false);
        this.keepAlive = new BooleanValue(2, "C00PacketKeepAlive", this, false);
        this.ping = new BooleanValue(3, "C01PacketPing", this, false);
        this.entityAction = new BooleanValue(4, "C0BPacketEntityAction", this, false);
        this.playerAbilities = new BooleanValue(5, "C13PacketPlayerAbilities", this, false);
        this.confirmTransaction = new BooleanValue(7, "C0FPacketConfirmTransaction", this, false);
        this.ncpTimer = new BooleanValue(132, "NCPTimerSemi", this, false);
        this.hac = new BooleanValue(696969, "HAC", this, false);
        this.noSprint = new BooleanValue(7, "NoSprint", this, false);
        this.blcSpoof = new BooleanValue(7, "BLC-Spoof", this, false);
        this.aac5 = new BooleanValue(7, "AAC5", this, false);
        this.antiVanillaEumel = new BooleanValue(7, "NoFlag", this, false);
        this.delay = new DoubleValue(1, "Delay", this, 1000.0, 1.0, 4000.0, 0);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.keepAlivePackets.clear();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (!this.keepAlivePackets.isEmpty()) {
            this.keepAlivePackets.clear();
        }
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        if (this.ncpTimer.getBoolean()) {
            Disabler.mc.thePlayer.setPosition(Disabler.mc.thePlayer.posX, Disabler.mc.thePlayer.posY - 0.017, Disabler.mc.thePlayer.posZ);
            Disabler.mc.thePlayer.motionY = 0.019;
        }
        if (this.hac.getBoolean() && Disabler.mc.thePlayer.ticksExisted % 10 == 0) {
            Disabler.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(Disabler.mc.thePlayer.posX, Disabler.mc.thePlayer.posY - 11.0, Disabler.mc.thePlayer.posZ, Disabler.mc.thePlayer.cameraYaw, Disabler.mc.thePlayer.cameraPitch, true));
        }
        if (this.aac5.getBoolean() && !Disabler.mc.isIntegratedServerRunning()) {
            Disabler.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(Disabler.mc.thePlayer.posX, Disabler.mc.thePlayer.posY - 1.0E159, Disabler.mc.thePlayer.posZ + 10.0, 0.0f, 0.0f, true));
            Disabler.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(Disabler.mc.thePlayer.posX, Disabler.mc.thePlayer.posY, Disabler.mc.thePlayer.posZ, 0.0f, 0.0f, true));
        }
        if (this.royalPixels.getBoolean()) {
            final C13PacketPlayerAbilities capabilities = new C13PacketPlayerAbilities();
            capabilities.setAllowFlying(true);
            capabilities.setFlying(true);
            Disabler.mc.thePlayer.sendQueue.addToSendQueue(capabilities);
        }
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        if (this.pingSpoof.getBoolean() && Disabler.mc.thePlayer != null && !this.keepAlivePackets.isEmpty()) {
            final ArrayList<CustomC00PacketKeepAlive> toRemove = new ArrayList<CustomC00PacketKeepAlive>();
            for (final CustomC00PacketKeepAlive packet : this.keepAlivePackets) {
                if (packet.getTime() < System.currentTimeMillis()) {
                    Disabler.mc.thePlayer.sendQueue.addToSendQueueDirect(new C00PacketKeepAlive(packet.getKey()));
                    toRemove.add(packet);
                }
            }
            this.keepAlivePackets.removeIf(toRemove::contains);
        }
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        if (this.universoCraft.getBoolean()) {
            if (packet instanceof S07PacketRespawn) {
                this.disabling = true;
            }
            else if (packet instanceof C02PacketUseEntity) {
                this.disabling = false;
            }
            else if (packet instanceof C03PacketPlayer && Disabler.mc.thePlayer.ticksExisted <= 10) {
                this.disabling = true;
            }
            else if (packet instanceof C0FPacketConfirmTransaction && this.disabling && Disabler.mc.thePlayer.ticksExisted < 350) {
                ((C0FPacketConfirmTransaction)packet).setUid((short)((Disabler.mc.thePlayer.ticksExisted % 2 == 0) ? -32768 : 32767));
            }
        }
        if (this.noSprint.getBoolean() && packet instanceof C0BPacketEntityAction && ((C0BPacketEntityAction)packet).getAction() == C0BPacketEntityAction.Action.START_SPRINTING) {
            eventSendPacket.setCanceled(true);
        }
        if (this.pingSpoof.getBoolean() && packet instanceof C00PacketKeepAlive) {
            final C00PacketKeepAlive c00PacketKeepAlive = (C00PacketKeepAlive)packet;
            this.keepAlivePackets.add(new CustomC00PacketKeepAlive(c00PacketKeepAlive.getKey(), (long)(System.currentTimeMillis() + this.delay.getValue() + RandomUtil.nextLong(0L, 200L))));
            eventSendPacket.setCanceled(true);
        }
        if (this.minemenStrafe.getBoolean() && packet instanceof C0FPacketConfirmTransaction && Disabler.mc.thePlayer.ticksExisted % 3 == 0) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C00PacketKeepAlive && this.keepAlive.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C01PacketPing && this.ping.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C0BPacketEntityAction && this.entityAction.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C13PacketPlayerAbilities && this.playerAbilities.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C0FPacketConfirmTransaction && this.confirmTransaction.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C03PacketPlayer && this.spectate.getBoolean()) {
            Disabler.mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(Disabler.mc.thePlayer.getUniqueID()));
        }
        if (packet instanceof C17PacketCustomPayload && this.blcSpoof.getBoolean()) {
            final C17PacketCustomPayload c17 = (C17PacketCustomPayload)eventSendPacket.getPacket();
            if (c17.getChannelName().equals("MC|Brand")) {
                eventSendPacket.setPacket(new C17PacketCustomPayload(c17.getChannelName(), new PacketBuffer(Unpooled.buffer()).writeString("blc")));
            }
        }
    }
    
    @EventTarget
    public void onRecv(final EventReadPacket event) {
        if (this.antiVanillaEumel.getBoolean() && event.getPacket() instanceof S08PacketPlayerPosLook && Disabler.mc.thePlayer.ticksExisted > 20) {
            PlayerUtil.sendChat("Sexed: " + Disabler.mc.thePlayer.ticksExisted);
            final double x = ((S08PacketPlayerPosLook)event.getPacket()).getX() - Disabler.mc.thePlayer.posX;
            final double y = ((S08PacketPlayerPosLook)event.getPacket()).getY() - Disabler.mc.thePlayer.posY;
            final double z = ((S08PacketPlayerPosLook)event.getPacket()).getZ() - Disabler.mc.thePlayer.posZ;
            final double diff = Math.sqrt(x * x + y * y + z * z);
            if (diff <= 8.0) {
                event.setCanceled(true);
                Disabler.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook)event.getPacket()).getX(), ((S08PacketPlayerPosLook)event.getPacket()).getY(), ((S08PacketPlayerPosLook)event.getPacket()).getZ(), ((S08PacketPlayerPosLook)event.getPacket()).getYaw(), ((S08PacketPlayerPosLook)event.getPacket()).getPitch(), true));
            }
        }
    }
}
