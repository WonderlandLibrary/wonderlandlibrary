/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.skeet;

import ClickGUIs.skeet.CheckBox;
import ClickGUIs.skeet.Combo;
import ClickGUIs.skeet.Comp;
import ClickGUIs.skeet.Slider;
import de.Hero.settings.Setting;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class Skeet
extends GuiScreen {
    public double dragY;
    private Module selectedModule;
    public ArrayList<Comp> comps = new ArrayList();
    public double posX = this.getScaledRes().getScaledWidth() / 2 - 150;
    public double dragX;
    public double posY = this.getScaledRes().getScaledHeight() / 2 - 100;
    public Category selectedCategory;
    public boolean dragging = false;
    public double height;
    public double width = this.posX + 300.0;
    public float y;
    public int modeIndex;

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        super.mouseReleased(n, n2, n3);
        this.dragging = false;
        for (Comp comp : this.comps) {
            comp.mouseReleased(n, n2, n3);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        this.dragging = false;
    }

    public boolean isInside(int n, int n2, double d, double d2, double d3, double d4) {
        return (double)n > d && (double)n < d3 && (double)n2 > d2 && (double)n2 < d4;
    }

    public ScaledResolution getScaledRes() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        super.keyTyped(c, n);
        for (Comp comp : this.comps) {
            comp.keyTyped(c, n);
        }
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        if (this.isInside(n, n2, this.posX, this.posY - 10.0, this.width, this.posY) && n3 == 0) {
            this.dragging = true;
            this.dragX = (double)n - this.posX;
            this.dragY = (double)n2 - this.posY;
        }
        int n4 = 0;
        Category[] object3 = Category.values();
        int n5 = object3.length;
        int n6 = 0;
        while (n6 < n5) {
            Category category = object3[n6];
            if (this.isInside(n, n2, this.posX + 80.0, this.posY + 1.0 + (double)n4, this.posX + 60.0, this.posY + 15.0 + (double)n4) && n3 == 0) {
                this.selectedCategory = category;
            }
            n4 += 15;
            ++n6;
        }
        n4 = 0;
        for (Module module : Exodus.INSTANCE.moduleManager.getModulesByCategory(this.selectedCategory)) {
            if (this.isInside(n, n2, this.posX + 65.0, this.posY + 1.0 + (double)n4, this.posX + 125.0 + (double)n4, this.posY + 15.0 + (double)n4)) {
                if (n3 == 0) {
                    module.toggle();
                }
                if (n3 == 1) {
                    n5 = 3;
                    this.comps.clear();
                    if (Exodus.INSTANCE.settingsManager.getSettingsByMod(module) != null) {
                        for (Setting setting : Exodus.INSTANCE.settingsManager.getSettingsByMod(module)) {
                            this.selectedModule = module;
                            if (setting.isCombo()) {
                                Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
                                this.comps.add(new Combo(225.0, n5, this, this.selectedModule, setting));
                                n5 += 15;
                            }
                            if (setting.isCheck()) {
                                Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
                                this.comps.add(new CheckBox(225.0, n5, this, this.selectedModule, setting));
                                n5 += 15;
                            }
                            if (!setting.isSlider()) continue;
                            Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
                            this.comps.add(new Slider(225.0, n5, this, this.selectedModule, setting));
                            n5 += 25;
                        }
                    }
                }
            }
            n4 += 15;
        }
        for (Comp comp : this.comps) {
            comp.mouseClicked(n, n2, n3);
        }
    }

    public Skeet() {
        this.height = this.height + 200.0 + 200.0;
        this.selectedCategory = Category.COMBAT;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        for (Module module : Exodus.INSTANCE.moduleManager.getModules()) {
            if (!this.dragging) continue;
            this.posX = (double)n - this.dragX;
            this.posY = (double)n2 - this.dragY;
        }
        this.width = this.posX + 300.0;
        this.height = this.posY + 200.0;
        Gui.drawRect(this.posX - 100.0, this.posY - 9.5, this.width, this.posY, ColorUtils.getRainbowWave(3.0f, 0.2f, 1.0f, 200L));
        Gui.drawRect(this.posX - 100.0, this.posY, this.width, this.height + 145.0, new Color(0, 0, 0).getRGB());
        int n3 = 0;
        Category[] categoryArray = Category.values();
        int n4 = categoryArray.length;
        int n5 = 0;
        while (n5 < n4) {
            Category category = categoryArray[n5];
            if (!category.name().equalsIgnoreCase("Hidden")) {
                Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
                this.fontRendererObj.drawString(category.name(), (int)this.posX - 90, (int)this.posY + n3 + 8, new Color(170, 170, 170).getRGB());
                n3 += 15;
            }
            ++n5;
        }
        n3 = 0;
        for (Module module : Exodus.INSTANCE.moduleManager.getModulesByCategory(this.selectedCategory)) {
            Gui.drawRect(this.posX - 35.0, this.posY + 1.0 + (double)n3 + (double)Module.settings, this.posX + 35.0 + (double)Module.settings, this.posY + 15.0 + (double)n3, new Color(28, 28, 28).getRGB());
            if (module.isToggled()) {
                this.fontRendererObj.drawString(module.getName(), (int)this.posX - 30, (int)(this.posY - 5.0) + n3, new Color(255, 255, 255).getRGB());
            } else {
                this.fontRendererObj.drawString(module.getName(), (int)this.posX - 30, (int)(this.posY - 5.0) + n3, new Color(170, 170, 170).getRGB());
            }
            n3 += 15;
        }
        for (Comp comp : this.comps) {
            comp.drawScreen(n, n2);
        }
    }
}

