// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.notifications;

import moonsense.utils.CustomFontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.MoonsenseClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

public class Notification
{
    public String title;
    public String text;
    public boolean showTimer;
    public long timeOnScreen;
    public long originalTimeOnScreen;
    public long originalTime;
    public final NotificationType type;
    public NotificationColor color;
    public float targetX;
    public float targetY;
    public float startingX;
    public float startingY;
    public float speed;
    public boolean leaving;
    public boolean left;
    public boolean joined;
    public boolean setDefaultY;
    
    public Notification(final String title, final String text, final boolean showTimer, final long timeOnScreen, final NotificationType type, final NotificationColor color, final int targetX, final int targetY, final int startingX, final int startingY, final int speed) {
        this.title = "";
        this.text = "";
        this.leaving = false;
        this.left = false;
        this.joined = false;
        this.setDefaultY = false;
        this.title = title;
        this.text = text;
        this.showTimer = showTimer;
        this.timeOnScreen = System.currentTimeMillis() + timeOnScreen;
        this.originalTime = timeOnScreen;
        this.originalTimeOnScreen = timeOnScreen;
        this.type = type;
        this.color = color;
        this.targetX = (float)targetX;
        this.targetY = (float)targetY;
        this.startingX = (float)startingX;
        this.startingY = (float)startingY;
        this.speed = (float)speed;
    }
    
    public void onRender(final int Num) {
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution sr = new ScaledResolution(mc);
        if (this.speed < 0.0f) {
            this.speed *= -1.0f;
        }
        if (this.speed == 0.0f) {
            this.speed = 2.0f;
        }
        if (System.currentTimeMillis() >= this.timeOnScreen) {
            this.leaving = true;
            this.targetY = (float)(sr.getScaledHeight() + 10);
        }
        if (!this.leaving && !this.left && this.setDefaultY) {
            this.targetY = (float)(sr.getScaledHeight() - 54 - 54 * Num);
        }
        final float orgSpeed = this.speed;
        if (this.targetX - this.startingX < 0.0f && this.startingX != this.targetX) {
            if ((this.targetX - this.startingX) * -1.0f < this.speed) {
                this.speed = (this.targetX - this.startingX) * -1.0f;
            }
        }
        else if (this.startingX != this.targetX && (this.targetX - this.startingX) * 1.0f < this.speed) {
            this.speed = (this.targetX - this.startingX) * -1.0f;
        }
        if (this.targetY - this.startingY < 0.0f && this.startingY != this.targetY) {
            if ((this.targetY - this.startingY) * -1.0f < this.speed && this.speed >= (this.targetY - this.startingY) * -1.0f) {
                this.speed = (this.targetY - this.startingY) * -1.0f;
            }
        }
        else if (this.startingY != this.targetY && (this.targetY - this.startingY) * -1.0f < this.speed && this.speed >= (this.targetY - this.startingY) * -1.0f) {
            this.speed = (this.targetY - this.startingY) * -1.0f;
        }
        if (this.speed <= 0.0f) {
            this.speed = orgSpeed;
        }
        if (this.startingX < this.targetX) {
            this.startingX += this.speed;
        }
        else if (this.startingX > this.targetX) {
            this.startingX -= this.speed;
        }
        if (this.startingY < this.targetY) {
            this.startingY += this.speed;
        }
        else if (this.startingY > this.targetY) {
            this.startingY -= this.speed;
        }
        if (this.startingX == this.targetX && this.startingY == this.targetY && !this.left && !this.leaving && !this.joined) {
            this.joined = true;
        }
        if (!this.joined) {
            this.timeOnScreen = System.currentTimeMillis() + this.originalTimeOnScreen;
        }
        double notWidth = 170.0;
        final double orgX = this.startingX;
        if (MoonsenseClient.titleRenderer.getWidth(this.title) * 1.1 + 45.0 >= 170.0) {
            notWidth = MoonsenseClient.titleRenderer.getWidth(this.title) * 1.1 + 50.0;
            this.startingX -= (float)(MoonsenseClient.titleRenderer.getWidth(this.title) * 1.1 - 170.0 + 50.0);
        }
        if (MoonsenseClient.textRenderer.getWidth(this.text) * 0.9 + 43.33 >= notWidth) {
            notWidth = MoonsenseClient.textRenderer.getWidth(this.text) * 0.9 + 50.0;
            this.startingX -= (float)(MoonsenseClient.textRenderer.getWidth(this.text) * 0.9 - 170.0 + 50.0);
        }
        GuiUtils.drawRoundedRect(this.startingX, this.startingY, (float)(this.startingX + notWidth), this.startingY + 45.0f, 7.0f, new Color(32, 34, 37, 204).getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.translate(4.0f, 4.0f, 0.0f);
        GlStateManager.scale(1.1, 1.1, 1.0);
        GlStateManager.translate(-4.0f, -4.0f, 0.0f);
        Gui.drawRect(-100, -100, 1, 1, -1);
        final CustomFontRenderer titleRenderer = MoonsenseClient.titleRenderer;
        final String title = this.title;
        final int x = (int)((this.startingX + 45.0f) / 1.1);
        final float n = this.startingY + 9.0f;
        MoonsenseClient.titleRenderer.getClass();
        titleRenderer.drawString(title, x, (int)((int)(n - 9 / 2) / 1.1), this.color.color);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(4.0f, 4.0f, 0.0f);
        GlStateManager.scale(0.9, 0.9, 1.0);
        GlStateManager.translate(-4.0f, -4.0f, 0.0f);
        Gui.drawRect(-100, -100, 1, 1, -1);
        final CustomFontRenderer textRenderer = MoonsenseClient.textRenderer;
        final String text = this.text;
        final float x2 = (float)((this.startingX + 43.33) / 0.9);
        final float n2 = this.startingY + 11.0f;
        MoonsenseClient.textRenderer.getClass();
        textRenderer.drawString(text, x2, (float)((n2 + 9.0f) / 0.9), this.color.color);
        GlStateManager.popMatrix();
        mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/notifications/" + this.type.type + this.color.colorName + ".png"));
        final int size = 30;
        Gui.drawModalRectWithCustomSizedTexture((int)this.startingX + 4, (int)this.startingY + 5, 0.0f, 0.0f, size, size, (float)size, (float)size);
        if (this.showTimer && this.timeOnScreen - System.currentTimeMillis() > 0L) {
            GuiUtils.drawRect(this.startingX, this.startingY + 40.0f, (float)(this.startingX + notWidth / this.originalTime * (this.timeOnScreen - System.currentTimeMillis())), this.startingY + 45.0f, this.color.color);
        }
        if (this.startingY == this.targetY && this.leaving) {
            this.left = true;
            MoonsenseClient.INSTANCE.getNotificationManager().notifications.remove(this);
        }
        this.startingX = (float)(int)orgX;
        this.speed = orgSpeed;
    }
}
