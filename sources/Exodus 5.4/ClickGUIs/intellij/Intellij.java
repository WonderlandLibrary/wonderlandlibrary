/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.intellij;

import ClickGUIs.intellij.CheckBox;
import ClickGUIs.intellij.Combo;
import ClickGUIs.intellij.Comp;
import ClickGUIs.intellij.Slider;
import de.Hero.settings.Setting;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class Intellij
extends GuiScreen {
    public int modeIndex;
    public double width;
    public double dragY;
    public double posX;
    public double height;
    private Module selectedModule;
    public double posY;
    public Category selectedCategory;
    public boolean dragging = false;
    public float y;
    public double dragX;
    public ArrayList<Comp> comps = new ArrayList();

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        super.mouseReleased(n, n2, n3);
        this.dragging = false;
        for (Comp comp : this.comps) {
            comp.mouseReleased(n, n2, n3);
        }
    }

    public Intellij() {
        this.posX = this.getScaledRes().getScaledWidth() / 2 - 150;
        this.posY = this.getScaledRes().getScaledHeight() / 2 - 100;
        this.width = this.posX + 300.0;
        this.height = this.height + 200.0 + 200.0;
        this.selectedCategory = Category.COMBAT;
    }

    public boolean isInside(int n, int n2, double d, double d2, double d3, double d4) {
        return (double)n > d && (double)n < d3 && (double)n2 > d2 && (double)n2 < d4;
    }

    public ScaledResolution getScaledRes() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    @Override
    public void initGui() {
        super.initGui();
        this.dragging = false;
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
            if (this.isInside(n, n2, this.posX, this.posY + 1.0 + (double)n4, this.posX + 60.0, this.posY + 25.0 + (double)n4) && n3 == 0) {
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
                            this.comps.add(new Slider(225.0, n5 - 15, this, this.selectedModule, setting));
                            n5 += 12;
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

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        super.keyTyped(c, n);
        for (Comp comp : this.comps) {
            comp.keyTyped(c, n);
        }
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
        Gui.drawRect(this.posX - 50.0, this.posY - 9.5, this.width + 95.0, this.posY, new Color(102, 102, 102).getRGB());
        FontUtil.normal.drawStringWithShadow("File Edit View Navigate Code Analyze Refactor Build Run Tools Git Window Help", this.posX - 50.0, (float)(this.posY - 7.5), -1);
        Gui.drawRect(this.posX - 50.0, this.posY, this.width, this.height + 85.0, new Color(102, 102, 102).darker().getRGB());
        Gui.drawRect(this.posX - -75.0, this.posY, this.width + 95.0, this.height + 85.0, new Color(51, 51, 51).getRGB());
        int n3 = 0;
        FontUtil.normal.drawStringWithShadow("Exodus > " + (Object)((Object)this.selectedCategory), this.posX - 45.0, (float)(this.posY + 1.5 + (double)n3), -1);
        FontUtil.normal.drawStringWithShadow("Exodus", this.posX - 45.0, (float)(this.posY + 30.0 + (double)n3), -1);
        Category[] categoryArray = Category.values();
        int n4 = categoryArray.length;
        int n5 = 0;
        while (n5 < n4) {
            Category category = categoryArray[n5];
            if (!category.name().equalsIgnoreCase("Hidden")) {
                Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
                char c = category.name().charAt(0);
                FontUtil.normal.drawStringWithShadow(String.valueOf(category.name().substring(0, 1).toUpperCase()) + category.name().substring(1).toLowerCase(), (int)this.posX + 4, (int)(this.posY + 15.0) + n3, new Color(170, 170, 170).darker().getRGB());
                n3 += 15;
            }
            ++n5;
        }
        n3 = 0;
        for (Module module : Exodus.INSTANCE.moduleManager.getModulesByCategory(this.selectedCategory)) {
            Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
            if (module.isToggled()) {
                FontUtil.normal.drawStringWithShadow(String.valueOf(module.getName()) + ".java", (int)this.posX + 80, (int)(this.posY + 5.0) + n3, new Color(255, 255, 255).getRGB());
            } else {
                FontUtil.normal.drawStringWithShadow(String.valueOf(module.getName()) + ".java", (int)this.posX + 80, (int)(this.posY + 5.0) + n3, new Color(170, 170, 170).getRGB());
            }
            n3 += 15;
        }
        for (Comp comp : this.comps) {
            comp.drawScreen(n, n2);
        }
    }
}

