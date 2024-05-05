package com.initial.ui;

import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import com.initial.utils.render.*;

public class ExitButton extends GuiButton
{
    private String buttonText;
    private int x;
    private int y;
    private int widthIn;
    private int heightIn;
    
    public ExitButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.buttonText = buttonText;
        this.x = x;
        this.y = y;
        this.widthIn = widthIn;
        this.heightIn = heightIn;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        RenderUtil.drawImage(new ResourceLocation("Desync/mainmenu/close.png"), this.x, this.y, this.widthIn, this.heightIn);
    }
}
