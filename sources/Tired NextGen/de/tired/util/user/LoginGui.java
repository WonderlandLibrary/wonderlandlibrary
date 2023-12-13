package de.tired.util.user;

import de.tired.Tired;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class LoginGui extends GuiScreen {

    private GuiTextField email, password;

    public LoginGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        email.drawTextBox(true);
        email.setMaxStringLength(100);
        password.drawTextBox(true);
        password.setCensored(true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(1337, (int) (width / 2f) - 50, (height / 2) + 50, 100, 15, "Login"));
        buttonList.add(new GuiButton(12, (int) (width / 2f) - 50, (height / 2) + 80, 100, 15, "Register"));
        this.email = new GuiTextField(12, mc.fontRendererObj, (int) (width / 2f) - 100, (height / 2) - 50, 200, 20);
        this.password = new GuiTextField(1, mc.fontRendererObj, (int) (width / 2f) - 100, (height / 2), 200, 20);
        super.initGui();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        email.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 12) {
            Minecraft.getMinecraft().displayGuiScreen(new RegisterGui());
        }
        if (button.id == 1337) {
            Tired.INSTANCE.tiredUser = MysqlConnect.userLogin(email.getText(), password.getText());
            if (MysqlConnect.userLogin(email.getText(), password.getText()) != null) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
            }
        }

        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        email.textboxKeyTyped(typedChar, keyCode);
        password.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
}
