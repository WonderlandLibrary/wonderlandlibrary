/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud.element.elements;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.FloatValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Model")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0010\u001a\u00020\u0011H\u0016J \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Model;", "Lnet/dev/important/gui/client/hud/element/Element;", "x", "", "y", "(DD)V", "customPitch", "Lnet/dev/important/value/FloatValue;", "customYaw", "pitchMode", "Lnet/dev/important/value/ListValue;", "rotate", "", "rotateDirection", "", "yawMode", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "drawEntityOnScreen", "", "yaw", "pitch", "entityLivingBase", "Lnet/minecraft/entity/EntityLivingBase;", "LiquidBounce"})
public final class Model
extends Element {
    @NotNull
    private final ListValue yawMode;
    @NotNull
    private final FloatValue customYaw;
    @NotNull
    private final ListValue pitchMode;
    @NotNull
    private final FloatValue customPitch;
    private float rotate;
    private boolean rotateDirection;

    public Model(double x, double y) {
        super(x, y, 0.0f, null, 12, null);
        String[] stringArray = new String[]{"Player", "Animation", "Custom"};
        this.yawMode = new ListValue("Yaw", stringArray, "Animation");
        this.customYaw = new FloatValue("CustomYaw", 0.0f, -180.0f, 180.0f);
        stringArray = new String[]{"Player", "Custom"};
        this.pitchMode = new ListValue("Pitch", stringArray, "Player");
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

    @Override
    @NotNull
    public Border drawElement() {
        float f;
        String string = ((String)this.yawMode.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "player": {
                f = MinecraftInstance.mc.field_71439_g.field_70177_z;
                break;
            }
            case "animation": {
                int delta = RenderUtils.deltaTime;
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
                f = this.rotate;
                break;
            }
            case "custom": {
                f = ((Number)this.customYaw.get()).floatValue();
                break;
            }
            default: {
                f = 0.0f;
            }
        }
        float yaw = f;
        String string2 = ((String)this.pitchMode.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String).toLowerCase()");
        String string3 = string2;
        float pitch = Intrinsics.areEqual(string3, "player") ? MinecraftInstance.mc.field_71439_g.field_70125_A : (Intrinsics.areEqual(string3, "custom") ? ((Number)this.customPitch.get()).floatValue() : 0.0f);
        pitch = pitch > 0.0f ? -pitch : Math.abs(pitch);
        string3 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNullExpressionValue(string3, "mc.thePlayer");
        this.drawEntityOnScreen(yaw, pitch, (EntityLivingBase)string3);
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
        GlStateManager.func_179114_b((float)(-((float)Math.atan(pitch / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        entityLivingBase.field_70761_aq = (float)Math.atan(yaw / 40.0f) * 20.0f;
        entityLivingBase.field_70177_z = (float)Math.atan(yaw / 40.0f) * 40.0f;
        entityLivingBase.field_70125_A = -((float)Math.atan(pitch / 40.0f)) * 20.0f;
        entityLivingBase.field_70759_as = entityLivingBase.field_70177_z;
        entityLivingBase.field_70758_at = entityLivingBase.field_70177_z;
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager renderManager = MinecraftInstance.mc.func_175598_ae();
        renderManager.func_178631_a(180.0f);
        renderManager.func_178633_a(false);
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

    public Model() {
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }
}

