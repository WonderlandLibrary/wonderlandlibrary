package com.canon.majik.api.utils.player.rotation;

public class Rotation {
    private final float yaw, pitch;
    private final Result result;

    public Rotation(float yaw, float pitch, Result result){
        this.yaw = yaw;
        this.pitch = pitch;
        this.result = result;
    }

    public Rotation(float yaw, float pitch){
        this(yaw, pitch, Result.SUCCESS);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public Result getResult() {
        return result;
    }
}
