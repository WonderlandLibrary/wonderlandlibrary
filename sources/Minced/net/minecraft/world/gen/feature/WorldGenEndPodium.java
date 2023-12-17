// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockTorch;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;

public class WorldGenEndPodium extends WorldGenerator
{
    public static final BlockPos END_PODIUM_LOCATION;
    public static final BlockPos END_PODIUM_CHUNK_POS;
    private final boolean activePortal;
    
    public WorldGenEndPodium(final boolean activePortalIn) {
        this.activePortal = activePortalIn;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(new BlockPos(position.getX() - 4, position.getY() - 1, position.getZ() - 4), new BlockPos(position.getX() + 4, position.getY() + 32, position.getZ() + 4))) {
            final double d0 = blockpos$mutableblockpos.getDistance(position.getX(), blockpos$mutableblockpos.getY(), position.getZ());
            if (d0 <= 3.5) {
                if (blockpos$mutableblockpos.getY() < position.getY()) {
                    if (d0 <= 2.5) {
                        this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
                    }
                    else {
                        if (blockpos$mutableblockpos.getY() >= position.getY()) {
                            continue;
                        }
                        this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.END_STONE.getDefaultState());
                    }
                }
                else if (blockpos$mutableblockpos.getY() > position.getY()) {
                    this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
                }
                else if (d0 > 2.5) {
                    this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
                }
                else if (this.activePortal) {
                    this.setBlockAndNotifyAdequately(worldIn, new BlockPos(blockpos$mutableblockpos), Blocks.END_PORTAL.getDefaultState());
                }
                else {
                    this.setBlockAndNotifyAdequately(worldIn, new BlockPos(blockpos$mutableblockpos), Blocks.AIR.getDefaultState());
                }
            }
        }
        for (int i = 0; i < 4; ++i) {
            this.setBlockAndNotifyAdequately(worldIn, position.up(i), Blocks.BEDROCK.getDefaultState());
        }
        final BlockPos blockpos = position.up(2);
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            this.setBlockAndNotifyAdequately(worldIn, blockpos.offset(enumfacing), Blocks.TORCH.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, enumfacing));
        }
        return true;
    }
    
    static {
        END_PODIUM_LOCATION = BlockPos.ORIGIN;
        END_PODIUM_CHUNK_POS = new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION.getX() - 4 & 0xFFFFFFF0, 0, WorldGenEndPodium.END_PODIUM_LOCATION.getZ() - 4 & 0xFFFFFFF0);
    }
}
