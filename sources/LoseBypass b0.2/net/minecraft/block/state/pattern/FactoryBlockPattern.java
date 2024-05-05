/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.state.pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockPattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class FactoryBlockPattern {
    private static final Joiner COMMA_JOIN = Joiner.on(",");
    private final List<String[]> depth = Lists.newArrayList();
    private final Map<Character, Predicate<BlockWorldState>> symbolMap = Maps.newHashMap();
    private int aisleHeight;
    private int rowWidth;

    private FactoryBlockPattern() {
        this.symbolMap.put(Character.valueOf(' '), Predicates.alwaysTrue());
    }

    public FactoryBlockPattern aisle(String ... aisle) {
        if (ArrayUtils.isEmpty(aisle)) throw new IllegalArgumentException("Empty pattern for aisle");
        if (StringUtils.isEmpty(aisle[0])) throw new IllegalArgumentException("Empty pattern for aisle");
        if (this.depth.isEmpty()) {
            this.aisleHeight = aisle.length;
            this.rowWidth = aisle[0].length();
        }
        if (aisle.length != this.aisleHeight) {
            throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + aisle.length + ")");
        }
        String[] stringArray = aisle;
        int n = stringArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                this.depth.add(aisle);
                return this;
            }
            String s = stringArray[n2];
            if (s.length() != this.rowWidth) {
                throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + s.length() + ")");
            }
            for (char c0 : s.toCharArray()) {
                if (this.symbolMap.containsKey(Character.valueOf(c0))) continue;
                this.symbolMap.put(Character.valueOf(c0), null);
            }
            ++n2;
        }
    }

    public static FactoryBlockPattern start() {
        return new FactoryBlockPattern();
    }

    public FactoryBlockPattern where(char symbol, Predicate<BlockWorldState> blockMatcher) {
        this.symbolMap.put(Character.valueOf(symbol), blockMatcher);
        return this;
    }

    public BlockPattern build() {
        return new BlockPattern(this.makePredicateArray());
    }

    private Predicate<BlockWorldState>[][][] makePredicateArray() {
        this.checkMissingPredicates();
        Predicate[][][] predicate = (Predicate[][][])Array.newInstance(Predicate.class, this.depth.size(), this.aisleHeight, this.rowWidth);
        int i = 0;
        while (i < this.depth.size()) {
            for (int j = 0; j < this.aisleHeight; ++j) {
                for (int k = 0; k < this.rowWidth; ++k) {
                    predicate[i][j][k] = this.symbolMap.get(Character.valueOf(this.depth.get(i)[j].charAt(k)));
                }
            }
            ++i;
        }
        return predicate;
    }

    private void checkMissingPredicates() {
        ArrayList<Character> list = Lists.newArrayList();
        Iterator<Map.Entry<Character, Predicate<BlockWorldState>>> iterator = this.symbolMap.entrySet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                if (list.isEmpty()) return;
                throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join(list) + " are missing");
            }
            Map.Entry<Character, Predicate<BlockWorldState>> entry = iterator.next();
            if (entry.getValue() != null) continue;
            list.add(entry.getKey());
        }
    }
}

