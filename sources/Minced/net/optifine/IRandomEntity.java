// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.world.biome.Biome;
import net.minecraft.util.math.BlockPos;

public interface IRandomEntity
{
    int getId();
    
    BlockPos getSpawnPosition();
    
    Biome getSpawnBiome();
    
    String getName();
    
    int getHealth();
    
    int getMaxHealth();
}
