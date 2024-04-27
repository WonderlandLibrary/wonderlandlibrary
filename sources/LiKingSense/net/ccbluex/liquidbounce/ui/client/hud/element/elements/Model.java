/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Model")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0010\u001a\u00020\u0011H\u0016J \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Model;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "(DD)V", "customPitch", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "customYaw", "pitchMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "rotate", "", "rotateDirection", "", "yawMode", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "drawEntityOnScreen", "", "yaw", "pitch", "entityLivingBase", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "LiKingSense"})
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
            Intrinsics.checkExpressionValueIsNotNull((Object)v1, (String)"(this as java.lang.String).toLowerCase()");
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
                    v2 = MinecraftInstance.mc.getThePlayer().getRotationYaw();
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
            Intrinsics.checkExpressionValueIsNotNull((Object)v4, (String)"(this as java.lang.String).toLowerCase()");
            var3_4 = v4;
            switch (var3_4.hashCode()) {
                case -1349088399: {
                    if (!var3_4.equals("custom")) ** break;
                    break;
                }
                case -985752863: {
                    if (!var3_4.equals("player")) ** break;
                    v5 = MinecraftInstance.mc.getThePlayer().getRotationPitch();
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
        this.drawEntityOnScreen(yaw, pitch, MinecraftInstance.mc.getThePlayer());
        return new Border(30.0f, 10.0f, -30.0f, -100.0f, 0.0f);
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
        Model.access$getFunctions$p$s1046033730().enableStandardItemLighting();
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
        Model.access$getFunctions$p$s1046033730().disableStandardItemLighting();
        MinecraftInstance.classProvider.getGlStateManager().disableRescaleNormal();
        Model.access$getFunctions$p$s1046033730().setActiveTextureLightMapTexUnit();
        MinecraftInstance.classProvider.getGlStateManager().disableTexture2D();
        Model.access$getFunctions$p$s1046033730().setActiveTextureDefaultTexUnit();
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

    public static final /* synthetic */ IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }
}

