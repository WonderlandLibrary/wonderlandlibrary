package koks.mainmenu.builder;

import koks.api.font.DirtyFontRenderer;
import koks.api.font.Fonts;
import koks.api.utils.RenderUtil;
import koks.mainmenu.elements.DrawnContent;
import koks.mainmenu.interfaces.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class WindowBuilder implements Element {
    String name;
    int id, width = 200, height = 200;
    boolean visible = true;
    DrawnContent content;

    public WindowBuilder(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public WindowBuilder dimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public WindowBuilder visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public WindowBuilder callback(DrawnContent content) {
        this.content = content;
        return this;
    }

    public Window build() {
        return new Window(id, name, width, height, visible, content);
    }

    public static class Window implements Element {
        /*
         * Information's about the window
         * */
        protected boolean draw = true;

        public int id;
        public String name;
        public int width, height;
        public final DrawnContent content;

        public GuiScreen currentScreen = null;

        /*
         * Window properties
         */
        public boolean visible, closeable, dragged;
        private boolean scrollable;
        public int maxScroll;
        public int scrollY;

        /*
         * Window positioning
         * */
        public int x, y;
        private int dragX, dragY;

        /*
         * Important stuff to render
         * */
        public final String closeChar = "\u0078";

        /**
         * TODO: Close and Open Animation, Implementing GuiScreens into Window, Resizable, Rounding Option, Shadow
         *
         * @param name         Name of the window (null -> no display)
         * @param width        Width of the window
         * @param visible      Visibility of the window
         * @param drawnContent Content which will be drawn on the display
         */
        public Window(int id, String name, int width, int height, boolean visible, DrawnContent drawnContent) {
            this.id = id;
            this.name = name;
            this.width = width;
            this.height = height;
            this.visible = visible;
            this.content = drawnContent;
            content.window = this;
            content.init();

            x = width + 1;
            y = height + 1;
        }

        /**
         * Make the window closeable
         */
        public Window makeCloseable() {
            closeable = true;
            return this;
        }

        /**
         * Make the window scrollable
         */
        public Window makeScrollable() {
            scrollable = true;
            return this;
        }

        /*
         * Handling the window
         * */

        /**
         * @param mouseX X-axis of the mouse
         * @param mouseY Y-axis of the mouse
         */
        public void draw(int mouseX, int mouseY) {
            if (visible) {
                handleDrag(mouseX, mouseY);
                if (draw) {
                    renderUtil.drawOutlineRect(x, y, x + width, y + height, 1F, outlineColor.getRGB(), insideColor.getRGB());
                    if (name != null) {
                        Gui.drawRect(x, y, x + width, (int) (y + titleFont.getStringHeight(name) + 2), outlineColor.darker().getRGB());
                        titleFont.drawString(name, x + width / 2F - titleFont.getStringWidth(name) / 2, y + 0.5F, Color.WHITE, false);
                    } else {
                        Gui.drawRect(x, y, x + width, (int) (y + closeFont.getStringHeight(closeChar) + 2), outlineColor.darker().getRGB());
                    }
                    if (closeable)
                        closeFont.drawString(closeChar, x + width - closeFont.getStringWidth(closeChar) - 2, y, Color.red, true);
                    GL11.glTranslated(x, y, 1);
                    if (currentScreen != null) {
                        currentScreen.width = width;
                        currentScreen.height = height - getYOffset();
                        currentScreen.drawScreen(mouseX, mouseY, Minecraft.getMinecraft().timer.renderPartialTicks);
                    }
                    GL11.glTranslated(-x, -y, 1);
                }

                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                final int yOffset = getYOffset();
                renderUtil.scissor(x, y + yOffset, x + width, y + height);
                content.draw(mouseX, mouseY, x, y + scrollY + yOffset, width, height - 2);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }
        }

        /**
         * Handle the mouse input
         */
        public void mouseInput() {
            if (scrollable && visible) {
                int wheel = Mouse.getEventDWheel();
                scrollY += wheel / 8F;
                if (scrollY > 0)
                    scrollY = 0;
                if (scrollY < -Math.abs(maxScroll)) {
                    scrollY = -Math.abs(maxScroll);
                }
            }
        }

        public void displayScreen(GuiScreen screen) {
            if(screen != null) {
                screen.mc = Minecraft.getMinecraft();
                screen.itemRender = screen.mc.getRenderItem();
                screen.fontRendererObj = screen.mc.fontRendererObj;
                screen.width = width;
                screen.height = height;
                screen.buttonList.clear();
                screen.init = true;
                screen.initGui();
            }
            this.currentScreen = screen;
        }

        /*
         * Handling mouse over
         * */

        /**
         * @param mouseX X-axis of the mouse
         * @param mouseY Y-axis of the mouse
         */
        public boolean isExit(int mouseX, int mouseY) {
            return visible && closeable && mouseX >= x + width - closeFont.getStringWidth(closeChar) - 2 && mouseX <= x + width && mouseY >= y && mouseY <= y + closeFont.getStringHeight(closeChar);
        }

        /**
         * @param mouseX X-axis of the mouse
         * @param mouseY Y-axis of the mouse
         */
        public boolean isMouseOver(int mouseX, int mouseY) {
            return visible && mouseX >= x - 2 && mouseX <= x + width + 2 && mouseY >= y && mouseY <= y + height;
        }

        /*
         * Handling dragging
         * */

        /**
         * Get the Y-Offset
         */
        public int getYOffset() {
            return !draw ? 0 : name != null ? (int) (titleFont.getStringHeight(name) + 2) : (int) (closeFont.getStringHeight(closeChar) + 2);
        }

        /**
         * @param mouseX X-axis of the mouse
         * @param mouseY Y-axis of the mouse
         */
        public void beginDrag(int mouseX, int mouseY) {
            if (visible) {
                dragX = mouseX - x;
                dragY = mouseY - y;
                dragged = true;
            }
        }

        /**
         * @param mouseX X-axis of the mouse
         * @param mouseY Y-axis of the mouse
         */
        private void handleDrag(int mouseX, int mouseY) {
            if (dragged && visible) {
                x = mouseX - dragX;
                y = mouseY - dragY;
            }
        }
    }
}
