// 
// Decompiled by Procyon v0.5.30
// 

package me.aidanmees.trivia.client.account.screen;

import me.aidanmees.trivia.client.account.AccountScreen;
import me.aidanmees.trivia.client.account.account.AccountManager;
import me.aidanmees.trivia.client.account.account.Alt;
import me.aidanmees.trivia.client.account.component.Button;
import me.aidanmees.trivia.client.tools.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class ScreenAddAccount extends Screen
{
    private AddButton addButton;
    private GuiTextField emailText;
    private GuiTextField passText;
    private GuiTextField nameText;
    
    public ScreenAddAccount() {
        this.addButton = new AddButton();
        this.emailText = new GuiTextField(-5, Minecraft.fontRendererObj, Minecraft.getMinecraft().currentScreen.width / 2 - 60, Minecraft.getMinecraft().currentScreen.height / 2 - 13 - 80, 120, 26);
        this.passText = new GuiTextField(-4, Minecraft.fontRendererObj, Minecraft.getMinecraft().currentScreen.width / 2 - 60, Minecraft.getMinecraft().currentScreen.height / 2 - 13 - 40, 120, 26);
        this.nameText = new GuiTextField(-3, Minecraft.fontRendererObj, Minecraft.getMinecraft().currentScreen.width / 2 - 60, Minecraft.getMinecraft().currentScreen.height / 2 - 13, 120, 26);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        if (!this.emailText.isFocused() && !this.emailText.getText().equals("§7Username") && !this.emailText.getText().equals("§cCannot Be Blank") && this.emailText.getText().length() == 0) {
            this.emailText.setText("§7Username");
        }
        if (!this.passText.isFocused() && !this.passText.getText().equals("§7Password") && this.passText.getText().length() == 0) {
            this.passText.setText("§7Password");
        }
        if (!this.nameText.isFocused() && !this.nameText.getText().equals("§7Name") && this.nameText.getText().length() == 0) {
            this.nameText.setText("§7Name");
        }
        RenderUtils.drawRect((float)0.0, (float)0.0, Minecraft.getMinecraft().currentScreen.width, Minecraft.getMinecraft().currentScreen.height, -804253680);
        this.addButton.draw(mouseX, mouseY);
        this.emailText.drawTextBox();
        this.passText.drawTextBox();
        this.nameText.drawTextBox();
    }
    
    @Override
    public void onClick(final int x, final int y, final int mouseButton) {
        if (this.emailText.getText().equals("§7Username") || this.emailText.getText().equals("§cCannot Be Blank")) {
            this.emailText.setText("");
        }
        if (this.passText.getText().equals("§7Password")) {
            this.passText.setText("");
        }
        if (this.nameText.getText().equals("§7Name")) {
            this.nameText.setText("");
        }
        if (this.addButton.isOver()) {
            this.addButton.onClick(mouseButton);
        }
        this.emailText.mouseClicked(x, y, mouseButton);
        this.passText.mouseClicked(x, y, mouseButton);
        this.nameText.mouseClicked(x, y, mouseButton);
    }
    
    @Override
    public void onKeyPress(final char c, final int key) {
        this.emailText.textboxKeyTyped(c, key);
        this.passText.textboxKeyTyped(c, key);
        this.nameText.textboxKeyTyped(c, key);
    }
    
    @Override
    public void update() {
        this.emailText.updateCursorCounter();
        this.passText.updateCursorCounter();
        this.nameText.updateCursorCounter();
    }
    
    private class AddButton extends Button
    {
        public AddButton() {
            super("Add Account", Minecraft.getMinecraft().currentScreen.width / 2 - 40, Minecraft.getMinecraft().currentScreen.width / 2 + 40, Minecraft.getMinecraft().currentScreen.height / 2 - 13 + 40, Minecraft.getMinecraft().currentScreen.height / 2 + 13 + 40, -15921907, -16777216);
        }
        
        @Override
        public void onClick(final int button) {
            if (ScreenAddAccount.this.emailText.getText().length() == 0 || ScreenAddAccount.this.emailText.getText().equals("§7Username") || ScreenAddAccount.this.emailText.getText().equals("§cCannot Be Blank")) {
                ScreenAddAccount.this.emailText.setText("§cCannot Be Blank");
                return;
            }
            AccountManager.addAlt(0, new Alt(ScreenAddAccount.this.emailText.getText(), ScreenAddAccount.this.nameText.getText(), ScreenAddAccount.this.passText.getText()));
            AccountScreen.getInstance().currentScreen = null;
            AccountManager.save();
            AccountScreen.getInstance().initGui();
            AccountScreen.getInstance().info = "§aAlt Added";
        }
    }
}
