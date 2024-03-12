// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.gui;

import net.minecraft.client.gui.GuiButton;
import java.awt.Rectangle;
import net.minecraft.client.gui.GuiScreen;

public interface TooltipProvider
{
    Rectangle getTooltipBounds(final GuiScreen p0, final int p1, final int p2);
    
    String[] getTooltipLines(final GuiButton p0, final int p1);
    
    boolean isRenderBorder();
}
