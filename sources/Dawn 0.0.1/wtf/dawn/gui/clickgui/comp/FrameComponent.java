package wtf.dawn.gui.clickgui.comp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import wtf.dawn.gui.clickgui.Click;
import wtf.dawn.gui.clickgui.comp.impl.ModuleComponent;
import wtf.dawn.module.Category;
import wtf.dawn.module.Module;
import wtf.dawn.ui.Screen;
import wtf.dawn.ui.annotations.Component;
import wtf.dawn.ui.components.BasicComponent;
import wtf.dawn.ui.util.DrawUtility;
import wtf.dawn.utils.font.FontUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component(name = "Frame", componentId = 1)
public class FrameComponent extends BasicComponent {

    Click parent;
    CopyOnWriteArrayList<Module> modulesArrayList;
    ArrayList<ModuleComponent> moduleComponentArrayList = new ArrayList<>();
    Category frameCurrentCategory;

    public <T> FrameComponent(int x, int y, int w, int h, boolean outline, int typeId, Click parentIn, CopyOnWriteArrayList<Module> modulesIn, Category categoryIn) {
        super(x, y, w, h, outline, typeId);

        this.modulesArrayList = modulesIn;
        this.parent = parentIn;
        this.frameCurrentCategory = categoryIn;
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        super.renderComponent(mouseX, mouseY);
        ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());

        moduleComponentArrayList.clear();

        AtomicInteger widthPlus = new AtomicInteger();
        this.modulesArrayList.forEach(module -> {
            if(module.getCategory() == frameCurrentCategory) {
                moduleComponentArrayList.add(new ModuleComponent(x, y + 20 + widthPlus.get(), 70, 16, false, 01, module));
                widthPlus.addAndGet(20);
            }
        });



        DrawUtility.drawRoundedRect(x, y, x + width, y + height + widthPlus.get(), 4, new Color(108, 145, 191).getRGB());
        DrawUtility.drawRoundedOutline(x, y, x + width, y + height + widthPlus.get(), 4, 2, -1);

        moduleComponentArrayList.forEach(moduleComponent -> {
            moduleComponent.renderComponent(mouseX, mouseY);
        });

        Screen.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, frameCurrentCategory.toString().toLowerCase(), (int) (x + ((float) width / 2)), (int) (y + ((float) height / 2 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2)), Color.WHITE.getRGB());
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseButton) {
        super.onClick(mouseX, mouseY, mouseButton);
        moduleComponentArrayList.forEach(moduleComponent -> moduleComponent.onClick(mouseX, mouseY, mouseButton));
    }
}
