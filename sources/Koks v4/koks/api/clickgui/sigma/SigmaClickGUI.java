package koks.api.clickgui.sigma;

import koks.Koks;
import koks.api.clickgui.Element;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author kroko
 * @created on 19.02.2021 : 03:07
 */
public class SigmaClickGUI extends GuiScreen {

    final int width = 120;
    final int height = 25;
    int lastWidth = -10;

    int settingScroll;

    boolean displaySetting;
    Module curModule;

    private final ArrayList<DrawCategory> drawCategories = new ArrayList<>();

    public SigmaClickGUI() {
        final Resolution resolution = Resolution.getResolution();
        int x = 10;
        int y = 10;

        for (Module.Category category : Module.Category.values()) {
            boolean hasModule = false;
            for (Module module : ModuleRegistry.getModules()) {
                if (module.getCategory() == category) {
                    hasModule = true;
                }
            }
            if (hasModule) {
                final DrawCategory drawCategory = new DrawCategory(category, x, y, width, height);
                drawCategories.add(drawCategory);
                if (x + drawCategory.width + 5 > resolution.getWidth() - drawCategory.width) {
                    y += drawCategory.maxPanelHeight + drawCategory.height + 20;
                    x = 10;
                } else {
                    x += width + 5;
                }
            }
        }
    }

    @Override
    public void initGui() {
        final Resolution resolution = Resolution.getResolution();
        if (lastWidth != resolution.getWidth()) {
            int x = 10;
            int y = 10;
            for (DrawCategory drawCategory : drawCategories) {
                drawCategory.x = x;
                drawCategory.y = y;
                if (x + drawCategory.width + 5 > resolution.getWidth() - drawCategory.width) {
                    y += drawCategory.maxPanelHeight + drawCategory.height + 20;
                    x = 10;
                } else {
                    x += width + 5;
                }
            }
        }
        lastWidth = resolution.getWidth();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (DrawCategory drawCategory : drawCategories)
            drawCategory.draw(mouseX, mouseY, partialTicks);

        if (displaySetting && curModule != null) {
            final Resolution resolution = Resolution.getResolution();
            final RenderUtil renderUtil = RenderUtil.getInstance();
            int width = 350;
            int height = 450;
            final Color color = new Color(21, 23, 24, 255);
            renderUtil.drawRoundedRect(resolution.getWidth() / 2D - width / 2D, resolution.getHeight() / 2D - height / 2D, width, height, 10, color);

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            renderUtil.scissor(resolution.getWidth() / 2 - width / 2, resolution.getHeight() / 2 - height / 2, resolution.getWidth() / 2 - width / 2 + width, resolution.getHeight() / 2 - height / 2 + height);
            int setY = resolution.getHeight() / 2 - height / 2 + 20;
            for (DrawCategory drawCategory : drawCategories)
                for (DrawModule drawModule : drawCategory.drawModules) {
                    final SigmaClickGUI vegaClickGUI = Koks.getKoks().sigmaClickGUI;
                    if (vegaClickGUI.displaySetting && vegaClickGUI.curModule == drawModule.module)
                        for (Element element : drawModule.elements) {
                            if (element.value.isVisible()) {
                                element.value.makeEnabled();
                                final int x = resolution.getWidth() / 2 - width / 2;
                                element.updatePosition(x - 30, setY + settingScroll, width, element.height);
                                element.drawScreen(mouseX, mouseY, partialTicks);
                                setY += element.height;
                            } else {
                                element.value.makeDisabled();
                            }
                        }
                }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean isHoverSetting(int mouseX, int mouseY) {
        int width = 350;
        int height = 450;
        final Resolution resolution = Resolution.getResolution();
        return mouseX >= resolution.getWidth() / 2 - width / 2 && mouseY >= resolution.getHeight() / 2 - height / 2 && mouseX <= resolution.getWidth() / 2 - width / 2 + width && mouseY <= resolution.getHeight() / 2 - height / 2 + height;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public boolean isClickGUI() {
        return true;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (displaySetting) {
            if (isHoverSetting(mouseX, mouseY)) {
                for (DrawCategory drawCategory : drawCategories)
                    for (DrawModule drawModule : drawCategory.drawModules) {
                        if (drawModule.module.equals(curModule)) {
                            for (Element element : drawModule.elements)
                                if (element.value.isVisible()) {
                                    element.handleResetButton(mouseX, mouseY, mouseButton);
                                    element.mouseClicked(mouseX, mouseY, mouseButton);
                                }
                        }
                    }
            } else {
                displaySetting = false;
            }
        } else
            for (DrawCategory drawCategory : drawCategories)
                drawCategory.onClick(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!displaySetting) {
            super.keyTyped(typedChar, keyCode);
        } else {
            if (keyCode == Keyboard.KEY_ESCAPE)
                displaySetting = false;
        }

        drawCategories.forEach(drawCategory -> {
            drawCategory.drawModules.forEach(drawModule -> {
                drawModule.elements.forEach(element -> {
                    element.keyTyped(typedChar, keyCode);
                });
            });
        });
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (DrawCategory drawCategory : drawCategories)
            drawCategory.onRelease();
        if (displaySetting)
            for (DrawCategory drawCategory : drawCategories)
                for (DrawModule drawModule : drawCategory.drawModules) {
                    if (drawModule.module.equals(curModule)) {
                        for (Element element : drawModule.elements)
                            element.mouseReleased(mouseX, mouseY, state);
                    }
                }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        if (Mouse.isCreated()) {
            final Resolution resolution = Resolution.getResolution();
            int i1 = resolution.getWidth();
            int j1 = resolution.getHeight();
            final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
            final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;

            if (!displaySetting) {
                for (DrawCategory drawCategory : drawCategories) {
                    if (drawCategory.isMouseOver(k1, l1))
                        drawCategory.handleMouseInput();
                }
            } else {
                if (isHoverSetting(k1, l1)) {
                    settingScroll += Mouse.getEventDWheel() / 8;
                    if (settingScroll >= 0)
                        settingScroll = 0;
                }
            }
        }
        super.handleMouseInput();
    }
}
