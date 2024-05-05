/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Model")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0010\u001a\u00020\u0011H\u0016J \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Model;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "(DD)V", "customPitch", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "customYaw", "pitchMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "rotate", "", "rotateDirection", "", "yawMode", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "drawEntityOnScreen", "", "yaw", "pitch", "entityLivingBase", "Lnet/minecraft/entity/EntityLivingBase;", "KyinoClient"})
public final class Model
extends Element {
    private final ListValue yawMode;
    private final FloatValue customYaw;
    private final ListValue pitchMode;
    private final FloatValue customPitch;
    private float rotate;
    private boolean rotateDirection;

    /*
     * Unable to fully structure code
     */
    @Override
    @NotNull
    public Border drawElement() {
        block24: {
            var2_1 = (String)this.yawMode.get();
            var3_3 = false;
            v0 = var2_1;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v1 = v0.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toLowerCase()");
            var2_1 = v1;
            tmp = -1;
            switch (var2_1.hashCode()) {
                case -1349088399: {
                    if (!var2_1.equals("custom")) break;
                    tmp = 1;
                    break;
                }
                case -985752863: {
                    if (!var2_1.equals("player")) break;
                    tmp = 2;
                    break;
                }
                case 1118509956: {
                    if (!var2_1.equals("animation")) break;
                    tmp = 3;
                    break;
                }
            }
            switch (tmp) {
                case 2: {
                    v2 = Model.access$getMc$p$s1046033730().field_71439_g.field_70177_z;
                    break;
                }
                case 3: {
                    delta = RenderUtils.deltaTime;
                    if (this.rotateDirection) {
                        if (this.rotate <= 70.0f) {
                            this.rotate += 0.12f * (float)delta;
                        } else {
                            this.rotateDirection = false;
                            this.rotate = 70.0f;
                        }
                    } else if (this.rotate >= -70.0f) {
                        this.rotate -= 0.12f * (float)delta;
                    } else {
                        this.rotateDirection = true;
                        this.rotate = -70.0f;
                    }
                    v2 = this.rotate;
                    break;
                }
                case 1: {
                    v2 = ((Number)this.customYaw.get()).floatValue();
                    break;
                }
                default: {
                    v2 = 0.0f;
                }
            }
            yaw = v2;
            var3_4 = (String)this.pitchMode.get();
            var4_7 = false;
            v3 = var3_4;
            if (v3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v4 = v3.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(v4, "(this as java.lang.String).toLowerCase()");
            var3_4 = v4;
            switch (var3_4.hashCode()) {
                case -1349088399: {
                    if (!var3_4.equals("custom")) ** break;
                    break;
                }
                case -985752863: {
                    if (!var3_4.equals("player")) ** break;
                    v5 = Model.access$getMc$p$s1046033730().field_71439_g.field_70125_A;
                    break block24;
                }
            }
            v5 = ((Number)this.customPitch.get()).floatValue();
            break block24;
            v5 = pitch = 0.0f;
        }
        if (pitch > (float)false) {
            v6 = -pitch;
        } else {
            var3_5 = false;
            v6 = Math.abs(pitch);
        }
        pitch = v6;
        v7 = Model.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(v7, "mc.thePlayer");
        this.drawEntityOnScreen(yaw, pitch, (EntityLivingBase)v7);
        return new Border(30.0f, 10.0f, -30.0f, -100.0f);
    }

    private final void drawEntityOnScreen(float yaw, float pitch, EntityLivingBase entityLivingBase) {
        GlStateManager.func_179117_G();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)50.0f);
        GlStateManager.func_179152_a((float)-50.0f, (float)50.0f, (float)50.0f);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float renderYawOffset = entityLivingBase.field_70761_aq;
        float rotationYaw = entityLivingBase.field_70177_z;
        float rotationPitch = entityLivingBase.field_70125_A;
        float prevRotationYawHead = entityLivingBase.field_70758_at;
        float rotationYawHead = entityLivingBase.field_70759_as;
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f = pitch / 40.0f;
        boolean bl = false;
        GlStateManager.func_179114_b((float)(-((float)Math.atan(f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        f = yaw / 40.0f;
        EntityLivingBase entityLivingBase2 = entityLivingBase;
        bl = false;
        float f2 = (float)Math.atan(f);
        entityLivingBase2.field_70761_aq = f2 * 20.0f;
        f = yaw / 40.0f;
        entityLivingBase2 = entityLivingBase;
        bl = false;
        f2 = (float)Math.atan(f);
        entityLivingBase2.field_70177_z = f2 * 40.0f;
        f = pitch / 40.0f;
        entityLivingBase2 = entityLivingBase;
        bl = false;
        f2 = (float)Math.atan(f);
        entityLivingBase2.field_70125_A = -f2 * 20.0f;
        entityLivingBase.field_70759_as = entityLivingBase.field_70177_z;
        entityLivingBase.field_70758_at = entityLivingBase.field_70177_z;
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        Minecraft minecraft = Model.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        RenderManager renderManager = minecraft.func_175598_ae();
        renderManager.func_178631_a(180.0f);
        RenderManager renderManager2 = renderManager;
        Intrinsics.checkExpressionValueIsNotNull(renderManager2, "renderManager");
        renderManager2.func_178633_a(false);
        renderManager.func_147940_a((Entity)entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        renderManager.func_178633_a(true);
        entityLivingBase.field_70761_aq = renderYawOffset;
        entityLivingBase.field_70177_z = rotationYaw;
        entityLivingBase.field_70125_A = rotationPitch;
        entityLivingBase.field_70758_at = prevRotationYawHead;
        entityLivingBase.field_70759_as = rotationYawHead;
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
        GlStateManager.func_179117_G();
    }

    public Model(double x, double y) {
        super(x, y, 0.0f, null, 12, null);
        this.yawMode = new ListValue("Yaw", new String[]{"Player", "Animation", "Custom"}, "Animation");
        this.customYaw = new FloatValue("CustomYaw", 0.0f, -180.0f, 180.0f);
        this.pitchMode = new ListValue("Pitch", new String[]{"Player", "Custom"}, "Player");
        this.customPitch = new FloatValue("CustomPitch", 0.0f, -90.0f, 90.0f);
    }

    public /* synthetic */ Model(double d, double d2, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 40.0;
        }
        if ((n & 2) != 0) {
            d2 = 100.0;
        }
        this(d, d2);
    }

    public Model() {
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

