package com.initial.ui;

import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import com.initial.font.*;
import net.minecraft.client.*;

public class TextButton extends GuiButton
{
    private String buttonText;
    private int x;
    private int y;
    private int widthIn;
    private int heightIn;
    MCFontRenderer font;
    
    public TextButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.buttonText = buttonText;
        this.x = x;
        this.y = y;
        this.widthIn = widthIn;
        this.heightIn = heightIn;
        this.font = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Desync/fonts/SF-Pro.ttf"), 18.0f, 0), true, true);
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        this.font.drawString(this.buttonText, (float)this.x, (float)this.y, -1);
    }
}
