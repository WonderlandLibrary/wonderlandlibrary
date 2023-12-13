package net.minecraft.client.gui.CustomButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

import me.aidanmees.trivia.client.tools.FontUtils;

public class GuiButtonDark extends GuiButton{
    private int x;
    private int y;
    private int x1;
    private int y1;
    private String text;

    public FontUtils font = new FontUtils("Segoe UI", Font.PLAIN, 25);

    public GuiButtonDark(int par1, int par2, int par3, int par4, int par5, String par6Str) {
        super(par1, par2, par3, par4, par5, par6Str);
        this.x = par2;
        this.y = par3;
        this.x1 = par4;
        this.y1 = par5;
        this.text = par6Str;
    }

    public GuiButtonDark(int i, int j, int k, String stringParams) {
        this(i, j, k, 149, 20, stringParams);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        boolean isOverButton = (mouseX >= this.x) && (mouseX <= this.x + this.x1) && (mouseY >= this.y)
                && (mouseY <= this.y + this.y1);
        if ((isOverButton)) {
            Gui.drawRect(this.xPosition , this.yPosition + height, this.xPosition + this.width, this.yPosition, 0x80000000);
            Gui.drawRect(this.xPosition, this.yPosition + height, this.xPosition + this.width, this.yPosition + height, 0xFFFFFFFF);
            
           
        }else{
            Gui.drawRect(this.xPosition, this.yPosition + height, this.xPosition + this.width, this.yPosition, 0x70000000);
            Gui.drawRect(this.xPosition, this.yPosition + height, this.xPosition + this.width, this.yPosition + height , 0xFFFFFFFF);
            
           
        }
        font.drawCenteredString(this.text, this.x + this.x1 / 2, (this.y + this.y1 / 2) - 9, -1);
    }
}
