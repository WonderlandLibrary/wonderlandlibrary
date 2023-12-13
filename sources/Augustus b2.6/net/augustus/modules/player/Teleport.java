// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.augustus.events.EventSendPacket;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.augustus.events.EventReadPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.augustus.events.EventUpdate;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class Teleport extends Module
{
    private final TimeHelper timeHelper;
    public StringValue mode;
    public BooleanValue autoDisable;
    private double[] xyz;
    private boolean shouldTeleport;
    private boolean teleported;
    
    public Teleport() {
        super("Teleport", new Color(141, 232, 5), Categorys.PLAYER);
        this.timeHelper = new TimeHelper();
        this.mode = new StringValue(0, "Mode", this, "Vulcan", new String[] { "Karhu", "Vulcan", "Vanilla" });
        this.autoDisable = new BooleanValue(1, "AutoDisable", this, true);
        this.xyz = new double[3];
        this.teleported = false;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.xyz = new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
        this.shouldTeleport = false;
        this.teleported = false;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.xyz = new double[3];
        this.shouldTeleport = false;
    }
    
    @EventTarget
    public void onEventTick(final EventUpdate eventUpdate) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        if (Teleport.mc.gameSettings.keyBindAttack.isKeyDown() && Teleport.mc.objectMouseOver != null && Teleport.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.xyz[0] == Double.POSITIVE_INFINITY) {
            final BlockPos blockPos = Teleport.mc.objectMouseOver.getBlockPos();
            final Block block = Teleport.mc.theWorld.getBlockState(blockPos).getBlock();
            this.xyz = new double[] { Teleport.mc.objectMouseOver.getBlockPos().getX() + 0.5, Teleport.mc.objectMouseOver.getBlockPos().getY() + block.getBlockBoundsMaxY(), Teleport.mc.objectMouseOver.getBlockPos().getZ() + 0.5 };
            this.shouldTeleport = true;
            this.timeHelper.reset();
        }
        if (this.shouldTeleport) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Karhu": {
                    if (!Teleport.mc.thePlayer.onGround) {
                        Teleport.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.xyz[0], this.xyz[1] + 0.3, this.xyz[2], true));
                        Teleport.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.xyz[0], this.xyz[1] + 0.3, this.xyz[2], false));
                        this.shouldTeleport = false;
                        this.teleported = true;
                        this.xyz = new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
                    }
                    if (Teleport.mc.thePlayer.onGround) {
                        Teleport.mc.thePlayer.jump();
                        break;
                    }
                    break;
                }
                case "Vanilla": {
                    Teleport.mc.thePlayer.setPosition(this.xyz[0], this.xyz[1] + 0.3, this.xyz[2]);
                    Teleport.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.xyz[0], this.xyz[1] + 0.3, this.xyz[2], false));
                    this.shouldTeleport = false;
                    this.teleported = true;
                    break;
                }
            }
        }
        if (this.autoDisable.getBoolean() && this.teleported) {
            this.toggle();
        }
    }
    
    @EventTarget
    public void onEventReadPacket(final EventReadPacket eventReadPacket) {
        final Packet packet = eventReadPacket.getPacket();
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Vulcan": {
                if (!(packet instanceof S08PacketPlayerPosLook) || !this.shouldTeleport) {
                    break;
                }
                final S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)packet;
                if (s08PacketPlayerPosLook.getX() == this.xyz[0] && s08PacketPlayerPosLook.getY() == this.xyz[1] && s08PacketPlayerPosLook.getZ() == this.xyz[2]) {
                    this.shouldTeleport = false;
                    this.teleported = true;
                    this.xyz = new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Vulcan": {
                if (!(packet instanceof C03PacketPlayer) || !this.shouldTeleport) {
                    break;
                }
                final C03PacketPlayer c03PacketPlayer = (C03PacketPlayer)packet;
                c03PacketPlayer.setX(this.xyz[0]);
                c03PacketPlayer.setY(this.xyz[1]);
                c03PacketPlayer.setZ(this.xyz[2]);
                Teleport.mc.thePlayer.setPosition(this.xyz[0], this.xyz[1], this.xyz[2]);
                Teleport.mc.thePlayer.onGround = true;
                if (this.timeHelper.reached(1000L)) {
                    this.shouldTeleport = false;
                    this.teleported = true;
                    this.xyz = new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
                    break;
                }
                break;
            }
        }
    }
}
