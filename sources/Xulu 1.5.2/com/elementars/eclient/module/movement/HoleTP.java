// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.event.events.LocalPlayerUpdateEvent;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.module.Category;
import net.minecraft.util.math.BlockPos;
import com.elementars.eclient.module.Module;

public class HoleTP extends Module
{
    private static final float DEFAULT_STEP_HEIGHT = 0.6f;
    float lastStep;
    private final BlockPos[] xd;
    private boolean wasOnGround;
    
    public HoleTP() {
        super("HoleTP", "Reverse step for holes", 0, Category.MOVEMENT, true);
        this.xd = new BlockPos[] { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, -1, 0) };
        this.wasOnGround = false;
    }
    
    @Override
    public void onEnable() {
        if (HoleTP.mc.player != null) {
            this.wasOnGround = HoleTP.mc.player.onGround;
        }
    }
    
    @Override
    public void onDisable() {
        if (HoleTP.mc.player != null) {
            HoleTP.mc.player.stepHeight = 0.6f;
        }
        if (HoleTP.mc.player != null && HoleTP.mc.player.getRidingEntity() != null) {
            HoleTP.mc.player.getRidingEntity().stepHeight = 1.0f;
        }
    }
    
    private void updateStepHeight(final EntityPlayer player) {
        player.stepHeight = (player.onGround ? 1.2f : 0.6f);
    }
    
    private boolean shouldUnstep(final BlockPos pos) {
        boolean should = true;
        for (final BlockPos position : this.xd) {
            if (HoleTP.mc.world.getBlockState(pos.add((Vec3i)position)).getBlock() != Blocks.BEDROCK && HoleTP.mc.world.getBlockState(pos.add((Vec3i)position)).getBlock() != Blocks.OBSIDIAN) {
                should = false;
            }
        }
        return should;
    }
    
    private void unstep(final EntityPlayer player) {
        if (!this.shouldUnstep(new BlockPos(player.getPositionVector()))) {
            return;
        }
        final AxisAlignedBB range = player.getEntityBoundingBox().expand(0.0, -1.2000000476837158, 0.0).contract(0.0, (double)player.height, 0.0);
        if (!player.world.collidesWithAnyBlock(range)) {
            return;
        }
        final List<AxisAlignedBB> collisionBoxes = (List<AxisAlignedBB>)player.world.getCollisionBoxes((Entity)player, range);
        final AtomicReference<Double> newY = new AtomicReference<Double>(0.0);
        final AtomicReference<Double> atomicReference;
        collisionBoxes.forEach(box -> atomicReference.set(Math.max(atomicReference.get(), box.maxY)));
        player.setPositionAndUpdate(player.posX, (double)newY.get(), player.posZ);
    }
    
    private void updateUnstep(final EntityPlayer player) {
        try {
            if (this.wasOnGround && !player.onGround && player.motionY <= 0.0) {
                this.unstep(player);
            }
        }
        finally {
            this.wasOnGround = player.onGround;
        }
    }
    
    @EventTarget
    public void onLocalPlayerUpdate(final LocalPlayerUpdateEvent event) {
        final EntityPlayer player = (EntityPlayer)event.getEntityLivingBase();
        if (player == null) {
            return;
        }
        this.updateUnstep(player);
    }
}
