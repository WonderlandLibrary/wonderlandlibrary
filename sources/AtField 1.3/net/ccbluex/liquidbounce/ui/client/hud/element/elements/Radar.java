/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Vector2f
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

@ElementInfo(name="NewRadar")
public final class Radar
extends Element {
    private final ListValue playerShapeValue;
    private final FloatValue viewDistanceValue;
    private final FloatValue fovSizeValue;
    private final FloatValue sizeValue;
    private final FloatValue playerSizeValue;
    private static final float SQRT_OF_TWO;
    private final BoolValue shadowValue;
    public static final Companion Companion;

    public Radar(double d, double d2) {
        super(d, d2, 0.0f, null, 12, null);
        this.sizeValue = new FloatValue("Size", 90.0f, 30.0f, 500.0f);
        this.viewDistanceValue = new FloatValue("View Distance", 4.0f, 0.5f, 32.0f);
        this.playerShapeValue = new ListValue("Player Shape", new String[]{"Rectangle", "Circle"}, "Circle");
        this.playerSizeValue = new FloatValue("Player Size", 5.0f, 0.5f, 20.0f);
        this.fovSizeValue = new FloatValue("FOV Size", 10.0f, 0.0f, 50.0f);
        this.shadowValue = new BoolValue("Shadow", false);
    }

    @Override
    public Border drawElement() {
        Color color = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color3 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        Color color4 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        IEntity iEntity = MinecraftInstance.mc.getRenderViewEntity();
        float f = ((Number)this.sizeValue.get()).floatValue();
        float f2 = ((Number)this.viewDistanceValue.get()).floatValue() * 16.0f;
        double d = ((double)f2 + (double)((Number)this.fovSizeValue.get()).floatValue()) * ((double)f2 + (double)((Number)this.fovSizeValue.get()).floatValue());
        float f3 = f / 2.0f;
        RoundedUtil.drawGradientRound(0.0f, 0.0f, f, f, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
        if (((Boolean)this.shadowValue.get()).booleanValue()) {
            RenderUtils.drawShadowWithCustomAlpha(0.0f, 0.0f, f, f, 255.0f);
        }
        float f4 = (float)this.getX();
        float f5 = (float)this.getY();
        float f6 = (float)this.getX();
        boolean bl = false;
        float f7 = (float)Math.ceil(f);
        float f8 = f4 + f7;
        f7 = (float)this.getY();
        f4 = f8;
        bl = false;
        float f9 = (float)Math.ceil(f);
        RenderUtils.makeScissorBox(f6, f5, f4, f7 + f9);
        GL11.glEnable((int)3089);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)f3, (float)f3, (float)0.0f);
        IEntity iEntity2 = iEntity;
        if (iEntity2 == null) {
            Intrinsics.throwNpe();
        }
        GL11.glRotatef((float)iEntity2.getRotationYaw(), (float)0.0f, (float)0.0f, (float)-1.0f);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        bl = StringsKt.equals((String)((String)this.playerShapeValue.get()), (String)"circle", (boolean)true);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferBuilder = tessellator.func_178180_c();
        if (bl) {
            GL11.glEnable((int)2832);
        }
        float f10 = ((Number)this.playerSizeValue.get()).floatValue();
        GL11.glEnable((int)2881);
        bufferBuilder.func_181668_a(0, DefaultVertexFormats.field_181706_f);
        GL11.glPointSize((float)f10);
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity iEntity3 : iWorldClient.getLoadedEntityList()) {
            boolean bl2;
            Vector2f vector2f;
            if (iEntity3 == null || iEntity3 == MinecraftInstance.mc.getThePlayer() || !EntityUtils.isSelected(iEntity3, false) || d < (double)(vector2f = new Vector2f((float)(iEntity.getPosX() - iEntity3.getPosX()), (float)(iEntity.getPosZ() - iEntity3.getPosZ()))).lengthSquared()) continue;
            boolean bl3 = bl2 = ((Number)this.fovSizeValue.get()).floatValue() > 0.0f;
            if (bl2) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(vector2f.x / f2 * f), (float)(vector2f.y / f2 * f), (float)0.0f);
                GL11.glRotatef((float)iEntity3.getRotationYaw(), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(ESP.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.ESP");
            }
            Color color5 = ((ESP)module).getColor(iEntity3);
            bufferBuilder.func_181662_b((double)(vector2f.x / f2 * f), (double)(vector2f.y / f2 * f), 0.0).func_181666_a((float)color5.getRed() / 255.0f, (float)color5.getGreen() / 255.0f, (float)color5.getBlue() / 255.0f, 1.0f).func_181675_d();
            if (!bl2) continue;
            GL11.glPopMatrix();
        }
        tessellator.func_78381_a();
        if (bl) {
            GL11.glDisable((int)2832);
        }
        GL11.glDisable((int)2881);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        return new Border(0.0f, 0.0f, f, f);
    }

    static {
        Companion = new Companion(null);
        float f = 2.0f;
        boolean bl = false;
        SQRT_OF_TWO = (float)Math.sqrt(f);
    }

    public Radar() {
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }

    public Radar(double d, double d2, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 5.0;
        }
        if ((n & 2) != 0) {
            d2 = 130.0;
        }
        this(d, d2);
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}

