/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.click;

import java.awt.Color;
import me.AveReborn.Client;
import me.AveReborn.mod.Category;
import me.AveReborn.ui.click.ClickMenuMods;
import me.AveReborn.util.Colors;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClickMenuCategory {
    public Category c;
    ClickMenuMods uiMenuMods;
    private MouseInputHandler handler;
    public boolean open;
    public int x;
    public int y;
    public int width;
    public int tab_height;
    public int x2;
    public int y2;
    public boolean drag = true;
    private double arrowAngle = 0.0;

    public ClickMenuCategory(Category c2, int x2, int y2, int width, int tab_height, MouseInputHandler handler) {
        this.c = c2;
        this.x = x2;
        this.y = y2;
        this.width = width;
        this.tab_height = tab_height;
        this.uiMenuMods = new ClickMenuMods(c2, handler);
        this.handler = handler;
    }

    public void draw(int mouseX, int mouseY) {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        UnicodeFontRenderer font = Client.instance.fontMgr.tahoma15;
        RenderUtil.drawRoundedRect(this.x, this.y, this.x + this.width, this.y + this.tab_height - 1, 1.5f, new Color(51, 102, 205).getRGB());
        String name = "";
        name = "Panel " + this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length());
        font.drawString(name, (float)this.x + (float)((this.width - font.getStringWidth(name)) / 2), this.y + (this.tab_height - font.FONT_HEIGHT) / 2, Colors.WHITE.c);
        double xMid = this.x + this.width - 6;
        double yMid = this.y + 10;
        this.arrowAngle = RenderUtil.getAnimationState(this.arrowAngle, this.uiMenuMods.open ? -90 : 0, 1000.0);
        GL11.glPushMatrix();
        GL11.glTranslated(xMid, yMid, 0.0);
        GL11.glRotated(this.arrowAngle, 0.0, 0.0, 1.0);
        GL11.glTranslated(- xMid, - yMid, 0.0);
        boolean hoverArrow = mouseX >= this.x + this.width - 15 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 7 && mouseY <= this.y + 17;
        boolean bl2 = hoverArrow;
        if (hoverArrow) {
            RenderUtil.drawImage(new ResourceLocation("Ave/icons/arrow-down.png"), this.x + this.width - 9, this.y + 7, 6, 6, new Color(0.7058824f, 0.7058824f, 0.7058824f));
        } else {
            RenderUtil.drawImage(new ResourceLocation("Ave/icons/arrow-down.png"), this.x + this.width - 9, this.y + 7, 6, 6);
        }
        GL11.glPopMatrix();
        RenderUtil.drawImage(new ResourceLocation("Ave/icons/panel.png"), this.x + 5, this.y + 6, 8, 8, new Color(255, 255, 255));
        this.upateUIMenuMods();
        this.width = font.getStringWidth(name) + 50;
        this.uiMenuMods.draw(mouseX, mouseY);
        this.move(mouseX, mouseY);
    }

    private void move(int mouseX, int mouseY) {
        boolean hoverArrow = mouseX >= this.x + this.width - 15 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 7 && mouseY <= this.y + 17;
        boolean bl2 = hoverArrow;
        if (!hoverArrow && this.isHovering(mouseX, mouseY) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
        }
        if (hoverArrow && this.handler.canExcecute()) {
            this.uiMenuMods.open = !this.uiMenuMods.open;
            boolean bl3 = this.uiMenuMods.open;
        }
        if (!Mouse.isButtonDown(0)) {
            this.drag = false;
        }
        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.tab_height) {
            return true;
        }
        return false;
    }

    private void upateUIMenuMods() {
        this.uiMenuMods.x = this.x;
        this.uiMenuMods.y = this.y;
        this.uiMenuMods.tab_height = this.tab_height;
        this.uiMenuMods.width = this.width;
    }

    public void mouseClick(int mouseX, int mouseY) {
        this.uiMenuMods.mouseClick(mouseX, mouseY);
    }

    public void mouseRelease(int mouseX, int mouseY) {
        this.uiMenuMods.mouseRelease(mouseX, mouseY);
    }
}

