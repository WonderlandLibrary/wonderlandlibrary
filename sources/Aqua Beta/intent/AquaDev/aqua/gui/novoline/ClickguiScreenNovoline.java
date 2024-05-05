package intent.AquaDev.aqua.gui.novoline;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.fr.lavache.anime.Animate;
import intent.AquaDev.aqua.gui.novoline.components.CategoryPaneNovoline;
import intent.AquaDev.aqua.gui.novoline.configScreen.ConfigScreenNovoline;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.utils.ColorUtils;
import intent.AquaDev.aqua.utils.RenderUtil;
import intent.AquaDev.aqua.utils.Translate;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButton2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class ClickguiScreenNovoline
extends GuiScreen {
    Translate translate;
    private ResourceLocation resourceLocation;
    public static boolean awaitingClose = true;
    private final List<CategoryPaneNovoline> categoryPanes = new ArrayList();
    private final GuiScreen parentScreen;
    public CategoryPaneNovoline current = null;
    private final Animate animate = new Animate();

    public ClickguiScreenNovoline(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    public void initGui() {
        try {
            File file = new File(System.getProperty((String)"user.dir") + "/" + Aqua.name + "//pic/" + Aqua.setmgr.getSetting("GuiElementsMode").getCurrentMode() + ".png");
            BufferedImage bi = ImageIO.read((File)file);
            this.resourceLocation = Minecraft.getMinecraft().getRenderManager().renderEngine.getDynamicTextureLocation("name", new DynamicTexture(bi));
        }
        catch (Exception file) {
            // empty catch block
        }
        this.translate = new Translate(0.0f, 0.0f);
        int x = 10;
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.buttonList.add((Object)new GuiButton2(2, 0, sr.getScaledHeight() - 55, 200, 200, "Ghost"));
        for (Category category : Category.values()) {
            CategoryPaneNovoline categoryPane = new CategoryPaneNovoline(x, 10, 100, 20, category, this);
            Aqua.INSTANCE.fileUtil.loadClickGui(categoryPane);
            this.categoryPanes.add((Object)categoryPane);
            x += 110;
        }
        awaitingClose = false;
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledRes = new ScaledResolution(this.mc);
        float posX = (float)Aqua.setmgr.getSetting("GuiElementsPosX").getCurrentNumber();
        float posY = (float)Aqua.setmgr.getSetting("GuiElementsPosY").getCurrentNumber();
        float width1 = (float)Aqua.setmgr.getSetting("GuiElementsWidth").getCurrentNumber();
        float height1 = (float)Aqua.setmgr.getSetting("GuiElementsHeight").getCurrentNumber();
        float alpha1 = (float)Aqua.setmgr.getSetting("GuiElementsBackgroundAlpha").getCurrentNumber();
        if (Aqua.moduleManager.getModuleByName("GuiElements").isToggled()) {
            if (Aqua.setmgr.getSetting("GuiElementsCustomPic").isState()) {
                try {
                    RenderUtil.drawImage((int)((int)((float)scaledRes.getScaledWidth() - this.animate.getValue() - posX)), (int)((int)((float)scaledRes.getScaledHeight() - posY)), (int)((int)width1), (int)((int)height1), (ResourceLocation)this.resourceLocation);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                int color = Aqua.setmgr.getSetting("HUDColor").getColor();
                Color colorAlpha = ColorUtils.getColorAlpha((int)color, (int)((int)alpha1));
                if (Aqua.setmgr.getSetting("GuiElementsBackgroundColor").isState()) {
                    Gui.drawRect2((double)0.0, (double)0.0, (double)this.mc.displayWidth, (double)this.mc.displayHeight, (int)colorAlpha.getRGB());
                }
                Blur.drawBlurred(() -> Gui.drawRect((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight, (int)-1), (boolean)false);
            }
            if (Aqua.setmgr.getSetting("GuiElementsCustomPic").isState()) {
                try {
                    RenderUtil.drawImage((int)((int)((float)scaledRes.getScaledWidth() - this.animate.getValue() - posX)), (int)((int)((float)scaledRes.getScaledHeight() - posY)), (int)((int)width1), (int)((int)height1), (ResourceLocation)this.resourceLocation);
                }
                catch (Exception color) {
                    // empty catch block
                }
            }
        }
        this.translate.interpolate((float)width, (float)height, 4.0);
        double xmod = (float)width / 2.0f - this.translate.getX() / 2.0f;
        double ymod = (float)height / 2.0f - this.translate.getY() / 2.0f;
        GlStateManager.translate((double)xmod, (double)ymod, (double)0.0);
        GlStateManager.scale((float)(this.translate.getX() / (float)width), (float)(this.translate.getY() / (float)height), (float)1.0f);
        if (awaitingClose) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtil.drawRoundedRect2Alpha((double)5.0, (double)(sr.getScaledHeight() - 55), (double)35.0, (double)35.0, (double)5.0, (Color)new Color(0, 0, 0, 100));
        Aqua.INSTANCE.comfortaa4.drawStringWithShadow("Config", 7.5f, (float)(sr.getScaledHeight() - 43), -1);
        GlStateManager.pushMatrix();
        if (this.parentScreen != null) {
            this.parentScreen.drawScreen(mouseX, mouseY, partialTicks);
        }
        for (CategoryPaneNovoline categoryPane : this.categoryPanes) {
            categoryPane.draw(categoryPane.getX(), categoryPane.getY(), mouseX, mouseY);
        }
        for (CategoryPaneNovoline categoryPane : this.categoryPanes) {
            if (!Mouse.isButtonDown((int)0) || !this.mouseOver(mouseX, mouseY, categoryPane.getX(), categoryPane.getY(), categoryPane.getWidth(), categoryPane.getHeight()) && this.current != categoryPane || this.current != categoryPane && this.current != null) continue;
            this.current = categoryPane;
            categoryPane.setX(mouseX - categoryPane.getWidth() / 2);
            categoryPane.setY(mouseY - categoryPane.getHeight() / 2);
        }
        if (!Mouse.isButtonDown((int)0)) {
            this.current = null;
        }
        GlStateManager.popMatrix();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (CategoryPaneNovoline categoryPane : this.categoryPanes) {
            categoryPane.clickMouse(mouseX, mouseY, mouseButton);
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (CategoryPaneNovoline categoryPane : this.categoryPanes) {
            categoryPane.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (CategoryPaneNovoline categoryPane : this.categoryPanes) {
            categoryPane.mouseReleased(mouseX, mouseY, state);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 || keyCode == Aqua.moduleManager.getModuleByName("GUI").getKeyBind()) {
            awaitingClose = true;
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    private boolean mouseOver(int x, int y, int modX, int modY, int modWidth, int modHeight) {
        return x >= modX && x <= modX + modWidth && y >= modY && y <= modY + modHeight;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 2) {
            this.mc.displayGuiScreen((GuiScreen)new ConfigScreenNovoline());
        }
    }

    public List<CategoryPaneNovoline> getCategoryPanes() {
        return this.categoryPanes;
    }
}
