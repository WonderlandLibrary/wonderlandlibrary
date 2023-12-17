// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import java.util.Arrays;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import java.util.Map;
import net.minecraft.world.biome.Biome;
import java.util.List;

public class MapGenVillage extends MapGenStructure
{
    public static final List<Biome> VILLAGE_SPAWN_BIOMES;
    private int size;
    private int distance;
    private final int minTownSeparation;
    
    public MapGenVillage() {
        this.distance = 32;
        this.minTownSeparation = 8;
    }
    
    public MapGenVillage(final Map<String, String> map) {
        this();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals("size")) {
                this.size = MathHelper.getInt(entry.getValue(), this.size, 0);
            }
            else {
                if (!entry.getKey().equals("distance")) {
                    continue;
                }
                this.distance = MathHelper.getInt(entry.getValue(), this.distance, 9);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Village";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        final int i = chunkX;
        final int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.distance - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.distance - 1;
        }
        int k = chunkX / this.distance;
        int l = chunkZ / this.distance;
        final Random random = this.world.setRandomSeed(k, l, 10387312);
        k *= this.distance;
        l *= this.distance;
        k += random.nextInt(this.distance - 8);
        l += random.nextInt(this.distance - 8);
        if (i == k && j == l) {
            final boolean flag = this.world.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, MapGenVillage.VILLAGE_SPAWN_BIOMES);
            if (flag) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final BlockPos pos, final boolean findUnexplored) {
        this.world = worldIn;
        return MapGenStructure.findNearestStructurePosBySpacing(worldIn, this, pos, this.distance, 8, 10387312, false, 100, findUnexplored);
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new Start(this.world, this.rand, chunkX, chunkZ, this.size);
    }
    
    static {
        VILLAGE_SPAWN_BIOMES = Arrays.asList(Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA);
    }
    
    public static class Start extends StructureStart
    {
        private boolean hasMoreThanTwoComponents;
        
        public Start() {
        }
        
        public Start(final World worldIn, final Random rand, final int x, final int z, final int size) {
            super(x, z);
            final List<StructureVillagePieces.PieceWeight> list = StructureVillagePieces.getStructureVillageWeightedPieceList(rand, size);
            final StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(worldIn.getBiomeProvider(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, size);
            this.components.add(structurevillagepieces$start);
            structurevillagepieces$start.buildComponent(structurevillagepieces$start, this.components, rand);
            final List<StructureComponent> list2 = structurevillagepieces$start.pendingRoads;
            final List<StructureComponent> list3 = structurevillagepieces$start.pendingHouses;
            while (!list2.isEmpty() || !list3.isEmpty()) {
                if (list2.isEmpty()) {
                    final int i = rand.nextInt(list3.size());
                    final StructureComponent structurecomponent = list3.remove(i);
                    structurecomponent.buildComponent(structurevillagepieces$start, this.components, rand);
                }
                else {
                    final int j = rand.nextInt(list2.size());
                    final StructureComponent structurecomponent2 = list2.remove(j);
                    structurecomponent2.buildComponent(structurevillagepieces$start, this.components, rand);
                }
            }
            this.updateBoundingBox();
            int k = 0;
            for (final StructureComponent structurecomponent3 : this.components) {
                if (!(structurecomponent3 instanceof StructureVillagePieces.Road)) {
                    ++k;
                }
            }
            this.hasMoreThanTwoComponents = (k > 2);
        }
        
        @Override
        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }
        
        @Override
        public void writeToNBT(final NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }
        
        @Override
        public void readFromNBT(final NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
        }
    }
}
