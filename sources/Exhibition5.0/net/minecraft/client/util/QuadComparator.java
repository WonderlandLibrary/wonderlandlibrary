// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.util;

import java.nio.FloatBuffer;
import java.util.Comparator;

public class QuadComparator implements Comparator
{
    private float field_147630_a;
    private float field_147628_b;
    private float field_147629_c;
    private FloatBuffer field_147627_d;
    private int field_178079_e;
    private static final String __OBFID = "CL_00000958";
    
    public QuadComparator(final FloatBuffer p_i46247_1_, final float p_i46247_2_, final float p_i46247_3_, final float p_i46247_4_, final int p_i46247_5_) {
        this.field_147627_d = p_i46247_1_;
        this.field_147630_a = p_i46247_2_;
        this.field_147628_b = p_i46247_3_;
        this.field_147629_c = p_i46247_4_;
        this.field_178079_e = p_i46247_5_;
    }
    
    public int compare(final Integer p_compare_1_, final Integer p_compare_2_) {
        final float var3 = this.field_147627_d.get(p_compare_1_) - this.field_147630_a;
        final float var4 = this.field_147627_d.get(p_compare_1_ + 1) - this.field_147628_b;
        final float var5 = this.field_147627_d.get(p_compare_1_ + 2) - this.field_147629_c;
        final float var6 = this.field_147627_d.get(p_compare_1_ + this.field_178079_e + 0) - this.field_147630_a;
        final float var7 = this.field_147627_d.get(p_compare_1_ + this.field_178079_e + 1) - this.field_147628_b;
        final float var8 = this.field_147627_d.get(p_compare_1_ + this.field_178079_e + 2) - this.field_147629_c;
        final float var9 = this.field_147627_d.get(p_compare_1_ + this.field_178079_e * 2 + 0) - this.field_147630_a;
        final float var10 = this.field_147627_d.get(p_compare_1_ + this.field_178079_e * 2 + 1) - this.field_147628_b;
        final float var11 = this.field_147627_d.get(p_compare_1_ + this.field_178079_e * 2 + 2) - this.field_147629_c;
        final float var12 = this.field_147627_d.get(p_compare_1_ + this.field_178079_e * 3 + 0) - this.field_147630_a;
        final float var13 = this.field_147627_d.get(p_compare_1_ + this.field_178079_e * 3 + 1) - this.field_147628_b;
        final float var14 = this.field_147627_d.get(p_compare_1_ + this.field_178079_e * 3 + 2) - this.field_147629_c;
        final float var15 = this.field_147627_d.get(p_compare_2_) - this.field_147630_a;
        final float var16 = this.field_147627_d.get(p_compare_2_ + 1) - this.field_147628_b;
        final float var17 = this.field_147627_d.get(p_compare_2_ + 2) - this.field_147629_c;
        final float var18 = this.field_147627_d.get(p_compare_2_ + this.field_178079_e + 0) - this.field_147630_a;
        final float var19 = this.field_147627_d.get(p_compare_2_ + this.field_178079_e + 1) - this.field_147628_b;
        final float var20 = this.field_147627_d.get(p_compare_2_ + this.field_178079_e + 2) - this.field_147629_c;
        final float var21 = this.field_147627_d.get(p_compare_2_ + this.field_178079_e * 2 + 0) - this.field_147630_a;
        final float var22 = this.field_147627_d.get(p_compare_2_ + this.field_178079_e * 2 + 1) - this.field_147628_b;
        final float var23 = this.field_147627_d.get(p_compare_2_ + this.field_178079_e * 2 + 2) - this.field_147629_c;
        final float var24 = this.field_147627_d.get(p_compare_2_ + this.field_178079_e * 3 + 0) - this.field_147630_a;
        final float var25 = this.field_147627_d.get(p_compare_2_ + this.field_178079_e * 3 + 1) - this.field_147628_b;
        final float var26 = this.field_147627_d.get(p_compare_2_ + this.field_178079_e * 3 + 2) - this.field_147629_c;
        final float var27 = (var3 + var6 + var9 + var12) * 0.25f;
        final float var28 = (var4 + var7 + var10 + var13) * 0.25f;
        final float var29 = (var5 + var8 + var11 + var14) * 0.25f;
        final float var30 = (var15 + var18 + var21 + var24) * 0.25f;
        final float var31 = (var16 + var19 + var22 + var25) * 0.25f;
        final float var32 = (var17 + var20 + var23 + var26) * 0.25f;
        final float var33 = var27 * var27 + var28 * var28 + var29 * var29;
        final float var34 = var30 * var30 + var31 * var31 + var32 * var32;
        return Float.compare(var34, var33);
    }
    
    @Override
    public int compare(final Object p_compare_1_, final Object p_compare_2_) {
        return this.compare((Integer)p_compare_1_, (Integer)p_compare_2_);
    }
}
