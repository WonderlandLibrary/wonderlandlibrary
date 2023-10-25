/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

public class PasswordField
extends Gui {
    private boolean canLoseFocus = true;
    public boolean isFocused = false;
    private final int height;
    private final int yPos;
    private String text = "";
    private int maxStringLength = 50;
    private final FontRenderer fontRenderer;
    private boolean isEnabled = true;
    private final int width;
    private int cursorPosition = 0;
    private boolean b = true;
    private int cursorCounter;
    private int i = 0;
    private int disabledColor = 0x707070;
    private int enabledColor = 0xE0E0E0;
    private boolean enableBackgroundDrawing = true;
    private final int xPos;
    private int selectionEnd = 0;

    public String getText() {
        String string = this.text.replaceAll(" ", "");
        return string;
    }

    public void setCanLoseFocus(boolean bl) {
        this.canLoseFocus = bl;
    }

    /*
     * Unable to fully structure code
     */
    public int type(int var1_1, int var2_2, boolean var3_3) {
        var4_4 = var2_2;
        var5_5 = var1_1 < 0;
        var6_6 = Math.abs(var1_1);
        var7_7 = 0;
        while (var7_7 < var6_6) {
            block4: {
                if (var5_5) ** GOTO lbl18
                var8_8 = this.text.length();
                if ((var4_4 = this.text.indexOf(32, var4_4)) != -1) ** GOTO lbl13
                var4_4 = var8_8;
                break block4;
                while (var4_4 < var8_8 && this.text.charAt(var4_4) == ' ') {
                    ++var4_4;
lbl13:
                    // 2 sources

                    if (var3_3) continue;
                    break block4;
                }
                break block4;
                while (var4_4 > 0 && this.text.charAt(var4_4 - 1) == ' ') {
                    --var4_4;
lbl18:
                    // 2 sources

                    if (var3_3) continue;
                }
                while (var4_4 > 0 && this.text.charAt(var4_4 - 1) != ' ') {
                    --var4_4;
                }
            }
            ++var7_7;
        }
        return var4_4;
    }

    public void cursorPos(int n) {
        this.setCursorPosition(this.selectionEnd + n);
    }

    public void func_73790_e(boolean bl) {
        this.b = bl;
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public int getNthWordFromPos(int n, int n2) {
        return this.type(n, this.getCursorPosition(), true);
    }

    public void drawTextBox() {
        if (this.func_73778_q()) {
            if (this.getEnableBackgroundDrawing()) {
                Gui.drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
                Gui.drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
            }
            int n = this.isEnabled ? this.enabledColor : this.disabledColor;
            int n2 = this.cursorPosition - this.i;
            int n3 = this.selectionEnd - this.i;
            String string = this.fontRenderer.trimStringToWidth(this.text.substring(this.i), this.getWidth());
            boolean bl = n2 >= 0 && n2 <= string.length();
            boolean bl2 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && bl;
            int n4 = this.enableBackgroundDrawing ? this.xPos + 4 : this.xPos;
            int n5 = this.enableBackgroundDrawing ? this.yPos + (this.height - 8) / 2 : this.yPos;
            int n6 = n4;
            if (n3 > string.length()) {
                n3 = string.length();
            }
            if (string.length() > 0) {
                if (bl) {
                    string.substring(0, n2);
                }
                Minecraft.getMinecraft();
                n6 = Minecraft.fontRendererObj.drawStringWithShadow(this.text.replaceAll("(?s).", "*"), n4, n5, n);
            }
            boolean bl3 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int n7 = n6;
            if (!bl) {
                n7 = n2 > 0 ? n4 + this.width : n4;
            } else if (bl3) {
                n7 = n6 - 1;
                --n6;
            }
            if (string.length() > 0 && bl && n2 < string.length()) {
                Minecraft.getMinecraft();
                Minecraft.fontRendererObj.drawStringWithShadow(string.substring(n2), n6, n5, n);
            }
            if (bl2) {
                if (bl3) {
                    Gui.drawRect(n7, n5 - 1, n7 + 1, n5 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
                } else {
                    Minecraft.getMinecraft();
                    Minecraft.fontRendererObj.drawStringWithShadow("_", n7, n5, n);
                }
            }
            if (n3 != n2) {
                int n8 = n4 + this.fontRenderer.getStringWidth(string.substring(0, n3));
                this.drawCursorVertical(n7, n5 - 1, n8 - 1, n5 + 1 + this.fontRenderer.FONT_HEIGHT);
            }
        }
    }

    public void writeText(String string) {
        int n;
        String string2 = "";
        String string3 = ChatAllowedCharacters.filterAllowedCharacters(string);
        int n2 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int n3 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int n4 = this.maxStringLength - this.text.length() - (n2 - this.selectionEnd);
        boolean bl = false;
        if (this.text.length() > 0) {
            string2 = String.valueOf(String.valueOf(String.valueOf(string2))) + this.text.substring(0, n2);
        }
        if (n4 < string3.length()) {
            string2 = String.valueOf(String.valueOf(String.valueOf(string2))) + string3.substring(0, n4);
            n = n4;
        } else {
            string2 = String.valueOf(String.valueOf(String.valueOf(string2))) + string3;
            n = string3.length();
        }
        if (this.text.length() > 0 && n3 < this.text.length()) {
            string2 = String.valueOf(String.valueOf(String.valueOf(string2))) + this.text.substring(n3);
        }
        this.text = string2.replaceAll(" ", "");
        this.cursorPos(n2 - this.selectionEnd + n);
    }

    public void func_73779_a(int n) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(n) - this.cursorPosition);
            }
        }
    }

    public PasswordField(FontRenderer fontRenderer, int n, int n2, int n3, int n4) {
        this.fontRenderer = fontRenderer;
        this.xPos = n;
        this.yPos = n2;
        this.width = n3;
        this.height = n4;
    }

    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    public void func_73794_g(int n) {
        this.enabledColor = n;
    }

    public void setCursorPosition(int n) {
        this.cursorPosition = n;
        int n2 = this.text.length();
        if (this.cursorPosition < 0) {
            this.cursorPosition = 0;
        }
        if (this.cursorPosition > n2) {
            this.cursorPosition = n2;
        }
        this.func_73800_i(this.cursorPosition);
    }

    public void setText(String string) {
        this.text = string.length() > this.maxStringLength ? string.substring(0, this.maxStringLength) : string;
        this.setCursorPositionEnd();
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    public boolean textboxKeyTyped(char c, int n) {
        if (!this.isEnabled || !this.isFocused) {
            return false;
        }
        switch (c) {
            case '\u0001': {
                this.setCursorPositionEnd();
                this.func_73800_i(0);
                return true;
            }
            case '\u0003': {
                GuiScreen.setClipboardString(this.getSelectedtext());
                return true;
            }
            case '\u0016': {
                this.writeText(GuiScreen.getClipboardString());
                return true;
            }
            case '\u0018': {
                GuiScreen.setClipboardString(this.getSelectedtext());
                this.writeText("");
                return true;
            }
        }
        switch (n) {
            case 14: {
                if (GuiScreen.isCtrlKeyDown()) {
                    this.func_73779_a(-1);
                } else {
                    this.deleteFromCursor(-1);
                }
                return true;
            }
            case 199: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.func_73800_i(0);
                } else {
                    this.setCursorPositionZero();
                }
                return true;
            }
            case 203: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.func_73800_i(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                    } else {
                        this.func_73800_i(this.getSelectionEnd() - 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(-1));
                } else {
                    this.cursorPos(-1);
                }
                return true;
            }
            case 205: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.func_73800_i(this.getNthWordFromPos(1, this.getSelectionEnd()));
                    } else {
                        this.func_73800_i(this.getSelectionEnd() + 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(1));
                } else {
                    this.cursorPos(1);
                }
                return true;
            }
            case 207: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.func_73800_i(this.text.length());
                } else {
                    this.setCursorPositionEnd();
                }
                return true;
            }
            case 211: {
                if (GuiScreen.isCtrlKeyDown()) {
                    this.func_73779_a(1);
                } else {
                    this.deleteFromCursor(1);
                }
                return true;
            }
        }
        if (ChatAllowedCharacters.isAllowedCharacter(c)) {
            this.writeText(Character.toString(c));
            return true;
        }
        return false;
    }

    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    private void drawCursorVertical(int n, int n2, int n3, int n4) {
        int n5;
        if (n < n3) {
            n5 = n;
            n = n3;
            n3 = n5;
        }
        if (n2 < n4) {
            n5 = n2;
            n2 = n4;
            n4 = n5;
        }
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)255.0f, (float)255.0f);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3058);
        GL11.glLogicOp((int)5387);
        worldRenderer.begin(7, worldRenderer.getVertexFormat());
        worldRenderer.pos(n, n4, 0.0);
        worldRenderer.pos(n3, n4, 0.0);
        worldRenderer.pos(n3, n2, 0.0);
        worldRenderer.pos(n, n2, 0.0);
        worldRenderer.finishDrawing();
        GL11.glDisable((int)3058);
        GL11.glEnable((int)3553);
    }

    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    public void func_73800_i(int n) {
        int n2 = this.text.length();
        if (n > n2) {
            n = n2;
        }
        if (n < 0) {
            n = 0;
        }
        this.selectionEnd = n;
        if (this.fontRenderer != null) {
            if (this.i > n2) {
                this.i = n2;
            }
            int n3 = this.getWidth();
            String string = this.fontRenderer.trimStringToWidth(this.text.substring(this.i), n3);
            int n4 = string.length() + this.i;
            if (n == this.i) {
                this.i -= this.fontRenderer.trimStringToWidth(this.text, n3, true).length();
            }
            if (n > n4) {
                this.i += n - n4;
            } else if (n <= this.i) {
                this.i -= this.i - n;
            }
            if (this.i < 0) {
                this.i = 0;
            }
            if (this.i > n2) {
                this.i = n2;
            }
        }
    }

    public void deleteFromCursor(int n) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean bl = n < 0;
                int n2 = bl ? this.cursorPosition + n : this.cursorPosition;
                int n3 = bl ? this.cursorPosition : this.cursorPosition + n;
                String string = "";
                if (n2 >= 0) {
                    string = this.text.substring(0, n2);
                }
                if (n3 < this.text.length()) {
                    string = String.valueOf(String.valueOf(String.valueOf(string))) + this.text.substring(n3);
                }
                this.text = string;
                if (bl) {
                    this.cursorPos(n);
                }
            }
        }
    }

    public void setMaxStringLength(int n) {
        this.maxStringLength = n;
        if (this.text.length() > n) {
            this.text = this.text.substring(0, n);
        }
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public String getSelectedtext() {
        int n = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int n2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(n, n2);
    }

    public void mouseClicked(int n, int n2, int n3) {
        boolean bl;
        boolean bl2 = bl = n >= this.xPos && n < this.xPos + this.width && n2 >= this.yPos && n2 < this.yPos + this.height;
        if (this.canLoseFocus) {
            this.setFocused(this.isEnabled && bl);
        }
        if (this.isFocused && n3 == 0) {
            int n4 = n - this.xPos;
            if (this.enableBackgroundDrawing) {
                n4 -= 4;
            }
            String string = this.fontRenderer.trimStringToWidth(this.text.substring(this.i), this.getWidth());
            this.setCursorPosition(this.fontRenderer.trimStringToWidth(string, n4).length() + this.i);
        }
    }

    public boolean func_73778_q() {
        return this.b;
    }

    public void setFocused(boolean bl) {
        if (bl && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = bl;
    }

    public int getNthWordFromCursor(int n) {
        return this.getNthWordFromPos(n, this.getCursorPosition());
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public void setEnableBackgroundDrawing(boolean bl) {
        this.enableBackgroundDrawing = bl;
    }

    public void updateCursorCounter() {
        ++this.cursorCounter;
    }
}

