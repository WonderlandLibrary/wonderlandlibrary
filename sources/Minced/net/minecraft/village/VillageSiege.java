// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.village;

import javax.annotation.Nullable;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class VillageSiege
{
    private final World world;
    private boolean hasSetupSiege;
    private int siegeState;
    private int siegeCount;
    private int nextSpawnTime;
    private Village village;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    
    public VillageSiege(final World worldIn) {
        this.siegeState = -1;
        this.world = worldIn;
    }
    
    public void tick() {
        if (this.world.isDaytime()) {
            this.siegeState = 0;
        }
        else if (this.siegeState != 2) {
            if (this.siegeState == 0) {
                final float f = this.world.getCelestialAngle(0.0f);
                if (f < 0.5 || f > 0.501) {
                    return;
                }
                this.siegeState = ((this.world.rand.nextInt(10) == 0) ? 1 : 2);
                this.hasSetupSiege = false;
                if (this.siegeState == 2) {
                    return;
                }
            }
            if (this.siegeState != -1) {
                if (!this.hasSetupSiege) {
                    if (!this.trySetupSiege()) {
                        return;
                    }
                    this.hasSetupSiege = true;
                }
                if (this.nextSpawnTime > 0) {
                    --this.nextSpawnTime;
                }
                else {
                    this.nextSpawnTime = 2;
                    if (this.siegeCount > 0) {
                        this.spawnZombie();
                        --this.siegeCount;
                    }
                    else {
                        this.siegeState = 2;
                    }
                }
            }
        }
    }
    
    private boolean trySetupSiege() {
        final List<EntityPlayer> list = this.world.playerEntities;
        for (final EntityPlayer entityplayer : list) {
            if (!entityplayer.isSpectator()) {
                this.village = this.world.getVillageCollection().getNearestVillage(new BlockPos(entityplayer), 1);
                if (this.village == null || this.village.getNumVillageDoors() < 10 || this.village.getTicksSinceLastDoorAdding() < 20 || this.village.getNumVillagers() < 20) {
                    continue;
                }
                final BlockPos blockpos = this.village.getCenter();
                final float f = (float)this.village.getVillageRadius();
                boolean flag = false;
                for (int i = 0; i < 10; ++i) {
                    final float f2 = this.world.rand.nextFloat() * 6.2831855f;
                    this.spawnX = blockpos.getX() + (int)(MathHelper.cos(f2) * f * 0.9);
                    this.spawnY = blockpos.getY();
                    this.spawnZ = blockpos.getZ() + (int)(MathHelper.sin(f2) * f * 0.9);
                    flag = false;
                    for (final Village village : this.world.getVillageCollection().getVillageList()) {
                        if (village != this.village && village.isBlockPosWithinSqVillageRadius(new BlockPos(this.spawnX, this.spawnY, this.spawnZ))) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        break;
                    }
                }
                if (flag) {
                    return false;
                }
                final Vec3d vec3d = this.findRandomSpawnPos(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
                if (vec3d != null) {
                    this.nextSpawnTime = 0;
                    this.siegeCount = 20;
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    private boolean spawnZombie() {
        final Vec3d vec3d = this.findRandomSpawnPos(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
        if (vec3d == null) {
            return false;
        }
        EntityZombie entityzombie;
        try {
            entityzombie = new EntityZombie(this.world);
            entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), null);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        entityzombie.setLocationAndAngles(vec3d.x, vec3d.y, vec3d.z, this.world.rand.nextFloat() * 360.0f, 0.0f);
        this.world.spawnEntity(entityzombie);
        final BlockPos blockpos = this.village.getCenter();
        entityzombie.setHomePosAndDistance(blockpos, this.village.getVillageRadius());
        return true;
    }
    
    @Nullable
    private Vec3d findRandomSpawnPos(final BlockPos pos) {
        for (int i = 0; i < 10; ++i) {
            final BlockPos blockpos = pos.add(this.world.rand.nextInt(16) - 8, this.world.rand.nextInt(6) - 3, this.world.rand.nextInt(16) - 8);
            if (this.village.isBlockPosWithinSqVillageRadius(blockpos) && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, this.world, blockpos)) {
                return new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }
        return null;
    }
}
