/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiPageButtonList$GuiResponder
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.util.ChatAllowedCharacters
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.objects;

import com.google.common.base.Predicate;
import liying.fonts.api.FontRenderer;
import liying.utils.LiYingUtil;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;

public class PasswordField
extends Gui {
    private String text = "";
    private int enabledColor = 0xE0E0E0;
    private int lineScrollOffset;
    public int textColor = -1;
    public int bottomBarColor = -1;
    private boolean canLoseFocus = true;
    private boolean enableBackgroundDrawing = true;
    public int yPosition;
    private final int id;
    private int maxStringLength = 32;
    private boolean visible = true;
    private GuiPageButtonList.GuiResponder field_175210_x;
    public String placeholder;
    public int cursorColor = -1;
    private int selectionEnd;
    public int xPosition;
    private boolean isEnabled = true;
    private boolean isFocused;
    public int width;
    public double placeHolderTextX;
    private int cursorPosition;
    private Predicate field_175209_y = PasswordField::lambda$new$0;
    private final int height;
    private int cursorCounter;
    private final FontRenderer fontRenderer;
    private int disabledColor = 0x707070;

    public int getSelectionEnd() {
        return this.selectionEnd;
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
            String string = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRenderer.trimStringToWidth(string, n4).length() + this.lineScrollOffset);
        }
    }

    public void setMaxStringLength(int n) {
        this.maxStringLength = n;
        if (this.text.length() > n) {
            this.text = this.text.substring(0, n);
        }
    }

    public void setEnableBackgroundDrawing(boolean bl) {
        this.enableBackgroundDrawing = bl;
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
        if (this.fontRenderer != null) {
            if (this.lineScrollOffset > n2) {
                this.lineScrollOffset = n2;
            }
            int n3 = this.getWidth();
            String string = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), n3);
            int n4 = string.length() + this.lineScrollOffset;
            if (n == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text, n3, true).length();
            }
            if (n > n4) {
                this.lineScrollOffset += n - n4;
            } else if (n <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - n;
            }
            this.lineScrollOffset = LiYingUtil.clamp_int(this.lineScrollOffset, 0, n2);
        }
    }

    public String getText() {
        return this.text;
    }

    public int getId() {
        return this.id;
    }

    public void setDisabledTextColour(int n) {
        this.disabledColor = n;
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
        Tessellator tessellator = Tessellator.func_178181_a();
        IWorldRenderer iWorldRenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        GlStateManager.func_179131_c((float)0.0f, (float)0.0f, (float)255.0f, (float)255.0f);
        GlStateManager.func_179090_x();
        GlStateManager.func_179115_u();
        GlStateManager.func_179116_f((int)5387);
        iWorldRenderer.begin(7, MinecraftInstance.classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_COLOR));
        iWorldRenderer.pos(n, n4, 0.0).endVertex();
        iWorldRenderer.pos(n3, n4, 0.0).endVertex();
        iWorldRenderer.pos(n3, n2, 0.0).endVertex();
        iWorldRenderer.pos(n, n2, 0.0).endVertex();
        tessellator.func_78381_a();
        GlStateManager.func_179134_v();
        GlStateManager.func_179098_w();
    }

    public void func_175205_a(Predicate predicate) {
        this.field_175209_y = predicate;
    }

    public boolean textboxKeyTyped(char c, int n) {
        if (!this.isFocused) {
            return false;
        }
        if (GuiScreen.func_175278_g((int)n)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        }
        if (GuiScreen.func_175280_f((int)n)) {
            GuiScreen.func_146275_d((String)this.getSelectedText());
            return true;
        }
        if (GuiScreen.func_175279_e((int)n)) {
            if (this.isEnabled) {
                this.writeText(GuiScreen.func_146277_j());
            }
            return true;
        }
        if (GuiScreen.func_175277_d((int)n)) {
            GuiScreen.func_146275_d((String)this.getSelectedText());
            if (this.isEnabled) {
                this.writeText("");
            }
            return true;
        }
        switch (n) {
            case 14: {
                if (GuiScreen.func_146271_m()) {
                    if (this.isEnabled) {
                        this.deleteWords(-1);
                    }
                } else if (this.isEnabled) {
                    this.deleteFromCursor(-1);
                }
                return true;
            }
            case 199: {
                if (GuiScreen.func_146272_n()) {
                    this.setSelectionPos(0);
                } else {
                    this.setCursorPositionZero();
                }
                return true;
            }
            case 203: {
                if (GuiScreen.func_146272_n()) {
                    if (GuiScreen.func_146271_m()) {
                        this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                    } else {
                        this.setSelectionPos(this.getSelectionEnd() - 1);
                    }
                } else if (GuiScreen.func_146271_m()) {
                    this.setCursorPosition(this.getNthWordFromCursor(-1));
                } else {
                    this.moveCursorBy(-1);
                }
                return true;
            }
            case 205: {
                if (GuiScreen.func_146272_n()) {
                    if (GuiScreen.func_146271_m()) {
                        this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                    } else {
                        this.setSelectionPos(this.getSelectionEnd() + 1);
                    }
                } else if (GuiScreen.func_146271_m()) {
                    this.setCursorPosition(this.getNthWordFromCursor(1));
                } else {
                    this.moveCursorBy(1);
                }
                return true;
            }
            case 207: {
                if (GuiScreen.func_146272_n()) {
                    this.setSelectionPos(this.text.length());
                } else {
                    this.setCursorPositionEnd();
                }
                return true;
            }
            case 211: {
                if (GuiScreen.func_146271_m()) {
                    if (this.isEnabled) {
                        this.deleteWords(1);
                    }
                } else if (this.isEnabled) {
                    this.deleteFromCursor(1);
                }
                return true;
            }
        }
        if (ChatAllowedCharacters.func_71566_a((char)c)) {
            if (this.isEnabled) {
                this.writeText(Character.toString(c));
            }
            return true;
        }
        return false;
    }

    public void setCanLoseFocus(boolean bl) {
        this.canLoseFocus = bl;
    }

    public int func_146197_a(int n, int n2, boolean bl) {
        int n3 = n2;
        boolean bl2 = n < 0;
        int n4 = Math.abs(n);
        for (int i = 0; i < n4; ++i) {
            if (!bl2) {
                int n5 = this.text.length();
                if ((n3 = this.text.indexOf(32, n3)) == -1) {
                    n3 = n5;
                    continue;
                }
                while (bl && n3 < n5 && this.text.charAt(n3) == ' ') {
                    ++n3;
                }
                continue;
            }
            while (bl && n3 > 0 && this.text.charAt(n3 - 1) == ' ') {
                --n3;
            }
            while (n3 > 0 && this.text.charAt(n3 - 1) != ' ') {
                --n3;
            }
        }
        return n3;
    }

    public void drawTextBox(String string, boolean bl) {
        if (bl) {
            string = string.replaceAll(".", "*");
        }
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                PasswordField.func_73734_a((int)this.xPosition, (int)(this.yPosition + this.height), (int)(this.xPosition + this.width), (int)(this.yPosition + this.height + 1), (int)this.bottomBarColor);
            }
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            int n = this.textColor;
            int n2 = this.cursorPosition - this.lineScrollOffset;
            int n3 = this.selectionEnd - this.lineScrollOffset;
            String string2 = this.fontRenderer.trimStringToWidth(string.substring(this.lineScrollOffset), this.getWidth());
            boolean bl2 = n2 >= 0 && n2 <= string2.length();
            boolean bl3 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && bl2;
            int n4 = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
            int n5 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 4 : this.yPosition;
            int n6 = n4;
            if (!this.isFocused && this.placeholder != null && string.isEmpty()) {
                this.fontRenderer.drawCenteredString(this.placeholder, (float)this.placeHolderTextX, n5, this.textColor);
            }
            if (n3 > string2.length()) {
                n3 = string2.length();
            }
            if (string2.length() > 0) {
                String string3 = bl2 ? string2.substring(0, n2) : string2;
                n6 = (int)this.fontRenderer.drawString((CharSequence)string3, (float)n4, (float)n5, n);
            }
            boolean bl4 = this.cursorPosition < string.length() || string.length() >= this.getMaxStringLength();
            int n7 = n6;
            if (!bl2) {
                n7 = n2 > 0 ? n4 + this.width : n4;
            } else if (bl4) {
                n7 = n6 - 1;
                --n6;
            }
            if (string2.length() > 0 && bl2 && n2 < string2.length()) {
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                n6 = (int)this.fontRenderer.drawString((CharSequence)string2.substring(n2), (float)n6 + 6.0f, (float)n5, n);
            }
            if (bl3) {
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                if (bl4) {
                    Gui.func_73734_a((int)(n7 + 4), (int)(n5 - 1), (int)(n7 + 5), (int)(n5 + 1 + this.fontRenderer.getHeight()), (int)this.cursorColor);
                } else {
                    this.fontRenderer.drawString((CharSequence)"|", (float)n7 + 4.0f, (float)n5, this.textColor);
                }
            }
            if (n3 != n2) {
                int n8 = n4 + this.fontRenderer.stringWidth(string2.substring(0, n3));
                this.drawCursorVertical(n7, n5 - 1, n8 - 1, n5 + 1 + this.fontRenderer.getHeight());
            }
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    public void setCursorPosition(int n) {
        this.cursorPosition = n;
        int n2 = this.text.length();
        this.cursorPosition = LiYingUtil.clamp_int(this.cursorPosition, 0, n2);
        this.setSelectionPos(this.cursorPosition);
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
                    string = string + this.text.substring(n3);
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

    public PasswordField(String string, int n, int n2, int n3, int n4, int n5, FontRenderer fontRenderer) {
        this.placeholder = string;
        this.id = n;
        this.xPosition = n2;
        this.yPosition = n3;
        this.width = n4;
        this.height = n5;
        this.fontRenderer = fontRenderer;
        this.placeHolderTextX = (float)(this.xPosition + this.width) / 2.0f;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public void setEnabled(boolean bl) {
        this.isEnabled = bl;
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public PasswordField(String string, int n, int n2, int n3, int n4, int n5, FontRenderer fontRenderer, int n6) {
        this.placeholder = string;
        this.id = n;
        this.xPosition = n2;
        this.yPosition = n3;
        this.width = n4;
        this.height = n5;
        this.fontRenderer = fontRenderer;
        this.textColor = n6;
        this.placeHolderTextX = (float)(this.xPosition + this.width) / 2.0f;
    }

    public void setText(String string) {
        if (this.field_175209_y.apply((Object)string)) {
            this.text = string.length() > this.maxStringLength ? string.substring(0, this.maxStringLength) : string;
            this.setCursorPositionEnd();
        }
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

    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    public int getNthWordFromCursor(int n) {
        return this.getNthWordFromPos(n, this.getCursorPosition());
    }

    public void moveCursorBy(int n) {
        this.setCursorPosition(this.selectionEnd + n);
    }

    public void func_175207_a(GuiPageButtonList.GuiResponder guiResponder) {
        this.field_175210_x = guiResponder;
    }

    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    public void writeText(String string) {
        int n;
        String string2 = "";
        String string3 = ChatAllowedCharacters.func_71565_a((String)string);
        int n2 = Math.min(this.cursorPosition, this.selectionEnd);
        int n3 = Math.max(this.cursorPosition, this.selectionEnd);
        int n4 = this.maxStringLength - this.text.length() - (n2 - n3);
        if (this.text.length() > 0) {
            string2 = string2 + this.text.substring(0, n2);
        }
        if (n4 < string3.length()) {
            string2 = string2 + string3.substring(0, n4);
            n = n4;
        } else {
            string2 = string2 + string3;
            n = string3.length();
        }
        if (this.text.length() > 0 && n3 < this.text.length()) {
            string2 = string2 + this.text.substring(n3);
        }
        if (this.field_175209_y.apply((Object)string2)) {
            this.text = string2;
            this.moveCursorBy(n2 - this.selectionEnd + n);
            if (this.field_175210_x != null) {
                this.field_175210_x.func_175319_a(this.id, this.text);
            }
        }
    }

    public String getSelectedText() {
        int n = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int n2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(n, n2);
    }

    public int getNthWordFromPos(int n, int n2) {
        return this.func_146197_a(n, n2, true);
    }

    public void setFocused(boolean bl) {
        if (bl && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = bl;
    }

    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    public void setTextColor(int n) {
        this.enabledColor = n;
    }

    public void drawTextBox() {
        this.drawTextBox(this.text, false);
    }

    public void drawPasswordBox() {
        this.drawTextBox(this.text, true);
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    private static boolean lambda$new$0(String string) {
        return true;
    }
}

