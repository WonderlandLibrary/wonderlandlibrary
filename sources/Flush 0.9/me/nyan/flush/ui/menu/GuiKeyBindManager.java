package me.nyan.flush.ui.menu;

import me.nyan.flush.Flush;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.ModuleManager;
import me.nyan.flush.ui.elements.TextBox;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

public class GuiKeyBindManager extends GuiScreen {
    private final ModuleManager moduleManager = Flush.getInstance().getModuleManager();

    private final Layout AZERTY = new Layout("FR", new Layout.Line[] {
            new Layout.Line(0, new char[] {
                    '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
            }),
            new Layout.Line(0.5F, new char[] {
                    'A', 'Z', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'
            }),
            new Layout.Line(0.8F, new char[] {
                    'Q', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M'
            }),
            new Layout.Line(1.3F, new char[] {
                    'W', 'X', 'C', 'V', 'B', 'N'
            })
    });
    private final Layout QWERTY = new Layout("EN", new Layout.Line[] {
            new Layout.Line(0, new char[] {
                    '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
            }),
            new Layout.Line(0.5F, new char[] {
                    'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'
            }),
            new Layout.Line(0.8F, new char[] {
                    'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'
            }),
            new Layout.Line(1.3F, new char[] {
                    'Z', 'X', 'C', 'V', 'B', 'N', 'M'
            })
    });
    private final Layout QWERTZ = new Layout("DE", new Layout.Line[] {
            new Layout.Line(0, new char[] {
                    '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
            }),
            new Layout.Line(0.5F, new char[] {
                    'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'I', 'O', 'P'
            }),
            new Layout.Line(0.8F, new char[] {
                    'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'
            }),
            new Layout.Line(1.3F, new char[] {
                    'Y', 'X', 'C', 'V', 'B', 'N', 'M'
            })
    });

    private final GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 22);
    private final Layout[] layouts = new Layout[] {QWERTY, AZERTY, QWERTZ};
    private Layout layout = layouts[0];

    private final int size = 35;
    private final int offset = 5;

    private MenuPanel menu;

    private ModuleSelector selector;

    public GuiKeyBindManager() {

    }

    @Override
    public void initGui() {
        menu = null;
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.fillRoundRect(getKeyboardX(), getKeyboardY(), getKeyboardWidth(), getKeyboardHeight(), 5, 0xFFDADADA);

        boolean menuHovered = isMenuHovered(mouseX, mouseY);

        float y = getKeyboardY() + offset;
        for (Layout.Line line : layout.lines) {
            float x = getKeyboardX() + offset + line.x * size;
            for (char key : line.keys) {
                String s = String.valueOf(key);
                boolean hovered = selector == null && !menuHovered && MouseUtils.hovered(mouseX, mouseY, x, y, x + size, y + size);

                RenderUtils.fillRoundRect(x + 2, y + 2, size, size, 5, 0xFFAAAAAA);
                RenderUtils.fillRoundRect(x, y, size, size, 5, hovered ? Mouse.isButtonDown(0) ? 0xFFDDDDDD : 0xFFEEEEEE : -1);
                font.drawXYCenteredString(s, x + size / 2F - 1, y + size / 2F, 0xFF000000);

                x += size + offset;
            }
            y += size + offset;
        }
        if (menu != null) {
            menu.draw(mouseX, mouseY);
        }

        boolean hovered = selector == null && MouseUtils.hovered(mouseX, mouseY, width - size - 4, height - size - 4, width - 4, height - 4);
        RenderUtils.fillRoundRect(width - size - 4, height - size - 4, size, size, 5,
                hovered ? Mouse.isButtonDown(0) ? 0xFFDDDDDD : 0xFFEEEEEE : -1);
        font.drawXYCenteredString(layout.name, width - 4 - size / 2F - 1, height - 4 - size / 2F, 0xFF000000);

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (selector != null) {
            selector.draw(mouseX, mouseY, Mouse.hasWheel() ? Mouse.getDWheel() : 0);
        }
    }

    private float getKeyboardX() {
        return width / 2F - getKeyboardWidth() / 2F;
    }

    private float getKeyboardY() {
        return height / 2F - getKeyboardHeight() / 2F;
    }

    private float getKeyboardWidth() {
        float width = 0;
        for (Layout.Line line : layout.lines) {
            if (width < line.keys.length + line.x) {
                width = line.keys.length + line.x;
            }
        }
        return width * (size + offset) + offset;
    }

    private float getKeyboardHeight() {
        return layout.lines.length * (size + offset) + offset;
    }

    private boolean isMenuHovered(int mouseX, int mouseY) {
        if (menu != null && selector == null) {
            ArrayList<Module> modules = moduleManager.getModules().stream()
                    .filter(module -> module.getKeys().contains(Keyboard.getKeyIndex(String.valueOf(menu.key))))
                    .collect(Collectors.toCollection(ArrayList::new));

            float height = (modules.size() + 1) * (menu.getSize() + menu.getOffset()) + menu.getBorder();
            return MouseUtils.hovered(mouseX, mouseY, menu.x, menu.y, menu.x + menu.getWidth(), menu.y + height);
        }
        return false;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (selector != null) {
            if (!selector.mouseClicked(mouseX, mouseY, mouseButton)) {
                selector = null;
            }
            return;
        }
        if (menu != null && !menu.mouseClicked(mouseX, mouseY, mouseButton)) {
            menu = null;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (selector != null) {
            selector.mouseReleased(mouseX, mouseY, state);
            return;
        }

        if (state != 0) {
            return;
        }

        boolean hovered = MouseUtils.hovered(mouseX, mouseY, width - size - 4, height - size - 4, width - 4, height - 4);
        if (hovered) {
            int i = 0;
            for (Layout layout : layouts) {
                i++;
                if (layout.equals(this.layout)) {
                    break;
                }
            }
            if (i >= layouts.length) {
                i = 0;
            }
            layout = layouts[i];
            menu = null;
        }

        if (isMenuHovered(mouseX, mouseY)) {
            if (menu != null) {
                menu.mouseReleased(mouseX, mouseY, state);
            }
            return;
        }

        float x2 = width / 2F - getKeyboardWidth() / 2F + offset;
        float y = height / 2F - getKeyboardHeight() / 2F + offset;

        for (Layout.Line line : layout.lines) {
            int x = (int) (x2 + line.x * size);
            for (char key : line.keys) {
                hovered = MouseUtils.hovered(mouseX, mouseY, x, y, x + size, y + size);
                if (hovered) {
                    menu = new MenuPanel(mouseX, mouseY, key);
                    break;
                }
                x += size + offset;
            }
            y += size + offset;
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (selector != null) {
            selector.keyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private static class Layout {
        private final String name;
        private final Line[] lines;

        public Layout(String name, Line[] lines) {
            this.name = name;
            this.lines = lines;
        }

        public String getName() {
            return name;
        }

        public Line[] getLines() {
            return lines;
        }

        private static class Line {
            private final char[] keys;
            private final float x;

            public Line(float x, char[] keys) {
                this.x = x;
                this.keys = keys;
            }

            public float getX() {
                return x;
            }

            public char[] getKeys() {
                return keys;
            }
        }
    }

    private class MenuPanel {
        private final float x;
        private final float y;
        private final char key;

        public MenuPanel(float x, float y, char key) {
            this.x = x;
            this.y = y;
            this.key = key;
        }

        public void draw(int mouseX, int mouseY) {
            GlyphPageFontRenderer menuFont = Flush.getFont("Roboto Light", 20);

            ArrayList<Module> modules = moduleManager.getModules().stream()
                    .filter(module -> module.getKeys().contains(Keyboard.getKeyIndex(String.valueOf(key))))
                    .collect(Collectors.toCollection(ArrayList::new));

            RenderUtils.fillRoundRect(x, y, getWidth(), getHeight(), 5, 0xFFEEEEEE);

            float mY = y + 2;
            for (Module module : modules) {
                boolean hovered = selector == null && MouseUtils.hovered(mouseX, mouseY, x + getWidth() - getBorder() * 3 - 12,
                        mY + getSize() / 2F - 6, x + getWidth() - getBorder() * 3, mY + getSize() / 2F + 6);

                RenderUtils.fillRoundRect(x + getBorder() + 1, mY + 1, getWidth() - getBorder() * 2, getSize(), 5, 0xFFDDDDDD);
                RenderUtils.fillRoundRect(x + getBorder(), mY, getWidth() - getBorder() * 2, getSize(), 5, 0xFFFFFFFF);

                float a = getSize() / 2F - menuFont.getFontHeight() / 2F;
                menuFont.drawString(module.getName(), x + getBorder() + a, mY + a, 0xFF000000);

                RenderUtils.fillCircle(x + getWidth() - getBorder() * 3 - 6, mY + getSize() / 2F, 6,
                        hovered ? Mouse.isButtonDown(0) ? 0xFFCCCCCC : 0xFFDDDDDD : 0xFFEEEEEE);
                RenderUtils.drawCross(x + getWidth() - getBorder() * 3 - 9, mY + getSize() / 2F - 3, 6, 6, 0xFF000000);
                mY += 20 + 2;
            }

            boolean hovered = selector == null && MouseUtils.hovered(mouseX, mouseY, x + getBorder(), mY, x + getWidth() - getBorder(), mY + getSize());
            RenderUtils.fillRoundRect(x + getBorder() + 1, mY + 1, getWidth() - getBorder() * 2, getSize(), 5, 0xFFDDDDDD);
            RenderUtils.fillRoundRect(x + getBorder(), mY, getWidth() - getBorder() * 2, getSize(), 5,
                    hovered ? Mouse.isButtonDown(0) ? 0xFFDDDDDD : 0xFFEEEEEE : 0xFFFFFFFF);

            RenderUtils.fillCircle(x + getWidth() / 2F, mY + getSize() / 2F, 8, 0xFFEEEEEE);

            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(0.5F);
            RenderUtils.drawLine(x + getWidth() / 2F, mY + getSize() / 2F - 5, x + getWidth() / 2F, mY + getSize() / 2F + 5, 0xFF000000);
            RenderUtils.drawLine(x + getWidth() / 2F - 5, mY + getSize() / 2F, x + getWidth() / 2F + 5, mY + getSize() / 2F, 0xFF000000);
        }

        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            if (button != 0) {
                return true;
            }
            return MouseUtils.hovered(mouseX, mouseY, getX(), getY(), getX() + getWidth(), getY() + getHeight());
        }

        public void mouseReleased(int mouseX, int mouseY, int button) {
            if (button != 0) {
                return;
            }
            ArrayList<Module> modules = moduleManager.getModules().stream()
                    .filter(module -> module.getKeys().contains(Keyboard.getKeyIndex(String.valueOf(key))))
                    .collect(Collectors.toCollection(ArrayList::new));

            float mY = y + getBorder();
            for (Module module : modules) {
                boolean hovered = MouseUtils.hovered(mouseX, mouseY, x + getWidth() - getBorder() * 3 - 12,
                        mY + getSize() / 2F - 6, x + getWidth() - getBorder() * 3, mY + getSize() / 2F + 6);
                if (hovered) {
                    module.getKeys().remove((Integer) Keyboard.getKeyIndex(String.valueOf(key)));
                    return;
                }
                mY += getSize() + getOffset();
            }

            boolean hovered = MouseUtils.hovered(mouseX, mouseY, x + getBorder(), mY, x + getWidth() - getBorder(), mY + getSize());
            if (hovered) {
                selector = new ModuleSelector(module -> {
                    module.addKey(Keyboard.getKeyIndex(String.valueOf(key)));
                    selector = null;
                });
            }
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getWidth() {
            return 100;
        }

        public float getHeight() {
            List<Module> modules = moduleManager.getModules().stream()
                    .filter(module -> module.getKeys().contains(Keyboard.getKeyIndex(String.valueOf(key))))
                    .collect(Collectors.toList());
            return (modules.size() + 1) * (getSize() + getOffset()) + getBorder();
        }

        private float getBorder() {
            return 2;
        }

        private float getOffset() {
            return 2;
        }

        private float getSize() {
            return 20;
        }
    }

    private class ModuleSelector {
        private final Minecraft mc;
        private ScaledResolution sr;
        private final Consumer<Module> onSelect;
        private float scroll;
        private float scrollSpeed;
        private final TextBox textField = new TextBox(0, 0, (int) getWidth() - getBorder() * 2, getSize());

        public ModuleSelector(Consumer<Module> onSelect) {
            mc = Minecraft.getMinecraft();
            sr = new ScaledResolution(mc);
            this.onSelect = onSelect;

            textField.setBackground(false);
            textField.setColor(-1);
            textField.setFocusedColor(-1);
        }

        public void draw(int mouseX, int mouseY, int wheel) {
            sr = new ScaledResolution(mc);

            Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xAA000000);

            GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 22);
            GlyphPageFontRenderer fontMedium = Flush.getFont("GoogleSansDisplay Medium", 24);
            GlyphPageFontRenderer titleFont = Flush.getFont("GoogleSansDisplay Medium", 40);
            GlyphPageFontRenderer fontLight = Flush.getFont("Roboto Light", 18);

            float height = getBorder();
            height += getSize() + getOffset();

            for (Module.Category category : Module.Category.values()) {
                List<Module> modules = getModules(category);
                if (modules.isEmpty()) {
                    continue;
                }

                height += fontMedium.getFontHeight() + getOffset();
                for (Module ignored : modules) {
                    height += getSize() + getOffset();
                }
                height += getOffset() * 2;
            }

            if (height > getHeight()) {
                if (MouseUtils.hovered(mouseX, mouseY, getX(), getY(), getX() + getWidth(), getY() + getHeight())) {
                    if (wheel > 0) {
                        if (scrollSpeed > 0) {
                            scrollSpeed = 0;
                        }
                        scrollSpeed -= 0.15F * Flush.getFrameTime();
                    } else if (wheel < 0) {
                        if (scrollSpeed < 0) {
                            scrollSpeed = 0;
                        }
                        scrollSpeed += 0.15F * Flush.getFrameTime();
                    }
                }

                scrollSpeed -= scrollSpeed / 100F * Flush.getFrameTime();
                scroll += scrollSpeed;

                if (scroll < 0) {
                    scroll -= scroll / 100F * Flush.getFrameTime();

                    if (scroll > 0) {
                        scroll = 0;
                    }
                }

                float max = height - getHeight();
                if (scroll > max) {
                    scroll -= (scroll - max) / 100F * Flush.getFrameTime();
                    if (scroll < max) {
                        scroll = max;
                    }
                }
            } else {
                scroll = 0;
            }

            if (Double.isNaN(scroll)) {
                scroll = 0;
            }
            if (Double.isNaN(scrollSpeed)) {
                scrollSpeed = 0;
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(getX(), getY(), 0);

            RenderUtils.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 0xFFF4F4F4);
            titleFont.drawString("Modules", 0, -titleFont.getFontHeight() - 6, -1);

            {
                float x = getBorder();
                float y = getBorder() - scroll;

                glEnable(GL_SCISSOR_TEST);
                RenderUtils.glScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());

                if (y + getSize() >= 0 && y <= getHeight()) {
                    RenderUtils.fillRoundRect(x + 1, y + 1, getWidth() - getBorder() * 2, getSize(), 5, 0xFFAAAAAA);
                    RenderUtils.fillRoundRect(x, y, getWidth() - getBorder() * 2, getSize(), 5, -1);

                    if (textField.getText().isEmpty()) {
                        float a = getSize() / 2F - fontLight.getFontHeight() / 2F;
                        fontLight.drawString("Search", x + a, y + a, 0xFF646464);
                    }

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x, y, 0);
                    textField.draw();
                    GlStateManager.popMatrix();
                }

                y += getSize() + getOffset();

                for (Module.Category category : Module.Category.values()) {
                    List<Module> modules = getModules(category);
                    if (modules.isEmpty()) {
                        continue;
                    }

                    int mHeight = modules.size() * (getSize() + getOffset()) - getOffset();
                    if (y + getSize() + mHeight >= 0 && y <= getHeight()) {
                        RenderUtils.fillRoundRect(x + 1, y + 1, getWidth() - getBorder() * 2, getSize() + mHeight, 5, 0xFFAAAAAA);
                        RenderUtils.fillRoundRect(x, y, getWidth() - getBorder() * 2, getSize() + mHeight, 5, -1);

                        fontMedium.drawString(category.name, x + getBorder(), y + getBorder(), 0xFF000000);
                    }
                    y += fontMedium.getFontHeight() + getOffset();

                    for (Module module : modules) {
                        if (y + getBorder() + getSize() >= 0 && y <= getHeight()) {
                            RenderUtils.fillRoundRect(x + getBorder() + 1, y + getBorder() + 1, getWidth() - getBorder() * 4, getSize(), 5, 0xFFAAAAAA);
                            RenderUtils.fillRoundRect(x + getBorder(), y + getBorder(), getWidth() - getBorder() * 4, getSize(), 5, 0xFFF8F8F8);

                            float a = getSize() / 2F - font.getFontHeight() / 2F;
                            font.drawString(module.getName(), x + getBorder() + a, y + getBorder() + a, 0xFF000000);

                            boolean hovered = MouseUtils.hovered(mouseX, mouseY,
                                    getX() + x + getBorder() + getWidth() - getBorder() * 4 - 8 * 3,
                                    getY() + y + getBorder() + getSize() / 2F - 8,
                                    getX() + x + getBorder() + getWidth() - getBorder() * 4 - 8,
                                    getY() + y + getBorder() + getSize() / 2F + 8);
                            RenderUtils.fillCircle(x + getBorder() + getWidth() - getBorder() * 4 - 8 * 2,
                                    y + getBorder() + getSize() / 2F, 8,
                                    hovered ? Mouse.isButtonDown(0) ? 0xFFCCCCCC : 0xFFDDDDDD : 0xFFEEEEEE);

                            GL11.glDisable(GL11.GL_LINE_SMOOTH);
                            GL11.glLineWidth(0.5F);
                            RenderUtils.drawLine(x + getBorder() + getWidth() - getBorder() * 4 - 8 * 2,
                                    y + getBorder() + getSize() / 2F - 5,
                                    x + getBorder() + getWidth() - getBorder() * 4 - 8 * 2,
                                    y + getBorder() + getSize() / 2F + 5, 0xFF000000);
                            RenderUtils.drawLine(x + getBorder() + getWidth() - getBorder() * 4 - 8 * 2 - 5,
                                    y + getBorder() + getSize() / 2F,
                                    x + getBorder() + getWidth() - getBorder() * 4 - 8 * 2 + 5,
                                    y + getBorder() + getSize() / 2F, 0xFF000000);
                        }

                        y += getSize() + getOffset();
                    }
                    y += getOffset() * 2;
                }
            }
            glDisable(GL_SCISSOR_TEST);

            GlStateManager.popMatrix();
        }

        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            textField.mouseClicked((int) (mouseX - (getX() + getBorder())), (int) (mouseY - (getY() + getBorder() - scroll)), button);

            if (button != 0) {
                return true;
            }
            return MouseUtils.hovered(mouseX, mouseY, getX(), getY(), getX() + getWidth(), getY() + getHeight());
        }

        public void mouseReleased(int mouseX, int mouseY, int button) {
            if (button != 0 || !MouseUtils.hovered(mouseX, mouseY, getX(), getY(), getX() + getWidth(), getY() + getHeight())) {
                return;
            }

            float x = getBorder();
            float y = getBorder() - scroll;

            GlyphPageFontRenderer fontMedium = Flush.getFont("GoogleSansDisplay Medium", 24);

            y += getSize() + getOffset();

            for (Module.Category category : Module.Category.values()) {
                List<Module> modules = getModules(category);
                if (modules.isEmpty()) {
                    continue;
                }

                y += fontMedium.getFontHeight() + getOffset();

                for (Module module : modules) {
                    boolean hovered = MouseUtils.hovered(mouseX, mouseY,
                            getX() + x + getBorder() + getWidth() - getBorder() * 4 - 8 * 3,
                            getY() + y + getBorder() + getSize() / 2F - 8,
                            getX() + x + getBorder() + getWidth() - getBorder() * 4 - 8,
                            getY() + y + getBorder() + getSize() / 2F + 8);
                    if (hovered) {
                        onSelect.accept(module);
                        break;
                    }
                    y += getSize() + getOffset();
                }
                y += getOffset() * 2;
            }
        }

        public float getX() {
            return sr.getScaledWidth() / 2F - getWidth() / 2F;
        }

        public float getY() {
            GlyphPageFontRenderer titleFont = Flush.getFont("GoogleSansDisplay Medium", 40);
            return sr.getScaledHeight() / 2F - getHeight() / 2F + (titleFont.getFontHeight() + 6) / 2F;
        }

        public float getWidth() {
            return 350;
        }

        public float getHeight() {
            GlyphPageFontRenderer titleFont = Flush.getFont("GoogleSansDisplay Medium", 40);
            return Math.min(400, sr.getScaledHeight() - (titleFont.getFontHeight() + 6) * 2);
        }
        
        private int getOffset() {
            return 5;
        }
        
        private int getBorder() {
            return 5;
        }
        
        private int getSize() {
            return 30;
        }

        private List<Module> getModules(Module.Category category) {
            return moduleManager.getModulesByCategory(category)
                    .stream()
                    .filter(module -> module.getName().toLowerCase().contains(textField.getText().toLowerCase()))
                    .sorted((c, c1) -> c.getName().compareToIgnoreCase(c1.getName()))
                    .collect(Collectors.toList());
        }

        public void keyTyped(char typedChar, int keyCode) {
            textField.keyTyped(typedChar, keyCode);
        }
    }
}