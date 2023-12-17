// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.altmanager;

import java.io.IOException;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiRenameAlt extends GuiScreen
{
    private final GuiAltManager manager;
    private GuiTextField nameField;
    private PasswordField pwField;
    private String status;
    
    public GuiRenameAlt(final GuiAltManager manager) {
        this.status = TextFormatting.GRAY + "Waiting...";
        this.manager = manager;
    }
    
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.manager.selectedAlt.setMask(this.nameField.getText());
                this.manager.selectedAlt.setPassword(this.pwField.getText());
                this.status = "Edited!";
                break;
            }
            case 1: {
                GuiRenameAlt.mc.displayGuiScreen(this.manager);
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        final ScaledResolution sr = new ScaledResolution(GuiRenameAlt.mc);
        RenderUtility.drawRectNotWH(0.0, 0.0, this.width, this.height, new Color(17, 17, 17, 255).getRGB());
        GuiRenameAlt.mc.fontRenderer.drawStringWithShadow("Edit Alt", this.width / 2.0f, 10.0f, -1);
        GuiRenameAlt.mc.fontRenderer.drawStringWithShadow(this.status, this.width / 2.0f, 20.0f, -1);
        this.nameField.drawTextBox();
        this.pwField.drawTextBox();
        if (this.nameField.getText().isEmpty() && !this.nameField.isFocused()) {
            this.drawString(GuiRenameAlt.mc.fontRenderer, "Name", this.width / 2 - 96, 66, -7829368);
        }
        if (this.pwField.getText().isEmpty() && !this.pwField.isFocused()) {
            this.drawString(GuiRenameAlt.mc.fontRenderer, "Password", this.width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(par1, par2, par3);
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiAltButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Edit"));
        this.buttonList.add(new GuiAltButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Cancel"));
        this.nameField = new GuiTextField(this.eventButton, GuiRenameAlt.mc.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.pwField = new PasswordField(GuiRenameAlt.mc.fontRenderer, this.width / 2 - 100, 100, 200, 20);
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.nameField.textboxKeyTyped(par1, par2);
        this.pwField.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.nameField.isFocused() || this.pwField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
            this.pwField.setFocused(!this.pwField.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.nameField.mouseClicked(par1, par2, par3);
        this.pwField.mouseClicked(par1, par2, par3);
    }
}
