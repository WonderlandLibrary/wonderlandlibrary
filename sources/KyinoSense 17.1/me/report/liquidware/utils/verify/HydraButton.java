/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 */
package me.report.liquidware.utils.verify;

import java.awt.Color;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class HydraButton
extends GuiButton {
    private int color;

    public HydraButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        if (this.field_146125_m) {
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            RenderUtils.drawRect((float)this.field_146128_h, (float)this.field_146129_i, (float)(this.field_146128_h + this.field_146120_f), (float)(this.field_146129_i + this.field_146121_g), this.color);
            Fonts.font40.func_175065_a(this.field_146126_j, (float)this.field_146128_h + (float)(this.field_146120_f / 2) - (float)(Fonts.font40.func_78256_a(this.field_146126_j) / 2), (float)this.field_146129_i + (float)(this.field_146121_g / 2) - (float)(Fonts.font40.getHeight() / 2), Color.WHITE.getRGB(), true);
        }
    }

    public void updateCoordinates(float x, float y) {
        this.field_146128_h = (int)x;
        this.field_146129_i = (int)y;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean hovered(int mouseX, int mouseY) {
        return mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
    }
}

