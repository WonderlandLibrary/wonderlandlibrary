package cc.novoline.gui.screen.alt.repository;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.MathHelper;

import java.awt.*;

import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_16.SFBOLD_16;

public class TokenField extends GuiTextField {

    private final String shit;

    public TokenField(int componentId, FontRenderer fontRenderer, int x, int y, int width, int height, String shit) {
        super(componentId, fontRenderer, x, y, width, height);
        this.shit = shit;
    }

    @Override
    public void drawTextBox() {
        if (this.getVisible()) {
            SFBOLD_16.drawString(this.shit, this.xPosition - SFBOLD_16.stringWidth(this.shit) - 5,
                    this.yPosition + 5, new Color(198, 198, 198).getRGB());
            drawRect(this.xPosition, this.yPosition + 15, this.xPosition + this.width, this.yPosition + 16,
                    new Color(198, 198, 198).getRGB());
            int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String s = SFBOLD_16.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.enableBackgroundDrawing ? (int) this.xPosition + 4 : (int) this.xPosition;
            int i1 = this.enableBackgroundDrawing ? (int) this.yPosition + (this.height - 8) / 2 : (int) this.yPosition;
            int j1 = l;

            if (k > s.length()) {
                k = s.length();
            }

            if (!s.isEmpty()) {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = (int) SFBOLD_16.drawString(s1, (float) l, (float) i1, i, true);
            }

            boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int k1 = j1;

            if (!flag) {
                k1 = j > 0 ? l + this.width : l;
            } else if (flag2) {
                k1 = j1 - 1;
                --j1;
            }

            if (!s.isEmpty() && flag && j < s.length()) {
                SFBOLD_16.drawString(s.substring(j), (float) j1, (float) i1, i, true);
            }

            if (flag1) {
                if (flag2) {
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRendererInstance.getHeight(), -3092272);
                } else {
                    SFBOLD_16.drawString("_", (float) k1, (float) i1, i, true);
                }
            }

            if (k != j) {
                int l1 = l + SFBOLD_16.stringWidth(s.substring(0, k));
                this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.getHeight());
            }
        }
    }

    @Override
    public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
        boolean flag = p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height;

        if (this.canLoseFocus) {
            setFocused(flag);
        }

        if (this.isFocused && flag && p_146192_3_ == 0) {
            int i = p_146192_1_ -(int)  this.xPosition;

            if (this.enableBackgroundDrawing) {
                i -= 4;
            }

            final String s = SFBOLD_16
                    .trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            setCursorPosition(SFBOLD_16.trimStringToWidth(s, i).length() + this.lineScrollOffset);
        }
    }

    @Override
    public void setSelectionPos(int p_146199_1_) {
        final int i = this.text.length();

        if (p_146199_1_ > i) p_146199_1_ = i;
        if (p_146199_1_ < 0) p_146199_1_ = 0;

        this.selectionEnd = p_146199_1_;

        if (this.lineScrollOffset > i) {
            this.lineScrollOffset = i;
        }

        final int j = getWidth();
        final String s = SFBOLD_16.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
        final int k = s.length() + this.lineScrollOffset;

        if (p_146199_1_ == this.lineScrollOffset) {
            this.lineScrollOffset -= SFBOLD_16.trimStringToWidth(this.text, j, true).length();
        }

        if (p_146199_1_ > k) {
            this.lineScrollOffset += p_146199_1_ - k;
        } else if (p_146199_1_ <= this.lineScrollOffset) {
            this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
        }

        this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, i);
    }

}
