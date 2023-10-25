/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseBase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseHoldingPattern
extends PhaseBase {
    private Path currentPath;
    private Vec3d targetLocation;
    private boolean clockwise;

    public PhaseHoldingPattern(EntityDragon dragonIn) {
        super(dragonIn);
    }

    public PhaseList<PhaseHoldingPattern> getPhaseList() {
        return PhaseList.HOLDING_PATTERN;
    }

    @Override
    public void doLocalUpdate() {
        double d0;
        double d = d0 = this.targetLocation == null ? 0.0 : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
        if (d0 < 100.0 || d0 > 22500.0 || this.dragon.isCollidedHorizontally || this.dragon.isCollidedVertically) {
            this.findNewTarget();
        }
    }

    @Override
    public void initPhase() {
        this.currentPath = null;
        this.targetLocation = null;
    }

    @Override
    @Nullable
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }

    private void findNewTarget() {
        if (this.currentPath != null && this.currentPath.isFinished()) {
            int i;
            BlockPos blockpos = this.dragon.world.getTopSolidOrLiquidBlock(new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION));
            int n = i = this.dragon.getFightManager() == null ? 0 : this.dragon.getFightManager().getNumAliveCrystals();
            if (this.dragon.getRNG().nextInt(i + 3) == 0) {
                this.dragon.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
                return;
            }
            double d0 = 64.0;
            EntityPlayer entityplayer = this.dragon.world.getNearestAttackablePlayer(blockpos, d0, d0);
            if (entityplayer != null) {
                d0 = entityplayer.getDistanceSqToCenter(blockpos) / 512.0;
            }
            if (entityplayer != null && (this.dragon.getRNG().nextInt(MathHelper.abs((int)d0) + 2) == 0 || this.dragon.getRNG().nextInt(i + 2) == 0)) {
                this.strafePlayer(entityplayer);
                return;
            }
        }
        if (this.currentPath == null || this.currentPath.isFinished()) {
            int j;
            int k = j = this.dragon.initPathPoints();
            if (this.dragon.getRNG().nextInt(8) == 0) {
                this.clockwise = !this.clockwise;
                k = j + 6;
            }
            k = this.clockwise ? ++k : --k;
            if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() >= 0) {
                if ((k %= 12) < 0) {
                    k += 12;
                }
            } else {
                k -= 12;
                k &= 7;
                k += 12;
            }
            this.currentPath = this.dragon.findPath(j, k, null);
            if (this.currentPath != null) {
                this.currentPath.incrementPathIndex();
            }
        }
        this.navigateToNextPathNode();
    }

    private void strafePlayer(EntityPlayer player) {
        this.dragon.getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
        this.dragon.getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER).setTarget(player);
    }

    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            double d2;
            Vec3d vec3d = this.currentPath.getCurrentPos();
            this.currentPath.incrementPathIndex();
            double d0 = vec3d.x;
            double d1 = vec3d.z;
            while (!((d2 = vec3d.y + (double)(this.dragon.getRNG().nextFloat() * 20.0f)) >= vec3d.y)) {
            }
            this.targetLocation = new Vec3d(d0, d2, d1);
        }
    }

    @Override
    public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc, @Nullable EntityPlayer plyr) {
        if (plyr != null && !plyr.capabilities.disableDamage) {
            this.strafePlayer(plyr);
        }
    }
}

