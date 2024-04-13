package wtf.dawn.ui.components;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import wtf.dawn.ui.util.DrawUtility;
import wtf.dawn.utils.font.FontUtil;

public class TextField extends GuiTextField {
    public TextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }

    @Override
    public void drawTextBox() {
        if (this.getVisible())
        {
            if (this.getEnableBackgroundDrawing())
            {

                DrawUtility.drawRoundedRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.getWidth() + 1, this.yPosition + this.height + 1, 4, -6250336);
                DrawUtility.drawRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.getWidth(), this.yPosition + this.height, 4, -16777216);

            }

            int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            int j = this.getCursorPosition() - this.lineScrollOffset;
            int k = this.getSelectionEnd() - this.lineScrollOffset;
            String s = FontUtil.normal.trimStringToWidth(this.getText().substring(this.lineScrollOffset), this.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused() && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
            int i1 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
            int j1 = l;

            if (k > s.length())
            {
                k = s.length();
            }

            if (s.length() > 0)
            {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = FontUtil.normal.drawStringWithShadow(s1, (float)l, (float)i1, i);
            }

            boolean flag2 = this.getCursorPosition() < this.getText().length() || this.getText().length() >= this.getMaxStringLength();
            int k1 = j1;

            if (!flag)
            {
                k1 = j > 0 ? l + this.getWidth() : l;
            }
            else if (flag2)
            {
                k1 = j1 - 1;
                --j1;
            }

            if (s.length() > 0 && flag && j < s.length())
            {
                j1 = FontUtil.normal.drawStringWithShadow(s.substring(j), (float)j1, (float)i1, i);
            }

            if (flag1)
            {
                if (flag2)
                {
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + FontUtil.normal.getHeight(), -3092272);
                }
                else
                {
                    FontUtil.normal.drawStringWithShadow("_", (float)k1, (float)i1, i);
                }
            }

            if (k != j)
            {
                int l1 = (int) (l + FontUtil.normal.getStringWidth(s.substring(0, k)));
                this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + FontUtil.normal.getHeight()
                );
            }
        }
    }
}
