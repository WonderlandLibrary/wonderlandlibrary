/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Model")
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
    public Border drawElement() {
        block27: {
            var2_1 = (String)this.yawMode.get();
            var3_3 = false;
            v0 = var2_1;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            var2_1 = v0.toLowerCase();
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
                    v1 = MinecraftInstance.mc.getThePlayer();
                    if (v1 == null) {
                        Intrinsics.throwNpe();
                    }
                    v2 = v1.getRotationYaw();
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
            var3_4 = v3.toLowerCase();
            switch (var3_4.hashCode()) {
                case -1349088399: {
                    if (!var3_4.equals("custom")) ** break;
                    break;
                }
                case -985752863: {
                    if (!var3_4.equals("player")) ** break;
                    v4 = MinecraftInstance.mc.getThePlayer();
                    if (v4 == null) {
                        Intrinsics.throwNpe();
                    }
                    v5 = v4.getRotationPitch();
                    break block27;
                }
            }
            v5 = ((Number)this.customPitch.get()).floatValue();
            break block27;
            v5 = pitch = 0.0f;
        }
        if (pitch > (float)false) {
            v6 = -pitch;
        } else {
            var3_5 = false;
            v6 = Math.abs(pitch);
        }
        pitch = v6;
        v7 = MinecraftInstance.mc.getThePlayer();
        if (v7 == null) {
            Intrinsics.throwNpe();
        }
        this.drawEntityOnScreen(yaw, pitch, v7);
        return new Border(30.0f, 10.0f, -30.0f, -100.0f);
    }

    private final void drawEntityOnScreen(float yaw, float pitch, IEntityLivingBase entityLivingBase) {
        MinecraftInstance.classProvider.getGlStateManager().resetColor();
        MinecraftInstance.classProvider.getGlStateManager().enableColorMaterial();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)50.0f);
        GL11.glScalef((float)-50.0f, (float)50.0f, (float)50.0f);
        GL11.glRotatef((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float renderYawOffset = entityLivingBase.getRenderYawOffset();
        float rotationYaw = entityLivingBase.getRotationYaw();
        float rotationPitch = entityLivingBase.getRotationPitch();
        float prevRotationYawHead = entityLivingBase.getPrevRotationYawHead();
        float rotationYawHead = entityLivingBase.getRotationYawHead();
        GL11.glRotatef((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        MinecraftInstance.functions.enableStandardItemLighting();
        GL11.glRotatef((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f = pitch / 40.0f;
        boolean bl = false;
        GL11.glRotatef((float)(-((float)Math.atan(f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        f = yaw / 40.0f;
        IEntityLivingBase iEntityLivingBase = entityLivingBase;
        bl = false;
        float f2 = (float)Math.atan(f);
        iEntityLivingBase.setRenderYawOffset(f2 * 20.0f);
        f = yaw / 40.0f;
        iEntityLivingBase = entityLivingBase;
        bl = false;
        f2 = (float)Math.atan(f);
        iEntityLivingBase.setRotationYaw(f2 * 40.0f);
        f = pitch / 40.0f;
        iEntityLivingBase = entityLivingBase;
        bl = false;
        f2 = (float)Math.atan(f);
        iEntityLivingBase.setRotationPitch(-f2 * 20.0f);
        entityLivingBase.setRotationYawHead(entityLivingBase.getRotationYaw());
        entityLivingBase.setPrevRotationYawHead(entityLivingBase.getRotationYaw());
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)0.0f);
        IRenderManager renderManager = MinecraftInstance.mc.getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.renderEntityWithPosYaw(entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        renderManager.setRenderShadow(true);
        entityLivingBase.setRenderYawOffset(renderYawOffset);
        entityLivingBase.setRotationYaw(rotationYaw);
        entityLivingBase.setRotationPitch(rotationPitch);
        entityLivingBase.setPrevRotationYawHead(prevRotationYawHead);
        entityLivingBase.setRotationYawHead(rotationYawHead);
        GL11.glPopMatrix();
        MinecraftInstance.functions.disableStandardItemLighting();
        MinecraftInstance.classProvider.getGlStateManager().disableRescaleNormal();
        MinecraftInstance.functions.setActiveTextureLightMapTexUnit();
        MinecraftInstance.classProvider.getGlStateManager().disableTexture2D();
        MinecraftInstance.functions.setActiveTextureDefaultTexUnit();
        MinecraftInstance.classProvider.getGlStateManager().resetColor();
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
}

