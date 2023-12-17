// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.base.Splitter;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.Logger;

public class GuiWorldSelection extends GuiScreen
{
    private static final Logger LOGGER;
    protected GuiScreen prevScreen;
    protected String title;
    private String worldVersTooltip;
    private GuiButton deleteButton;
    private GuiButton selectButton;
    private GuiButton renameButton;
    private GuiButton copyButton;
    private GuiListWorldSelection selectionList;
    
    public GuiWorldSelection(final GuiScreen screenIn) {
        this.title = "Select world";
        this.prevScreen = screenIn;
    }
    
    @Override
    public void initGui() {
        this.title = I18n.format("selectWorld.title", new Object[0]);
        this.selectionList = new GuiListWorldSelection(this, GuiWorldSelection.mc, this.width, this.height, 32, this.height - 64, 36);
        this.postInit();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.selectionList.handleMouseInput();
    }
    
    public void postInit() {
        this.selectButton = this.addButton(new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("selectWorld.select", new Object[0])));
        this.addButton(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("selectWorld.create", new Object[0])));
        this.renameButton = this.addButton(new GuiButton(4, this.width / 2 - 154, this.height - 28, 72, 20, I18n.format("selectWorld.edit", new Object[0])));
        this.deleteButton = this.addButton(new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, I18n.format("selectWorld.delete", new Object[0])));
        this.copyButton = this.addButton(new GuiButton(5, this.width / 2 + 4, this.height - 28, 72, 20, I18n.format("selectWorld.recreate", new Object[0])));
        this.addButton(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20, I18n.format("gui.cancel", new Object[0])));
        this.selectButton.enabled = false;
        this.deleteButton.enabled = false;
        this.renameButton.enabled = false;
        this.copyButton.enabled = false;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            final GuiListWorldSelectionEntry guilistworldselectionentry = this.selectionList.getSelectedWorld();
            if (button.id == 2) {
                if (guilistworldselectionentry != null) {
                    guilistworldselectionentry.deleteWorld();
                }
            }
            else if (button.id == 1) {
                if (guilistworldselectionentry != null) {
                    guilistworldselectionentry.joinWorld();
                }
            }
            else if (button.id == 3) {
                GuiWorldSelection.mc.displayGuiScreen(new GuiCreateWorld(this));
            }
            else if (button.id == 4) {
                if (guilistworldselectionentry != null) {
                    guilistworldselectionentry.editWorld();
                }
            }
            else if (button.id == 0) {
                GuiWorldSelection.mc.displayGuiScreen(this.prevScreen);
            }
            else if (button.id == 5 && guilistworldselectionentry != null) {
                guilistworldselectionentry.recreateWorld();
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.worldVersTooltip = null;
        this.selectionList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.worldVersTooltip != null) {
            this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split((CharSequence)this.worldVersTooltip)), mouseX, mouseY);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.selectionList.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.selectionList.mouseReleased(mouseX, mouseY, state);
    }
    
    public void setVersionTooltip(final String p_184861_1_) {
        this.worldVersTooltip = p_184861_1_;
    }
    
    public void selectWorld(@Nullable final GuiListWorldSelectionEntry entry) {
        final boolean flag = entry != null;
        this.selectButton.enabled = flag;
        this.deleteButton.enabled = flag;
        this.renameButton.enabled = flag;
        this.copyButton.enabled = flag;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
