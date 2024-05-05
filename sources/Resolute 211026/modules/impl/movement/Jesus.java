// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventLiquidBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.util.misc.TimerUtils;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Jesus extends Module
{
    public ModeSetting mode;
    TimerUtils timerUtils;
    boolean nextTick;
    
    public Jesus() {
        super("Jesus", 0, "Allows you to walk on water", Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", "Simple", new String[] { "Simple", "NCP", "Matrix" });
        this.timerUtils = new TimerUtils();
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEnable() {
        Jesus.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onDisable() {
        Jesus.mc.timer.timerSpeed = 1.0f;
        Jesus.mc.thePlayer.speedInAir = 0.02f;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion) {
            final EventMotion event = (EventMotion)e;
            if (this.mode.is("NCP") && Jesus.mc.thePlayer.onGround && !collideBlock(new AxisAlignedBB(Jesus.mc.thePlayer.getEntityBoundingBox().maxX, Jesus.mc.thePlayer.getEntityBoundingBox().maxY, Jesus.mc.thePlayer.getEntityBoundingBox().maxZ, Jesus.mc.thePlayer.getEntityBoundingBox().minX, Jesus.mc.thePlayer.getEntityBoundingBox().minY - 0.01, Jesus.mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid)) {
                Jesus.mc.timer.timerSpeed = 1.0f;
            }
            if (this.mode.is("Solid") && e.isPre()) {
                final boolean sh = this.shouldJesus();
                if (Jesus.mc.thePlayer.isInWater() && !Jesus.mc.thePlayer.isSneaking() && sh) {
                    Jesus.mc.thePlayer.motionY = 0.09;
                }
                if (isOnLiquid(0.001) && isTotalOnLiquid(0.001) && Jesus.mc.thePlayer.onGround && !Jesus.mc.thePlayer.isInWater()) {
                    event.setY(event.getY() + ((Jesus.mc.thePlayer.ticksExisted % 2 == 0) ? 1.0E-10 : -1.0E-12));
                }
            }
            if (this.mode.is("Simple")) {
                if (e instanceof EventLiquidBB && !Jesus.mc.thePlayer.isInWater() && Jesus.mc.thePlayer.fallDistance < 4.0f && !Jesus.mc.thePlayer.isSneaking()) {
                    ((EventLiquidBB)e).setAxisAlignedBB(new AxisAlignedBB(((EventLiquidBB)e).getPos().getX(), ((EventLiquidBB)e).getPos().getY(), ((EventLiquidBB)e).getPos().getZ(), ((EventLiquidBB)e).getPos().getX() + 1, ((EventLiquidBB)e).getPos().getY() + 1, ((EventLiquidBB)e).getPos().getZ() + 1));
                }
                if (Jesus.mc.thePlayer.isInWater() && !Jesus.mc.thePlayer.isSneaking()) {
                    Jesus.mc.thePlayer.motionY = 0.2;
                }
            }
            if (this.mode.is("Matrix") && Jesus.mc.thePlayer.isInWater() && !Jesus.mc.gameSettings.keyBindJump.pressed && !Jesus.mc.gameSettings.keyBindSneak.pressed) {
                Jesus.mc.thePlayer.motionY = 0.0;
                if (Jesus.mc.gameSettings.keyBindForward.pressed) {
                    final double dir = MovementUtils.getDirection();
                    final EntityPlayerSP thePlayer = Jesus.mc.thePlayer;
                    thePlayer.motionX += -Math.sin(dir) * 0.01;
                    final EntityPlayerSP thePlayer2 = Jesus.mc.thePlayer;
                    thePlayer2.motionZ += Math.cos(dir) * 0.01;
                    if (this.timerUtils.hasTimeElapsed(200L, true)) {
                        Jesus.mc.timer.timerSpeed = 1.25f;
                    }
                    else {
                        Jesus.mc.timer.timerSpeed = 1.0f;
                    }
                }
            }
        }
        if (e instanceof EventPacket && this.mode.is("NCP")) {
            if (Jesus.mc.thePlayer == null) {
                return;
            }
            if (((EventPacket)e).getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer packetPlayer = ((EventPacket)e).getPacket();
                if (collideBlock(new AxisAlignedBB(Jesus.mc.thePlayer.getEntityBoundingBox().maxX, Jesus.mc.thePlayer.getEntityBoundingBox().maxY, Jesus.mc.thePlayer.getEntityBoundingBox().maxZ, Jesus.mc.thePlayer.getEntityBoundingBox().minX, Jesus.mc.thePlayer.getEntityBoundingBox().minY - 0.01, Jesus.mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid)) {
                    Jesus.mc.thePlayer.motionY = 0.4;
                    this.nextTick = !this.nextTick;
                    Jesus.mc.timer.timerSpeed = 1.0f;
                    if (this.nextTick) {
                        final C03PacketPlayer c03PacketPlayer = packetPlayer;
                        c03PacketPlayer.y -= 0.001;
                    }
                }
                else {
                    Jesus.mc.timer.timerSpeed = 1.0f;
                }
            }
        }
        if (e instanceof EventLiquidBB) {
            final EventLiquidBB event2 = (EventLiquidBB)e;
            if (this.mode.is("Solid")) {
                final int n = -1;
                if (event2.getPos().getY() + 0.9 < Jesus.mc.thePlayer.boundingBox.minY && n <= 4) {
                    event2.setAxisAlignedBB(new AxisAlignedBB(event2.getPos().getX(), event2.getPos().getY(), event2.getPos().getZ(), event2.getPos().getX() + 1, event2.getPos().getY() + 1, event2.getPos().getZ() + 1));
                    event2.setCancelled(this.shouldSetBoundingBox());
                }
            }
        }
    }
    
    private boolean shouldSetBoundingBox() {
        return !Jesus.mc.thePlayer.isSneaking() && Jesus.mc.thePlayer.fallDistance < 12.0f;
    }
    
    public static boolean collideBlock(final AxisAlignedBB axisAlignedBB, final Collidable collide) {
        for (int x = MathHelper.floor_double(Jesus.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Jesus.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(Jesus.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Jesus.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = getBlockAtPos(new BlockPos(x, axisAlignedBB.minY, z));
                if (!collide.collideBlock(block)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static Block getBlockAtPos(final BlockPos inBlockPos) {
        final IBlockState s = Jesus.mc.theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }
    
    public static boolean isOnLiquid(final double profondeur) {
        boolean onLiquid = false;
        if (Jesus.mc.theWorld.getBlockState(new BlockPos(Jesus.mc.thePlayer.posX, Jesus.mc.thePlayer.posY - profondeur, Jesus.mc.thePlayer.posZ)).getBlock().getMaterial().isLiquid()) {
            onLiquid = true;
        }
        return onLiquid;
    }
    
    public static boolean isTotalOnLiquid(final double profondeur) {
        for (double x = Jesus.mc.thePlayer.boundingBox.minX; x < Jesus.mc.thePlayer.boundingBox.maxX; x += 0.009999999776482582) {
            for (double z = Jesus.mc.thePlayer.boundingBox.minZ; z < Jesus.mc.thePlayer.boundingBox.maxZ; z += 0.009999999776482582) {
                final Block block = Jesus.mc.theWorld.getBlockState(new BlockPos(x, Jesus.mc.thePlayer.posY - profondeur, z)).getBlock();
                if (!(block instanceof BlockLiquid) && !(block instanceof BlockAir)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    boolean shouldJesus() {
        final double x = Jesus.mc.thePlayer.posX;
        final double y = Jesus.mc.thePlayer.posY;
        final double z = Jesus.mc.thePlayer.posZ;
        final ArrayList<BlockPos> pos = new ArrayList<BlockPos>(Arrays.asList(new BlockPos(x + 0.3, y, z + 0.3), new BlockPos(x - 0.3, y, z + 0.3), new BlockPos(x + 0.3, y, z - 0.3), new BlockPos(x - 0.3, y, z - 0.3)));
        for (final BlockPos po : pos) {
            if (!(Jesus.mc.theWorld.getBlockState(po).getBlock() instanceof BlockLiquid)) {
                continue;
            }
            if (Jesus.mc.theWorld.getBlockState(po).getProperties().get((Object)BlockLiquid.LEVEL) instanceof Integer && (int)Jesus.mc.theWorld.getBlockState(po).getProperties().get((Object)BlockLiquid.LEVEL) <= 4) {
                return true;
            }
        }
        return false;
    }
    
    interface Collidable
    {
        boolean collideBlock(final Block p0);
    }
}
