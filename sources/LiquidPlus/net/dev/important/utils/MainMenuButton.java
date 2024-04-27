/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.utils;

import java.awt.Color;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class MainMenuButton
extends GuiButton {
    public MainMenuButton(int buttonId, int x, int y, int width, int height, String buttonText) {
        super(buttonId, x, y, width, height, buttonText);
    }

    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        if (this.field_146125_m) {
            Color color = new Color(180, 180, 180);
            GameFontRenderer fontrenderer = Fonts.fontSFUI35;
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            RenderUtils.drawRoundedRect2(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, 6, new Color(1, 1, 1, 80).getRGB());
            if (this.field_146123_n) {
                GL11.glPushMatrix();
                RenderUtils.color(color.darker().getRGB());
                GL11.glPopMatrix();
            }
            this.func_146119_b(mc, mouseX, mouseY);
            int stringColor = new Color(255, 255, 255, 200).getRGB();
            if (this.field_146123_n) {
                stringColor = color.darker().getRGB();
            }
            this.func_73732_a(fontrenderer, this.field_146126_j.toUpperCase(), this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + (this.field_146121_g - 6) / 2, stringColor);
        }
    }

    protected void func_146119_b(Minecraft mc, int mouseX, int mouseY) {
    }
}

