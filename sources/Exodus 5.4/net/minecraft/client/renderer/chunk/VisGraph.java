/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Set;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntegerCache;

public class VisGraph {
    private int field_178611_f = 4096;
    private static final int[] field_178613_e;
    private static final int field_178614_b;
    private static final int field_178616_a;
    private final BitSet field_178612_d = new BitSet(4096);
    private static final int field_178615_c;

    private static int getIndex(BlockPos blockPos) {
        return VisGraph.getIndex(blockPos.getX() & 0xF, blockPos.getY() & 0xF, blockPos.getZ() & 0xF);
    }

    public SetVisibility computeVisibility() {
        SetVisibility setVisibility = new SetVisibility();
        if (4096 - this.field_178611_f < 256) {
            setVisibility.setAllVisible(true);
        } else if (this.field_178611_f == 0) {
            setVisibility.setAllVisible(false);
        } else {
            int[] nArray = field_178613_e;
            int n = field_178613_e.length;
            int n2 = 0;
            while (n2 < n) {
                int n3 = nArray[n2];
                if (!this.field_178612_d.get(n3)) {
                    setVisibility.setManyVisible(this.func_178604_a(n3));
                }
                ++n2;
            }
        }
        return setVisibility;
    }

    public Set<EnumFacing> func_178609_b(BlockPos blockPos) {
        return this.func_178604_a(VisGraph.getIndex(blockPos));
    }

    private int func_178603_a(int n, EnumFacing enumFacing) {
        switch (enumFacing) {
            case DOWN: {
                if ((n >> 8 & 0xF) == 0) {
                    return -1;
                }
                return n - field_178615_c;
            }
            case UP: {
                if ((n >> 8 & 0xF) == 15) {
                    return -1;
                }
                return n + field_178615_c;
            }
            case NORTH: {
                if ((n >> 4 & 0xF) == 0) {
                    return -1;
                }
                return n - field_178614_b;
            }
            case SOUTH: {
                if ((n >> 4 & 0xF) == 15) {
                    return -1;
                }
                return n + field_178614_b;
            }
            case WEST: {
                if ((n >> 0 & 0xF) == 0) {
                    return -1;
                }
                return n - field_178616_a;
            }
            case EAST: {
                if ((n >> 0 & 0xF) == 15) {
                    return -1;
                }
                return n + field_178616_a;
            }
        }
        return -1;
    }

    public void func_178606_a(BlockPos blockPos) {
        this.field_178612_d.set(VisGraph.getIndex(blockPos), true);
        --this.field_178611_f;
    }

    static {
        field_178616_a = (int)Math.pow(16.0, 0.0);
        field_178614_b = (int)Math.pow(16.0, 1.0);
        field_178615_c = (int)Math.pow(16.0, 2.0);
        field_178613_e = new int[1352];
        boolean bl = false;
        int n = 15;
        int n2 = 0;
        int n3 = 0;
        while (n3 < 16) {
            int n4 = 0;
            while (n4 < 16) {
                int n5 = 0;
                while (n5 < 16) {
                    if (n3 == 0 || n3 == 15 || n4 == 0 || n4 == 15 || n5 == 0 || n5 == 15) {
                        VisGraph.field_178613_e[n2++] = VisGraph.getIndex(n3, n4, n5);
                    }
                    ++n5;
                }
                ++n4;
            }
            ++n3;
        }
    }

    private static int getIndex(int n, int n2, int n3) {
        return n << 0 | n2 << 8 | n3 << 4;
    }

    private Set<EnumFacing> func_178604_a(int n) {
        EnumSet<EnumFacing> enumSet = EnumSet.noneOf(EnumFacing.class);
        LinkedList linkedList = Lists.newLinkedList();
        linkedList.add(IntegerCache.func_181756_a(n));
        this.field_178612_d.set(n, true);
        while (!linkedList.isEmpty()) {
            int n2 = (Integer)linkedList.poll();
            this.func_178610_a(n2, enumSet);
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n3 = enumFacingArray.length;
            int n4 = 0;
            while (n4 < n3) {
                EnumFacing enumFacing = enumFacingArray[n4];
                int n5 = this.func_178603_a(n2, enumFacing);
                if (n5 >= 0 && !this.field_178612_d.get(n5)) {
                    this.field_178612_d.set(n5, true);
                    linkedList.add(IntegerCache.func_181756_a(n5));
                }
                ++n4;
            }
        }
        return enumSet;
    }

    private void func_178610_a(int n, Set<EnumFacing> set) {
        int n2 = n >> 0 & 0xF;
        if (n2 == 0) {
            set.add(EnumFacing.WEST);
        } else if (n2 == 15) {
            set.add(EnumFacing.EAST);
        }
        int n3 = n >> 8 & 0xF;
        if (n3 == 0) {
            set.add(EnumFacing.DOWN);
        } else if (n3 == 15) {
            set.add(EnumFacing.UP);
        }
        int n4 = n >> 4 & 0xF;
        if (n4 == 0) {
            set.add(EnumFacing.NORTH);
        } else if (n4 == 15) {
            set.add(EnumFacing.SOUTH);
        }
    }
}

