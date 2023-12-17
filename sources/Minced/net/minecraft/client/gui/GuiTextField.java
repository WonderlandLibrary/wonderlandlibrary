// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ChatAllowedCharacters;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;

public class GuiTextField extends Gui
{
    private final int id;
    private final FontRenderer fontRenderer;
    public int x;
    public int y;
    private final int width;
    private final int height;
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    private boolean enableBackgroundDrawing;
    private boolean canLoseFocus;
    private boolean isFocused;
    private boolean isEnabled;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor;
    private int disabledColor;
    private boolean visible;
    private GuiPageButtonList.GuiResponder guiResponder;
    private Predicate<String> validator;
    
    public GuiTextField(final int componentId, final FontRenderer fontrendererObj, final int x, final int y, final int par5Width, final int par6Height) {
        this.text = "";
        this.maxStringLength = 32;
        this.enableBackgroundDrawing = true;
        this.canLoseFocus = true;
        this.isEnabled = true;
        this.enabledColor = 14737632;
        this.disabledColor = 7368816;
        this.visible = true;
        this.validator = (Predicate<String>)Predicates.alwaysTrue();
        this.id = componentId;
        this.fontRenderer = fontrendererObj;
        this.x = x;
        this.y = y;
        this.width = par5Width;
        this.height = par6Height;
    }
    
    public void setGuiResponder(final GuiPageButtonList.GuiResponder guiResponderIn) {
        this.guiResponder = guiResponderIn;
    }
    
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }
    
    public void setText(final String textIn) {
        if (this.validator.apply((Object)textIn)) {
            if (textIn.length() > this.maxStringLength) {
                this.text = textIn.substring(0, this.maxStringLength);
            }
            else {
                this.text = textIn;
            }
            this.setCursorPositionEnd();
        }
    }
    
    public String getText() {
        return this.text;
    }
    
    public String getSelectedText() {
        final int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(i, j);
    }
    
    public void setValidator(final Predicate<String> theValidator) {
        this.validator = theValidator;
    }
    
    public void writeText(final String textToWrite) {
        String s = "";
        final String s2 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
        final int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        final int k = this.maxStringLength - this.text.length() - (i - j);
        if (!this.text.isEmpty()) {
            s += this.text.substring(0, i);
        }
        int l;
        if (k < s2.length()) {
            s += s2.substring(0, k);
            l = k;
        }
        else {
            s += s2;
            l = s2.length();
        }
        if (!this.text.isEmpty() && j < this.text.length()) {
            s += this.text.substring(j);
        }
        if (this.validator.apply((Object)s)) {
            this.text = s;
            this.moveCursorBy(i - this.selectionEnd + l);
            this.setResponderEntryValue(this.id, this.text);
        }
    }
    
    public void setResponderEntryValue(final int idIn, final String textIn) {
        if (this.guiResponder != null) {
            this.guiResponder.setEntryValue(idIn, textIn);
        }
    }
    
    public void deleteWords(final int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
            }
        }
    }
    
    public void deleteFromCursor(final int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                final boolean flag = num < 0;
                final int i = flag ? (this.cursorPosition + num) : this.cursorPosition;
                final int j = flag ? this.cursorPosition : (this.cursorPosition + num);
                String s = "";
                if (i >= 0) {
                    s = this.text.substring(0, i);
                }
                if (j < this.text.length()) {
                    s += this.text.substring(j);
                }
                if (this.validator.apply((Object)s)) {
                    this.text = s;
                    if (flag) {
                        this.moveCursorBy(num);
                    }
                    this.setResponderEntryValue(this.id, this.text);
                }
            }
        }
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getNthWordFromCursor(final int numWords) {
        return this.getNthWordFromPos(numWords, this.getCursorPosition());
    }
    
    public int getNthWordFromPos(final int n, final int pos) {
        return this.getNthWordFromPosWS(n, pos, true);
    }
    
    public int getNthWordFromPosWS(final int n, final int pos, final boolean skipWs) {
        int i = pos;
        final boolean flag = n < 0;
        for (int j = Math.abs(n), k = 0; k < j; ++k) {
            if (!flag) {
                final int l = this.text.length();
                i = this.text.indexOf(32, i);
                if (i == -1) {
                    i = l;
                }
                else {
                    while (skipWs && i < l && this.text.charAt(i) == ' ') {
                        ++i;
                    }
                }
            }
            else {
                while (skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }
                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }
        return i;
    }
    
    public void moveCursorBy(final int num) {
        this.setCursorPosition(this.selectionEnd + num);
    }
    
    public void setCursorPosition(final int pos) {
        this.cursorPosition = pos;
        final int i = this.text.length();
        this.setSelectionPos(this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i));
    }
    
    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }
    
    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }
    
    public boolean textboxKeyTyped(final char typedChar, final int keyCode) {
        if (!this.isFocused) {
            return false;
        }
        if (GuiScreen.isKeyComboCtrlA(keyCode)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        }
        if (GuiScreen.isKeyComboCtrlC(keyCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        }
        if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            if (this.isEnabled) {
                this.writeText(GuiScreen.getClipboardString());
            }
            return true;
        }
        if (GuiScreen.isKeyComboCtrlX(keyCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            if (this.isEnabled) {
                this.writeText("");
            }
            return true;
        }
        switch (keyCode) {
            case 14: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled) {
                        this.deleteWords(-1);
                    }
                }
                else if (this.isEnabled) {
                    this.deleteFromCursor(-1);
                }
                return true;
            }
            case 199: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.setSelectionPos(0);
                }
                else {
                    this.setCursorPositionZero();
                }
                return true;
            }
            case 203: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                    }
                    else {
                        this.setSelectionPos(this.getSelectionEnd() - 1);
                    }
                }
                else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(-1));
                }
                else {
                    this.moveCursorBy(-1);
                }
                return true;
            }
            case 205: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                    }
                    else {
                        this.setSelectionPos(this.getSelectionEnd() + 1);
                    }
                }
                else if (GuiScreen.isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(1));
                }
                else {
                    this.moveCursorBy(1);
                }
                return true;
            }
            case 207: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.setSelectionPos(this.text.length());
                }
                else {
                    this.setCursorPositionEnd();
                }
                return true;
            }
            case 211: {
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled) {
                        this.deleteWords(1);
                    }
                }
                else if (this.isEnabled) {
                    this.deleteFromCursor(1);
                }
                return true;
            }
            default: {
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    if (this.isEnabled) {
                        this.writeText(Character.toString(typedChar));
                    }
                    return true;
                }
                return false;
            }
        }
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final boolean flag = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
        if (this.canLoseFocus) {
            this.setFocused(flag);
        }
        if (this.isFocused && flag && mouseButton == 0) {
            int i = mouseX - this.x;
            if (this.enableBackgroundDrawing) {
                i -= 4;
            }
            final String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRenderer.trimStringToWidth(s, i).length() + this.lineScrollOffset);
            return true;
        }
        return false;
    }
    
    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                Gui.drawRect((float)(this.x - 1), (float)(this.y - 1), (float)(this.x + this.width + 1), (float)(this.y + this.height + 1), -6250336);
                Gui.drawRect((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height), -16777216);
            }
            final int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            final int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            final String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            final boolean flag = j >= 0 && j <= s.length();
            final boolean flag2 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            final int l = this.enableBackgroundDrawing ? (this.x + 4) : this.x;
            final int i2 = this.enableBackgroundDrawing ? (this.y + (this.height - 8) / 2) : this.y;
            int j2 = l;
            if (k > s.length()) {
                k = s.length();
            }
            if (!s.isEmpty()) {
                final String s2 = flag ? s.substring(0, j) : s;
                j2 = this.fontRenderer.drawStringWithShadow(s2, (float)l, (float)i2, i);
            }
            final boolean flag3 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int k2 = j2;
            if (!flag) {
                k2 = ((j > 0) ? (l + this.width) : l);
            }
            else if (flag3) {
                k2 = j2 - 1;
                --j2;
            }
            if (!s.isEmpty() && flag && j < s.length()) {
                j2 = this.fontRenderer.drawStringWithShadow(s.substring(j), (float)j2, (float)i2, i);
            }
            if (flag2) {
                if (flag3) {
                    Gui.drawRect((float)k2, (float)(i2 - 1), (float)(k2 + 1), (float)(i2 + 1 + this.fontRenderer.FONT_HEIGHT), -3092272);
                }
                else {
                    this.fontRenderer.drawStringWithShadow("_", (float)k2, (float)i2, i);
                }
            }
            if (k != j) {
                final int l2 = l + this.fontRenderer.getStringWidth(s.substring(0, k));
                this.drawSelectionBox(k2, i2 - 1, l2 - 1, i2 + 1 + this.fontRenderer.FONT_HEIGHT);
            }
        }
    }
    
    public void drawTextBox2() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                RenderUtility.drawRound((float)this.x, (float)this.y, (float)this.width, (float)this.height, 2.0f, new Color(22, 22, 22));
            }
            final int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            final int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            final String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            final boolean flag = j >= 0 && j <= s.length();
            final boolean flag2 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            final int l = this.enableBackgroundDrawing ? (this.x + 4) : this.x;
            final int i2 = this.enableBackgroundDrawing ? (this.y + (this.height - 8) / 2) : this.y;
            int j2 = l;
            if (k > s.length()) {
                k = s.length();
            }
            if (!s.isEmpty()) {
                final String s2 = flag ? s.substring(0, j) : s;
                j2 = this.fontRenderer.drawStringWithShadow(s2, (float)l, (float)i2, i);
            }
            final boolean flag3 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int k2 = j2;
            if (!flag) {
                k2 = ((j > 0) ? (l + this.width) : l);
            }
            else if (flag3) {
                k2 = j2 - 1;
                --j2;
            }
            if (!s.isEmpty() && flag && j < s.length()) {
                j2 = this.fontRenderer.drawStringWithShadow(s.substring(j), (float)j2, (float)i2, i);
            }
            if (flag2) {
                if (flag3) {
                    Gui.drawRect((float)k2, (float)(i2 - 1), (float)(k2 + 1), (float)(i2 + 1 + this.fontRenderer.FONT_HEIGHT), -3092272);
                }
                else {
                    this.fontRenderer.drawStringWithShadow("_", (float)k2, (float)i2, i);
                }
            }
            if (k != j) {
                final int l2 = l + this.fontRenderer.getStringWidth(s.substring(0, k));
                this.drawSelectionBox(k2, i2 - 1, l2 - 1, i2 + 1 + this.fontRenderer.FONT_HEIGHT);
            }
        }
    }
    
    private void drawSelectionBox(int startX, int startY, int endX, int endY) {
        if (startX < endX) {
            final int i = startX;
            startX = endX;
            endX = i;
        }
        if (startY < endY) {
            final int j = startY;
            startY = endY;
            endY = j;
        }
        if (endX > this.x + this.width) {
            endX = this.x + this.width;
        }
        if (startX > this.x + this.width) {
            startX = this.x + this.width;
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(startX, endY, 0.0).endVertex();
        bufferbuilder.pos(endX, endY, 0.0).endVertex();
        bufferbuilder.pos(endX, startY, 0.0).endVertex();
        bufferbuilder.pos(startX, startY, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }
    
    public void setMaxStringLength(final int length) {
        this.maxStringLength = length;
        if (this.text.length() > length) {
            this.text = this.text.substring(0, length);
        }
    }
    
    public int getMaxStringLength() {
        return this.maxStringLength;
    }
    
    public int getCursorPosition() {
        return this.cursorPosition;
    }
    
    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }
    
    public void setEnableBackgroundDrawing(final boolean enableBackgroundDrawingIn) {
        this.enableBackgroundDrawing = enableBackgroundDrawingIn;
    }
    
    public void setTextColor(final int color) {
        this.enabledColor = color;
    }
    
    public void setDisabledTextColour(final int color) {
        this.disabledColor = color;
    }
    
    public void setFocused(final boolean isFocusedIn) {
        if (isFocusedIn && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = isFocusedIn;
        if (Minecraft.getMinecraft().currentScreen != null) {
            Minecraft.getMinecraft().currentScreen.setFocused(isFocusedIn);
        }
    }
    
    public boolean isFocused() {
        return this.isFocused;
    }
    
    public void setEnabled(final boolean enabled) {
        this.isEnabled = enabled;
    }
    
    public int getSelectionEnd() {
        return this.selectionEnd;
    }
    
    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
    }
    
    public void setSelectionPos(int position) {
        final int i = this.text.length();
        if (position > i) {
            position = i;
        }
        if (position < 0) {
            position = 0;
        }
        this.selectionEnd = position;
        if (this.fontRenderer != null) {
            if (this.lineScrollOffset > i) {
                this.lineScrollOffset = i;
            }
            final int j = this.getWidth();
            final String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
            final int k = s.length() + this.lineScrollOffset;
            if (position == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text, j, true).length();
            }
            if (position > k) {
                this.lineScrollOffset += position - k;
            }
            else if (position <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - position;
            }
            this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
        }
    }
    
    public void setCanLoseFocus(final boolean canLoseFocusIn) {
        this.canLoseFocus = canLoseFocusIn;
    }
    
    public boolean getVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean isVisible) {
        this.visible = isVisible;
    }
}
