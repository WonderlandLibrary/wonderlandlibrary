// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.end;

import java.util.Random;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeEndDecorator;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.List;
import net.minecraft.world.WorldServer;

public enum DragonSpawnManager
{
    START {
        @Override
        public void process(final WorldServer worldIn, final DragonFightManager manager, final List<EntityEnderCrystal> crystals, final int ticks, final BlockPos pos) {
            final BlockPos blockpos = new BlockPos(0, 128, 0);
            for (final EntityEnderCrystal entityendercrystal : crystals) {
                entityendercrystal.setBeamTarget(blockpos);
            }
            manager.setRespawnState(DragonSpawnManager$1.PREPARING_TO_SUMMON_PILLARS);
        }
    }, 
    PREPARING_TO_SUMMON_PILLARS {
        @Override
        public void process(final WorldServer worldIn, final DragonFightManager manager, final List<EntityEnderCrystal> crystals, final int ticks, final BlockPos pos) {
            if (ticks < 100) {
                if (ticks == 0 || ticks == 50 || ticks == 51 || ticks == 52 || ticks >= 95) {
                    worldIn.playEvent(3001, new BlockPos(0, 128, 0), 0);
                }
            }
            else {
                manager.setRespawnState(DragonSpawnManager$2.SUMMONING_PILLARS);
            }
        }
    }, 
    SUMMONING_PILLARS {
        @Override
        public void process(final WorldServer worldIn, final DragonFightManager manager, final List<EntityEnderCrystal> crystals, final int ticks, final BlockPos pos) {
            final int i = 40;
            final boolean flag = ticks % 40 == 0;
            final boolean flag2 = ticks % 40 == 39;
            if (flag || flag2) {
                final WorldGenSpikes.EndSpike[] aworldgenspikes$endspike = BiomeEndDecorator.getSpikesForWorld(worldIn);
                final int j = ticks / 40;
                if (j < aworldgenspikes$endspike.length) {
                    final WorldGenSpikes.EndSpike worldgenspikes$endspike = aworldgenspikes$endspike[j];
                    if (flag) {
                        for (final EntityEnderCrystal entityendercrystal : crystals) {
                            entityendercrystal.setBeamTarget(new BlockPos(worldgenspikes$endspike.getCenterX(), worldgenspikes$endspike.getHeight() + 1, worldgenspikes$endspike.getCenterZ()));
                        }
                    }
                    else {
                        final int k = 10;
                        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(new BlockPos(worldgenspikes$endspike.getCenterX() - 10, worldgenspikes$endspike.getHeight() - 10, worldgenspikes$endspike.getCenterZ() - 10), new BlockPos(worldgenspikes$endspike.getCenterX() + 10, worldgenspikes$endspike.getHeight() + 10, worldgenspikes$endspike.getCenterZ() + 10))) {
                            worldIn.setBlockToAir(blockpos$mutableblockpos);
                        }
                        worldIn.createExplosion(null, worldgenspikes$endspike.getCenterX() + 0.5f, worldgenspikes$endspike.getHeight(), worldgenspikes$endspike.getCenterZ() + 0.5f, 5.0f, true);
                        final WorldGenSpikes worldgenspikes = new WorldGenSpikes();
                        worldgenspikes.setSpike(worldgenspikes$endspike);
                        worldgenspikes.setCrystalInvulnerable(true);
                        worldgenspikes.setBeamTarget(new BlockPos(0, 128, 0));
                        worldgenspikes.generate(worldIn, new Random(), new BlockPos(worldgenspikes$endspike.getCenterX(), 45, worldgenspikes$endspike.getCenterZ()));
                    }
                }
                else if (flag) {
                    manager.setRespawnState(DragonSpawnManager$3.SUMMONING_DRAGON);
                }
            }
        }
    }, 
    SUMMONING_DRAGON {
        @Override
        public void process(final WorldServer worldIn, final DragonFightManager manager, final List<EntityEnderCrystal> crystals, final int ticks, final BlockPos pos) {
            if (ticks >= 100) {
                manager.setRespawnState(DragonSpawnManager$4.END);
                manager.resetSpikeCrystals();
                for (final EntityEnderCrystal entityendercrystal : crystals) {
                    entityendercrystal.setBeamTarget(null);
                    worldIn.createExplosion(entityendercrystal, entityendercrystal.posX, entityendercrystal.posY, entityendercrystal.posZ, 6.0f, false);
                    entityendercrystal.setDead();
                }
            }
            else if (ticks >= 80) {
                worldIn.playEvent(3001, new BlockPos(0, 128, 0), 0);
            }
            else if (ticks == 0) {
                for (final EntityEnderCrystal entityendercrystal2 : crystals) {
                    entityendercrystal2.setBeamTarget(new BlockPos(0, 128, 0));
                }
            }
            else if (ticks < 5) {
                worldIn.playEvent(3001, new BlockPos(0, 128, 0), 0);
            }
        }
    }, 
    END {
        @Override
        public void process(final WorldServer worldIn, final DragonFightManager manager, final List<EntityEnderCrystal> crystals, final int ticks, final BlockPos pos) {
        }
    };
    
    public abstract void process(final WorldServer p0, final DragonFightManager p1, final List<EntityEnderCrystal> p2, final int p3, final BlockPos p4);
}
