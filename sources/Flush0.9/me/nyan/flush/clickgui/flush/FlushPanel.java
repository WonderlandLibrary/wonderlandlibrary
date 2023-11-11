package me.nyan.flush.clickgui.flush;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.flush.component.Component;
import me.nyan.flush.clickgui.flush.component.components.ModuleComponent;
import me.nyan.flush.module.Module;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector2f;
import java.awt.Color;
import java.util.ArrayList;

public class FlushPanel {
    private float x, y;
    private boolean expanded;
    private float expand;
    private Vector2f dragVec;
    public static final float WIDTH = 120, TITLE_HEIGHT = 20, HEIGHT = 200;
    private final Module.Category category;
    private final int panelColor = 0xFF1E1E1E;
    private final ArrayList<ModuleComponent> components = new ArrayList<>();
    private float scroll;
    private float scrollSpeed;

    public FlushPanel(Module.Category category, float x, float y) {
        this.category = category;
        this.x = x;
        this.y = y;

        ArrayList<Module> modules = new ArrayList<>(Flush.getInstance().getModuleManager().getModulesByCategory(this.category));
        modules.sort((c, c1) -> c.getName().compareToIgnoreCase(c1.getName()));
        int i = 0;
        for (Module module : modules) {
            components.add(new ModuleComponent(module, i));
            i++;
        }
    }

    public void init() {
        ArrayList<Module> modules = new ArrayList<>(Flush.getInstance().getModuleManager().getModulesByCategory(this.category));
        modules.sort((c, c1) -> c.getName().compareToIgnoreCase(c1.getName()));
        int i = 0;
        for (Module module : modules) {
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

            components.add(new ModuleComponent(module, i));
            i++;
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

        for (Component component : components) {
            component.init();
        }
    }

    public void draw(float translatedX, float translatedY, int mouseX, int mouseY, float partialTicks, float scale, float wheel) {
        float height = 0;
        for (Component component : components) {
            height += component.getFullHeight();
        }

        if (height > HEIGHT) {
            Gui.drawRect(x, y + TITLE_HEIGHT, x + WIDTH, y + TITLE_HEIGHT + HEIGHT * expand, 0xFF121212);

            if (MouseUtils.hovered(mouseX, mouseY, x, y + TITLE_HEIGHT, x + WIDTH, y + TITLE_HEIGHT + HEIGHT) && expanded) {
                if (wheel > 0) {
                    if (scrollSpeed > 0) {
                        scrollSpeed = 0;
                    }
                    scrollSpeed -= 0.5 * Flush.getFrameTime();
                } else if (wheel < 0) {
                    if (scrollSpeed < 0) {
                        scrollSpeed = 0;
                    }
                    scrollSpeed += 0.5 * Flush.getFrameTime();
                }
            }

            scrollSpeed -= scrollSpeed / 100F * Flush.getFrameTime();
            scroll += scrollSpeed;

            if (scroll < 0) {
                scroll -= scroll / 50F * Flush.getFrameTime();

                if (scroll > 0) {
                    scroll = 0;
                }
            }

            float max = height - HEIGHT;
            if (scroll > max) {
                scroll -= (scroll - max) / 50F * Flush.getFrameTime();
                if (scroll < max) {
                    scroll = max;
                }
            }
        } else {
            scroll = 0;
        }

        if (dragVec != null) {
            x = mouseX - dragVec.x;
            y = mouseY - dragVec.y;
        }

        if (!expanded) {
            expand -= expand / 100F * Flush.getFrameTime();
            if (expand < 0.01) {
                expand = 0;
            }
        } else {
            expand += ((1 - expand) / 100F) * Flush.getFrameTime();
            if (expand > 0.99) {
                expand = 1;
            }
        }
        expand = Math.max(Math.min(expand, 1), 0);

        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay Medium", 18);
        Gui.drawRect(x, y, x + WIDTH, y + TITLE_HEIGHT, panelColor);
        font.drawXYCenteredString(category.name, x + WIDTH / 2F, y + TITLE_HEIGHT / 2F, 0xFFF2F2F2);

        if (expand == 0) {
            return;
        }

        for (Component component : components) {
            component.update();
        }

        float y = this.y + TITLE_HEIGHT;
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor((x + translatedX) * scale, (y + translatedY - 1) * scale,
                (x + translatedX + WIDTH) * scale, (getScissorY() + translatedY) * scale);
        y -= scroll;
        for (Component component : components) {
            component.draw(x, y, mouseX, mouseY, partialTicks);
            y += component.getFullHeight();
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
    }

    private float getScissorY() {
        float currentY = y + TITLE_HEIGHT;
        for (Component component : components) {
            currentY += component.getFullHeight();
        }
        return Math.min(y + TITLE_HEIGHT + HEIGHT * expand, currentY);
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y, x + WIDTH, y + TITLE_HEIGHT)) {
            if (button == 0) {
                dragVec = new Vector2f(mouseX - x, mouseY - y);
            }
            if (button == 1) {
                expanded = !expanded;
            }
        }

        if (expand != 1 || !MouseUtils.hovered(mouseX, mouseY, x, y + TITLE_HEIGHT, x + WIDTH, y + TITLE_HEIGHT + HEIGHT)) {
            return;
        }

        float y = this.y + TITLE_HEIGHT - scroll;
        for (Component component : components) {
            component.mouseClicked(x, y, mouseX, mouseY, button);
            y += component.getFullHeight();
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (button == 0) {
            dragVec = null;
        }

        if (expand != 1) {
            return;
        }

        float y = this.y + TITLE_HEIGHT - scroll;
        for (Component component : components) {
            component.mouseReleased(x, y, mouseX, mouseY, button);
            y += component.getFullHeight();
        }
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        if (!expanded) {
            return false;
        }

        boolean cancel = false;
        for (Component component : components) {
            if (component.keyTyped(typedChar, keyCode)) {
                cancel = true;
            }
        }

        return cancel;
    }
}
