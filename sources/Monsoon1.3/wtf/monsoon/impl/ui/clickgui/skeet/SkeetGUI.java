package wtf.monsoon.impl.ui.clickgui.skeet;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.Setting;
import wtf.monsoon.api.util.render.ColorUtil;
import wtf.monsoon.api.util.render.RenderUtil;
import wtf.monsoon.impl.ui.clickgui.DraggableComponent;
import wtf.monsoon.impl.ui.clickgui.dropdown1.api.CGUI;
import wtf.monsoon.impl.ui.clickgui.skeet.comp.ModuleButton;
import wtf.monsoon.impl.ui.clickgui.skeet.comp.SkeetCategoryButton;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class SkeetGUI extends GuiScreen {

    public ArrayList<SkeetCategoryButton> categoryButtons = new ArrayList<>();
    public ArrayList<ModuleButton> moduleButtons = new ArrayList<>();
    public DraggableComponent top;
    public int x, y;
    public Category viewing;
    public Module configuring;

    public SkeetGUI(int x, int y) {
        this.x = x;
        this.y = y;

        this.top = new DraggableComponent(x, y, 420, 5, ColorUtil.colorLerpv2(new Color(0,140,255), new Color(0,255,255), 0.5f).getRGB());
    }

    @Override
    public void initGui() {
        setViewing(Category.COMBAT);
        setConfiguring(Monsoon.INSTANCE.manager.getModulesByCategory(viewing).get(0));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(top.getxPosition() - 3, top.getyPosition() - 3, top.getxPosition() + 423, top.getyPosition() + 328, -1);
        Gui.drawRect(top.getxPosition(), top.getyPosition() + 5, top.getxPosition() + 420, top.getyPosition() + 325, new Color(33,33,33).getRGB());
        top.draw(mouseX, mouseY);

        Monsoon.INSTANCE.getFont().drawString(viewing.getName(), top.getxPosition() + 85, top.getyPosition() + 10, -1);
        Gui.drawRect(top.getxPosition() + 84, top.getyPosition() + 10 + Monsoon.INSTANCE.getFont().getHeight() + 1,top.getxPosition() + 85 + Monsoon.INSTANCE.getFont().getStringWidth(viewing.getName()) + 1, top.getyPosition() + 10 + Monsoon.INSTANCE.getFont().getHeight() + 3, -1);

        //side bar
        Gui.drawRect(top.getxPosition(), top.getyPosition() + 5, top.getxPosition() + 80, top.getyPosition() + 325, new Color(23,23,23).getRGB());
        categoryButtons.add(new SkeetCategoryButton(new ResourceLocation("monsoon/skeet/combat.png"), top.getxPosition() + 5, top.getyPosition() + 15,85, 40, 1, Category.COMBAT));
        categoryButtons.add(new SkeetCategoryButton(new ResourceLocation("monsoon/skeet/movement.png"), top.getxPosition() + 15, top.getyPosition() + 65,45, 45, 1, Category.MOVEMENT));
        categoryButtons.add(new SkeetCategoryButton(new ResourceLocation("monsoon/skeet/player.png"), top.getxPosition(), top.getyPosition() + 125,75, 45, 1, Category.PLAYER));
        categoryButtons.add(new SkeetCategoryButton(new ResourceLocation("monsoon/skeet/visual.png"), top.getxPosition(), top.getyPosition() + 185,75, 45, 1, Category.RENDER));
        categoryButtons.add(new SkeetCategoryButton(new ResourceLocation("monsoon/skeet/exploit.png"), top.getxPosition() + 15, top.getyPosition() + 245,45, 45, 1, Category.EXPLOIT));

        int countModule = 2;
        for(Module m  : Monsoon.INSTANCE.manager.getModulesByCategory(viewing)) {
           // Monsoon.getFont().drawString(m.getName(), top.getxPosition() + 85, top.getyPosition() + 15 * countModule, -1);
            moduleButtons.add(new ModuleButton(top.getxPosition() + 85, top.getyPosition() + 15 * countModule, m));
            countModule++;
        }

        Gui.drawRect(top.getxPosition() + 180,top.getyPosition() + 5, top.getxPosition() + 183,top.getyPosition() + 325, -1);

        int countSetting = 1;
        for(Setting set : configuring.settings) {
        	Monsoon.INSTANCE.getFont().drawString(set.name, top.getxPosition() + 195, top.getyPosition() + 15 * countSetting, -1);
            countSetting++;
        }

        new GuiButton(3,top.getxPosition() + 368, top.getyPosition() + 305,50, 18, "ClickGUI").drawButton(mc,mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(SkeetCategoryButton button : categoryButtons) {
            if(RenderUtil.isHovered(top.getxPosition(), top.getyPosition() + 5, top.getxPosition() + 80, top.getyPosition() + 325, mouseX, mouseY))
            button.onClick(mouseX, mouseY);
        }
        if(CGUI.isHover(top.getxPosition() + 368, top.getyPosition() + 305,50, 18, mouseX, mouseY)) {
        	Monsoon.INSTANCE.getClickGUI().display();
        }
        for(ModuleButton b : moduleButtons) {
            if(RenderUtil.isHovered(b.x + b.w + 2, b.y + 1, b.x + b.w + 11, b.y + b.h, mouseX, mouseY) && b.m.category == viewing) {
                //b.onClick(mouseX, mouseY, mouseButton);
                if (mouseButton == 0) b.m.toggle();
                if (mouseButton == 1) Monsoon.INSTANCE.getSkeetGui().setConfiguring(b.m);
            }

        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 3:
            	Monsoon.INSTANCE.getClickGUI().display();
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void setViewing(Category c) {
        this.viewing = c;
    }

    public void setConfiguring(Module configuring) {
        this.configuring = configuring;
    }
}
