package me.nyan.flush.clickgui.sigma.component.components;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.sigma.SigmaClickGui;
import me.nyan.flush.clickgui.sigma.component.Component;
import me.nyan.flush.module.Module;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.util.ArrayList;

public class SettingsPanel {
    private final Module module;
    private final ArrayList<Component> settings;
    private final GlyphPageFontRenderer fontBold = Flush.getFont("GoogleSansDisplay Medium", 40);
    private float animation;
    private boolean closing;
    private float speed = 0.01F;
    private float scroll;
    public ScaledResolution sr;

    public SettingsPanel(ModuleComponent moduleComponent) {
        this.module = moduleComponent.getModule();
        this.settings = moduleComponent.getSettings();
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (animation < 1) {
            speed -= 0.00004 * Flush.getFrameTime();
            speed = Math.max(0.0001F, speed);
        }
        if (animation > 1) {
            speed += 0.00002 * Flush.getFrameTime();
        }
        if (animation == 1) {
            speed = 0.01F;
        }
        animation += speed * Flush.getFrameTime();
        animation = Math.max(Math.min(animation, closing ? 2 : 1), 0);

        if (animation == 2) {
            if (Minecraft.getMinecraft().currentScreen instanceof SigmaClickGui) {
                ((SigmaClickGui) Minecraft.getMinecraft().currentScreen).openSettings(null);
            }
        }

        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (MouseUtils.hovered(mouseX, mouseY, getX(), getY(), getX() + getWidth(), getY() + getHeight())) {
                float max = -getHeight() + getSettingsFullHeight();

                if (wheel > 0 && scroll > 0) {
                    scroll -= 15;
                } else if (wheel < 0) {
                    scroll += 15;
                }

                scroll = getSettingsFullHeight() < getHeight() ? 0 : Math.min(Math.max(0, scroll), max);
            }
        }

        float scale = getAnimation();
        Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtils.alpha(0xFF000000, 120 * scale));
        GlStateManager.pushMatrix();
        GlStateManager.translate(sr.getScaledWidth() / 2F - (getWidth() / 2F) * scale, sr.getScaledHeight() / 2F - (getHeight() / 2F) * scale + fontBold.getFontHeight() / 2F - scroll, 1);
        GlStateManager.scale(scale, scale, 1);
        fontBold.drawString(module.getName(), 0, -fontBold.getFontHeight() - 6, ColorUtils.alpha(-1, scale * 255));
        RenderUtils.fillRoundRect(0, 0, getWidth(), getHeight(), 5, ColorUtils.alpha(-1, scale * 255));

        int y = 15;
        for (Component setting : settings) {
            setting.draw(15, y, mouseX, mouseY, partialTicks);
            y += setting.getFullHeight();
        }
        GlStateManager.popMatrix();
    }

    private int getSettingsFullHeight() {
        int height = 0;
        for (Component setting : settings) {
            height += setting.getFullHeight();
        }
        return height;
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        float x = sr.getScaledWidth() / 2F - (getWidth() / 2F);
        float y = sr.getScaledHeight() / 2F - (getHeight() / 2F) + fontBold.getFontHeight() / 2F;
        if ((button == 0 || button == 1) && !MouseUtils.hovered(mouseX, mouseY, x, y, x + getWidth(), y + getHeight())) {
            closing = true;
            return;
        }
        y = 15;
        for (Component setting : settings) {
            setting.mouseClicked(getX() + 15, getY() + y, mouseX, mouseY, button);
            y += setting.getFullHeight();
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        int y = 15;
        for (Component setting : settings) {
            setting.mouseReleased(15, y, mouseX, mouseY, button);
            y += setting.getFullHeight();
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            closing = true;
            ((SigmaClickGui) Minecraft.getMinecraft().currentScreen).close();
        }
    }

    public float getX() {
        return sr.getScaledWidth() / 2F - (getWidth() / 2F);
    }

    public float getY() {
        return sr.getScaledHeight() / 2F - (getHeight() / 2F) + fontBold.getFontHeight() / 2F;
    }

    public float getWidth() {
        return 250;
    }

    public float getHeight() {
        return Math.min(300, sr.getScaledHeight() - 80);
    }

    public float getAnimation() {
        return Math.min(animation > 1 ? 1 - (animation - 1) : animation, 1);
    }
}
