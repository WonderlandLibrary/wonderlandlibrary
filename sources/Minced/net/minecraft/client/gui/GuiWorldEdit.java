// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.commons.io.FileUtils;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiWorldEdit extends GuiScreen
{
    private final GuiScreen lastScreen;
    private GuiTextField nameEdit;
    private final String worldId;
    
    public GuiWorldEdit(final GuiScreen p_i46593_1_, final String p_i46593_2_) {
        this.lastScreen = p_i46593_1_;
        this.worldId = p_i46593_2_;
    }
    
    @Override
    public void updateScreen() {
        this.nameEdit.updateCursorCounter();
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        final GuiButton guibutton = this.addButton(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 24 + 12, I18n.format("selectWorld.edit.resetIcon", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 48 + 12, I18n.format("selectWorld.edit.openFolder", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectWorld.edit.save", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
        guibutton.enabled = GuiWorldEdit.mc.getSaveLoader().getFile(this.worldId, "icon.png").isFile();
        final ISaveFormat isaveformat = GuiWorldEdit.mc.getSaveLoader();
        final WorldInfo worldinfo = isaveformat.getWorldInfo(this.worldId);
        final String s = (worldinfo == null) ? "" : worldinfo.getWorldName();
        (this.nameEdit = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, 60, 200, 20)).setFocused(true);
        this.nameEdit.setText(s);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                GuiWorldEdit.mc.displayGuiScreen(this.lastScreen);
            }
            else if (button.id == 0) {
                final ISaveFormat isaveformat = GuiWorldEdit.mc.getSaveLoader();
                isaveformat.renameWorld(this.worldId, this.nameEdit.getText().trim());
                GuiWorldEdit.mc.displayGuiScreen(this.lastScreen);
            }
            else if (button.id == 3) {
                final ISaveFormat isaveformat2 = GuiWorldEdit.mc.getSaveLoader();
                FileUtils.deleteQuietly(isaveformat2.getFile(this.worldId, "icon.png"));
                button.enabled = false;
            }
            else if (button.id == 4) {
                final ISaveFormat isaveformat3 = GuiWorldEdit.mc.getSaveLoader();
                OpenGlHelper.openFile(isaveformat3.getFile(this.worldId, "icon.png").getParentFile());
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.nameEdit.textboxKeyTyped(typedChar, keyCode);
        this.buttonList.get(2).enabled = !this.nameEdit.getText().trim().isEmpty();
        if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed(this.buttonList.get(2));
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameEdit.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("selectWorld.edit.title", new Object[0]), this.width / 2, 20, 16777215);
        this.drawString(this.fontRenderer, I18n.format("selectWorld.enterName", new Object[0]), this.width / 2 - 100, 47, 10526880);
        this.nameEdit.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
