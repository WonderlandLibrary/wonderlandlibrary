package tech.drainwalk.gui.menu.window.windows.menuwindow;

import lombok.Getter;
import lombok.Setter;
import tech.drainwalk.gui.menu.MenuMain;
import tech.drainwalk.gui.menu.window.Window;
import tech.drainwalk.gui.menu.window.windows.menuwindow.components.Component;
import tech.drainwalk.gui.menu.window.windows.menuwindow.components.module.ModuleComponent;
import tech.drainwalk.gui.menu.window.windows.menuwindow.components.panel.CategoryComponent;
import tech.drainwalk.gui.menu.window.windows.menuwindow.components.panel.PanelComponent;
import tech.drainwalk.gui.menu.window.windows.menuwindow.components.type.ModuleTypeComponent;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;

import java.util.ArrayList;
import java.util.Collection;

public class MenuWindow extends Window {
    private final Collection<Component> menuComponents = new ArrayList<>();

    @Getter
    @Setter
    private Module selectedModule;
    @Getter
    @Setter
    private Category selectedCategory = Category.COMBAT;


    public MenuWindow() {
        super(true);
        windowActive = true;
        this.windowX = 20;
        this.windowY = 20;
        this.windowWidth = 455.5f;
        this.windowHeight = 297.5f;
        menuComponents.add(new PanelComponent(windowX, windowY, windowWidth, windowHeight,this));
        menuComponents.add(new CategoryComponent(windowX, windowY,this));
        menuComponents.add(new ModuleTypeComponent(windowX, windowY,this));
        menuComponents.add(new ModuleComponent(windowX, windowY,this));

    }

    @Override
    public void renderWindow(int mouseX, int mouseY, float partialTicks) {
        for (Component component : menuComponents) {
            component.setX(windowX);
            component.setY(windowY);
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
       if(!MenuMain.settingWindow.isInWindowBound() && !MenuMain.colorPickerWindow.isInWindowBound()) {
           for (Component component : menuComponents) {
               component.mouseClicked(mouseX, mouseY, mouseButton);
           }
       }
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        super.updateScreen(mouseX,mouseY);
        for (Component component : menuComponents) {
            component.updateScreen(mouseX, mouseY);
        }
        setCanDragging(Component.canDragging);
    }
}
