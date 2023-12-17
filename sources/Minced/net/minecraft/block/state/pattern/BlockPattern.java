// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state.pattern;

import com.google.common.base.MoreObjects;
import net.minecraft.util.math.Vec3i;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import java.util.Iterator;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import com.google.common.cache.LoadingCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.BlockWorldState;
import com.google.common.base.Predicate;

public class BlockPattern
{
    private final Predicate<BlockWorldState>[][][] blockMatches;
    private final int fingerLength;
    private final int thumbLength;
    private final int palmLength;
    
    public BlockPattern(final Predicate<BlockWorldState>[][][] predicatesIn) {
        this.blockMatches = predicatesIn;
        this.fingerLength = predicatesIn.length;
        if (this.fingerLength > 0) {
            this.thumbLength = predicatesIn[0].length;
            if (this.thumbLength > 0) {
                this.palmLength = predicatesIn[0][0].length;
            }
            else {
                this.palmLength = 0;
            }
        }
        else {
            this.thumbLength = 0;
            this.palmLength = 0;
        }
    }
    
    public int getFingerLength() {
        return this.fingerLength;
    }
    
    public int getThumbLength() {
        return this.thumbLength;
    }
    
    public int getPalmLength() {
        return this.palmLength;
    }
    
    @Nullable
    private PatternHelper checkPatternAt(final BlockPos pos, final EnumFacing finger, final EnumFacing thumb, final LoadingCache<BlockPos, BlockWorldState> lcache) {
        for (int i = 0; i < this.palmLength; ++i) {
            for (int j = 0; j < this.thumbLength; ++j) {
                for (int k = 0; k < this.fingerLength; ++k) {
                    if (!this.blockMatches[k][j][i].apply(lcache.getUnchecked((Object)translateOffset(pos, finger, thumb, i, j, k)))) {
                        return null;
                    }
                }
            }
        }
        return new PatternHelper(pos, finger, thumb, lcache, this.palmLength, this.thumbLength, this.fingerLength);
    }
    
    @Nullable
    public PatternHelper match(final World worldIn, final BlockPos pos) {
        final LoadingCache<BlockPos, BlockWorldState> loadingcache = createLoadingCache(worldIn, false);
        final int i = Math.max(Math.max(this.palmLength, this.thumbLength), this.fingerLength);
        for (final BlockPos blockpos : BlockPos.getAllInBox(pos, pos.add(i - 1, i - 1, i - 1))) {
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                for (final EnumFacing enumfacing2 : EnumFacing.values()) {
                    if (enumfacing2 != enumfacing && enumfacing2 != enumfacing.getOpposite()) {
                        final PatternHelper blockpattern$patternhelper = this.checkPatternAt(blockpos, enumfacing, enumfacing2, loadingcache);
                        if (blockpattern$patternhelper != null) {
                            return blockpattern$patternhelper;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static LoadingCache<BlockPos, BlockWorldState> createLoadingCache(final World worldIn, final boolean forceLoadIn) {
        return (LoadingCache<BlockPos, BlockWorldState>)CacheBuilder.newBuilder().build((com.google.common.cache.CacheLoader)new CacheLoader(worldIn, forceLoadIn));
    }
    
    protected static BlockPos translateOffset(final BlockPos pos, final EnumFacing finger, final EnumFacing thumb, final int palmOffset, final int thumbOffset, final int fingerOffset) {
        if (finger != thumb && finger != thumb.getOpposite()) {
            final Vec3i vec3i = new Vec3i(finger.getXOffset(), finger.getYOffset(), finger.getZOffset());
            final Vec3i vec3i2 = new Vec3i(thumb.getXOffset(), thumb.getYOffset(), thumb.getZOffset());
            final Vec3i vec3i3 = vec3i.crossProduct(vec3i2);
            return pos.add(vec3i2.getX() * -thumbOffset + vec3i3.getX() * palmOffset + vec3i.getX() * fingerOffset, vec3i2.getY() * -thumbOffset + vec3i3.getY() * palmOffset + vec3i.getY() * fingerOffset, vec3i2.getZ() * -thumbOffset + vec3i3.getZ() * palmOffset + vec3i.getZ() * fingerOffset);
        }
        throw new IllegalArgumentException("Invalid forwards & up combination");
    }
    
    static class CacheLoader extends com.google.common.cache.CacheLoader<BlockPos, BlockWorldState>
    {
        private final World world;
        private final boolean forceLoad;
        
        public CacheLoader(final World worldIn, final boolean forceLoadIn) {
            this.world = worldIn;
            this.forceLoad = forceLoadIn;
        }
        
        public BlockWorldState load(final BlockPos p_load_1_) throws Exception {
            return new BlockWorldState(this.world, p_load_1_, this.forceLoad);
        }
    }
    
    public static class PatternHelper
    {
        private final BlockPos frontTopLeft;
        private final EnumFacing forwards;
        private final EnumFacing up;
        private final LoadingCache<BlockPos, BlockWorldState> lcache;
        private final int width;
        private final int height;
        private final int depth;
        
        public PatternHelper(final BlockPos posIn, final EnumFacing fingerIn, final EnumFacing thumbIn, final LoadingCache<BlockPos, BlockWorldState> lcacheIn, final int widthIn, final int heightIn, final int depthIn) {
            this.frontTopLeft = posIn;
            this.forwards = fingerIn;
            this.up = thumbIn;
            this.lcache = lcacheIn;
            this.width = widthIn;
            this.height = heightIn;
            this.depth = depthIn;
        }
        
        public BlockPos getFrontTopLeft() {
            return this.frontTopLeft;
        }
        
        public EnumFacing getForwards() {
            return this.forwards;
        }
        
        public EnumFacing getUp() {
            return this.up;
        }
        
        public int getWidth() {
            return this.width;
        }
        
        public int getHeight() {
            return this.height;
        }
        
        public BlockWorldState translateOffset(final int palmOffset, final int thumbOffset, final int fingerOffset) {
            return (BlockWorldState)this.lcache.getUnchecked((Object)BlockPattern.translateOffset(this.frontTopLeft, this.getForwards(), this.getUp(), palmOffset, thumbOffset, fingerOffset));
        }
        
        @Override
        public String toString() {
            return MoreObjects.toStringHelper((Object)this).add("up", (Object)this.up).add("forwards", (Object)this.forwards).add("frontTopLeft", (Object)this.frontTopLeft).toString();
        }
    }
}
