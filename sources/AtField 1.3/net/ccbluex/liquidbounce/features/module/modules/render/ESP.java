/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.FloatCompanionObject
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Vector3f
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FloatCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.WorldToScreen;
import net.ccbluex.liquidbounce.utils.render.shader.FramebufferShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

@ModuleInfo(name="ESP", description="Allows you to see targets through walls.", category=ModuleCategory.RENDER)
public final class ESP
extends Module {
    private final FloatValue shaderGlowRadius;
    @JvmField
    public final FloatValue wireframeWidth;
    @JvmField
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "OtherBox", "WireFrame", "2D", "Real2D", "Outline", "ShaderOutline", "ShaderGlow"}, "Box");
    public static final Companion Companion = new Companion(null);
    private final IntegerValue colorGreenValue;
    private final BoolValue colorRainbow;
    private final IntegerValue colorRedValue;
    @JvmField
    public static boolean renderNameTags = true;
    private final IntegerValue colorBlueValue;
    private final BoolValue colorTeam;
    @JvmField
    public final FloatValue outlineWidth = new FloatValue("Outline-Width", 3.0f, 0.5f, 5.0f);
    private final FloatValue shaderOutlineRadius;

    public final Color getColor(@Nullable IEntity iEntity) {
        ESP eSP = this;
        boolean bl = false;
        boolean bl2 = false;
        ESP eSP2 = eSP;
        boolean bl3 = false;
        if (iEntity != null && MinecraftInstance.classProvider.isEntityLivingBase(iEntity)) {
            IEntityLivingBase iEntityLivingBase = iEntity.asEntityLivingBase();
            if (iEntityLivingBase.getHurtTime() > 0) {
                return Color.RED;
            }
            if (EntityUtils.isFriend((IEntity)iEntityLivingBase)) {
                return Color.BLUE;
            }
            if (((Boolean)eSP2.colorTeam.get()).booleanValue()) {
                IIChatComponent iIChatComponent = iEntityLivingBase.getDisplayName();
                if (iIChatComponent == null) {
                } else {
                    String string = iIChatComponent.getFormattedText();
                    int n = 0;
                    String string2 = string;
                    if (string2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    char[] cArray = string2.toCharArray();
                    int n2 = Integer.MAX_VALUE;
                    int n3 = cArray.length;
                    for (n = 0; n < n3; ++n) {
                        int n4;
                        if (cArray[n] != '\u00a7' || n + 1 >= cArray.length || (n4 = GameFontRenderer.Companion.getColorIndex(cArray[n + 1])) < 0 || n4 > 15) continue;
                        n2 = ColorUtils.hexColors[n4];
                        break;
                    }
                    return new Color(n2);
                }
            }
        }
        return (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent var1_1) {
        var2_2 = (String)this.modeValue.get();
        var3_3 = WorldToScreen.getMatrix(2982);
        var4_4 = WorldToScreen.getMatrix(2983);
        var5_5 = StringsKt.equals((String)var2_2, (String)"real2d", (boolean)true);
        if (var5_5) {
            GL11.glPushAttrib((int)8192);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glMatrixMode((int)5889);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho((double)0.0, (double)MinecraftInstance.mc.getDisplayWidth(), (double)MinecraftInstance.mc.getDisplayHeight(), (double)0.0, (double)-1.0, (double)1.0);
            GL11.glMatrixMode((int)5888);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glDisable((int)2929);
            GL11.glBlendFunc((int)770, (int)771);
            MinecraftInstance.classProvider.getGlStateManager().enableTexture2D();
            GL11.glDepthMask((boolean)true);
            GL11.glLineWidth((float)1.0f);
        }
        v0 = MinecraftInstance.mc.getTheWorld();
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity var6_7 : v0.getLoadedEntityList()) {
            block13: {
                if (!(var6_7.equals(MinecraftInstance.mc.getThePlayer()) ^ true) || !EntityUtils.isSelected(var6_7, false)) continue;
                var8_8 = var6_7.asEntityLivingBase();
                var9_9 = this.getColor(var8_8);
                var10_10 = var2_2;
                var11_11 = false;
                v1 = var10_10;
                if (v1 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var10_10 = v1.toLowerCase();
                switch (var10_10.hashCode()) {
                    case 1650: {
                        if (!var10_10.equals("2d")) ** break;
                        break;
                    }
                    case -934973296: {
                        if (!var10_10.equals("real2d")) ** break;
                        break block13;
                    }
                    case 97739: {
                        if (!var10_10.equals("box")) ** break;
                        ** GOTO lbl47
                    }
                    case -1171135301: {
                        if (!var10_10.equals("otherbox")) ** break;
lbl47:
                        // 2 sources

                        RenderUtils.drawEntityBox(var6_7, var9_9, StringsKt.equals((String)var2_2, (String)"otherbox", (boolean)true) == false);
                        ** break;
                    }
                }
                var11_12 = MinecraftInstance.mc.getRenderManager();
                var12_14 = MinecraftInstance.mc.getTimer();
                var13_15 = var8_8.getLastTickPosX() + (var8_8.getPosX() - var8_8.getLastTickPosX()) * (double)var12_14.getRenderPartialTicks() - var11_12.getRenderPosX();
                var15_18 = var8_8.getLastTickPosY() + (var8_8.getPosY() - var8_8.getLastTickPosY()) * (double)var12_14.getRenderPartialTicks() - var11_12.getRenderPosY();
                var17_21 = var8_8.getLastTickPosZ() + (var8_8.getPosZ() - var8_8.getLastTickPosZ()) * (double)var12_14.getRenderPartialTicks() - var11_12.getRenderPosZ();
                RenderUtils.draw2D(var8_8, var13_15, var15_18, var17_21, var9_9.getRGB(), Color.BLACK.getRGB());
                ** break;
            }
            var11_13 = MinecraftInstance.mc.getRenderManager();
            var12_14 = MinecraftInstance.mc.getTimer();
            var13_16 = var8_8.getEntityBoundingBox().offset(-var8_8.getPosX(), -var8_8.getPosY(), -var8_8.getPosZ()).offset(var8_8.getLastTickPosX() + (var8_8.getPosX() - var8_8.getLastTickPosX()) * (double)var12_14.getRenderPartialTicks(), var8_8.getLastTickPosY() + (var8_8.getPosY() - var8_8.getLastTickPosY()) * (double)var12_14.getRenderPartialTicks(), var8_8.getLastTickPosZ() + (var8_8.getPosZ() - var8_8.getLastTickPosZ()) * (double)var12_14.getRenderPartialTicks()).offset(-var11_13.getRenderPosX(), -var11_13.getRenderPosY(), -var11_13.getRenderPosZ());
            var14_17 = new double[][]{{var13_16.getMinX(), var13_16.getMinY(), var13_16.getMinZ()}, {var13_16.getMinX(), var13_16.getMaxY(), var13_16.getMinZ()}, {var13_16.getMaxX(), var13_16.getMaxY(), var13_16.getMinZ()}, {var13_16.getMaxX(), var13_16.getMinY(), var13_16.getMinZ()}, {var13_16.getMinX(), var13_16.getMinY(), var13_16.getMaxZ()}, {var13_16.getMinX(), var13_16.getMaxY(), var13_16.getMaxZ()}, {var13_16.getMaxX(), var13_16.getMaxY(), var13_16.getMaxZ()}, {var13_16.getMaxX(), var13_16.getMinY(), var13_16.getMaxZ()}};
            var15_19 = FloatCompanionObject.INSTANCE.getMAX_VALUE();
            var16_20 = FloatCompanionObject.INSTANCE.getMAX_VALUE();
            var17_22 = -1.0f;
            var18_23 = -1.0f;
            for (double[] var19_24 : var14_17) {
                if (WorldToScreen.worldToScreen(new Vector3f((float)var19_24[0], (float)var19_24[1], (float)var19_24[2]), var3_3, var4_4, MinecraftInstance.mc.getDisplayWidth(), MinecraftInstance.mc.getDisplayHeight()) == null) {
                    continue;
                }
                var15_19 = Math.min(var23_28.x, var15_19);
                var16_20 = Math.min(var23_28.y, var16_20);
                var17_22 = Math.max(var23_28.x, var17_22);
                var18_23 = Math.max(var23_28.y, var18_23);
            }
            if (!(var15_19 > (float)false || var16_20 > (float)false || var17_22 <= (float)MinecraftInstance.mc.getDisplayWidth()) && !(var18_23 <= (float)MinecraftInstance.mc.getDisplayWidth())) continue;
            GL11.glColor4f((float)((float)var9_9.getRed() / 255.0f), (float)((float)var9_9.getGreen() / 255.0f), (float)((float)var9_9.getBlue() / 255.0f), (float)1.0f);
            GL11.glBegin((int)2);
            GL11.glVertex2f((float)var15_19, (float)var16_20);
            GL11.glVertex2f((float)var15_19, (float)var18_23);
            GL11.glVertex2f((float)var17_22, (float)var18_23);
            GL11.glVertex2f((float)var17_22, (float)var16_20);
            GL11.glEnd();
            ** break;
lbl82:
            // 8 sources

        }
        if (var5_5) {
            GL11.glEnable((int)2929);
            GL11.glMatrixMode((int)5889);
            GL11.glPopMatrix();
            GL11.glMatrixMode((int)5888);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onRender2D(Render2DEvent render2DEvent) {
        Object object = (String)this.modeValue.get();
        boolean bl = false;
        String string = object;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string2 = string.toLowerCase();
        FramebufferShader framebufferShader = StringsKt.equals((String)string2, (String)"shaderoutline", (boolean)true) ? (FramebufferShader)OutlineShader.OUTLINE_SHADER : (FramebufferShader)(StringsKt.equals((String)string2, (String)"shaderglow", (boolean)true) ? GlowShader.GLOW_SHADER : null);
        if (framebufferShader == null) {
            return;
        }
        object = framebufferShader;
        ((FramebufferShader)object).startDraw(render2DEvent.getPartialTicks());
        renderNameTags = false;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity iEntity : iWorldClient.getLoadedEntityList()) {
            if (!EntityUtils.isSelected(iEntity, false)) continue;
            MinecraftInstance.mc.getRenderManager().renderEntityStatic(iEntity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
        }
        renderNameTags = true;
        float f = StringsKt.equals((String)string2, (String)"shaderoutline", (boolean)true) ? ((Number)this.shaderOutlineRadius.get()).floatValue() : (StringsKt.equals((String)string2, (String)"shaderglow", (boolean)true) ? ((Number)this.shaderGlowRadius.get()).floatValue() : 1.0f);
        ((FramebufferShader)object).stopDraw(this.getColor(null), f, 1.0f);
    }

    public ESP() {
        this.wireframeWidth = new FloatValue("WireFrame-Width", 2.0f, 0.5f, 5.0f);
        this.shaderOutlineRadius = new FloatValue("ShaderOutline-Radius", 1.35f, 1.0f, 2.0f);
        this.shaderGlowRadius = new FloatValue("ShaderGlow-Radius", 2.3f, 2.0f, 3.0f);
        this.colorRedValue = new IntegerValue("R", 255, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 255, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
        this.colorTeam = new BoolValue("Team", false);
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}

