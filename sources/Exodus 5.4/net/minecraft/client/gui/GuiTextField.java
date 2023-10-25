/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

public class GuiTextField
extends Gui {
    private int selectionEnd;
    private boolean visible = true;
    private boolean enableBackgroundDrawing = true;
    private int maxStringLength = 32;
    private int cursorCounter;
    private boolean isEnabled = true;
    private final int height;
    private final int id;
    public int yPosition;
    private boolean canLoseFocus = true;
    private boolean isFocused;
    private int enabledColor = 0xE0E0E0;
    private Predicate<String> field_175209_y = Predicates.alwaysTrue();
    private String text = "";
    private int disabledColor = 0x707070;
    private final FontRenderer fontRendererInstance;
    private int lineScrollOffset;
    private int cursorPosition;
    private final int width;
    private GuiPageButtonList.GuiResponder field_175210_x;
    public int xPosition;

    public int getNthWordFromCursor(int n) {
        return this.getNthWordFromPos(n, this.getCursorPosition());
    }

    public void setCursorPosition(int n) {
        this.cursorPosition = n;
        int n2 = this.text.length();
        this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, n2);
        this.setSelectionPos(this.cursorPosition);
    }

    public String getText() {
        return this.text;
    }

    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    public void setTextColor(int n) {
        this.enabledColor = n;
    }

    public void moveCursorBy(int n) {
        this.setCursorPosition(this.selectionEnd + n);
    }

    public void setFocused(boolean bl) {
        if (bl && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = bl;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void deleteWords(int n) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(n) - this.cursorPosition);
            }
        }
    }

    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    public void setSelectionPos(int n) {
        int n2 = this.text.length();
        if (n > n2) {
            n = n2;
        }
        if (n < 0) {
            n = 0;
        }
        this.selectionEnd = n;
        if (this.fontRendererInstance != null) {
            if (this.lineScrollOffset > n2) {
                this.lineScrollOffset = n2;
            }
            int n3 = this.getWidth();
            String string = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), n3);
            int n4 = string.length() + this.lineScrollOffset;
            if (n == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, n3, true).length();
            }
            if (n > n4) {
                this.lineScrollOffset += n - n4;
            } else if (n <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - n;
            }
            this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, n2);
        }
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                GuiTextField.drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
                GuiTextField.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
            }
            int n = this.isEnabled ? this.enabledColor : this.disabledColor;
            int n2 = this.cursorPosition - this.lineScrollOffset;
            int n3 = this.selectionEnd - this.lineScrollOffset;
            String string = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            boolean bl = n2 >= 0 && n2 <= string.length();
            boolean bl2 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && bl;
            int n4 = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
            int n5 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
            int n6 = n4;
            if (n3 > string.length()) {
                n3 = string.length();
            }
            if (string.length() > 0) {
                String string2 = bl ? string.substring(0, n2) : string;
                n6 = this.fontRendererInstance.drawStringWithShadow(string2, n4, n5, n);
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
                n6 = this.fontRendererInstance.drawStringWithShadow(string.substring(n2), n6, n5, n);
            }
            if (bl2) {
                if (bl3) {
                    Gui.drawRect(n7, n5 - 1, n7 + 1, n5 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
                } else {
                    this.fontRendererInstance.drawStringWithShadow("_", n7, n5, n);
                }
            }
            if (n3 != n2) {
                int n8 = n4 + this.fontRendererInstance.getStringWidth(string.substring(0, n3));
                this.drawCursorVertical(n7, n5 - 1, n8 - 1, n5 + 1 + this.fontRendererInstance.FONT_HEIGHT);
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    public int func_146197_a(int var1_1, int var2_2, boolean var3_3) {
        var4_4 = var2_2;
        var5_5 = var1_1 < 0;
        var6_6 = Math.abs(var1_1);
        var7_7 = 0;
        while (var7_7 < var6_6) {
            block4: {
                if (var5_5) ** GOTO lbl15
                var8_8 = this.text.length();
                if ((var4_4 = this.text.indexOf(32, var4_4)) != -1) ** GOTO lbl12
                var4_4 = var8_8;
                break block4;
lbl-1000:
                // 1 sources

                {
                    ++var4_4;
lbl12:
                    // 2 sources

                    ** while (var3_3 && var4_4 < var8_8 && this.text.charAt((int)var4_4) == ' ')
                }
lbl13:
                // 1 sources

                break block4;
lbl-1000:
                // 1 sources

                {
                    --var4_4;
lbl15:
                    // 2 sources

                    ** while (var3_3 && var4_4 > 0 && this.text.charAt((int)(var4_4 - 1)) == ' ')
                }
lbl16:
                // 2 sources

                while (var4_4 > 0 && this.text.charAt(var4_4 - 1) != ' ') {
                    --var4_4;
                }
            }
            ++var7_7;
        }
        return var4_4;
    }

    public void func_175207_a(GuiPageButtonList.GuiResponder guiResponder) {
        this.field_175210_x = guiResponder;
    }

    public void setDisabledTextColour(int n) {
        this.disabledColor = n;
    }

    public int getNthWordFromPos(int n, int n2) {
        return this.func_146197_a(n, n2, true);
    }

    public int getId() {
        return this.id;
    }

    public void func_175205_a(Predicate<String> predicate) {
        this.field_175209_y = predicate;
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    public void setCursorPositionZero() {
        this.setCursorPosition(0);
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
        if (n3 > this.xPosition + this.width) {
            n3 = this.xPosition + this.width;
        }
        if (n > this.xPosition + this.width) {
            n = this.xPosition + this.width;
        }
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(n, n4, 0.0).endVertex();
        worldRenderer.pos(n3, n4, 0.0).endVertex();
        worldRenderer.pos(n3, n2, 0.0).endVertex();
        worldRenderer.pos(n, n2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public void mouseClicked(int n, int n2, int n3) {
        boolean bl;
        boolean bl2 = bl = n >= this.xPosition && n < this.xPosition + this.width && n2 >= this.yPosition && n2 < this.yPosition + this.height;
        if (this.canLoseFocus) {
            this.setFocused(bl);
        }
        if (this.isFocused && bl && n3 == 0) {
            int n4 = n - this.xPosition;
            if (this.enableBackgroundDrawing) {
                n4 -= 4;
            }
            String string = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(string, n4).length() + this.lineScrollOffset);
        }
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public void setCanLoseFocus(boolean bl) {
        this.canLoseFocus = bl;
    }

    public void setText(String string) {
        if (this.field_175209_y.apply((Object)string)) {
            this.text = string.length() > this.maxStringLength ? string.substring(0, this.maxStringLength) : string;
            this.setCursorPositionEnd();
        }
    }

    public GuiTextField(int n, FontRenderer fontRenderer, int n2, int n3, int n4, int n5) {
        this.id = n;
        this.fontRendererInstance = fontRenderer;
        this.xPosition = n2;
        this.yPosition = n3;
        this.width = n4;
        this.height = n5;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public void setEnableBackgroundDrawing(boolean bl) {
        this.enableBackgroundDrawing = bl;
    }

    public void setMaxStringLength(int n) {
        this.maxStringLength = n;
        if (this.text.length() > n) {
            this.text = this.text.substring(0, n);
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
                    string = String.valueOf(string) + this.text.substring(n3);
                }
                if (this.field_175209_y.apply((Object)string)) {
                    this.text = string;
                    if (bl) {
                        this.moveCursorBy(n);
                    }
                    if (this.field_175210_x != null) {
                        this.field_175210_x.func_175319_a(this.id, this.text);
                    }
                }
            }
        }
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public void writeText(String string) {
        String string2 = "";
        String string3 = ChatAllowedCharacters.filterAllowedCharacters(string);
        int n = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int n2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int n3 = this.maxStringLength - this.text.length() - (n - n2);
        int n4 = 0;
        if (this.text.length() > 0) {
            string2 = String.valueOf(string2) + this.text.substring(0, n);
        }
        if (n3 < string3.length()) {
            string2 = String.valueOf(string2) + string3.substring(0, n3);
            n4 = n3;
        } else {
            string2 = String.valueOf(string2) + string3;
            n4 = string3.length();
        }
        if (this.text.length() > 0 && n2 < this.text.length()) {
            string2 = String.valueOf(string2) + this.text.substring(n2);
        }
        if (this.field_175209_y.apply((Object)string2)) {
            this.text = string2;
            this.moveCursorBy(n - this.selectionEnd + n4);
            if (this.field_175210_x != null) {
                this.field_175210_x.func_175319_a(this.id, this.text);
            }
        }
    }

    public boolean textboxKeyTyped(char c, int n) {
        if (!this.isFocused) {
            return false;
        }
        if (GuiScreen.isKeyComboCtrlA(n)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        }
        if (GuiScreen.isKeyComboCtrlC(n)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        }
        if (GuiScreen.isKeyComboCtrlV(n)) {
            if (this.isEnabled) {
                this.writeText(GuiScreen.getClipboardString());
            }
            return true;
        }
        if (GuiScreen.isKeyComboCtrlX(n)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            if (this.isEnabled) {
                this.writeText("");
            }
            return true;
        }
        switch (n) {
            case 14: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled) {
                        this.deleteWords(-1);
                    }
                } else if (this.isEnabled) {
                    this.deleteFromCursor(-1);
                }
                return true;
            }
            case 199: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.setSelectionPos(0);
                } else {
                    this.setCursorPositionZero();
                }
                return true;
            }
            case 203: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                    } else {
                        this.setSelectionPos(this.getSelectionEnd() - 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(-1));
                } else {
                    this.moveCursorBy(-1);
                }
                return true;
            }
            case 205: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                    } else {
                        this.setSelectionPos(this.getSelectionEnd() + 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(1));
                } else {
                    this.moveCursorBy(1);
                }
                return true;
            }
            case 207: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.setSelectionPos(this.text.length());
                } else {
                    this.setCursorPositionEnd();
                }
                return true;
            }
            case 211: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled) {
                        this.deleteWords(1);
                    }
                } else if (this.isEnabled) {
                    this.deleteFromCursor(1);
                }
                return true;
            }
        }
        if (ChatAllowedCharacters.isAllowedCharacter(c)) {
            if (this.isEnabled) {
                this.writeText(Character.toString(c));
            }
            return true;
        }
        return false;
    }

    public String getSelectedText() {
        int n = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int n2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(n, n2);
    }

    public void setEnabled(boolean bl) {
        this.isEnabled = bl;
    }
}

