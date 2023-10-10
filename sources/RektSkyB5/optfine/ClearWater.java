/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import optfine.BlockPosM;
import optfine.BlockUtils;
import optfine.Config;

public class ClearWater {
    public static void updateWaterOpacity(GameSettings p_updateWaterOpacity_0_, World p_updateWaterOpacity_1_) {
        Entity entity;
        IChunkProvider ichunkprovider;
        if (p_updateWaterOpacity_0_ != null) {
            int i2 = 3;
            if (p_updateWaterOpacity_0_.ofClearWater) {
                i2 = 1;
            }
            BlockUtils.setLightOpacity(Blocks.water, i2);
            BlockUtils.setLightOpacity(Blocks.flowing_water, i2);
        }
        if (p_updateWaterOpacity_1_ != null && (ichunkprovider = p_updateWaterOpacity_1_.getChunkProvider()) != null && (entity = Config.getMinecraft().getRenderViewEntity()) != null) {
            int j2 = (int)entity.posX / 16;
            int k2 = (int)entity.posZ / 16;
            int l2 = j2 - 512;
            int i1 = j2 + 512;
            int j1 = k2 - 512;
            int k1 = k2 + 512;
            int l1 = 0;
            for (int i2 = l2; i2 < i1; ++i2) {
                for (int j22 = j1; j22 < k1; ++j22) {
                    Chunk chunk;
                    if (!ichunkprovider.chunkExists(i2, j22) || (chunk = ichunkprovider.provideChunk(i2, j22)) == null || chunk instanceof EmptyChunk) continue;
                    int k22 = i2 << 4;
                    int l22 = j22 << 4;
                    int i3 = k22 + 16;
                    int j3 = l22 + 16;
                    BlockPosM blockposm = new BlockPosM(0, 0, 0);
                    BlockPosM blockposm1 = new BlockPosM(0, 0, 0);
                    for (int k3 = k22; k3 < i3; ++k3) {
                        block3: for (int l3 = l22; l3 < j3; ++l3) {
                            blockposm.setXyz(k3, 0, l3);
                            BlockPos blockpos = p_updateWaterOpacity_1_.getPrecipitationHeight(blockposm);
                            for (int i4 = 0; i4 < blockpos.getY(); ++i4) {
                                blockposm1.setXyz(k3, i4, l3);
                                IBlockState iblockstate = p_updateWaterOpacity_1_.getBlockState(blockposm1);
                                if (iblockstate.getBlock().getMaterial() != Material.water) continue;
                                p_updateWaterOpacity_1_.markBlocksDirtyVertical(k3, l3, blockposm1.getY(), blockpos.getY());
                                ++l1;
                                continue block3;
                            }
                        }
                    }
                }
            }
            if (l1 > 0) {
                String s2 = "server";
                if (Config.isMinecraftThread()) {
                    s2 = "client";
                }
                Config.dbg("ClearWater (" + s2 + ") relighted " + l1 + " chunks");
            }
        }
    }
}

