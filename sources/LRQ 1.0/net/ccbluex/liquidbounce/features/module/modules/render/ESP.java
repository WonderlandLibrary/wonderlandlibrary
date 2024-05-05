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
 *  net.minecraft.util.ResourceLocation
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
import net.ccbluex.liquidbounce.utils.ClientUtils;
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
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

@ModuleInfo(name="ESP", description="Allows you to see targets through walls.", category=ModuleCategory.RENDER)
public final class ESP
extends Module {
    @JvmField
    public final ListValue modeValue = new ListValue("Mode", new String[]{"datou", "Box", "OtherBox", "WireFrame", "2D", "Real2D", "Outline", "ShaderOutline", "ShaderGlow"}, "Box");
    @JvmField
    public final FloatValue outlineWidth = new FloatValue("Outline-Width", 3.0f, 0.5f, 5.0f);
    @JvmField
    public final FloatValue wireframeWidth = new FloatValue("WireFrame-Width", 2.0f, 0.5f, 5.0f);
    private final FloatValue shaderOutlineRadius = new FloatValue("ShaderOutline-Radius", 1.35f, 1.0f, 2.0f);
    private final FloatValue shaderGlowRadius = new FloatValue("ShaderGlow-Radius", 2.3f, 2.0f, 3.0f);
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final ListValue datouValue = new ListValue("datou", new String[]{"yaoer", "caomou", "kaka"}, "kaka");
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final BoolValue colorTeam = new BoolValue("Team", false);
    @JvmField
    public static boolean renderNameTags;
    public static final Companion Companion;

    public final ListValue getDatouValue() {
        return this.datouValue;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent event) {
        mode = (String)this.modeValue.get();
        mvMatrix = WorldToScreen.getMatrix(2982);
        projectionMatrix = WorldToScreen.getMatrix(2983);
        real2d = StringsKt.equals((String)mode, (String)"real2d", (boolean)true);
        if (real2d) {
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
        for (IEntity entity : v0.getLoadedEntityList()) {
            block18: {
                block17: {
                    if (!(entity.equals(MinecraftInstance.mc.getThePlayer()) ^ true) || !EntityUtils.isSelected(entity, false)) continue;
                    entityLiving = entity.asEntityLivingBase();
                    color = this.getColor(entityLiving);
                    var10_10 = mode;
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
                        case 95356861: {
                            if (!var10_10.equals("datou")) ** break;
                            break block17;
                        }
                        case -934973296: {
                            if (!var10_10.equals("real2d")) ** break;
                            break block18;
                        }
                        case 97739: {
                            if (!var10_10.equals("box")) ** break;
                            ** GOTO lbl50
                        }
                        case -1171135301: {
                            if (!var10_10.equals("otherbox")) ** break;
lbl50:
                            // 2 sources

                            RenderUtils.drawEntityBox(entity, color, StringsKt.equals((String)mode, (String)"otherbox", (boolean)true) == false);
                            ** break;
                        }
                    }
                    renderManager = MinecraftInstance.mc.getRenderManager();
                    timer = MinecraftInstance.mc.getTimer();
                    posX = entityLiving.getLastTickPosX() + (entityLiving.getPosX() - entityLiving.getLastTickPosX()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosX();
                    posY = entityLiving.getLastTickPosY() + (entityLiving.getPosY() - entityLiving.getLastTickPosY()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosY();
                    posZ = entityLiving.getLastTickPosZ() + (entityLiving.getPosZ() - entityLiving.getLastTickPosZ()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosZ();
                    RenderUtils.draw2D(entityLiving, posX, posY, posZ, color.getRGB(), Color.BLACK.getRGB());
                    ** break;
                }
                pX = entity.getLastTickPosX() + (entity.getPosX() - entity.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosX();
                pY = entity.getLastTickPosY() + (entity.getPosY() - entity.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosY();
                pZ = entity.getLastTickPosZ() + (entity.getPosZ() - entity.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosZ();
                GL11.glPushMatrix();
                GL11.glTranslatef((float)((float)pX), (float)((float)(pY + (double)(entity.isSneaking() != false ? 0.8f : 1.3f))), (float)((float)pZ));
                GL11.glNormal3f((float)1.0f, (float)1.0f, (float)1.0f);
                MinecraftInstance.mc2.func_175598_ae();
                GL11.glRotatef((float)(-MinecraftInstance.mc.getRenderManager().getPlayerViewY()), (float)0.0f, (float)1.0f, (float)0.0f);
                MinecraftInstance.mc2.func_175598_ae();
                GL11.glRotatef((float)MinecraftInstance.mc.getRenderManager().getPlayerViewX(), (float)1.0f, (float)0.0f, (float)0.0f);
                scale = 0.06f;
                GL11.glScalef((float)(-scale), (float)(-scale), (float)scale);
                GL11.glDisable((int)2896);
                GL11.glDisable((int)2929);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glPushMatrix();
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                if (((String)this.datouValue.get()).equals("yaoer")) {
                    RenderUtils.image(new ResourceLocation("liquidbounce/yaoer.png"), -8, -14, 16, 16);
                }
                if (((String)this.datouValue.get()).equals("caomou")) {
                    RenderUtils.image(new ResourceLocation("liquidbounce/caomou.png"), -8, -15, 16, 16);
                }
                if (((String)this.datouValue.get()).equals("kaka")) {
                    RenderUtils.image(new ResourceLocation("liquidbounce/kaka.png"), -8, -15, 16, 16);
                }
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                ** break;
            }
            renderManager = MinecraftInstance.mc.getRenderManager();
            timer = MinecraftInstance.mc.getTimer();
            bb = entityLiving.getEntityBoundingBox().offset(-entityLiving.getPosX(), -entityLiving.getPosY(), -entityLiving.getPosZ()).offset(entityLiving.getLastTickPosX() + (entityLiving.getPosX() - entityLiving.getLastTickPosX()) * (double)timer.getRenderPartialTicks(), entityLiving.getLastTickPosY() + (entityLiving.getPosY() - entityLiving.getLastTickPosY()) * (double)timer.getRenderPartialTicks(), entityLiving.getLastTickPosZ() + (entityLiving.getPosZ() - entityLiving.getLastTickPosZ()) * (double)timer.getRenderPartialTicks()).offset(-renderManager.getRenderPosX(), -renderManager.getRenderPosY(), -renderManager.getRenderPosZ());
            boxVertices = new double[][]{{bb.getMinX(), bb.getMinY(), bb.getMinZ()}, {bb.getMinX(), bb.getMaxY(), bb.getMinZ()}, {bb.getMaxX(), bb.getMaxY(), bb.getMinZ()}, {bb.getMaxX(), bb.getMinY(), bb.getMinZ()}, {bb.getMinX(), bb.getMinY(), bb.getMaxZ()}, {bb.getMinX(), bb.getMaxY(), bb.getMaxZ()}, {bb.getMaxX(), bb.getMaxY(), bb.getMaxZ()}, {bb.getMaxX(), bb.getMinY(), bb.getMaxZ()}};
            minX = FloatCompanionObject.INSTANCE.getMAX_VALUE();
            minY = FloatCompanionObject.INSTANCE.getMAX_VALUE();
            maxX = -1.0f;
            maxY = -1.0f;
            for (double[] boxVertex : boxVertices) {
                if (WorldToScreen.worldToScreen(new Vector3f((float)boxVertex[0], (float)boxVertex[1], (float)boxVertex[2]), mvMatrix, projectionMatrix, MinecraftInstance.mc.getDisplayWidth(), MinecraftInstance.mc.getDisplayHeight()) == null) {
                    continue;
                }
                minX = Math.min(screenPos.x, minX);
                minY = Math.min(screenPos.y, minY);
                maxX = Math.max(screenPos.x, maxX);
                maxY = Math.max(screenPos.y, maxY);
            }
            if (!(minX > (float)false || minY > (float)false || maxX <= (float)MinecraftInstance.mc.getDisplayWidth()) && !(maxY <= (float)MinecraftInstance.mc.getDisplayWidth())) continue;
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
            GL11.glBegin((int)2);
            GL11.glVertex2f((float)minX, (float)minY);
            GL11.glVertex2f((float)minX, (float)maxY);
            GL11.glVertex2f((float)maxX, (float)maxY);
            GL11.glVertex2f((float)maxX, (float)minY);
            GL11.glEnd();
lbl114:
            // 10 sources

        }
        if (real2d) {
            GL11.glEnable((int)2929);
            GL11.glMatrixMode((int)5889);
            GL11.glPopMatrix();
            GL11.glMatrixMode((int)5888);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    @EventTarget
    public final void onRender2D(Render2DEvent event) {
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String mode = string2.toLowerCase();
        FramebufferShader framebufferShader = StringsKt.equals((String)mode, (String)"shaderoutline", (boolean)true) ? (FramebufferShader)OutlineShader.OUTLINE_SHADER : (FramebufferShader)(StringsKt.equals((String)mode, (String)"shaderglow", (boolean)true) ? GlowShader.GLOW_SHADER : null);
        if (framebufferShader == null) {
            return;
        }
        FramebufferShader shader = framebufferShader;
        shader.startDraw(event.getPartialTicks());
        renderNameTags = false;
        try {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            for (IEntity entity : iWorldClient.getLoadedEntityList()) {
                if (!EntityUtils.isSelected(entity, false)) continue;
                MinecraftInstance.mc.getRenderManager().renderEntityStatic(entity, MinecraftInstance.mc.getTimer().getRenderPartialTicks(), true);
            }
        }
        catch (Exception ex) {
            ClientUtils.getLogger().error("An error occurred while rendering all entities for shader esp", (Throwable)ex);
        }
        renderNameTags = true;
        float radius = StringsKt.equals((String)mode, (String)"shaderoutline", (boolean)true) ? ((Number)this.shaderOutlineRadius.get()).floatValue() : (StringsKt.equals((String)mode, (String)"shaderglow", (boolean)true) ? ((Number)this.shaderGlowRadius.get()).floatValue() : 1.0f);
        shader.stopDraw(this.getColor(null), radius, 1.0f);
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    /*
     * WARNING - void declaration
     */
    public final Color getColor(@Nullable IEntity entity) {
        ESP eSP = this;
        boolean bl = false;
        boolean bl2 = false;
        ESP $this$run = eSP;
        boolean bl3 = false;
        if (entity != null && MinecraftInstance.classProvider.isEntityLivingBase(entity)) {
            IEntityLivingBase entityLivingBase = entity.asEntityLivingBase();
            if (entityLivingBase.getHurtTime() > 0) {
                return Color.RED;
            }
            if (EntityUtils.isFriend(entityLivingBase)) {
                return Color.BLUE;
            }
            if (((Boolean)$this$run.colorTeam.get()).booleanValue()) {
                IIChatComponent iIChatComponent = entityLivingBase.getDisplayName();
                if (iIChatComponent == null) {
                } else {
                    String string = iIChatComponent.getFormattedText();
                    int n = 0;
                    String string2 = string;
                    if (string2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    char[] chars = string2.toCharArray();
                    int color = Integer.MAX_VALUE;
                    n = 0;
                    int n2 = chars.length;
                    while (n < n2) {
                        int index;
                        void i;
                        if (chars[i] == '\u00a7' && i + true < chars.length && (index = GameFontRenderer.Companion.getColorIndex(chars[i + true])) >= 0 && index <= 15) {
                            color = ColorUtils.hexColors[index];
                            break;
                        }
                        ++i;
                    }
                    return new Color(color);
                }
            }
        }
        return (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
    }

    static {
        Companion = new Companion(null);
        renderNameTags = true;
    }

    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

