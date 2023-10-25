/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class MapGenVillage
extends MapGenStructure {
    private int terrainType;
    private int field_82665_g = 32;
    private int field_82666_h = 8;
    public static final List<BiomeGenBase> villageSpawnBiomes = Arrays.asList(BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna);

    @Override
    protected StructureStart getStructureStart(int n, int n2) {
        return new Start(this.worldObj, this.rand, n, n2, this.terrainType);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int n, int n2) {
        boolean bl;
        int n3 = n;
        int n4 = n2;
        if (n < 0) {
            n -= this.field_82665_g - 1;
        }
        if (n2 < 0) {
            n2 -= this.field_82665_g - 1;
        }
        int n5 = n / this.field_82665_g;
        int n6 = n2 / this.field_82665_g;
        Random random = this.worldObj.setRandomSeed(n5, n6, 10387312);
        n5 *= this.field_82665_g;
        n6 *= this.field_82665_g;
        return n3 == (n5 += random.nextInt(this.field_82665_g - this.field_82666_h)) && n4 == (n6 += random.nextInt(this.field_82665_g - this.field_82666_h)) && (bl = this.worldObj.getWorldChunkManager().areBiomesViable(n3 * 16 + 8, n4 * 16 + 8, 0, villageSpawnBiomes));
    }

    @Override
    public String getStructureName() {
        return "Village";
    }

    public MapGenVillage() {
    }

    public MapGenVillage(Map<String, String> map) {
        this();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals("size")) {
                this.terrainType = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.terrainType, 0);
                continue;
            }
            if (!entry.getKey().equals("distance")) continue;
            this.field_82665_g = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_82665_g, this.field_82666_h + 1);
        }
    }

    public static class Start
    extends StructureStart {
        private boolean hasMoreThanTwoComponents;

        @Override
        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }

        @Override
        public void writeToNBT(NBTTagCompound nBTTagCompound) {
            super.writeToNBT(nBTTagCompound);
            nBTTagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }

        public Start() {
        }

        @Override
        public void readFromNBT(NBTTagCompound nBTTagCompound) {
            super.readFromNBT(nBTTagCompound);
            this.hasMoreThanTwoComponents = nBTTagCompound.getBoolean("Valid");
        }

        public Start(World world, Random random, int n, int n2, int n3) {
            super(n, n2);
            int n4;
            List<StructureVillagePieces.PieceWeight> list = StructureVillagePieces.getStructureVillageWeightedPieceList(random, n3);
            StructureVillagePieces.Start start = new StructureVillagePieces.Start(world.getWorldChunkManager(), 0, random, (n << 4) + 2, (n2 << 4) + 2, list, n3);
            this.components.add(start);
            start.buildComponent(start, this.components, random);
            List<StructureComponent> list2 = start.field_74930_j;
            List<StructureComponent> list3 = start.field_74932_i;
            while (!list2.isEmpty() || !list3.isEmpty()) {
                StructureComponent structureComponent;
                if (list2.isEmpty()) {
                    n4 = random.nextInt(list3.size());
                    structureComponent = list3.remove(n4);
                    structureComponent.buildComponent(start, this.components, random);
                    continue;
                }
                n4 = random.nextInt(list2.size());
                structureComponent = list2.remove(n4);
                structureComponent.buildComponent(start, this.components, random);
            }
            this.updateBoundingBox();
            n4 = 0;
            for (StructureComponent structureComponent : this.components) {
                if (structureComponent instanceof StructureVillagePieces.Road) continue;
                ++n4;
            }
            this.hasMoreThanTwoComponents = n4 > 2;
        }
    }
}

