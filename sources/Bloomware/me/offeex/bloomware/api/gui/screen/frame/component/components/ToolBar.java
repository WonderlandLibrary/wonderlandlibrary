package me.offeex.bloomware.api.gui.screen.frame.component.components;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.gui.screen.frame.component.Component;
import me.offeex.bloomware.api.manager.managers.FontManagerr;
import me.offeex.bloomware.api.structure.ColorMutable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolBar {
    private final ArrayList<Component> components;
    private int x, y, width, height;

    public ToolBar(int x, int y, int width, int height, Component... components) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.components = new ArrayList<>(Arrays.asList(components));
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void updateProperties(int newX, int newY, int newWidth, int newHeight) {
        x = newX;
        y = newY;
        width = newWidth;
        height = newHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void render(MatrixStack stack) {
        DrawableHelper.fill(stack, x, y, x + width, y + height, new ColorMutable(0, 0, 0, 100).getArgb());
        FontManagerr.INSTANCE.drawString(stack, Bloomware.NAME, x + 2, y + 2, ColorMutable.Companion.getWHITE(), null);
        FontManagerr.INSTANCE.drawString(stack, "Version: " + Bloomware.INSTANCE.getVERSION(), x + 2, y + 12, ColorMutable.Companion.getWHITE(), null);
    }

    public List<Component> getComponents() {
        return components;
    }
}
