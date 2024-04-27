/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.elements;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.Element;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class ButtonElement
extends Element {
    protected String displayName;
    protected int color = 0xFFFFFF;
    public int hoverTime;

    @Override
    public void drawScreen(int n, int n2, float f) {
        LiquidBounce.clickGui.style.drawButtonElement(n, n2, this);
        super.drawScreen(n, n2, f);
    }

    public boolean isHovering(int n, int n2) {
        return n >= this.getX() && n <= this.getX() + this.getWidth() && n2 >= this.getY() && n2 <= this.getY() + 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    public ButtonElement(String string) {
        this.createButton(string);
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setColor(int n) {
        this.color = n;
    }

    public void createButton(String string) {
        this.displayName = string;
    }

    public int getColor() {
        return this.color;
    }
}

