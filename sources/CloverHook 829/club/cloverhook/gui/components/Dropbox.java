package club.cloverhook.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.font.FontRenderer;
import club.cloverhook.utils.font.Fonts;

/**
 * @author antja03
 */
public class Dropbox extends Gui {

    private boolean droppedDown;

    private int posX;
    private int posY;
    private int width;
    private int height;

    private FontRenderer fontRenderer;

    private int optionHeight;

    private int backroundColor;
    private int optionBackgroundColor;
    private int selectedBackgroundColor;
    private int selectedColor;
    private int optionColor;

    private String[] options;
    private int currentOption;

    public Dropbox(int posX, int posY, int width, int height, FontRenderer fontRenderer, int defaultOption, String... options) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.fontRenderer = fontRenderer;

        this.currentOption = defaultOption;
        this.options = options;

        this.optionHeight = 15;
        this.backroundColor = ColorCreator.create(10, 10, 10, 255);
        this.optionBackgroundColor = ColorCreator.create(18, 18, 18);
        this.selectedBackgroundColor = ColorCreator.create(18, 18, 18);
        this.selectedColor = ColorCreator.create(220, 220, 220);
        this.optionColor = ColorCreator.create(150, 150, 150);
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseX >= posX && mouseY >= posY && mouseX <= posX + width && mouseY <= posY + height)
            droppedDown = !droppedDown;
        else
            droppedDown = false;

        ArrayList<String> uninclusiveOptions = getOptionsWithoutSelected();
        for (String string : uninclusiveOptions) {
            if (mouseX >= posX
                    && mouseY >= posY + height + uninclusiveOptions.indexOf(string) * optionHeight
                    && mouseX <= posX + width
                    && mouseY <= posY + height + uninclusiveOptions.indexOf(string) * optionHeight + optionHeight) {

                for (int i = 0; i < options.length; i++) {
                    String option = options[i];
                    if (option.equals(string))
                        currentOption = i;
                }

            }
        }
    }

    public void drawDropbox(int mouseX, int mouseY) {
        if (!droppedDown) {
            drawRect(posX, posY, posX + width, posY + height, backroundColor);
        } else {
            ArrayList<String> uninclusiveOptions = getOptionsWithoutSelected();
            drawRect(posX, posY, posX + width, posY + height, backroundColor);
            drawRect(posX, posY + height, posX + width, posY + height + uninclusiveOptions.size() * optionHeight, optionBackgroundColor);
            for (String string : uninclusiveOptions) {
                fontRenderer.drawCenteredString(string, posX + width / 2, posY + height + 1 + uninclusiveOptions.indexOf(string) * optionHeight + fontRenderer.getHeight() / 2, optionColor);
            }
        }

        fontRenderer.drawCenteredString(options[currentOption], posX + width / 2, posY + height / 2 - fontRenderer.getHeight() / 2, selectedColor);
    }

    public ArrayList<String> getOptionsWithoutSelected() {
        ArrayList<String> optionsNA = new ArrayList<>();
        for (String string : options) {
            if (string != options[currentOption]) {
                optionsNA.add(string);
            }
        }
        return optionsNA;
    }

    public String getSelected() {
        return options[currentOption];
    }

    public void setSelected(String string) {
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            if (option.equals(string))
                currentOption = i;
        }
    }

}
