package me.aquavit.liquidsense.ui.client.gui.elements;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

import java.awt.*;

public class GuiUsernameField extends Gui {

    private final int id;
    private final FontRenderer fontRendererInstance;
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;
    private String text = "";
    private int maxStringLength = 32;
    private int cursorCounter;
    private boolean enableBackgroundDrawing = true;
    private boolean canLoseFocus = true;
    private boolean isFocused;
    private boolean isEnabled = true;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;
    private boolean visible = true;
    private GuiPageButtonList.GuiResponder field_175210_x;
    private Predicate<String> validator = Predicates.alwaysTrue();

    public GuiUsernameField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        this.id = componentId;
        this.fontRendererInstance = fontrendererObj;
        this.xPosition = x;
        this.yPosition = y;
        this.width = par5Width;
        this.height = par6Height;
    }

    public void func_175207_a(GuiPageButtonList.GuiResponder p_175207_1_) {
        this.field_175210_x = p_175207_1_;
    }

    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    public void setText(String p_setText_1_) {
        if (this.validator.apply(p_setText_1_)) {
            if (p_setText_1_.length() > this.maxStringLength) {
                this.text = p_setText_1_.substring(0, this.maxStringLength);
            } else {
                this.text = p_setText_1_;
            }

            this.setCursorPositionEnd();
        }
    }

    public String getText() {
        return this.text;
    }

    public String getSelectedText() {
        int lvt_1_1_ = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int lvt_2_1_ = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(lvt_1_1_, lvt_2_1_);
    }

    public void setValidator(Predicate<String> p_setValidator_1_) {
        this.validator = p_setValidator_1_;
    }

    public void writeText(String p_writeText_1_) {
        String lvt_2_1_ = "";
        String lvt_3_1_ = ChatAllowedCharacters.filterAllowedCharacters(p_writeText_1_);
        int lvt_4_1_ = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int lvt_5_1_ = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int lvt_6_1_ = this.maxStringLength - this.text.length() - (lvt_4_1_ - lvt_5_1_);
        if (this.text.length() > 0) {
            lvt_2_1_ = lvt_2_1_ + this.text.substring(0, lvt_4_1_);
        }

        int lvt_7_1_;
        if (lvt_6_1_ < lvt_3_1_.length()) {
            lvt_2_1_ = lvt_2_1_ + lvt_3_1_.substring(0, lvt_6_1_);
            lvt_7_1_ = lvt_6_1_;
        } else {
            lvt_2_1_ = lvt_2_1_ + lvt_3_1_;
            lvt_7_1_ = lvt_3_1_.length();
        }

        if (this.text.length() > 0 && lvt_5_1_ < this.text.length()) {
            lvt_2_1_ = lvt_2_1_ + this.text.substring(lvt_5_1_);
        }

        if (this.validator.apply(lvt_2_1_)) {
            this.text = lvt_2_1_;
            this.moveCursorBy(lvt_4_1_ - this.selectionEnd + lvt_7_1_);
            if (this.field_175210_x != null) {
                this.field_175210_x.func_175319_a(this.id, this.text);
            }

        }
    }

    public void deleteWords(int p_deleteWords_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(p_deleteWords_1_) - this.cursorPosition);
            }
        }
    }

    public void deleteFromCursor(int p_deleteFromCursor_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean lvt_2_1_ = p_deleteFromCursor_1_ < 0;
                int lvt_3_1_ = lvt_2_1_ ? this.cursorPosition + p_deleteFromCursor_1_ : this.cursorPosition;
                int lvt_4_1_ = lvt_2_1_ ? this.cursorPosition : this.cursorPosition + p_deleteFromCursor_1_;
                String lvt_5_1_ = "";
                if (lvt_3_1_ >= 0) {
                    lvt_5_1_ = this.text.substring(0, lvt_3_1_);
                }

                if (lvt_4_1_ < this.text.length()) {
                    lvt_5_1_ = lvt_5_1_ + this.text.substring(lvt_4_1_);
                }

                if (this.validator.apply(lvt_5_1_)) {
                    this.text = lvt_5_1_;
                    if (lvt_2_1_) {
                        this.moveCursorBy(p_deleteFromCursor_1_);
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

    public int getNthWordFromCursor(int p_getNthWordFromCursor_1_) {
        return this.getNthWordFromPos(p_getNthWordFromCursor_1_, this.getCursorPosition());
    }

    public int getNthWordFromPos(int p_getNthWordFromPos_1_, int p_getNthWordFromPos_2_) {
        return this.func_146197_a(p_getNthWordFromPos_1_, p_getNthWordFromPos_2_, true);
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
        int lvt_4_1_ = p_146197_2_;
        boolean lvt_5_1_ = p_146197_1_ < 0;
        int lvt_6_1_ = Math.abs(p_146197_1_);

        for(int lvt_7_1_ = 0; lvt_7_1_ < lvt_6_1_; ++lvt_7_1_) {
            if (!lvt_5_1_) {
                int lvt_8_1_ = this.text.length();
                lvt_4_1_ = this.text.indexOf(32, lvt_4_1_);
                if (lvt_4_1_ == -1) {
                    lvt_4_1_ = lvt_8_1_;
                } else {
                    while(p_146197_3_ && lvt_4_1_ < lvt_8_1_ && this.text.charAt(lvt_4_1_) == ' ') {
                        ++lvt_4_1_;
                    }
                }
            } else {
                while(p_146197_3_ && lvt_4_1_ > 0 && this.text.charAt(lvt_4_1_ - 1) == ' ') {
                    --lvt_4_1_;
                }

                while(lvt_4_1_ > 0 && this.text.charAt(lvt_4_1_ - 1) != ' ') {
                    --lvt_4_1_;
                }
            }
        }

        return lvt_4_1_;
    }

    public void moveCursorBy(int p_moveCursorBy_1_) {
        this.setCursorPosition(this.selectionEnd + p_moveCursorBy_1_);
    }

    public void setCursorPosition(int p_setCursorPosition_1_) {
        this.cursorPosition = p_setCursorPosition_1_;
        int lvt_2_1_ = this.text.length();
        this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, lvt_2_1_);
        this.setSelectionPos(this.cursorPosition);
    }

    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    public boolean textboxKeyTyped(char p_textboxKeyTyped_1_, int p_textboxKeyTyped_2_) {
        if (!this.isFocused) {
            return false;
        } else if (GuiScreen.isKeyComboCtrlA(p_textboxKeyTyped_2_)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        } else if (GuiScreen.isKeyComboCtrlC(p_textboxKeyTyped_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        } else if (GuiScreen.isKeyComboCtrlV(p_textboxKeyTyped_2_)) {
            if (this.isEnabled) {
                this.writeText(GuiScreen.getClipboardString());
            }

            return true;
        } else if (GuiScreen.isKeyComboCtrlX(p_textboxKeyTyped_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            if (this.isEnabled) {
                this.writeText("");
            }

            return true;
        } else {
            switch(p_textboxKeyTyped_2_) {
                case 14:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.isEnabled) {
                            this.deleteWords(-1);
                        }
                    } else if (this.isEnabled) {
                        this.deleteFromCursor(-1);
                    }

                    return true;
                case 199:
                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(0);
                    } else {
                        this.setCursorPositionZero();
                    }

                    return true;
                case 203:
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
                case 205:
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
                case 207:
                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(this.text.length());
                    } else {
                        this.setCursorPositionEnd();
                    }

                    return true;
                case 211:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.isEnabled) {
                            this.deleteWords(1);
                        }
                    } else if (this.isEnabled) {
                        this.deleteFromCursor(1);
                    }

                    return true;
                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(p_textboxKeyTyped_1_)) {
                        if (this.isEnabled) {
                            this.writeText(Character.toString(p_textboxKeyTyped_1_));
                        }

                        return true;
                    } else {
                        return false;
                    }
            }
        }
    }

    public void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) {
        boolean lvt_4_1_ = p_mouseClicked_1_ >= this.xPosition && p_mouseClicked_1_ < this.xPosition + this.width && p_mouseClicked_2_ >= this.yPosition && p_mouseClicked_2_ < this.yPosition + this.height;
        if (this.canLoseFocus) {
            this.setFocused(lvt_4_1_);
        }

        if (this.isFocused && lvt_4_1_ && p_mouseClicked_3_ == 0) {
            int lvt_5_1_ = p_mouseClicked_1_ - this.xPosition;
            if (this.enableBackgroundDrawing) {
                lvt_5_1_ -= 4;
            }

            String lvt_6_1_ = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(lvt_6_1_, lvt_5_1_).length() + this.lineScrollOffset);
        }

    }

    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                //drawRect(this.xPosition, this.yPosition - 1, this.xPosition + this.width, this.yPosition + this.height + 1, -6250336);

                //drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216); //背景
                drawRect(this.xPosition, this.yPosition + this.height, this.xPosition + this.width, this.yPosition + this.height + 1, Color.WHITE.getRGB());//描边
            }

            int fontColor = this.isEnabled ? Color.WHITE.getRGB() : this.disabledColor;
            int lvt_2_1_ = this.cursorPosition - this.lineScrollOffset;
            int lvt_3_1_ = this.selectionEnd - this.lineScrollOffset;
            String lvt_4_1_ = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            boolean lvt_5_1_ = lvt_2_1_ >= 0 && lvt_2_1_ <= lvt_4_1_.length();
            boolean lvt_6_1_ = this.isFocused && this.cursorCounter / 6 % 2 == 0 && lvt_5_1_;
            int lvt_7_1_ = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
            int lvt_8_1_ = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
            int lvt_9_1_ = lvt_7_1_;
            if (lvt_3_1_ > lvt_4_1_.length()) {
                lvt_3_1_ = lvt_4_1_.length();
            }

            if (lvt_4_1_.length() > 0) {
                String lvt_10_1_ = lvt_5_1_ ? lvt_4_1_.substring(0, lvt_2_1_) : lvt_4_1_;
                lvt_9_1_ = this.fontRendererInstance.drawStringWithShadow(lvt_10_1_, (float)lvt_7_1_, (float)lvt_8_1_ + 1, fontColor);
            }

            boolean lvt_10_2_ = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int lvt_11_1_ = lvt_9_1_;
            if (!lvt_5_1_) {
                lvt_11_1_ = lvt_2_1_ > 0 ? lvt_7_1_ + this.width : lvt_7_1_;
            } else if (lvt_10_2_) {
                lvt_11_1_ = lvt_9_1_ - 1;
                --lvt_9_1_;
            }

            if (lvt_4_1_.length() > 0 && lvt_5_1_ && lvt_2_1_ < lvt_4_1_.length()) {
                lvt_9_1_ = this.fontRendererInstance.drawStringWithShadow(lvt_4_1_.substring(lvt_2_1_), (float)lvt_9_1_, (float)lvt_8_1_, fontColor);
            }

            if (lvt_6_1_) {
                if (lvt_10_2_) {
                    Gui.drawRect(lvt_11_1_, lvt_8_1_ - 1, lvt_11_1_ + 1, lvt_8_1_ + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
                } else {
                    this.fontRendererInstance.drawStringWithShadow("_", (float)lvt_11_1_, (float)lvt_8_1_, fontColor);
                }
            }

            if (lvt_3_1_ != lvt_2_1_) {
                int lvt_12_1_ = lvt_7_1_ + this.fontRendererInstance.getStringWidth(lvt_4_1_.substring(0, lvt_3_1_));
                this.drawCursorVertical(lvt_11_1_, lvt_8_1_ - 1, lvt_12_1_ - 1, lvt_8_1_ + 1 + this.fontRendererInstance.FONT_HEIGHT);
            }

        }
    }

    private void drawCursorVertical(int p_drawCursorVertical_1_, int p_drawCursorVertical_2_, int p_drawCursorVertical_3_, int p_drawCursorVertical_4_) {
        int lvt_5_2_;
        if (p_drawCursorVertical_1_ < p_drawCursorVertical_3_) {
            lvt_5_2_ = p_drawCursorVertical_1_;
            p_drawCursorVertical_1_ = p_drawCursorVertical_3_;
            p_drawCursorVertical_3_ = lvt_5_2_;
        }

        if (p_drawCursorVertical_2_ < p_drawCursorVertical_4_) {
            lvt_5_2_ = p_drawCursorVertical_2_;
            p_drawCursorVertical_2_ = p_drawCursorVertical_4_;
            p_drawCursorVertical_4_ = lvt_5_2_;
        }

        if (p_drawCursorVertical_3_ > this.xPosition + this.width) {
            p_drawCursorVertical_3_ = this.xPosition + this.width;
        }

        if (p_drawCursorVertical_1_ > this.xPosition + this.width) {
            p_drawCursorVertical_1_ = this.xPosition + this.width;
        }

        Tessellator lvt_5_3_ = Tessellator.getInstance();
        WorldRenderer lvt_6_1_ = lvt_5_3_.getWorldRenderer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        lvt_6_1_.begin(7, DefaultVertexFormats.POSITION);
        lvt_6_1_.pos((double)p_drawCursorVertical_1_, (double)p_drawCursorVertical_4_, 0.0D).endVertex();
        lvt_6_1_.pos((double)p_drawCursorVertical_3_, (double)p_drawCursorVertical_4_, 0.0D).endVertex();
        lvt_6_1_.pos((double)p_drawCursorVertical_3_, (double)p_drawCursorVertical_2_, 0.0D).endVertex();
        lvt_6_1_.pos((double)p_drawCursorVertical_1_, (double)p_drawCursorVertical_2_, 0.0D).endVertex();
        lvt_5_3_.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public void setMaxStringLength(int p_setMaxStringLength_1_) {
        this.maxStringLength = p_setMaxStringLength_1_;
        if (this.text.length() > p_setMaxStringLength_1_) {
            this.text = this.text.substring(0, p_setMaxStringLength_1_);
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

    public void setEnableBackgroundDrawing(boolean p_setEnableBackgroundDrawing_1_) {
        this.enableBackgroundDrawing = p_setEnableBackgroundDrawing_1_;
    }

    public void setTextColor(int p_setTextColor_1_) {
        this.enabledColor = p_setTextColor_1_;
    }

    public void setDisabledTextColour(int p_setDisabledTextColour_1_) {
        this.disabledColor = p_setDisabledTextColour_1_;
    }

    public void setFocused(boolean p_setFocused_1_) {
        if (p_setFocused_1_ && !this.isFocused) {
            this.cursorCounter = 0;
        }

        this.isFocused = p_setFocused_1_;
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public void setEnabled(boolean p_setEnabled_1_) {
        this.isEnabled = p_setEnabled_1_;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    public void setSelectionPos(int p_setSelectionPos_1_) {
        int lvt_2_1_ = this.text.length();
        if (p_setSelectionPos_1_ > lvt_2_1_) {
            p_setSelectionPos_1_ = lvt_2_1_;
        }

        if (p_setSelectionPos_1_ < 0) {
            p_setSelectionPos_1_ = 0;
        }

        this.selectionEnd = p_setSelectionPos_1_;
        if (this.fontRendererInstance != null) {
            if (this.lineScrollOffset > lvt_2_1_) {
                this.lineScrollOffset = lvt_2_1_;
            }

            int lvt_3_1_ = this.getWidth();
            String lvt_4_1_ = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), lvt_3_1_);
            int lvt_5_1_ = lvt_4_1_.length() + this.lineScrollOffset;
            if (p_setSelectionPos_1_ == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, lvt_3_1_, true).length();
            }

            if (p_setSelectionPos_1_ > lvt_5_1_) {
                this.lineScrollOffset += p_setSelectionPos_1_ - lvt_5_1_;
            } else if (p_setSelectionPos_1_ <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - p_setSelectionPos_1_;
            }

            this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, lvt_2_1_);
        }

    }

    public void setCanLoseFocus(boolean p_setCanLoseFocus_1_) {
        this.canLoseFocus = p_setCanLoseFocus_1_;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean p_setVisible_1_) {
        this.visible = p_setVisible_1_;
    }
}
