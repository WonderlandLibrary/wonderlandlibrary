/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.stats.StatList
 *  net.minecraft.util.MathHelper
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.StepConfirmEvent;
import net.dev.important.event.StepEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.exploit.Phase;
import net.dev.important.modules.module.modules.movement.Fly;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;

@Info(name="Step", description="Allows you to step up blocks.", category=Category.MOVEMENT, cnName="\u5feb\u901f\u4e0a\u65b9\u5757")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001d\u001a\u00020\bH\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020\u001fH\u0016J\u0010\u0010!\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020#H\u0007J\u0010\u0010$\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020%H\u0007J\u0010\u0010&\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020'H\u0007J\u0010\u0010(\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020)H\u0007J\u0010\u0010*\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020+H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\u00020\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2={"Lnet/dev/important/modules/module/modules/movement/Step;", "Lnet/dev/important/modules/module/Module;", "()V", "delayValue", "Lnet/dev/important/value/IntegerValue;", "heightValue", "Lnet/dev/important/value/FloatValue;", "isAACStep", "", "isStep", "jumpHeightValue", "modeValue", "Lnet/dev/important/value/ListValue;", "ncpNextStep", "", "spartanSwitch", "stepX", "", "stepY", "stepZ", "tag", "", "getTag", "()Ljava/lang/String;", "ticks", "timer", "Lnet/dev/important/utils/timer/MSTimer;", "timerValue", "usedTimer", "couldStep", "fakeJump", "", "onDisable", "onMove", "event", "Lnet/dev/important/event/MoveEvent;", "onPacket", "Lnet/dev/important/event/PacketEvent;", "onStep", "Lnet/dev/important/event/StepEvent;", "onStepConfirm", "Lnet/dev/important/event/StepConfirmEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Step
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final FloatValue heightValue;
    @NotNull
    private final FloatValue timerValue;
    @NotNull
    private final FloatValue jumpHeightValue;
    @NotNull
    private final IntegerValue delayValue;
    private boolean isStep;
    private double stepX;
    private double stepY;
    private double stepZ;
    private int ncpNextStep;
    private boolean spartanSwitch;
    private boolean isAACStep;
    private int ticks;
    @NotNull
    private final MSTimer timer;
    private boolean usedTimer;

    public Step() {
        String[] stringArray = new String[]{"Vanilla", "Jump", "Hypixel", "MotionNCP", "OldNCP", "AAC", "LAAC", "AAC3.3.4", "Spartan", "Rewinside", "1.5Twillight"};
        this.modeValue = new ListValue("Mode", stringArray, "Hypixel");
        this.heightValue = new FloatValue("Height", 1.0f, 0.6f, 10.0f);
        this.timerValue = new FloatValue("Timer", 1.0f, 0.3f, 10.0f, "x");
        this.jumpHeightValue = new FloatValue("JumpHeight", 0.42f, 0.37f, 0.42f);
        this.delayValue = new IntegerValue("Delay", 0, 0, 500, "ms");
        this.timer = new MSTimer();
    }

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        MinecraftInstance.mc.field_71439_g.field_70138_W = 0.5f;
        MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
        MinecraftInstance.mc.field_71439_g.field_71102_ce = 0.02f;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.usedTimer) {
            MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
            this.usedTimer = false;
        }
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals(mode, "jump", true) && MinecraftInstance.mc.field_71439_g.field_70123_F && MinecraftInstance.mc.field_71439_g.field_70122_E && !MinecraftInstance.mc.field_71474_y.field_74314_A.func_151470_d()) {
            this.fakeJump();
            MinecraftInstance.mc.field_71439_g.field_70181_x = ((Number)this.jumpHeightValue.get()).floatValue();
        } else if (StringsKt.equals(mode, "laac", true)) {
            if (!(!MinecraftInstance.mc.field_71439_g.field_70123_F || MinecraftInstance.mc.field_71439_g.func_70617_f_() || MinecraftInstance.mc.field_71439_g.func_70090_H() || MinecraftInstance.mc.field_71439_g.func_180799_ab() || MinecraftInstance.mc.field_71439_g.field_70134_J)) {
                if (MinecraftInstance.mc.field_71439_g.field_70122_E && this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                    this.isStep = true;
                    this.fakeJump();
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70181_x += 0.620000001490116;
                    float f = MinecraftInstance.mc.field_71439_g.field_70177_z * ((float)Math.PI / 180);
                    EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP2.field_70159_w -= (double)MathHelper.func_76126_a((float)f) * 0.2;
                    entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP2.field_70179_y += (double)MathHelper.func_76134_b((float)f) * 0.2;
                    this.timer.reset();
                }
                MinecraftInstance.mc.field_71439_g.field_70122_E = true;
            } else {
                this.isStep = false;
            }
        } else if (StringsKt.equals(mode, "aac3.3.4", true)) {
            if (MinecraftInstance.mc.field_71439_g.field_70123_F && MovementUtils.isMoving()) {
                EntityPlayerSP entityPlayerSP;
                if (MinecraftInstance.mc.field_71439_g.field_70122_E && this.couldStep()) {
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70159_w *= 1.26;
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70179_y *= 1.26;
                    MinecraftInstance.mc.field_71439_g.func_70664_aZ();
                    this.isAACStep = true;
                }
                if (this.isAACStep) {
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70181_x -= 0.015;
                    if (!MinecraftInstance.mc.field_71439_g.func_71039_bw() && MinecraftInstance.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f) {
                        MinecraftInstance.mc.field_71439_g.field_70747_aH = 0.3f;
                    }
                }
            } else {
                this.isAACStep = false;
            }
        } else if (StringsKt.equals(mode, "1.5Twillight", true)) {
            if (MovementUtils.isMoving() && MinecraftInstance.mc.field_71439_g.field_70123_F) {
                int n = this.ticks;
                this.ticks = n + 1;
                if (this.ticks == 1) {
                    MinecraftInstance.mc.field_71439_g.field_70181_x = 0.4399;
                }
                if (this.ticks == 12) {
                    MinecraftInstance.mc.field_71439_g.field_70181_x = 0.4399;
                }
                if (this.ticks >= 40) {
                    this.ticks = 0;
                }
            } else if (MinecraftInstance.mc.field_71439_g.field_70122_E) {
                this.ticks = 0;
            }
        }
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals(mode, "motionncp", true) && MinecraftInstance.mc.field_71439_g.field_70123_F && !MinecraftInstance.mc.field_71474_y.field_74314_A.func_151470_d()) {
            if (MinecraftInstance.mc.field_71439_g.field_70122_E && this.couldStep()) {
                this.fakeJump();
                MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
                event.setY(0.41999998688698);
                this.ncpNextStep = 1;
            } else if (this.ncpNextStep == 1) {
                event.setY(0.33319999363422);
                this.ncpNextStep = 2;
            } else if (this.ncpNextStep == 2) {
                double yaw = MovementUtils.getDirection();
                event.setY(0.24813599859094704);
                event.setX(-Math.sin(yaw) * 0.7);
                event.setZ(Math.cos(yaw) * 0.7);
                this.ncpNextStep = 0;
            }
        }
    }

    @EventTarget
    public final void onStep(@NotNull StepEvent event) {
        float height;
        String flyMode;
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        Module module2 = Client.INSTANCE.getModuleManager().getModule(Phase.class);
        Intrinsics.checkNotNull(module2);
        Phase phaseMod = (Phase)module2;
        if (phaseMod.getState() && !StringsKt.equals((String)phaseMod.modeValue.get(), "hypixel", true)) {
            event.setStepHeight(0.0f);
            return;
        }
        Module module3 = Client.INSTANCE.getModuleManager().get(Fly.class);
        if (module3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.movement.Fly");
        }
        Fly fly = (Fly)module3;
        if (fly.getState() && StringsKt.equals(flyMode = (String)fly.modeValue.get(), "Rewinside", true)) {
            event.setStepHeight(0.0f);
            return;
        }
        String mode = (String)this.modeValue.get();
        if (!MinecraftInstance.mc.field_71439_g.field_70122_E || !this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue()) || StringsKt.equals(mode, "Jump", true) || StringsKt.equals(mode, "MotionNCP", true) || StringsKt.equals(mode, "LAAC", true) || StringsKt.equals(mode, "AAC3.3.4", true) || StringsKt.equals(mode, "AACv4", true) || StringsKt.equals(mode, "1.5Twillight", true)) {
            MinecraftInstance.mc.field_71439_g.field_70138_W = 0.5f;
            event.setStepHeight(0.5f);
            return;
        }
        MinecraftInstance.mc.field_71439_g.field_70138_W = height = ((Number)this.heightValue.get()).floatValue();
        MinecraftInstance.mc.field_71428_T.field_74278_d = ((Number)this.timerValue.get()).floatValue();
        this.usedTimer = true;
        event.setStepHeight(height);
        if (event.getStepHeight() > 0.5f) {
            this.isStep = true;
            this.stepX = MinecraftInstance.mc.field_71439_g.field_70165_t;
            this.stepY = MinecraftInstance.mc.field_71439_g.field_70163_u;
            this.stepZ = MinecraftInstance.mc.field_71439_g.field_70161_v;
        }
    }

    @EventTarget(ignoreCondition=true)
    public final void onStepConfirm(@NotNull StepConfirmEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71439_g == null || !this.isStep) {
            return;
        }
        if (MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72338_b - this.stepY > 0.5) {
            String mode = (String)this.modeValue.get();
            if (StringsKt.equals(mode, "Hypixel", true) || StringsKt.equals(mode, "AAC", true)) {
                this.fakeJump();
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                this.timer.reset();
            } else if (StringsKt.equals(mode, "Spartan", true)) {
                this.fakeJump();
                if (this.spartanSwitch) {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                } else {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.6, this.stepZ, false));
                }
                this.spartanSwitch = !this.spartanSwitch;
                this.timer.reset();
            } else if (StringsKt.equals(mode, "Rewinside", true)) {
                this.fakeJump();
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                this.timer.reset();
            }
        }
        this.isStep = false;
        this.stepX = 0.0;
        this.stepY = 0.0;
        this.stepZ = 0.0;
    }

    @EventTarget(ignoreCondition=true)
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer && this.isStep && StringsKt.equals((String)this.modeValue.get(), "OldNCP", true)) {
            ((C03PacketPlayer)packet).field_149477_b += 0.07;
            this.isStep = false;
        }
    }

    private final void fakeJump() {
        MinecraftInstance.mc.field_71439_g.field_70160_al = true;
        MinecraftInstance.mc.field_71439_g.func_71029_a(StatList.field_75953_u);
    }

    private final boolean couldStep() {
        double yaw = MovementUtils.getDirection();
        double x = -Math.sin(yaw) * 0.4;
        double z = Math.cos(yaw) * 0.4;
        return MinecraftInstance.mc.field_71441_e.func_147461_a(MinecraftInstance.mc.field_71439_g.func_174813_aQ().func_72317_d(x, 1.001335979112147, z)).isEmpty();
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

