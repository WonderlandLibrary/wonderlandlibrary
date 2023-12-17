// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import java.io.IOException;
import java.util.Collection;
import net.minecraft.client.resources.I18n;
import com.google.common.collect.Lists;
import java.util.List;

public class GuiYesNo extends GuiScreen
{
    protected GuiYesNoCallback parentScreen;
    protected String messageLine1;
    private final String messageLine2;
    private final List<String> listLines;
    protected String confirmButtonText;
    protected String cancelButtonText;
    protected int parentButtonClickedId;
    private int ticksUntilEnable;
    
    public GuiYesNo(final GuiYesNoCallback parentScreenIn, final String messageLine1In, final String messageLine2In, final int parentButtonClickedIdIn) {
        this.listLines = (List<String>)Lists.newArrayList();
        this.parentScreen = parentScreenIn;
        this.messageLine1 = messageLine1In;
        this.messageLine2 = messageLine2In;
        this.parentButtonClickedId = parentButtonClickedIdIn;
        this.confirmButtonText = I18n.format("gui.yes", new Object[0]);
        this.cancelButtonText = I18n.format("gui.no", new Object[0]);
    }
    
    public GuiYesNo(final GuiYesNoCallback parentScreenIn, final String messageLine1In, final String messageLine2In, final String confirmButtonTextIn, final String cancelButtonTextIn, final int parentButtonClickedIdIn) {
        this.listLines = (List<String>)Lists.newArrayList();
        this.parentScreen = parentScreenIn;
        this.messageLine1 = messageLine1In;
        this.messageLine2 = messageLine2In;
        this.confirmButtonText = confirmButtonTextIn;
        this.cancelButtonText = cancelButtonTextIn;
        this.parentButtonClickedId = parentButtonClickedIdIn;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
        this.listLines.clear();
        this.listLines.addAll(this.fontRenderer.listFormattedStringToWidth(this.messageLine2, this.width - 50));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        this.parentScreen.confirmClicked(button.id == 0, this.parentButtonClickedId);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.messageLine1, this.width / 2, 70, 16777215);
        int i = 90;
        for (final String s : this.listLines) {
            this.drawCenteredString(this.fontRenderer, s, this.width / 2, i, 16777215);
            i += this.fontRenderer.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void setButtonDelay(final int ticksUntilEnableIn) {
        this.ticksUntilEnable = ticksUntilEnableIn;
        for (final GuiButton guibutton : this.buttonList) {
            guibutton.enabled = false;
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        final int ticksUntilEnable = this.ticksUntilEnable - 1;
        this.ticksUntilEnable = ticksUntilEnable;
        if (ticksUntilEnable == 0) {
            for (final GuiButton guibutton : this.buttonList) {
                guibutton.enabled = true;
            }
        }
    }
}
