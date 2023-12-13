// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import com.elementars.eclient.util.Wrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.play.client.CPacketPlayer;
import com.elementars.eclient.event.events.EventSendPacket;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.block.BlockLiquid;
import com.elementars.eclient.event.events.AddCollisionBoxToListEvent;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.EntityUtil;
import com.elementars.eclient.module.ModuleManager;
import com.elementars.eclient.module.Category;
import net.minecraft.util.math.AxisAlignedBB;
import com.elementars.eclient.module.Module;

public class Jesus extends Module
{
    private static final AxisAlignedBB WATER_WALK_AA;
    
    public Jesus() {
        super("Jesus", "Walk on water", 0, Category.MOVEMENT, true);
    }
    
    @Override
    public void onUpdate() {
        if (!ModuleManager.isModuleEnabled("Freecam") && EntityUtil.isInWater((Entity)Jesus.mc.player) && !Jesus.mc.player.isSneaking()) {
            Jesus.mc.player.motionY = 0.1;
            if (Jesus.mc.player.getRidingEntity() != null && !(Jesus.mc.player.getRidingEntity() instanceof EntityBoat)) {
                Jesus.mc.player.getRidingEntity().motionY = 0.3;
            }
        }
    }
    
    @EventTarget
    public void onCollision(final AddCollisionBoxToListEvent event) {
        if (Jesus.mc.player != null && event.getBlock() instanceof BlockLiquid && (EntityUtil.isDrivenByPlayer(event.getEntity()) || event.getEntity() == Jesus.mc.player) && !(event.getEntity() instanceof EntityBoat) && !Jesus.mc.player.isSneaking() && Jesus.mc.player.fallDistance < 3.0f && !EntityUtil.isInWater((Entity)Jesus.mc.player) && (EntityUtil.isAboveWater((Entity)Jesus.mc.player, false) || EntityUtil.isAboveWater(Jesus.mc.player.getRidingEntity(), false)) && isAboveBlock((Entity)Jesus.mc.player, event.getPos())) {
            final AxisAlignedBB axisalignedbb = Jesus.WATER_WALK_AA.offset(event.getPos());
            if (event.getEntityBox().intersects(axisalignedbb)) {
                event.getCollidingBoxes().add(axisalignedbb);
            }
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onPacket(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketPlayer && EntityUtil.isAboveWater((Entity)Jesus.mc.player, true) && !EntityUtil.isInWater((Entity)Jesus.mc.player) && !isAboveLand((Entity)Jesus.mc.player)) {
            final int ticks = Jesus.mc.player.ticksExisted % 2;
            if (ticks == 0) {
                final CPacketPlayer cPacketPlayer = (CPacketPlayer)event.getPacket();
                cPacketPlayer.y += 0.02;
            }
        }
    }
    
    private static boolean isAboveLand(final Entity entity) {
        if (entity == null) {
            return false;
        }
        final double y = entity.posY - 0.01;
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (Wrapper.getWorld().getBlockState(pos).getBlock().isFullBlock(Wrapper.getWorld().getBlockState(pos))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isAboveBlock(final Entity entity, final BlockPos pos) {
        return entity.posY >= pos.getY();
    }
    
    static {
        WATER_WALK_AA = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.99, 1.0);
    }
}
