/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.gui.components;

import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.font.GameFontRenderer;

public class TextEditComponent {
    String text;
    boolean password;
    int curserIndex = -1;
    int posX;
    int posY;
    int width;
    int offsetX = 5;
    int offsetY = 3;
    int height;
    int color = -1;
    GameFontRenderer fontRenderer;
    Runnable submit;
    boolean numberOnly;

    public TextEditComponent(String startText, GameFontRenderer font, Runnable submit, boolean numberOnly) {
        this.text = startText;
        this.fontRenderer = font;
        this.submit = submit;
        this.numberOnly = numberOnly;
    }

    public void setOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public boolean isTyping() {
        if (this.curserIndex == -1) return false;
        return true;
    }

    public void setTyping(boolean typing) {
        this.curserIndex = typing ? this.text.length() : -1;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.curserIndex = -1;
        this.text = text;
    }

    public void updatePosition(int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void mouseClicked(int mouseX, int mouseY) {
        String text = this.text;
        if (this.password) {
            text = text.replaceAll(".", "*");
        }
        if (mouseX <= this.posX || mouseX >= this.width || mouseY <= this.posY || mouseY >= this.height) {
            this.curserIndex = -1;
            return;
        }
        float width = this.posX + this.offsetX + 1;
        this.curserIndex = text.length();
        int i = 0;
        while (i < text.length()) {
            float half = (float)this.fontRenderer.getStringWidth(String.valueOf(text.charAt(i))) / 2.0f;
            if ((float)mouseX < (width += half)) {
                this.curserIndex = i;
                return;
            }
            width += half;
            ++i;
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.curserIndex == -1) return;
        if (keyCode == 14) {
            if (this.curserIndex == 0) return;
            String text1 = this.text.substring(0, this.curserIndex - 1);
            String text2 = this.text.substring(this.curserIndex);
            this.text = text1 + text2;
            --this.curserIndex;
            return;
        }
        if (keyCode == 28) {
            this.submit.run();
            this.curserIndex = -1;
            return;
        }
        if (keyCode == 205) {
            this.curserIndex = Math.min(this.curserIndex + 1, this.text.length());
            return;
        }
        if (keyCode == 203) {
            this.curserIndex = Math.max(0, this.curserIndex - 1);
            return;
        }
        if (!(Character.isAlphabetic(typedChar) && !this.numberOnly || Character.isDigit(typedChar) || typedChar == '.')) {
            if (typedChar != '@') return;
        }
        String text1 = this.text.substring(0, this.curserIndex);
        String text2 = "";
        if (this.text.length() != this.curserIndex) {
            text2 = this.text.substring(this.curserIndex);
        }
        ++this.curserIndex;
        this.text = text1 + typedChar + text2;
    }

    public void drawString() {
        String text = this.text;
        if (this.password) {
            text = text.replaceAll(".", "*");
        }
        this.fontRenderer.drawString(text, this.posX + this.offsetX, this.posY + this.offsetY, this.color);
        if (this.curserIndex == -1) return;
        int offset = this.fontRenderer.getStringWidth(text.substring(0, this.curserIndex));
        if (System.currentTimeMillis() % 1000L <= 500L) return;
        GuiUtil.drawQuad((float)(this.posX + offset + this.offsetX) - 0.5f, this.posY + this.offsetY - 1, 1.0f, this.fontRenderer.getStringHeight(), this.color);
    }

    public void updatePosition(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}

