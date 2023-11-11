package me.nyan.flush.customhud;

import com.google.common.collect.Lists;
import me.nyan.flush.Flush;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.Setting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class GuiConfigureHud extends GuiScreen {
    private final CustomHud customHud = Flush.getInstance().getCustomHud();
    private Vector2f dragVec;
    private Component dragging;

    private Vector2f resizeVec;
    private Component resizing;

    private Component selected;
    private static float panelX = 60, panelY = 60;
    private final float panelHeight = 320;
    private Vector2f panelDragVec;

    private float scroll;
    private float scrollSpeed;

    @Override
    public void initGui() {
        selected = null;
        customHud.load();
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        customHud.save();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float wheel = Mouse.hasWheel() ? Mouse.getDWheel() : 0;
        drawComponents(mouseX, mouseY);
        drawPanel(mouseX, mouseY, partialTicks, wheel);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPanel(int mouseX, int mouseY, float partialTicks, float wheel) {
        boolean selecting = selected != null;
        float totalHeight = 20;
        if (selecting) {
            for (Setting setting : selected.getSettings()) {
                if (!setting.shouldShow()) {
                    continue;
                }
                totalHeight += setting.getFullHeight();
            }
        } else {
            for (Map.Entry<String, Class<? extends Component>> ignored : customHud.getMap().entrySet()) {
                totalHeight += 20;
            }
        }

        if (totalHeight > panelHeight) {
            Gui.drawRect(panelX, panelY + 20, panelX + 120, panelY + panelHeight, 0xFF121212);

            if (MouseUtils.hovered(mouseX, mouseY, panelX, panelY + 20, panelX + 120, panelY + panelHeight)) {
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

            float max = totalHeight - panelHeight;
            if (scroll > max) {
                scroll -= (scroll - max) / 50F * Flush.getFrameTime();
                if (scroll < max) {
                    scroll = max;
                }
            }
        } else {
            scroll = 0;
        }

        if (panelDragVec != null) {
            panelX = mouseX - panelDragVec.x;
            panelY = mouseY - panelDragVec.y;
        }

        float x = panelX;
        float y = panelY;
        Gui.drawRect(x, y, x + 120, y + 20, 0xFF1E1E1E);

        String title = "";
        if (selecting) {
            for (Map.Entry<String, Class<? extends Component>> entry : customHud.getMap().entrySet()) {
                if (entry.getValue().equals(selected.getClass())) {
                    title = entry.getKey();
                    break;
                }
            }
        } else {
            title = "Add...";
        }
        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 18);
        Flush.getFont("GoogleSansDisplay Medium", 20)
                .drawXYCenteredString(title, x + 120 / 2F, y + 20 / 2F, 0xFFF2F2F2);

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(x, y, x + 120, getScissorY());

        y += 20;
        y -= scroll;

        if (selecting) {
            for (Setting setting : selected.getSettings()) {
                if (!setting.shouldShow()) {
                    continue;
                }
                setting.draw(x, y, mouseX, mouseY, partialTicks);
                y += setting.getFullHeight();
            }
        } else {
            for (Map.Entry<String, Class<? extends Component>> entry : customHud.getMap().entrySet()) {
                Gui.drawRect(x, y, x + 120, y + 20, 0xFF121212);
                font.drawXYCenteredString(entry.getKey(), x + 120 / 2F, y + 20 / 2F, 0xFFF2F2F2);
                y += 20;
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
    }

    private float getScissorY() {
        float currentY = panelY + 20;
        if (selected != null) {
            for (Setting setting : selected.getSettings()) {
                if (!setting.shouldShow()) {
                    continue;
                }
                currentY += setting.getFullHeight();
            }
        } else {
            for (Map.Entry<String, Class<? extends Component>> ignored : customHud.getMap().entrySet()) {
                currentY += 20;
            }
        }
        return Math.min(panelY + 20 + panelHeight, currentY);
    }

    private void drawComponents(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        for (Component component : customHud.getComponents()) {
            GlStateManager.pushMatrix();

            RenderUtils.drawRectangle(
                    component.getScaledX(),
                    component.getScaledY() - 1,
                    component.getWidth() + 1,
                    component.getHeight() + 1,
                    0xFFAAAAAA, 0.5F
            );

            Gui.drawRect(
                    component.getScaledX(),
                    component.getScaledY(),
                    component.getScaledX() + component.getWidth(),
                    component.getScaledY() + component.getHeight(),
                    component == selected ? 0xAA000000 : 0x33000000
            );

            RenderUtils.glColor(-1);

            if (component.getResizeType() != Component.ResizeType.NONE) {
                GlStateManager.scale(
                        component.getScaleX(),
                        (component.getResizeType() != Component.ResizeType.CUSTOM ? component.getScaleX() : component.getScaleY()),
                        1
                );
                component.draw(
                        component.getScaledX() / component.getScaleX(),
                        component.getScaledY() / (component.getResizeType() != Component.ResizeType.CUSTOM ? component.getScaleX() : component.getScaleY())
                );
            } else {
                component.draw(component.getScaledX(), component.getScaledY());
            }
            GlStateManager.popMatrix();
        }

        if (resizing != null) {
            float scaleX = (mouseX - resizeVec.x) / resizing.width();
            float scaleY = (mouseY - resizeVec.y) / resizing.height();

            GL11.glLineWidth(1);

            if (scaleX > 0.9 && scaleX < 1.1) {
                scaleX = 1;
                RenderUtils.drawLine(
                        resizing.getScaledX(),
                        resizing.getScaledY() + resizing.getHeight() / 2F,
                        resizing.getScaledX() + resizing.getWidth(),
                        resizing.getScaledY() + resizing.getHeight() / 2F,
                        -1
                );
            }
            if (scaleY > 0.9 && scaleY < 1.1) {
                scaleY = 1;
                RenderUtils.drawLine(
                        resizing.getScaledX() + resizing.getWidth() / 2F,
                        resizing.getScaledY(),
                        resizing.getScaledX() + resizing.getWidth() / 2F,
                        resizing.getScaledY() + resizing.getHeight(),
                        -1
                );
            }

            if (resizing.getResizeType() == Component.ResizeType.CUSTOM && Math.abs(scaleX - scaleY) < 0.1) {
                scaleX = scaleY = (scaleX + scaleY) / 2;
                RenderUtils.drawLine(
                        resizing.getScaledX(),
                        resizing.getScaledY(),
                        resizing.getScaledX() + resizing.getWidth(),
                        resizing.getScaledY() + resizing.getHeight(),
                        -1
                );
            }

            resizing.setScaleX(scaleX);
            resizing.setScaleY(scaleY);
        }

        if (dragging != null) {
            double x = Math.max(Math.min((mouseX - dragVec.x) / (float) (sr.getScaledWidth() - dragging.getWidth()), 1), 0);
            double y = Math.max(Math.min((mouseY - dragVec.y) / (float) (sr.getScaledHeight() - dragging.getHeight()), 1), 0);
            dragging.setX(x);
            dragging.setY(y);

            GL11.glLineWidth(1);

            double[] positions = new double[] {0, 0.5, 1};
            for (double position : positions) {
                if (Math.abs(dragging.getX() - position) < 0.005) {
                    dragging.setX(position);
                    RenderUtils.drawLine(
                            (float) position * sr.getScaledWidth(),
                            0,
                            (float) position * sr.getScaledWidth(),
                            sr.getScaledHeight(),
                            -1
                    );
                }
                if (Math.abs(dragging.getY() - position) < 0.005) {
                    dragging.setY(position);
                    RenderUtils.drawLine(0,
                            (float) position * sr.getScaledHeight(),
                            sr.getScaledWidth(),
                            (float) position * sr.getScaledHeight(),
                            -1
                    );
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean selecting = selected != null;

        boolean unselect = !MouseUtils.hovered(mouseX, mouseY, panelX, panelY, panelX + 120, panelY + panelHeight);

        if (MouseUtils.hovered(mouseX, mouseY, panelX, panelY, panelX + 120, panelY + 20)) {
            if (mouseButton == 0) {
                panelDragVec = new Vector2f(mouseX - panelX, mouseY - panelY);
                return;
            }
        }

        float y = panelY + 20 - scroll;
        if (MouseUtils.hovered(mouseX, mouseY, panelX, panelY + 20, panelX + 120, panelY + panelHeight)) {
            if (selecting) {
                for (Setting setting : selected.getSettings()) {
                    if (!setting.shouldShow()) {
                        continue;
                    }
                    setting.mouseClicked(panelX, y, mouseX, mouseY, mouseButton);
                    if (MouseUtils.hovered(mouseX, mouseY, panelX, y, panelX + setting.getWidth(), y + setting.getFullHeight())) {
                        return;
                    }
                    y += setting.getFullHeight();
                }
            } else {
                try {
                    if (mouseButton == 0) {
                        for (Map.Entry<String, Class<? extends Component>> entry : customHud.getMap().entrySet()) {
                            if (MouseUtils.hovered(mouseX, mouseY, panelX, y, panelX + 120, y + 20)) {
                                Component component = entry.getValue().newInstance();
                                customHud.getComponents().add(component);
                                selected = component;
                                return;
                            }
                            y += 20;
                        }
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if (unselect) {
            selected = null;
        }

        for (Component component : Lists.reverse(customHud.getComponents())) {
            if (MouseUtils.hovered(mouseX, mouseY, component.getScaledX(), component.getScaledY(),
                    component.getScaledX() + component.getWidth(), component.getScaledY() + component.getHeight()) &&
                    mouseButton == 0) {
                if (MouseUtils.hovered(mouseX, mouseY,
                        component.getScaledX() + component.getWidth() - 5,
                        component.getScaledY() + component.getHeight() - 5,
                        component.getScaledX() + component.getWidth(),
                        component.getScaledY() + component.getHeight()
                ) && component.getResizeType() != Component.ResizeType.NONE) {
                    resizeVec = new Vector2f(component.getScaledX(), component.getScaledY());
                    resizing = component;
                    break;
                }
                dragVec = new Vector2f(mouseX - component.getScaledX(), mouseY - component.getScaledY());
                dragging = component;
                selected = component;
                break;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (dragging != null) {
            dragging = null;
            customHud.save();
        }

        if (resizing != null) {
            resizing = null;
            customHud.save();
        }

        if (state == 0) {
            panelDragVec = null;
        }

        if (selected != null) {
            int y = 40;
            for (Setting setting : selected.getSettings()) {
                if (!setting.shouldShow()) {
                    continue;
                }
                setting.mouseReleased(40, y, mouseX, mouseY, state);
                y += setting.getFullHeight();
            }
            customHud.save();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (selected != null) {
            boolean cancel = false;
            for (Setting setting : selected.getSettings()) {
                if (!setting.shouldShow()) {
                    continue;
                }
                if (setting.keyTyped(typedChar, keyCode)) {
                    cancel = true;
                }
            }

            if (!cancel) {
                if (keyCode == Keyboard.KEY_DELETE) {
                    customHud.getComponents().remove(selected);
                    selected.dispose();
                    selected = null;
                    customHud.save();
                    return;
                }

                float i = 1;
                if (GuiScreen.isShiftKeyDown()) {
                    i = 10;
                }
                if (GuiScreen.isCtrlKeyDown()) {
                    i = 0.5F;
                }

                if (keyCode == Keyboard.KEY_LEFT) {
                    selected.setScaledX(selected.getScaledX() - i);
                    customHud.save();
                }
                if (keyCode == Keyboard.KEY_RIGHT) {
                    selected.setScaledX(selected.getScaledX() + i);
                    customHud.save();
                }
                if (keyCode == Keyboard.KEY_UP) {
                    selected.setScaledY(selected.getScaledY() - i);
                    customHud.save();
                }
                if (keyCode == Keyboard.KEY_DOWN) {
                    selected.setScaledY(selected.getScaledY() + i);
                    customHud.save();
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
