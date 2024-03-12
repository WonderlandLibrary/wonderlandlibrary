// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.render;

import java.util.List;
import java.util.ArrayList;
import net.minecraft.util.ClassInheritanceMultiMap;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import java.util.Map;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.Chunk;
import java.util.ConcurrentModificationException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class ChunkVisibility
{
    public static final int MASK_FACINGS = 63;
    public static final EnumFacing[][] enumFacingArrays;
    public static final EnumFacing[][] enumFacingOppositeArrays;
    private static int counter;
    private static int iMaxStatic;
    private static int iMaxStaticFinal;
    private static World worldLast;
    private static int pcxLast;
    private static int pczLast;
    
    public static int getMaxChunkY(final World world, final Entity viewEntity, final int renderDistanceChunks) {
        final int i = MathHelper.floor_double(viewEntity.posX) >> 4;
        final int j = MathHelper.floor_double(viewEntity.posY) >> 4;
        final int k = MathHelper.floor_double(viewEntity.posZ) >> 4;
        final Chunk chunk = world.getChunkFromChunkCoords(i, k);
        int l = i - renderDistanceChunks;
        int i2 = i + renderDistanceChunks;
        int j2 = k - renderDistanceChunks;
        int k2 = k + renderDistanceChunks;
        if (world != ChunkVisibility.worldLast || i != ChunkVisibility.pcxLast || k != ChunkVisibility.pczLast) {
            ChunkVisibility.counter = 0;
            ChunkVisibility.iMaxStaticFinal = 16;
            ChunkVisibility.worldLast = world;
            ChunkVisibility.pcxLast = i;
            ChunkVisibility.pczLast = k;
        }
        if (ChunkVisibility.counter == 0) {
            ChunkVisibility.iMaxStatic = -1;
        }
        int l2 = ChunkVisibility.iMaxStatic;
        switch (ChunkVisibility.counter) {
            case 0: {
                i2 = i;
                k2 = k;
                break;
            }
            case 1: {
                l = i;
                k2 = k;
                break;
            }
            case 2: {
                i2 = i;
                j2 = k;
                break;
            }
            case 3: {
                l = i;
                j2 = k;
                break;
            }
        }
        for (int i3 = l; i3 < i2; ++i3) {
            for (int j3 = j2; j3 < k2; ++j3) {
                final Chunk chunk2 = world.getChunkFromChunkCoords(i3, j3);
                if (!chunk2.isEmpty()) {
                    final ExtendedBlockStorage[] aextendedblockstorage = chunk2.getBlockStorageArray();
                    int k3 = aextendedblockstorage.length - 1;
                    while (k3 > l2) {
                        final ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[k3];
                        if (extendedblockstorage != null && !extendedblockstorage.isEmpty()) {
                            if (k3 > l2) {
                                l2 = k3;
                                break;
                            }
                            break;
                        }
                        else {
                            --k3;
                        }
                    }
                    try {
                        final Map<BlockPos, TileEntity> map = chunk2.getTileEntityMap();
                        if (!map.isEmpty()) {
                            for (final BlockPos blockpos : map.keySet()) {
                                final int l3 = blockpos.getY() >> 4;
                                if (l3 > l2) {
                                    l2 = l3;
                                }
                            }
                        }
                    }
                    catch (ConcurrentModificationException ex) {}
                    final ClassInheritanceMultiMap<Entity>[] classinheritancemultimap = chunk2.getEntityLists();
                    int i4 = classinheritancemultimap.length - 1;
                    while (i4 > l2) {
                        final ClassInheritanceMultiMap<Entity> classinheritancemultimap2 = classinheritancemultimap[i4];
                        if (!classinheritancemultimap2.isEmpty() && (chunk2 != chunk || i4 != j || classinheritancemultimap2.size() != 1)) {
                            if (i4 > l2) {
                                l2 = i4;
                                break;
                            }
                            break;
                        }
                        else {
                            --i4;
                        }
                    }
                }
            }
        }
        if (ChunkVisibility.counter < 3) {
            ChunkVisibility.iMaxStatic = l2;
            l2 = ChunkVisibility.iMaxStaticFinal;
        }
        else {
            ChunkVisibility.iMaxStaticFinal = l2;
            ChunkVisibility.iMaxStatic = -1;
        }
        ChunkVisibility.counter = (ChunkVisibility.counter + 1) % 4;
        return l2 << 4;
    }
    
    public static boolean isFinished() {
        return ChunkVisibility.counter == 0;
    }
    
    private static EnumFacing[][] makeEnumFacingArrays(final boolean opposite) {
        final int i = 64;
        final EnumFacing[][] aenumfacing = new EnumFacing[i][];
        for (int j = 0; j < i; ++j) {
            final List<EnumFacing> list = new ArrayList<EnumFacing>();
            for (int k = 0; k < EnumFacing.VALUES.length; ++k) {
                final EnumFacing enumfacing = EnumFacing.VALUES[k];
                final EnumFacing enumfacing2 = opposite ? enumfacing.getOpposite() : enumfacing;
                final int l = 1 << enumfacing2.ordinal();
                if ((j & l) != 0x0) {
                    list.add(enumfacing);
                }
            }
            final EnumFacing[] aenumfacing2 = list.toArray(new EnumFacing[list.size()]);
            aenumfacing[j] = aenumfacing2;
        }
        return aenumfacing;
    }
    
    public static EnumFacing[] getFacingsNotOpposite(final int setDisabled) {
        final int i = ~setDisabled & 0x3F;
        return ChunkVisibility.enumFacingOppositeArrays[i];
    }
    
    public static void reset() {
        ChunkVisibility.worldLast = null;
    }
    
    static {
        enumFacingArrays = makeEnumFacingArrays(false);
        enumFacingOppositeArrays = makeEnumFacingArrays(true);
        ChunkVisibility.counter = 0;
        ChunkVisibility.iMaxStatic = -1;
        ChunkVisibility.iMaxStaticFinal = 16;
        ChunkVisibility.worldLast = null;
        ChunkVisibility.pcxLast = Integer.MIN_VALUE;
        ChunkVisibility.pczLast = Integer.MIN_VALUE;
    }
}
