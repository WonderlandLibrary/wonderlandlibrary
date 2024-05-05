/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.text.StringsKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import jx.utils.CFont.FontLoaders;
import jx.utils.Recorder;
import jx.utils.ShadowUtils;
import jx.utils.render.DrawArc;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="GameInfo2")
public final class SessionInfo2
extends Element {
    private final FloatValue radiusValue;
    private final FloatValue shadowValue;
    private final ListValue shadowColorMode;
    private final IntegerValue shadowColorRedValue;
    private final IntegerValue shadowColorGreenValue;
    private final IntegerValue shadowColorBlueValue;
    private long minute;
    private long second;
    private final MSTimer time2;
    private final MSTimer time1;

    public final long getMinute() {
        return this.minute;
    }

    public final void setMinute(long l) {
        this.minute = l;
    }

    public final long getSecond() {
        return this.second;
    }

    public final void setSecond(long l) {
        this.second = l;
    }

    public final MSTimer getTime2() {
        return this.time2;
    }

    public final MSTimer getTime1() {
        return this.time1;
    }

    @Override
    public Border drawElement() {
        double x2 = 145.0;
        double y2 = (double)(Fonts.font35.getFontHeight() * 5 + 18) * 1.15;
        long durationInMillis = System.currentTimeMillis() - LiquidBounce.INSTANCE.getPlayTimeStart();
        long hour = durationInMillis / (long)3600000 % (long)24;
        float arc = 0.0f;
        if (this.time2.hasTimePassed(1000L)) {
            if (this.second == 59L) {
                ++this.minute;
            }
            ++this.second;
            this.time2.reset();
        }
        if (this.second > 60L) {
            this.second = 0L;
        }
        if (this.minute != 59L) {
            arc = this.minute != 0L ? (float)(360.0 / (double)(59L / this.minute)) : 0.0f;
        } else {
            this.minute = 0L;
            this.second = 0L;
        }
        RenderUtils.drawRoundedRect(-4.0f, 0.0f, (float)x2, (float)y2, ((Number)this.radiusValue.get()).floatValue(), new Color(32, 30, 30).getRGB());
        GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPushMatrix();
        ShadowUtils.INSTANCE.shadow(((Number)this.shadowValue.get()).floatValue(), (Function0<Unit>)((Function0)new Function0<Unit>(this, x2, y2){
            final /* synthetic */ SessionInfo2 this$0;
            final /* synthetic */ double $x2;
            final /* synthetic */ double $y2;

            public final void invoke() {
                GL11.glPushMatrix();
                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                RenderUtils.originalRoundedRect(-2.0f, 0.0f, (float)this.$x2, (float)this.$y2, ((Number)SessionInfo2.access$getRadiusValue$p(this.this$0).get()).floatValue(), StringsKt.equals((String)((String)SessionInfo2.access$getShadowColorMode$p(this.this$0).get()), (String)"background", (boolean)true) ? new Color(32, 30, 30).getRGB() : new Color(((Number)SessionInfo2.access$getShadowColorRedValue$p(this.this$0).get()).intValue(), ((Number)SessionInfo2.access$getShadowColorGreenValue$p(this.this$0).get()).intValue(), ((Number)SessionInfo2.access$getShadowColorBlueValue$p(this.this$0).get()).intValue()).getRGB());
                GL11.glPopMatrix();
            }
            {
                this.this$0 = sessionInfo2;
                this.$x2 = d;
                this.$y2 = d2;
                super(0);
            }
        }), (Function0<Unit>)((Function0)new Function0<Unit>(this, x2, y2){
            final /* synthetic */ SessionInfo2 this$0;
            final /* synthetic */ double $x2;
            final /* synthetic */ double $y2;

            public final void invoke() {
                GL11.glPushMatrix();
                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderUtils.fastRoundedRect(-2.0f, 0.0f, (float)this.$x2, (float)this.$y2, ((Number)SessionInfo2.access$getRadiusValue$p(this.this$0).get()).floatValue());
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GL11.glPopMatrix();
            }
            {
                this.this$0 = sessionInfo2;
                this.$x2 = d;
                this.$y2 = d2;
                super(0);
            }
        }));
        GL11.glPopMatrix();
        GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
        GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        FontLoaders.tenacitybold20.drawString("Session", 2.0f, 2.5f, Color.WHITE.getRGB());
        FontLoaders.tenacitybold20.drawString("Play Time", (float)x2 - 2.0f - (float)FontLoaders.tenacity22.getStringWidth("Play Time"), 2.5f, Color.WHITE.getRGB());
        FontLoaders.tenacitybold18.drawCenteredString("" + this.minute + ':' + this.second, (double)((float)x2 - 2.0f) - (double)((float)FontLoaders.tenacity22.getStringWidth("Play Time") / 2.0f), (double)((float)(y2 / (double)2.0f - (double)2.0f)) + (double)3.0f, Color.WHITE.getRGB());
        RenderUtils.drawRect((float)x2 - 2.0f - (float)FontLoaders.tenacity22.getStringWidth("Play Time"), (float)((double)((float)Fonts.font35.getFontHeight() + 2.5f) + 0.0), (float)x2 - 2.0f, (float)Fonts.font35.getFontHeight() + 2.5f + 1.16f, Color.WHITE.getRGB());
        DrawArc.drawArc((float)x2 - 2.0f - (float)FontLoaders.tenacity22.getStringWidth("Play Time") / 2.0f, (float)(y2 / (double)2.0f) + 3.0f, 22.0, Color.WHITE.getRGB(), 0, 360.0, 6.0f);
        DrawArc.drawArc((float)x2 - 2.0f - (float)FontLoaders.tenacity22.getStringWidth("Play Time") / 2.0f, (float)(y2 / (double)2.0f) + 3.0f, 22.0, new Color(0, 95, 255).getRGB(), 0, arc, 6.0f);
        RenderUtils.drawRect(2.0f, (float)((double)((float)Fonts.font35.getFontHeight() + 2.5f) + 0.0), 2.0f + (float)FontLoaders.tenacity22.getStringWidth("Session"), (float)Fonts.font35.getFontHeight() + 2.5f + 1.16f, Color.WHITE.getRGB());
        Fonts.font35.drawStringWithShadow("Players Killed: ", 2, (int)((double)(Fonts.font35.getFontHeight() * 2) * 1.15 + (double)3.0f), Color.WHITE.getRGB());
        Fonts.font35.drawStringWithShadow(String.valueOf(Recorder.INSTANCE.getKillCounts()), (int)(4.0f + (float)Fonts.font35.getStringWidth("Players Killed: ".toString())), (int)((double)(Fonts.font35.getFontHeight() * 2) * 1.15 + (double)4.0f), Color.WHITE.getRGB());
        Fonts.font35.drawStringWithShadow("Win: ", 2, (int)((double)(Fonts.font35.getFontHeight() * 3) * 1.15 + (double)3.0f + (double)4.0f), Color.WHITE.getRGB());
        Fonts.font35.drawStringWithShadow(String.valueOf(Recorder.INSTANCE.getWin()), (int)(4.0f + (float)Fonts.font35.getStringWidth("Win: ".toString())), (int)((double)(Fonts.font35.getFontHeight() * 3) * 1.15 + (double)8.0f), Color.WHITE.getRGB());
        Fonts.font35.drawStringWithShadow("Total: ", 2, (int)((double)(Fonts.font35.getFontHeight() * 4) * 1.15 + (double)3.0f + (double)8.0f), Color.WHITE.getRGB());
        Fonts.font35.drawStringWithShadow(String.valueOf(Recorder.INSTANCE.getTotalPlayed()), (int)(4.0f + (float)Fonts.font35.getStringWidth("Total: ".toString())), (int)((double)(Fonts.font35.getFontHeight() * 4) * 1.15 + (double)12.0f), Color.WHITE.getRGB());
        return new Border(-2.0f, 0.0f, (float)x2, (float)y2);
    }

    public SessionInfo2(double x, double y, float scale) {
        super(x, y, scale, null, 8, null);
        this.radiusValue = new FloatValue("Radius", 4.25f, 0.0f, 10.0f);
        this.shadowValue = new FloatValue("shadow-Value", 10.0f, 0.0f, 20.0f);
        this.shadowColorMode = new ListValue("Shadow-Color", new String[]{"Background", "Custom"}, "Background");
        this.shadowColorRedValue = new IntegerValue("Shadow-Red", 0, 0, 255);
        this.shadowColorGreenValue = new IntegerValue("Shadow-Green", 111, 0, 255);
        this.shadowColorBlueValue = new IntegerValue("Shadow-Blue", 255, 0, 255);
        this.time2 = new MSTimer();
        this.time1 = new MSTimer();
    }

    public /* synthetic */ SessionInfo2(double d, double d2, float f, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    public SessionInfo2() {
        this(0.0, 0.0, 0.0f, 7, null);
    }

    public static final /* synthetic */ FloatValue access$getRadiusValue$p(SessionInfo2 $this) {
        return $this.radiusValue;
    }

    public static final /* synthetic */ ListValue access$getShadowColorMode$p(SessionInfo2 $this) {
        return $this.shadowColorMode;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorRedValue$p(SessionInfo2 $this) {
        return $this.shadowColorRedValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorGreenValue$p(SessionInfo2 $this) {
        return $this.shadowColorGreenValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorBlueValue$p(SessionInfo2 $this) {
        return $this.shadowColorBlueValue;
    }
}

