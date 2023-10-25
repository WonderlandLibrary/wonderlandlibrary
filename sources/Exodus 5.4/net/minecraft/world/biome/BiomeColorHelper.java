/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeColorHelper {
    private static final ColorResolver field_180289_b;
    private static final ColorResolver field_180291_a;
    private static final ColorResolver field_180290_c;

    public static int getGrassColorAtPos(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return BiomeColorHelper.func_180285_a(iBlockAccess, blockPos, field_180291_a);
    }

    public static int getWaterColorAtPos(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return BiomeColorHelper.func_180285_a(iBlockAccess, blockPos, field_180290_c);
    }

    public static int getFoliageColorAtPos(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return BiomeColorHelper.func_180285_a(iBlockAccess, blockPos, field_180289_b);
    }

    private static int func_180285_a(IBlockAccess iBlockAccess, BlockPos blockPos, ColorResolver colorResolver) {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(blockPos.add(-1, 0, -1), blockPos.add(1, 0, 1))) {
            int n4 = colorResolver.getColorAtPos(iBlockAccess.getBiomeGenForCoords(mutableBlockPos), mutableBlockPos);
            n += (n4 & 0xFF0000) >> 16;
            n2 += (n4 & 0xFF00) >> 8;
            n3 += n4 & 0xFF;
        }
        return (n / 9 & 0xFF) << 16 | (n2 / 9 & 0xFF) << 8 | n3 / 9 & 0xFF;
    }

    static {
        field_180291_a = new ColorResolver(){

            @Override
            public int getColorAtPos(BiomeGenBase biomeGenBase, BlockPos blockPos) {
                return biomeGenBase.getGrassColorAtPos(blockPos);
            }
        };
        field_180289_b = new ColorResolver(){

            @Override
            public int getColorAtPos(BiomeGenBase biomeGenBase, BlockPos blockPos) {
                return biomeGenBase.getFoliageColorAtPos(blockPos);
            }
        };
        field_180290_c = new ColorResolver(){

            @Override
            public int getColorAtPos(BiomeGenBase biomeGenBase, BlockPos blockPos) {
                return biomeGenBase.waterColorMultiplier;
            }
        };
    }

    static interface ColorResolver {
        public int getColorAtPos(BiomeGenBase var1, BlockPos var2);
    }
}

