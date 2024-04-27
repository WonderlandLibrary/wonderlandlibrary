package me.ui;

import java.awt.Color;
import me.utils.render.VisualUtils;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class IGuiButton {
    String name;
    int x;
    int y;
    final int delta = RenderUtils.deltaTime;
    final float speedDelta = 0.01f * (float)this.delta;
    IGuiScreen guiScreen;
    float displayTime = System.currentTimeMillis();
    float nowTime = System.currentTimeMillis();
    public boolean isHovered = false;
    private final float moveX = 0.0f;
    float animeTime = 5.0f;
    float time = 5.0f;
    public float hoverAni = 0.0f;
    String icon;

    public IGuiButton(String name, IGuiScreen guiScreen, String icon) {
        this.name = name;
        this.guiScreen = guiScreen;
        this.icon = icon;
    }

    public void draw(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        this.isHovered = this.isHovered(x, y, x + 120, y + 18, mouseX, mouseY);
        this.hoverAni = (float)RenderUtils.getAnimationState(this.hoverAni, this.isHovered ? 255.0 : 0.0, 500.0);
        int stringColor = new Color(255, 255, 255, 240).getRGB();
        float finalAni = IGuiButton.clampValue(this.hoverAni / 100.0f, 0.0f, 1.0f);
        if (this.hoverAni > 1.0f) {
            Color color = new Color(180, 180, 180);
            GL11.glPushMatrix();
            VisualUtils.color(color.darker().getRGB());
            GL11.glPopMatrix();
            stringColor = color.darker().getRGB();
        }
        RenderUtils.drawRoundedRect(x, y, x + 120, y + 18, 6, new Color(255, 255, 255, 35).getRGB());
        Fonts.bold40.drawStringWithShadow(this.name, (int)((float)x + 34.285713f), (int)((float)y + 9.0f - (float)Fonts.bold40.getFontHeight() / 2.0f), stringColor);
    }

    public void onClick() {
        if (this.isHovered) {
            if (this.guiScreen == null) {
                Minecraft.getMinecraft().shutdown();
            } else {
                MinecraftInstance.mc.displayGuiScreen(this.guiScreen);
            }
        }
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public static float clampValue(float value, float floor, float cap) {
        if (value < floor) {
            return floor;
        }
        return Math.min(value, cap);
    }
}
