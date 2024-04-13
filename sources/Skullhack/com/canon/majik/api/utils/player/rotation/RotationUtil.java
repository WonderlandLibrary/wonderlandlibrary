package com.canon.majik.api.utils.player.rotation;

import com.canon.majik.api.event.events.PlayerUpdateEvent;
import com.canon.majik.api.utils.Globals;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil implements Globals {

    private static Rotation calculateIncrement(float prev, float current, boolean yaw, float max) {
        float diff = 0;
        if (prev < current) {
            diff = current - prev;
        } else if (prev > current) {
            diff = -(prev - current);
        }

        return new Rotation((yaw ? diff < -350 ? -180 : diff > 350 ? 180 : prev : prev) + MathHelper.clamp(diff, -max, max), 0, diff < -max || diff > max ? Result.AWAIT_ROTATIONS : Result.SUCCESS);
    }

    public static Rotation face(Rotation angle, PlayerUpdateEvent event, Rotation prev, float maxYaw) {
        Rotation yaw = calculateIncrement(prev.getYaw(), angle.getYaw(), true, maxYaw);
        Rotation pitch = calculateIncrement(prev.getPitch(), angle.getPitch(), false, maxYaw);

        event.setYaw(yaw.getYaw());
        event.setPitch(pitch.getYaw());

        return new Rotation(yaw.getYaw(), pitch.getYaw(), yaw.getResult() == Result.AWAIT_ROTATIONS || pitch.getResult() == Result.AWAIT_ROTATIONS ? Result.AWAIT_ROTATIONS : Result.SUCCESS);
    }

    public static Rotation facePos(Vec3d vec, PlayerUpdateEvent event, Rotation prev, float maxYaw) {
        Rotation angle = calculateAngle(vec);
        return face(angle, event, prev, maxYaw);
    }

    public static Rotation faceEntity(Entity entity, PlayerUpdateEvent event, Rotation prev, float maxYaw) {
        float partialTicks = mc.getRenderPartialTicks();
        Rotation angle = calculateAngle(entity.getPositionEyes(partialTicks));
        return face(angle, event, prev, maxYaw);
    }

    public static Rotation calculateAngle(Vec3d to) {
        float yaw = (float) (Math.toDegrees(Math.atan2(to.subtract(mc.player.getPositionEyes(1)).z, to.subtract(mc.player.getPositionEyes(1)).x)) - 90);
        float pitch = (float) Math.toDegrees(-Math.atan2(to.subtract(mc.player.getPositionEyes(1)).y, Math.hypot(to.subtract(mc.player.getPositionEyes(1)).x, to.subtract(mc.player.getPositionEyes(1)).z)));
        return new Rotation(MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch));
    }

}
