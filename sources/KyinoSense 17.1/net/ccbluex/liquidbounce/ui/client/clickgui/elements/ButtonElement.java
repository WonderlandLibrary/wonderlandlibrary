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

    public ButtonElement(String displayName) {
        this.createButton(displayName);
    }

    public void createButton(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float button) {
        LiquidBounce.clickGui.style.drawButtonElement(mouseX, mouseY, this);
        super.drawScreen(mouseX, mouseY, button);
    }

    @Override
    public int getHeight() {
        return 16;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + 16;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }
}

