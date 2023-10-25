package net.minecraft.world.gen.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenVillage
extends MapGenStructure {
    public static final List<BiomeGenBase> villageSpawnBiomes = Arrays.asList((Object[])new BiomeGenBase[]{BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna});
    private int terrainType;
    private int field_82665_g = 32;
    private int field_82666_h = 8;

    public MapGenVillage() {
    }

    public MapGenVillage(Map<String, String> p_i2093_1_) {
        this();
        for (Map.Entry entry : p_i2093_1_.entrySet()) {
            if (((String)entry.getKey()).equals((Object)"size")) {
                this.terrainType = MathHelper.parseIntWithDefaultAndMax((String)((String)entry.getValue()), (int)this.terrainType, (int)0);
                continue;
            }
            if (!((String)entry.getKey()).equals((Object)"distance")) continue;
            this.field_82665_g = MathHelper.parseIntWithDefaultAndMax((String)((String)entry.getValue()), (int)this.field_82665_g, (int)(this.field_82666_h + 1));
        }
    }

    public String getStructureName() {
        return "Village";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        boolean flag;
        int i = chunkX;
        int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.field_82665_g - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.field_82665_g - 1;
        }
        int k = chunkX / this.field_82665_g;
        int l = chunkZ / this.field_82665_g;
        Random random = this.worldObj.setRandomSeed(k, l, 10387312);
        k *= this.field_82665_g;
        l *= this.field_82665_g;
        return i == (k += random.nextInt(this.field_82665_g - this.field_82666_h)) && j == (l += random.nextInt(this.field_82665_g - this.field_82666_h)) && (flag = this.worldObj.getWorldChunkManager().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, villageSpawnBiomes));
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new Start(this.worldObj, this.rand, chunkX, chunkZ, this.terrainType);
    }
}
