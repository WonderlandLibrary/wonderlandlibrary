/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.ui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import tk.rektsky.module.Category;
import tk.rektsky.ui.clickgui.AlloyColors;
import tk.rektsky.ui.clickgui.components.impl.CategoryComponent;

public class AlloyClickGUI
extends GuiScreen {
    private static final Color TRANSPARENT = new Color(5853884, true);
    private static final Color BG = new Color(-1084665156, true);
    static ArrayList<CategoryComponent> components = new ArrayList();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, TRANSPARENT.getRGB(), BG.getRGB());
        for (CategoryComponent component : components) {
            component.draw(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (CategoryComponent component : components) {
            component.onClick(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    static {
        CategoryComponent COMBAT = new CategoryComponent(AlloyColors.COMBAT, Category.COMBAT, 4, 4);
        components.add(COMBAT);
        CategoryComponent MOVEMENT = new CategoryComponent(AlloyColors.MOVEMENT, Category.MOVEMENT, 4 + COMBAT.getWidth() + 30, 4);
        components.add(MOVEMENT);
        CategoryComponent RENDER = new CategoryComponent(AlloyColors.RENDER, Category.RENDER, 4 + COMBAT.getWidth() + MOVEMENT.getWidth() + 60, 4);
        components.add(RENDER);
        CategoryComponent PLAYER = new CategoryComponent(AlloyColors.PLAYER, Category.PLAYER, 4 + COMBAT.getWidth() + MOVEMENT.getWidth() + RENDER.getWidth() + 90, 4);
        components.add(PLAYER);
        CategoryComponent EXPLOIT = new CategoryComponent(AlloyColors.EXPLOIT, Category.EXPLOIT, 4 + COMBAT.getWidth() + MOVEMENT.getWidth() + RENDER.getWidth() + PLAYER.getWidth() + 120, 4);
        components.add(EXPLOIT);
        CategoryComponent REKTSKY = new CategoryComponent(AlloyColors.REKTSKY, Category.REKTSKY, 4 + COMBAT.getWidth() + MOVEMENT.getWidth() + RENDER.getWidth() + PLAYER.getWidth() + EXPLOIT.getWidth() + 150, 4);
        components.add(REKTSKY);
        CategoryComponent WORLD = new CategoryComponent(AlloyColors.WORLD, Category.WORLD, 4 + COMBAT.getWidth() + MOVEMENT.getWidth() + RENDER.getWidth() + PLAYER.getWidth() + EXPLOIT.getWidth() + REKTSKY.getWidth() + 180, 4);
        components.add(WORLD);
    }
}

