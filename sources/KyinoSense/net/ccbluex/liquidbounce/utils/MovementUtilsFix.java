/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0004J\u0006\u0010\u0014\u001a\u00020\u0012R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0007R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\r\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/utils/MovementUtilsFix;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "<set-?>", "", "bps", "getBps", "()D", "direction", "getDirection", "lastX", "lastY", "lastZ", "movingYaw", "", "getMovingYaw", "()F", "setMotion", "", "speed", "updateBlocksPerSecond", "KyinoClient"})
public final class MovementUtilsFix
extends MinecraftInstance {
    private static double bps;
    private static double lastX;
    private static double lastY;
    private static double lastZ;
    public static final MovementUtilsFix INSTANCE;

    public final double getDirection() {
        float rotationYaw = Minecraft.func_71410_x().field_71439_g.field_70177_z;
        if (Minecraft.func_71410_x().field_71439_g.field_70701_bs < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.func_71410_x().field_71439_g.field_70701_bs < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.func_71410_x().field_71439_g.field_70701_bs > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.func_71410_x().field_71439_g.field_70702_br > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (Minecraft.func_71410_x().field_71439_g.field_70702_br < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public final void setMotion(double speed) {
        double forward = MinecraftInstance.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = MinecraftInstance.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = MinecraftInstance.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            MinecraftInstance.mc.field_71439_g.field_70159_w = 0.0;
            MinecraftInstance.mc.field_71439_g.field_70179_y = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double d = Math.toRadians(yaw + 90.0f);
            boolean bl = false;
            double cos = Math.cos(d);
            double d2 = Math.toRadians(yaw + 90.0f);
            boolean bl2 = false;
            double sin = Math.sin(d2);
            MinecraftInstance.mc.field_71439_g.field_70159_w = forward * speed * cos + strafe * speed * sin;
            MinecraftInstance.mc.field_71439_g.field_70179_y = forward * speed * sin - strafe * speed * cos;
        }
    }

    public final double getBps() {
        return bps;
    }

    public final void updateBlocksPerSecond() {
        if (MinecraftInstance.mc.field_71439_g == null || MinecraftInstance.mc.field_71439_g.field_70173_aa < 1) {
            bps = 0.0;
        }
        double distance = MinecraftInstance.mc.field_71439_g.func_70011_f(lastX, lastY, lastZ);
        lastX = MinecraftInstance.mc.field_71439_g.field_70165_t;
        lastY = MinecraftInstance.mc.field_71439_g.field_70163_u;
        lastZ = MinecraftInstance.mc.field_71439_g.field_70161_v;
        bps = distance * (double)((float)20 * MinecraftInstance.mc.field_71428_T.field_74278_d);
    }

    public final float getMovingYaw() {
        return (float)(this.getDirection() * (double)180.0f / Math.PI);
    }

    private MovementUtilsFix() {
    }

    static {
        MovementUtilsFix movementUtilsFix;
        INSTANCE = movementUtilsFix = new MovementUtilsFix();
    }
}

