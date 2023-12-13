package de.tired.base.guis.mainmenu;

import de.tired.util.render.AnimationUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

public class MainMenu extends GuiScreen {

    private boolean isHoveringLeftBar;

    private int leftBarWidth = 30, barHeight = height;

    private float animation;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawDefaultBackground();
        final boolean hover = isHover(0, 0, leftBarWidth, height, mouseX, mouseY);

        animation = (float) AnimationUtil.getAnimationState(animation, hover ? 100 : 10, 700 );

        this.leftBarWidth = (int) animation;

        Gui.drawRect(0, 0, leftBarWidth, height, new Color(30, 30, 30).getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    public boolean isHover(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }


    enum Buttons {

        MULTIPLAYER("Multiplayer"), SINGLEPLAYER("Singleplayer"), ALT_LOGIN("AltLogin"), QUIT("Quit");

        final String text;

        Buttons(String text) {
            this.text = text;
        }
    }

}
