/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.AxisAlignedBB
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.movement;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.movement.Fly;
import net.dev.important.modules.module.modules.movement.Speed;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.Rotation;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Info(name="TargetStrafe", description="Strafe around your target.", category=Category.MOVEMENT, cnName="\u76ee\u6807\u8f6c\u5708")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010<\u001a\u00020=H\u0002J\b\u0010>\u001a\u00020\u0011H\u0002J\u0018\u0010?\u001a\u00020\u00112\u0006\u0010@\u001a\u00020\u00192\u0006\u0010A\u001a\u00020\u0019H\u0002J\u0010\u0010B\u001a\u00020=2\u0006\u0010C\u001a\u00020DH\u0007J\b\u0010E\u001a\u00020=H\u0016J\u000e\u0010F\u001a\u00020=2\u0006\u0010C\u001a\u00020GJ\u000e\u0010H\u001a\u00020=2\u0006\u0010C\u001a\u00020DJ\u0010\u0010I\u001a\u00020=2\u0006\u0010C\u001a\u00020JH\u0007J.\u0010K\u001a\u00020=2\u0006\u0010L\u001a\u00020D2\u0006\u0010M\u001a\u00020\u00042\u0006\u0010N\u001a\u00020\n2\u0006\u0010O\u001a\u00020\n2\u0006\u0010P\u001a\u00020\u0004R\u0011\u0010\u0003\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0010\u001a\u00020\u00118F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0014\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\fR\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\u00020\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001a\u0010\u001e\u001a\u00020\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001b\"\u0004\b \u0010\u001dR\u000e\u0010!\u001a\u00020\"X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010$\u001a\u00020\u00118F\u00a2\u0006\u0006\u001a\u0004\b%\u0010\u0013R\u000e\u0010&\u001a\u00020'X\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010(\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0006\"\u0004\b*\u0010+R\u000e\u0010,\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020/X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u00100\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u00102R\u000e\u00103\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020/X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020/X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u000209X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020/X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006Q"}, d2={"Lnet/dev/important/modules/module/modules/movement/TargetStrafe;", "Lnet/dev/important/modules/module/Module;", "()V", "Enemydistance", "", "getEnemydistance", "()D", "accuracyValue", "Lnet/dev/important/value/IntegerValue;", "algorithm", "", "getAlgorithm", "()F", "blueValue", "brightnessValue", "Lnet/dev/important/value/FloatValue;", "canStrafe", "", "getCanStrafe", "()Z", "cansize", "getCansize", "colorType", "Lnet/dev/important/value/ListValue;", "consts", "", "getConsts", "()I", "setConsts", "(I)V", "direction", "getDirection", "setDirection", "fly", "Lnet/dev/important/modules/module/modules/movement/Fly;", "greenValue", "keyMode", "getKeyMode", "killAura", "Lnet/dev/important/modules/module/modules/combat/KillAura;", "lastDist", "getLastDist", "setLastDist", "(D)V", "mixerSecondsValue", "modeValue", "outLine", "Lnet/dev/important/value/BoolValue;", "radius", "getRadius", "()Lnet/dev/important/value/FloatValue;", "radiusMode", "redValue", "render", "safewalk", "saturationValue", "speed", "Lnet/dev/important/modules/module/modules/movement/Speed;", "thicknessValue", "thirdPerson", "check", "", "checkVoid", "isVoid", "X", "Z", "moveStrafe", "event", "Lnet/dev/important/event/MoveEvent;", "onInitialize", "onMotion", "Lnet/dev/important/event/MotionEvent;", "onMove", "onRender3D", "Lnet/dev/important/event/Render3DEvent;", "setSpeed", "moveEvent", "moveSpeed", "pseudoYaw", "pseudoStrafe", "pseudoForward", "LiquidBounce"})
public final class TargetStrafe
extends Module {
    private int direction = 1;
    @NotNull
    private final FloatValue radius = new FloatValue("Radius", 2.0f, 0.1f, 4.0f);
    @NotNull
    private final BoolValue render = new BoolValue("Render", true);
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final ListValue radiusMode;
    @NotNull
    private final BoolValue safewalk;
    @NotNull
    private final BoolValue thirdPerson;
    @NotNull
    private final ListValue colorType;
    @NotNull
    private final IntegerValue redValue;
    @NotNull
    private final IntegerValue greenValue;
    @NotNull
    private final IntegerValue blueValue;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue mixerSecondsValue;
    @NotNull
    private final IntegerValue accuracyValue;
    @NotNull
    private final FloatValue thicknessValue;
    @NotNull
    private final BoolValue outLine;
    private KillAura killAura;
    private Speed speed;
    private Fly fly;
    private int consts;
    private double lastDist;

    public TargetStrafe() {
        String[] stringArray = new String[]{"Jump", "None"};
        this.modeValue = new ListValue("KeyMode", stringArray, "None");
        stringArray = new String[]{"TrueRadius", "Simple"};
        this.radiusMode = new ListValue("RadiusMode", stringArray, "Simple");
        this.safewalk = new BoolValue("SafeWalk", true);
        this.thirdPerson = new BoolValue("ThirdPerson", true);
        stringArray = new String[]{"Custom", "Rainbow", "Rainbow2", "Sky", "Fade", "Mixer"};
        this.colorType = new ListValue("Color", stringArray, "Custom");
        this.redValue = new IntegerValue("Red", 255, 0, 255);
        this.greenValue = new IntegerValue("Green", 255, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.saturationValue = new FloatValue("Saturation", 0.7f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.mixerSecondsValue = new IntegerValue("Mixer-Seconds", 2, 1, 10);
        this.accuracyValue = new IntegerValue("Accuracy", 0, 0, 59);
        this.thicknessValue = new FloatValue("Thickness", 1.0f, 0.1f, 5.0f);
        this.outLine = new BoolValue("Outline", true);
    }

    public final int getDirection() {
        return this.direction;
    }

    public final void setDirection(int n) {
        this.direction = n;
    }

    @NotNull
    public final FloatValue getRadius() {
        return this.radius;
    }

    public final int getConsts() {
        return this.consts;
    }

    public final void setConsts(int n) {
        this.consts = n;
    }

    public final double getLastDist() {
        return this.lastDist;
    }

    public final void setLastDist(double d) {
        this.lastDist = d;
    }

    @Override
    public void onInitialize() {
        Module module2 = Client.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.KillAura");
        }
        this.killAura = (KillAura)module2;
        Module module3 = Client.INSTANCE.getModuleManager().getModule(Speed.class);
        if (module3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.movement.Speed");
        }
        this.speed = (Speed)module3;
        Module module4 = Client.INSTANCE.getModuleManager().getModule(Fly.class);
        if (module4 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.movement.Fly");
        }
        this.fly = (Fly)module4;
    }

    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.thirdPerson.get()).booleanValue()) {
            MinecraftInstance.mc.field_71474_y.field_74320_O = this.getCanStrafe() ? 2 : 0;
        }
    }

    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        double xDist = event.getX();
        double zDist = event.getZ();
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @EventTarget
    public final void moveStrafe(@NotNull MoveEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.onMove(event);
        if (!this.isVoid(0, 0) && this.getCanStrafe()) {
            KillAura killAura = this.killAura;
            if (killAura == null) {
                Intrinsics.throwUninitializedPropertyAccessException("killAura");
                killAura = null;
            }
            Rotation strafe = RotationUtils.getRotations((Entity)killAura.getTarget());
            this.setSpeed(event, this.lastDist, strafe.getYaw(), ((Number)this.radius.get()).floatValue(), 1.0);
        }
    }

    public final boolean getKeyMode() {
        String string = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        String string2 = string;
        return Intrinsics.areEqual(string2, "jump") ? MinecraftInstance.mc.field_71474_y.field_74314_A.func_151470_d() : (Intrinsics.areEqual(string2, "none") ? !(MinecraftInstance.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f) || !(MinecraftInstance.mc.field_71439_g.field_71158_b.field_78900_b == 0.0f) : false);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean getCanStrafe() {
        KillAura killAura;
        KillAura killAura2 = this.killAura;
        if (killAura2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("killAura");
            killAura2 = null;
        }
        if (!killAura2.getState()) return false;
        Speed speed = this.speed;
        if (speed == null) {
            Intrinsics.throwUninitializedPropertyAccessException("speed");
            speed = null;
        }
        if (!speed.getState()) {
            Fly fly = this.fly;
            if (fly == null) {
                Intrinsics.throwUninitializedPropertyAccessException("fly");
                fly = null;
            }
            if (!fly.getState()) return false;
        }
        if ((killAura = this.killAura) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("killAura");
            killAura = null;
        }
        if (killAura.getTarget() == null) return false;
        if (MinecraftInstance.mc.field_71439_g.func_70093_af()) return false;
        if (!this.getKeyMode()) return false;
        return true;
    }

    public final float getCansize() {
        float f;
        String string = ((String)this.radiusMode.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        if (Intrinsics.areEqual(string, "simple")) {
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            KillAura killAura = this.killAura;
            if (killAura == null) {
                Intrinsics.throwUninitializedPropertyAccessException("killAura");
                killAura = null;
            }
            EntityLivingBase entityLivingBase = killAura.getTarget();
            Intrinsics.checkNotNull(entityLivingBase);
            double d = entityLivingBase.field_70165_t;
            double d2 = MinecraftInstance.mc.field_71439_g.field_70163_u;
            KillAura killAura2 = this.killAura;
            if (killAura2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("killAura");
                killAura2 = null;
            }
            EntityLivingBase entityLivingBase2 = killAura2.getTarget();
            Intrinsics.checkNotNull(entityLivingBase2);
            f = 45.0f / (float)entityPlayerSP.func_70011_f(d, d2, entityLivingBase2.field_70161_v);
        } else {
            f = 45.0f;
        }
        return f;
    }

    public final double getEnemydistance() {
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        KillAura killAura = this.killAura;
        if (killAura == null) {
            Intrinsics.throwUninitializedPropertyAccessException("killAura");
            killAura = null;
        }
        EntityLivingBase entityLivingBase = killAura.getTarget();
        Intrinsics.checkNotNull(entityLivingBase);
        double d = entityLivingBase.field_70165_t;
        double d2 = MinecraftInstance.mc.field_71439_g.field_70163_u;
        KillAura killAura2 = this.killAura;
        if (killAura2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("killAura");
            killAura2 = null;
        }
        EntityLivingBase entityLivingBase2 = killAura2.getTarget();
        Intrinsics.checkNotNull(entityLivingBase2);
        return entityPlayerSP.func_70011_f(d, d2, entityLivingBase2.field_70161_v);
    }

    public final float getAlgorithm() {
        return (float)Math.max(this.getEnemydistance() - ((Number)this.radius.get()).doubleValue(), this.getEnemydistance() - (this.getEnemydistance() - (double)(((Number)this.radius.get()).floatValue() / (((Number)this.radius.get()).floatValue() * (float)2))));
    }

    public final void setSpeed(@NotNull MoveEvent moveEvent, double moveSpeed, float pseudoYaw, float pseudoStrafe, double pseudoForward) {
        Intrinsics.checkNotNullParameter(moveEvent, "moveEvent");
        float yaw = pseudoYaw;
        double forward = pseudoForward;
        float strafe = pseudoStrafe;
        float strafe2 = 0.0f;
        this.check();
        String string = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        if (string.equals("jump")) {
            strafe = (float)((double)pseudoStrafe * 0.98 * (double)this.consts);
        } else {
            string = ((String)this.modeValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            if (string.equals("none")) {
                strafe = this.consts;
            }
        }
        if (!(forward == 0.0)) {
            if ((double)strafe > 0.0) {
                string = ((String)this.radiusMode.get()).toLowerCase();
                Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
                if (Intrinsics.areEqual(string, "trueradius")) {
                    yaw += forward > 0.0 ? -this.getCansize() : this.getCansize();
                }
                strafe2 += forward > 0.0 ? (float)-45 / this.getAlgorithm() : (float)45 / this.getAlgorithm();
            } else if ((double)strafe < 0.0) {
                string = ((String)this.radiusMode.get()).toLowerCase();
                Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
                if (Intrinsics.areEqual(string, "trueradius")) {
                    yaw += forward > 0.0 ? this.getCansize() : -this.getCansize();
                }
                strafe2 += forward > 0.0 ? (float)45 / this.getAlgorithm() : (float)-45 / this.getAlgorithm();
            }
            strafe = 0.0f;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if ((double)strafe > 0.0) {
            strafe = 1.0f;
        } else if ((double)strafe < 0.0) {
            strafe = -1.0f;
        }
        double mx = Math.cos(Math.toRadians((double)yaw + 90.0 + (double)strafe2));
        double mz = Math.sin(Math.toRadians((double)yaw + 90.0 + (double)strafe2));
        moveEvent.setX(forward * moveSpeed * mx + (double)strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - (double)strafe * moveSpeed * mx);
    }

    private final void check() {
        if (MinecraftInstance.mc.field_71439_g.field_70123_F || this.checkVoid()) {
            this.consts = this.consts < 2 ? ++this.consts : -1;
        }
        switch (this.consts) {
            case 0: {
                this.consts = 1;
                break;
            }
            case 2: {
                this.consts = -1;
            }
        }
    }

    private final boolean checkVoid() {
        int n = -1;
        while (n < 1) {
            int x = n++;
            int n2 = -1;
            while (n2 < 1) {
                int z;
                if (!this.isVoid(x, z = n2++)) continue;
                return true;
            }
        }
        return false;
    }

    private final boolean isVoid(int X, int Z) {
        Module module2 = Client.INSTANCE.getModuleManager().getModule(Fly.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.movement.Fly");
        }
        Fly fly = (Fly)module2;
        if (fly.getState()) {
            return false;
        }
        if (MinecraftInstance.mc.field_71439_g.field_70163_u < 0.0) {
            return true;
        }
        for (int off = 0; off < (int)MinecraftInstance.mc.field_71439_g.field_70163_u + 2; off += 2) {
            AxisAlignedBB axisAlignedBB = MinecraftInstance.mc.field_71439_g.func_174813_aQ().func_72317_d((double)X, (double)(-off), (double)Z);
            Intrinsics.checkNotNullExpressionValue(axisAlignedBB, "mc.thePlayer.entityBound\u2026toDouble(), Z.toDouble())");
            AxisAlignedBB bb = axisAlignedBB;
            WorldClient worldClient = MinecraftInstance.mc.field_71441_e;
            Intrinsics.checkNotNull(worldClient);
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            if (entityPlayerSP == null) {
                throw new NullPointerException("null cannot be cast to non-null type net.minecraft.entity.Entity");
            }
            if (worldClient.func_72945_a((Entity)entityPlayerSP, bb).isEmpty()) {
                continue;
            }
            return false;
        }
        return true;
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        block27: {
            block28: {
                int i;
                Intrinsics.checkNotNullParameter(event, "event");
                KillAura killAura = this.killAura;
                if (killAura == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("killAura");
                    killAura = null;
                }
                EntityLivingBase target = killAura.getTarget();
                if (!this.getCanStrafe() || !((Boolean)this.render.get()).booleanValue()) break block27;
                if (target == null) {
                    return;
                }
                GL11.glPushMatrix();
                GL11.glTranslated((double)(target.field_70142_S + (target.field_70165_t - target.field_70142_S) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - MinecraftInstance.mc.func_175598_ae().field_78725_b), (double)(target.field_70137_T + (target.field_70163_u - target.field_70137_T) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - MinecraftInstance.mc.func_175598_ae().field_78726_c), (double)(target.field_70136_U + (target.field_70161_v - target.field_70136_U) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - MinecraftInstance.mc.func_175598_ae().field_78723_d));
                GL11.glEnable((int)3042);
                GL11.glEnable((int)2848);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2929);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                if (((Boolean)this.outLine.get()).booleanValue()) {
                    GL11.glLineWidth((float)(((Number)this.thicknessValue.get()).floatValue() + 1.25f));
                    GL11.glColor3f((float)0.0f, (float)0.0f, (float)0.0f);
                    GL11.glBegin((int)2);
                    int n = 60 - ((Number)this.accuracyValue.get()).intValue();
                    if (n <= 0) {
                        throw new IllegalArgumentException("Step must be positive, was: " + n + '.');
                    }
                    int n2 = 0;
                    int n3 = ProgressionUtilKt.getProgressionLastElement(0, 360, n);
                    if (n2 <= n3) {
                        int i2;
                        do {
                            i2 = n2;
                            n2 += n;
                            GL11.glVertex2f((float)((float)Math.cos((double)i2 * Math.PI / 180.0) * ((Number)this.radius.get()).floatValue()), (float)((float)Math.sin((double)i2 * Math.PI / 180.0) * ((Number)this.radius.get()).floatValue()));
                        } while (i2 != n3);
                    }
                    GL11.glEnd();
                }
                Color rainbow2 = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                Color sky = RenderUtils.skyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                Color mixer = ColorMixer.getMixedColor(0, ((Number)this.mixerSecondsValue.get()).intValue());
                Color fade = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), 0, 100);
                GL11.glLineWidth((float)((Number)this.thicknessValue.get()).floatValue());
                GL11.glBegin((int)2);
                int n = 60 - ((Number)this.accuracyValue.get()).intValue();
                if (n <= 0) {
                    throw new IllegalArgumentException("Step must be positive, was: " + n + '.');
                }
                int n4 = 0;
                int n5 = ProgressionUtilKt.getProgressionLastElement(0, 360, n);
                if (n4 > n5) break block28;
                do {
                    i = n4;
                    n4 += n;
                    switch ((String)this.colorType.get()) {
                        case "Custom": {
                            GL11.glColor3f((float)(((Number)this.redValue.get()).floatValue() / 255.0f), (float)(((Number)this.greenValue.get()).floatValue() / 255.0f), (float)(((Number)this.blueValue.get()).floatValue() / 255.0f));
                            break;
                        }
                        case "Rainbow": {
                            Color rainbow = new Color(Color.HSBtoRGB((float)((double)MinecraftInstance.mc.field_71439_g.field_70173_aa / 70.0 + Math.sin((double)i / 50.0 * 1.75)) % 1.0f, 0.7f, 1.0f));
                            GL11.glColor3f((float)((float)rainbow.getRed() / 255.0f), (float)((float)rainbow.getGreen() / 255.0f), (float)((float)rainbow.getBlue() / 255.0f));
                            break;
                        }
                        case "Rainbow2": {
                            Color color = rainbow2;
                            Intrinsics.checkNotNull(color);
                            GL11.glColor3f((float)((float)color.getRed() / 255.0f), (float)((float)rainbow2.getGreen() / 255.0f), (float)((float)rainbow2.getBlue() / 255.0f));
                            break;
                        }
                        case "Sky": {
                            GL11.glColor3f((float)((float)sky.getRed() / 255.0f), (float)((float)sky.getGreen() / 255.0f), (float)((float)sky.getBlue() / 255.0f));
                            break;
                        }
                        case "Mixer": {
                            GL11.glColor3f((float)((float)mixer.getRed() / 255.0f), (float)((float)mixer.getGreen() / 255.0f), (float)((float)mixer.getBlue() / 255.0f));
                            break;
                        }
                        default: {
                            GL11.glColor3f((float)((float)fade.getRed() / 255.0f), (float)((float)fade.getGreen() / 255.0f), (float)((float)fade.getBlue() / 255.0f));
                        }
                    }
                    GL11.glVertex2f((float)((float)Math.cos((double)i * Math.PI / 180.0) * ((Number)this.radius.get()).floatValue()), (float)((float)Math.sin((double)i * Math.PI / 180.0) * ((Number)this.radius.get()).floatValue()));
                } while (i != n5);
            }
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
            GlStateManager.func_179117_G();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }
}

