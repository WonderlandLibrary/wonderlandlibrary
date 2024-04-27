package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import me.manager.ColorManager;
import me.utils.player.PlayerUtil;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="TargetStrafe", description="quq", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\\\n\u0002\n\n\b\n\b\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J%0&2\b0J'0(20HJ)0&2*02+0HJ,0-2.0/HJ00-2.01HR0X¢\n\u0000\b\"\b\bR\t0\n¢\b\n\u0000\b\fR\r0¢\b\n\u0000\bR0\n¢\b\n\u0000\b\fR0\n¢\b\n\u0000\b\fR0¢\b\n\u0000\bR0¢\b\n\u0000\bR0X¢\n\u0000\b \"\b!\"R#0\n¢\b\n\u0000\b$\f¨2"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/TargetStrafe;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "direction", "", "getDirection", "()I", "setDirection", "(I)V", "holdSpaceValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getHoldSpaceValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "killAura", "Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura;", "getKillAura", "()Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura;", "onlySpeedValue", "getOnlySpeedValue", "onlyflyValue", "getOnlyflyValue", "radiusValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getRadiusValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "renderModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getRenderModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "target", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getTarget", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "setTarget", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)V", "thirdPersonViewValue", "getThirdPersonViewValue", "canStrafe", "", "getStrafeDistance", "", "isVoid", "xPos", "zPos", "onMove", "", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "Pride"})
public final class TargetStrafe
extends Module {
    @NotNull
    private final ListValue renderModeValue = new ListValue("RenderMode", new String[]{"Circle", "Pentagon", "None"}, "Pentagon");
    @NotNull
    private final BoolValue thirdPersonViewValue = new BoolValue("ThirdPersonView", false);
    @NotNull
    private final FloatValue radiusValue = new FloatValue("Radius", 0.1f, 0.5f, 5.0f);
    @NotNull
    private final BoolValue holdSpaceValue = new BoolValue("HoldSpace", false);
    @NotNull
    private final BoolValue onlySpeedValue = new BoolValue("OnlySpeed", false);
    @NotNull
    private final BoolValue onlyflyValue = new BoolValue("keyFly", false);
    private int direction = -1;
    @NotNull
    private final KillAura killAura;
    @Nullable
    private IEntityLivingBase target;

    @NotNull
    public final ListValue getRenderModeValue() {
        return this.renderModeValue;
    }

    @NotNull
    public final BoolValue getThirdPersonViewValue() {
        return this.thirdPersonViewValue;
    }

    @NotNull
    public final FloatValue getRadiusValue() {
        return this.radiusValue;
    }

    @NotNull
    public final BoolValue getHoldSpaceValue() {
        return this.holdSpaceValue;
    }

    @NotNull
    public final BoolValue getOnlySpeedValue() {
        return this.onlySpeedValue;
    }

    @NotNull
    public final BoolValue getOnlyflyValue() {
        return this.onlyflyValue;
    }

    public final int getDirection() {
        return this.direction;
    }

    public final void setDirection(int n) {
        this.direction = n;
    }

    @NotNull
    public final KillAura getKillAura() {
        return this.killAura;
    }

    @Nullable
    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityLivingBase auraTarget = this.killAura.getTarget();
        if (auraTarget != null) {
            this.target = auraTarget;
        }
        if (Intrinsics.areEqual((String)this.renderModeValue.get(), "None") ^ true && this.canStrafe(this.target)) {
            int[] counter = new int[]{0};
            if (StringsKt.equals((String)this.renderModeValue.get(), "Circle", true)) {
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)2881);
                GL11.glEnable((int)2832);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glHint((int)3154, (int)4354);
                GL11.glHint((int)3155, (int)4354);
                GL11.glHint((int)3153, (int)4354);
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                GL11.glLineWidth((float)1.0f);
                GL11.glBegin((int)3);
                IEntityLivingBase iEntityLivingBase = this.target;
                if (iEntityLivingBase == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityLivingBase.getLastTickPosX();
                IEntityLivingBase iEntityLivingBase2 = this.target;
                if (iEntityLivingBase2 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = iEntityLivingBase2.getPosX();
                IEntityLivingBase iEntityLivingBase3 = this.target;
                if (iEntityLivingBase3 == null) {
                    Intrinsics.throwNpe();
                }
                double x = d + (d2 - iEntityLivingBase3.getLastTickPosX()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                IEntityLivingBase iEntityLivingBase4 = this.target;
                if (iEntityLivingBase4 == null) {
                    Intrinsics.throwNpe();
                }
                double d3 = iEntityLivingBase4.getLastTickPosY();
                IEntityLivingBase iEntityLivingBase5 = this.target;
                if (iEntityLivingBase5 == null) {
                    Intrinsics.throwNpe();
                }
                double d4 = iEntityLivingBase5.getPosY();
                IEntityLivingBase iEntityLivingBase6 = this.target;
                if (iEntityLivingBase6 == null) {
                    Intrinsics.throwNpe();
                }
                double y = d3 + (d4 - iEntityLivingBase6.getLastTickPosY()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY();
                IEntityLivingBase iEntityLivingBase7 = this.target;
                if (iEntityLivingBase7 == null) {
                    Intrinsics.throwNpe();
                }
                double d5 = iEntityLivingBase7.getLastTickPosZ();
                IEntityLivingBase iEntityLivingBase8 = this.target;
                if (iEntityLivingBase8 == null) {
                    Intrinsics.throwNpe();
                }
                double d6 = iEntityLivingBase8.getPosZ();
                IEntityLivingBase iEntityLivingBase9 = this.target;
                if (iEntityLivingBase9 == null) {
                    Intrinsics.throwNpe();
                }
                double z = d5 + (d6 - iEntityLivingBase9.getLastTickPosZ()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
                int n = 0;
                int n2 = 359;
                while (n <= n2) {
                    void i;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    double d7 = (double)i / 50.0 * 1.75;
                    double d8 = (double)iEntityPlayerSP.getTicksExisted() / 70.0;
                    boolean bl = false;
                    double d9 = Math.sin(d7);
                    int n3 = Color.HSBtoRGB((float)((d8 + d9) % (double)1.0f), 0.7f, 1.0f);
                    Color rainbow = new Color(n3);
                    GL11.glColor3f((float)((float)rainbow.getRed() / 255.0f), (float)((float)rainbow.getGreen() / 255.0f), (float)((float)rainbow.getBlue() / 255.0f));
                    d7 = (double)i * (Math.PI * 2) / 45.0;
                    d8 = ((Number)this.radiusValue.get()).doubleValue();
                    double d10 = x;
                    bl = false;
                    d9 = Math.cos(d7);
                    double d11 = d10 + d8 * d9;
                    d7 = (double)i * (Math.PI * 2) / 45.0;
                    double d12 = ((Number)this.radiusValue.get()).doubleValue();
                    d9 = z;
                    d8 = y;
                    d10 = d11;
                    bl = false;
                    double d13 = Math.sin(d7);
                    GL11.glVertex3d((double)d10, (double)d8, (double)(d9 + d12 * d13));
                    ++i;
                }
                GL11.glEnd();
                GL11.glDepthMask((boolean)true);
                GL11.glEnable((int)2929);
                GL11.glDisable((int)2848);
                GL11.glDisable((int)2881);
                GL11.glEnable((int)2832);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
            } else {
                float rad = ((Number)this.radiusValue.get()).floatValue();
                if (this.target == null) {
                    return;
                }
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                RenderUtils.startDrawing();
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                GL11.glLineWidth((float)1.0f);
                GL11.glBegin((int)3);
                IEntityLivingBase iEntityLivingBase = this.target;
                if (iEntityLivingBase == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityLivingBase.getLastTickPosX();
                IEntityLivingBase iEntityLivingBase10 = this.target;
                if (iEntityLivingBase10 == null) {
                    Intrinsics.throwNpe();
                }
                double d14 = iEntityLivingBase10.getPosX();
                IEntityLivingBase iEntityLivingBase11 = this.target;
                if (iEntityLivingBase11 == null) {
                    Intrinsics.throwNpe();
                }
                double x = d + (d14 - iEntityLivingBase11.getLastTickPosX()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                IEntityLivingBase iEntityLivingBase12 = this.target;
                if (iEntityLivingBase12 == null) {
                    Intrinsics.throwNpe();
                }
                double d15 = iEntityLivingBase12.getLastTickPosY();
                IEntityLivingBase iEntityLivingBase13 = this.target;
                if (iEntityLivingBase13 == null) {
                    Intrinsics.throwNpe();
                }
                double d16 = iEntityLivingBase13.getPosY();
                IEntityLivingBase iEntityLivingBase14 = this.target;
                if (iEntityLivingBase14 == null) {
                    Intrinsics.throwNpe();
                }
                double y = d15 + (d16 - iEntityLivingBase14.getLastTickPosY()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY();
                IEntityLivingBase iEntityLivingBase15 = this.target;
                if (iEntityLivingBase15 == null) {
                    Intrinsics.throwNpe();
                }
                double d17 = iEntityLivingBase15.getLastTickPosZ();
                IEntityLivingBase iEntityLivingBase16 = this.target;
                if (iEntityLivingBase16 == null) {
                    Intrinsics.throwNpe();
                }
                double d18 = iEntityLivingBase16.getPosZ();
                IEntityLivingBase iEntityLivingBase17 = this.target;
                if (iEntityLivingBase17 == null) {
                    Intrinsics.throwNpe();
                }
                double z = d17 + (d18 - iEntityLivingBase17.getLastTickPosZ()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
                int n = 0;
                int n4 = 10;
                while (n <= n4) {
                    double d19;
                    double d20;
                    double d21;
                    boolean bl;
                    double d22;
                    double d23;
                    double d24;
                    void i;
                    counter[0] = counter[0] + 1;
                    Color rainbow = new Color(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107));
                    GL11.glColor3f((float)((float)rainbow.getRed() / 255.0f), (float)((float)rainbow.getGreen() / 255.0f), (float)((float)rainbow.getBlue() / 255.0f));
                    if ((double)rad < 0.8 && (double)rad > 0.0) {
                        d24 = (double)i * (Math.PI * 2) / 3.0;
                        d23 = rad;
                        d22 = x;
                        bl = false;
                        d21 = Math.cos(d24);
                        double d25 = d22 + d23 * d21;
                        d24 = (double)i * (Math.PI * 2) / 3.0;
                        d20 = rad;
                        d21 = z;
                        d23 = y;
                        d22 = d25;
                        bl = false;
                        d19 = Math.sin(d24);
                        GL11.glVertex3d((double)d22, (double)d23, (double)(d21 + d20 * d19));
                    }
                    if ((double)rad < 1.5 && (double)rad > 0.7) {
                        counter[0] = counter[0] + 1;
                        RenderUtils.glColor3(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107));
                        d24 = (double)i * (Math.PI * 2) / 4.0;
                        d23 = rad;
                        d22 = x;
                        bl = false;
                        d21 = Math.cos(d24);
                        double d26 = d22 + d23 * d21;
                        d24 = (double)i * (Math.PI * 2) / 4.0;
                        d20 = rad;
                        d21 = z;
                        d23 = y;
                        d22 = d26;
                        bl = false;
                        d19 = Math.sin(d24);
                        GL11.glVertex3d((double)d22, (double)d23, (double)(d21 + d20 * d19));
                    }
                    if ((double)rad < 2.0 && (double)rad > 1.4) {
                        counter[0] = counter[0] + 1;
                        RenderUtils.glColor3(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107));
                        d24 = (double)i * (Math.PI * 2) / 5.0;
                        d23 = rad;
                        d22 = x;
                        bl = false;
                        d21 = Math.cos(d24);
                        double d27 = d22 + d23 * d21;
                        d24 = (double)i * (Math.PI * 2) / 5.0;
                        d20 = rad;
                        d21 = z;
                        d23 = y;
                        d22 = d27;
                        bl = false;
                        d19 = Math.sin(d24);
                        GL11.glVertex3d((double)d22, (double)d23, (double)(d21 + d20 * d19));
                    }
                    if ((double)rad < 2.4 && (double)rad > 1.9) {
                        counter[0] = counter[0] + 1;
                        RenderUtils.glColor3(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107));
                        d24 = (double)i * (Math.PI * 2) / 6.0;
                        d23 = rad;
                        d22 = x;
                        bl = false;
                        d21 = Math.cos(d24);
                        double d28 = d22 + d23 * d21;
                        d24 = (double)i * (Math.PI * 2) / 6.0;
                        d20 = rad;
                        d21 = z;
                        d23 = y;
                        d22 = d28;
                        bl = false;
                        d19 = Math.sin(d24);
                        GL11.glVertex3d((double)d22, (double)d23, (double)(d21 + d20 * d19));
                    }
                    if ((double)rad < 2.7 && (double)rad > 2.3) {
                        counter[0] = counter[0] + 1;
                        RenderUtils.glColor3(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107));
                        d24 = (double)i * (Math.PI * 2) / 7.0;
                        d23 = rad;
                        d22 = x;
                        bl = false;
                        d21 = Math.cos(d24);
                        double d29 = d22 + d23 * d21;
                        d24 = (double)i * (Math.PI * 2) / 7.0;
                        d20 = rad;
                        d21 = z;
                        d23 = y;
                        d22 = d29;
                        bl = false;
                        d19 = Math.sin(d24);
                        GL11.glVertex3d((double)d22, (double)d23, (double)(d21 + d20 * d19));
                    }
                    if ((double)rad < 6.0 && (double)rad > 2.6) {
                        counter[0] = counter[0] + 1;
                        RenderUtils.glColor3(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107));
                        d24 = (double)i * (Math.PI * 2) / 8.0;
                        d23 = rad;
                        d22 = x;
                        bl = false;
                        d21 = Math.cos(d24);
                        double d30 = d22 + d23 * d21;
                        d24 = (double)i * (Math.PI * 2) / 8.0;
                        d20 = rad;
                        d21 = z;
                        d23 = y;
                        d22 = d30;
                        bl = false;
                        d19 = Math.sin(d24);
                        GL11.glVertex3d((double)d22, (double)d23, (double)(d21 + d20 * d19));
                    }
                    if ((double)rad < 7.0 && (double)rad > 5.9) {
                        counter[0] = counter[0] + 1;
                        RenderUtils.glColor3(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107));
                        d24 = (double)i * (Math.PI * 2) / 9.0;
                        d23 = rad;
                        d22 = x;
                        bl = false;
                        d21 = Math.cos(d24);
                        double d31 = d22 + d23 * d21;
                        d24 = (double)i * (Math.PI * 2) / 9.0;
                        d20 = rad;
                        d21 = z;
                        d23 = y;
                        d22 = d31;
                        bl = false;
                        d19 = Math.sin(d24);
                        GL11.glVertex3d((double)d22, (double)d23, (double)(d21 + d20 * d19));
                    }
                    if ((double)rad < 11.0 && (double)rad > 6.9) {
                        counter[0] = counter[0] + 1;
                        RenderUtils.glColor3(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107));
                        d24 = (double)i * (Math.PI * 2) / 10.0;
                        d23 = rad;
                        d22 = x;
                        bl = false;
                        d21 = Math.cos(d24);
                        double d32 = d22 + d23 * d21;
                        d24 = (double)i * (Math.PI * 2) / 10.0;
                        d20 = rad;
                        d21 = z;
                        d23 = y;
                        d22 = d32;
                        bl = false;
                        d19 = Math.sin(d24);
                        GL11.glVertex3d((double)d22, (double)d23, (double)(d21 + d20 * d19));
                    }
                    ++i;
                }
                GL11.glEnd();
                GL11.glDepthMask((boolean)true);
                GL11.glEnable((int)2929);
                RenderUtils.stopDrawing();
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        double d;
        double[] dArray;
        float mathStrafe;
        float yaw;
        block39: {
            double moveAssist;
            block41: {
                float rotAssist;
                block40: {
                    float targetStrafe;
                    block36: {
                        block38: {
                            block37: {
                                float f;
                                Intrinsics.checkParameterIsNotNull(event, "event");
                                IEntityLivingBase auraTarget = this.killAura.getTarget();
                                if (auraTarget != null) {
                                    this.target = auraTarget;
                                }
                                if (!this.canStrafe(this.target)) {
                                    return;
                                }
                                boolean aroundVoid = false;
                                int n = -1;
                                boolean bl = false;
                                while (n <= 0) {
                                    void x;
                                    int n2 = -1;
                                    boolean bl2 = false;
                                    while (n2 <= 0) {
                                        void z;
                                        if (this.isVoid((int)x, (int)z)) {
                                            aroundVoid = true;
                                        }
                                        ++z;
                                    }
                                    ++x;
                                }
                                yaw = RotationUtils.getRotationFromEyeHasPrev(this.killAura.getTarget()).getYaw();
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (iEntityPlayerSP.isCollidedHorizontally() || aroundVoid) {
                                    this.direction *= -1;
                                }
                                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP2 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (iEntityPlayerSP2.getMoveStrafing() != 0.0f) {
                                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                                    if (iEntityPlayerSP3 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    f = iEntityPlayerSP3.getMoveStrafing() * (float)this.direction;
                                } else {
                                    f = targetStrafe = (float)this.direction;
                                }
                                if (!PlayerUtil.isBlockUnder()) {
                                    targetStrafe = 0.0f;
                                }
                                float f2 = 45;
                                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP4 == null) {
                                    Intrinsics.throwNpe();
                                }
                                IEntityLivingBase iEntityLivingBase = this.killAura.getTarget();
                                if (iEntityLivingBase == null) {
                                    Intrinsics.throwNpe();
                                }
                                rotAssist = f2 / iEntityPlayerSP4.getDistanceToEntity(iEntityLivingBase);
                                IEntityLivingBase iEntityLivingBase2 = this.killAura.getTarget();
                                if (iEntityLivingBase2 == null) {
                                    Intrinsics.throwNpe();
                                }
                                moveAssist = 45.0f / this.getStrafeDistance(iEntityLivingBase2);
                                mathStrafe = 0.0f;
                                if (!(targetStrafe > 0.0f)) break block36;
                                IEntityLivingBase iEntityLivingBase3 = this.target;
                                if (iEntityLivingBase3 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d2 = iEntityLivingBase3.getEntityBoundingBox().getMinY();
                                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP5 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (d2 > iEntityPlayerSP5.getEntityBoundingBox().getMaxY()) break block37;
                                IEntityLivingBase iEntityLivingBase4 = this.target;
                                if (iEntityLivingBase4 == null) {
                                    Intrinsics.throwNpe();
                                }
                                double d3 = iEntityLivingBase4.getEntityBoundingBox().getMaxY();
                                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP6 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (!(d3 < iEntityPlayerSP6.getEntityBoundingBox().getMinY())) break block38;
                            }
                            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP == null) {
                                Intrinsics.throwNpe();
                            }
                            IEntityLivingBase iEntityLivingBase = this.killAura.getTarget();
                            if (iEntityLivingBase == null) {
                                Intrinsics.throwNpe();
                            }
                            if (iEntityPlayerSP.getDistanceToEntity(iEntityLivingBase) < ((Number)this.radiusValue.get()).floatValue()) {
                                yaw += -rotAssist;
                            }
                        }
                        mathStrafe += -((float)moveAssist);
                        break block39;
                    }
                    if (!(targetStrafe < 0.0f)) break block39;
                    IEntityLivingBase iEntityLivingBase = this.target;
                    if (iEntityLivingBase == null) {
                        Intrinsics.throwNpe();
                    }
                    double d4 = iEntityLivingBase.getEntityBoundingBox().getMinY();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (d4 > iEntityPlayerSP.getEntityBoundingBox().getMaxY()) break block40;
                    IEntityLivingBase iEntityLivingBase5 = this.target;
                    if (iEntityLivingBase5 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d5 = iEntityLivingBase5.getEntityBoundingBox().getMaxY();
                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP7 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(d5 < iEntityPlayerSP7.getEntityBoundingBox().getMinY())) break block41;
                }
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                IEntityLivingBase iEntityLivingBase = this.killAura.getTarget();
                if (iEntityLivingBase == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getDistanceToEntity(iEntityLivingBase) < ((Number)this.radiusValue.get()).floatValue()) {
                    yaw += rotAssist;
                }
            }
            mathStrafe += (float)moveAssist;
        }
        double d6 = Math.toRadians(yaw + 90.0f + mathStrafe);
        int n = 0;
        double[] dArray2 = dArray = new double[2];
        boolean bl = false;
        dArray[n] = d = Math.cos(d6);
        d6 = Math.toRadians(yaw + 90.0f + mathStrafe);
        n = 1;
        dArray = dArray2;
        bl = false;
        dArray[n] = d = Math.sin(d6);
        double[] doSomeMath = dArray2;
        double d7 = event.getX();
        double d8 = 2.0;
        boolean bl3 = false;
        double d9 = Math.pow(d7, d8);
        d7 = event.getZ();
        d8 = 2.0;
        double d10 = d9;
        bl3 = false;
        double d11 = Math.pow(d7, d8);
        d7 = d10 + d11;
        boolean bl4 = false;
        double moveSpeed = Math.sqrt(d7);
        double[] asLast = new double[]{moveSpeed * doSomeMath[0], moveSpeed * doSomeMath[1]};
        event.setX(asLast[0]);
        event.setZ(asLast[1]);
        if (!((Boolean)this.thirdPersonViewValue.get()).booleanValue()) {
            return;
        }
        MinecraftInstance.mc2.gameSettings.thirdPersonView = this.canStrafe(this.target) ? 3 : 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean canStrafe(@Nullable IEntityLivingBase target) {
        if (target == null) return false;
        if (((Boolean)this.holdSpaceValue.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP.getMovementInput().getJump()) return false;
        }
        if ((Boolean)this.onlySpeedValue.get() == false) return true;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module.getState()) return true;
        if ((Boolean)this.onlyflyValue.get() == false) return false;
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().get(Fly.class);
        if (module2 == null) {
            Intrinsics.throwNpe();
        }
        if (!module2.getState()) return false;
        return true;
    }

    private final float getStrafeDistance(IEntityLivingBase target) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        float f = iEntityPlayerSP.getDistanceToEntity(target) - ((Number)this.radiusValue.get()).floatValue();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        float f2 = iEntityPlayerSP2.getDistanceToEntity(target);
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        return RangesKt.coerceAtLeast(f, f2 - (iEntityPlayerSP3.getDistanceToEntity(target) - ((Number)this.radiusValue.get()).floatValue() / (((Number)this.radiusValue.get()).floatValue() * (float)2)));
    }

    private final boolean isVoid(int xPos, int zPos) {
        block7: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getPosY() < 0.0) {
                return true;
            }
            int off = 0;
            while (true) {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (off >= (int)iEntityPlayerSP2.getPosY() + 2) break block7;
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                IAxisAlignedBB bb = iEntityPlayerSP3.getEntityBoundingBox().offset(xPos, -((double)off), zPos);
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iWorldClient.getCollidingBoundingBoxes(iEntityPlayerSP4, bb).isEmpty()) break;
                off += 2;
            }
            return false;
        }
        return true;
    }

    public TargetStrafe() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        this.killAura = (KillAura)module;
    }
}
