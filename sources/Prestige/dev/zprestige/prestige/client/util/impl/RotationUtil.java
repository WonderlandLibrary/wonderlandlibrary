package dev.zprestige.prestige.client.util.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.util.MC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;

public class RotationUtil implements MC {

    public static RotationUtil INSTANCE = new RotationUtil();

    public Rotation getNeededRotations(float f, float f2, float f3, float f4, float f5, float f6) {
        double d = f - f4;
        double d2 = f2 - f5;
        double d3 = f3 - f6;
        float[] fArray = new float[2];
        fArray[0] = getMc().player.getYaw();
        fArray[1] = getMc().player.getPitch();
        return new Rotation(getMc().player.getYaw() + wrap((float)Math.toDegrees(Math.atan2(d3, d)) - 90 - getMc().player.getYaw()), fArray[1] + wrap(-((float)Math.toDegrees(Math.atan2(d2, Math.sqrt(d * d + d3 * d3)))) - fArray[1]));
    }

    public Rotation getNeededRotations(float f, float f2, float f3) {
        Vec3d vec3d = getMc().player.getEyePos();
        double d = f - vec3d.x;
        double d2 = f2 - vec3d.y;
        double d3 = f3 - vec3d.z;
        float[] fArray = new float[2];
        fArray[0] = getMc().player.getYaw();
        fArray[1] = getMc().player.getPitch();
        return new Rotation(fArray[0] + wrap((float)Math.toDegrees(Math.atan2(d3, d)) - 90 - fArray[0]), fArray[1] + wrap((float)(-Math.toDegrees(Math.atan2(d2, Math.sqrt(d * d + d3 * d3)))) - fArray[1]));
    }

    public BlockHitResult blockRaycast(BlockPos blockPos) {
        return getMc().world.raycastBlock(getMc().player.getEyePos(), getMc().player.getEyePos().add(getPlayerLookVec(getMc().player).multiply(6)), blockPos, VoxelShapes.fullCube(), getMc().world.getBlockState(blockPos));
    }

    public HitResult playerRaycast(Rotation rotation) {
        return getMc().world.raycast(new RaycastContext(getMc().player.getEyePos(), getPlayerLookVec(rotation).multiply(6).add(getMc().player.getEyePos()), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, getMc().player));
    }

    public Vec3d getPlayerLookVec(Rotation rotation) {
        float f = (float)Math.PI / 180;
        float f2 = (float)Math.PI;
        float f3 = -MathHelper.cos(-rotation.getPitch() * f);
        return new Vec3d(MathHelper.sin(-rotation.getYaw() * f - f2) * f3, MathHelper.sin(-rotation.getPitch() * f), MathHelper.cos(-rotation.getYaw() * f - f2) * f3).normalize();
    }

    public boolean setPitch(Rotation rotation, float f, float f2, float f3) {
        setRotation(new Rotation(getMc().player.getYaw(), MathUtil.interpolate(getMc().player.getPitch(), rotation.getPitch() + f2 / 2, Prestige.Companion.getRenderManager().getMs() * (1.1f - f) * 5 / f3 * RandomUtil.INSTANCE.randomInRange(0.5f, 1))));
        return getMc().player.getPitch() - 5 < rotation.getPitch() && getMc().player.getPitch() + 5 > rotation.getPitch();
    }

    Vec3d getPlayerLookVec(PlayerEntity playerEntity) {
        float f = (float)Math.PI / 180;
        float f2 = (float)Math.PI;
        float f3 = -MathHelper.cos(-playerEntity.getPitch() * f);
        return new Vec3d(MathHelper.sin(-playerEntity.getYaw() * f - f2) * f3, MathHelper.sin(-playerEntity.getPitch() * f), MathHelper.cos(-playerEntity.getYaw() * f - f2) * f3).normalize();
    }

    public void setRotation(Rotation rotation) {
        getMc().player.setYaw(rotation.getYaw());
        getMc().player.setPitch(rotation.getPitch());
    }

    public BlockHitResult blockRaycast(BlockPos blockPos, PlayerEntity playerEntity) {
        return getMc().world.raycastBlock(playerEntity.getEyePos(), getPlayerLookVec(playerEntity).multiply(6).add(playerEntity.getEyePos()), blockPos, VoxelShapes.fullCube(), getMc().world.getBlockState(blockPos));
    }

    public BlockHitResult blockRaycastRotation(BlockPos blockPos, Rotation rotation) {
        return getMc().world.raycastBlock(getMc().player.getEyePos(), getPlayerLookVec(rotation).multiply(6), blockPos, VoxelShapes.fullCube(), getMc().world.getBlockState(blockPos));
    }

    float wrap(float f) {
        float f2 = f;
        if ((f2 %= 360) >= 180) {
            f2 -= 360;
        }
        if (f2 < -180) {
            f2 += 360;
        }
        return f2;
    }

    public Rotation getNeededRotations(Vec3d vec3d) {
        return this.getNeededRotations((float)vec3d.x, (float)vec3d.y, (float)vec3d.z);
    }

    public boolean setYaw(Rotation rotation, float f, float f2, float f3) {
        float f4 = MathUtil.interpolate(getMc().player.getYaw(), rotation.getYaw() + f2 / 2, Prestige.Companion.getRenderManager().getMs() * (1.1f - f) * 5 / f3 * RandomUtil.INSTANCE.randomInRange(0.5f, 1));
        float f5 = MathUtil.interpolate(getMc().player.getPitch(), rotation.getPitch() + f2 / 2, Prestige.Companion.getRenderManager().getMs() * (1.1f - f) * 5 / f3 * RandomUtil.INSTANCE.randomInRange(0.5f, 1));
        setRotation(new Rotation(f4, f5));
        return getMc().player.getYaw() - 5 < rotation.getYaw() && getMc().player.getYaw() + 5 > rotation.getYaw();
    }

    public boolean setRotation(Rotation rotation, float f, float f2, float f3) {
        float f4 = MathUtil.interpolate(getMc().player.getYaw(), rotation.getYaw() + f2, Prestige.Companion.getRenderManager().getMs() * (1.1f - f) * 5 / f3 * RandomUtil.INSTANCE.randomInRange(0.5f, 1));
        float f5 = MathUtil.interpolate(getMc().player.getPitch(), rotation.getPitch() + f2 / 2, Prestige.Companion.getRenderManager().getMs() * (1.1f - f) * 5 / f3 * RandomUtil.INSTANCE.randomInRange(0.5f, 1));
        setRotation(new Rotation(f4, f5));
        return getMc().player.getYaw() - 5 < rotation.getYaw() && getMc().player.getYaw() + 5 > rotation.getYaw() && getMc().player.getPitch() - 5 < rotation.getPitch() && getMc().player.getPitch() + 5 > rotation.getPitch();
    }

    public boolean inRange(Rotation rotation, Rotation rotation2, float f) {
        return Math.abs(rotation2.getYaw() - rotation.getYaw()) < f && Math.abs(rotation2.getPitch() - rotation.getPitch()) < f;
    }

    public void setRotation(float f, float f2) {
        this.setRotation(new Rotation(f, f2));
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }
}
