package wtf.dawn.ui.impl;

import net.minecraft.client.gui.GuiButton;
import wtf.dawn.alts.SessionManager;
import wtf.dawn.ui.MainMenu;
import wtf.dawn.ui.Screen;
import wtf.dawn.ui.components.TextField;
import wtf.dawn.utils.font.FontUtil;

import java.awt.*;
import java.io.IOException;

public class AltManager extends Screen {

    private TextField p1;
    private GuiButton p2;

    @Override
    public void init() {
        super.init();

        p2 = new GuiButton(2, width / 2 - 100, height / 2, "Login");
        p1 = new TextField(1,  mc.fontRendererObj, width / 2 - 70, height / 2 - 30, 150, 20);
        this.buttonList.add(p2);
    }
    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);

        this.drawDefaultBackground();

        FontUtil.normal.drawCenteredString("Beta Alt Manager", width / 2, height / 2 - 45, Color.WHITE.getRGB());

        p1.drawTextBox();

        p1.updateCursorCounter();

        if (p1.getText().isEmpty()){
            FontUtil.normal.drawString("(Cracked) Username", width / 2 - 45, height / 2 - 24, Color.GRAY.getRGB());
            p2.enabled = false;
        } else {
            p2.enabled = true;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        p1.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        p1.textboxKeyTyped(typedChar, keyCode);
        p1.updateCursorCounter();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);


            new SessionManager().offlineLogin(p1.getText());

            mc.displayGuiScreen(new MainMenu());

    }
}
