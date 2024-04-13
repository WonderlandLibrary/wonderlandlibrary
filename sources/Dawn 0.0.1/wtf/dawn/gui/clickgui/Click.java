package wtf.dawn.gui.clickgui;

import net.minecraft.client.gui.GuiScreen;
import wtf.dawn.gui.clickgui.comp.FrameComponent;
import wtf.dawn.module.Category;
import wtf.dawn.module.Module;
import wtf.dawn.ui.components.BasicComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Click extends GuiScreen {

    public ArrayList<BasicComponent> frames = new ArrayList<>();
    public CopyOnWriteArrayList<Module> modules;
    public Click(CopyOnWriteArrayList<Module> modules) {
        this.modules = modules;
    }

    @Override
    public void initGui() {
        super.initGui();
        AtomicInteger offset = new AtomicInteger(5);

        Arrays.stream(Category.values()).sorted().forEach(category -> {
            frames.add(new FrameComponent(offset.get(), 5, 70, 20, false, 0, this, modules, category));
            offset.addAndGet(75);
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        frames.forEach(basicComponent -> { basicComponent.renderComponent(mouseX, mouseY); });
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        frames.forEach(basicComponent -> { basicComponent.onClick(mouseX, mouseY, mouseButton); });
    }
}
