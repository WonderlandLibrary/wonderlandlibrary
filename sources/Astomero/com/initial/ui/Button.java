package com.initial.ui;

import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import com.initial.font.*;
import net.minecraft.client.*;
import java.awt.*;
import com.initial.utils.render.*;

public class Button extends GuiButton
{
    private int x;
    private int y;
    private int x1;
    private int y1;
    private String text;
    double size;
    MCFontRenderer font;
    
    public Button(final int par1, final int par2, final int par3, final int par4, final int par5, final String par6Str) {
        super(par1, par2, par3, par4, par5, par6Str);
        this.size = 0.0;
        this.x = par2;
        this.y = par3;
        this.x1 = par4;
        this.y1 = par5;
        this.text = par6Str;
        this.font = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Desync/fonts/SF-Pro.ttf"), 18.0f, 0), true, true);
    }
    
    public Button(final int i, final int j, final int k, final String stringParams) {
        this(i, j, k, 200, 20, stringParams);
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        final boolean isOverButton = mouseX >= this.x && mouseX <= this.x + this.x1 && mouseY >= this.y && mouseY <= this.y + this.y1;
        final int color = isOverButton ? new Color(255, 255, 255).getRGB() : new Color(200, 200, 200).getRGB();
        RenderUtil.drawRoundedRect2(this.x - this.size, this.y - this.size, this.x + this.x1 + this.size, this.y + this.y1 + this.size, 6.0, new Color(1, 1, 1, 150).getRGB());
        this.font.drawStringWithShadow(this.text, this.x + this.x1 / 4.3, this.y + this.y1 / 2 - 3, color);
    }
}
