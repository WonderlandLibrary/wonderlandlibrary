/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.FadeState;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification$WhenMappings;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import org.lwjgl.opengl.GL11;

public final class Notification {
    private final int animeTime;
    private int nowY;
    private long animeXTime;
    private float x;
    private final String title;
    private final float width;
    private final String content;
    private String s;
    private long animeYTime;
    private final int height = 30;
    private FadeState fadeState;
    private long displayTime;
    private final NotifyType type;
    private int n2;
    private final int time;
    private int textLength;

    public final float getWidth() {
        return this.width;
    }

    public Notification(String string, String string2, NotifyType notifyType, int n, int n2, int n3, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n3 & 8) != 0) {
            n = 1000;
        }
        if ((n3 & 0x10) != 0) {
            n2 = 350;
        }
        this(string, string2, notifyType, n, n2);
    }

    public final int getHeight() {
        return this.height;
    }

    public final boolean drawNotification(int n, float f, float f2, float f3) {
        double d;
        int n2 = -(n + 1) * (this.height + 2);
        long l = System.currentTimeMillis();
        double d2 = this.nowY;
        if (this.nowY != n2) {
            d = (double)(l - this.animeYTime) / (double)this.animeTime;
            if (d > 1.0) {
                this.nowY = n2;
                d = 1.0;
            } else {
                d = EaseUtils.easeOutQuart(d);
            }
            GL11.glTranslated((double)0.0, (double)((double)(n2 - this.nowY) * d), (double)0.0);
        } else {
            this.animeYTime = l;
        }
        GL11.glTranslated((double)1.0, (double)this.nowY, (double)0.0);
        d = (double)(l - this.animeXTime) / (double)this.animeTime;
        switch (Notification$WhenMappings.$EnumSwitchMapping$0[this.fadeState.ordinal()]) {
            case 1: {
                if (d > 1.0) {
                    this.fadeState = FadeState.STAY;
                    this.animeXTime = l;
                    d = 1.0;
                }
                d = EaseUtils.easeOutQuart(d);
                d2 += (double)(n2 - this.nowY) * d;
                break;
            }
            case 2: {
                d = 1.0;
                if (l - this.animeXTime <= (long)this.time) break;
                this.fadeState = FadeState.OUT;
                this.animeXTime = l;
                break;
            }
            case 3: {
                if (d > 1.0) {
                    this.fadeState = FadeState.END;
                    this.animeXTime = l;
                    d = 2.0;
                }
                d = 1.0 - EaseUtils.INSTANCE.easeInQuart(d);
                break;
            }
            case 4: {
                return true;
            }
        }
        Color color = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color3 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        Color color4 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        GL11.glTranslated((double)((double)this.width - (double)this.width * d), (double)0.0, (double)0.0);
        GL11.glTranslatef((float)(-this.width), (float)0.0f, (float)0.0f);
        float f4 = this.width - this.width * ((float)(l - this.displayTime) / ((float)this.animeTime * 2.0f + (float)this.time));
        float f5 = 0.0f;
        float f6 = -2.0f;
        float f7 = 44.0f;
        boolean bl = false;
        float f8 = Math.max(f4, f5);
        RoundedUtil.drawGradientRound(f7, f6, f8, 28.0f, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
        if (this.type == NotifyType.SUCCESS) {
            this.s = "SUCCESS";
        } else if (this.type == NotifyType.ERROR) {
            this.s = "ERROR";
        } else if (this.type == NotifyType.WARNING) {
            this.s = "WARNING";
        } else if (this.type == NotifyType.INFO) {
            this.s = "INFO";
        }
        if (this.s.equals("INFO")) {
            RoundedUtil.drawGradientRound(44.0f, -2.0f, this.width, 28.0f, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
            Fonts.nbicon20.drawString("h", 48.0f, 3.0f, new Color(224, 194, 30, 255).getRGB());
            Fonts.font35.drawString(this.title, 62.0f, 3.0f, new Color(224, 194, 30, 255).getRGB());
            Fonts.font35.drawString(this.content, 48.0f, 16.0f, Color.white.getRGB());
        }
        if (this.s.equals("WARNING")) {
            RoundedUtil.drawGradientRound(44.0f, -2.0f, this.width, 28.0f, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
            Fonts.nbicon20.drawString("h", 48.0f, 3.0f, new Color(224, 194, 30, 255).getRGB());
            Fonts.font35.drawString(this.title, 62.0f, 3.0f, new Color(224, 194, 30, 255).getRGB());
            Fonts.font35.drawString(this.content, 48.0f, 16.0f, Color.white.getRGB());
        }
        if (this.s.equals("SUCCESS")) {
            RoundedUtil.drawGradientRound(44.0f, -2.0f, this.width - 20.0f, 28.0f, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
            Fonts.nbicon20.drawString("i", 48.0f, 3.0f, new Color(0, 157, 255, 240).getRGB());
            Fonts.font40.drawString(this.title, 62.0f, 3.0f, new Color(0, 157, 255, 240).getRGB());
            Fonts.font35.drawString(this.content, 48.0f, 16.0f, Color.white.getRGB());
        }
        if (this.s.equals("ERROR")) {
            RoundedUtil.drawGradientRound(44.0f, -2.0f, this.width - 20.0f, 28.0f, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
            Fonts.nbicon20.drawString("g", 48.0f, 3.0f, new Color(206, 33, 33, 240).getRGB());
            Fonts.font40.drawString(this.title, 62.0f, 3.0f, new Color(206, 33, 33, 240).getRGB());
            Fonts.font35.drawString(this.content, 48.0f, 16.0f, Color.white.getRGB());
        }
        return false;
    }

    public final void setAnimeYTime(long l) {
        this.animeYTime = l;
    }

    public final long getAnimeXTime() {
        return this.animeXTime;
    }

    public final long getAnimeYTime() {
        return this.animeYTime;
    }

    public final void setN2(int n) {
        this.n2 = n;
    }

    public final String getContent() {
        return this.content;
    }

    public final float getX() {
        return this.x;
    }

    public final int getAnimeTime() {
        return this.animeTime;
    }

    public final FadeState getFadeState() {
        return this.fadeState;
    }

    public final void setFadeState(FadeState fadeState) {
        this.fadeState = fadeState;
    }

    public final int getNowY() {
        return this.nowY;
    }

    public final void setAnimeXTime(long l) {
        this.animeXTime = l;
    }

    public final void setTextLength(int n) {
        this.textLength = n;
    }

    public final String getTitle() {
        return this.title;
    }

    public final int getTextLength() {
        return this.textLength;
    }

    public final void setX(float f) {
        this.x = f;
    }

    public final void setNowY(int n) {
        this.nowY = n;
    }

    public final int getTime() {
        return this.time;
    }

    public Notification(String string, String string2, NotifyType notifyType, int n, int n2) {
        this.title = string;
        this.content = string2;
        this.type = notifyType;
        this.time = n;
        this.animeTime = n2;
        this.n2 = Fonts.tenacitybold35.getStringWidth(this.content);
        this.textLength = Math.max(this.n2, 0);
        this.width = (float)this.textLength + 80.0f;
        this.height = 30;
        this.fadeState = FadeState.IN;
        this.nowY = -this.height;
        this.displayTime = System.currentTimeMillis();
        this.animeXTime = System.currentTimeMillis();
        this.animeYTime = System.currentTimeMillis();
    }

    public final NotifyType getType() {
        return this.type;
    }

    public final void setDisplayTime(long l) {
        this.displayTime = l;
    }

    public final long getDisplayTime() {
        return this.displayTime;
    }

    public final int getN2() {
        return this.n2;
    }
}

