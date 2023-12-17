// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiListButton extends GuiButton
{
    private boolean value;
    private final String localizationStr;
    private final GuiPageButtonList.GuiResponder guiResponder;
    
    public GuiListButton(final GuiPageButtonList.GuiResponder responder, final int buttonId, final int x, final int y, final String localizationStrIn, final boolean valueIn) {
        super(buttonId, x, y, 150, 20, "");
        this.localizationStr = localizationStrIn;
        this.value = valueIn;
        this.displayString = this.buildDisplayString();
        this.guiResponder = responder;
    }
    
    private String buildDisplayString() {
        return I18n.format(this.localizationStr, new Object[0]) + ": " + I18n.format(this.value ? "gui.yes" : "gui.no", new Object[0]);
    }
    
    public void setValue(final boolean valueIn) {
        this.value = valueIn;
        this.displayString = this.buildDisplayString();
        this.guiResponder.setEntryValue(this.id, valueIn);
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.value = !this.value;
            this.displayString = this.buildDisplayString();
            this.guiResponder.setEntryValue(this.id, this.value);
            return true;
        }
        return false;
    }
}
