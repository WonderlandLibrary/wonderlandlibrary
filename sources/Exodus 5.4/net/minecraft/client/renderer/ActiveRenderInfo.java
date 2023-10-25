/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ActiveRenderInfo {
    private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
    private static float rotationYZ;
    private static final FloatBuffer OBJECTCOORDS;
    private static float rotationXY;
    private static float rotationX;
    private static final FloatBuffer PROJECTION;
    private static Vec3 position;
    private static float rotationZ;
    private static float rotationXZ;
    private static final FloatBuffer MODELVIEW;

    public static float getRotationX() {
        return rotationX;
    }

    public static void updateRenderInfo(EntityPlayer entityPlayer, boolean bl) {
        GlStateManager.getFloat(2982, MODELVIEW);
        GlStateManager.getFloat(2983, PROJECTION);
        GL11.glGetInteger((int)2978, (IntBuffer)VIEWPORT);
        float f = (VIEWPORT.get(0) + VIEWPORT.get(2)) / 2;
        float f2 = (VIEWPORT.get(1) + VIEWPORT.get(3)) / 2;
        GLU.gluUnProject((float)f, (float)f2, (float)0.0f, (FloatBuffer)MODELVIEW, (FloatBuffer)PROJECTION, (IntBuffer)VIEWPORT, (FloatBuffer)OBJECTCOORDS);
        position = new Vec3(OBJECTCOORDS.get(0), OBJECTCOORDS.get(1), OBJECTCOORDS.get(2));
        int n = bl ? 1 : 0;
        float f3 = entityPlayer.rotationPitch;
        float f4 = entityPlayer.rotationYaw;
        rotationX = MathHelper.cos(f4 * (float)Math.PI / 180.0f) * (float)(1 - n * 2);
        rotationZ = MathHelper.sin(f4 * (float)Math.PI / 180.0f) * (float)(1 - n * 2);
        rotationYZ = -rotationZ * MathHelper.sin(f3 * (float)Math.PI / 180.0f) * (float)(1 - n * 2);
        rotationXY = rotationX * MathHelper.sin(f3 * (float)Math.PI / 180.0f) * (float)(1 - n * 2);
        rotationXZ = MathHelper.cos(f3 * (float)Math.PI / 180.0f);
    }

    public static float getRotationXZ() {
        return rotationXZ;
    }

    public static float getRotationXY() {
        return rotationXY;
    }

    public static Vec3 getPosition() {
        return position;
    }

    public static float getRotationYZ() {
        return rotationYZ;
    }

    public static Vec3 projectViewFromEntity(Entity entity, double d) {
        double d2 = entity.prevPosX + (entity.posX - entity.prevPosX) * d;
        double d3 = entity.prevPosY + (entity.posY - entity.prevPosY) * d;
        double d4 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * d;
        double d5 = d2 + ActiveRenderInfo.position.xCoord;
        double d6 = d3 + ActiveRenderInfo.position.yCoord;
        double d7 = d4 + ActiveRenderInfo.position.zCoord;
        return new Vec3(d5, d6, d7);
    }

    static {
        MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
        PROJECTION = GLAllocation.createDirectFloatBuffer(16);
        OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
        position = new Vec3(0.0, 0.0, 0.0);
    }

    public static float getRotationZ() {
        return rotationZ;
    }

    public static Block getBlockAtEntityViewpoint(World world, Entity entity, float f) {
        Vec3 vec3 = ActiveRenderInfo.projectViewFromEntity(entity, f);
        BlockPos blockPos = new BlockPos(vec3);
        IBlockState iBlockState = world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (block.getMaterial().isLiquid()) {
            float f2;
            float f3 = 0.0f;
            if (iBlockState.getBlock() instanceof BlockLiquid) {
                f3 = BlockLiquid.getLiquidHeightPercent(iBlockState.getValue(BlockLiquid.LEVEL)) - 0.11111111f;
            }
            if (vec3.yCoord >= (double)(f2 = (float)(blockPos.getY() + 1) - f3)) {
                block = world.getBlockState(blockPos.up()).getBlock();
            }
        }
        return block;
    }
}

