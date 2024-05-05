/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.ranges.RangesKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import jx.utils.CFont.FontLoaders;
import jx.utils.EaseUtils;
import jx.utils.render.RoundedUtil;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.FadeState;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification$WhenMappings;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public final class Notification {
    private int width;
    private int width2;
    private int height;
    private FadeState fadeState;
    private float x;
    private final float widthSpacing = 25.0f;
    private float y;
    private final int height2 = 28;
    private int nowY;
    private long displayTime;
    private long animeXTime;
    private long animeYTime;
    private float notifWidth;
    private float notifX;
    private final String title;
    private final String content;
    private final NotifyType type;
    private final int time;
    private final int animeTime;

    public final int getWidth() {
        return this.width;
    }

    public final void setWidth(int n) {
        this.width = n;
    }

    public final int getWidth2() {
        return this.width2;
    }

    public final void setWidth2(int n) {
        this.width2 = n;
    }

    public final int getHeight() {
        return this.height;
    }

    public final void setHeight(int n) {
        this.height = n;
    }

    public final FadeState getFadeState() {
        return this.fadeState;
    }

    public final void setFadeState(FadeState fadeState) {
        this.fadeState = fadeState;
    }

    public final float getX() {
        return this.x;
    }

    public final void setX(float f) {
        this.x = f;
    }

    public final float getY() {
        return this.y;
    }

    public final void setY(float f) {
        this.y = f;
    }

    public final int getHeight2() {
        return this.height2;
    }

    public final int getNowY() {
        return this.nowY;
    }

    public final void setNowY(int n) {
        this.nowY = n;
    }

    public final long getDisplayTime() {
        return this.displayTime;
    }

    public final void setDisplayTime(long l) {
        this.displayTime = l;
    }

    public final long getAnimeXTime() {
        return this.animeXTime;
    }

    public final void setAnimeXTime(long l) {
        this.animeXTime = l;
    }

    public final long getAnimeYTime() {
        return this.animeYTime;
    }

    public final void setAnimeYTime(long l) {
        this.animeYTime = l;
    }

    public final float getNotifWidth() {
        return this.notifWidth;
    }

    public final void setNotifWidth(float f) {
        this.notifWidth = f;
    }

    public final float getNotifX() {
        return this.notifX;
    }

    public final void setNotifX(float f) {
        this.notifX = f;
    }

    public final boolean drawNotification(int index, IFontRenderer font) {
        double pct;
        int realY = -(index + 1) * this.height;
        long nowTime = System.currentTimeMillis();
        if (this.nowY != realY) {
            pct = (double)(nowTime - this.animeYTime) / (double)this.animeTime;
            if (pct > 1.0) {
                this.nowY = realY;
                pct = 1.0;
            } else {
                pct = EaseUtils.easeOutExpo(pct);
            }
            GL11.glTranslated((double)0.0, (double)((double)(realY - this.nowY) * pct), (double)0.0);
        } else {
            this.animeYTime = nowTime;
        }
        GL11.glTranslated((double)0.0, (double)this.nowY, (double)0.0);
        pct = (double)(nowTime - this.animeXTime) / (double)this.animeTime;
        switch (Notification$WhenMappings.$EnumSwitchMapping$0[this.fadeState.ordinal()]) {
            case 1: {
                if (pct > 1.0) {
                    this.fadeState = FadeState.STAY;
                    this.animeXTime = nowTime;
                    pct = 1.0;
                }
                pct = EaseUtils.easeOutExpo(pct);
                break;
            }
            case 2: {
                pct = 1.0;
                if (nowTime - this.animeXTime <= (long)this.time) break;
                this.fadeState = FadeState.OUT;
                this.animeXTime = nowTime;
                break;
            }
            case 3: {
                if (pct > 1.0) {
                    this.fadeState = FadeState.END;
                    this.animeXTime = nowTime;
                    pct = 1.0;
                }
                pct = 1.0 - EaseUtils.easeInExpo(pct);
                break;
            }
            case 4: {
                return true;
            }
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hud = (HUD)module;
        switch ((String)hud.getNotificationStyle().get()) {
            case "Windows11": {
                GL11.glTranslated((double)((double)this.width2 - (double)this.width2 * pct), (double)0.0, (double)0.0);
                GL11.glTranslatef((float)(-((float)this.width2)), (float)0.0f, (float)0.0f);
                String s1 = "";
                switch (Notification$WhenMappings.$EnumSwitchMapping$1[this.type.ordinal()]) {
                    case 1: {
                        s1 = "liquidbounce/notification/securityAndMaintenance_Error.png";
                        break;
                    }
                    case 2: {
                        s1 = "liquidbounce/notification/securityAndMaintenance_Alert.png";
                        break;
                    }
                    case 3: {
                        s1 = "liquidbounce/notification/securityAndMaintenance.png";
                        break;
                    }
                    case 4: {
                        s1 = "liquidbounce/notification/securityAndMaintenance_Alert.png";
                    }
                }
                RoundedUtil.drawRound(0.0f, 0.0f, this.width2, this.height2, 4.0f, new Color(0, 230, 230, 255));
                float f = 5.0f;
                Fonts.SFUI35.drawString(this.title, 30.0f, f, Color.BLACK.getRGB(), false);
                Fonts.font30.drawString(this.content, 30.0f, 5.0f + (float)Fonts.SFUI35.getFontHeight() + 2.0f, new Color(0, 0, 0, 255).getRGB(), false);
                RenderUtils.drawImage(MinecraftInstance.classProvider.createResourceLocation(s1), 4, 5, 20, 20);
                break;
            }
            case "None": {
                GL11.glTranslated((double)((double)this.width - (double)this.width * pct), (double)0.0, (double)0.0);
                GL11.glTranslatef((float)(-((float)this.width)), (float)0.0f, (float)0.0f);
                String s1 = "";
                Color renderColor = new Color(79, 216, 7);
                switch (Notification$WhenMappings.$EnumSwitchMapping$2[this.type.ordinal()]) {
                    case 1: {
                        s1 = "B";
                        renderColor = new Color(255, 68, 50);
                        break;
                    }
                    case 2: {
                        s1 = "C";
                        renderColor = new Color(7, 68, 255);
                        break;
                    }
                    case 3: {
                        s1 = "A";
                        renderColor = new Color(79, 216, 7);
                        break;
                    }
                    case 4: {
                        s1 = "D";
                        renderColor = new Color(220, 225, 6);
                    }
                }
                RoundedUtil.drawRound(0.0f, -1.0f, this.width, (float)this.height - 10.0f, 3.0f, new Color(29, 29, 31, 255));
                GameFontRenderer icon60 = Fonts.ico1;
                Color color = Color.WHITE;
                if (icon60 != null) {
                    icon60.drawString(s1, 7.0f, 11.0f, color.getRGB());
                }
                GameFontRenderer font35 = Fonts.sfbold35;
                String s2 = this.title;
                float f = (float)((double)((float)font.getFontHeight() / 2.0f) + 5.0);
                font35.drawString(s2, 28.0f, f, color.getRGB(), false);
                Fonts.sfbold28.drawString(this.content, 28.0f, (float)((double)((float)font.getFontHeight() / 2.0f) + 15.0), new Color(255, 255, 255, 100).getRGB(), false);
                RenderUtils.drawGoodCircle(this.width - 6, (double)((float)font.getFontHeight() / 2.0f) + 12.0, 2.0f, renderColor.getRGB());
            }
        }
        GlStateManager.func_179117_G();
        return false;
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getContent() {
        return this.content;
    }

    public final NotifyType getType() {
        return this.type;
    }

    public final int getTime() {
        return this.time;
    }

    public final int getAnimeTime() {
        return this.animeTime;
    }

    public Notification(String title, String content, NotifyType type, int time, int animeTime) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.time = time;
        this.animeTime = animeTime;
        this.width = RangesKt.coerceAtLeast((int)100, (int)(FontLoaders.JelloM20.getStringWidth(this.content) + 22));
        this.width2 = RangesKt.coerceAtLeast((int)100, (int)(FontLoaders.JelloM20.getStringWidth(this.content) + 12));
        this.height = 45;
        this.fadeState = FadeState.IN;
        this.widthSpacing = 25.0f;
        this.height2 = 28;
        this.nowY = -this.height;
        this.displayTime = System.currentTimeMillis();
        this.animeXTime = System.currentTimeMillis();
        this.animeYTime = System.currentTimeMillis();
        this.notifWidth = (float)(17 + Math.max(FontLoaders.tenacitybold18.getStringWidth(this.content), FontLoaders.tenacitybold20.getStringWidth(this.title))) + this.widthSpacing;
        this.notifX = this.notifWidth + (float)5;
    }

    public /* synthetic */ Notification(String string, String string2, NotifyType notifyType, int n, int n2, int n3, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n3 & 8) != 0) {
            n = 1500;
        }
        if ((n3 & 0x10) != 0) {
            n2 = 500;
        }
        this(string, string2, notifyType, n, n2);
    }
}

