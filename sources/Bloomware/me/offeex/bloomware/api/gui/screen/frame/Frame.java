package me.offeex.bloomware.api.gui.screen.frame;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.gui.screen.ClickGUI;
import me.offeex.bloomware.api.gui.screen.frame.component.Component;
import me.offeex.bloomware.api.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.api.manager.managers.FontManagerr;
import me.offeex.bloomware.api.manager.managers.ModuleManager;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.client.Colors;
import me.offeex.bloomware.client.module.client.Gui;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Locale;



public class Frame {
    private final Colors colors;
    public Module.Category category;
    private final ArrayList<Component> buttons;
    private boolean open;
    private int x, y, height;
    private final int width;
    private final int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;

    public Frame(Module.Category category) {
        colors = Colors.INSTANCE;
        this.category = category;
        this.buttons = new ArrayList<>();
        this.width = 124;
        this.x = 5;
        this.y = 5;
        this.barHeight = 16;
        this.dragX = 0;
        this.open = true;
        this.isDragging = false;
        int componentY = this.barHeight;
        for (Module m : ModuleManager.INSTANCE.getModulesByCategory(category)) {
            ModuleButton moduleButton = new ModuleButton(m, this, componentY);
            buttons.add(moduleButton);
            componentY += Component.COMPONENT_HEIGHT;
        }
        update();
    }

    public void renderFrame(MatrixStack matrices) {
        if (isDragging)
            DrawableHelper.fill(matrices, x, y, x + width, y + barHeight, colors.getDragging().getColor().getArgb());
        else
            DrawableHelper.fill(matrices, x, y, x + width, y + barHeight, colors.getTopBar().getColor().getArgb());

        float offsetWidth = 0;
        switch (Gui.INSTANCE.getCategoryOffset().getSelected()) {
            case "Center" -> offsetWidth = width / 2f - FontManagerr.INSTANCE.width(category.getTitle()) / 2f;
            case "Left" -> offsetWidth = 2;
            case "Right" -> offsetWidth = width - FontManagerr.INSTANCE.width(category.getTitle()) - 2;
        }
        FontManagerr.INSTANCE.drawVCenteredString(matrices, category.getTitle(), x + offsetWidth, y + barHeight / 2, colors.getText().getColor());

        if (open && !buttons.isEmpty()) {
            buttons.forEach(c -> {
                if (c instanceof ModuleButton modButton && modButton.module.getName().toLowerCase(Locale.ROOT).contains(Bloomware.INSTANCE.getGui().searchField.getText().toLowerCase(Locale.ROOT))) c.render(matrices);
            });
            DrawableHelper.fill(matrices, this.x, y + 16, this.x + 1, y + height,  colors.getBorders().getColor().getArgb());
            DrawableHelper.fill(matrices, this.x, y + height, this.x + width, y + height + 1,  colors.getBorders().getColor().getArgb());
            DrawableHelper.fill(matrices, this.x, y + barHeight - 1, this.x + width, y + barHeight,  colors.getBorders().getColor().getArgb());
        }
    }

    public void setDrag(final boolean drag) {
        if (drag && ClickGUI.dragging == null) {
            ClickGUI.dragging = this;
            this.isDragging = true;
        }
        else {
            if (ClickGUI.dragging == this)
                ClickGUI.dragging = null;
            this.isDragging = false;
        }
    }

    public void update() {
        int offY = this.barHeight;
        for (Component comp : this.buttons) {
            comp.setOffsetY(offY);
            offY += comp.getHeight();
        }
        this.height = offY;
    }

    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(Math.max(0, Math.min(Bloomware.INSTANCE.getMc().getWindow().getScaledWidth() - this.width, mouseX - dragX)));
            this.setY(Math.max(0, Math.min(Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() - this.height, mouseY - dragY)));
        }
    }

    public boolean isHovered(final double x, final double y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

    public ArrayList<Component> getButtons() {
        return buttons;
    }

    public boolean isOpen() {
        return open;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
