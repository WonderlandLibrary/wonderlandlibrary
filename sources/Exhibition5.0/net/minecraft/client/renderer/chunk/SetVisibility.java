// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.chunk;

import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import java.util.Set;
import java.util.BitSet;

public class SetVisibility
{
    private static final int field_178623_a;
    private final BitSet field_178622_b;
    private static final String __OBFID = "CL_00002448";
    
    public SetVisibility() {
        this.field_178622_b = new BitSet(SetVisibility.field_178623_a * SetVisibility.field_178623_a);
    }
    
    public void func_178620_a(final Set p_178620_1_) {
        for (final EnumFacing var3 : p_178620_1_) {
            for (final EnumFacing var5 : p_178620_1_) {
                this.func_178619_a(var3, var5, true);
            }
        }
    }
    
    public void func_178619_a(final EnumFacing p_178619_1_, final EnumFacing p_178619_2_, final boolean p_178619_3_) {
        this.field_178622_b.set(p_178619_1_.ordinal() + p_178619_2_.ordinal() * SetVisibility.field_178623_a, p_178619_3_);
        this.field_178622_b.set(p_178619_2_.ordinal() + p_178619_1_.ordinal() * SetVisibility.field_178623_a, p_178619_3_);
    }
    
    public void func_178618_a(final boolean p_178618_1_) {
        this.field_178622_b.set(0, this.field_178622_b.size(), p_178618_1_);
    }
    
    public boolean func_178621_a(final EnumFacing p_178621_1_, final EnumFacing p_178621_2_) {
        return this.field_178622_b.get(p_178621_1_.ordinal() + p_178621_2_.ordinal() * SetVisibility.field_178623_a);
    }
    
    @Override
    public String toString() {
        final StringBuilder var1 = new StringBuilder();
        var1.append(' ');
        for (final EnumFacing var5 : EnumFacing.values()) {
            var1.append(' ').append(var5.toString().toUpperCase().charAt(0));
        }
        var1.append('\n');
        for (final EnumFacing var5 : EnumFacing.values()) {
            var1.append(var5.toString().toUpperCase().charAt(0));
            for (final EnumFacing var9 : EnumFacing.values()) {
                if (var5 == var9) {
                    var1.append("  ");
                }
                else {
                    final boolean var10 = this.func_178621_a(var5, var9);
                    var1.append(' ').append(var10 ? 'Y' : 'n');
                }
            }
            var1.append('\n');
        }
        return var1.toString();
    }
    
    static {
        field_178623_a = EnumFacing.values().length;
    }
}
