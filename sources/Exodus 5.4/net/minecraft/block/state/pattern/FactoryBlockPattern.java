/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.commons.lang3.StringUtils
 */
package net.minecraft.block.state.pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockPattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class FactoryBlockPattern {
    private static final Joiner COMMA_JOIN = Joiner.on((String)",");
    private final Map<Character, Predicate<BlockWorldState>> symbolMap;
    private int aisleHeight;
    private final List<String[]> depth = Lists.newArrayList();
    private int rowWidth;

    public BlockPattern build() {
        return new BlockPattern(this.makePredicateArray());
    }

    public FactoryBlockPattern where(char c, Predicate<BlockWorldState> predicate) {
        this.symbolMap.put(Character.valueOf(c), predicate);
        return this;
    }

    private Predicate<BlockWorldState>[][][] makePredicateArray() {
        this.checkMissingPredicates();
        Predicate[][][] predicateArray = (Predicate[][][])Array.newInstance(Predicate.class, this.depth.size(), this.aisleHeight, this.rowWidth);
        int n = 0;
        while (n < this.depth.size()) {
            int n2 = 0;
            while (n2 < this.aisleHeight) {
                int n3 = 0;
                while (n3 < this.rowWidth) {
                    predicateArray[n][n2][n3] = this.symbolMap.get(Character.valueOf(this.depth.get(n)[n2].charAt(n3)));
                    ++n3;
                }
                ++n2;
            }
            ++n;
        }
        return predicateArray;
    }

    public FactoryBlockPattern aisle(String ... stringArray) {
        if (!ArrayUtils.isEmpty((Object[])stringArray) && !StringUtils.isEmpty((CharSequence)stringArray[0])) {
            if (this.depth.isEmpty()) {
                this.aisleHeight = stringArray.length;
                this.rowWidth = stringArray[0].length();
            }
            if (stringArray.length != this.aisleHeight) {
                throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + stringArray.length + ")");
            }
            String[] stringArray2 = stringArray;
            int n = stringArray.length;
            int n2 = 0;
            while (n2 < n) {
                String string = stringArray2[n2];
                if (string.length() != this.rowWidth) {
                    throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + string.length() + ")");
                }
                char[] cArray = string.toCharArray();
                int n3 = cArray.length;
                int n4 = 0;
                while (n4 < n3) {
                    char c = cArray[n4];
                    if (!this.symbolMap.containsKey(Character.valueOf(c))) {
                        this.symbolMap.put(Character.valueOf(c), null);
                    }
                    ++n4;
                }
                ++n2;
            }
            this.depth.add(stringArray);
            return this;
        }
        throw new IllegalArgumentException("Empty pattern for aisle");
    }

    private FactoryBlockPattern() {
        this.symbolMap = Maps.newHashMap();
        this.symbolMap.put(Character.valueOf(' '), (Predicate<BlockWorldState>)Predicates.alwaysTrue());
    }

    private void checkMissingPredicates() {
        ArrayList arrayList = Lists.newArrayList();
        for (Map.Entry<Character, Predicate<BlockWorldState>> entry : this.symbolMap.entrySet()) {
            if (entry.getValue() != null) continue;
            arrayList.add(entry.getKey());
        }
        if (!arrayList.isEmpty()) {
            throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join((Iterable)arrayList) + " are missing");
        }
    }

    public static FactoryBlockPattern start() {
        return new FactoryBlockPattern();
    }
}

