// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.model;

import optifine.Reflector;
import javax.vecmath.Matrix4f;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.IModelPart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import javax.vecmath.AxisAngle4d;
import com.google.common.collect.Maps;
import javax.vecmath.Matrix4d;
import java.util.Map;
import net.minecraftforge.client.model.ITransformation;
import net.minecraftforge.client.model.IModelState;

public enum ModelRotation implements IModelState, ITransformation
{
    X0_Y0("X0_Y0", 0, "X0_Y0", 0, 0, 0), 
    X0_Y90("X0_Y90", 1, "X0_Y90", 1, 0, 90), 
    X0_Y180("X0_Y180", 2, "X0_Y180", 2, 0, 180), 
    X0_Y270("X0_Y270", 3, "X0_Y270", 3, 0, 270), 
    X90_Y0("X90_Y0", 4, "X90_Y0", 4, 90, 0), 
    X90_Y90("X90_Y90", 5, "X90_Y90", 5, 90, 90), 
    X90_Y180("X90_Y180", 6, "X90_Y180", 6, 90, 180), 
    X90_Y270("X90_Y270", 7, "X90_Y270", 7, 90, 270), 
    X180_Y0("X180_Y0", 8, "X180_Y0", 8, 180, 0), 
    X180_Y90("X180_Y90", 9, "X180_Y90", 9, 180, 90), 
    X180_Y180("X180_Y180", 10, "X180_Y180", 10, 180, 180), 
    X180_Y270("X180_Y270", 11, "X180_Y270", 11, 180, 270), 
    X270_Y0("X270_Y0", 12, "X270_Y0", 12, 270, 0), 
    X270_Y90("X270_Y90", 13, "X270_Y90", 13, 270, 90), 
    X270_Y180("X270_Y180", 14, "X270_Y180", 14, 270, 180), 
    X270_Y270("X270_Y270", 15, "X270_Y270", 15, 270, 270);
    
    private static final Map field_177546_q;
    private final int field_177545_r;
    private final Matrix4d field_177544_s;
    private final int field_177543_t;
    private final int field_177542_u;
    private static final String __OBFID = "CL_00002393";
    private static final ModelRotation[] $VALUES;
    
    static {
        field_177546_q = Maps.newHashMap();
        $VALUES = new ModelRotation[] { ModelRotation.X0_Y0, ModelRotation.X0_Y90, ModelRotation.X0_Y180, ModelRotation.X0_Y270, ModelRotation.X90_Y0, ModelRotation.X90_Y90, ModelRotation.X90_Y180, ModelRotation.X90_Y270, ModelRotation.X180_Y0, ModelRotation.X180_Y90, ModelRotation.X180_Y180, ModelRotation.X180_Y270, ModelRotation.X270_Y0, ModelRotation.X270_Y90, ModelRotation.X270_Y180, ModelRotation.X270_Y270 };
        for (final ModelRotation var4 : values()) {
            ModelRotation.field_177546_q.put(var4.field_177545_r, var4);
        }
    }
    
    private static int func_177521_b(final int p_177521_0_, final int p_177521_1_) {
        return p_177521_0_ * 360 + p_177521_1_;
    }
    
    private ModelRotation(final String name, final int ordinal, final String p_i46087_1_, final int p_i46087_2_, final int p_i46087_3_, final int p_i46087_4_) {
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
    
    public int func_177520_a(final EnumFacing facing, final int vertexIndex) {
        int var3 = vertexIndex;
        if (facing.getAxis() == EnumFacing.Axis.X) {
            var3 = (vertexIndex + this.field_177543_t) % 4;
        }
        EnumFacing var4 = facing;
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
    
    @Override
    public TRSRTransformation apply(final IModelPart part) {
        return new TRSRTransformation(this.getMatrix());
    }
    
    @Override
    public Matrix4f getMatrix() {
        return (Matrix4f)(Reflector.ForgeHooksClient_getMatrix.exists() ? Reflector.call(Reflector.ForgeHooksClient_getMatrix, this) : new Matrix4f(this.func_177525_a()));
    }
    
    @Override
    public EnumFacing rotate(final EnumFacing facing) {
        return this.func_177523_a(facing);
    }
    
    @Override
    public int rotate(final EnumFacing facing, final int vertexIndex) {
        return this.func_177520_a(facing, vertexIndex);
    }
}
