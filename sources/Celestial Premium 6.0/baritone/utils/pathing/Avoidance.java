/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap
 */
package baritone.utils.pathing;

import baritone.Baritone;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.IPlayerContext;
import it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.math.BlockPos;

public class Avoidance {
    private final int centerX;
    private final int centerY;
    private final int centerZ;
    private final double coefficient;
    private final int radius;
    private final int radiusSq;

    public Avoidance(BlockPos center, double coefficient, int radius) {
        this(center.getX(), center.getY(), center.getZ(), coefficient, radius);
    }

    public Avoidance(int centerX, int centerY, int centerZ, double coefficient, int radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.coefficient = coefficient;
        this.radius = radius;
        this.radiusSq = radius * radius;
    }

    public double coefficient(int x, int y, int z) {
        int xDiff = x - this.centerX;
        int yDiff = y - this.centerY;
        int zDiff = z - this.centerZ;
        return xDiff * xDiff + yDiff * yDiff + zDiff * zDiff <= this.radiusSq ? this.coefficient : 1.0;
    }

    public static List<Avoidance> create(IPlayerContext ctx) {
        if (!((Boolean)Baritone.settings().avoidance.value).booleanValue()) {
            return Collections.emptyList();
        }
        ArrayList<Avoidance> res = new ArrayList<Avoidance>();
        double mobSpawnerCoeff = (Double)Baritone.settings().mobSpawnerAvoidanceCoefficient.value;
        double mobCoeff = (Double)Baritone.settings().mobAvoidanceCoefficient.value;
        if (mobSpawnerCoeff != 1.0) {
            ctx.worldData().getCachedWorld().getLocationsOf("mob_spawner", 1, ctx.playerFeet().x, ctx.playerFeet().z, 2).forEach(mobspawner -> res.add(new Avoidance((BlockPos)mobspawner, mobSpawnerCoeff, (Integer)Baritone.settings().mobSpawnerAvoidanceRadius.value)));
        }
        if (mobCoeff != 1.0) {
            ctx.world().loadedEntityList.stream().filter(entity -> entity instanceof EntityMob).filter(entity -> !(entity instanceof EntitySpider) || (double)ctx.player().getBrightness() < 0.5).filter(entity -> !(entity instanceof EntityPigZombie) || ((EntityPigZombie)entity).isAngry()).filter(entity -> !(entity instanceof EntityEnderman) || ((EntityEnderman)entity).isScreaming()).forEach(entity -> res.add(new Avoidance(new BlockPos((Entity)entity), mobCoeff, (Integer)Baritone.settings().mobAvoidanceRadius.value)));
        }
        return res;
    }

    public void applySpherical(Long2DoubleOpenHashMap map) {
        for (int x = -this.radius; x <= this.radius; ++x) {
            for (int y = -this.radius; y <= this.radius; ++y) {
                for (int z = -this.radius; z <= this.radius; ++z) {
                    if (x * x + y * y + z * z > this.radius * this.radius) continue;
                    long hash = BetterBlockPos.longHash(this.centerX + x, this.centerY + y, this.centerZ + z);
                    map.put(hash, map.get(hash) * this.coefficient);
                }
            }
        }
    }
}

