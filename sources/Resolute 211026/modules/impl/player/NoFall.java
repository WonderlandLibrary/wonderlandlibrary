// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import java.util.Arrays;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.Resolute.events.impl.EventPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.util.List;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class NoFall extends Module
{
    public ModeSetting mode;
    private static final List<Double> BLOCK_HEIGHTS;
    private float fallDist;
    private boolean packetModify;
    private boolean needSpoof;
    private int packet1Count;
    
    public NoFall() {
        super("NoFall", 0, "Removes fall damage", Category.PLAYER);
        this.mode = new ModeSetting("Mode", "Ground Spoof", new String[] { "Ground Spoof", "NCP", "Verus", "Packet", "None", "Edit", "Rounded", "Dev" });
        this.fallDist = 0.0f;
        this.packetModify = false;
        this.needSpoof = false;
        this.packet1Count = 0;
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEnable() {
        this.packetModify = false;
        this.needSpoof = false;
        this.packet1Count = 0;
    }
    
    @Override
    public void onDisable() {
        NoFall.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.mode.getMode());
        if (e instanceof EventMotion) {
            final EventMotion eventMotion = (EventMotion)e;
            if (this.mode.is("Dev")) {
                if (MovementUtils.isOverVoid()) {
                    return;
                }
                if (NoFall.mc.thePlayer.fallDistance >= 2.5f) {
                    eventMotion.setOnGround(NoFall.mc.thePlayer.ticksExisted % 2 == 0);
                }
            }
            if (this.mode.is("Verus")) {
                if (NoFall.mc.thePlayer.fallDistance - NoFall.mc.thePlayer.motionY > 3.0) {
                    NoFall.mc.thePlayer.motionY = 0.0;
                    NoFall.mc.thePlayer.fallDistance = 0.0f;
                    final EntityPlayerSP thePlayer = NoFall.mc.thePlayer;
                    thePlayer.motionX *= 0.6;
                    final EntityPlayerSP thePlayer2 = NoFall.mc.thePlayer;
                    thePlayer2.motionZ *= 0.6;
                    this.needSpoof = true;
                }
                if (NoFall.mc.thePlayer.fallDistance / 3.0f > this.packet1Count) {
                    this.packet1Count = (int)(NoFall.mc.thePlayer.fallDistance / 3.0f);
                    this.packetModify = true;
                }
                if (NoFall.mc.thePlayer.onGround) {
                    this.packet1Count = 0;
                }
            }
            if (this.mode.is("NCP")) {
                final BlockPos blockPos = new BlockPos(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY - 6.0, NoFall.mc.thePlayer.posZ);
                final Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
                final BlockPos blockPos2 = new BlockPos(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY - 5.0, NoFall.mc.thePlayer.posZ);
                final Block block2 = Minecraft.getMinecraft().theWorld.getBlockState(blockPos2).getBlock();
                final BlockPos blockPos3 = new BlockPos(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY - 4.0, NoFall.mc.thePlayer.posZ);
                final Block block3 = Minecraft.getMinecraft().theWorld.getBlockState(blockPos3).getBlock();
                if ((block != Blocks.air || block2 != Blocks.air || block3 != Blocks.air) && NoFall.mc.thePlayer.fallDistance > 2.0f) {
                    NoFall.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY + 0.1, NoFall.mc.thePlayer.posZ, false));
                    NoFall.mc.thePlayer.motionY = -10.0;
                    NoFall.mc.thePlayer.fallDistance = 0.0f;
                }
            }
            Label_0703: {
                if (this.mode.is("Edit")) {
                    if (this.fallDist > NoFall.mc.thePlayer.fallDistance) {
                        this.fallDist = 0.0f;
                    }
                    if (NoFall.mc.thePlayer.motionY < 0.0 && NoFall.mc.thePlayer.fallDistance > 2.124 && !this.checkVoid(NoFall.mc.thePlayer) && this.isBlockUnder() && !NoFall.mc.thePlayer.isSpectator() && !NoFall.mc.thePlayer.capabilities.allowFlying) {
                        final double fallingDist = NoFall.mc.thePlayer.fallDistance - this.fallDist;
                        final double motionY;
                        final double realDist;
                        if ((realDist = fallingDist + -(((motionY = NoFall.mc.thePlayer.motionY) - 0.08) * 0.9800000190734863)) >= 3.0) {
                            eventMotion.setOnGround(true);
                            break Label_0703;
                        }
                    }
                    return;
                }
            }
            Label_0899: {
                if (this.mode.is("Watchdog")) {
                    if (this.fallDist > NoFall.mc.thePlayer.fallDistance) {
                        this.fallDist = 0.0f;
                    }
                    if (NoFall.mc.thePlayer.motionY < 0.0 && NoFall.mc.thePlayer.fallDistance > 2.124 && !this.checkVoid(NoFall.mc.thePlayer) && this.isBlockUnder() && !NoFall.mc.thePlayer.isSpectator() && !NoFall.mc.thePlayer.capabilities.allowFlying) {
                        final double fallingDist = NoFall.mc.thePlayer.fallDistance - this.fallDist;
                        final double motionY;
                        final double realDist;
                        if ((realDist = fallingDist + -(((motionY = NoFall.mc.thePlayer.motionY) - 0.08) * 0.9800000190734863)) >= 3.0) {
                            NoFall.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
                            this.fallDist = NoFall.mc.thePlayer.fallDistance;
                            break Label_0899;
                        }
                    }
                    return;
                }
            }
            if (this.mode.is("Rounded")) {
                final double minFallDist = MovementUtils.getMinFallDist();
                if (NoFall.mc.thePlayer.fallDistance % minFallDist == 0.0) {
                    final double currentYOffset = MovementUtils.getBlockHeight();
                    final double n;
                    NoFall.BLOCK_HEIGHTS.sort((h, h1) -> (int)((Math.abs(n - h) - Math.abs(n - h1)) * 10.0));
                    final double yPos = (int)eventMotion.getY() + NoFall.BLOCK_HEIGHTS.get(0);
                    eventMotion.setY(yPos);
                }
            }
            if (e.isPre() && this.mode.is("Packet") && NoFall.mc.thePlayer.fallDistance > 3.0f) {
                NoFall.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
            }
            if (this.mode.is("Ground Spoof") && NoFall.mc.thePlayer.fallDistance > 3.0f) {
                eventMotion.setOnGround(true);
                NoFall.mc.thePlayer.fallDistance = 0.0f;
            }
        }
        if (e instanceof EventPacket && ((EventPacket)e).getPacket() instanceof C03PacketPlayer) {
            final C03PacketPlayer packet = ((EventPacket)e).getPacket();
            if (this.mode.is("Verus") && this.needSpoof) {
                packet.onGround = true;
                this.needSpoof = false;
            }
        }
    }
    
    private boolean checkVoid(final EntityLivingBase entity) {
        for (int b = -1; b <= 0; b = (byte)(b + 1)) {
            for (int b2 = -1; b2 <= 0; b2 = (byte)(b2 + 1)) {
                if (this.isVoid(b, b2, entity)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isVoid(final int X, final int Z, final EntityLivingBase entity) {
        if (NoFall.mc.thePlayer.posY < 0.0) {
            return true;
        }
        for (int off = 0; off < (int)entity.posY + 2; off += 2) {
            final AxisAlignedBB bb = entity.getEntityBoundingBox().offset(X, -off, Z);
            if (!NoFall.mc.theWorld.getCollidingBoundingBoxes(entity, bb).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isBlockUnder() {
        for (int offset = 0; offset < NoFall.mc.thePlayer.posY + NoFall.mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = NoFall.mc.thePlayer.getEntityBoundingBox().offset(0.0, -offset, 0.0);
            if (NoFall.mc.theWorld.getCollidingBoundingBoxes(NoFall.mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    static {
        BLOCK_HEIGHTS = Arrays.asList(0.015625, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0);
    }
}
