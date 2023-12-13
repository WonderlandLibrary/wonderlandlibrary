package me.nyan.flush.ui.elements;

import me.nyan.flush.Flush;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import me.nyan.flush.utils.render.Stencil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.*;

public class TextBox extends Gui {
    private final String name;
    private float x, y, width, height;
    private boolean enabled = true, background = true, password;

    private int color = 0xFF1E1E1E, focusedColor = 0xFF353535;

    private GlyphPageFontRenderer font;
    private int fontSize = 20;

    private final StringBuilder textBuilder = new StringBuilder();
    private boolean focused;
    private int cursor;
    private boolean showCursor;
    private float focusedLevel;
    private boolean stencil = true;

    private float offset;

    private final Timer timer = new Timer();

    public TextBox(float x, float y, float width, float height) {
        this(x, y, width, height, false);
    }

    public TextBox(float x, float y, float width, float height, boolean focused) {
        this(null, x, y, width, height, focused);
    }

    public TextBox(String name, float x, float y, float width, float height) {
        this(name, x, y, width, height, false);
    }

    public TextBox(String name, float x, float y, float width, float height, boolean focused) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.focused = focused;
    }

    public void draw() {
        if (focused) {
            focusedLevel += 1 / 200F * Flush.getFrameTime();
            if (focusedLevel > 1) {
                focusedLevel = 1;
            }
        } else {
            focusedLevel -= 1 / 200F * Flush.getFrameTime();
            if (focusedLevel < 0) {
                focusedLevel = 0;
            }
        }

        if (timer.hasTimeElapsed(500, true)) {
            showCursor = !showCursor;
        }

        int textColor = ColorUtils.contrast(focused ? focusedColor : color);

        if (background) {
            glPushMatrix();
            RenderUtils.fillRoundRect(x, y, width, height, 3, color);

            if (focusedLevel > 0) {
                if (stencil) {
                    Stencil.setup();
                    RenderUtils.fillRoundRect(x, y, width, height, 3, 0);
                    Stencil.draw();
                }
                RenderUtils.fillCircle(x + width / 2f, y + height / 2f, focusedLevel * width, focusedColor);
                if (stencil) {
                    Stencil.finish();
                }
            }
            glPopMatrix();

            //RenderUtils.fillRoundRect(x, y, width, height, 6, focused ? focusedColor : color);
        }

        font = Flush.getFont("GoogleSansDisplay", fontSize);

        if (stencil) {
            Stencil.setup();
            RenderUtils.fillRectangle(x, y, x + width, y + height, 0);
            Stencil.draw();
        }

        float border = height / 2F - font.getFontHeight() / 2F;
        float x = this.x + border - offset;
        float y = this.y + border;

        if (textBuilder.length() > 0) {
            for (int i = 0; i < textBuilder.length(); i++) {
                char c = password ? '*' : textBuilder.charAt(i);
                if (i == cursor) {
                    x += drawCursor(x, y, font, textColor);
                }

                x += font.drawChar(c, x, y, enabled ? textColor : 0xFFAAAAAA);
            }
        } else if (name != null) {
            font.drawString(name, x, y, 0xFFAAAAAA);
        }

        if (cursor == textBuilder.length()) {
            drawCursor(x, y, font, textColor);
        }

        if (stencil) {
            Stencil.finish();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!enabled) {
            return;
        }

        if (mouseButton == 0) {
            focused = MouseUtils.hovered(mouseX, mouseY, x, y, x + width, y + height);

            if (focused) {
                float border = height / 2F - font.getFontHeight() / 2F;
                float x = this.x + border - offset;
                for (int i = 0; i < textBuilder.length(); i++) {
                    char c = password ? '*' : textBuilder.charAt(i);
                    if (i == cursor) {
                        x += cursorWidth();
                    }

                    x += font.getCharWidth(c);

                    if (x > mouseX) {
                        setCursor(i);
                        return;
                    }
                }
                setCursor(textBuilder.length());
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (!focused) {
            return;
        }

        if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            insert(cursor, GuiScreen.getClipboardString());
        }

        switch (keyCode) {
            case Keyboard.KEY_BACK:
                if (GuiScreen.isCtrlKeyDown()) {
                    deleteWord(-1);
                    break;
                }
                delete(cursor - 1);
                break;
            case Keyboard.KEY_DELETE:
                if (GuiScreen.isCtrlKeyDown()) {
                    deleteWord(1);
                    break;
                }
                delete(cursor);
                break;

            case Keyboard.KEY_LEFT:
                if (cursor > 0) {
                    moveCursor(-1);
                }
                break;
            case Keyboard.KEY_RIGHT:
                if (cursor < textBuilder.length()) {
                    moveCursor(1);
                }
                break;
            default:
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    insert(cursor, typedChar);
                }
        }
    }

    private void deleteWord(int direction) {
        int i = cursor - (direction < 0 ? 1 : 0);
        do {
            delete(i);
            if (direction < 0) {
                i--;
            }
        } while (i >= 0 && i < textBuilder.length() && textBuilder.charAt(i) != ' ');
    }

    private int drawCursor(float x, float y, GlyphPageFontRenderer font, int color) {
        if (focused && showCursor) {
            glLineWidth(1);
            glDisable(GL_LINE_SMOOTH);
            RenderUtils.drawLine(x + cursorWidth(), y, x + cursorWidth(), y + font.getFontHeight(), color);
        }
        return cursorWidth();
    }

    private int cursorWidth() {
        return focused ? 1 : 0;
    }

    public void insert(int offset, String text) {
        textBuilder.insert(offset, text);
        setCursor(offset + text.length());
    }

    public void insert(int offset, char c) {
        textBuilder.insert(offset, c);
        setCursor(offset + 1);
    }

    public void delete(int offset) {
        if (offset < 0 || offset >= textBuilder.length()) {
            return;
        }
        textBuilder.deleteCharAt(offset);
        setCursor(offset);
    }

    public void setCursor(int position) {
        cursor = Math.min(Math.max(position, 0), textBuilder.length());

        float width = 0;
        for (int i = 0; i < cursor; i++) {
            char c = textBuilder.charAt(i);
            if (i == cursor) {
                x += cursorWidth();
            }
            width += font.getCharWidth(c);
        }
        float border = height / 2F - font.getFontHeight() / 2F;
        if (width > (this.width - border * 2)) {
            offset = width - (this.width - border * 2);
        } else {
            offset = 0;
        }
    }

    public void moveCursor(int offset) {
        setCursor(cursor + offset);
    }

    public void setText(String text) {
        font = Flush.getFont("GoogleSansDisplay", fontSize);

        textBuilder.replace(0, textBuilder.length(), text);
        setCursor(textBuilder.length());
    }

    public String getText() {
        return textBuilder.toString();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            focused = false;
        }
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public boolean isBackground() {
        return background;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setFocusedColor(int focusedColor) {
        this.focusedColor = focusedColor;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public boolean isPassword() {
        return password;
    }

    public void setStencil(boolean stencil) {
        this.stencil = stencil;
    }
}
