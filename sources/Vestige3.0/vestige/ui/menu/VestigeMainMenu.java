package vestige.ui.menu;

import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import vestige.Vestige;
import vestige.font.VestigeFontRenderer;
import vestige.ui.menu.components.Button;
import vestige.util.misc.AudioUtil;
import vestige.util.render.ColorUtil;
import vestige.util.render.DrawUtil;

import java.awt.Color;
import java.io.IOException;

public class VestigeMainMenu extends GuiScreen {

    private final Button[] buttons = {
            new Button("Singleplayer"),
            new Button("Multiplayer"),
            new Button("Alt login"),
            new Button("Settings"),
            new Button("Quit")
    };

    private final int textColor = new Color(220, 220, 220).getRGB();

    @Override
    public void initGui() {
        for(Button button : buttons) {
            button.updateState(false);
            button.setAnimationDone(true);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        try {
            super.actionPerformed(button);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        VestigeFontRenderer bigFont = Vestige.instance.getFontManager().getProductSans23();
        VestigeFontRenderer titleFont = Vestige.instance.getFontManager().getProductSansTitle();

        ScaledResolution sr = new ScaledResolution(mc);

        DrawUtil.renderMainMenuBackground(this, sr);

        int buttonWidth = 80;
        int buttonHeight = 20;

        int totalHeight = buttonHeight * buttons.length;

        double y = sr.getScaledHeight() / 2 - totalHeight * 0.3;

        double clientNameY = Math.max(y - 70, 40);

        DrawUtil.drawImage(new ResourceLocation("vestige/VestigeLogo.png"), sr.getScaledWidth() / 2 - 32, (int) clientNameY, 60, 16);

        int startX = sr.getScaledWidth() / 2 - buttonWidth / 2;
        int endX = sr.getScaledWidth() / 2 + buttonWidth / 2;

        for(Button button : buttons) {
            Gui.drawRect(startX, y, endX, y + buttonHeight, 0x50000000);

            button.updateState(mouseX > startX && mouseX < endX && mouseY > y && mouseY < y + buttonHeight);

            if(button.isHovered() || !button.isAnimationDone()) {
                double scale = button.getMult();

                Gui.drawRect(startX, y, startX + buttonWidth * scale, y + buttonHeight, ColorUtil.buttonHoveredColor);
            }

            String buttonName = button.getName();

            bigFont.drawStringWithShadow(buttonName, sr.getScaledWidth() / 2 - bigFont.getStringWidth(buttonName) / 2, y + 5, textColor);

            y += buttonHeight;
        }

        //String clientName = Vestige.instance.name;

        //titleFont.drawStringWithShadow(clientName, sr.getScaledWidth() / 2 - titleFont.getStringWidth(clientName) / 2, clientNameY, textColor);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mouseButton == 0) {
            int buttonWidth = 80;
            int buttonHeight = 20;

            int totalHeight = buttonHeight * buttons.length;

            ScaledResolution sr = new ScaledResolution(mc);

            double y = sr.getScaledHeight() / 2 - totalHeight * 0.3;

            int startX = sr.getScaledWidth() / 2 - buttonWidth / 2;
            int endX = sr.getScaledWidth() / 2 + buttonWidth / 2;

            for(Button button : buttons) {
                if(mouseX > startX && mouseX < endX && mouseY > y && mouseY < y + buttonHeight) {
                    switch (button.getName()) {
                        case "Singleplayer":
                            mc.displayGuiScreen(new GuiSelectWorld(this));
                            break;
                        case "Multiplayer":
                            mc.displayGuiScreen(new GuiMultiplayer(this));
                            break;
                        case "Alt login":
                            mc.displayGuiScreen(new AltLoginScreen());
                            break;
                        case "Settings":
                            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                            break;
                        case "Quit":
                            mc.shutdown();
                            break;
                    }

                    AudioUtil.buttonClick();
                }

                y += buttonHeight;
            }
        }
    }

}
