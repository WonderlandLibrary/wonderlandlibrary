// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.notify.xenza;

import java.awt.Font;
import net.augustus.ui.GuiIngameHook;
import net.augustus.utils.skid.tomorrow.RenderUtil;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.augustus.utils.ResourceUtil;
import net.minecraft.util.ResourceLocation;
import net.augustus.utils.skid.tomorrow.AnimationUtils;
import net.augustus.font.UnicodeFontRenderer;
import net.augustus.notify.NotificationType;
import net.augustus.utils.TimeHelper;

public class Notification
{
    public float x;
    public float width;
    public float height;
    public String name;
    public float lastTime;
    public TimeHelper timer;
    public NotificationType type;
    public boolean setBack;
    private float fy;
    private float cy;
    private TimeHelper anitimer;
    private static UnicodeFontRenderer arial16;
    private AnimationUtils animationUtils;
    private AnimationUtils animationUtils2;
    private ResourceLocation check;
    private ResourceLocation cross;
    
    public Notification(final String name, final NotificationType type) {
        this.cy = 0.0f;
        this.anitimer = new TimeHelper();
        this.animationUtils = new AnimationUtils();
        this.animationUtils2 = new AnimationUtils();
        this.check = ResourceUtil.loadResourceLocation("pictures/check.png", "check");
        this.cross = ResourceUtil.loadResourceLocation("pictures/cross.png", "cross");
        this.name = name;
        this.type = type;
        this.lastTime = 1.5f;
        this.width = (float)(Notification.arial16.getStringWidth(name) + 25);
        this.height = 20.0f;
    }
    
    public Notification(final String name, final NotificationType type, final float lastTime) {
        this.cy = 0.0f;
        this.anitimer = new TimeHelper();
        this.animationUtils = new AnimationUtils();
        this.animationUtils2 = new AnimationUtils();
        this.check = ResourceUtil.loadResourceLocation("pictures/check.png", "check");
        this.cross = ResourceUtil.loadResourceLocation("pictures/cross.png", "cross");
        this.name = name;
        this.type = type;
        this.lastTime = lastTime;
        this.width = (float)(Notification.arial16.getStringWidth(name) + 25);
        this.height = 20.0f;
    }
    
    public void render(final float y) {
        this.fy = y;
        if (this.cy == 0.0f) {
            this.cy = this.fy + 25.0f;
        }
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        RenderUtil.drawRect((float)(sr.getScaledWidth_double() - this.x), this.cy, (float)(sr.getScaledWidth_double() - this.x + this.width), this.cy + this.height, new Color(10, 10, 10, 220));
        RenderUtil.drawRect((float)(sr.getScaledWidth_double() - this.x), this.cy + this.height - 1.0f, (float)(sr.getScaledWidth_double() - this.x) + this.width, this.cy + this.height, new Color(180, 180, 180));
        switch (this.type) {
            case ToggleOn: {
                RenderUtil.drawCustomImageAlpha(sr.getScaledWidth() - this.x + 3.0f, this.cy + 4.0f, 12, 12, this.check, -1, 255.0f);
                break;
            }
            case ToggleOff: {
                RenderUtil.drawCustomImageAlpha(sr.getScaledWidth() - this.x + 3.0f, this.cy + 4.0f, 12, 12, this.cross, -1, 255.0f);
                break;
            }
        }
        Notification.arial16.drawString(this.name, sr.getScaledWidth() - this.x + 18.0f, this.cy + 7.0f, -1);
    }
    
    public void update() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (this.timer == null && Math.abs(this.x - this.width) < 0.1f) {
            (this.timer = new TimeHelper()).reset();
        }
        if (this.anitimer.reached(10L)) {
            this.cy = this.animationUtils.animate(this.fy, this.cy, 0.1f);
            if (!this.setBack) {
                this.x = this.animationUtils2.animate(this.width, this.x, 0.1f);
            }
            else {
                this.x = this.animationUtils2.animate(0.0f, this.x, 0.1f);
            }
            this.anitimer.reset();
        }
    }
    
    static {
        try {
            Notification.arial16 = new UnicodeFontRenderer(Font.createFont(0, GuiIngameHook.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(16.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
