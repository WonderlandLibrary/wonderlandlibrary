/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.event.StrafeEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.minecraft.client.entity.EntityPlayerSP;
import org.jetbrains.annotations.NotNull;

@Info(name="Strafe", description="Allows you to freely move in mid air.", category=Category.MOVEMENT, cnName="\u7075\u6d3b\u79fb\u52a8")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0014H\u0007J\u0010\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0016H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lnet/dev/important/modules/module/modules/movement/Strafe;", "Lnet/dev/important/modules/module/Module;", "()V", "allDirectionsJumpValue", "Lnet/dev/important/value/BoolValue;", "jump", "", "noMoveStopValue", "onGroundStrafeValue", "strengthValue", "Lnet/dev/important/value/FloatValue;", "wasDown", "getMoveYaw", "", "onEnable", "", "onJump", "event", "Lnet/dev/important/event/JumpEvent;", "onStrafe", "Lnet/dev/important/event/StrafeEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Strafe
extends Module {
    @NotNull
    private FloatValue strengthValue = new FloatValue("Strength", 0.5f, 0.0f, 1.0f, "x");
    @NotNull
    private BoolValue noMoveStopValue = new BoolValue("NoMoveStop", false);
    @NotNull
    private BoolValue onGroundStrafeValue = new BoolValue("OnGroundStrafe", false);
    @NotNull
    private BoolValue allDirectionsJumpValue = new BoolValue("AllDirectionsJump", false);
    private boolean wasDown;
    private boolean jump;

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.jump) {
            event.cancelEvent();
        }
    }

    @Override
    public void onEnable() {
        this.wasDown = false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block8: {
            block9: {
                Intrinsics.checkNotNullParameter(event, "event");
                EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNull(entityPlayerSP);
                if (!entityPlayerSP.field_70122_E || !MinecraftInstance.mc.field_71474_y.field_74314_A.func_151470_d() || !((Boolean)this.allDirectionsJumpValue.get()).booleanValue()) break block8;
                EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNull(entityPlayerSP2);
                if (!(entityPlayerSP2.field_71158_b.field_78900_b == 0.0f)) break block9;
                EntityPlayerSP entityPlayerSP3 = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNull(entityPlayerSP3);
                if (entityPlayerSP3.field_71158_b.field_78902_a == 0.0f) break block8;
            }
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP);
            if (!entityPlayerSP.func_70090_H()) {
                EntityPlayerSP entityPlayerSP4 = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNull(entityPlayerSP4);
                if (!entityPlayerSP4.func_180799_ab()) {
                    EntityPlayerSP entityPlayerSP5 = MinecraftInstance.mc.field_71439_g;
                    Intrinsics.checkNotNull(entityPlayerSP5);
                    if (!entityPlayerSP5.func_70617_f_()) {
                        EntityPlayerSP entityPlayerSP6 = MinecraftInstance.mc.field_71439_g;
                        Intrinsics.checkNotNull(entityPlayerSP6);
                        if (!entityPlayerSP6.field_70134_J) {
                            if (MinecraftInstance.mc.field_71474_y.field_74314_A.func_151470_d()) {
                                MinecraftInstance.mc.field_71474_y.field_74314_A.field_74513_e = false;
                                this.wasDown = true;
                            }
                            EntityPlayerSP entityPlayerSP7 = MinecraftInstance.mc.field_71439_g;
                            Intrinsics.checkNotNull(entityPlayerSP7);
                            float yaw = entityPlayerSP7.field_70177_z;
                            Intrinsics.checkNotNull(MinecraftInstance.mc.field_71439_g);
                            MinecraftInstance.mc.field_71439_g.field_70177_z = this.getMoveYaw();
                            EntityPlayerSP entityPlayerSP8 = MinecraftInstance.mc.field_71439_g;
                            Intrinsics.checkNotNull(entityPlayerSP8);
                            entityPlayerSP8.func_70664_aZ();
                            Intrinsics.checkNotNull(MinecraftInstance.mc.field_71439_g);
                            MinecraftInstance.mc.field_71439_g.field_70177_z = yaw;
                            this.jump = true;
                            if (!this.wasDown) return;
                            MinecraftInstance.mc.field_71474_y.field_74314_A.field_74513_e = true;
                            this.wasDown = false;
                            return;
                        }
                    }
                }
            }
        }
        this.jump = false;
    }

    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP);
        double d = entityPlayerSP.field_70159_w;
        EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP2);
        double d2 = d * entityPlayerSP2.field_70159_w;
        EntityPlayerSP entityPlayerSP3 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP3);
        double d3 = entityPlayerSP3.field_70179_y;
        EntityPlayerSP entityPlayerSP4 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP4);
        double shotSpeed = Math.sqrt(d2 + d3 * entityPlayerSP4.field_70179_y);
        double speed = shotSpeed * ((Number)this.strengthValue.get()).doubleValue();
        EntityPlayerSP entityPlayerSP5 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP5);
        double motionX = entityPlayerSP5.field_70159_w * (double)(1.0f - ((Number)this.strengthValue.get()).floatValue());
        EntityPlayerSP entityPlayerSP6 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP6);
        double motionZ = entityPlayerSP6.field_70179_y * (double)(1.0f - ((Number)this.strengthValue.get()).floatValue());
        EntityPlayerSP entityPlayerSP7 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP7);
        if (entityPlayerSP7.field_71158_b.field_78900_b == 0.0f) {
            EntityPlayerSP entityPlayerSP8 = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP8);
            if (entityPlayerSP8.field_71158_b.field_78902_a == 0.0f) {
                if (((Boolean)this.noMoveStopValue.get()).booleanValue()) {
                    Intrinsics.checkNotNull(MinecraftInstance.mc.field_71439_g);
                    MinecraftInstance.mc.field_71439_g.field_70159_w = 0.0;
                    Intrinsics.checkNotNull(MinecraftInstance.mc.field_71439_g);
                    MinecraftInstance.mc.field_71439_g.field_70179_y = 0.0;
                }
                return;
            }
        }
        EntityPlayerSP entityPlayerSP9 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP9);
        if (!entityPlayerSP9.field_70122_E || ((Boolean)this.onGroundStrafeValue.get()).booleanValue()) {
            float yaw = this.getMoveYaw();
            Intrinsics.checkNotNull(MinecraftInstance.mc.field_71439_g);
            MinecraftInstance.mc.field_71439_g.field_70159_w = -Math.sin(Math.toRadians(yaw)) * speed + motionX;
            Intrinsics.checkNotNull(MinecraftInstance.mc.field_71439_g);
            MinecraftInstance.mc.field_71439_g.field_70179_y = Math.cos(Math.toRadians(yaw)) * speed + motionZ;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private final float getMoveYaw() {
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP);
        float moveYaw = entityPlayerSP.field_70177_z;
        EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP2);
        if (!(entityPlayerSP2.field_70701_bs == 0.0f)) {
            EntityPlayerSP entityPlayerSP3 = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP3);
            if (entityPlayerSP3.field_70702_br == 0.0f) {
                EntityPlayerSP entityPlayerSP4 = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNull(entityPlayerSP4);
                return moveYaw += (float)(entityPlayerSP4.field_70701_bs > 0.0f ? 0 : 180);
            }
        }
        EntityPlayerSP entityPlayerSP5 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP5);
        if (!(entityPlayerSP5.field_70701_bs == 0.0f)) {
            EntityPlayerSP entityPlayerSP6 = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP6);
            if (!(entityPlayerSP6.field_70702_br == 0.0f)) {
                EntityPlayerSP entityPlayerSP7 = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNull(entityPlayerSP7);
                if (entityPlayerSP7.field_70701_bs > 0.0f) {
                    EntityPlayerSP entityPlayerSP8 = MinecraftInstance.mc.field_71439_g;
                    Intrinsics.checkNotNull(entityPlayerSP8);
                    moveYaw += (float)(entityPlayerSP8.field_70702_br > 0.0f ? -45 : 45);
                } else {
                    EntityPlayerSP entityPlayerSP9 = MinecraftInstance.mc.field_71439_g;
                    Intrinsics.checkNotNull(entityPlayerSP9);
                    moveYaw -= (float)(entityPlayerSP9.field_70702_br > 0.0f ? -45 : 45);
                }
                EntityPlayerSP entityPlayerSP10 = MinecraftInstance.mc.field_71439_g;
                Intrinsics.checkNotNull(entityPlayerSP10);
                return moveYaw += (float)(entityPlayerSP10.field_70701_bs > 0.0f ? 0 : 180);
            }
        }
        EntityPlayerSP entityPlayerSP11 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP11);
        if (entityPlayerSP11.field_70702_br == 0.0f) return moveYaw;
        EntityPlayerSP entityPlayerSP12 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP12);
        if (!(entityPlayerSP12.field_70701_bs == 0.0f)) return moveYaw;
        EntityPlayerSP entityPlayerSP13 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP13);
        moveYaw += (float)(entityPlayerSP13.field_70702_br > 0.0f ? -90 : 90);
        return moveYaw;
    }
}

