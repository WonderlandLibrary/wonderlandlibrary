package wtf.diablo.gui.clickGui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import wtf.diablo.Diablo;
import wtf.diablo.gui.clickGui.impl.Panel;
import wtf.diablo.gui.clickGui.impl.userinfo.UserInfo;
import wtf.diablo.gui.clickGui.impl.visualpreview.VisualPreview;
import wtf.diablo.gui.config.ConfigMenu;
import wtf.diablo.module.data.Category;
import wtf.diablo.utils.glstuff.BlurUtils;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGui extends GuiScreen {

    public ArrayList<Panel> panels = new ArrayList<>();
    public VisualPreview visualPreview;
    public UserInfo userInfo;
    public GuiButton configButton;
    public int lastMouseX, lastMouseY;

    public ClickGui() {
        int count = 0;
        for (Category c : Category.values()) {
            panels.add(new Panel(10 + (count * 105), c));
            count++;
        }
        visualPreview = new VisualPreview(250, 250);
        userInfo = new UserInfo();
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

        RenderUtil.drawGradientRect(0, this.width, sr.getScaledWidth(), sr.getScaledHeight(), new Color(14, 14, 14, 153).getRGB(), 0x00000000);

        for (Panel panel : panels) {
            panel.drawScreen(mouseX, mouseY, partialTicks);
        }

        //visualPreview.drawScreen(mouseX, mouseY, partialTicks);
        //userInfo.drawScreen(mouseX, mouseY, partialTicks);

        //button for config manager

        configButton.drawButton(mc, mouseX, mouseY);

        BlurUtils.drawWholeScreen();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Panel panel : panels) {
            panel.keyTyped(typedChar, keyCode);
        }
        //visualPreview.keyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Panel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
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