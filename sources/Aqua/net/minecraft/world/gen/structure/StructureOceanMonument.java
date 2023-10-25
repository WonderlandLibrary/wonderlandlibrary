package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureOceanMonument
extends MapGenStructure {
    private int field_175800_f = 32;
    private int field_175801_g = 5;
    public static final List<BiomeGenBase> field_175802_d = Arrays.asList((Object[])new BiomeGenBase[]{BiomeGenBase.ocean, BiomeGenBase.deepOcean, BiomeGenBase.river, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver});
    private static final List<BiomeGenBase.SpawnListEntry> field_175803_h = Lists.newArrayList();

    public StructureOceanMonument() {
    }

    public StructureOceanMonument(Map<String, String> p_i45608_1_) {
        this();
        for (Map.Entry entry : p_i45608_1_.entrySet()) {
            if (((String)entry.getKey()).equals((Object)"spacing")) {
                this.field_175800_f = MathHelper.parseIntWithDefaultAndMax((String)((String)entry.getValue()), (int)this.field_175800_f, (int)1);
                continue;
            }
            if (!((String)entry.getKey()).equals((Object)"separation")) continue;
            this.field_175801_g = MathHelper.parseIntWithDefaultAndMax((String)((String)entry.getValue()), (int)this.field_175801_g, (int)1);
        }
    }

    public String getStructureName() {
        return "Monument";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.field_175800_f - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.field_175800_f - 1;
        }
        int k = chunkX / this.field_175800_f;
        int l = chunkZ / this.field_175800_f;
        Random random = this.worldObj.setRandomSeed(k, l, 10387313);
        k *= this.field_175800_f;
        l *= this.field_175800_f;
        if (i == (k += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2) && j == (l += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2)) {
            if (this.worldObj.getWorldChunkManager().getBiomeGenerator(new BlockPos(i * 16 + 8, 64, j * 16 + 8), (BiomeGenBase)null) != BiomeGenBase.deepOcean) {
                return false;
            }
            boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(i * 16 + 8, j * 16 + 8, 29, field_175802_d);
            if (flag) {
                return true;
            }
        }
        return false;
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new StartMonument(this.worldObj, this.rand, chunkX, chunkZ);
    }

    public List<BiomeGenBase.SpawnListEntry> getScatteredFeatureSpawnList() {
        return field_175803_h;
    }

    static {
        field_175803_h.add((Object)new BiomeGenBase.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }
}
