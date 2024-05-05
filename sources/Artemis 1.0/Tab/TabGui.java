package artemis.Tab;
import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.util.GLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;

public class TabGui extends GuiScreen{
	 
	
	public Minecraft mc = Minecraft.getMinecraft();

	
	Category Category;
	
	module module;
	
    private ArrayList<String> categorys = new ArrayList<String>();

    private int selectedMods, selectedTab;

    private int tab;

    private int tabHeight = 20;

    public TabGui() {

        Category[] array;
        int j = (array = Category.values()).length;

        for (int i = 0; i < j; i++) {
            Category Category = array[i];

            if (Category.name().equalsIgnoreCase("gui")) {
                continue;
            }

            this.categorys.add(Category.toString().substring(0, 1) + Category.toString().substring(1, Category.toString().length()).toLowerCase());
        }
    }
    public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}

    public void render() {
    	
    	
    	
    	FontRenderer fr = mc.fontRendererObj;
        int count = 0;
        
        for (Category Category : Category.values()) {
            if (!Category.name().equalsIgnoreCase("gui")) {
                int y = tabHeight + (count * 15);

                GLUtils.glColor(new Color(47, 47, 47, 230).hashCode());
                GLUtils.drawRect(0, y, 70, y + 15);
                count++;
            }
        }

       int count1 = 0;
        for (Category Category : Category.values()) {
            if (Category != Category.GUI) {
            	int y = tabHeight + (count1 * 15);
            	String name = Category.toString();
                if (Category.name().equalsIgnoreCase(categorys.get(this.selectedTab))) {
                	 
                    //Selected
                   //fr.drawString("> " + Category.name(), 6, tabHeight + count1 * 15 + 6, -1);
                	this.drawRect(0, y, 70, y + 15, rainbow(300));
                   fr.drawString("  " + name, 6,  tabHeight + count1 * 15 + 6, -1);
                } else {
                    //Not Selected
                   // fr.drawString(Category.name(), 6, tabHeight + count1 * 15 + 6, -1);
                	
                    fr.drawString(name, 6,  tabHeight + count1 * 15 + 6, -1);
                	
                }
                count1++;
            }
        }
        if (tab == 1 || tab == 2) {
            int modCount = 0;

            for (module module : getModsForCategorys()) {
                int color;
                if (module.isToggled()) {
                    color = rainbow(300);
                } else {
                    color = -1;
                }

                int y = tabHeight + (modCount * 15);
                GLUtils.glColor(new Color(47, 47, 47, 230).hashCode());
                //module Rect
                GLUtils.drawRect(73, y, 89 + this.getLongestModuleWidth(), y + 15);
                fr.drawString(!module.getName().equalsIgnoreCase(this.getModsForCategorys().get(this.selectedMods).getName()) ? module.getName() : "> " + module.getName(), 80 , y + 6, color);
                modCount++;
            }
        }
        this.drawVerticalLine(0, 19, 110, rainbow(300));
        this.drawVerticalLine(70, 19, 110, rainbow(300));
        this.drawHorizontalLine(0, 70, 110, rainbow(300));
        this.drawHorizontalLine(0, 70, 19, rainbow(300));

    }

    public void onKeyPressed(int key) {

        if (key == Keyboard.KEY_UP) {
            this.up();
        }

        if (key == Keyboard.KEY_DOWN) {
            this.down();
        }
        if (key == Keyboard.KEY_LEFT) {
            this.left();
        }
        if (key == Keyboard.KEY_RIGHT) {
            this.right();
        }
        if (key == Keyboard.KEY_RETURN) {
            this.enter();
        }

    }

    private void enter() {

        if (tab == 1) {
            getModsForCategorys().get(this.selectedMods).toggle();
        }
    }

    private void right() {

        if (tab == 1) {
            this.enter();
        } else {
            if (tab == 0) {
                tab = 1;
                this.selectedMods = 0;
            }
        }
    }

    private void left() {

        if (tab == 1) {
            tab = 0;
        }
    }

    private void down() {

        if (tab == 0) {
            if (this.selectedTab >= this.categorys.size() - 1) {
                this.selectedTab = -1;
            }
            this.selectedTab += 1;
        } else if (tab == 1) {
            if (this.selectedMods >= getModsForCategorys().size() - 1) {
                this.selectedMods = -1;
            }

            this.selectedMods += 1;
        }
    }

    private void up() {

        if (tab == 0) {
            if (this.selectedTab <= 0) {
                this.selectedTab = this.categorys.size();
            }
            this.selectedTab -= 1;
        } else if (tab == 1) {
            if (this.selectedTab <= 0) {
                this.selectedMods = getModsForCategorys().size();
            }

            this.selectedMods -= 1;
        }
    }

    private ArrayList<module> getModsForCategorys() {

        ArrayList<module> modules = new ArrayList<module>();

        for (module module : Artemis.getModules()) {
            if (module.getCategory() == Category.valueOf(this.categorys.get(this.selectedTab).toUpperCase())) {
                modules.add(module);
            }
        }

        return modules;
    }

    private int getLongestModuleWidth() {
    	FontRenderer fr = mc.fontRendererObj;
        int longest = 0;
        for (module module : getModsForCategorys()) {
            if (fr.getStringWidth(module.getName()) + 5 > longest) {
                longest = fr.getStringWidth(module.getName()) + 5;
            }
        }

        return longest;
    }
}