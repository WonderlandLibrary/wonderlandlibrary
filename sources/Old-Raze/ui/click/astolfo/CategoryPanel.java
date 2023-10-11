package markgg.ui.click.astolfo;

import markgg.RazeClient;
import markgg.modules.Module;
import markgg.modules.Module.Category;
import markgg.ui.click.astolfo.buttons.Button;
import markgg.ui.click.astolfo.buttons.ModuleButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

public class CategoryPanel extends Button {

    public ArrayList<ModuleButton> moduleButtons = new ArrayList<>();

    public Color color;
    public Module.Category category;

    public boolean dragged;
    public int mouseX2, mouseY2;
    public int count;

    public CategoryPanel(float x, float y, float width, float height, Module.Category category, Color color) {
        super(x, y, width, height);
        this.category = category;
        this.color = Module.getCategoryColor(category);

        int count = 0;

        final float startModuleY = y + height;
        for(Module module : RazeClient.getModuleManager().getModules().values()){
            if(module.getCategory() == category) {
                moduleButtons.add(new ModuleButton(x, startModuleY + height * count, width, height, module, color));
                count++;
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(dragged){
            x = mouseX2 + mouseX;
            y = mouseY2 + mouseY;
        }

        Gui.drawRect(x, y, x + width, y + height, 0xff181A17);

        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(category.name.toLowerCase(), x + 4.5f, (float) (y + height / 2 - 2.5), 0xffffffff);

        count = 0;

        if(category.expanded) {
            final float start = y + height;

            for(ModuleButton moduleButton : moduleButtons) {
                moduleButton.x = x;
                moduleButton.y = start + count;
                moduleButton.draw(mouseX, mouseY);
                count += moduleButton.finalHeight;
            }
        }

        Gui.drawRect(x, (y + count) + height, x + width, (y + count) + height + 2, 0xff181A17);
        drawAstolfoBorderedRect(x, y, x + width, (y + count) + height + 2, 1.2f, color.getRGB());
    }

    @Override
    public void action(int x, int y, boolean click, int button) {
        if(isHovered(x, y)){
            if(click) {
                if(button == 0){
                    dragged = true;
                    mouseY2 = (int) (this.y - y);
                    mouseX2 = (int) (this.x - x);
                }else
                    category.expanded = !category.expanded;
            }
        }
        if(!click)
            dragged = false;
    }

    @Override
    public void key(char typedChar, int key) { }

    public static void drawAstolfoBorderedRect(float left, float top, float right, float bottom, float thickness, int color) {
        Gui.drawRect(left - thickness, top, left, bottom + 1.f, color);
        Gui.drawRect(right, top, right + thickness, bottom + 1.f, color);
        Gui.drawRect(left, top + thickness, right, top, color);
        Gui.drawRect(left, bottom, right, bottom + thickness, color);
    }
}
