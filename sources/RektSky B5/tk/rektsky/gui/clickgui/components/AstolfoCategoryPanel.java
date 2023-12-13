/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui.clickgui.components;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.gui.clickgui.components.AstolfoButton;
import tk.rektsky.gui.clickgui.components.AstolfoModuleButton;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;

public class AstolfoCategoryPanel
extends AstolfoButton {
    public Category category;
    public Color color;
    public boolean dragged;
    public int mouseX2;
    public int mouseY2;
    public float count;
    public ArrayList<AstolfoModuleButton> moduleButtons = new ArrayList();

    public AstolfoCategoryPanel(float x2, float y2, float width, float height, Category cat, Color color) {
        super(x2, y2, width, height);
        this.category = cat;
        this.color = color;
        int count = 0;
        float startY = y2 + height;
        for (Module mod : ModulesManager.getModules()) {
            if (mod.category != this.category) continue;
            this.moduleButtons.add(new AstolfoModuleButton(x2, startY + height * (float)count, width, height, mod, color));
            ++count;
        }
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        if (this.dragged) {
            this.x = this.mouseX2 + mouseX;
            this.y = this.mouseY2 + mouseY;
        }
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), -14408909);
        AstolfoClickGui.categoryNameFont.drawString(this.category.getName(), this.x + 4.0f, this.y + this.height / 2.0f - 4.0f, this.color.getRGB());
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("rektsky/clickgui/" + this.category.name().toLowerCase() + ".png"));
        int offset = 4;
        if (this.category == Category.PLAYER) {
            offset = 2;
        }
        GlStateManager.color((float)this.color.getRed() / 255.0f, (float)this.color.getGreen() / 255.0f, (float)this.color.getBlue() / 255.0f);
        Gui.drawModalRectWithCustomSizedTexture((int)(this.x + this.width - 16.0f), (int)(this.y + (float)offset), 0.0f, 0.0f, 12, 12, 12.0f, 12.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        this.count = 0.0f;
        if (this.category.isEnabled()) {
            float startY = this.y + this.height;
            for (AstolfoModuleButton modulePanel : this.moduleButtons) {
                modulePanel.x = this.x;
                modulePanel.y = startY + this.count;
                modulePanel.drawPanel(mouseX, mouseY);
                this.count += modulePanel.finalHeight;
            }
        }
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if (this.isHovered(mouseX, mouseY) && click) {
            if (button == 0) {
                this.dragged = true;
                this.mouseX2 = (int)(this.x - (float)mouseX);
                this.mouseY2 = (int)(this.y - (float)mouseY);
            } else {
                this.category.setEnabled(!this.category.isEnabled());
            }
        }
        if (!click) {
            this.dragged = false;
        }
    }
}

