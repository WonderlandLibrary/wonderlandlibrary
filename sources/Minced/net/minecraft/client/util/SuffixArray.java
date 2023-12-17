// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.util;

import org.apache.logging.log4j.LogManager;
import java.util.Set;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Collections;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.Swapper;
import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class SuffixArray<T>
{
    private static final boolean DEBUG_PRINT_COMPARISONS;
    private static final boolean DEBUG_PRINT_ARRAY;
    private static final Logger LOGGER;
    protected final List<T> list;
    private final IntList chars;
    private final IntList wordStarts;
    private IntList suffixToT;
    private IntList offsets;
    private int maxStringLength;
    
    public SuffixArray() {
        this.list = (List<T>)Lists.newArrayList();
        this.chars = (IntList)new IntArrayList();
        this.wordStarts = (IntList)new IntArrayList();
        this.suffixToT = (IntList)new IntArrayList();
        this.offsets = (IntList)new IntArrayList();
    }
    
    public void add(final T p_194057_1_, final String p_194057_2_) {
        this.maxStringLength = Math.max(this.maxStringLength, p_194057_2_.length());
        final int i = this.list.size();
        this.list.add(p_194057_1_);
        this.wordStarts.add(this.chars.size());
        for (int j = 0; j < p_194057_2_.length(); ++j) {
            this.suffixToT.add(i);
            this.offsets.add(j);
            this.chars.add((int)p_194057_2_.charAt(j));
        }
        this.suffixToT.add(i);
        this.offsets.add(p_194057_2_.length());
        this.chars.add(-1);
    }
    
    public void generate() {
        final int i = this.chars.size();
        final int[] aint = new int[i];
        final int[] aint2 = new int[i];
        final int[] aint3 = new int[i];
        final int[] aint4 = new int[i];
        final IntComparator intcomparator = (IntComparator)new IntComparator() {
            public int compare(final int p_compare_1_, final int p_compare_2_) {
                return (aint2[p_compare_1_] == aint2[p_compare_2_]) ? Integer.compare(aint3[p_compare_1_], aint3[p_compare_2_]) : Integer.compare(aint2[p_compare_1_], aint2[p_compare_2_]);
            }
            
            public int compare(final Integer p_compare_1_, final Integer p_compare_2_) {
                return this.compare((int)p_compare_1_, (int)p_compare_2_);
            }
        };
        final Swapper swapper = (p_194054_3_, p_194054_4_) -> {
            if (p_194054_3_ != p_194054_4_) {
                int i2 = aint2[p_194054_3_];
                aint2[p_194054_3_] = aint2[p_194054_4_];
                aint2[p_194054_4_] = i2;
                i2 = aint3[p_194054_3_];
                aint3[p_194054_3_] = aint3[p_194054_4_];
                aint3[p_194054_4_] = i2;
                i2 = aint4[p_194054_3_];
                aint4[p_194054_3_] = aint4[p_194054_4_];
                aint4[p_194054_4_] = i2;
            }
        };
        for (int j = 0; j < i; ++j) {
            aint[j] = this.chars.getInt(j);
        }
        for (int k1 = 1, l = Math.min(i, this.maxStringLength); k1 * 2 < l; k1 *= 2) {
            for (int m = 0; m < i; aint4[m] = m++) {
                aint2[m] = aint[m];
                aint3[m] = ((m + k1 < i) ? aint[m + k1] : -2);
            }
            Arrays.quickSort(0, i, intcomparator, swapper);
            for (int l2 = 0; l2 < i; ++l2) {
                if (l2 > 0 && aint2[l2] == aint2[l2 - 1] && aint3[l2] == aint3[l2 - 1]) {
                    aint[aint4[l2]] = aint[aint4[l2 - 1]];
                }
                else {
                    aint[aint4[l2]] = l2;
                }
            }
        }
        final IntList intlist1 = this.suffixToT;
        final IntList intlist2 = this.offsets;
        this.suffixToT = (IntList)new IntArrayList(intlist1.size());
        this.offsets = (IntList)new IntArrayList(intlist2.size());
        for (final int j2 : aint4) {
            this.suffixToT.add(intlist1.getInt(j2));
            this.offsets.add(intlist2.getInt(j2));
        }
        if (SuffixArray.DEBUG_PRINT_ARRAY) {
            this.printArray();
        }
    }
    
    private void printArray() {
        for (int i2 = 0; i2 < this.suffixToT.size(); ++i2) {
            SuffixArray.LOGGER.debug("{} {}", (Object)i2, (Object)this.getString(i2));
        }
        SuffixArray.LOGGER.debug("");
    }
    
    private String getString(final int p_194059_1_) {
        final int i2 = this.offsets.getInt(p_194059_1_);
        final int j2 = this.wordStarts.getInt(this.suffixToT.getInt(p_194059_1_));
        final StringBuilder stringbuilder = new StringBuilder();
        for (int k2 = 0; j2 + k2 < this.chars.size(); ++k2) {
            if (k2 == i2) {
                stringbuilder.append('^');
            }
            final int l2 = (int)this.chars.get(j2 + k2);
            if (l2 == -1) {
                break;
            }
            stringbuilder.append((char)l2);
        }
        return stringbuilder.toString();
    }
    
    private int compare(final String p_194056_1_, final int p_194056_2_) {
        final int i2 = this.wordStarts.getInt(this.suffixToT.getInt(p_194056_2_));
        final int j2 = this.offsets.getInt(p_194056_2_);
        for (int k2 = 0; k2 < p_194056_1_.length(); ++k2) {
            final int l2 = this.chars.getInt(i2 + j2 + k2);
            if (l2 == -1) {
                return 1;
            }
            final char c0 = p_194056_1_.charAt(k2);
            final char c2 = (char)l2;
            if (c0 < c2) {
                return -1;
            }
            if (c0 > c2) {
                return 1;
            }
        }
        return 0;
    }
    
    public List<T> search(final String p_194055_1_) {
        final int i2 = this.suffixToT.size();
        int j2 = 0;
        int k2 = i2;
        while (j2 < k2) {
            final int l2 = j2 + (k2 - j2) / 2;
            final int i3 = this.compare(p_194055_1_, l2);
            if (SuffixArray.DEBUG_PRINT_COMPARISONS) {
                SuffixArray.LOGGER.debug("comparing lower \"{}\" with {} \"{}\": {}", (Object)p_194055_1_, (Object)l2, (Object)this.getString(l2), (Object)i3);
            }
            if (i3 > 0) {
                j2 = l2 + 1;
            }
            else {
                k2 = l2;
            }
        }
        if (j2 >= 0 && j2 < i2) {
            final int i4 = j2;
            k2 = i2;
            while (j2 < k2) {
                final int j3 = j2 + (k2 - j2) / 2;
                final int j4 = this.compare(p_194055_1_, j3);
                if (SuffixArray.DEBUG_PRINT_COMPARISONS) {
                    SuffixArray.LOGGER.debug("comparing upper \"{}\" with {} \"{}\": {}", (Object)p_194055_1_, (Object)j3, (Object)this.getString(j3), (Object)j4);
                }
                if (j4 >= 0) {
                    j2 = j3 + 1;
                }
                else {
                    k2 = j3;
                }
            }
            final int k3 = j2;
            final IntSet intset = (IntSet)new IntOpenHashSet();
            for (int k4 = i4; k4 < k3; ++k4) {
                intset.add(this.suffixToT.getInt(k4));
            }
            final int[] aint4 = intset.toIntArray();
            java.util.Arrays.sort(aint4);
            final Set<T> set = (Set<T>)Sets.newLinkedHashSet();
            for (final int l3 : aint4) {
                set.add(this.list.get(l3));
            }
            return (List<T>)Lists.newArrayList((Iterable)set);
        }
        return Collections.emptyList();
    }
    
    static {
        DEBUG_PRINT_COMPARISONS = Boolean.parseBoolean(System.getProperty("SuffixArray.printComparisons", "false"));
        DEBUG_PRINT_ARRAY = Boolean.parseBoolean(System.getProperty("SuffixArray.printArray", "false"));
        LOGGER = LogManager.getLogger();
    }
}
