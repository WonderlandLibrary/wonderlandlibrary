package dev.tenacity.ui.clickgui.dropdown.component;

import dev.tenacity.Tenacity;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.ui.Drag;
import dev.tenacity.ui.IScreen;
import dev.tenacity.util.misc.HoverUtil;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.RenderUtil;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class CategoryPanelComponent implements IScreen {

    private final ModuleCategory category;

    public static float offsetX, moduleOffset;

    private final List<ModulePanelComponent> modulePanelComponentList = new ArrayList<>();

    private final Drag drag;

    public CategoryPanelComponent(final ModuleCategory category) {
        this.category = category;
        drag = new Drag(category.getPosX(), category.getPosY());
    }

    @Override
    public void initGUI() {
        if(modulePanelComponentList.isEmpty())
            Tenacity.getInstance().getModuleRepository().getModulesInCategory(category).forEach(moduleInCategory ->
                    modulePanelComponentList.add(new ModulePanelComponent(Tenacity.getInstance().getModuleRepository().getModule(moduleInCategory))));

        modulePanelComponentList.forEach(ModulePanelComponent::initGUI);
    }

    final int width = 100;
    final int height = 18;

    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        drag.onDraw(mouseX, mouseY);
        final CustomFont font = FontUtil.getFont("OpenSans-SemiBold", 20);

        RenderUtil.drawRect(drag.getX(), drag.getY(), width, height, ColorUtil.getSurfaceColor().getRGB());
        font.drawString(category.getName(), drag.getX() + 3, drag.getY() + ((height / 2f)) - 5, -1);

        moduleOffset = 0;
        modulePanelComponentList.forEach(modulePanelComponent -> {
            modulePanelComponent.setPosX(drag.getX());
            modulePanelComponent.setPosY(drag.getY() + height + (moduleOffset * 20));

            modulePanelComponent.drawScreen(mouseX, mouseY);

            moduleOffset += 1 + (modulePanelComponent.getSettingOffset() * (15 / 20f));
        });
    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        modulePanelComponentList.forEach(modulePanelComponent -> modulePanelComponent.keyTyped(typedChar, keyCode));
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        drag.onClick(mouseX, mouseY, mouseButton, HoverUtil.isHovering(drag.getX(), drag.getY(), width, 20, mouseX, mouseY));
        modulePanelComponentList.forEach(modulePanelComponent -> modulePanelComponent.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        drag.onRelease(state);
        modulePanelComponentList.forEach(modulePanelComponent -> modulePanelComponent.mouseReleased(mouseX, mouseY, state));
    }
}
