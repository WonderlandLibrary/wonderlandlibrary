package koks.gui;

import de.liquiddev.ircclient.client.ClientType;
import de.liquiddev.ircclient.util.IrcUuid;
import koks.Koks;
import koks.api.Methods;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author kroko
 * @created on 12.02.2021 : 20:31
 */
public interface GuiSetup {

    default void init() {
        Koks.getKoks().particleManager.init();
    }

    default void draw(GuiScreen screen, Resolution resolution) {
        Color start = new Color(0x1E1D1E);
        Color end = new Color(0x161616);

        Gui.drawGradientRect(0, 0, resolution.getWidth(), resolution.getHeight(), start.getRGB(), end.getRGB());

        Koks.getKoks().particleManager.draw(0,0);
    }

    class GuiSetupIRC extends GuiScreen implements GuiSetup {

        public GuiTextField textField;

        @Override
        public void initGui() {
            init();
            final Resolution resolution = Resolution.getResolution();
            buttonList.add(new GuiButton(0, resolution.getWidth() / 2 - 50, resolution.getHeight() / 2, 100, 20, I18n.format("gui.done", new Object[0])));
            textField = new GuiTextField(1, fontRendererObj, resolution.getWidth() / 2 - 250 / 2, resolution.getHeight() / 2 - 30, 250, 20);
            super.initGui();
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            final Resolution resolution = Resolution.getResolution();
            draw(this, resolution);
            final RenderUtil renderUtil = RenderUtil.getInstance();
            textField.drawTextBox();
            final String setupIRC = "Setup - IRC Name";
            fontRendererObj.drawString(setupIRC, textField.xPosition + textField.getWidth() / 2 - fontRendererObj.getStringWidth(setupIRC) / 2, textField.yPosition - fontRendererObj.FONT_HEIGHT - 1, -1);
            final String textFieldString = "IRC Name";
            if(textField.getText().isEmpty())
                fontRendererObj.drawString(textFieldString, textField.xPosition + textField.getWidth() / 2 - fontRendererObj.getStringWidth(textFieldString) / 2, textField.yPosition + textField.getHeight() / 2 - fontRendererObj.FONT_HEIGHT / 2, -1);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            if(textField.isFocused())
                textField.textboxKeyTyped(typedChar, keyCode);
            super.keyTyped(typedChar, keyCode);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            switch (button.id) {
                case 0:
                    Koks.getKoks().irc.executeCommand(".irc nick " + textField.getText());
                    File file = new File(System.getProperty("user.home"), "koksuuid.txt");
                    String uuid = IrcUuid.getUuid(ClientType.KOKS);
                    file.createNewFile();
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                    bufferedWriter.write(uuid);
                    bufferedWriter.close();
                    mc.displayGuiScreen(new GuiMainMenu());
                    break;
            }
            super.actionPerformed(button);
        }
    }
}
