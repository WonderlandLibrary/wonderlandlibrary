// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.client.settings.GameSettings;

public class ClearWater
{
    public static void updateWaterOpacity(final GameSettings settings, final World world) {
        if (settings != null) {
            byte cp = 3;
            if (settings.ofClearWater) {
                cp = 1;
            }
            BlockUtils.setLightOpacity(Blocks.water, cp);
            BlockUtils.setLightOpacity(Blocks.flowing_water, cp);
        }
        if (world != null) {
            final IChunkProvider var25 = world.getChunkProvider();
            if (var25 != null) {
                final Entity rve = Config.getMinecraft().func_175606_aa();
                if (rve != null) {
                    final int cViewX = (int)rve.posX / 16;
                    final int cViewZ = (int)rve.posZ / 16;
                    final int cXMin = cViewX - 512;
                    final int cXMax = cViewX + 512;
                    final int cZMin = cViewZ - 512;
                    final int cZMax = cViewZ + 512;
                    int countUpdated = 0;
                    for (int threadName = cXMin; threadName < cXMax; ++threadName) {
                        for (int cz = cZMin; cz < cZMax; ++cz) {
                            if (var25.chunkExists(threadName, cz)) {
                                final Chunk c = var25.provideChunk(threadName, cz);
                                if (c != null && !(c instanceof EmptyChunk)) {
                                    final int x0 = threadName << 4;
                                    final int z0 = cz << 4;
                                    final int x2 = x0 + 16;
                                    final int z2 = z0 + 16;
                                    final BlockPosM posXZ = new BlockPosM(0, 0, 0);
                                    final BlockPosM posXYZ = new BlockPosM(0, 0, 0);
                                    for (int x3 = x0; x3 < x2; ++x3) {
                                        for (int z3 = z0; z3 < z2; ++z3) {
                                            posXZ.setXyz(x3, 0, z3);
                                            final BlockPos posH = world.func_175725_q(posXZ);
                                            for (int y = 0; y < posH.getY(); ++y) {
                                                posXYZ.setXyz(x3, y, z3);
                                                final IBlockState bs = world.getBlockState(posXYZ);
                                                if (bs.getBlock().getMaterial() == Material.water) {
                                                    world.markBlocksDirtyVertical(x3, z3, posXYZ.getY(), posH.getY());
                                                    ++countUpdated;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (countUpdated > 0) {
                        String var26 = "server";
                        if (Config.isMinecraftThread()) {
                            var26 = "client";
                        }
                        Config.dbg("ClearWater (" + var26 + ") relighted " + countUpdated + " chunks");
                    }
                }
            }
        }
    }
}
