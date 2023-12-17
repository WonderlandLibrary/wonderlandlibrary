// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import com.google.common.collect.Lists;
import net.minecraft.world.biome.Biome;
import java.util.List;

public class MapGenNetherBridge extends MapGenStructure
{
    private final List<Biome.SpawnListEntry> spawnList;
    
    public MapGenNetherBridge() {
        (this.spawnList = (List<Biome.SpawnListEntry>)Lists.newArrayList()).add(new Biome.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
        this.spawnList.add(new Biome.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
        this.spawnList.add(new Biome.SpawnListEntry(EntityWitherSkeleton.class, 8, 5, 5));
        this.spawnList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 2, 5, 5));
        this.spawnList.add(new Biome.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
    }
    
    @Override
    public String getStructureName() {
        return "Fortress";
    }
    
    public List<Biome.SpawnListEntry> getSpawnList() {
        return this.spawnList;
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int chunkX, final int chunkZ) {
        final int i = chunkX >> 4;
        final int j = chunkZ >> 4;
        this.rand.setSeed((long)(i ^ j << 4) ^ this.world.getSeed());
        this.rand.nextInt();
        return this.rand.nextInt(3) == 0 && chunkX == (i << 4) + 4 + this.rand.nextInt(8) && chunkZ == (j << 4) + 4 + this.rand.nextInt(8);
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new Start(this.world, this.rand, chunkX, chunkZ);
    }
    
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final BlockPos pos, final boolean findUnexplored) {
        final int i = 1000;
        final int j = pos.getX() >> 4;
        final int k = pos.getZ() >> 4;
        for (int l = 0; l <= 1000; ++l) {
            for (int i2 = -l; i2 <= l; ++i2) {
                final boolean flag = i2 == -l || i2 == l;
                for (int j2 = -l; j2 <= l; ++j2) {
                    final boolean flag2 = j2 == -l || j2 == l;
                    if (flag || flag2) {
                        final int k2 = j + i2;
                        final int l2 = k + j2;
                        if (this.canSpawnStructureAtCoords(k2, l2) && (!findUnexplored || !worldIn.isChunkGeneratedAt(k2, l2))) {
                            return new BlockPos((k2 << 4) + 8, 64, (l2 << 4) + 8);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static class Start extends StructureStart
    {
        public Start() {
        }
        
        public Start(final World worldIn, final Random random, final int chunkX, final int chunkZ) {
            super(chunkX, chunkZ);
            final StructureNetherBridgePieces.Start structurenetherbridgepieces$start = new StructureNetherBridgePieces.Start(random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            this.components.add(structurenetherbridgepieces$start);
            structurenetherbridgepieces$start.buildComponent(structurenetherbridgepieces$start, this.components, random);
            final List<StructureComponent> list = structurenetherbridgepieces$start.pendingChildren;
            while (!list.isEmpty()) {
                final int i = random.nextInt(list.size());
                final StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(structurenetherbridgepieces$start, this.components, random);
            }
            this.updateBoundingBox();
            this.setRandomHeight(worldIn, random, 48, 70);
        }
    }
}
