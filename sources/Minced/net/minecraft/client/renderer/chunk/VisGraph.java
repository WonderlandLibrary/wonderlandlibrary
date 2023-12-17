// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.util.IntegerCache;
import java.util.ArrayDeque;
import java.util.EnumSet;
import net.minecraft.util.EnumFacing;
import java.util.Set;
import net.minecraft.util.math.BlockPos;
import java.util.BitSet;

public class VisGraph
{
    private static final int DX;
    private static final int DZ;
    private static final int DY;
    private final BitSet bitSet;
    private static final int[] INDEX_OF_EDGES;
    private int empty;
    
    public VisGraph() {
        this.bitSet = new BitSet(4096);
        this.empty = 4096;
    }
    
    public void setOpaqueCube(final BlockPos pos) {
        this.bitSet.set(getIndex(pos), true);
        --this.empty;
    }
    
    private static int getIndex(final BlockPos pos) {
        return getIndex(pos.getX() & 0xF, pos.getY() & 0xF, pos.getZ() & 0xF);
    }
    
    private static int getIndex(final int x, final int y, final int z) {
        return x << 0 | y << 8 | z << 4;
    }
    
    public SetVisibility computeVisibility() {
        final SetVisibility setvisibility = new SetVisibility();
        if (4096 - this.empty < 256) {
            setvisibility.setAllVisible(true);
        }
        else if (this.empty == 0) {
            setvisibility.setAllVisible(false);
        }
        else {
            for (final int i : VisGraph.INDEX_OF_EDGES) {
                if (!this.bitSet.get(i)) {
                    setvisibility.setManyVisible(this.floodFill(i));
                }
            }
        }
        return setvisibility;
    }
    
    public Set<EnumFacing> getVisibleFacings(final BlockPos pos) {
        return this.floodFill(getIndex(pos));
    }
    
    private Set<EnumFacing> floodFill(final int pos) {
        final Set<EnumFacing> set = EnumSet.noneOf(EnumFacing.class);
        final ArrayDeque arraydeque = new ArrayDeque(384);
        arraydeque.add(IntegerCache.getInteger(pos));
        this.bitSet.set(pos, true);
        while (!arraydeque.isEmpty()) {
            final int i = arraydeque.poll();
            this.addEdges(i, set);
            for (final EnumFacing enumfacing : EnumFacing.VALUES) {
                final int j = this.getNeighborIndexAtFace(i, enumfacing);
                if (j >= 0 && !this.bitSet.get(j)) {
                    this.bitSet.set(j, true);
                    arraydeque.add(IntegerCache.getInteger(j));
                }
            }
        }
        return set;
    }
    
    private void addEdges(final int pos, final Set<EnumFacing> p_178610_2_) {
        final int i = pos >> 0 & 0xF;
        if (i == 0) {
            p_178610_2_.add(EnumFacing.WEST);
        }
        else if (i == 15) {
            p_178610_2_.add(EnumFacing.EAST);
        }
        final int j = pos >> 8 & 0xF;
        if (j == 0) {
            p_178610_2_.add(EnumFacing.DOWN);
        }
        else if (j == 15) {
            p_178610_2_.add(EnumFacing.UP);
        }
        final int k = pos >> 4 & 0xF;
        if (k == 0) {
            p_178610_2_.add(EnumFacing.NORTH);
        }
        else if (k == 15) {
            p_178610_2_.add(EnumFacing.SOUTH);
        }
    }
    
    private int getNeighborIndexAtFace(final int pos, final EnumFacing facing) {
        switch (facing) {
            case DOWN: {
                if ((pos >> 8 & 0xF) == 0x0) {
                    return -1;
                }
                return pos - VisGraph.DY;
            }
            case UP: {
                if ((pos >> 8 & 0xF) == 0xF) {
                    return -1;
                }
                return pos + VisGraph.DY;
            }
            case NORTH: {
                if ((pos >> 4 & 0xF) == 0x0) {
                    return -1;
                }
                return pos - VisGraph.DZ;
            }
            case SOUTH: {
                if ((pos >> 4 & 0xF) == 0xF) {
                    return -1;
                }
                return pos + VisGraph.DZ;
            }
            case WEST: {
                if ((pos >> 0 & 0xF) == 0x0) {
                    return -1;
                }
                return pos - VisGraph.DX;
            }
            case EAST: {
                if ((pos >> 0 & 0xF) == 0xF) {
                    return -1;
                }
                return pos + VisGraph.DX;
            }
            default: {
                return -1;
            }
        }
    }
    
    static {
        DX = (int)Math.pow(16.0, 0.0);
        DZ = (int)Math.pow(16.0, 1.0);
        DY = (int)Math.pow(16.0, 2.0);
        INDEX_OF_EDGES = new int[1352];
        final int i = 0;
        final int j = 15;
        int k = 0;
        for (int l = 0; l < 16; ++l) {
            for (int i2 = 0; i2 < 16; ++i2) {
                for (int j2 = 0; j2 < 16; ++j2) {
                    if (l == 0 || l == 15 || i2 == 0 || i2 == 15 || j2 == 0 || j2 == 15) {
                        VisGraph.INDEX_OF_EDGES[k++] = getIndex(l, i2, j2);
                    }
                }
            }
        }
    }
}
