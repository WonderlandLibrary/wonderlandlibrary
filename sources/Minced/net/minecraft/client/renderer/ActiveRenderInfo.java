// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.glu.GLU;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ActiveRenderInfo
{
    private static final IntBuffer VIEWPORT;
    private static final FloatBuffer MODELVIEW;
    private static final FloatBuffer PROJECTION;
    private static final FloatBuffer OBJECTCOORDS;
    private static Vec3d position;
    private static float rotationX;
    private static float rotationXZ;
    private static float rotationZ;
    private static float rotationYZ;
    private static float rotationXY;
    
    public static void updateRenderInfo(final EntityPlayer entityplayerIn, final boolean p_74583_1_) {
        GlStateManager.getFloat(2982, ActiveRenderInfo.MODELVIEW);
        GlStateManager.getFloat(2983, ActiveRenderInfo.PROJECTION);
        GlStateManager.glGetInteger(2978, ActiveRenderInfo.VIEWPORT);
        final float f = (float)((ActiveRenderInfo.VIEWPORT.get(0) + ActiveRenderInfo.VIEWPORT.get(2)) / 2);
        final float f2 = (float)((ActiveRenderInfo.VIEWPORT.get(1) + ActiveRenderInfo.VIEWPORT.get(3)) / 2);
        GLU.gluUnProject(f, f2, 0.0f, ActiveRenderInfo.MODELVIEW, ActiveRenderInfo.PROJECTION, ActiveRenderInfo.VIEWPORT, ActiveRenderInfo.OBJECTCOORDS);
        ActiveRenderInfo.position = new Vec3d(ActiveRenderInfo.OBJECTCOORDS.get(0), ActiveRenderInfo.OBJECTCOORDS.get(1), ActiveRenderInfo.OBJECTCOORDS.get(2));
        final int i = p_74583_1_ ? 1 : 0;
        final float f3 = entityplayerIn.rotationPitch;
        final float f4 = entityplayerIn.rotationYaw;
        ActiveRenderInfo.rotationX = MathHelper.cos(f4 * 0.017453292f) * (1 - i * 2);
        ActiveRenderInfo.rotationZ = MathHelper.sin(f4 * 0.017453292f) * (1 - i * 2);
        ActiveRenderInfo.rotationYZ = -ActiveRenderInfo.rotationZ * MathHelper.sin(f3 * 0.017453292f) * (1 - i * 2);
        ActiveRenderInfo.rotationXY = ActiveRenderInfo.rotationX * MathHelper.sin(f3 * 0.017453292f) * (1 - i * 2);
        ActiveRenderInfo.rotationXZ = MathHelper.cos(f3 * 0.017453292f);
    }
    
    public static Vec3d projectViewFromEntity(final Entity entityIn, final double p_178806_1_) {
        final double d0 = entityIn.prevPosX + (entityIn.posX - entityIn.prevPosX) * p_178806_1_;
        final double d2 = entityIn.prevPosY + (entityIn.posY - entityIn.prevPosY) * p_178806_1_;
        final double d3 = entityIn.prevPosZ + (entityIn.posZ - entityIn.prevPosZ) * p_178806_1_;
        final double d4 = d0 + ActiveRenderInfo.position.x;
        final double d5 = d2 + ActiveRenderInfo.position.y;
        final double d6 = d3 + ActiveRenderInfo.position.z;
        return new Vec3d(d4, d5, d6);
    }
    
    public static IBlockState getBlockStateAtEntityViewpoint(final World worldIn, final Entity entityIn, final float p_186703_2_) {
        final Vec3d vec3d = projectViewFromEntity(entityIn, p_186703_2_);
        final BlockPos blockpos = new BlockPos(vec3d);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getMaterial().isLiquid()) {
            float f = 0.0f;
            if (iblockstate.getBlock() instanceof BlockLiquid) {
                f = BlockLiquid.getLiquidHeightPercent(iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL)) - 0.11111111f;
            }
            final float f2 = blockpos.getY() + 1 - f;
            if (vec3d.y >= f2) {
                iblockstate = worldIn.getBlockState(blockpos.up());
            }
        }
        return iblockstate;
    }
    
    public static float getRotationX() {
        return ActiveRenderInfo.rotationX;
    }
    
    public static float getRotationXZ() {
        return ActiveRenderInfo.rotationXZ;
    }
    
    public static float getRotationZ() {
        return ActiveRenderInfo.rotationZ;
    }
    
    public static float getRotationYZ() {
        return ActiveRenderInfo.rotationYZ;
    }
    
    public static float getRotationXY() {
        return ActiveRenderInfo.rotationXY;
    }
    
    static {
        VIEWPORT = GLAllocation.createDirectIntBuffer(16);
        MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
        PROJECTION = GLAllocation.createDirectFloatBuffer(16);
        OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
        ActiveRenderInfo.position = new Vec3d(0.0, 0.0, 0.0);
    }
}
