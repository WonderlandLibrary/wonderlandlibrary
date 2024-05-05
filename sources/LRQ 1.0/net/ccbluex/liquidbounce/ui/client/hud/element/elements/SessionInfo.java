/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import jx.utils.ShadowUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="GameInfo")
public final class SessionInfo
extends Element {
    private final FloatValue radiusValue;
    private final FloatValue shadowValue;
    private final ListValue shadowColorMode;
    private final IntegerValue shadowColorRedValue;
    private final IntegerValue shadowColorGreenValue;
    private final IntegerValue shadowColorBlueValue;
    private double ms;
    private double health;
    private double speed;
    private double fps;

    @Override
    public Border drawElement() {
        int x2 = 114;
        int y2 = 100;
        long durationInMillis = System.currentTimeMillis() - LiquidBounce.INSTANCE.getPlayTimeStart();
        RenderUtils.drawRoundedRect(0.0f, 0.0f, (float)x2, (float)y2, ((Number)this.radiusValue.get()).floatValue(), new Color(32, 30, 30).getRGB());
        GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPushMatrix();
        ShadowUtils.INSTANCE.shadow(((Number)this.shadowValue.get()).floatValue(), (Function0<Unit>)((Function0)new Function0<Unit>(this, x2, y2){
            final /* synthetic */ SessionInfo this$0;
            final /* synthetic */ int $x2;
            final /* synthetic */ int $y2;

            public final void invoke() {
                GL11.glPushMatrix();
                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                RenderUtils.originalRoundedRect(0.0f, 0.0f, this.$x2, this.$y2, ((Number)SessionInfo.access$getRadiusValue$p(this.this$0).get()).floatValue(), StringsKt.equals((String)((String)SessionInfo.access$getShadowColorMode$p(this.this$0).get()), (String)"background", (boolean)true) ? new Color(32, 30, 30).getRGB() : new Color(((Number)SessionInfo.access$getShadowColorRedValue$p(this.this$0).get()).intValue(), ((Number)SessionInfo.access$getShadowColorGreenValue$p(this.this$0).get()).intValue(), ((Number)SessionInfo.access$getShadowColorBlueValue$p(this.this$0).get()).intValue()).getRGB());
                GL11.glPopMatrix();
            }
            {
                this.this$0 = sessionInfo;
                this.$x2 = n;
                this.$y2 = n2;
                super(0);
            }
        }), (Function0<Unit>)((Function0)new Function0<Unit>(this, x2, y2){
            final /* synthetic */ SessionInfo this$0;
            final /* synthetic */ int $x2;
            final /* synthetic */ int $y2;

            public final void invoke() {
                GL11.glPushMatrix();
                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderUtils.fastRoundedRect(0.0f, 0.0f, this.$x2, this.$y2, ((Number)SessionInfo.access$getRadiusValue$p(this.this$0).get()).floatValue());
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GL11.glPopMatrix();
            }
            {
                this.this$0 = sessionInfo;
                this.$x2 = n;
                this.$y2 = n2;
                super(0);
            }
        }));
        GL11.glPopMatrix();
        GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
        GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        Fonts.tenacitybold40.drawCenteredString("GameInfo", 57.0f, 5.0f, Color.WHITE.getRGB());
        RenderUtils.drawRect(5.0f, 80.0f, 109.0f, 85.0f, Color.WHITE.getRGB());
        RenderUtils.drawRect(5.0f, 17.0f, 109.0f, 19.0f, Color.WHITE.getRGB());
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        this.health = RangesKt.coerceAtLeast((double)RangesKt.coerceAtMost((double)RenderUtils.getAnimationState2(this.health, this.health / (double)iEntityPlayerSP.getMaxHealth() * (double)55.0f, 200.0), (double)55.0), (double)4.0);
        this.fps = RangesKt.coerceAtLeast((double)RangesKt.coerceAtMost((double)RenderUtils.getAnimationState2(this.fps, (float)MinecraftInstance.mc.getDebugFPS() / 300.0f * 55.0f, 200.0), (double)55.0), (double)4.0);
        this.ms = RangesKt.coerceAtLeast((double)RangesKt.coerceAtMost((double)RenderUtils.getAnimationState2(this.ms, (float)EntityUtils.getPing(MinecraftInstance.mc.getThePlayer()) / 50.0f * 55.0f, 200.0), (double)55.0), (double)4.0);
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        this.speed = RangesKt.coerceAtLeast((double)RangesKt.coerceAtMost((double)RenderUtils.getAnimationState2(this.speed, MovementUtils.INSTANCE.getBlockSpeed(iEntityPlayerSP2) / (double)20.0f * (double)55.0f, 200.0), (double)55.0), (double)4.0);
        Fonts.tenacitybold30.drawString("Health", 7.0f, 90.0f, Color.WHITE.getRGB());
        Fonts.tenacitybold30.drawString("Speed", 7.0f + (float)Fonts.tenacitybold30.getStringWidth("Health") + (float)10, 90.0f, Color.WHITE.getRGB());
        Fonts.tenacitybold30.drawString("FPS", 7.0f + (float)Fonts.tenacitybold30.getStringWidth("Health") + (float)Fonts.tenacitybold30.getStringWidth("Speed") + (float)20, 90.0f, Color.WHITE.getRGB());
        Fonts.tenacitybold30.drawString("MS", 7.0f + (float)Fonts.tenacitybold30.getStringWidth("Health") + (float)Fonts.tenacitybold30.getStringWidth("Speed") + (float)Fonts.tenacitybold30.getStringWidth("FPS") + (float)30, 90.0f, Color.WHITE.getRGB());
        RenderUtils.drawRoundedRect(10.0f, 77.0f, 15.0f, (float)((double)77.0f - this.health), 2.0f, new Color(0, 95, 255).getRGB());
        RenderUtils.drawRoundedRect(10.0f + (float)Fonts.tenacitybold30.getStringWidth("Health") + 10.0f, 77.0f, 10.0f + (float)Fonts.tenacitybold30.getStringWidth("Health") + 10.0f + 5.0f, (float)((double)77.0f - this.speed), 2.0f, new Color(0, 95, 255).getRGB());
        RenderUtils.drawRoundedRect(10.0f + (float)Fonts.tenacitybold30.getStringWidth("Health") + (float)Fonts.tenacitybold30.getStringWidth("Speed") + 20.0f, 77.0f, 10.0f + (float)Fonts.tenacitybold30.getStringWidth("Health") + (float)Fonts.tenacitybold30.getStringWidth("Speed") + 20.0f + 5.0f, (float)((double)77.0f - this.fps), 2.0f, new Color(0, 95, 255).getRGB());
        RenderUtils.drawRoundedRect(10.0f + (float)Fonts.tenacitybold30.getStringWidth("Health") + (float)Fonts.tenacitybold30.getStringWidth("Speed") + (float)Fonts.tenacitybold30.getStringWidth("FPS") + 30.0f, 77.0f, 10.0f + (float)Fonts.tenacitybold30.getStringWidth("Health") + (float)Fonts.tenacitybold30.getStringWidth("Speed") + (float)Fonts.tenacitybold30.getStringWidth("FPS") + 30.0f + 5.0f, (float)((double)77.0f - this.ms), 2.0f, new Color(0, 95, 255).getRGB());
        return new Border(0.0f, 0.0f, x2, y2);
    }

    public SessionInfo(double x, double y, float scale) {
        super(x, y, scale, null, 8, null);
        this.radiusValue = new FloatValue("Radius", 4.25f, 0.0f, 10.0f);
        this.shadowValue = new FloatValue("shadow-Value", 10.0f, 0.0f, 20.0f);
        this.shadowColorMode = new ListValue("Shadow-Color", new String[]{"Background", "Custom"}, "Background");
        this.shadowColorRedValue = new IntegerValue("Shadow-Red", 0, 0, 255);
        this.shadowColorGreenValue = new IntegerValue("Shadow-Green", 111, 0, 255);
        this.shadowColorBlueValue = new IntegerValue("Shadow-Blue", 255, 0, 255);
    }

    public /* synthetic */ SessionInfo(double d, double d2, float f, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 10.0;
        }
        if ((n & 2) != 0) {
            d2 = 10.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        this(d, d2, f);
    }

    public SessionInfo() {
        this(0.0, 0.0, 0.0f, 7, null);
    }

    public static final /* synthetic */ FloatValue access$getRadiusValue$p(SessionInfo $this) {
        return $this.radiusValue;
    }

    public static final /* synthetic */ ListValue access$getShadowColorMode$p(SessionInfo $this) {
        return $this.shadowColorMode;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorRedValue$p(SessionInfo $this) {
        return $this.shadowColorRedValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorGreenValue$p(SessionInfo $this) {
        return $this.shadowColorGreenValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorBlueValue$p(SessionInfo $this) {
        return $this.shadowColorBlueValue;
    }
}

