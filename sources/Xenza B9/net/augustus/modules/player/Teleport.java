// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.player;

import net.augustus.events.EventSendPacket;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.augustus.events.EventReadPacket;
import net.augustus.events.EventUpdate;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.client.network.NetHandlerPlayClient;
import java.util.Iterator;
import net.augustus.utils.skid.azura.Path;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.utils.skid.azura.PathUtil;
import net.minecraft.entity.Entity;
import net.augustus.utils.skid.azura.CustomVec3;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.skid.azura.RaytraceUtil;
import net.minecraft.util.MovingObjectPosition;
import net.augustus.events.EventPreMotion;
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
        this.mode = new StringValue(0, "Mode", this, "Vulcan", new String[] { "Karhu", "Vulcan", "Vanilla", "SpartanB453" });
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
    public void onMove(final EventPreMotion e) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        if (Teleport.mc.gameSettings.keyBindAttack.isKeyDown() && Teleport.mc.objectMouseOver != null && Teleport.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.xyz[0] == Double.POSITIVE_INFINITY) {
            final BlockPos blockPos = Teleport.mc.objectMouseOver.getBlockPos();
            final Block block = Teleport.mc.theWorld.getBlockState(blockPos).getBlock();
            this.xyz = new double[] { Teleport.mc.objectMouseOver.getBlockPos().getX() + 0.5, Teleport.mc.objectMouseOver.getBlockPos().getY() + block.getBlockBoundsMaxY(), Teleport.mc.objectMouseOver.getBlockPos().getZ() + 0.5 };
            this.shouldTeleport = true;
            this.timeHelper.reset();
        }
        if (this.shouldTeleport) {
            final String selected;
            final String var4 = selected = this.mode.getSelected();
            switch (selected) {
                case "SpartanB453": {
                    final MovingObjectPosition position = RaytraceUtil.rayTrace(200.0, e.getYaw(), e.getPitch());
                    if (!Teleport.mc.gameSettings.keyBindAttack.pressed || position.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || Teleport.mc.theWorld.isAirBlock(position.getBlockPos())) {
                        break;
                    }
                    Teleport.mc.gameSettings.keyBindAttack.pressed = false;
                    if (!Teleport.mc.thePlayer.onGround) {
                        ChatUtil.sendChat("Please stand on ground when selecting...");
                        return;
                    }
                    final CustomVec3 target = new CustomVec3(position.getBlockPos());
                    target.setY(Teleport.mc.thePlayer.posY);
                    final Path path = PathUtil.findPath(new CustomVec3(Teleport.mc.thePlayer), target, 0.15);
                    for (final CustomVec3 v : path.getAdditionalVectors()) {
                        final NetHandlerPlayClient sendQueue = Teleport.mc.thePlayer.sendQueue;
                        final double x = v.getX();
                        e.x = x;
                        final double y = v.getY();
                        e.y = y;
                        sendQueue.addToSendQueueDirect(new C03PacketPlayer.C04PacketPlayerPosition(x, y, e.z = v.getZ(), true));
                    }
                    Teleport.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer.C04PacketPlayerPosition(e.x, e.y + 5000.0, e.z, true));
                    Teleport.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer.C04PacketPlayerPosition(e.x, e.y, e.z, true));
                    break;
                }
            }
        }
        if (this.autoDisable.getBoolean() && this.teleported) {
            this.toggle();
        }
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
            final String selected;
            final String var4 = selected = this.mode.getSelected();
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
        final String var3 = this.mode.getSelected();
        byte var4 = -1;
        switch (var3.hashCode()) {
            case -1721492669: {
                if (var3.equals("Vulcan")) {
                    var4 = 0;
                    break;
                }
                break;
            }
        }
        switch (var4) {
            case 0: {
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
        final String var3 = this.mode.getSelected();
        byte var4 = -1;
        switch (var3.hashCode()) {
            case -1721492669: {
                if (var3.equals("Vulcan")) {
                    var4 = 0;
                    break;
                }
                break;
            }
        }
        switch (var4) {
            case 0: {
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
