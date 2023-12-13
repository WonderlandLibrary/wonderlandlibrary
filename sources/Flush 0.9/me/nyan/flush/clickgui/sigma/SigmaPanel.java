package me.nyan.flush.clickgui.sigma;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.sigma.component.Component;
import me.nyan.flush.clickgui.sigma.component.components.ModuleComponent;
import me.nyan.flush.module.Module;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector2f;
import java.util.ArrayList;

public class SigmaPanel {
    private float x, y;
    private Vector2f dragVec;
    public static final float WIDTH = 100, TITLEHEIGHT = 30, HEIGHT = 130;
    private final Module.Category category;
    private final int panelColor = 0xEEF0F0F0;
    private final ArrayList<ModuleComponent> components = new ArrayList<>();
    private float scroll;

    public SigmaPanel(Module.Category category, float x, float y) {
        this.category = category;
        this.x = x;
        this.y = y;

        for (Module module : Flush.getInstance().getModuleManager().getModulesByCategory(this.category)) {
            components.add(new ModuleComponent(module));
        }
        components.sort((c, c1) -> c.getModule().getName().compareToIgnoreCase(c1.getModule().getName()));
    }

    public void init() {
        for (Module module : Flush.getInstance().getModuleManager().getModulesByCategory(this.category)) {
            boolean contains = false;
            for (ModuleComponent component : components) {
                if (component.getModule() == module) {
                    contains = true;
                    break;
                }
            }

            if (contains) {
                continue;
            }

            components.add(new ModuleComponent(module));
        }
        for (ModuleComponent component : components) {
            boolean contains = false;
            for (Module module : Flush.getInstance().getModuleManager().getModulesByCategory(this.category)) {
                if (component.getModule() == module) {
                    contains = true;
                    break;
                }
            }

            if (contains) {
                continue;
            }

            components.remove(component);
        }
        components.sort((c, c1) -> c.getModule().getName().compareToIgnoreCase(c1.getModule().getName()));

        for (Component component : components) {
            component.init();
        }
    }

    public void draw(float translatedX, float translatedY, int mouseX, int mouseY, float partialTicks, float scale, int wheel) {
        if (dragVec != null) {
            x = mouseX - dragVec.x;
            y = mouseY - dragVec.y;

            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            if (x > sr.getScaledWidth() - WIDTH) {
                x = sr.getScaledWidth() - WIDTH;
            }
            if (y > sr.getScaledHeight() - (HEIGHT + TITLEHEIGHT)) {
                y = sr.getScaledHeight() - (HEIGHT + TITLEHEIGHT);
            }
        }
        if (MouseUtils.hovered(mouseX, mouseY, x, y + TITLEHEIGHT, x + WIDTH, y + TITLEHEIGHT + HEIGHT)) {
            float max = -HEIGHT + components.size() * 15;

            if (wheel > 0 && scroll > 0) {
                scroll -= 15;
            } else if (wheel < 0) {
                scroll += 15;
            }

            scroll = components.size() * 15 < HEIGHT ? 0 : Math.min(Math.max(0, scroll), max);
        }

        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 24);
        Gui.drawRect(x, y, x + WIDTH, y + TITLEHEIGHT, panelColor);
        font.drawString(category.name, x + 10, y + TITLEHEIGHT / 2F - font.getFontHeight() / 2F, 0xFF787878);

        float y = this.y + TITLEHEIGHT;
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(translatedX + x * scale, translatedY + y * scale, translatedX + x * scale + WIDTH * scale, translatedY + y * scale + HEIGHT * scale);
        y -= scroll;
        for (Component component : components) {
            component.draw(x, y, mouseX, mouseY, partialTicks);
            y += component.getFullHeight();
        }
        Gui.drawRect(x, y, x + WIDTH, y + HEIGHT, 0xFFFAFAFA);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y, x + WIDTH, y + TITLEHEIGHT)) {
            if (button == 0 || button == 1) {
                dragVec = new Vector2f(mouseX - x, mouseY - y);
                return true;
            }
        }

        if (!MouseUtils.hovered(mouseX, mouseY, x, y + TITLEHEIGHT, x + WIDTH, y + TITLEHEIGHT + HEIGHT)) {
            return false;
        }
        float y = this.y + TITLEHEIGHT - scroll;
        for (Component component : components) {
            component.mouseClicked(x, y, mouseX, mouseY, button);
            y += component.getFullHeight();
        }
        return true;
    }

    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        dragVec = null;

        if (!MouseUtils.hovered(mouseX, mouseY, x, y, x + WIDTH, y + TITLEHEIGHT + HEIGHT)) {
            return false;
        }
        float y = this.y + TITLEHEIGHT - scroll;
        for (Component component : components) {
            component.mouseReleased(x, y, mouseX, mouseY, button);
            y += component.getFullHeight();
        }
        return true;
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        boolean cancel = false;
        for (Component component : components) {
            if (component.keyTyped(typedChar, keyCode)) {
                cancel = true;
            }
        }

        return cancel;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return MouseUtils.hovered(mouseX, mouseY, x, y, x + WIDTH, y + TITLEHEIGHT + HEIGHT);
    }

    public boolean isDragging() {
        return dragVec != null;
    }
}
