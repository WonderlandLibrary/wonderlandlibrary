/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.base.Predicate
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.cache.CacheLoader
 *  com.google.common.cache.LoadingCache
 */
package net.minecraft.block.state.pattern;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class BlockPattern {
    private final Predicate<BlockWorldState>[][][] blockMatches;
    private final int palmLength;
    private final int fingerLength;
    private final int thumbLength;

    public int getPalmLength() {
        return this.palmLength;
    }

    public BlockPattern(Predicate<BlockWorldState>[][][] predicateArray) {
        this.blockMatches = predicateArray;
        this.fingerLength = predicateArray.length;
        if (this.fingerLength > 0) {
            this.thumbLength = predicateArray[0].length;
            this.palmLength = this.thumbLength > 0 ? predicateArray[0][0].length : 0;
        } else {
            this.thumbLength = 0;
            this.palmLength = 0;
        }
    }

    public PatternHelper match(World world, BlockPos blockPos) {
        LoadingCache<BlockPos, BlockWorldState> loadingCache = BlockPattern.func_181627_a(world, false);
        int n = Math.max(Math.max(this.palmLength, this.thumbLength), this.fingerLength);
        for (BlockPos blockPos2 : BlockPos.getAllInBox(blockPos, blockPos.add(n - 1, n - 1, n - 1))) {
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n2 = enumFacingArray.length;
            int n3 = 0;
            while (n3 < n2) {
                EnumFacing enumFacing = enumFacingArray[n3];
                EnumFacing[] enumFacingArray2 = EnumFacing.values();
                int n4 = enumFacingArray2.length;
                int n5 = 0;
                while (n5 < n4) {
                    PatternHelper patternHelper;
                    EnumFacing enumFacing2 = enumFacingArray2[n5];
                    if (enumFacing2 != enumFacing && enumFacing2 != enumFacing.getOpposite() && (patternHelper = this.checkPatternAt(blockPos2, enumFacing, enumFacing2, loadingCache)) != null) {
                        return patternHelper;
                    }
                    ++n5;
                }
                ++n3;
            }
        }
        return null;
    }

    private PatternHelper checkPatternAt(BlockPos blockPos, EnumFacing enumFacing, EnumFacing enumFacing2, LoadingCache<BlockPos, BlockWorldState> loadingCache) {
        int n = 0;
        while (n < this.palmLength) {
            int n2 = 0;
            while (n2 < this.thumbLength) {
                int n3 = 0;
                while (n3 < this.fingerLength) {
                    if (!this.blockMatches[n3][n2][n].apply((Object)((BlockWorldState)loadingCache.getUnchecked((Object)BlockPattern.translateOffset(blockPos, enumFacing, enumFacing2, n, n2, n3))))) {
                        return null;
                    }
                    ++n3;
                }
                ++n2;
            }
            ++n;
        }
        return new PatternHelper(blockPos, enumFacing, enumFacing2, loadingCache, this.palmLength, this.thumbLength, this.fingerLength);
    }

    public int getThumbLength() {
        return this.thumbLength;
    }

    protected static BlockPos translateOffset(BlockPos blockPos, EnumFacing enumFacing, EnumFacing enumFacing2, int n, int n2, int n3) {
        if (enumFacing != enumFacing2 && enumFacing != enumFacing2.getOpposite()) {
            Vec3i vec3i = new Vec3i(enumFacing.getFrontOffsetX(), enumFacing.getFrontOffsetY(), enumFacing.getFrontOffsetZ());
            Vec3i vec3i2 = new Vec3i(enumFacing2.getFrontOffsetX(), enumFacing2.getFrontOffsetY(), enumFacing2.getFrontOffsetZ());
            Vec3i vec3i3 = vec3i.crossProduct(vec3i2);
            return blockPos.add(vec3i2.getX() * -n2 + vec3i3.getX() * n + vec3i.getX() * n3, vec3i2.getY() * -n2 + vec3i3.getY() * n + vec3i.getY() * n3, vec3i2.getZ() * -n2 + vec3i3.getZ() * n + vec3i.getZ() * n3);
        }
        throw new IllegalArgumentException("Invalid forwards & up combination");
    }

    public static LoadingCache<BlockPos, BlockWorldState> func_181627_a(World world, boolean bl) {
        return CacheBuilder.newBuilder().build((com.google.common.cache.CacheLoader)new CacheLoader(world, bl));
    }

    public static class PatternHelper {
        private final BlockPos pos;
        private final LoadingCache<BlockPos, BlockWorldState> lcache;
        private final int field_181120_e;
        private final EnumFacing finger;
        private final int field_181121_f;
        private final EnumFacing thumb;
        private final int field_181122_g;

        public EnumFacing getFinger() {
            return this.finger;
        }

        public BlockPos func_181117_a() {
            return this.pos;
        }

        public EnumFacing getThumb() {
            return this.thumb;
        }

        public PatternHelper(BlockPos blockPos, EnumFacing enumFacing, EnumFacing enumFacing2, LoadingCache<BlockPos, BlockWorldState> loadingCache, int n, int n2, int n3) {
            this.pos = blockPos;
            this.finger = enumFacing;
            this.thumb = enumFacing2;
            this.lcache = loadingCache;
            this.field_181120_e = n;
            this.field_181121_f = n2;
            this.field_181122_g = n3;
        }

        public String toString() {
            return Objects.toStringHelper((Object)this).add("up", (Object)this.thumb).add("forwards", (Object)this.finger).add("frontTopLeft", (Object)this.pos).toString();
        }

        public int func_181118_d() {
            return this.field_181120_e;
        }

        public BlockWorldState translateOffset(int n, int n2, int n3) {
            return (BlockWorldState)this.lcache.getUnchecked((Object)BlockPattern.translateOffset(this.pos, this.getFinger(), this.getThumb(), n, n2, n3));
        }

        public int func_181119_e() {
            return this.field_181121_f;
        }
    }

    static class CacheLoader
    extends com.google.common.cache.CacheLoader<BlockPos, BlockWorldState> {
        private final boolean field_181626_b;
        private final World world;

        public CacheLoader(World world, boolean bl) {
            this.world = world;
            this.field_181626_b = bl;
        }

        public BlockWorldState load(BlockPos blockPos) throws Exception {
            return new BlockWorldState(this.world, blockPos, this.field_181626_b);
        }
    }
}

