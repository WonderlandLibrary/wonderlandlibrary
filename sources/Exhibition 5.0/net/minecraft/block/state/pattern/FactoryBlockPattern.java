// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.state.pattern;

import java.util.Iterator;
import java.util.ArrayList;
import java.lang.reflect.Array;
import com.google.common.base.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;
import com.google.common.base.Joiner;

public class FactoryBlockPattern
{
    private static final Joiner field_177667_a;
    private final List field_177665_b;
    private final Map field_177666_c;
    private int field_177663_d;
    private int field_177664_e;
    private static final String __OBFID = "CL_00002021";
    
    private FactoryBlockPattern() {
        this.field_177665_b = Lists.newArrayList();
        (this.field_177666_c = Maps.newHashMap()).put(' ', Predicates.alwaysTrue());
    }
    
    public FactoryBlockPattern aisle(final String... p_177659_1_) {
        if (ArrayUtils.isEmpty((Object[])p_177659_1_) || StringUtils.isEmpty((CharSequence)p_177659_1_[0])) {
            throw new IllegalArgumentException("Empty pattern for aisle");
        }
        if (this.field_177665_b.isEmpty()) {
            this.field_177663_d = p_177659_1_.length;
            this.field_177664_e = p_177659_1_[0].length();
        }
        if (p_177659_1_.length != this.field_177663_d) {
            throw new IllegalArgumentException("Expected aisle with height of " + this.field_177663_d + ", but was given one with a height of " + p_177659_1_.length + ")");
        }
        for (final String var5 : p_177659_1_) {
            if (var5.length() != this.field_177664_e) {
                throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.field_177664_e + ", found one with " + var5.length() + ")");
            }
            for (final char var9 : var5.toCharArray()) {
                if (!this.field_177666_c.containsKey(var9)) {
                    this.field_177666_c.put(var9, null);
                }
            }
        }
        this.field_177665_b.add(p_177659_1_);
        return this;
    }
    
    public static FactoryBlockPattern start() {
        return new FactoryBlockPattern();
    }
    
    public FactoryBlockPattern where(final char p_177662_1_, final Predicate p_177662_2_) {
        this.field_177666_c.put(p_177662_1_, p_177662_2_);
        return this;
    }
    
    public BlockPattern build() {
        return new BlockPattern(this.func_177658_c());
    }
    
    private Predicate[][][] func_177658_c() {
        this.func_177657_d();
        final Predicate[][][] var1 = (Predicate[][][])Array.newInstance(Predicate.class, this.field_177665_b.size(), this.field_177663_d, this.field_177664_e);
        for (int var2 = 0; var2 < this.field_177665_b.size(); ++var2) {
            for (int var3 = 0; var3 < this.field_177663_d; ++var3) {
                for (int var4 = 0; var4 < this.field_177664_e; ++var4) {
                    var1[var2][var3][var4] = this.field_177666_c.get(((String[])this.field_177665_b.get(var2))[var3].charAt(var4));
                }
            }
        }
        return var1;
    }
    
    private void func_177657_d() {
        final ArrayList var1 = Lists.newArrayList();
        for (final Map.Entry var3 : this.field_177666_c.entrySet()) {
            if (var3.getValue() == null) {
                var1.add(var3.getKey());
            }
        }
        if (!var1.isEmpty()) {
            throw new IllegalStateException("Predicates for character(s) " + FactoryBlockPattern.field_177667_a.join((Iterable)var1) + " are missing");
        }
    }
    
    static {
        field_177667_a = Joiner.on(",");
    }
}
