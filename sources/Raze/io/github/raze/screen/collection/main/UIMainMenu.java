package io.github.raze.screen.collection.main;

import io.github.raze.screen.system.UICustomButton;
import io.github.raze.utilities.collection.fonts.CFontUtil;
import io.github.raze.utilities.collection.visual.RoundUtil;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UIMainMenu extends GuiScreen {

    private final List<UICustomButton> buttonList;

    public UIMainMenu() {buttonList = new ArrayList<>();}

    protected void actionPerformed(UICustomButton button) throws IOException {
        switch (button.id) {
            case 1:
                mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 2:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 3:
                mc.displayGuiScreen(new UIAltManager());
                break;
            case 4:
                mc.displayGuiScreen(new GuiOptions(this,mc.gameSettings));
                break;
            case 5:
                mc.shutdown();
                break;
        }

        super.actionPerformed(button);
    }

    public void render(int mouseX, int mouseY, float partialTicks) {


        mc.getTextureManager().bindTexture(new ResourceLocation("raze/background/background.png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width + 100, height + 100, width, height);

        RoundUtil.drawSmoothRoundedRect(
                (float) (width - 220) / 2,
                (float) (height - 145) / 2,
                (float) (width + 220) / 2,
                (float) (height + 110) / 2,
                16,
                new Color(30, 30, 30, 245).getRGB()
        );

        buttonList.clear();

        UICustomButton singlePlayerBut = new UICustomButton(1, width / 2 - 100, height / 2 - 40, 200, 20, "Singleplayer", new Color(45, 45, 45, 250), new Color(75,75,75, 250), Color.WHITE);
        buttonList.add(singlePlayerBut);

        UICustomButton multiPlayerBut = new UICustomButton(2, width / 2 - 100, height / 2 - 20 + 2, 200, 20, "Multiplayer", new Color(45, 45, 45, 250), new Color(75,75,75, 250), Color.WHITE);
        buttonList.add(multiPlayerBut);

        UICustomButton altManagerBut = new UICustomButton(3, width / 2 - 100, height / 2 + 4, 200, 20, "Alt Manager", new Color(45, 45, 45, 250), new Color(75,75,75, 250), Color.WHITE);
        buttonList.add(altManagerBut);

        UICustomButton optionsBut = new UICustomButton(4, width / 2 - 100, height / 2 + 20 + 6, 99, 20, "Options", new Color(45, 45, 45, 250), new Color(75,75,75, 250), Color.WHITE);
        buttonList.add(optionsBut);

        UICustomButton quitBut = new UICustomButton(5, width / 2 + 1, height / 2 + 20 + 6, 99, 20, "Quit", new Color(45, 45, 45, 250), new Color(75,75,75, 250), Color.WHITE);
        buttonList.add(quitBut);

        for (UICustomButton button : buttonList) {
            button.drawButton(mouseX, mouseY);
        }

        CFontUtil.Jello_Light_40.getRenderer().drawString (
                "Raze",
                width / 2.0D - 25,
                height / 2.0D - 70,
                Color.WHITE
        );

        super.render(mouseX, mouseY, partialTicks);
    }

    public void click(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (UICustomButton button : buttonList) {
            if (button.mousePressed(mc, mouseX, mouseY)) {
                actionPerformed(button);
                break;
            }
        }

        super.click(mouseX, mouseY, mouseButton);
    }

}