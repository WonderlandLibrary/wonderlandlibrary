/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenNetherBridge
extends MapGenStructure {
    private List<BiomeGenBase.SpawnListEntry> spawnList = Lists.newArrayList();

    @Override
    public String getStructureName() {
        return "Fortress";
    }

    @Override
    protected StructureStart getStructureStart(int n, int n2) {
        return new Start(this.worldObj, this.rand, n, n2);
    }

    public List<BiomeGenBase.SpawnListEntry> getSpawnList() {
        return this.spawnList;
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int n, int n2) {
        int n3 = n >> 4;
        int n4 = n2 >> 4;
        this.rand.setSeed((long)(n3 ^ n4 << 4) ^ this.worldObj.getSeed());
        this.rand.nextInt();
        return this.rand.nextInt(3) != 0 ? false : (n != (n3 << 4) + 4 + this.rand.nextInt(8) ? false : n2 == (n4 << 4) + 4 + this.rand.nextInt(8));
    }

    public MapGenNetherBridge() {
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
    }

    public static class Start
    extends StructureStart {
        public Start() {
        }

        public Start(World world, Random random, int n, int n2) {
            super(n, n2);
            StructureNetherBridgePieces.Start start = new StructureNetherBridgePieces.Start(random, (n << 4) + 2, (n2 << 4) + 2);
            this.components.add(start);
            start.buildComponent(start, this.components, random);
            List<StructureComponent> list = start.field_74967_d;
            while (!list.isEmpty()) {
                int n3 = random.nextInt(list.size());
                StructureComponent structureComponent = list.remove(n3);
                structureComponent.buildComponent(start, this.components, random);
            }
            this.updateBoundingBox();
            this.setRandomHeight(world, random, 48, 70);
        }
    }
}

