/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Set;
import net.minecraft.util.EnumFacing;

public class SetVisibility {
    private static final int field_178623_a = EnumFacing.values().length;
    private final BitSet field_178622_b = new BitSet(field_178623_a * field_178623_a);
    private static final String __OBFID = "CL_00002448";

    public void func_178620_a(Set p_178620_1_) {
        for (EnumFacing var3 : p_178620_1_) {
            for (EnumFacing var5 : p_178620_1_) {
                this.func_178619_a(var3, var5, true);
            }
        }
    }

    public void func_178619_a(EnumFacing p_178619_1_, EnumFacing p_178619_2_, boolean p_178619_3_) {
        this.field_178622_b.set(p_178619_1_.ordinal() + p_178619_2_.ordinal() * field_178623_a, p_178619_3_);
        this.field_178622_b.set(p_178619_2_.ordinal() + p_178619_1_.ordinal() * field_178623_a, p_178619_3_);
    }

    public void func_178618_a(boolean p_178618_1_) {
        this.field_178622_b.set(0, this.field_178622_b.size(), p_178618_1_);
    }

    public boolean func_178621_a(EnumFacing p_178621_1_, EnumFacing p_178621_2_) {
        return this.field_178622_b.get(p_178621_1_.ordinal() + p_178621_2_.ordinal() * field_178623_a);
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append(' ');
        for (EnumFacing var5 : EnumFacing.values()) {
            var1.append(' ').append(var5.toString().toUpperCase().charAt(0));
        }
        var1.append('\n');
        for (EnumFacing var5 : EnumFacing.values()) {
            var1.append(var5.toString().toUpperCase().charAt(0));
            for (EnumFacing var9 : EnumFacing.values()) {
                if (var5 == var9) {
                    var1.append("  ");
                    continue;
                }
                boolean var10 = this.func_178621_a(var5, var9);
                var1.append(' ').append(var10 ? (char)'Y' : 'n');
            }
            var1.append('\n');
        }
        return var1.toString();
    }
}

