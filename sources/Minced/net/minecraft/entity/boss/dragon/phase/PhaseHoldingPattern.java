// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.util.DamageSource;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.Vec3d;
import net.minecraft.pathfinding.Path;

public class PhaseHoldingPattern extends PhaseBase
{
    private Path currentPath;
    private Vec3d targetLocation;
    private boolean clockwise;
    
    public PhaseHoldingPattern(final EntityDragon dragonIn) {
        super(dragonIn);
    }
    
    @Override
    public PhaseList<PhaseHoldingPattern> getType() {
        return PhaseList.HOLDING_PATTERN;
    }
    
    @Override
    public void doLocalUpdate() {
        final double d0 = (this.targetLocation == null) ? 0.0 : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
        if (d0 < 100.0 || d0 > 22500.0 || this.dragon.collidedHorizontally || this.dragon.collidedVertically) {
            this.findNewTarget();
        }
    }
    
    @Override
    public void initPhase() {
        this.currentPath = null;
        this.targetLocation = null;
    }
    
    @Nullable
    @Override
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }
    
    private void findNewTarget() {
        if (this.currentPath != null && this.currentPath.isFinished()) {
            final BlockPos blockpos = this.dragon.world.getTopSolidOrLiquidBlock(new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION));
            final int i = (this.dragon.getFightManager() == null) ? 0 : this.dragon.getFightManager().getNumAliveCrystals();
            if (this.dragon.getRNG().nextInt(i + 3) == 0) {
                this.dragon.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
                return;
            }
            double d0 = 64.0;
            final EntityPlayer entityplayer = this.dragon.world.getNearestAttackablePlayer(blockpos, d0, d0);
            if (entityplayer != null) {
                d0 = entityplayer.getDistanceSqToCenter(blockpos) / 512.0;
            }
            if (entityplayer != null && (this.dragon.getRNG().nextInt(MathHelper.abs((int)d0) + 2) == 0 || this.dragon.getRNG().nextInt(i + 2) == 0)) {
                this.strafePlayer(entityplayer);
                return;
            }
        }
        if (this.currentPath == null || this.currentPath.isFinished()) {
            int k;
            final int j = k = this.dragon.initPathPoints();
            if (this.dragon.getRNG().nextInt(8) == 0) {
                this.clockwise = !this.clockwise;
                k = j + 6;
            }
            if (this.clockwise) {
                ++k;
            }
            else {
                --k;
            }
            if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() >= 0) {
                k %= 12;
                if (k < 0) {
                    k += 12;
                }
            }
            else {
                k -= 12;
                k &= 0x7;
                k += 12;
            }
            this.currentPath = this.dragon.findPath(j, k, null);
            if (this.currentPath != null) {
                this.currentPath.incrementPathIndex();
            }
        }
        this.navigateToNextPathNode();
    }
    
    private void strafePlayer(final EntityPlayer player) {
        this.dragon.getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
        this.dragon.getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER).setTarget(player);
    }
    
    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            final Vec3d vec3d = this.currentPath.getCurrentPos();
            this.currentPath.incrementPathIndex();
            final double d0 = vec3d.x;
            final double d2 = vec3d.z;
            double d3;
            do {
                d3 = vec3d.y + this.dragon.getRNG().nextFloat() * 20.0f;
            } while (d3 < vec3d.y);
            this.targetLocation = new Vec3d(d0, d3, d2);
        }
    }
    
    @Override
    public void onCrystalDestroyed(final EntityEnderCrystal crystal, final BlockPos pos, final DamageSource dmgSrc, @Nullable final EntityPlayer plyr) {
        if (plyr != null && !plyr.capabilities.disableDamage) {
            this.strafePlayer(plyr);
        }
    }
}
