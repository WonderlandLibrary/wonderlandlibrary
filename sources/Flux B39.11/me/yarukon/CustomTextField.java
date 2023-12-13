package me.yarukon;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.fontRenderer.FontUtils;
import today.flux.utility.ColorUtils;
import today.flux.utility.MathUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class CustomTextField extends Gui {
    public float x;
    public float y;
    public float width;
    public float height;

    public boolean enabled = true;
    FontUtils font;
    private String text = "";
    private String prompt;
    private int maxTextLength = 32;
    private int cursorCounter;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private boolean shiftDown;
    private boolean focused;

    public CustomTextField(FontUtils font, String prompt, float x, float y, float width, float height) {
        this.prompt = prompt;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.font = font;

        cursorCounter = 0;
    }

    public void updateScreen() {
        if (isFocused())
            cursorCounter++;
        else
            cursorCounter = 0;
    }

    public void drawTextBox(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawOutlinedRect(x, y, x + width, y + height, 1.0F, 0xff058669);
        RenderUtil.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, 0xffffffff);
        int cursorPos = this.cursorPosition - this.lineScrollOffset;
        int selectionPos = this.selectionEnd - this.lineScrollOffset;
        String text = this.font.trimStringToWidth(getText().substring(this.lineScrollOffset), this.getAdjustedWidth(), false);
        float cursorX = 5;

        if (!text.isEmpty()) {
            String s1 = cursorPos >= 0 && cursorPos <= text.length() ? text.substring(0, cursorPos) : text;
            cursorX = 5 + font.getStringWidth(s1);
        }

        //TODO Render these shits
        if(!isFocused() && text.isEmpty()) {
            FontManager.sans16.drawString(prompt, x + 5, y + (height / 2f - FontManager.sans16.getHeight() / 2f), 0xffbcbcbc);
        } else {
            RenderUtil.drawRect(x + 5, y, x + 7 + FontManager.sans14.getStringWidth(prompt), y + 1, 0xffffffff);
            FontManager.sans14.drawString(prompt, x + 6, y - (FontManager.sans14.getHeight() / 2f) + 1, 0xff058669);
        }

        if (!text.isEmpty())
            font.drawString(text, x + 5, y + (height / 2f - font.getHeight() / 2f), ColorUtils.GREY.c);

        if (isFocused()) {
            //Cursor
            if (selectionPos == cursorPos) {
                RenderUtil.drawRect(x + cursorX, y + 5, x + cursorX + 1, y + height - 5, RenderUtil.reAlpha(0xff058669, cursorCounter / 6 % 2 == 0 ? 1 : 0));
            }
        }

        if (selectionPos != cursorPos) {
            float selectionX = 5 + font.getStringWidth(text.substring(0, selectionPos));
            RenderUtil.drawRect(x + cursorX, y + 5, x + selectionX, y + height - 5, 0x800000ff);
        }
    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {
        this.setFocused(RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height));

        if (isFocused() && button == 0) {
            float i = mouseX - (x + 5);
            String s = font.trimStringToWidth(getText().substring(this.lineScrollOffset), this.getAdjustedWidth(), false);
            this.setCursorPosition(font.trimStringToWidth(s, i, false).length() + this.lineScrollOffset);
        }
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!this.canWrite())
            return;

        this.shiftDown = GuiScreen.isShiftKeyDown();
        if (GuiScreen.isKeyComboCtrlA(keyCode)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
        } else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
            this.setToClipboard(this.getSelectedText());
        } else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            String clipboardContent = this.getFromClipboard();
            if(clipboardContent != null) {
                this.writeText(clipboardContent);
            }
        } else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
            this.setToClipboard(this.getSelectedText());
            this.writeText("");
        } else {
            switch (keyCode) {
                case Keyboard.KEY_BACK:
                    this.shiftDown = false;
                    this.delete(-1);
                    this.shiftDown = GuiScreen.isShiftKeyDown();
                    break;
                case Keyboard.KEY_INSERT:
                case Keyboard.KEY_DOWN:
                case Keyboard.KEY_UP:
                case Keyboard.KEY_PRIOR:
                case Keyboard.KEY_NEXT:
                case Keyboard.KEY_DELETE:
                    this.shiftDown = false;
                    this.delete(1);
                    this.shiftDown = GuiScreen.isShiftKeyDown();
                    break;

                case Keyboard.KEY_LEFT:
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    } else {
                        this.moveCursor(-1);
                    }
                    break;
                case Keyboard.KEY_RIGHT:
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    } else {
                        this.moveCursor(1);
                    }
                    break;
                case Keyboard.KEY_HOME:
                    this.setCursorPositionZero();
                    break;
                case Keyboard.KEY_END:
                    this.setCursorPositionEnd();
                    break;
                case Keyboard.KEY_RETURN:
                    apply();
                    setFocused(false);
                    break;
                default:
                    break;
            }
        }

        if (isValidChar(typedChar)) {
            this.writeText(Character.toString(typedChar));
            cursorCounter = 0;
        }
    }

    public void apply() {
    }

    public boolean canWrite() {
        return isFocused() && this.isEnabled();
    }

    public void updateText(String text) {
        setValue(text);
    }

    public void setValue(String text) {
        this.text = text;
    }

    private void delete(int number) {
        if (GuiScreen.isCtrlKeyDown()) {
            this.deleteWords(number);
        } else {
            this.deleteFromCursor(number);
        }
    }

    public void deleteWords(int number) {
        if (!getText().isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(number) - this.cursorPosition);
            }
        }
    }

    public void deleteFromCursor(int number) {
        if (!getText().isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                int i = moveCursor(getText(), this.cursorPosition, number);
                int j = Math.min(i, this.cursorPosition);
                int k = Math.max(i, this.cursorPosition);
                if (j != k) {
                    updateText(new StringBuilder(getText()).delete(j, k).toString());
                    this.setCursorPosition(j);
                }
            }
        }
    }

    public int getNthWordFromCursor(int numWords) {
        return this.getNthWordFromPos(numWords, getCursorPosition());
    }

    private int getNthWordFromPos(int n, int pos) {
        return this.getNthWordFromPosWS(n, pos, true);
    }

    private int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
        int i = pos;
        boolean flag = n < 0;
        int j = Math.abs(n);

        for (int k = 0; i < j; ++k) {
            if (!flag) {
                int l = getText().length();
                i = getText().indexOf(32, i);
                if (i == -1) {
                    i = l;
                } else {
                    while (skipWs && i < l && getText().charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while (skipWs && i > 0 && getText().charAt(i - 1) == ' ') {
                    --i;
                }

                while (i > 0 && getText().charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }

    public void moveCursor(int number) {
        this.setCursorPosition(moveCursor(getText(), this.cursorPosition, number));
    }

    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(getText().length());
    }

    public void limitCursorPosition(int pos) {
        this.cursorPosition = MathUtils.clampValue(pos, 0, getText().length());
    }

    public void writeText(String textToWrite) {
        int min = Math.min(this.cursorPosition, this.selectionEnd);
        int max = Math.max(this.cursorPosition, this.selectionEnd);
        int maxLength = this.maxTextLength - getText().length() - (min - max);
        String s = stripInvalidChars(textToWrite);
        int length = s.length();
        if (maxLength < length) {
            s = s.substring(0, maxLength);
            length = maxLength;
        }

        updateText((new StringBuilder(getText())).replace(min, max, s).toString());
        limitCursorPosition(min + length);
        this.setSelectionPos(this.cursorPosition);
    }

    public void setSelectionPos(int position) {
        int maxScrollOffset = getText().length() - font.trimStringToWidth(getText(), getAdjustedWidth(), true).length();
        this.selectionEnd = MathUtils.clampValue(position, 0, getText().length());

        if (lineScrollOffset > maxScrollOffset)
            this.lineScrollOffset = maxScrollOffset;

        String s = font.trimStringToWidth(getText().substring(this.lineScrollOffset), getAdjustedWidth(), false);
        int offset = s.length() + lineScrollOffset;

        if (selectionEnd == lineScrollOffset) {
            this.lineScrollOffset -= font.trimStringToWidth(getText(), getAdjustedWidth(), true).length();
        }

        if (selectionEnd > offset) {
            this.lineScrollOffset += selectionEnd - offset;
        } else if (selectionEnd <= this.lineScrollOffset) {
            this.lineScrollOffset -= this.lineScrollOffset - selectionEnd;
        }

        this.lineScrollOffset = MathUtils.clampValue(this.lineScrollOffset, 0, maxScrollOffset);
    }

    public boolean isValidChar(char chr) {
        return chr != 167 && chr >= ' ' && chr != 127;
    }

    public String stripInvalidChars(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] var2 = s.toCharArray();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            char c = var2[var4];
            if (isValidChar(c)) {
                stringBuilder.append(c);
            }
        }

        return stringBuilder.toString();
    }

    public static int moveCursor(String string, int cursor, int delta) {
        int i = string.length();
        int j;

        if (delta >= 0) {
            for(j = 0; cursor < i && j < delta; ++j) {
                if (Character.isHighSurrogate(string.charAt(cursor++)) && cursor < i && Character.isLowSurrogate(string.charAt(cursor))) {
                    ++cursor;
                }
            }
        } else {
            for(j = delta; cursor > 0 && j < 0; ++j) {
                --cursor;
                if (Character.isLowSurrogate(string.charAt(cursor)) && cursor > 0 && Character.isHighSurrogate(string.charAt(cursor - 1))) {
                    --cursor;
                }
            }
        }

        return cursor;
    }

    public void setToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public String getFromClipboard() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            DataFlavor dataFlavor = DataFlavor.stringFlavor;

            if (clipboard.isDataFlavorAvailable(dataFlavor)) {
                return (String) clipboard.getData(dataFlavor);
            }
        } catch (Exception ignored) {}

        return null;
    }

    public float getAdjustedWidth() {
        return width - 10;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        if (text.length() > maxTextLength) {
            setValue(text.substring(0, maxTextLength));
        } else {
            setValue(text);
        }

        this.setCursorPositionEnd();
        this.setSelectionPos(this.cursorPosition);
    }

    public String getSelectedText() {
        int i = Math.min(this.cursorPosition, this.selectionEnd);
        int j = Math.max(this.cursorPosition, this.selectionEnd);
        return getText().substring(i, j);
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public void setCursorPosition(int pos) {
        this.limitCursorPosition(pos);
        if (!this.shiftDown) {
            this.setSelectionPos(this.cursorPosition);
        }
    }

    public int getMaxTextLength() {
        return maxTextLength;
    }

    public void setMaxTextLength(int maxTextLength) {
        this.maxTextLength = maxTextLength;
    }
}
