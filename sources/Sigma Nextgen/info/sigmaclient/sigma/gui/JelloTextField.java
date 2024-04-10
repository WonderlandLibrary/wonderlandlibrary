package info.sigmaclient.sigma.gui;

import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

import static info.sigmaclient.sigma.utils.render.RenderUtils.drawRect;

public class JelloTextField extends TextFieldWidget
{
    String placeHold = "";

    @Getter
    @Setter
    private Color placeHoldColor = new Color(150, 150, 150);

    @Getter
    @Setter
    private Color textColor = new Color(220, 220, 220);

    public JelloTextField(int componentId, FontRenderer fontrendererObj, float x, float y, int par5Width, int par6Height) {
        super(fontrendererObj, (int)x, (int) y, par5Width, par6Height, new StringTextComponent(""));
        enableBackgroundDrawing = false;
    }

    public JelloTextField(int componentId, FontRenderer fontrendererObj, float x, float y, int par5Width, int par6Height, String placeHold) {
        super(fontrendererObj, (int)x, (int)y, par5Width, par6Height, new StringTextComponent(""));
        this.setEnableBackgroundDrawing(false);
        this.placeHold = placeHold;
    }

    /**
     * Draws the textbox
     */
    public void drawTextBox()
    {
        if (this.getVisible())
        {
            if (this.getEnableBackgroundDrawing())
            {
                drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
                drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
            }
            drawRect(
                    this.x - 1, this.y + this.height - 1,
                    this.x + this.width + 1, this.y + this.height,
                    new Color(200, 200, 200).getRGB());
            int i = new Color(0,0,0).getRGB();
            int j = this.getCursorPosition() - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String s = JelloFontUtil.jelloFont24.trimStringToWidth(this.getText().substring(this.lineScrollOffset), this.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused() && (System.currentTimeMillis() / 50) / 6 % 2 == 0 && flag;
            int l = this.enableBackgroundDrawing ? this.x + 4 : this.x;
            int i1 = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
            int j1 = l;

            if (k > s.length())
            {
                k = s.length();
            }

            if (!s.isEmpty())
            {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = JelloFontUtil.jelloFont24.drawString(s1, (float)l, (float)i1 + 3, textColor.getRGB());
            }else{
                JelloFontUtil.jelloFont24.drawNoBSString(placeHold, (float)l, (float)i1 + 3, placeHoldColor.getRGB());
            }

            boolean flag2 = this.getCursorPosition() < this.getText().length() || this.getText().length() >= this.getMaxStringLength();
            int k1 = j1;

            if (!flag)
            {
                k1 = j > 0 ? l + this.width : l;
            }
            else if (flag2)
            {
                k1 = j1 - 1;
                --j1;
            }

            if (!s.isEmpty() && flag && j < s.length())
            {
                j1 = JelloFontUtil.jelloFont24.drawNoBSString(s.substring(j), (float)j1 + 1, (float)i1 + 3, textColor.getRGB());
            }

            if (flag1)
            {
                drawRect(k1 + 1, i1 - 1 + 2, k1 + 2, i1 + 1 + JelloFontUtil.jelloFont22.getHeight() + 2, -3092272);
            }

            if (k != j)
            {
                int l1 = (int) (l + JelloFontUtil.jelloFont24.getStringWidth(s.substring(0, k)));
                this.drawSelectionBox(k1, i1 - 1 + 2, l1 - 1, i1 + 1 + JelloFontUtil.jelloFont24.getHeight() + 2);
            }
        }
    }
    /**
     * Draws the textbox
     */
    public void drawTextBoxCustom(float alpha, float textOffsetY, int placeHoldColo, int texts)
    {
        int al = (int)(alpha * 255);
        Color placeHoldColor = new Color(placeHoldColo, placeHoldColo, placeHoldColo, al);
        Color textColor = new Color(texts, texts, texts, al);

        if (this.getVisible())
        {
            if (this.getEnableBackgroundDrawing())
            {
                drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
                drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
            }
            drawRect(
                    this.x - 1, this.y + this.height - 1,
                    this.x + this.width + 1, this.y + this.height,
                    new Color(200, 200, 200, al).getRGB());
            int i = new Color(0,0,0).getRGB();
            int j = this.getCursorPosition() - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String s = JelloFontUtil.jelloFont24.trimStringToWidth(this.getText().substring(this.lineScrollOffset), this.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused() && (System.currentTimeMillis() / 50) / 6 % 2 == 0 && flag;
            int l = this.enableBackgroundDrawing ? this.x + 4 : this.x;
            int i1 = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
            int j1 = l;

            if (k > s.length())
            {
                k = s.length();
            }

            if (!s.isEmpty())
            {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = JelloFontUtil.jelloFont24.drawString(s1, (float)l, (float)i1 + 3 + textOffsetY, textColor.getRGB());
            }else{
                JelloFontUtil.jelloFont24.drawNoBSString(placeHold, (float)l, (float)i1 + 3 + textOffsetY, placeHoldColor.getRGB());
            }

            boolean flag2 = this.getCursorPosition() < this.getText().length() || this.getText().length() >= this.getMaxStringLength();
            int k1 = j1;

            if (!flag)
            {
                k1 = j > 0 ? l + this.width : l;
            }
            else if (flag2)
            {
                k1 = j1 - 1;
                --j1;
            }

            if (!s.isEmpty() && flag && j < s.length())
            {
                j1 = JelloFontUtil.jelloFont24.drawNoBSString(s.substring(j), (float)j1 + 1, (float)i1 + 3 + textOffsetY, textColor.getRGB());
            }

            if (flag1)
            {
                drawRect(k1 + 1, i1 - 1 + 2 + textOffsetY, k1 + 2, i1 + 1 + JelloFontUtil.jelloFont22.getHeight() + 2 + textOffsetY, ColorUtils.reAlpha(-3092272, al).getRGB());
            }

            if (k != j)
            {
                int l1 = (int) (l + JelloFontUtil.jelloFont24.getStringWidth(s.substring(0, k)) + 1);
                this.drawSelectionBox(k1, i1 - 1 + 2 + (int)textOffsetY, l1 - 1, i1 + 1 + JelloFontUtil.jelloFont24.getHeight() + 2 + (int)textOffsetY);
            }
        }
    }

    /**
     * Called when mouse is clicked, regardless as to whether it is over this button or not.
     */
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        boolean flag = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;

        if (this.canLoseFocus)
        {
            this.setFocused(flag);
        }

        if (this.isFocused() && flag && mouseButton == 0)
        {
            int i = mouseX - this.x + 4;

            if (this.enableBackgroundDrawing)
            {
                i -= 4;
            }

            String s = JelloFontUtil.jelloFont24.trimStringToWidth(this.getText().substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(JelloFontUtil.jelloFont24.trimStringToWidth(s, i).length() + this.lineScrollOffset);
            return true;
        }
        else
        {
            return false;
        }
    }
}
