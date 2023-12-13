package me.dev.legacy.manager;

import net.minecraft.client.Minecraft;

public class Mapping
{
    public static final String tickLength;
    public static final String timer;
    public static final String placedBlockDirection;
    public static final String playerPosLookYaw;
    public static final String playerPosLookPitch;
    public static final String isInWeb;
    public static final String cPacketPlayerYaw;
    public static final String cPacketPlayerPitch;
    public static final String renderManagerRenderPosX;
    public static final String renderManagerRenderPosY;
    public static final String renderManagerRenderPosZ;
    public static final String rightClickDelayTimer;
    public static final String sPacketEntityVelocityMotionX;
    public static final String sPacketEntityVelocityMotionY;
    public static final String sPacketEntityVelocityMotionZ;

    public static boolean isObfuscated() {
        try {
            return Minecraft.class.getDeclaredField("instance") == null;
        }
        catch (Exception e) {
            return true;
        }
    }

    static {
        tickLength = (isObfuscated() ? "field_194149_e" : "tickLength");
        timer = (isObfuscated() ? "field_71428_T" : "timer");
        placedBlockDirection = (isObfuscated() ? "field_149579_d" : "placedBlockDirection");
        playerPosLookYaw = (isObfuscated() ? "field_148936_d" : "yaw");
        playerPosLookPitch = (isObfuscated() ? "field_148937_e" : "pitch");
        isInWeb = (isObfuscated() ? "field_70134_J" : "isInWeb");
        cPacketPlayerYaw = (isObfuscated() ? "field_149476_e" : "yaw");
        cPacketPlayerPitch = (isObfuscated() ? "field_149473_f" : "pitch");
        renderManagerRenderPosX = (isObfuscated() ? "field_78725_b" : "renderPosX");
        renderManagerRenderPosY = (isObfuscated() ? "field_78726_c" : "renderPosY");
        renderManagerRenderPosZ = (isObfuscated() ? "field_78723_d" : "renderPosZ");
        rightClickDelayTimer = (isObfuscated() ? "field_71467_ac" : "rightClickDelayTimer");
        sPacketEntityVelocityMotionX = (isObfuscated() ? "field_70159_w" : "motionX");
        sPacketEntityVelocityMotionY = (isObfuscated() ? "field_70181_x" : "motionY");
        sPacketEntityVelocityMotionZ = (isObfuscated() ? "field_70179_y" : "motionZ");
    }
}
