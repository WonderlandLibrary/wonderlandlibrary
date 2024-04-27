package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FloatCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.ITimer;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

@ModuleInfo(name="ESP", description="Allows you to see targets through walls.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000R\n\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\b\u0000 20:B¢J02\b0J020HJ02\b0HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0X¢\n\u0000R\n08X¢\n\u0000R\f0\r8X¢\n\u0000R0\rX¢\n\u0000R0\rX¢\n\u0000R08VX¢\bR0\r8X¢\n\u0000¨ "}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/ESP;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "colorBlueValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "colorGreenValue", "colorRainbow", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "colorRedValue", "colorTeam", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "outlineWidth", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "shaderGlowRadius", "shaderOutlineRadius", "tag", "", "getTag", "()Ljava/lang/String;", "wireframeWidth", "getColor", "Ljava/awt/Color;", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "onRender2D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "Companion", "Pride"})
public final class ESP
extends Module {
    @JvmField
    @NotNull
    public final ListValue modeValue = new ListValue("Mode", new String[]{"BigGod", "Box", "OtherBox", "WireFrame", "2D", "Real2D", "Outline", "ShaderOutline", "ShaderGlow"}, "Box");
    @JvmField
    @NotNull
    public final FloatValue outlineWidth = new FloatValue("Outline-Width", 3.0f, 0.5f, 5.0f);
    @JvmField
    @NotNull
    public final FloatValue wireframeWidth = new FloatValue("WireFrame-Width", 2.0f, 0.5f, 5.0f);
    private final FloatValue shaderOutlineRadius = new FloatValue("ShaderOutline-Radius", 1.35f, 1.0f, 2.0f);
    private final FloatValue shaderGlowRadius = new FloatValue("ShaderGlow-Radius", 2.3f, 2.0f, 3.0f);
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final BoolValue colorTeam = new BoolValue("Team", false);
    @JvmField
    public static boolean renderNameTags;
    public static final Companion Companion;

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent event) {
        String mode = (String)this.modeValue.get();
        Matrix4f mvMatrix = WorldToScreen.getMatrix(2982);
        Matrix4f projectionMatrix = WorldToScreen.getMatrix(2983);
        boolean real2d = StringsKt.equals(mode, "real2d", true);
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
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity entity : iWorldClient.getLoadedEntityList()) {
            if (!(Intrinsics.areEqual(entity, MinecraftInstance.mc.getThePlayer()) ^ true) || !EntityUtils.isSelected(entity, false)) continue;
            IEntityLivingBase entityLiving = entity.asEntityLivingBase();
            Color color = this.getColor(entityLiving);
            String string = mode;
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "box": 
                case "otherbox": {
                    RenderUtils.drawEntityBox(entity, color, !StringsKt.equals(mode, "otherbox", true));
                    break;
                }
                case "2d": {
                    IRenderManager renderManager = MinecraftInstance.mc.getRenderManager();
                    ITimer timer = MinecraftInstance.mc.getTimer();
                    double posX = entityLiving.getLastTickPosX() + (entityLiving.getPosX() - entityLiving.getLastTickPosX()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosX();
                    double posY = entityLiving.getLastTickPosY() + (entityLiving.getPosY() - entityLiving.getLastTickPosY()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosY();
                    double posZ = entityLiving.getLastTickPosZ() + (entityLiving.getPosZ() - entityLiving.getLastTickPosZ()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosZ();
                    int n = color.getRGB();
                    Color color2 = Color.BLACK;
                    Intrinsics.checkExpressionValueIsNotNull(color2, "Color.BLACK");
                    RenderUtils.draw2D(entityLiving, posX, posY, posZ, n, color2.getRGB());
                    break;
                }
                case "biggod": {
                    double var10000 = entityLiving.getLastTickPosX() + (entityLiving.getPosX() - entityLiving.getLastTickPosX()) * (Double)((Object)Float.valueOf(MinecraftInstance.mc.getTimer().getRenderPartialTicks()));
                    MinecraftInstance.mc.getRenderManager();
                    double pX = var10000 - MinecraftInstance.mc.getRenderManager().getRenderPosX();
                    var10000 = entityLiving.getLastTickPosY() + (entityLiving.getPosY() - entityLiving.getLastTickPosY()) * (Double)((Object)Float.valueOf(MinecraftInstance.mc.getTimer().getRenderPartialTicks()));
                    MinecraftInstance.mc.getRenderManager();
                    double pY = var10000 - MinecraftInstance.mc.getRenderManager().getRenderPosY();
                    var10000 = entityLiving.getLastTickPosZ() + (entityLiving.getPosZ() - entityLiving.getLastTickPosZ()) * (Double)((Object)Float.valueOf(MinecraftInstance.mc.getTimer().getRenderPartialTicks()));
                    MinecraftInstance.mc.getRenderManager();
                    double pZ = var10000 - MinecraftInstance.mc.getRenderManager().getRenderPosZ();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)((float)pX), (float)((float)pY + (entityLiving.isSneaking() ? 0.8f : 1.3f)), (float)((float)pZ));
                    GL11.glNormal3f((float)1.0f, (float)1.0f, (float)1.0f);
                    GL11.glRotatef((float)(-MinecraftInstance.mc.getRenderManager().getPlayerViewY()), (float)0.0f, (float)1.0f, (float)0.0f);
                    GL11.glRotatef((float)MinecraftInstance.mc.getRenderManager().getPlayerViewX(), (float)1.0f, (float)0.0f, (float)0.0f);
                    float scale = 0.06f;
                    GL11.glScalef((float)(-scale), (float)(-scale), (float)scale);
                    GL11.glDisable((int)2896);
                    GL11.glDisable((int)2929);
                    GL11.glEnable((int)3042);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glPushMatrix();
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    RenderUtils.drawImage(MinecraftInstance.classProvider.createResourceLocation("pride/sweatsoy.png"), (int)-8.0, (int)-14.0, 16, 16);
                    GL11.glPopMatrix();
                    GL11.glPopMatrix();
                    break;
                }
                case "real2d": {
                    IRenderManager renderManager = MinecraftInstance.mc.getRenderManager();
                    ITimer timer = MinecraftInstance.mc.getTimer();
                    IAxisAlignedBB bb = entityLiving.getEntityBoundingBox().offset(-entityLiving.getPosX(), -entityLiving.getPosY(), -entityLiving.getPosZ()).offset(entityLiving.getLastTickPosX() + (entityLiving.getPosX() - entityLiving.getLastTickPosX()) * (double)timer.getRenderPartialTicks(), entityLiving.getLastTickPosY() + (entityLiving.getPosY() - entityLiving.getLastTickPosY()) * (double)timer.getRenderPartialTicks(), entityLiving.getLastTickPosZ() + (entityLiving.getPosZ() - entityLiving.getLastTickPosZ()) * (double)timer.getRenderPartialTicks()).offset(-renderManager.getRenderPosX(), -renderManager.getRenderPosY(), -renderManager.getRenderPosZ());
                    double[][] boxVertices = new double[][]{{bb.getMinX(), bb.getMinY(), bb.getMinZ()}, {bb.getMinX(), bb.getMaxY(), bb.getMinZ()}, {bb.getMaxX(), bb.getMaxY(), bb.getMinZ()}, {bb.getMaxX(), bb.getMinY(), bb.getMinZ()}, {bb.getMinX(), bb.getMinY(), bb.getMaxZ()}, {bb.getMinX(), bb.getMaxY(), bb.getMaxZ()}, {bb.getMaxX(), bb.getMaxY(), bb.getMaxZ()}, {bb.getMaxX(), bb.getMinY(), bb.getMaxZ()}};
                    float minX = FloatCompanionObject.INSTANCE.getMAX_VALUE();
                    float minY = FloatCompanionObject.INSTANCE.getMAX_VALUE();
                    float maxX = -1.0f;
                    float maxY = -1.0f;
                    for (double[] boxVertex : boxVertices) {
                        Vector2f screenPos;
                        if (WorldToScreen.worldToScreen(new Vector3f((float)boxVertex[0], (float)boxVertex[1], (float)boxVertex[2]), mvMatrix, projectionMatrix, MinecraftInstance.mc.getDisplayWidth(), MinecraftInstance.mc.getDisplayHeight()) == null) {
                            continue;
                        }
                        minX = Math.min(screenPos.x, minX);
                        minY = Math.min(screenPos.y, minY);
                        maxX = Math.max(screenPos.x, maxX);
                        maxY = Math.max(screenPos.y, maxY);
                    }
                    if (!(minX > 0.0f || minY > 0.0f || maxX <= (float)MinecraftInstance.mc.getDisplayWidth()) && !(maxY <= (float)MinecraftInstance.mc.getDisplayWidth())) break;
                    GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
                    GL11.glBegin((int)2);
                    GL11.glVertex2f((float)minX, (float)minY);
                    GL11.glVertex2f((float)minX, (float)maxY);
                    GL11.glVertex2f((float)maxX, (float)maxY);
                    GL11.glVertex2f((float)maxX, (float)minY);
                    GL11.glEnd();
                    break;
                }
            }
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
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        String mode = string3;
        FramebufferShader framebufferShader = StringsKt.equals(mode, "shaderoutline", true) ? (FramebufferShader)OutlineShader.OUTLINE_SHADER : (FramebufferShader)(StringsKt.equals(mode, "shaderglow", true) ? GlowShader.GLOW_SHADER : null);
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
        float radius = StringsKt.equals(mode, "shaderoutline", true) ? ((Number)this.shaderOutlineRadius.get()).floatValue() : (StringsKt.equals(mode, "shaderglow", true) ? ((Number)this.shaderGlowRadius.get()).floatValue() : 1.0f);
        shader.stopDraw(this.getColor(null), radius, 1.0f);
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final Color getColor(@Nullable IEntity entity) {
        ESP eSP = this;
        boolean bl = false;
        boolean bl2 = false;
        ESP $this$run = eSP;
        boolean bl3 = false;
        if (entity != null && MinecraftInstance.classProvider.isEntityLivingBase(entity)) {
            IEntityLivingBase entityLivingBase = entity.asEntityLivingBase();
            if (entityLivingBase.getHurtTime() > 0) {
                Color color = Color.RED;
                Intrinsics.checkExpressionValueIsNotNull(color, "Color.RED");
                return color;
            }
            if (EntityUtils.INSTANCE.isFriend(entityLivingBase)) {
                Color color = Color.BLUE;
                Intrinsics.checkExpressionValueIsNotNull(color, "Color.BLUE");
                return color;
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
                    char[] cArray = string2.toCharArray();
                    Intrinsics.checkExpressionValueIsNotNull(cArray, "(this as java.lang.String).toCharArray()");
                    char[] chars = cArray;
                    int color = Integer.MAX_VALUE;
                    n = 0;
                    int n2 = chars.length;
                    while (n < n2) {
                        int index;
                        void i;
                        if (chars[i] == '§' && i + true < chars.length && (index = GameFontRenderer.Companion.getColorIndex(chars[i + true])) >= 0 && index <= 15) {
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\u0000\b\u000020B\b¢R08@X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/ESP$Companion;", "", "()V", "renderNameTags", "", "Pride"})
    public static final class Companion {
        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
