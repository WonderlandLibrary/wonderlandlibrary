/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.util.glu.GLU;

public class ActiveRenderInfo {
    private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
    private static Vec3d position = new Vec3d(0.0, 0.0, 0.0);
    private static float rotationX;
    private static float rotationXZ;
    private static float rotationZ;
    private static float rotationYZ;
    private static float rotationXY;

    public static void updateRenderInfo(EntityPlayer entityplayerIn, boolean p_74583_1_) {
        GlStateManager.getFloat(2982, MODELVIEW);
        GlStateManager.getFloat(2983, PROJECTION);
        GlStateManager.glGetInteger(2978, VIEWPORT);
        float f = (VIEWPORT.get(0) + VIEWPORT.get(2)) / 2;
        float f1 = (VIEWPORT.get(1) + VIEWPORT.get(3)) / 2;
        GLU.gluUnProject(f, f1, 0.0f, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
        position = new Vec3d(OBJECTCOORDS.get(0), OBJECTCOORDS.get(1), OBJECTCOORDS.get(2));
        int i = p_74583_1_ ? 1 : 0;
        float f2 = entityplayerIn.rotationPitch;
        float f3 = entityplayerIn.rotationYaw;
        rotationX = MathHelper.cos(f3 * ((float)Math.PI / 180)) * (float)(1 - i * 2);
        rotationZ = MathHelper.sin(f3 * ((float)Math.PI / 180)) * (float)(1 - i * 2);
        rotationYZ = -rotationZ * MathHelper.sin(f2 * ((float)Math.PI / 180)) * (float)(1 - i * 2);
        rotationXY = rotationX * MathHelper.sin(f2 * ((float)Math.PI / 180)) * (float)(1 - i * 2);
        rotationXZ = MathHelper.cos(f2 * ((float)Math.PI / 180));
    }

    public static Vec3d projectViewFromEntity(Entity entityIn, double p_178806_1_) {
        double d0 = entityIn.prevPosX + (entityIn.posX - entityIn.prevPosX) * p_178806_1_;
        double d1 = entityIn.prevPosY + (entityIn.posY - entityIn.prevPosY) * p_178806_1_;
        double d2 = entityIn.prevPosZ + (entityIn.posZ - entityIn.prevPosZ) * p_178806_1_;
        double d3 = d0 + ActiveRenderInfo.position.x;
        double d4 = d1 + ActiveRenderInfo.position.y;
        double d5 = d2 + ActiveRenderInfo.position.z;
        return new Vec3d(d3, d4, d5);
    }

    public static IBlockState getBlockStateAtEntityViewpoint(World worldIn, Entity entityIn, float p_186703_2_) {
        Vec3d vec3d = ActiveRenderInfo.projectViewFromEntity(entityIn, p_186703_2_);
        BlockPos blockpos = new BlockPos(vec3d);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getMaterial().isLiquid()) {
            float f1;
            float f = 0.0f;
            if (iblockstate.getBlock() instanceof BlockLiquid) {
                f = BlockLiquid.getLiquidHeightPercent(iblockstate.getValue(BlockLiquid.LEVEL)) - 0.11111111f;
            }
            if (vec3d.y >= (double)(f1 = (float)(blockpos.getY() + 1) - f)) {
                iblockstate = worldIn.getBlockState(blockpos.up());
            }
        }
        return iblockstate;
    }

    public static float getRotationX() {
        return rotationX;
    }

    public static float getRotationXZ() {
        return rotationXZ;
    }

    public static float getRotationZ() {
        return rotationZ;
    }

    public static float getRotationYZ() {
        return rotationYZ;
    }

    public static float getRotationXY() {
        return rotationXY;
    }
}

