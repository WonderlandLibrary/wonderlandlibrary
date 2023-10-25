/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.Arrays
 *  it.unimi.dsi.fastutil.Swapper
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.ints.IntComparator
 *  it.unimi.dsi.fastutil.ints.IntList
 *  it.unimi.dsi.fastutil.ints.IntOpenHashSet
 */
package net.minecraft.client.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.Swapper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SuffixArray<T> {
    private static final boolean field_194062_b = Boolean.parseBoolean(System.getProperty("SuffixArray.printComparisons", "false"));
    private static final boolean field_194063_c = Boolean.parseBoolean(System.getProperty("SuffixArray.printArray", "false"));
    private static final Logger field_194064_d = LogManager.getLogger();
    protected final List<T> field_194061_a = Lists.newArrayList();
    private final IntList field_194065_e = new IntArrayList();
    private final IntList field_194066_f = new IntArrayList();
    private IntList field_194067_g = new IntArrayList();
    private IntList field_194068_h = new IntArrayList();
    private int field_194069_i;

    public void func_194057_a(T p_194057_1_, String p_194057_2_) {
        this.field_194069_i = Math.max(this.field_194069_i, p_194057_2_.length());
        int i = this.field_194061_a.size();
        this.field_194061_a.add(p_194057_1_);
        this.field_194066_f.add(this.field_194065_e.size());
        for (int j = 0; j < p_194057_2_.length(); ++j) {
            this.field_194067_g.add(i);
            this.field_194068_h.add(j);
            this.field_194065_e.add((int)p_194057_2_.charAt(j));
        }
        this.field_194067_g.add(i);
        this.field_194068_h.add(p_194057_2_.length());
        this.field_194065_e.add(-1);
    }

    public void func_194058_a() {
        int i = this.field_194065_e.size();
        int[] aint = new int[i];
        final int[] aint1 = new int[i];
        final int[] aint2 = new int[i];
        int[] aint3 = new int[i];
        IntComparator intcomparator = new IntComparator(){

            public int compare(int p_compare_1_, int p_compare_2_) {
                return aint1[p_compare_1_] == aint1[p_compare_2_] ? Integer.compare(aint2[p_compare_1_], aint2[p_compare_2_]) : Integer.compare(aint1[p_compare_1_], aint1[p_compare_2_]);
            }

            public int compare(Integer p_compare_1_, Integer p_compare_2_) {
                return this.compare((int)p_compare_1_, (int)p_compare_2_);
            }
        };
        Swapper swapper = (p_194054_3_, p_194054_4_) -> {
            if (p_194054_3_ != p_194054_4_) {
                int i2 = aint1[p_194054_3_];
                aint1[p_194054_3_] = aint1[p_194054_4_];
                aint1[p_194054_4_] = i2;
                i2 = aint2[p_194054_3_];
                aint2[p_194054_3_] = aint2[p_194054_4_];
                aint2[p_194054_4_] = i2;
                i2 = aint3[p_194054_3_];
                aint3[p_194054_3_] = aint3[p_194054_4_];
                aint3[p_194054_4_] = i2;
            }
        };
        for (int j = 0; j < i; ++j) {
            aint[j] = this.field_194065_e.getInt(j);
        }
        int k1 = 1;
        int k = Math.min(i, this.field_194069_i);
        while (k1 * 2 < k) {
            int l = 0;
            while (l < i) {
                aint1[l] = aint[l];
                aint2[l] = l + k1 < i ? aint[l + k1] : -2;
                aint3[l] = l++;
            }
            it.unimi.dsi.fastutil.Arrays.quickSort((int)0, (int)i, (IntComparator)intcomparator, (Swapper)swapper);
            for (int l1 = 0; l1 < i; ++l1) {
                aint[aint3[l1]] = l1 > 0 && aint1[l1] == aint1[l1 - 1] && aint2[l1] == aint2[l1 - 1] ? aint[aint3[l1 - 1]] : l1;
            }
            k1 *= 2;
        }
        IntList intlist1 = this.field_194067_g;
        IntList intlist = this.field_194068_h;
        this.field_194067_g = new IntArrayList(intlist1.size());
        this.field_194068_h = new IntArrayList(intlist.size());
        for (int i1 = 0; i1 < i; ++i1) {
            int j1 = aint3[i1];
            this.field_194067_g.add(intlist1.getInt(j1));
            this.field_194068_h.add(intlist.getInt(j1));
        }
        if (field_194063_c) {
            this.func_194060_b();
        }
    }

    private void func_194060_b() {
        for (int i2 = 0; i2 < this.field_194067_g.size(); ++i2) {
            field_194064_d.debug("{} {}", i2, this.func_194059_a(i2));
        }
        field_194064_d.debug("");
    }

    private String func_194059_a(int p_194059_1_) {
        int i2 = this.field_194068_h.getInt(p_194059_1_);
        int j2 = this.field_194066_f.getInt(this.field_194067_g.getInt(p_194059_1_));
        StringBuilder stringbuilder = new StringBuilder();
        int k2 = 0;
        while (j2 + k2 < this.field_194065_e.size()) {
            int l2;
            if (k2 == i2) {
                stringbuilder.append('^');
            }
            if ((l2 = ((Integer)this.field_194065_e.get(j2 + k2)).intValue()) == -1) break;
            stringbuilder.append((char)l2);
            ++k2;
        }
        return stringbuilder.toString();
    }

    private int func_194056_a(String p_194056_1_, int p_194056_2_) {
        int i2 = this.field_194066_f.getInt(this.field_194067_g.getInt(p_194056_2_));
        int j2 = this.field_194068_h.getInt(p_194056_2_);
        for (int k2 = 0; k2 < p_194056_1_.length(); ++k2) {
            char c1;
            int l2 = this.field_194065_e.getInt(i2 + j2 + k2);
            if (l2 == -1) {
                return 1;
            }
            char c0 = p_194056_1_.charAt(k2);
            if (c0 < (c1 = (char)l2)) {
                return -1;
            }
            if (c0 <= c1) continue;
            return 1;
        }
        return 0;
    }

    public List<T> func_194055_a(String p_194055_1_) {
        int i2 = this.field_194067_g.size();
        int j2 = 0;
        int k2 = i2;
        while (j2 < k2) {
            int l2 = j2 + (k2 - j2) / 2;
            int i3 = this.func_194056_a(p_194055_1_, l2);
            if (field_194062_b) {
                field_194064_d.debug("comparing lower \"{}\" with {} \"{}\": {}", p_194055_1_, l2, this.func_194059_a(l2), i3);
            }
            if (i3 > 0) {
                j2 = l2 + 1;
                continue;
            }
            k2 = l2;
        }
        if (j2 >= 0 && j2 < i2) {
            int i4 = j2;
            k2 = i2;
            while (j2 < k2) {
                int j4 = j2 + (k2 - j2) / 2;
                int j3 = this.func_194056_a(p_194055_1_, j4);
                if (field_194062_b) {
                    field_194064_d.debug("comparing upper \"{}\" with {} \"{}\": {}", p_194055_1_, j4, this.func_194059_a(j4), j3);
                }
                if (j3 >= 0) {
                    j2 = j4 + 1;
                    continue;
                }
                k2 = j4;
            }
            int k4 = j2;
            IntOpenHashSet intset = new IntOpenHashSet();
            for (int k3 = i4; k3 < k4; ++k3) {
                intset.add(this.field_194067_g.getInt(k3));
            }
            int[] aint4 = intset.toIntArray();
            Arrays.sort(aint4);
            LinkedHashSet<T> set = Sets.newLinkedHashSet();
            for (int l3 : aint4) {
                set.add(this.field_194061_a.get(l3));
            }
            return Lists.newArrayList(set);
        }
        return Collections.emptyList();
    }
}

