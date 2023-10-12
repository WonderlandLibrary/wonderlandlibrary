package wtf.diablo.gui.clickGuiMaterial;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.diablo.Diablo;
import wtf.diablo.gui.clickGui.impl.Panel;
import wtf.diablo.gui.clickGui.impl.userinfo.UserInfo;
import wtf.diablo.gui.clickGui.impl.visualpreview.VisualPreview;
import wtf.diablo.gui.clickGuiMaterial.impl.MainPanel;
import wtf.diablo.gui.config.ConfigMenu;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.impl.Render.Chams;
import wtf.diablo.utils.animations.Animation;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGui extends GuiScreen {
    public GuiButton configButton;
    public int lastMouseX, lastMouseY;
    public MainPanel mainPanel;

    public ClickGui() {
        ScaledResolution sr = new ScaledResolution(mc);

        mainPanel = new MainPanel(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2);
    }

    public void initGui() {
        configButton = new GuiButton(0, 205, 25, "Config Menu");
    }

    public void openClickGUI() {
        mc.displayGuiScreen(this);
    }

    Animation transAnim = new Animation();

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        Diablo.moduleManager.settingUpdateEvent();

        mainPanel.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        mainPanel.keyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        mainPanel.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mainPanel.mouseReleased(mouseX, mouseY, state);
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
            //mainPanel.scroll(i, lastMouseX, lastMouseY);
        }
    }
}
