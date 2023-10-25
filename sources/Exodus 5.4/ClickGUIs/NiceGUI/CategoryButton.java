/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.NiceGUI;

import ClickGUIs.NiceGUI.NiceGui;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class CategoryButton {
    int width;
    NiceGui parent;
    int height;
    int mouseTicks = 0;
    Category category;
    int y;
    Minecraft mc = Minecraft.getMinecraft();
    int x;

    public void setY(int n) {
        this.y = n;
    }

    public void setWidth(int n) {
        this.width = n;
    }

    public boolean isWithinButton(int n, int n2) {
        return n > this.x && n < this.x + this.width && n2 > this.y && n2 < this.y + this.height;
    }

    public void mouseClicked(int n, int n2) {
        if (this.isWithinButton(n, n2)) {
            this.parent.currentCategory = this.category;
        }
    }

    public int getX() {
        return this.x;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int n) {
        this.x = n;
    }

    public Category getCategory() {
        return this.category;
    }

    public CategoryButton(int n, int n2, Category category, NiceGui niceGui) {
        this.x = n;
        this.y = n2;
        this.category = category;
        this.parent = niceGui;
        this.width = 45;
        this.height = 45;
    }

    public void setHeight(int n) {
        this.height = n;
    }

    public void draw(int n, int n2) {
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        if (this.isWithinButton(n, n2)) {
            if (this.mouseTicks < 3) {
                ++this.mouseTicks;
            }
        } else if (this.mouseTicks > 0) {
            --this.mouseTicks;
        }
        FontUtil.normal.drawString(this.category.name, this.x, this.y, -1);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

