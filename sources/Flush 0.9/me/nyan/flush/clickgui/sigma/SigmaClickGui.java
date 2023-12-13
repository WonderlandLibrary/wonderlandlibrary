package me.nyan.flush.clickgui.sigma;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.ClickGui;
import me.nyan.flush.clickgui.sigma.component.components.ModuleComponent;
import me.nyan.flush.clickgui.sigma.component.components.SettingsPanel;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.render.ModuleClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SigmaClickGui extends ClickGui {
    private final ArrayList<SigmaPanel> panels = new ArrayList<>();
    private boolean closing;
    private float animation;
    private SettingsPanel openedSettings;
    private boolean cancelClick;

    public SigmaClickGui() {
        int x = 15;
        int y = 15;
        int i = 0;
        for (Module.Category category : Module.Category.values()) {
            i++;
            panels.add(new SigmaPanel(category, x, y));

            if (i == 4) {
                x = 15;
                y += SigmaPanel.TITLEHEIGHT + SigmaPanel.HEIGHT + 5;
                continue;
            }
            x += SigmaPanel.WIDTH + 5;
        }
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        animation = 0;
        closing = false;

        for (SigmaPanel panel : panels) {
            panel.init();
        }
    }

    @Override
    public void onResize(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        this.width = width;
        this.height = height;
        this.buttonList.clear();
        this.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        animation += (closing ? -1 : 1) * 0.014F * Flush.getFrameTime();
        animation = Math.max(Math.min(animation, 1), 0);
        //BlurUtils.blur((int) (animation * 30));

        if (animation == 0) {
            closing = false;

            mc.displayGuiScreen(null);

            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        }

        float scale = 1;
        int x = 0, y = 0;
        if (openedSettings != null) {
            openedSettings.sr = new ScaledResolution(Minecraft.getMinecraft());
            scale = openedSettings.getAnimation() * 0.04F;
            GlStateManager.pushMatrix();
            x = (int) (scale * width);
            y = (int) (scale * height);
            scale = 1 - scale * 2;
        }
        x += width / 2F * (animation - 1) / 2F;
        y += height / 2F * (animation - 1) / 2F;
        scale *= 1 - animation + 1;

        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1);

        int wheel = Mouse.hasWheel() ? Mouse.getDWheel() : 0;
        List<SigmaPanel> reversedPanels = new ArrayList<>(panels);
        SigmaPanel hoveredPanel = null;
        if (openedSettings == null) {
            for (SigmaPanel panel : reversedPanels) {
                if (panel.isHovered(mouseX, mouseY)) {
                    hoveredPanel = panel;
                    break;
                }
            }
        }
        Collections.reverse(reversedPanels);

        for (SigmaPanel panel : reversedPanels) {
            boolean cancel = hoveredPanel != panel && !panel.isDragging();
            panel.draw(x, y, cancel ? -1 : mouseX, cancel ? -1 : mouseY, partialTicks, scale, wheel);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (openedSettings != null) {
            GlStateManager.popMatrix();
            openedSettings.draw(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        cancelClick = false;
        if (openedSettings != null) {
            openedSettings.mouseClicked(mouseX, mouseY, mouseButton);
            cancelClick = true;
            return;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (animation != 1) {
            return;
        }

        for (SigmaPanel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, mouseButton)) {
                panels.remove(panel);
                panels.add(0, panel);
                break;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (openedSettings != null) {
            openedSettings.mouseReleased(mouseX, mouseY, state);
            return;
        }

        if (animation != 1 || cancelClick) {
            return;
        }

        for (SigmaPanel panel : panels) {
            if (panel.mouseReleased(mouseX, mouseY, state)) {
                break;
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (openedSettings != null) {
            openedSettings.keyTyped(typedChar, keyCode);
            return;
        }
        if (animation != 1) {
            return;
        }

        boolean cancel = false;
        for (SigmaPanel panel : panels) {
            if (panel.keyTyped(typedChar, keyCode)) {
                cancel = true;
            }
        }

        if (cancel) {
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            closing = true;
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        moduleManager.getModule(ModuleClickGui.class).disable();
    }

    public void openSettings(ModuleComponent moduleComponent) {
        if (moduleComponent == null) {
            openedSettings = null;
            return;
        }
        openedSettings = new SettingsPanel(moduleComponent);
    }

    public void close() {
        closing = true;
    }
}
