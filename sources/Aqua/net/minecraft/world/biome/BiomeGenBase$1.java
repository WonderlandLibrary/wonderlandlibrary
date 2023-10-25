package net.minecraft.world.biome;

import net.minecraft.entity.EnumCreatureType;

static class BiomeGenBase.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$entity$EnumCreatureType;

    static {
        $SwitchMap$net$minecraft$entity$EnumCreatureType = new int[EnumCreatureType.values().length];
        try {
            BiomeGenBase.1.$SwitchMap$net$minecraft$entity$EnumCreatureType[EnumCreatureType.MONSTER.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            BiomeGenBase.1.$SwitchMap$net$minecraft$entity$EnumCreatureType[EnumCreatureType.CREATURE.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            BiomeGenBase.1.$SwitchMap$net$minecraft$entity$EnumCreatureType[EnumCreatureType.WATER_CREATURE.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            BiomeGenBase.1.$SwitchMap$net$minecraft$entity$EnumCreatureType[EnumCreatureType.AMBIENT.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
