/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public interface IChunkGenerator {
    public Chunk provideChunk(int var1, int var2);

    public void populate(int var1, int var2);

    public boolean generateStructures(Chunk var1, int var2, int var3);

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType var1, BlockPos var2);

    @Nullable
    public BlockPos getStrongholdGen(World var1, String var2, BlockPos var3, boolean var4);

    public void recreateStructures(Chunk var1, int var2, int var3);

    public boolean func_193414_a(World var1, String var2, BlockPos var3);
}

