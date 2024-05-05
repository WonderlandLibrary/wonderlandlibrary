package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class BlockPattern {
    private final Predicate<BlockWorldState>[][][] blockMatches;
    private final int fingerLength;
    private final int thumbLength;
    private final int palmLength;

    public BlockPattern(Predicate<BlockWorldState>[][][] predicatesIn) {
        this.blockMatches = predicatesIn;
        this.fingerLength = predicatesIn.length;
        if (this.fingerLength > 0) {
            this.thumbLength = predicatesIn[0].length;
            this.palmLength = this.thumbLength > 0 ? predicatesIn[0][0].length : 0;
        } else {
            this.thumbLength = 0;
            this.palmLength = 0;
        }
    }

    public int getThumbLength() {
        return this.thumbLength;
    }

    public int getPalmLength() {
        return this.palmLength;
    }

    private PatternHelper checkPatternAt(BlockPos pos, EnumFacing finger, EnumFacing thumb, LoadingCache<BlockPos, BlockWorldState> lcache) {
        for (int i = 0; i < this.palmLength; ++i) {
            for (int j = 0; j < this.thumbLength; ++j) {
                for (int k = 0; k < this.fingerLength; ++k) {
                    if (this.blockMatches[k][j][i].apply(lcache.getUnchecked((Object)BlockPattern.translateOffset(pos, finger, thumb, i, j, k)))) continue;
                    return null;
                }
            }
        }
        return new PatternHelper(pos, finger, thumb, lcache, this.palmLength, this.thumbLength, this.fingerLength);
    }

    public PatternHelper match(World worldIn, BlockPos pos) {
        LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.func_181627_a(worldIn, false);
        int i = Math.max((int)Math.max((int)this.palmLength, (int)this.thumbLength), (int)this.fingerLength);
        for (BlockPos blockpos : BlockPos.getAllInBox((BlockPos)pos, (BlockPos)pos.add(i - 1, i - 1, i - 1))) {
            for (EnumFacing enumfacing : EnumFacing.values()) {
                for (EnumFacing enumfacing1 : EnumFacing.values()) {
                    PatternHelper blockpattern$patternhelper;
                    if (enumfacing1 == enumfacing || enumfacing1 == enumfacing.getOpposite() || (blockpattern$patternhelper = this.checkPatternAt(blockpos, enumfacing, enumfacing1, loadingcache)) == null) continue;
                    return blockpattern$patternhelper;
                }
            }
        }
        return null;
    }

    public static LoadingCache<BlockPos, BlockWorldState> func_181627_a(World p_181627_0_, boolean p_181627_1_) {
        return CacheBuilder.newBuilder().build((com.google.common.cache.CacheLoader)new CacheLoader(p_181627_0_, p_181627_1_));
    }

    protected static BlockPos translateOffset(BlockPos pos, EnumFacing finger, EnumFacing thumb, int palmOffset, int thumbOffset, int fingerOffset) {
        if (finger != thumb && finger != thumb.getOpposite()) {
            Vec3i vec3i = new Vec3i(finger.getFrontOffsetX(), finger.getFrontOffsetY(), finger.getFrontOffsetZ());
            Vec3i vec3i1 = new Vec3i(thumb.getFrontOffsetX(), thumb.getFrontOffsetY(), thumb.getFrontOffsetZ());
            Vec3i vec3i2 = vec3i.crossProduct(vec3i1);
            return pos.add(vec3i1.getX() * -thumbOffset + vec3i2.getX() * palmOffset + vec3i.getX() * fingerOffset, vec3i1.getY() * -thumbOffset + vec3i2.getY() * palmOffset + vec3i.getY() * fingerOffset, vec3i1.getZ() * -thumbOffset + vec3i2.getZ() * palmOffset + vec3i.getZ() * fingerOffset);
        }
        throw new IllegalArgumentException("Invalid forwards & up combination");
    }
}
