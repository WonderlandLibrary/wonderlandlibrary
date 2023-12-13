// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class SelectionButton extends GuiButton
{
    public SelectionButton(final int buttonId, final int x, final int y, final String buttonText) {
        super(buttonId, x, y, buttonText);
    }
    
    public SelectionButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
    }
}
