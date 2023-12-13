package koks.mainmenu;

import koks.Koks;
import koks.api.font.DirtyFontRenderer;
import koks.api.font.Fonts;
import koks.api.manager.mainmenu.MainMenu;
import koks.api.registry.file.FileRegistry;
import koks.api.utils.ColorPicker;
import koks.api.utils.Resolution;
import koks.mainmenu.builder.WindowBuilder;
import koks.mainmenu.elements.Action;
import koks.mainmenu.elements.ContextMenu;
import koks.mainmenu.elements.DrawnContent;
import koks.mainmenu.elements.Panel;
import koks.mainmenu.interfaces.Element;
import koks.shader.BackgroundShader;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Util;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Windows extends MainMenu implements Element {

    public static WindowBuilder.Window activeWindow = null;

    public static ArrayList<WindowBuilder.Window> windows = new ArrayList<>();
    private static final ContextMenu contextMenu = new ContextMenu();

    private final ArrayList<String> information = new ArrayList<>();

    private final BackgroundShader backgroundShader = new BackgroundShader();

    private final Panel panel = new Panel(mc.currentScreen);

    /*
     * TODO: Panel
     * */
    public Windows() {
        windows.add(panel);
        windows.add(new WindowBuilder(0, "Options").visible(false).dimensions(200, 200).callback(new DrawnContent() {

            final ArrayList<Option> options = new ArrayList<>();

            @Override
            public void init() {
                options.add(new Option("Client Color", new Action() {
                    @Override
                    public void doAction() {
                        final WindowBuilder.Window clientColor = getWindow(1);
                        clientColor.visible = true;
                    }
                }));

                options.add(new Option("Open Folder", new Action() {
                    @Override
                    public void doAction() {
                        final File dir = Koks.getKoks().DIR;
                        final String absolutePath = dir.getAbsolutePath();

                        if (Util.getOSType() == Util.EnumOS.OSX) {
                            try {
                                Runtime.getRuntime().exec(new String[]{"/usr/bin/open", absolutePath});
                            } catch (IOException ignored) {
                            }
                        } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                            String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", absolutePath);
                            try {
                                Runtime.getRuntime().exec(s1);
                            } catch (IOException ignored) {
                            }
                        }
                    }
                }));

                options.add(new Option("Reset Client", new Action() {
                    @Override
                    public void doAction() {
                        final File dir = Koks.getKoks().DIR;
                        if (dir.exists()) {
                            try {
                                FileUtils.deleteDirectory(dir);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!dir.exists())
                            dir.mkdirs();

                        FileRegistry.readAll();
                        FileRegistry.writeAll();
                    }
                }));
            }

            @Override
            public void click(int mouseX, int mouseY, int button) {
                int offsetY = window.y + window.getYOffset();
                for (Option option : options) {
                    final String name = option.name;
                    final int addY = (int) optionFont.getStringHeight(name);
                    if (mouseY > offsetY && mouseY < offsetY + addY) {
                        option.action.doAction();
                    }
                    offsetY += addY;
                }
            }

            @Override
            public void draw(int mouseX, int mouseY, int x, int y, int width, int height) {
                int offsetY = y;
                for (Option option : options) {
                    final String name = option.name;
                    final int addY = (int) optionFont.getStringHeight(name);
                    if (!isDragging() && isFront(window, mouseX, mouseY) && mouseY > offsetY && mouseY < offsetY + addY && mouseX >= x && mouseX <= x + width) {
                        Gui.drawRect(x, offsetY, x + width, offsetY + addY, new Color(0x141214).darker().getRGB());
                    }
                    optionFont.drawString(name, x + width / 2F - optionFont.getStringWidth(name) / 2F, offsetY, Color.white, true);
                    offsetY += addY;
                }
                final int newHeight = offsetY - y + window.getYOffset();
                window.height = MathHelper.clamp_int(newHeight, 0, 200);
                if (newHeight > 200) {
                    window.makeScrollable();
                    window.maxScroll = newHeight - window.height;
                }
            }

            @AllArgsConstructor
            class Option {
                final String name;
                final Action action;
            }
        }).build().makeCloseable());

        windows.add(new WindowBuilder(1, "Client Color").dimensions(200, 127).visible(false).callback(new DrawnContent() {

            final ColorPicker colorPicker = new ColorPicker(ColorPicker.Type.QUAD, Koks.getKoks().clientColor.getRGB());

            @Override
            public void input(char typedChar, int keyCode) {
                final Resolution resolution = Resolution.getResolution();
                int i1 = resolution.getWidth();
                int j1 = resolution.getHeight();
                final int k1 = Mouse.getX() * i1 / mc.displayWidth;
                final int l1 = j1 - Mouse.getY() * j1 / mc.displayHeight - 1;

                if (isFront(window, k1, l1) && !isDragging())
                    colorPicker.handleInput(typedChar, keyCode);
            }

            @Override
            public void click(int mouseX, int mouseY, int button) {
                if (isFront(window, mouseX, mouseY) && !isDragging())
                    colorPicker.handleClick(mouseX, mouseY, button);
            }

            @Override
            public void draw(int mouseX, int mouseY, int x, int y, int width, int height) {
                colorPicker.draw(x + 5, y + 5, 100, 100, mouseX, mouseY, Koks.getKoks().clientColor, isFront(window, mouseX, mouseY) && !isDragging());
                Koks.getKoks().clientColor = new Color(colorPicker.getCurrentValue());
            }
        }).build().makeCloseable());
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        final Resolution resolution = Resolution.getResolution();
        GlStateManager.disableCull();
        backgroundShader.use();
        backgroundShader.init();
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex2d(0, 0);
        GL11.glVertex2d(resolution.width, 0);
        GL11.glVertex2d(resolution.width, resolution.height);
        GL11.glVertex2d(0, resolution.height);
        GL11.glEnd();
        backgroundShader.unUse();

        int offsetY = resolution.height;
        for (String information : information) {
            offsetY -= infoFont.getStringHeight(information);
            infoFont.drawString(information, 2, offsetY, Color.GRAY, true);
            offsetY += 2;
        }

        windows.forEach(window -> window.draw(mouseX, mouseY));

        contextMenu.draw(mouseX, mouseY);
    }

    @Override
    public void init() {
        information.clear();

        /* Adding all authors */
        StringBuilder madeBy = new StringBuilder("Made by§8: §f");
        for (String author : Koks.authors) {
            madeBy.append(author).append(", ");
        }
        madeBy = new StringBuilder(madeBy.substring(0, madeBy.length() - 2));

        /* Add all information's */
        information.add("Based on §fMinecraft 1.8.9 §7(§cMojang AB§7)");
        information.add(madeBy.toString());
        information.add("Version§8: §f" + Koks.version);

        contextMenu.init();
    }

    @Override
    public void mouseInput() {
        windows.forEach(WindowBuilder.Window::mouseInput);
    }

    @Override
    public void mouseRelease() {
        windows.forEach(window -> window.dragged = false);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final Resolution resolution = Resolution.getResolution();
        Collections.reverse(windows);
        activeWindow = null;
        switch (mouseButton) {
            case 0:
                if (contextMenu.isMouseOver(mouseX, mouseY)) {
                    contextMenu.click(mouseX, mouseY, mouseButton);
                    contextMenu.visible = false;
                    return;
                }
                contextMenu.visible = false;
                for (WindowBuilder.Window window : windows) {
                    if (window.visible && !window.isExit(mouseX, mouseY) && mouseX >= window.x - 2 && mouseX <= window.x + window.width + 2 && mouseY >= window.y - 2 && mouseY < window.y + window.getYOffset()) {
                        window.beginDrag(mouseX, mouseY);
                        activeWindow = window;
                        resort(window);
                        break;
                    } else if (window.visible && window.isExit(mouseX, mouseY)) {
                        if (window.closeable)
                            window.visible = false;
                        break;
                    } else if (window.visible && mouseX >= window.x && mouseX <= window.x + window.width && mouseY >= window.y + window.getYOffset() && mouseY <= window.y + window.height) {
                        activeWindow = window;
                        window.content.click(mouseX, mouseY, mouseButton);
                        resort(window);
                        break;
                    }
                }
                break;
            case 1:
                boolean hoverWindow = false;
                for (WindowBuilder.Window window : windows) {
                    if (window.isMouseOver(mouseX, mouseY)) {
                        hoverWindow = true;
                        activeWindow = window;
                        break;
                    }
                }
                if (!hoverWindow) {
                    /* Context Menu */
                    contextMenu.visible = true;
                    contextMenu.x = mouseX;
                    contextMenu.y = mouseY;

                    if (contextMenu.x + contextMenu.calcWidth >= resolution.getWidth()) {
                        contextMenu.width = -contextMenu.calcWidth;
                    } else {
                        contextMenu.width = contextMenu.calcWidth;
                    }
                }
                break;
        }
        Collections.reverse(windows);
    }

    @Override
    public void keyInput(char typedChar, int keyCode) {
        if (activeWindow != null) {
            activeWindow.content.input(typedChar, keyCode);
        }

        if (keyCode == Keyboard.KEY_SPACE) {
            panel.visible = !panel.visible;
        }
    }

    @Override
    public void updateButton(GuiButton button) {

    }

    /**
     * Used to check if the window is in front
     *
     * @param window Which window will be checked if its in front
     * @param mouseX X-axis of the mouse
     * @param mouseY Y-axis of the mouse
     */
    public static boolean isFront(WindowBuilder.Window window, int mouseX, int mouseY) {
        WindowBuilder.Window front = null;
        if (contextMenu.isMouseOver(mouseX, mouseY))
            return false;
        for (WindowBuilder.Window w : windows) {
            if (w.isMouseOver(mouseX, mouseY)) {
                front = w;
            }
        }
        return front == window;
    }

    /**
     * Resort all windows to layer the current window to the front
     *
     * @param window Window which will be layered
     */
    private void resort(WindowBuilder.Window window) {
        final ArrayList<WindowBuilder.Window> newSort = new ArrayList<>();
        newSort.add(window);
        for (WindowBuilder.Window w : windows) {
            if (!newSort.contains(w)) {
                newSort.add(w);
            }
        }
        windows = newSort;
    }

    /**
     * Check if the user dragging a window
     */
    private boolean isDragging() {
        boolean isDragging = false;
        for (WindowBuilder.Window w : windows)
            if (w.dragged) {
                isDragging = true;
            }
        return isDragging;
    }

    /**
     * Get a window with the id
     */
    public static WindowBuilder.Window getWindow(int id) {
        return windows.stream().filter(window -> window.id == id).findAny().orElse(null);
    }
}
