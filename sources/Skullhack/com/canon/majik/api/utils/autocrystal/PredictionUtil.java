package com.canon.majik.api.utils.autocrystal;

import com.canon.majik.api.utils.Globals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class PredictionUtil implements Globals {

    public static AxisAlignedBB predictedTarget(EntityLivingBase entity, int predict) {
        AxisAlignedBB[] bb = predictedTargetBoxes(entity, predict);
        return bb[bb.length-1];
    }

    static double motionX;
    static double motionY;
    static double motionZ;
    static boolean onGround;
    static boolean isCollidedHorizontally;
    static boolean isCollidedVertically;
    static boolean isCollided;

    public static AxisAlignedBB[] predictedTargetBoxes(EntityLivingBase entity, int predict) {
        return Arrays.stream(predictions(entity, predict)).map(Prediction::getBoundingBox).toArray(AxisAlignedBB[]::new);
    }

    public static Vec3d getCenter(AxisAlignedBB alignedBB) {
        double centerX = alignedBB.minX + (alignedBB.maxX - alignedBB.minX)/2;
        double centerZ = alignedBB.minZ + (alignedBB.maxZ - alignedBB.minZ)/2;
        double centerY = alignedBB.minY + (alignedBB.maxY - alignedBB.minY)/2;
        return new Vec3d(centerX, centerY, centerZ);
    }

    public static Prediction[] predictions(EntityLivingBase entity, int predict) {

        Prediction[] predictions = new Prediction[predict+1];
        AxisAlignedBB box = entity.getEntityBoundingBox();
        motionX = entity.posX - entity.lastTickPosX;
        motionY = ((entity.posY - entity.lastTickPosY)-0.08)*0.9800000190734863D;
        motionZ = entity.posZ - entity.lastTickPosZ;
        onGround = entity.onGround;
        isCollidedHorizontally = entity.collidedHorizontally;
        isCollidedVertically = entity.collidedVertically;
        isCollided = entity.collided;

        if(predict <= 0) {
            Arrays.fill(predictions, getPrediction(box));
            return predictions;
        }

        float[] input;
        if(entity != mc.player) {
            input = predictInput(motionX, motionZ, entity.rotationYaw, entity.isSneaking());
        } else {
            input = new float[]{mc.player.moveForward*0.5f, mc.player.moveStrafing*0.5f};
        }
        predictions[0] = getPrediction(box);

        for (int i = 0; i < predict; i++) {

            if(entity instanceof EntityPlayer) {
                box = movePlayerWithHeadingSimulation(input[1], input[0], (EntityPlayer) entity, box);
            } else {
                box = moveEntityWithHeadingSimulation(entity.moveStrafing, entity.moveForward, entity, box);
            }
            predictions[i+1] = getPrediction(box);
        }
        return predictions;
    }

    private static Prediction getPrediction(AxisAlignedBB box) {
        return new Prediction(motionX, motionY, motionZ, onGround, isCollidedHorizontally, isCollidedVertically, isCollided, box);
    }

    private static AxisAlignedBB movePlayerWithHeadingSimulation(float strafe, float forward, EntityPlayer entity , AxisAlignedBB bb) {

        if (entity.capabilities.isFlying && entity.getRidingEntity() == null)
        {
            double d3 = motionY;
            bb = moveEntityWithHeadingSimulation(strafe, forward, entity, bb);
            motionY = d3 * 0.6D;
        }
        else
        {
            bb = moveEntityWithHeadingSimulation(strafe, forward, entity, bb);
        }
        return bb;
    }

    private static AxisAlignedBB moveEntityWithHeadingSimulation(float strafe, float forward,EntityLivingBase entity ,AxisAlignedBB bb) {
        Vec3d center = getCenter(bb);
        if (!mc.world.isMaterialInBB(bb.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.WATER) || entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isFlying)
        {
            if (!mc.world.isMaterialInBB(bb.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA) || entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isFlying)
            {

                float f4 = 0.91F;

                if (onGround)
                {
                    f4 = mc.world.getBlockState(new BlockPos(MathHelper.floor(center.x), MathHelper.floor(bb.minY) - 1, MathHelper.floor(center.z))).getBlock().slipperiness * 0.91F;
                }

                float f = 0.16277136F / (f4 * f4 * f4);
                float f5;

                if (onGround)
                {
                    f5 = entity.getAIMoveSpeed() * f;
                }
                else
                {
                    f5 = entity.jumpMovementFactor;
                }

                moveFlyingSimulation(strafe, forward, f5, entity);
                f4 = 0.91F;

                if (onGround)
                {
                    f4 = mc.world.getBlockState(new BlockPos(MathHelper.floor(center.x), MathHelper.floor(bb.minY) - 1, MathHelper.floor(center.z))).getBlock().slipperiness * 0.91F;
                }

                if (entity.isOnLadder())
                {
                    float f6 = 0.15F;
                    motionX = MathHelper.clamp(motionX, -f6, f6);
                    motionZ = MathHelper.clamp(motionZ, -f6, f6);

                    if (motionY < -0.15D)
                    {
                        motionY = -0.15D;
                    }

                    boolean flag = entity.isSneaking() && entity instanceof EntityPlayer;

                    if (flag && entity.motionY < 0.0D)
                    {
                        motionY = 0.0D;
                    }
                }

                bb = moveEntitySimulation(motionX, motionY, motionZ, entity, bb);

                if (isCollidedHorizontally && entity.isOnLadder())
                {
                    motionY = 0.2D;
                }
                motionY -= 0.08D;

                motionY *= 0.9800000190734863D;
                motionX *= f4;
                motionZ *= f4;
            }
            else
            {
                double d1 = bb.minY;
                moveFlyingSimulation(strafe, forward, 0.02F, entity);
                bb = moveEntitySimulation(motionX, motionY, motionZ, entity, bb);
                motionX *= 0.5D;
                motionY *= 0.5D;
                motionZ *= 0.5D;
                motionY -= 0.02D;

                if (isCollidedHorizontally && mc.world.getCollisionBoxes(entity, bb.offset(motionX, motionY + 0.6000000238418579D - bb.minY + d1, motionZ)).isEmpty() && !mc.world.containsAnyLiquid(bb.offset(motionX, motionY + 0.6000000238418579D - bb.minY + d1, motionZ)))
                {
                    motionY = 0.30000001192092896D;
                }
            }
        }
        else
        {
            double d0 = bb.minY;
            float f1 = 0.8F;
            float f2 = 0.02F;
            float f3 = (float) EnchantmentHelper.getDepthStriderModifier(entity);

            if (f3 > 3.0F)
            {
                f3 = 3.0F;
            }

            if (!onGround)
            {
                f3 *= 0.5F;
            }

            if (f3 > 0.0F)
            {
                f1 += (0.54600006F - f1) * f3 / 3.0F;
                f2 += (entity.getAIMoveSpeed() - f2) * f3 / 3.0F;
            }

            moveFlyingSimulation(strafe, forward, f2, entity);
            bb = moveEntitySimulation(motionX, motionY, motionZ, entity, bb);
            motionX *= f1;
            motionY *= 0.800000011920929D;
            motionZ *= f1;
            motionY -= 0.02D;

            if (isCollidedHorizontally && mc.world.getCollisionBoxes(entity, bb.offset(motionX, motionY + 0.6000000238418579D - bb.minY + d0, motionZ)).isEmpty() && !mc.world.containsAnyLiquid(bb.offset(motionX, motionY + 0.6000000238418579D - bb.minY + d0, motionZ)))
            {
                motionY = 0.30000001192092896D;
            }
        }
        return bb;
    }

    private static void moveFlyingSimulation(float strafe, float forward, float friction, Entity entity) {
        float f = strafe * strafe + forward * forward;

        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt(f);

            if (f < 1.0F) {
                f = 1.0F;
            }

            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            float f1;
            float f2;
            f1 = MathHelper.sin(entity.rotationYaw * (float) Math.PI / 180.0F);
            f2 = MathHelper.cos(entity.rotationYaw * (float) Math.PI / 180.0F);

            motionX += strafe * f2 - forward * f1;
            motionZ += forward * f2 + strafe * f1;
        }
    }

    private static AxisAlignedBB moveEntitySimulation(double x, double y, double z,Entity entity ,AxisAlignedBB bb) {
        if (mc.world.isMaterialInBB(bb.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.WEB)) {
            x *= 0.25D;
            y *= 0.05000000074505806D;
            z *= 0.25D;
            motionX = 0.0D;
            motionY = 0.0D;
            motionZ = 0.0D;
        }

        double d3 = x;
        double d4 = y;
        double d5 = z;
        boolean flag = ((onGround && entity.isSneaking())) && entity instanceof EntityPlayer;

        if (flag) {
            double d6;

            for (d6 = 0.05D; x != 0.0D && mc.world.getCollisionBoxes(entity, bb.offset(x, -1.0D, 0.0D)).isEmpty(); d3 = x) {
                if (x < d6 && x >= -d6) {
                    x = 0.0D;
                } else if (x > 0.0D) {
                    x -= d6;
                } else {
                    x += d6;
                }
            }

            for (; z != 0.0D && mc.world.getCollisionBoxes(entity, bb.offset(0.0D, -1.0D, z)).isEmpty(); d5 = z) {
                if (z < d6 && z >= -d6) {
                    z = 0.0D;
                } else if (z > 0.0D) {
                    z -= d6;
                } else {
                    z += d6;
                }
            }

            for (; x != 0.0D && z != 0.0D && mc.world.getCollisionBoxes(entity, bb.offset(x, -1.0D, z)).isEmpty(); d5 = z) {
                if (x < d6 && x >= -d6) {
                    x = 0.0D;
                } else if (x > 0.0D) {
                    x -= d6;
                } else {
                    x += d6;
                }

                d3 = x;

                if (z < d6 && z >= -d6) {
                    z = 0.0D;
                } else if (z > 0.0D) {
                    z -= d6;
                } else {
                    z += d6;
                }
            }
        }

        List<AxisAlignedBB> list1 = mc.world.getCollisionBoxes(entity, bb.offset(x, y, z));
        AxisAlignedBB axisalignedbb = bb;

        for (AxisAlignedBB axisalignedbb1 : list1) {
            y = axisalignedbb1.calculateYOffset(bb, y);
        }

        bb = bb.offset(0.0D, y, 0.0D);
        boolean flag1 = onGround || d4 != y && d4 < 0.0D;

        for (AxisAlignedBB axisalignedbb2 : list1) {
            x = axisalignedbb2.calculateXOffset(bb, x);
        }

        bb = bb.offset(x, 0.0D, 0.0D);

        for (AxisAlignedBB axisalignedbb13 : list1) {
            z = axisalignedbb13.calculateZOffset(bb, z);
        }

        bb = bb.offset(0.0D, 0.0D, z);

        if (entity.stepHeight > 0.0F && flag1 && (d3 != x || d5 != z)) {
            double d11 = x;
            double d7 = y;
            double d8 = z;
            AxisAlignedBB axisalignedbb3 = bb;
            bb = axisalignedbb;
            y = entity.stepHeight;
            List<AxisAlignedBB> list = mc.world.getCollisionBoxes(entity, bb.offset(d3, y, d5));
            AxisAlignedBB axisalignedbb4 = bb;
            AxisAlignedBB axisalignedbb5 = axisalignedbb4.offset(d3, 0.0D, d5);
            double d9 = y;

            for (AxisAlignedBB axisalignedbb6 : list) {
                d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
            }

            axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);
            double d15 = d3;

            for (AxisAlignedBB axisalignedbb7 : list) {
                d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
            }

            axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);
            double d16 = d5;

            for (AxisAlignedBB axisalignedbb8 : list) {
                d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
            }

            axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);
            AxisAlignedBB axisalignedbb14 = bb;
            double d17 = y;

            for (AxisAlignedBB axisalignedbb9 : list) {
                d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
            }

            axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
            double d18 = d3;

            for (AxisAlignedBB axisalignedbb10 : list) {
                d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
            }

            axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
            double d19 = d5;

            for (AxisAlignedBB axisalignedbb11 : list) {
                d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
            }

            axisalignedbb14 = axisalignedbb14.offset(0.0D, 0.0D, d19);
            double d20 = d15 * d15 + d16 * d16;
            double d10 = d18 * d18 + d19 * d19;

            if (d20 > d10) {
                x = d15;
                z = d16;
                y = -d9;
                bb = axisalignedbb4;
            } else {
                x = d18;
                z = d19;
                y = -d17;
                bb = axisalignedbb14;
            }

            for (AxisAlignedBB axisalignedbb12 : list) {
                y = axisalignedbb12.calculateYOffset(bb, y);
            }

            bb = bb.offset(0.0D, y, 0.0D);

            if (d11 * d11 + d8 * d8 >= x * x + z * z) {
                x = d11;
                y = d7;
                z = d8;
                bb = axisalignedbb3;
            }
        }

        isCollidedHorizontally = d3 != x || d5 != z;
        isCollidedVertically = d4 != y;
        onGround = isCollidedVertically && d4 < 0.0D;
        isCollided = isCollidedHorizontally || isCollidedVertically;
        Vec3d center2 = getCenter(bb);
        int i = MathHelper.floor(center2.x);
        int j = MathHelper.floor(bb.minY - 0.20000000298023224D);
        int k = MathHelper.floor(center2.z);
        BlockPos blockpos = new BlockPos(i, j, k);
        Block block1 = mc.world.getBlockState(blockpos).getBlock();

        if (block1.getMaterial(block1.getDefaultState()) == Material.AIR) {
            Block block = mc.world.getBlockState(blockpos.down()).getBlock();

            if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
                block1 = block;
                blockpos = blockpos.down();
            }
        }

        if (d3 != x) {
            motionX = 0.0D;
        }

        if (d5 != z) {
            motionZ = 0.0D;
        }

        if (d4 != y) {
            motionY = 0;
        }


        return bb;
    }

    private static float[] predictInput(double motionX, double motionZ, float yaw, boolean sneaking){
        float forward = (float) (motionZ*Math.cos(Math.toRadians(yaw))-motionX*Math.sin(Math.toRadians(yaw)));
        float strafe = (float) (motionX*Math.cos(Math.toRadians(yaw))+motionZ*Math.sin(Math.toRadians(yaw)));
        if(Math.abs(forward) > 0.1) {
            forward = Math.signum(forward);
        } else {
            forward = 0;
        }
        if(Math.abs(strafe) > 0.1) {
            strafe = Math.signum(strafe);
        } else {
            strafe = 0;
        }
        if(sneaking) {
            forward*=0.3;
            strafe*=0.3;
        }
        float[] input = {forward, strafe};
        return input;
    }

    public static class Prediction {
        double motionX;
        double motionY;
        double motionZ;
        boolean onGround;
        boolean isCollidedHorizontally;
        boolean isCollidedVertically;
        boolean isCollided;
        AxisAlignedBB boundingBox;

        public Prediction(double motionX, double motionY, double motionZ, boolean onGround, boolean isCollidedHorizontally, boolean isCollidedVertically, boolean isCollided, AxisAlignedBB boundingBox) {
            this.motionX = motionX;
            this.motionY = motionY;
            this.motionZ = motionZ;
            this.onGround = onGround;
            this.isCollidedHorizontally = isCollidedHorizontally;
            this.isCollidedVertically = isCollidedVertically;
            this.isCollided = isCollided;
            this.boundingBox = boundingBox;
        }

        public double getMotionX() {
            return motionX;
        }

        public double getMotionY() {
            return motionY;
        }

        public double getMotionZ() {
            return motionZ;
        }

        public boolean isOnGround() {
            return onGround;
        }

        public boolean isCollidedHorizontally() {
            return isCollidedHorizontally;
        }

        public boolean isCollidedVertically() {
            return isCollidedVertically;
        }

        public boolean isCollided() {
            return isCollided;
        }

        public AxisAlignedBB getBoundingBox() {
            return boundingBox;
        }

        public double getX() {
            return (boundingBox.maxX+boundingBox.minX)/2;
        }

        public double getY() {
            return boundingBox.minY;
        }

        public double getZ() {
            return (boundingBox.maxZ+boundingBox.minZ)/2;
        }
    }
}
