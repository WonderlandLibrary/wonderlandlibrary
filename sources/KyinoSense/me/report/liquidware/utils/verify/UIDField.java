/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.MathHelper
 *  org.lwjgl.input.Mouse
 */
package me.report.liquidware.utils.verify;

import java.awt.Color;
import me.report.liquidware.utils.verify.GuiTextField;
import net.ccbluex.liquidbounce.features.module.modules.world.Timer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

public class UIDField
extends GuiTextField {
    private final String shit;
    private int color;
    private int textColor;
    private final Timer timer = new Timer();

    public UIDField(int componentId, FontRenderer fontRenderer, int x, int y, int width, int height, String shit) {
        super(componentId, fontRenderer, x, y, width, height);
        this.shit = shit;
    }

    @Override
    public void drawTextBox() {
        if (this.getVisible()) {
            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.func_71410_x());
            int lmx = scaledresolution.func_78326_a();
            int imy = scaledresolution.func_78328_b();
            int mouseX = Mouse.getX() * lmx / Minecraft.func_71410_x().field_71443_c;
            int mouseY = imy - Mouse.getY() * imy / Minecraft.func_71410_x().field_71440_d - 1;
            boolean hovered = (float)mouseX >= this.xPosition && (float)mouseY >= this.yPosition && (float)mouseX < this.xPosition + (float)this.width && (float)mouseY < this.yPosition + (float)this.height;
            RenderUtils.drawBorderedRect(this.xPosition, this.yPosition, this.xPosition + (float)this.width, this.yPosition + (float)this.height, 0.1f, this.color == -1 && (hovered || this.isFocused) ? new Color(0, 0, 0, 100).getRGB() : new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 50).getRGB());
            Fonts.font35.drawString("See your UID", this.xPosition + 5.0f, this.yPosition + 5.0f, this.textColor);
            int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String s = Fonts.font35.func_78269_a(this.text.substring(this.lineScrollOffset), this.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.enableBackgroundDrawing ? (int)this.xPosition + 4 : (int)this.xPosition;
            int i1 = this.enableBackgroundDrawing ? (int)this.yPosition + (this.height - 8) / 2 : (int)this.yPosition;
            int j1 = l;
            if (k > s.length()) {
                k = s.length();
            }
            if (!s.isEmpty()) {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = Fonts.font35.func_175065_a(s1, this.xPosition + 5.0f, this.yPosition + 17.0f, this.textColor, this.color != -1);
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
                Fonts.font35.func_175065_a(s.substring(j), j1, i1, i, true);
            }
            if (flag1) {
                if (flag2) {
                    Gui.func_73734_a((int)k1, (int)(i1 - 1), (int)(k1 + 1), (int)(i1 + 1 + this.fontRendererInstance.field_78288_b), (int)-3092272);
                } else {
                    Fonts.font35.func_175065_a("|", this.xPosition + 5.0f + (float)(this.getText().isEmpty() ? 0 : Fonts.font35.func_78256_a(this.getText()) + 1), this.yPosition + 17.0f, i, true);
                }
            }
            if (k != j) {
                int l1 = l + Fonts.font35.func_78256_a(s.substring(0, k));
                this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.field_78288_b);
            }
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
        boolean flag;
        boolean bl = flag = (float)p_146192_1_ >= this.xPosition && (float)p_146192_1_ < this.xPosition + (float)this.width && (float)p_146192_2_ >= this.yPosition && (float)p_146192_2_ < this.yPosition + (float)this.height;
        if (this.canLoseFocus) {
            this.setFocused(flag);
        }
        if (this.isFocused && flag && p_146192_3_ == 0) {
            int i = p_146192_1_ - (int)this.xPosition;
            if (this.enableBackgroundDrawing) {
                i -= 4;
            }
            String s = Fonts.font35.func_78269_a(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(Fonts.font35.func_78269_a(s, i).length() + this.lineScrollOffset);
        }
    }

    @Override
    public void setSelectionPos(int p_146199_1_) {
        int i = this.text.length();
        if (p_146199_1_ > i) {
            p_146199_1_ = i;
        }
        if (p_146199_1_ < 0) {
            p_146199_1_ = 0;
        }
        this.selectionEnd = p_146199_1_;
        if (this.lineScrollOffset > i) {
            this.lineScrollOffset = i;
        }
        int j = this.getWidth();
        String s = Fonts.font35.func_78269_a(this.text.substring(this.lineScrollOffset), j);
        int k = s.length() + this.lineScrollOffset;
        if (p_146199_1_ == this.lineScrollOffset) {
            this.lineScrollOffset -= Fonts.font35.func_78262_a(this.text, j, true).length();
        }
        if (p_146199_1_ > k) {
            this.lineScrollOffset += p_146199_1_ - k;
        } else if (p_146199_1_ <= this.lineScrollOffset) {
            this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
        }
        this.lineScrollOffset = MathHelper.func_76125_a((int)this.lineScrollOffset, (int)0, (int)i);
    }

    public void updateCoordinates(float x, float y) {
        this.xPosition = x;
        this.yPosition = y;
    }
}

