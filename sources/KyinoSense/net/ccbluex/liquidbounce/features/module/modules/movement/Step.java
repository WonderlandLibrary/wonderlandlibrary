/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.stats.StatList
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Timer
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.StepConfirmEvent;
import net.ccbluex.liquidbounce.event.StepEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Step", description="Allows you to step up blocks.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\"\u001a\u00020\bH\u0002J\b\u0010#\u001a\u00020$H\u0002J\b\u0010%\u001a\u00020$H\u0016J\u0010\u0010&\u001a\u00020$2\u0006\u0010'\u001a\u00020(H\u0007J\u0010\u0010)\u001a\u00020$2\u0006\u0010'\u001a\u00020*H\u0007J\u0010\u0010+\u001a\u00020$2\u0006\u0010'\u001a\u00020,H\u0007J\u0010\u0010-\u001a\u00020$2\u0006\u0010'\u001a\u00020.H\u0007J\u0010\u0010/\u001a\u00020$2\u0006\u0010'\u001a\u000200H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u0016\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\u00198VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00061"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/Step;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "heightValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "isAACStep", "", "isStep", "jumpHeightValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "ncp1Values", "", "", "[Ljava/lang/Double;", "ncp2Values", "ncpNextStep", "", "spartanSwitch", "stepX", "stepY", "stepZ", "tag", "", "getTag", "()Ljava/lang/String;", "ticks", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "useTimer", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "usedTimer", "couldStep", "fakeJump", "", "onDisable", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onStep", "Lnet/ccbluex/liquidbounce/event/StepEvent;", "onStepConfirm", "Lnet/ccbluex/liquidbounce/event/StepConfirmEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Step
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Jump", "NewNCP", "NCPPacket", "NCP", "MotionNCP", "OldNCP", "Blocksmc", "Verus", "Vulcan", "Matrix", "AAC", "LAAC", "AAC3.3.4", "Spartan", "Rewinside", "1.5Twillight"}, "Vanilla");
    private final FloatValue heightValue = new FloatValue("Height", 1.5f, 0.6f, 10.0f);
    private final FloatValue jumpHeightValue = new FloatValue("JumpHeight", 0.42f, 0.37f, 0.42f);
    private final IntegerValue delayValue = new IntegerValue("Delay", 150, 0, 500, "ms");
    private final BoolValue useTimer = new BoolValue("UseTimer", true);
    private boolean isStep;
    private double stepX;
    private double stepY;
    private double stepZ;
    private int ncpNextStep;
    private boolean spartanSwitch;
    private boolean isAACStep;
    private int ticks;
    private final MSTimer timer = new MSTimer();
    private boolean usedTimer;
    private final Double[] ncp1Values = new Double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.919};
    private final Double[] ncp2Values = new Double[]{0.42, 0.7532, 1.01, 1.093, 1.015};

    @Override
    public void onDisable() {
        if (Step.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        Step.access$getMc$p$s1046033730().field_71439_g.field_70138_W = 0.5f;
        Step.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
        Step.access$getMc$p$s1046033730().field_71439_g.field_71102_ce = 0.02f;
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.usedTimer) {
            Step.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
            this.usedTimer = false;
        }
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals(mode, "jump", true) && Step.access$getMc$p$s1046033730().field_71439_g.field_70123_F && Step.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            KeyBinding keyBinding = Step.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
            Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
            if (!keyBinding.func_151470_d()) {
                this.fakeJump();
                Step.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.jumpHeightValue.get()).floatValue();
                return;
            }
        }
        if (StringsKt.equals(mode, "laac", true)) {
            if (Step.access$getMc$p$s1046033730().field_71439_g.field_70123_F) {
                EntityPlayerSP entityPlayerSP = Step.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (!entityPlayerSP.func_70617_f_()) {
                    EntityPlayerSP entityPlayerSP2 = Step.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                    if (!entityPlayerSP2.func_70090_H()) {
                        EntityPlayerSP entityPlayerSP3 = Step.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                        if (!entityPlayerSP3.func_180799_ab() && !Step.access$getMc$p$s1046033730().field_71439_g.field_70134_J) {
                            if (Step.access$getMc$p$s1046033730().field_71439_g.field_70122_E && this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                                this.isStep = true;
                                this.fakeJump();
                                Step.access$getMc$p$s1046033730().field_71439_g.field_70181_x += 0.620000001490116;
                                float f = Step.access$getMc$p$s1046033730().field_71439_g.field_70177_z * ((float)Math.PI / 180);
                                Step.access$getMc$p$s1046033730().field_71439_g.field_70159_w -= (double)MathHelper.func_76126_a((float)f) * 0.2;
                                Step.access$getMc$p$s1046033730().field_71439_g.field_70179_y += (double)MathHelper.func_76134_b((float)f) * 0.2;
                                this.timer.reset();
                            }
                            Step.access$getMc$p$s1046033730().field_71439_g.field_70122_E = true;
                            return;
                        }
                    }
                }
            }
            this.isStep = false;
            return;
        }
        if (StringsKt.equals(mode, "aac3.3.4", true)) {
            if (Step.access$getMc$p$s1046033730().field_71439_g.field_70123_F && MovementUtils.isMoving()) {
                if (Step.access$getMc$p$s1046033730().field_71439_g.field_70122_E && this.couldStep()) {
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 1.26;
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 1.26;
                    Step.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
                    this.isAACStep = true;
                }
                if (!this.isAACStep) return;
                Step.access$getMc$p$s1046033730().field_71439_g.field_70181_x -= 0.015;
                EntityPlayerSP entityPlayerSP = Step.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (entityPlayerSP.func_71039_bw()) return;
                if (Step.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78902_a != 0.0f) return;
                Step.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.3f;
                return;
            }
            this.isAACStep = false;
            return;
        }
        if (!StringsKt.equals(mode, "1.5Twillight", true)) return;
        if (MovementUtils.isMoving() && Step.access$getMc$p$s1046033730().field_71439_g.field_70123_F) {
            int n = this.ticks;
            this.ticks = n + 1;
            if (this.ticks == 1) {
                Step.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.4399;
            }
            if (this.ticks == 12) {
                Step.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.4399;
            }
            if (this.ticks < 40) return;
            this.ticks = 0;
            return;
        }
        if (!Step.access$getMc$p$s1046033730().field_71439_g.field_70122_E) return;
        this.ticks = 0;
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals(mode, "motionncp", true) && Step.access$getMc$p$s1046033730().field_71439_g.field_70123_F) {
            KeyBinding keyBinding = Step.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
            Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
            if (!keyBinding.func_151470_d()) {
                if (Step.access$getMc$p$s1046033730().field_71439_g.field_70122_E && this.couldStep()) {
                    this.fakeJump();
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                    event.setY(0.41999998688698);
                    this.ncpNextStep = 1;
                } else if (this.ncpNextStep == 1) {
                    event.setY(0.33319999363422);
                    this.ncpNextStep = 2;
                } else if (this.ncpNextStep == 2) {
                    double yaw = MovementUtils.getDirection();
                    event.setY(0.24813599859094704);
                    MoveEvent moveEvent = event;
                    boolean bl = false;
                    double d = Math.sin(yaw);
                    moveEvent.setX(-d * 0.7);
                    moveEvent = event;
                    bl = false;
                    d = Math.cos(yaw);
                    moveEvent.setZ(d * 0.7);
                    this.ncpNextStep = 0;
                }
            }
        }
    }

    @EventTarget
    public final void onStep(@NotNull StepEvent event) {
        float height;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (Step.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        String mode = (String)this.modeValue.get();
        if (!Step.access$getMc$p$s1046033730().field_71439_g.field_70122_E || !this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue()) || StringsKt.equals(mode, "Jump", true) || StringsKt.equals(mode, "MotionNCP", true) || StringsKt.equals(mode, "LAAC", true) || StringsKt.equals(mode, "AAC3.3.4", true) || StringsKt.equals(mode, "AACv4", true) || StringsKt.equals(mode, "1.5Twillight", true)) {
            Step.access$getMc$p$s1046033730().field_71439_g.field_70138_W = 0.5f;
            event.setStepHeight(0.5f);
            return;
        }
        Step.access$getMc$p$s1046033730().field_71439_g.field_70138_W = height = ((Number)this.heightValue.get()).floatValue();
        this.usedTimer = true;
        event.setStepHeight(height);
        if (event.getStepHeight() > 0.5f) {
            this.isStep = true;
            this.stepX = Step.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
            this.stepY = Step.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
            this.stepZ = Step.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget(ignoreCondition=true)
    public final void onStepConfirm(@NotNull StepConfirmEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (Step.access$getMc$p$s1046033730().field_71439_g == null || !this.isStep) {
            return;
        }
        EntityPlayerSP entityPlayerSP = Step.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (entityPlayerSP.func_174813_aQ().field_72338_b - this.stepY > 0.8) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(Step.class);
            if (module == null) {
                Intrinsics.throwNpe();
            }
            if (module.getState() && ((Boolean)this.useTimer.get()).booleanValue() && Step.access$getMc$p$s1046033730().field_71439_g.field_70122_E && !((String)this.modeValue.get()).equals("Matrix")) {
                Step.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.52f;
            }
            Module module2 = LiquidBounce.INSTANCE.getModuleManager().get(Step.class);
            if (module2 == null) {
                Intrinsics.throwNpe();
            }
            if (module2.getState() && ((Boolean)this.useTimer.get()).booleanValue() && Step.access$getMc$p$s1046033730().field_71439_g.field_70122_E && ((String)this.modeValue.get()).equals("Matrix")) {
                Step.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.12f;
            }
        } else {
            Step.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
        }
        EntityPlayerSP entityPlayerSP2 = Step.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        if (entityPlayerSP2.func_174813_aQ().field_72338_b - this.stepY > 0.8) {
            String mode = (String)this.modeValue.get();
            if (StringsKt.equals(mode, "NCPPacket", true)) {
                EntityPlayerSP entityPlayerSP3 = Step.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                double rHeight = entityPlayerSP3.func_174813_aQ().field_72338_b - this.stepY;
                if (rHeight > 2.019) {
                    Double[] $this$forEach$iv = this.ncp1Values;
                    boolean $i$f$forEach = false;
                    for (Double element$iv : $this$forEach$iv) {
                        double it = ((Number)element$iv).doubleValue();
                        boolean bl = false;
                        Minecraft minecraft = Step.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + it, this.stepZ, false));
                    }
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                } else if (rHeight > 1.869) {
                    int $this$forEach$iv = 0;
                    int $i$f$forEach = 7;
                    while ($this$forEach$iv <= $i$f$forEach) {
                        void i;
                        Minecraft minecraft = Step.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + this.ncp1Values[i], this.stepZ, false));
                        ++i;
                    }
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                } else if (rHeight > 1.5) {
                    int $i$f$forEach = 6;
                    for (int i = 0; i <= $i$f$forEach; ++i) {
                        Minecraft minecraft = Step.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + this.ncp1Values[i], this.stepZ, false));
                    }
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                } else if (rHeight > 1.015) {
                    Double[] $this$forEach$iv = this.ncp2Values;
                    boolean $i$f$forEach = false;
                    for (Double element$iv : $this$forEach$iv) {
                        double it = ((Number)element$iv).doubleValue();
                        boolean bl = false;
                        Minecraft minecraft = Step.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + it, this.stepZ, false));
                    }
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                } else if (rHeight > 0.875) {
                    Minecraft minecraft = Step.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                    Minecraft minecraft2 = Step.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                    minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                } else if (rHeight > 0.6) {
                    Minecraft minecraft = Step.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.39, this.stepZ, false));
                    Minecraft minecraft3 = Step.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                    minecraft3.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.6938, this.stepZ, false));
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    Step.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                }
            } else if (StringsKt.equals(mode, "Blocksmc", true)) {
                this.fakeJump();
                EntityPlayerSP entityPlayerSP4 = Step.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
                BlockPos pos = entityPlayerSP4.func_180425_c().func_177963_a(0.0, -1.5, 0.0);
                Minecraft minecraft = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(pos, 1, new ItemStack(Blocks.field_150348_b.func_180665_b((World)Step.access$getMc$p$s1046033730().field_71441_e, pos)), 0.0f, 0.5f + (float)Math.random() * (float)0.44, 0.0f));
                Minecraft minecraft4 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft4, "mc");
                minecraft4.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                Minecraft minecraft5 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft5, "mc");
                minecraft5.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                Minecraft minecraft6 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft6, "mc");
                minecraft6.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.0, this.stepZ, true));
                this.timer.reset();
            } else if (StringsKt.equals(mode, "NCP", true) || StringsKt.equals(mode, "AAC", true)) {
                this.fakeJump();
                Minecraft minecraft = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                Minecraft minecraft7 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft7, "mc");
                minecraft7.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                this.timer.reset();
            } else if (StringsKt.equals(mode, "Spartan", true)) {
                this.fakeJump();
                if (this.spartanSwitch) {
                    Minecraft minecraft = Step.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                    Minecraft minecraft8 = Step.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft8, "mc");
                    minecraft8.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                    Minecraft minecraft9 = Step.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft9, "mc");
                    minecraft9.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                } else {
                    Minecraft minecraft = Step.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.6, this.stepZ, false));
                }
                this.spartanSwitch = !this.spartanSwitch;
                this.timer.reset();
            } else if (StringsKt.equals(mode, "Vulcan", true)) {
                EntityPlayerSP entityPlayerSP5 = Step.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP5, "mc.thePlayer");
                double superHeight = entityPlayerSP5.func_174813_aQ().field_72338_b - this.stepY;
                this.fakeJump();
                if (superHeight > 2.0) {
                    Double[] stpPacket;
                    Double[] $this$forEach$iv = stpPacket = new Double[]{0.5, 1.0, 1.5, 2.0};
                    boolean $i$f$forEach = false;
                    for (Double element$iv : $this$forEach$iv) {
                        double it = ((Number)element$iv).doubleValue();
                        boolean bl = false;
                        Step.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + it, this.stepZ, true));
                    }
                } else if (superHeight <= 2.0 && superHeight > 1.5) {
                    Double[] stpPacket;
                    Double[] $this$forEach$iv = stpPacket = new Double[]{0.5, 1.0, 1.5};
                    boolean $i$f$forEach = false;
                    for (Double element$iv : $this$forEach$iv) {
                        double it = ((Number)element$iv).doubleValue();
                        boolean bl = false;
                        Step.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + it, this.stepZ, true));
                    }
                } else if (superHeight <= 1.5 && superHeight > 1.0) {
                    Double[] stpPacket;
                    Double[] $this$forEach$iv = stpPacket = new Double[]{0.5, 1.0};
                    boolean $i$f$forEach = false;
                    for (Double element$iv : $this$forEach$iv) {
                        double it = ((Number)element$iv).doubleValue();
                        boolean bl = false;
                        Step.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + it, this.stepZ, true));
                    }
                } else if (superHeight <= 1.0 && superHeight > 0.6) {
                    Double[] stpPacket;
                    Double[] $this$forEach$iv = stpPacket = new Double[]{0.5};
                    boolean $i$f$forEach = false;
                    for (Double element$iv : $this$forEach$iv) {
                        double it = ((Number)element$iv).doubleValue();
                        boolean bl = false;
                        Step.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + it, this.stepZ, true));
                    }
                }
                this.timer.reset();
            } else if (StringsKt.equals(mode, "Verus", true)) {
                EntityPlayerSP entityPlayerSP6 = Step.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP6, "mc.thePlayer");
                double superHeight = entityPlayerSP6.func_174813_aQ().field_72338_b - this.stepY;
                double d = superHeight * 2.0;
                float f = 1.0f;
                Timer timer = Step.access$getMc$p$s1046033730().field_71428_T;
                boolean bl = false;
                double d2 = Math.ceil(d);
                timer.field_74278_d = f / (float)d2;
                double superHighest = 0.0;
                this.fakeJump();
                double d3 = superHeight * 2.0;
                int n = 0;
                int n2 = (int)(Math.ceil(d3) - 1.0);
                bl = false;
                n = 0;
                n = 0;
                int n3 = n2;
                while (n < n3) {
                    int it = n++;
                    boolean bl2 = false;
                    Step.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + (superHighest += 0.5), this.stepZ, true));
                }
                this.timer.reset();
            } else if (StringsKt.equals(mode, "NewNCP", true)) {
                this.fakeJump();
                Minecraft minecraft = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                Minecraft minecraft10 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft10, "mc");
                minecraft10.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                Minecraft minecraft11 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft11, "mc");
                minecraft11.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.0, this.stepZ, true));
            } else if (StringsKt.equals(mode, "Matrix", true)) {
                this.fakeJump();
                Minecraft minecraft = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                Minecraft minecraft12 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft12, "mc");
                minecraft12.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                Minecraft minecraft13 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft13, "mc");
                minecraft13.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
                this.timer.reset();
            } else if (StringsKt.equals(mode, "Rewinside", true)) {
                this.fakeJump();
                Minecraft minecraft = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.41999998688698, this.stepZ, false));
                Minecraft minecraft14 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft14, "mc");
                minecraft14.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 0.7531999805212, this.stepZ, false));
                Minecraft minecraft15 = Step.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft15, "mc");
                minecraft15.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.stepX, this.stepY + 1.001335979112147, this.stepZ, false));
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
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer && this.isStep && StringsKt.equals((String)this.modeValue.get(), "OldNCP", true)) {
            ((C03PacketPlayer)packet).field_149477_b += 0.07;
            this.isStep = false;
        }
    }

    private final void fakeJump() {
        Step.access$getMc$p$s1046033730().field_71439_g.field_70160_al = true;
        Step.access$getMc$p$s1046033730().field_71439_g.func_71029_a(StatList.field_75953_u);
    }

    private final boolean couldStep() {
        double yaw = MovementUtils.getDirection();
        boolean bl = false;
        double x = -Math.sin(yaw) * 0.4;
        boolean bl2 = false;
        double z = Math.cos(yaw) * 0.4;
        WorldClient worldClient = Step.access$getMc$p$s1046033730().field_71441_e;
        EntityPlayerSP entityPlayerSP = Step.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        return worldClient.func_147461_a(entityPlayerSP.func_174813_aQ().func_72317_d(x, 1.001335979112147, z)).isEmpty();
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

