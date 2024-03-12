// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.src.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.client.settings.GameSettings;

public class ClearWater
{
    public static void updateWaterOpacity(final GameSettings settings, final World world) {
        if (settings != null) {
            int i = 3;
            if (settings.ofClearWater) {
                i = 1;
            }
            BlockAir.setLightOpacity(Blocks.water, i);
            BlockAir.setLightOpacity(Blocks.flowing_water, i);
        }
        if (world != null) {
            final IChunkProvider ichunkprovider = world.getChunkProvider();
            if (ichunkprovider != null) {
                final Entity entity = Config.getMinecraft().getRenderViewEntity();
                if (entity != null) {
                    final int j = (int)entity.posX / 16;
                    final int k = (int)entity.posZ / 16;
                    final int l = j - 512;
                    final int i2 = j + 512;
                    final int j2 = k - 512;
                    final int k2 = k + 512;
                    int l2 = 0;
                    for (int i3 = l; i3 < i2; ++i3) {
                        for (int j3 = j2; j3 < k2; ++j3) {
                            if (ichunkprovider.chunkExists(i3, j3)) {
                                final Chunk chunk = ichunkprovider.provideChunk(i3, j3);
                                if (chunk != null && !(chunk instanceof EmptyChunk)) {
                                    final int k3 = i3 << 4;
                                    final int l3 = j3 << 4;
                                    final int i4 = k3 + 16;
                                    final int j4 = l3 + 16;
                                    final BlockPosM blockposm = new BlockPosM(0, 0, 0);
                                    final BlockPosM blockposm2 = new BlockPosM(0, 0, 0);
                                    for (int k4 = k3; k4 < i4; ++k4) {
                                        for (int l4 = l3; l4 < j4; ++l4) {
                                            blockposm.setXyz(k4, 0, l4);
                                            final BlockPos blockpos = world.getPrecipitationHeight(blockposm);
                                            for (int i5 = 0; i5 < blockpos.getY(); ++i5) {
                                                blockposm2.setXyz(k4, i5, l4);
                                                final IBlockState iblockstate = world.getBlockState(blockposm2);
                                                if (iblockstate.getBlock().getMaterial() == Material.water) {
                                                    world.markBlocksDirtyVertical(k4, l4, blockposm2.getY(), blockpos.getY());
                                                    ++l2;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (l2 > 0) {
                        String s = "server";
                        if (Config.isMinecraftThread()) {
                            s = "client";
                        }
                        Config.dbg("ClearWater (" + s + ") relighted " + l2 + " chunks");
                    }
                }
            }
        }
    }
}
