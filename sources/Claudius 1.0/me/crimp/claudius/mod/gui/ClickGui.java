package me.crimp.claudius.mod.gui;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.Feature;
import me.crimp.claudius.mod.gui.components.Component;
import me.crimp.claudius.mod.gui.components.items.Item;
import me.crimp.claudius.mod.gui.components.items.button.ModuleButton;
import me.crimp.claudius.mod.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ClickGui extends GuiScreen {
    private static final ClickGui INSTANCE = new ClickGui();

    private final ArrayList<Component> components = new ArrayList<>();

    public ClickGui() {
        this.load();
    }

    public static ClickGui getInstance() {
        return INSTANCE;
    }

    public static ClickGui getClickGui() {
        return ClickGui.getInstance();
    }

    private void load() {
        int x = -84;
        for (final Module.Category category : claudius.moduleManager.getCategories()) this.components.add(new Component(category.getName(), x += 100, 23, true) {
            @Override
            public void setupItems() {
                claudius.moduleManager.getModulesByCategory(category).forEach(module -> {
                    if (!module.hidden) this.addButton(new ModuleButton(module));
                });
            }
        });
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : this.components) for (Item item : component.getItems()) {
            if (!(item instanceof ModuleButton)) continue;
            ModuleButton button = (ModuleButton) item;
            Module mod = button.getModule();
            if (module == null || !module.equals(mod)) continue;
            button.initSettings();
        }
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) this.components.forEach(component -> component.setY(component.getY() - 10));
        else if (dWheel > 0) this.components.forEach(component -> component.setY(component.getY() + 10));
    }

    public int getTextOffset() {
        return -6;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
}

