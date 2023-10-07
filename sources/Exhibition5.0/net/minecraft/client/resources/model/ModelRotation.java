// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources.model;

import com.google.common.collect.Maps;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import java.util.Map;

public enum ModelRotation
{
    X0_Y0("X0_Y0", 0, 0, 0), 
    X0_Y90("X0_Y90", 1, 0, 90), 
    X0_Y180("X0_Y180", 2, 0, 180), 
    X0_Y270("X0_Y270", 3, 0, 270), 
    X90_Y0("X90_Y0", 4, 90, 0), 
    X90_Y90("X90_Y90", 5, 90, 90), 
    X90_Y180("X90_Y180", 6, 90, 180), 
    X90_Y270("X90_Y270", 7, 90, 270), 
    X180_Y0("X180_Y0", 8, 180, 0), 
    X180_Y90("X180_Y90", 9, 180, 90), 
    X180_Y180("X180_Y180", 10, 180, 180), 
    X180_Y270("X180_Y270", 11, 180, 270), 
    X270_Y0("X270_Y0", 12, 270, 0), 
    X270_Y90("X270_Y90", 13, 270, 90), 
    X270_Y180("X270_Y180", 14, 270, 180), 
    X270_Y270("X270_Y270", 15, 270, 270);
    
    private static final Map field_177546_q;
    private final int field_177545_r;
    private final Matrix4d field_177544_s;
    private final int field_177543_t;
    private final int field_177542_u;
    private static final ModelRotation[] $VALUES;
    private static final String __OBFID = "CL_00002393";
    
    private static int func_177521_b(final int p_177521_0_, final int p_177521_1_) {
        return p_177521_0_ * 360 + p_177521_1_;
    }
    
    private ModelRotation(final String p_i46087_1_, final int p_i46087_2_, final int p_i46087_3_, final int p_i46087_4_) {
        this.field_177545_r = func_177521_b(p_i46087_3_, p_i46087_4_);
        this.field_177544_s = new Matrix4d();
        final Matrix4d var5 = new Matrix4d();
        var5.setIdentity();
        var5.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, (double)(-p_i46087_3_ * 0.017453292f)));
        this.field_177543_t = MathHelper.abs_int(p_i46087_3_ / 90);
        final Matrix4d var6 = new Matrix4d();
        var6.setIdentity();
        var6.setRotation(new AxisAngle4d(0.0, 1.0, 0.0, (double)(-p_i46087_4_ * 0.017453292f)));
        this.field_177542_u = MathHelper.abs_int(p_i46087_4_ / 90);
        this.field_177544_s.mul(var6, var5);
    }
    
    public Matrix4d func_177525_a() {
        return this.field_177544_s;
    }
    
    public EnumFacing func_177523_a(final EnumFacing p_177523_1_) {
        EnumFacing var2 = p_177523_1_;
        for (int var3 = 0; var3 < this.field_177543_t; ++var3) {
            var2 = var2.rotateAround(EnumFacing.Axis.X);
        }
        if (var2.getAxis() != EnumFacing.Axis.Y) {
            for (int var3 = 0; var3 < this.field_177542_u; ++var3) {
                var2 = var2.rotateAround(EnumFacing.Axis.Y);
            }
        }
        return var2;
    }
    
    public int func_177520_a(final EnumFacing p_177520_1_, final int p_177520_2_) {
        int var3 = p_177520_2_;
        if (p_177520_1_.getAxis() == EnumFacing.Axis.X) {
            var3 = (p_177520_2_ + this.field_177543_t) % 4;
        }
        EnumFacing var4 = p_177520_1_;
        for (int var5 = 0; var5 < this.field_177543_t; ++var5) {
            var4 = var4.rotateAround(EnumFacing.Axis.X);
        }
        if (var4.getAxis() == EnumFacing.Axis.Y) {
            var3 = (var3 + this.field_177542_u) % 4;
        }
        return var3;
    }
    
    public static ModelRotation func_177524_a(final int p_177524_0_, final int p_177524_1_) {
        return ModelRotation.field_177546_q.get(func_177521_b(MathHelper.func_180184_b(p_177524_0_, 360), MathHelper.func_180184_b(p_177524_1_, 360)));
    }
    
    static {
        field_177546_q = Maps.newHashMap();
        $VALUES = new ModelRotation[] { ModelRotation.X0_Y0, ModelRotation.X0_Y90, ModelRotation.X0_Y180, ModelRotation.X0_Y270, ModelRotation.X90_Y0, ModelRotation.X90_Y90, ModelRotation.X90_Y180, ModelRotation.X90_Y270, ModelRotation.X180_Y0, ModelRotation.X180_Y90, ModelRotation.X180_Y180, ModelRotation.X180_Y270, ModelRotation.X270_Y0, ModelRotation.X270_Y90, ModelRotation.X270_Y180, ModelRotation.X270_Y270 };
        for (final ModelRotation var4 : values()) {
            ModelRotation.field_177546_q.put(var4.field_177545_r, var4);
        }
    }
}
