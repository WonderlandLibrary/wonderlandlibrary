/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.MathHelper
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.report.liquidware.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils2233;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.entity.EntityValidator;
import net.ccbluex.liquidbounce.utils.entity.impl.VoidCheck;
import net.ccbluex.liquidbounce.utils.entity.impl.WallCheck;
import net.ccbluex.liquidbounce.utils.render.ESPUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="TargetStrafe", description="Strafe around your target.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u00101\u001a\u00020\fJ\b\u00102\u001a\u00020\fH\u0002J\u0011\u00103\u001a\b\u0012\u0004\u0012\u00020504\u00a2\u0006\u0002\u00106J\u0006\u00107\u001a\u000208J\u0018\u00109\u001a\u00020\f2\u0006\u0010:\u001a\u00020\t2\u0006\u0010;\u001a\u00020\tH\u0002J\b\u0010<\u001a\u00020=H\u0016J\u0010\u0010>\u001a\u00020=2\u0006\u0010?\u001a\u00020@H\u0007J\u0010\u0010A\u001a\u00020=2\u0006\u0010?\u001a\u00020BH\u0007J\u0010\u0010C\u001a\u00020=2\u0006\u0010?\u001a\u00020DH\u0007J\u001a\u0010E\u001a\u00020=2\b\u0010?\u001a\u0004\u0018\u00010B2\u0006\u0010F\u001a\u000208H\u0007J \u0010E\u001a\u00020=2\b\u0010?\u001a\u0004\u0018\u00010B2\u0006\u0010G\u001a\u0002082\u0006\u0010F\u001a\u000208J\b\u0010H\u001a\u00020=H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000e\"\u0004\b\u0013\u0010\u0010R\u0011\u0010\u0014\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0007R\u0011\u0010\u0016\u001a\u00020\f8F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u000eR\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\tX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\tX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010*\u001a\u0004\u0018\u00010+8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010-R\u000e\u0010.\u001a\u00020/X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006I"}, d2={"Lme/report/liquidware/modules/movement/TargetStrafe;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "alwaysRender", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "behind", "getBehind", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "direction", "", "displayTag", "hasChangedThirdPerson", "", "getHasChangedThirdPerson", "()Z", "setHasChangedThirdPerson", "(Z)V", "hasModifiedMovement", "getHasModifiedMovement", "setHasModifiedMovement", "holdSpaceValue", "getHoldSpaceValue", "keyMode", "getKeyMode", "killAura", "Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura;", "lastView", "getLastView", "()I", "setLastView", "(I)V", "markValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onlySpeedValue", "onlyflyValue", "radius", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "range", "safewalk", "slice", "space", "tag", "", "getTag", "()Ljava/lang/String;", "targetValidator", "Lnet/ccbluex/liquidbounce/utils/entity/EntityValidator;", "thirdPersonViewValue", "canStrafe", "checkVoid", "getData", "", "", "()[Ljava/lang/Float;", "getMovingDir", "", "isVoid", "X", "Z", "onEnable", "", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "strafe", "moveSpeed", "speed", "switchDirection", "KyinoClient"})
public final class TargetStrafe
extends Module {
    private final ListValue modeValue = new ListValue("KeyMode", new String[]{"Jump", "None"}, "None");
    private final FloatValue radius = new FloatValue("Range", 1.5f, 0.1f, 4.0f);
    private final BoolValue safewalk = new BoolValue("Safewalk", true);
    @NotNull
    private final BoolValue behind = new BoolValue("Behind", false);
    private final BoolValue alwaysRender = new BoolValue("Render", true);
    private final BoolValue onlyflyValue = new BoolValue("keyFly", false);
    @NotNull
    private final BoolValue holdSpaceValue = new BoolValue("HoldSpace", false);
    private final BoolValue thirdPersonViewValue = new BoolValue("ThirdPerson-View", false);
    private final BoolValue displayTag = new BoolValue("ArrayList-Tag", false);
    private final BoolValue onlySpeedValue = new BoolValue("OnlySpeed", false);
    private final EntityValidator targetValidator;
    private KillAura killAura;
    private int direction = -1;
    private final int slice;
    private final int range;
    private boolean space = (Boolean)this.holdSpaceValue.get();
    private boolean hasChangedThirdPerson = true;
    private boolean markValue;
    private int lastView;
    private boolean hasModifiedMovement;

    @NotNull
    public final BoolValue getBehind() {
        return this.behind;
    }

    @NotNull
    public final BoolValue getHoldSpaceValue() {
        return this.holdSpaceValue;
    }

    public final boolean getHasChangedThirdPerson() {
        return this.hasChangedThirdPerson;
    }

    public final void setHasChangedThirdPerson(boolean bl) {
        this.hasChangedThirdPerson = bl;
    }

    public final int getLastView() {
        return this.lastView;
    }

    public final void setLastView(int n) {
        this.lastView = n;
    }

    public final boolean getHasModifiedMovement() {
        return this.hasModifiedMovement;
    }

    public final void setHasModifiedMovement(boolean bl) {
        this.hasModifiedMovement = bl;
    }

    @Override
    public void onEnable() {
        if (this.killAura == null) {
            this.killAura = (KillAura)LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getEventState() == EventState.PRE) {
            if (TargetStrafe.access$getMc$p$s1046033730().field_71439_g.field_70123_F) {
                this.switchDirection();
            }
            KeyBinding keyBinding = TargetStrafe.access$getMc$p$s1046033730().field_71474_y.field_74370_x;
            Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindLeft");
            if (keyBinding.func_151470_d()) {
                this.direction = 1;
            }
            KeyBinding keyBinding2 = TargetStrafe.access$getMc$p$s1046033730().field_71474_y.field_74366_z;
            Intrinsics.checkExpressionValueIsNotNull(keyBinding2, "mc.gameSettings.keyBindRight");
            if (keyBinding2.func_151470_d()) {
                this.direction = -1;
            }
        }
        if (event.getEventState() == EventState.PRE && (TargetStrafe.access$getMc$p$s1046033730().field_71439_g.field_70123_F || ((Boolean)this.safewalk.get()).booleanValue() && this.checkVoid())) {
            this.direction = -this.direction;
        }
    }

    private final void switchDirection() {
        this.direction = this.direction == 1 ? -1 : 1;
    }

    public final double getMovingDir() {
        Float[] dt = this.getData();
        return MovementUtils2233.getDirectionRotation(dt[0].floatValue(), dt[1].floatValue(), dt[2].floatValue());
    }

    @NotNull
    public final Float[] getData() {
        KillAura killAura = this.killAura;
        if ((killAura != null ? killAura.getTarget() : null) == null) {
            return new Float[]{Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(0.0f)};
        }
        KillAura killAura2 = this.killAura;
        if (killAura2 == null) {
            Intrinsics.throwNpe();
        }
        EntityLivingBase entityLivingBase = killAura2.getTarget();
        if (entityLivingBase == null) {
            Intrinsics.throwNpe();
        }
        EntityLivingBase target = entityLivingBase;
        float rotYaw = RotationUtils.getRotationsEntity(target).getYaw();
        float forward = TargetStrafe.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)target) <= ((Number)this.radius.get()).floatValue() ? 0.0f : 1.0f;
        float strafe = this.direction;
        return new Float[]{Float.valueOf(rotYaw), Float.valueOf(strafe), Float.valueOf(forward)};
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        KillAura killAura = this.killAura;
        if (killAura == null) {
            Intrinsics.throwNpe();
        }
        EntityLivingBase target = killAura.getTarget();
        if (this.canStrafe()) {
            if (!this.hasModifiedMovement) {
                this.strafe(event, MovementUtils2233.getSpeed(event.getX(), event.getZ()));
            }
            if (((Boolean)this.safewalk.get()).booleanValue() && this.checkVoid()) {
                event.setSafeWalk(true);
            }
            if (((Boolean)this.onlySpeedValue.get()).booleanValue()) {
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (module.getState()) {
                    // empty if block
                }
            }
            if (!((Boolean)this.holdSpaceValue.get()).booleanValue() || TargetStrafe.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78901_c) {
                // empty if block
            }
        }
        this.hasModifiedMovement = false;
    }

    @EventTarget
    public final void strafe(@Nullable MoveEvent event, double moveSpeed) {
        EntityLivingBase target;
        KillAura killAura = this.killAura;
        if (killAura == null) {
            Intrinsics.throwNpe();
        }
        if ((target = killAura.getTarget()) != null) {
            MovementUtils.setSpeed(event, MovementUtils.getSpeed(), RotationUtils.getRotationsEntity(target).getYaw(), this.direction, TargetStrafe.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)target) <= ((Number)this.radius.get()).floatValue() ? 0.0 : 1.0);
        }
        KillAura killAura2 = this.killAura;
        if (killAura2 == null) {
            Intrinsics.throwNpe();
        }
        float rotYaw = RotationUtils.getRotationsEntity(killAura2.getTarget()).getYaw();
        if (TargetStrafe.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)target) <= ((Number)this.radius.get()).floatValue()) {
            MovementUtils.setSpeed(event, moveSpeed, rotYaw, this.direction, 0.0);
        } else {
            MovementUtils.setSpeed(event, moveSpeed, rotYaw, this.direction, 1.0);
        }
        if (((Boolean)this.behind.get()).booleanValue()) {
            EntityLivingBase entityLivingBase = target;
            if (entityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            double xPos = entityLivingBase.field_70165_t + -Math.sin(Math.toRadians(target.field_70177_z)) * (double)-2;
            double zPos = target.field_70161_v + Math.cos(Math.toRadians(target.field_70177_z)) * (double)-2;
            if (event != null) {
                event.setX1(moveSpeed * (double)(-MathHelper.func_76126_a((float)((float)Math.toRadians(RotationUtils.getRotations122(xPos, target.field_70163_u, zPos)[0])))));
            }
            if (event != null) {
                event.setZ1(moveSpeed * (double)MathHelper.func_76134_b((float)((float)Math.toRadians(RotationUtils.getRotations122(xPos, target.field_70163_u, zPos)[0]))));
            }
        } else if (TargetStrafe.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)target) <= ((Number)this.radius.get()).floatValue()) {
            MovementUtils.setSpeed(event, moveSpeed, rotYaw, this.direction, 0.0);
        } else {
            MovementUtils.setSpeed(event, moveSpeed, rotYaw, this.direction, 1.0);
        }
        this.hasModifiedMovement = true;
        if (!((Boolean)this.thirdPersonViewValue.get()).booleanValue()) {
            return;
        }
        TargetStrafe.access$getMc$p$s1046033730().field_71474_y.field_74320_O = target != null ? 9 : 0;
    }

    /*
     * WARNING - void declaration
     */
    private final boolean checkVoid() {
        int n = -1;
        boolean bl = false;
        while (n <= 0) {
            void x;
            int n2 = -1;
            boolean bl2 = false;
            while (n2 <= 0) {
                void z;
                if (this.isVoid((int)x, (int)z)) {
                    return true;
                }
                ++z;
            }
            ++x;
        }
        return false;
    }

    private final boolean isVoid(int X, int Z) {
        if (TargetStrafe.access$getMc$p$s1046033730().field_71439_g.field_70163_u < 0.0) {
            return true;
        }
        for (int off = 0; off < (int)TargetStrafe.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 2; off += 2) {
            AxisAlignedBB bb;
            EntityPlayerSP entityPlayerSP = TargetStrafe.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP.func_174813_aQ().func_72317_d((double)X, (double)(-off), (double)Z), "mc.thePlayer.entityBound\u2026toDouble(), Z.toDouble())");
            WorldClient worldClient = TargetStrafe.access$getMc$p$s1046033730().field_71441_e;
            if (worldClient == null) {
                Intrinsics.throwNpe();
            }
            EntityPlayerSP entityPlayerSP2 = TargetStrafe.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.Entity");
            }
            if (worldClient.func_72945_a((Entity)entityPlayerSP2, bb).isEmpty()) {
                continue;
            }
            return false;
        }
        return true;
    }

    public final void strafe(@Nullable MoveEvent event, double speed, double moveSpeed) {
        EntityLivingBase target;
        KillAura killAura = this.killAura;
        Object object = killAura != null ? killAura.getTarget() : null;
        if (object == null) {
            Intrinsics.throwNpe();
        }
        if ((target = object) != null) {
            MovementUtils.setSpeed(event, speed, RotationUtils.getRotationsEntity(target).getYaw(), this.direction, TargetStrafe.access$getMc$p$s1046033730().field_71439_g.func_70032_d((Entity)target) <= ((Number)this.radius.get()).floatValue() ? 0.0 : 1.0);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public final boolean getKeyMode() {
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case 3387192: {
                if (!string.equals("none")) return false;
                break;
            }
            case 3273774: {
                if (!string.equals("jump")) return false;
                KeyBinding keyBinding = TargetStrafe.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
                Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
                boolean bl2 = keyBinding.func_151470_d();
                return bl2;
            }
        }
        if (TargetStrafe.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78902_a != 0.0f) return true;
        if (TargetStrafe.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78900_b != 0.0f) return true;
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean canStrafe() {
        if (this.killAura == null) {
            this.killAura = (KillAura)LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        }
        KillAura killAura = this.killAura;
        if (killAura == null) {
            Intrinsics.throwNpe();
        }
        if (killAura.getState()) {
            KillAura killAura2 = this.killAura;
            if (killAura2 == null) {
                Intrinsics.throwNpe();
            }
            if (killAura2.getTarget() != null && this.getState()) {
                KillAura killAura3 = this.killAura;
                if (killAura3 == null) {
                    Intrinsics.throwNpe();
                }
                if (this.targetValidator.validate((Entity)killAura3.getTarget()) && (!((Boolean)this.holdSpaceValue.get()).booleanValue() || TargetStrafe.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78901_c)) {
                    if ((Boolean)this.onlySpeedValue.get() == false) return true;
                    Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                    if (module == null) {
                        Intrinsics.throwNpe();
                    }
                    if (module.getState()) return true;
                }
            }
        }
        if ((Boolean)this.onlyflyValue.get() == false) return false;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Fly.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (!module.getState()) return false;
        if (!this.getKeyMode()) return false;
        return true;
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        EntityLivingBase target;
        Intrinsics.checkParameterIsNotNull(event, "event");
        KillAura killAura = this.killAura;
        Object object = target = killAura != null ? killAura.getTarget() : null;
        if ((this.canStrafe() || ((Boolean)this.alwaysRender.get()).booleanValue()) && ((Boolean)this.alwaysRender.get()).booleanValue()) {
            if (target == null) {
                return;
            }
            double posX = target.field_70142_S + (target.field_70165_t - target.field_70142_S) * (double)event.getPartialTicks() - TargetStrafe.access$getMc$p$s1046033730().func_175598_ae().field_78725_b;
            double d = target.field_70137_T + (target.field_70163_u - target.field_70137_T) * (double)event.getPartialTicks();
            Minecraft minecraft = TargetStrafe.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            double posY = d - minecraft.func_175598_ae().field_78726_c;
            double d2 = target.field_70136_U + (target.field_70161_v - target.field_70136_U) * (double)event.getPartialTicks();
            Minecraft minecraft2 = TargetStrafe.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            double posZ = d2 - minecraft2.func_175598_ae().field_78723_d;
            ESPUtils.cylinder((Entity)target, posX, posY, posZ, this.range, this.slice);
            ESPUtils.shadow((Entity)target, posX, posY, posZ, this.range, this.slice);
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return (Boolean)this.displayTag.get() != false ? String.valueOf(((Number)this.radius.get()).floatValue()) : null;
    }

    public TargetStrafe() {
        this.slice = 64;
        this.range = 1;
        this.targetValidator = new EntityValidator();
        this.targetValidator.add(new VoidCheck());
        this.targetValidator.add(new WallCheck());
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

