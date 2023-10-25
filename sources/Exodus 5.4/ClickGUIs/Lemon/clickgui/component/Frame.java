/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ClickGUIs.Lemon.clickgui.component;

import ClickGUIs.Lemon.clickgui.component.Component;
import ClickGUIs.Lemon.clickgui.component.components.Button;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.ui.Draw;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Frame {
    public Category category;
    private int x;
    private boolean open;
    public int dragX;
    int tY;
    public int dragY;
    private int y;
    private int barHeight;
    private boolean isDragging;
    private int width;
    public ArrayList<Component> components;

    public Frame(Category category) {
        this.tY = this.barHeight;
        this.components = new ArrayList();
        this.category = category;
        this.width = 88;
        this.x = 5;
        this.y = 5;
        this.barHeight = 13;
        this.dragX = 0;
        this.open = false;
        this.isDragging = false;
        for (Module module : Exodus.INSTANCE.moduleManager.getModulesByCategory(this.category)) {
            Button button = new Button(module, this, this.tY);
            this.components.add(button);
            this.tY += 12;
        }
    }

    public boolean isOpen() {
        return this.open;
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    public void setX(int n) {
        this.x = n;
    }

    public void updatePosition(int n, int n2) {
        if (this.isDragging) {
            this.setX(n - this.dragX);
            this.setY(n2 - this.dragY);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public boolean isWithinHeader(int n, int n2) {
        return n >= this.x && n <= this.x + this.width && n2 >= this.y && n2 <= this.y + this.barHeight;
    }

    public void setY(int n) {
        this.y = n;
    }

    public void renderFrame(FontRenderer fontRenderer) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, CustomIngameGui.getColorInt(this.y / 8));
        Gui.drawRect(this.x, this.y, this.x + this.width - 74, this.y + this.barHeight, -1879048192);
        Draw.fixTextFlickering();
        if (this.category.name().equalsIgnoreCase("Combat")) {
            Draw.drawImg(new ResourceLocation("Terror/combat.png"), this.x - 2, this.y - 2, this.width - 70, 17.0);
        }
        if (this.category.name().equalsIgnoreCase("Movement")) {
            Draw.drawImg(new ResourceLocation("Terror/movement.png"), this.x - 2, this.y - 2, this.width - 70, 17.0);
        }
        if (this.category.name().equalsIgnoreCase("Player")) {
            Draw.drawImg(new ResourceLocation("Terror/player.png"), this.x - 2, this.y - 2, this.width - 70, 17.0);
        }
        if (this.category.name().equalsIgnoreCase("Skyblock")) {
            Draw.drawImg(new ResourceLocation("Terror/skyblock.png"), this.x - 2, this.y - 2, this.width - 70, 17.0);
        }
        if (this.category.name().equalsIgnoreCase("World")) {
            Draw.drawImg(new ResourceLocation("Terror/world.png"), this.x - 2, this.y - 2, this.width - 70, 15.5);
        }
        if (this.category.name().equalsIgnoreCase("Render")) {
            Draw.drawImg(new ResourceLocation("Terror/render.png"), this.x - 2, this.y - 1, this.width - 70, 15.5);
        }
        if (this.category.name().equalsIgnoreCase("Misc")) {
            Draw.drawImg(new ResourceLocation("Terror/misc.png"), this.x - 2, (float)this.y - 0.5f, this.width - 70, 15.5);
        }
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        FontUtil.normal.drawStringWithShadow(this.category.name(), (this.x + 14) * 2 + 5, ((float)this.y + 2.5f) * 2.0f + 5.0f, -1);
        fontRenderer.drawStringWithShadow(this.open ? "-" : "+", (this.x + this.width - 10) * 2 + 5, ((float)this.y + 2.5f) * 2.0f + 5.0f, -1);
        GL11.glPopMatrix();
        if (this.open && !this.components.isEmpty()) {
            for (Component component : this.components) {
                component.renderComponent();
            }
        }
    }

    public void setDrag(boolean bl) {
        this.isDragging = bl;
    }

    public void setOpen(boolean bl) {
        this.open = bl;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

    public void refresh() {
        int n = this.barHeight;
        for (Component component : this.components) {
            component.setOff(n);
            n += component.getHeight();
        }
    }
}

