/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package ad;

import ad.Colors;
import java.awt.Color;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

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
    private float moveX = 0.0f;
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
        Color color;
        Color color2;
        this.x = x;
        this.y = y;
        this.isHovered = this.isHovered(x, y, x + 120, y + 18, mouseX, mouseY);
        this.hoverAni = RenderUtils.getAnimationState(this.hoverAni, this.isHovered ? 255.0f : 0.0f, 500.0f);
        float finalAni = IGuiButton.clampValue(this.hoverAni / 100.0f, 0.0f, 1.0f);
        if (this.hoverAni > 1.0f) {
            // empty if block
        }
        float f = x - 1;
        float f2 = y - 1;
        RenderUtils.drawRoundRect(x + 121, y + 19, (float)color2, (float)color2, 0.getRGB());
        float f3 = x - 1;
        float f4 = y - 1;
        RenderUtils.drawRoundRect(x + 121, y + 19, (float)color, (float)color, 0.getRGB());
        Fonts.posterama35.drawStringWithShadow(this.name, (int)((float)x + 34.285713f), (int)((float)y + 9.0f - (float)Fonts.posterama35.getFontHeight() / 2.0f), Colors.WHITE.c);
    }

    public void onClick() {
        if (this.isHovered) {
            if (this.guiScreen == null) {
                Minecraft.func_71410_x().func_71400_g();
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

