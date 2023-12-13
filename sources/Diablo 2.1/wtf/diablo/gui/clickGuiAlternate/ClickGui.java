package wtf.diablo.gui.clickGuiAlternate;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import wtf.diablo.Diablo;
import wtf.diablo.gui.clickGuiAlternate.impl.Panel;
import wtf.diablo.gui.clickGuiAlternate.impl.configs.ConfigTab;
import wtf.diablo.gui.config.ConfigMenu;
import wtf.diablo.module.data.Category;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.glstuff.BlurUtils;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGui extends GuiScreen {

    public ArrayList<Panel> panels = new ArrayList<>();
    public GuiButton configButton;
    public int lastMouseX, lastMouseY;
    public ConfigTab configTab;

    public ClickGui() {
        int count = 0;
        for (Category c : Category.values()) {
            panels.add(new Panel(10 + (count * 110), c));
            count++;
        }
        configTab = new ConfigTab(10 + (count * 105));
    }

    public void initGui() {
        configButton = new GuiButton(0, width - 205, height - 25, "Config Menu");
    }

    public void openClickGUI() {
        mc.displayGuiScreen(this);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        lastMouseX = mouseX;
        lastMouseY = mouseY;
        // RenderUtil.drawGlow(100, 100, 20, 0x00000000,-1);
        //Calling Update Settings Event
        Diablo.moduleManager.settingUpdateEvent();

        RenderUtil.drawGradientRect(0, -100, sr.getScaledWidth(), sr.getScaledHeight(), new Color(14, 14, 14, 153).getRGB(), 0x00000000);

        for (Panel panel : panels) {
            panel.drawScreen(mouseX, mouseY, partialTicks);
        }

        //configButton.drawButton(mc, mouseX, mouseY);
        //configTab.drawScreen(mouseX,mouseY,partialTicks);
        RenderUtil.drawRoundedRect(width - 205, height - 25,200,23,10,new Color(66, 7, 78, 200).getRGB());
        Fonts.apple24.drawString("Config Manager",width - 185, height - 23,-1);
        Fonts.SFReg18.drawString("(Why is this so large - Ai)",width - 150, height - 11,-1);

        BlurUtils.drawWholeScreen();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Panel panel : panels) {
            panel.keyTyped(typedChar, keyCode);
        }
        //visualPreview.keyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
        //configTab.keyTyped(typedChar,keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Panel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
        //configTab.mouseClicked(mouseX,mouseY,mouseButton);
        //visualPreview.mouseClicked(mouseX, mouseY, mouseButton);
        if (configButton.mousePressed(mc, mouseX, mouseY)) {
            buttonHandler(configButton.id);
        }
    }

    public void buttonHandler(int id) {
        switch (id) {
            case 0:
                new ConfigMenu(this).openGUI();
                break;
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
        //configTab.mouseReleased(mouseX,mouseY,state);
        // visualPreview.mouseReleased(mouseX, mouseY, state);
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0) {
            if (i > 1) {
                i = 1;
            }

            if (i < -1) {
                i = -1;
            }

            if (!isShiftKeyDown()) {
                i *= 7;
            }
            for (Panel panel : panels) {
                panel.scroll(i, lastMouseX, lastMouseY);
            }
        }
    }
}