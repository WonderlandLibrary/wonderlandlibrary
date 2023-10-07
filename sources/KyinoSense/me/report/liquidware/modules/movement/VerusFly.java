/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.concurrent.GenericFutureListener
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.util.MathHelper
 */
package me.report.liquidware.modules.movement;

import io.netty.util.concurrent.GenericFutureListener;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import obfuscator.NativeMethod;

@ModuleInfo(name="VerusFly", description="Allows you to fly in verus.", category=ModuleCategory.MOVEMENT)
public class VerusFly
extends Module {
    private int ticks;
    private int offGroundTicks;

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    private void onPre(MotionEvent event) {
        if (event.getEventState() == EventState.PRE) {
            ++this.ticks;
            this.offGroundTicks = Minecraft.func_71410_x().field_71439_g.field_70122_E ? 0 : ++this.offGroundTicks;
            if (this.ticks == 1 && !Minecraft.func_71410_x().field_71439_g.field_70122_E) {
                this.setState(false);
                return;
            }
            if (this.offGroundTicks >= 2 && !Minecraft.func_71410_x().field_71474_y.field_74311_E.func_151470_d()) {
                if (Minecraft.func_71410_x().field_71474_y.field_74314_A.func_151470_d()) {
                    if (Minecraft.func_71410_x().field_71439_g.field_70173_aa % 2 == 0) {
                        Minecraft.func_71410_x().field_71439_g.field_70181_x = 0.42f;
                    }
                } else {
                    if (Minecraft.func_71410_x().field_71439_g.field_70122_E) {
                        Minecraft.func_71410_x().field_71439_g.func_70664_aZ();
                    }
                    if (Minecraft.func_71410_x().field_71439_g.field_70143_R > 1.0f) {
                        Minecraft.func_71410_x().field_71439_g.field_70181_x = -(Minecraft.func_71410_x().field_71439_g.field_70163_u - Math.floor(Minecraft.func_71410_x().field_71439_g.field_70163_u));
                    }
                    if (Minecraft.func_71410_x().field_71439_g.field_70181_x == 0.0) {
                        Minecraft.func_71410_x().field_71439_g.func_70664_aZ();
                        Minecraft.func_71410_x().field_71439_g.field_70122_E = true;
                        Minecraft.func_71410_x().field_71439_g.field_70143_R = 0.0f;
                    }
                }
            }
            this.strafe(1.0f);
            if (this.ticks == 1) {
                VerusFly.mc.field_71428_T.field_74278_d = 0.15f;
                VerusFly.damagePlayer(3.42f, 1, true, false);
                Minecraft.func_71410_x().field_71439_g.func_70664_aZ();
                VerusFly.mc.field_71439_g.field_70122_E = true;
            } else {
                VerusFly.mc.field_71428_T.field_74278_d = 1.0f;
            }
        }
    }

    public static void damagePlayer(double value, int packets, boolean groundCheck, boolean hurtTimeCheck) {
        if (!(groundCheck && !Minecraft.func_71410_x().field_71439_g.field_70122_E || hurtTimeCheck && Minecraft.func_71410_x().field_71439_g.field_70737_aN != 0)) {
            double x = Minecraft.func_71410_x().field_71439_g.field_70165_t;
            double y = Minecraft.func_71410_x().field_71439_g.field_70163_u;
            double z = Minecraft.func_71410_x().field_71439_g.field_70161_v;
            for (int i = 0; i < packets; ++i) {
                Minecraft.func_71410_x().func_147114_u().func_147298_b().func_179288_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + value, z, false), null, new GenericFutureListener[0]);
                Minecraft.func_71410_x().func_147114_u().func_147298_b().func_179288_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false), null, new GenericFutureListener[0]);
            }
            Minecraft.func_71410_x().func_147114_u().func_147298_b().func_179288_a((Packet)new C03PacketPlayer(true), null, new GenericFutureListener[0]);
        }
    }

    public void strafe(float speed) {
        if (!this.isMoving()) {
            return;
        }
        double yaw = this.getDirection();
        Minecraft.func_71410_x().field_71439_g.field_70159_w = -MathHelper.func_76126_a((float)((float)yaw)) * speed;
        Minecraft.func_71410_x().field_71439_g.field_70179_y = MathHelper.func_76134_b((float)((float)yaw)) * speed;
    }

    public double getDirection() {
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

    public boolean isMoving() {
        return Minecraft.func_71410_x().field_71439_g != null && (Minecraft.func_71410_x().field_71439_g.field_71158_b.field_78900_b != 0.0f || Minecraft.func_71410_x().field_71439_g.field_71158_b.field_78902_a != 0.0f);
    }
}

