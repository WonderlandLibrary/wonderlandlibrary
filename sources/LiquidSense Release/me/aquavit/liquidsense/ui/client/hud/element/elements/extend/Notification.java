package me.aquavit.liquidsense.ui.client.hud.element.elements.extend;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Notification {
    private int width;
    private int height = 30;

    private FadeState fadeState;
    private int nowY;
    private long displayTime;
    private long animeXTime;
    private long animeYTime;

    private String title;

    private String content;

    private ColorType type;
    private int time;
    private int animeTime;

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
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

    public final void setNowY(int y) {
        this.nowY = y;
    }

    public final long getDisplayTime() {
        return this.displayTime;
    }

    public final void setDisplayTime(long time) {
        this.displayTime = time;
    }

    public final long getAnimeXTime() {
        return this.animeXTime;
    }

    public final void setAnimeXTime(long time) {
        this.animeXTime = time;
    }

    public final long getAnimeYTime() {
        return this.animeYTime;
    }

    public final void setAnimeYTime(long time) {
        this.animeYTime = time;
    }

    public final double easeInExpo(double x) {
        return (x == 0.0) ? 0.0 : Math.pow(2.0,10.0 * x - 10.0);
    }

    public final double easeOutExpo(double x) {
        return (x == 1.0) ? 1.0 : 1.0 - Math.pow(2.0,-10.0 * x);
    }

    public final boolean drawNotification(int index) {
        double pct;
        int realY = -(index + 1) * this.height;
        long nowTime = System.currentTimeMillis();
        ResourceLocation image = new ResourceLocation(LiquidSense.CLIENT_NAME.toLowerCase()+ "/notification/" + this.type.name() + ".png");
        if (this.nowY != realY) {
            pct = (double)(nowTime - this.animeYTime) / (double)this.animeTime;
            if (pct > 1.0) {
                this.nowY = realY;
                pct = 1.0;
            } else {
                pct = this.easeOutExpo(pct);
            }
            GL11.glTranslated(0.0, (double)(realY - this.nowY) * pct, 0.0);
        } else {
            this.animeYTime = nowTime;
        }
        GL11.glTranslated(0.0, this.nowY, 0.0);
        pct = (double)(nowTime - this.animeXTime) / (double)this.animeTime;
        switch (this.fadeState) {
            case IN: {
                if (pct > 1.0) {
                    this.fadeState = FadeState.STAY;
                    this.animeXTime = nowTime;
                    pct = 1.0;
                }
                pct = this.easeOutExpo(pct);
                break;
            }
            case STAY: {
                pct = 1.0;
                if (nowTime - this.animeXTime <= (long) this.time) break;
                this.fadeState = FadeState.OUT;
                this.animeXTime = nowTime;
                break;
            }
            case OUT: {
                if (pct > 1.0) {
                    this.fadeState = FadeState.END;
                    this.animeXTime = nowTime;
                    pct = 1.0;
                }
                pct = 1.0 - this.easeInExpo(pct);
                break;
            }
            case END: {
                return true;
            }
        }
        GL11.glTranslated((double)this.width - (double)this.width * pct, 0.0, 0.0);
        GL11.glTranslatef(-((float)this.width), 0.0f, 0.0f);
        RenderUtils.drawShader(-22.0f, 0.0f, (float)this.width + (float)22, (float)this.height);
        RenderUtils.drawRect(-22.0f, 0.0f, (float)this.width, (float)this.height, this.type.renderColor);
        RenderUtils.drawRect(-22.0f, 0.0f, (float)this.width, (float)this.height, new Color(0, 0, 0, 150));
        RenderUtils.drawRect(-22.0f, (float)this.height - 2.0f,
                Math.max((float)this.width - (float)this.width * ((float)(nowTime - this.displayTime) / ((float)this.animeTime * 2.0f + (float)this.time)),
                        -22.0f), (float)this.height, this.type.renderColor);
        Fonts.font18.drawString(this.title, 7.0f, 6.0f, -1);
        Fonts.font16.drawString(this.content, 7.0f, 17.0f, -1);
        RenderUtils.drawImage(image, -19, 3, 22, 22);
        GlStateManager.resetColor();
        return false;
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getContent() {
        return this.content;
    }

    public final ColorType getType() {
        return this.type;
    }

    public final int getTime() {
        return this.time;
    }

    public final int getAnimeTime() {
        return this.animeTime;
    }

    public Notification(String title, String content, ColorType type, int time, int animeTime) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.time = time;
        this.animeTime = animeTime;
        this.width = Math.max(100, Math.max(Fonts.font18.getStringWidth(title), Fonts.font18.getStringWidth(content) + 10));
        this.fadeState = FadeState.IN;
        this.nowY = -this.height;
        this.displayTime = System.currentTimeMillis();
        this.animeXTime = System.currentTimeMillis();
        this.animeYTime = System.currentTimeMillis();
    }
}
