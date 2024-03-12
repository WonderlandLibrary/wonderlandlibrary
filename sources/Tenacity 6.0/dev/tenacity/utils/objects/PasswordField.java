// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.objects;

import org.lwjgl.input.Keyboard;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import dev.tenacity.utils.render.GLUtil;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import com.google.common.base.Predicate;
import net.minecraft.client.gui.GuiPageButtonList;
import dev.tenacity.utils.font.CustomFont;
import net.minecraft.client.gui.Gui;

public class PasswordField extends Gui
{
    private final int id;
    private final int height;
    private final CustomFont fontRenderer;
    public int width;
    public int bottomBarColor;
    public int textColor;
    public int cursorColor;
    public int xPosition;
    public int yPosition;
    public String placeholder;
    public double placeHolderTextX;
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
    private GuiPageButtonList.GuiResponder field_175210_x;
    private Predicate<String> field_175209_y;
    
    public PasswordField(final String placeholder, final int componentId, final int x, final int y, final int par5Width, final int par6Height, final CustomFont fr) {
        this.bottomBarColor = -1;
        this.textColor = -1;
        this.cursorColor = -1;
        this.text = "";
        this.maxStringLength = 32;
        this.enableBackgroundDrawing = true;
        this.canLoseFocus = true;
        this.isEnabled = true;
        this.enabledColor = 14737632;
        this.disabledColor = 7368816;
        this.visible = true;
        this.field_175209_y = (Predicate<String>)(s -> true);
        this.placeholder = placeholder;
        this.id = componentId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = par5Width;
        this.height = par6Height;
        this.fontRenderer = fr;
        this.placeHolderTextX = (this.xPosition + this.width) / 2.0f;
    }
    
    public PasswordField(final String placeholder, final int componentId, final int x, final int y, final int par5Width, final int par6Height, final CustomFont fr, final int textColor) {
        this.bottomBarColor = -1;
        this.textColor = -1;
        this.cursorColor = -1;
        this.text = "";
        this.maxStringLength = 32;
        this.enableBackgroundDrawing = true;
        this.canLoseFocus = true;
        this.isEnabled = true;
        this.enabledColor = 14737632;
        this.disabledColor = 7368816;
        this.visible = true;
        this.field_175209_y = (Predicate<String>)(s -> true);
        this.placeholder = placeholder;
        this.id = componentId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = par5Width;
        this.height = par6Height;
        this.fontRenderer = fr;
        this.textColor = textColor;
        this.placeHolderTextX = (this.xPosition + this.width) / 2.0f;
    }
    
    public void func_175207_a(final GuiPageButtonList.GuiResponder p_175207_1_) {
        this.field_175210_x = p_175207_1_;
    }
    
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String p_146180_1_) {
        if (this.field_175209_y.apply((Object)p_146180_1_)) {
            if (p_146180_1_.length() > this.maxStringLength) {
                this.text = p_146180_1_.substring(0, this.maxStringLength);
            }
            else {
                this.text = p_146180_1_;
            }
            this.setCursorPositionEnd();
        }
    }
    
    public String getSelectedText() {
        final int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(i, j);
    }
    
    public void func_175205_a(final Predicate<String> p_175205_1_) {
        this.field_175209_y = p_175205_1_;
    }
    
    public void writeText(final String text) {
        String s = "";
        final String s2 = ChatAllowedCharacters.filterAllowedCharacters(text);
        final int i = Math.min(this.cursorPosition, this.selectionEnd);
        final int j = Math.max(this.cursorPosition, this.selectionEnd);
        final int k = this.maxStringLength - this.text.length() - (i - j);
        if (this.text.length() > 0) {
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
        if (this.text.length() > 0 && j < this.text.length()) {
            s += this.text.substring(j);
        }
        if (this.field_175209_y.apply((Object)s)) {
            this.text = s;
            this.moveCursorBy(i - this.selectionEnd + l);
            if (this.field_175210_x != null) {
                this.field_175210_x.func_175319_a(this.id, this.text);
            }
        }
    }
    
    public void deleteWords(final int p_146177_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                this.deleteFromCursor(this.getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
            }
        }
    }
    
    public void drawTextBox() {
        this.drawTextBox(this.text, false);
    }
    
    public void drawPasswordBox() {
        this.drawTextBox(this.text, true);
    }
    
    public void deleteFromCursor(final int p_146175_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                final boolean flag = p_146175_1_ < 0;
                final int i = flag ? (this.cursorPosition + p_146175_1_) : this.cursorPosition;
                final int j = flag ? this.cursorPosition : (this.cursorPosition + p_146175_1_);
                String s = "";
                if (i >= 0) {
                    s = this.text.substring(0, i);
                }
                if (j < this.text.length()) {
                    s += this.text.substring(j);
                }
                if (this.field_175209_y.apply((Object)s)) {
                    this.text = s;
                    if (flag) {
                        this.moveCursorBy(p_146175_1_);
                    }
                    if (this.field_175210_x != null) {
                        this.field_175210_x.func_175319_a(this.id, this.text);
                    }
                }
            }
        }
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getNthWordFromCursor(final int p_146187_1_) {
        return this.getNthWordFromPos(p_146187_1_, this.getCursorPosition());
    }
    
    public int getNthWordFromPos(final int p_146183_1_, final int p_146183_2_) {
        return this.func_146197_a(p_146183_1_, p_146183_2_, true);
    }
    
    public int func_146197_a(final int p_146197_1_, final int p_146197_2_, final boolean p_146197_3_) {
        int i = p_146197_2_;
        final boolean flag = p_146197_1_ < 0;
        for (int j = Math.abs(p_146197_1_), k = 0; k < j; ++k) {
            if (!flag) {
                final int l = this.text.length();
                i = this.text.indexOf(32, i);
                if (i == -1) {
                    i = l;
                }
                else {
                    while (p_146197_3_ && i < l && this.text.charAt(i) == ' ') {
                        ++i;
                    }
                }
            }
            else {
                while (p_146197_3_ && i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }
                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }
        return i;
    }
    
    public void moveCursorBy(final int p_146182_1_) {
        this.setCursorPosition(this.selectionEnd + p_146182_1_);
    }
    
    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }
    
    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }
    
    public boolean textboxKeyTyped(final char p_146201_1_, final int p_146201_2_) {
        if (!this.isFocused) {
            return false;
        }
        if (GuiScreen.isKeyComboCtrlA(p_146201_2_)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        }
        if (GuiScreen.isKeyComboCtrlC(p_146201_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        }
        if (GuiScreen.isKeyComboCtrlV(p_146201_2_)) {
            if (this.isEnabled) {
                this.writeText(GuiScreen.getClipboardString());
            }
            return true;
        }
        if (GuiScreen.isKeyComboCtrlX(p_146201_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            if (this.isEnabled) {
                this.writeText("");
            }
            return true;
        }
        switch (p_146201_2_) {
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
                if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_)) {
                    if (this.isEnabled) {
                        this.writeText(Character.toString(p_146201_1_));
                    }
                    return true;
                }
                return false;
            }
        }
    }
    
    public void mouseClicked(final int p_146192_1_, final int p_146192_2_, final int p_146192_3_) {
        final boolean flag = p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height;
        if (this.canLoseFocus) {
            this.setFocused(flag);
        }
        if (this.isFocused && flag && p_146192_3_ == 0) {
            int i = p_146192_1_ - this.xPosition;
            if (this.enableBackgroundDrawing) {
                i -= 4;
            }
            final String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRenderer.trimStringToWidth(s, i).length() + this.lineScrollOffset);
        }
    }
    
    public void drawTextBox(String text, final boolean password) {
        if (password) {
            text = text.replaceAll(".", "*");
        }
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                RenderUtil.resetColor();
                Gui.drawRect(this.xPosition, this.yPosition + this.height, this.xPosition + this.width, this.yPosition + this.height + 1, this.bottomBarColor);
            }
            RenderUtil.resetColor();
            final int i = this.textColor;
            final int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            final String s = this.fontRenderer.trimStringToWidth(text.substring(this.lineScrollOffset), this.getWidth());
            final boolean flag = j >= 0 && j <= s.length();
            final boolean flag2 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            final int l = this.enableBackgroundDrawing ? (this.xPosition + 4) : this.xPosition;
            final int i2 = this.enableBackgroundDrawing ? (this.yPosition + (this.height - 8) / 4) : this.yPosition;
            int j2 = l;
            if (!this.isFocused && this.placeholder != null && text.isEmpty()) {
                this.fontRenderer.drawCenteredString(this.placeholder, (float)this.placeHolderTextX, (float)i2, this.textColor);
            }
            if (k > s.length()) {
                k = s.length();
            }
            if (s.length() > 0) {
                final String s2 = flag ? s.substring(0, j) : s;
                j2 = this.fontRenderer.drawString(s2, (float)l, (float)i2, i);
            }
            final boolean flag3 = this.cursorPosition < text.length() || text.length() >= this.getMaxStringLength();
            int k2 = j2;
            if (!flag) {
                k2 = ((j > 0) ? (l + this.width) : l);
            }
            else if (flag3) {
                k2 = j2 - 1;
                --j2;
            }
            if (s.length() > 0 && flag && j < s.length()) {
                RenderUtil.resetColor();
                j2 = this.fontRenderer.drawString(s.substring(j), j2 + 6.0f, (float)i2, i);
            }
            if (flag2) {
                RenderUtil.resetColor();
                if (flag3) {
                    Gui.drawRect(k2 + 4, i2 - 1, k2 + 5, i2 + 1 + this.fontRenderer.getHeight(), this.cursorColor);
                }
                else if (System.currentTimeMillis() % 1000L < 500L) {
                    this.fontRenderer.drawString("|", k2 + 2.0f, i2 - 0.5f, this.textColor);
                }
            }
            if (k != j) {
                final int l2 = (int)(l + this.fontRenderer.getStringWidth(s.substring(0, k)));
                this.drawCursorVertical(k2, i2 - 1, l2 - 1, i2 + 1 + this.fontRenderer.getHeight());
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
        if (p_146188_1_ < p_146188_3_) {
            final int i = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = i;
        }
        if (p_146188_2_ < p_146188_4_) {
            final int j = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = j;
        }
        if (p_146188_3_ > this.xPosition + this.width) {
            p_146188_3_ = this.xPosition + this.width;
        }
        if (p_146188_1_ > this.xPosition + this.width) {
            p_146188_1_ = this.xPosition + this.width;
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
        GLUtil.setup2DRendering();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(p_146188_1_, p_146188_4_, 0.0).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_4_, 0.0).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_2_, 0.0).endVertex();
        worldrenderer.pos(p_146188_1_, p_146188_2_, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GLUtil.end2DRendering();
    }
    
    public int getMaxStringLength() {
        return this.maxStringLength;
    }
    
    public void setMaxStringLength(final int p_146203_1_) {
        this.maxStringLength = p_146203_1_;
        if (this.text.length() > p_146203_1_) {
            this.text = this.text.substring(0, p_146203_1_);
        }
    }
    
    public int getCursorPosition() {
        return this.cursorPosition;
    }
    
    public void setCursorPosition(final int p_146190_1_) {
        this.cursorPosition = p_146190_1_;
        final int i = this.text.length();
        this.setSelectionPos(this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, i));
    }
    
    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }
    
    public void setEnableBackgroundDrawing(final boolean p_146185_1_) {
        this.enableBackgroundDrawing = p_146185_1_;
    }
    
    public void setTextColor(final int p_146193_1_) {
        this.enabledColor = p_146193_1_;
    }
    
    public void setDisabledTextColour(final int p_146204_1_) {
        this.disabledColor = p_146204_1_;
    }
    
    public boolean isFocused() {
        return this.isFocused;
    }
    
    public void setFocused(final boolean p_146195_1_) {
        if (p_146195_1_ && !this.isFocused) {
            this.cursorCounter = 0;
        }
        Keyboard.enableRepeatEvents(this.isFocused = p_146195_1_);
    }
    
    public void setEnabled(final boolean p_146184_1_) {
        this.isEnabled = p_146184_1_;
    }
    
    public int getSelectionEnd() {
        return this.selectionEnd;
    }
    
    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
    }
    
    public void setSelectionPos(int p_146199_1_) {
        final int i = this.text.length();
        if (p_146199_1_ > i) {
            p_146199_1_ = i;
        }
        if (p_146199_1_ < 0) {
            p_146199_1_ = 0;
        }
        this.selectionEnd = p_146199_1_;
        if (this.fontRenderer != null) {
            if (this.lineScrollOffset > i) {
                this.lineScrollOffset = i;
            }
            final int j = this.getWidth();
            final String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
            final int k = s.length() + this.lineScrollOffset;
            if (p_146199_1_ == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text, j, true).length();
            }
            if (p_146199_1_ > k) {
                this.lineScrollOffset += p_146199_1_ - k;
            }
            else if (p_146199_1_ <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
            }
            this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, i);
        }
    }
    
    public void setCanLoseFocus(final boolean p_146205_1_) {
        this.canLoseFocus = p_146205_1_;
    }
    
    public boolean getVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean p_146189_1_) {
        this.visible = p_146189_1_;
    }
}
