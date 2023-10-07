// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.auth;

import java.awt.Color;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import java.io.IOException;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAuth extends GuiScreen
{
    private GuiTextField uidField;
    
    @Override
    public void updateScreen() {
        this.uidField.updateCursorCounter();
        super.updateScreen();
    }
    
    @Override
    public void onGuiClosed() {
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.uidField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void initGui() {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        (this.uidField = new GuiTextField(5, this.fontRendererObj, sr.getScaledWidth() / 2 - 100, 55, 200, 20)).setMaxStringLength(10);
        final int j = this.height / 4 + 48;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 200, 20, "Login"));
        this.buttonList.get(0).visible = false;
        this.mc.func_181537_a(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawBackground(0);
        this.buttonList.get(0).drawBetterButton(this.mc, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.uidField.drawTextBox();
        if (this.uidField.getText().isEmpty() && !this.uidField.isFocused()) {
            this.mc.fontRendererObj.drawStringWithShadow("Free xenza i guess ;)", sr.getScaledWidth() / 2.0f - 96.0f, 61.0f, Color.gray.getRGB());
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.uidField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
