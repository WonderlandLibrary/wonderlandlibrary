// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ActiveRenderInfo
{
    private static final IntBuffer field_178814_a;
    private static final FloatBuffer field_178812_b;
    private static final FloatBuffer field_178813_c;
    private static final FloatBuffer field_178810_d;
    private static Vec3 field_178811_e;
    private static float rotationX;
    private static float rotationXZ;
    private static float rotationZ;
    private static float rotationYZ;
    private static float rotationXY;
    private static final String __OBFID = "CL_00000626";
    
    public static void updateRenderInfo(final EntityPlayer p_74583_0_, final boolean p_74583_1_) {
        GlStateManager.getFloat(2982, ActiveRenderInfo.field_178812_b);
        GlStateManager.getFloat(2983, ActiveRenderInfo.field_178813_c);
        GL11.glGetInteger(2978, ActiveRenderInfo.field_178814_a);
        final float var2 = (ActiveRenderInfo.field_178814_a.get(0) + ActiveRenderInfo.field_178814_a.get(2)) / 2;
        final float var3 = (ActiveRenderInfo.field_178814_a.get(1) + ActiveRenderInfo.field_178814_a.get(3)) / 2;
        GLU.gluUnProject(var2, var3, 0.0f, ActiveRenderInfo.field_178812_b, ActiveRenderInfo.field_178813_c, ActiveRenderInfo.field_178814_a, ActiveRenderInfo.field_178810_d);
        ActiveRenderInfo.field_178811_e = new Vec3(ActiveRenderInfo.field_178810_d.get(0), ActiveRenderInfo.field_178810_d.get(1), ActiveRenderInfo.field_178810_d.get(2));
        final int var4 = p_74583_1_ ? 1 : 0;
        final float var5 = p_74583_0_.rotationPitch;
        final float var6 = p_74583_0_.rotationYaw;
        ActiveRenderInfo.rotationX = MathHelper.cos(var6 * 3.1415927f / 180.0f) * (1 - var4 * 2);
        ActiveRenderInfo.rotationZ = MathHelper.sin(var6 * 3.1415927f / 180.0f) * (1 - var4 * 2);
        ActiveRenderInfo.rotationYZ = -ActiveRenderInfo.rotationZ * MathHelper.sin(var5 * 3.1415927f / 180.0f) * (1 - var4 * 2);
        ActiveRenderInfo.rotationXY = ActiveRenderInfo.rotationX * MathHelper.sin(var5 * 3.1415927f / 180.0f) * (1 - var4 * 2);
        ActiveRenderInfo.rotationXZ = MathHelper.cos(var5 * 3.1415927f / 180.0f);
    }
    
    public static Vec3 func_178806_a(final Entity p_178806_0_, final double p_178806_1_) {
        final double var3 = p_178806_0_.prevPosX + (p_178806_0_.posX - p_178806_0_.prevPosX) * p_178806_1_;
        final double var4 = p_178806_0_.prevPosY + (p_178806_0_.posY - p_178806_0_.prevPosY) * p_178806_1_;
        final double var5 = p_178806_0_.prevPosZ + (p_178806_0_.posZ - p_178806_0_.prevPosZ) * p_178806_1_;
        final double var6 = var3 + ActiveRenderInfo.field_178811_e.xCoord;
        final double var7 = var4 + ActiveRenderInfo.field_178811_e.yCoord;
        final double var8 = var5 + ActiveRenderInfo.field_178811_e.zCoord;
        return new Vec3(var6, var7, var8);
    }
    
    public static Block func_180786_a(final World worldIn, final Entity p_180786_1_, final float p_180786_2_) {
        final Vec3 var3 = func_178806_a(p_180786_1_, p_180786_2_);
        final BlockPos var4 = new BlockPos(var3);
        final IBlockState var5 = worldIn.getBlockState(var4);
        Block var6 = var5.getBlock();
        if (var6.getMaterial().isLiquid()) {
            float var7 = 0.0f;
            if (var5.getBlock() instanceof BlockLiquid) {
                var7 = BlockLiquid.getLiquidHeightPercent((int)var5.getValue(BlockLiquid.LEVEL)) - 0.11111111f;
            }
            final float var8 = var4.getY() + 1 - var7;
            if (var3.yCoord >= var8) {
                var6 = worldIn.getBlockState(var4.offsetUp()).getBlock();
            }
        }
        return var6;
    }
    
    public static Vec3 func_178804_a() {
        return ActiveRenderInfo.field_178811_e;
    }
    
    public static float func_178808_b() {
        return ActiveRenderInfo.rotationX;
    }
    
    public static float func_178809_c() {
        return ActiveRenderInfo.rotationXZ;
    }
    
    public static float func_178803_d() {
        return ActiveRenderInfo.rotationZ;
    }
    
    public static float func_178805_e() {
        return ActiveRenderInfo.rotationYZ;
    }
    
    public static float func_178807_f() {
        return ActiveRenderInfo.rotationXY;
    }
    
    static {
        field_178814_a = GLAllocation.createDirectIntBuffer(16);
        field_178812_b = GLAllocation.createDirectFloatBuffer(16);
        field_178813_c = GLAllocation.createDirectFloatBuffer(16);
        field_178810_d = GLAllocation.createDirectFloatBuffer(3);
        ActiveRenderInfo.field_178811_e = new Vec3(0.0, 0.0, 0.0);
    }
}
