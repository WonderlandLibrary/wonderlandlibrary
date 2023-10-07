/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.arithmo.gui.click.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import me.arithmo.Client;
import me.arithmo.gui.click.ClickGui;
import me.arithmo.gui.click.components.Button;
import me.arithmo.gui.click.components.CategoryButton;
import me.arithmo.gui.click.components.CategoryPanel;
import me.arithmo.gui.click.components.Checkbox;
import me.arithmo.gui.click.components.ColorPreview;
import me.arithmo.gui.click.components.DropdownBox;
import me.arithmo.gui.click.components.DropdownButton;
import me.arithmo.gui.click.components.MainPanel;
import me.arithmo.gui.click.components.RGBSlider;
import me.arithmo.gui.click.components.SLButton;
import me.arithmo.gui.click.components.Slider;
import me.arithmo.gui.click.ui.UI;
import me.arithmo.management.ColorManager;
import me.arithmo.management.ColorObject;
import me.arithmo.management.command.impl.Color;
import me.arithmo.management.keybinding.Bindable;
import me.arithmo.management.keybinding.Keybind;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Options;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.MathUtils;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.StringConversions;
import me.arithmo.util.misc.ChatUtil;
import me.arithmo.util.render.Colors;
import me.arithmo.util.render.NharFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

public class Zeus
extends UI {
    private Minecraft mc = Minecraft.getMinecraft();
    private float hue;

    @Override
    public void mainConstructor(ClickGui p0, MainPanel panel) {
    }

    @Override
    public void onClose() {
    }

    @Override
    public void mainPanelDraw(MainPanel panel, int p0, int p1) {
        GlStateManager.pushMatrix();
        GlStateManager.popMatrix();
        RenderingUtil.rectangleBordered((double)(panel.x + panel.dragX) - 0.3, (double)(panel.y + panel.dragY) - 0.3, (double)(panel.x + 400.0f + panel.dragX) + 0.3, (double)(panel.y + 270.0f + panel.dragY) + 0.3, 0.5, Colors.getColor(60), Colors.getColor(10));
        RenderingUtil.rectangleBordered((double)(panel.x + panel.dragX) + 0.6, (double)(panel.y + panel.dragY) + 0.6, (double)(panel.x + 400.0f + panel.dragX) - 0.6, (double)(panel.y + 270.0f + panel.dragY) - 0.6, 1.3, Colors.getColor(60), Colors.getColor(40));
        RenderingUtil.rectangleBordered((double)(panel.x + panel.dragX) + 2.5, (double)(panel.y + panel.dragY) + 2.5, (double)(panel.x + 400.0f + panel.dragX) - 2.5, (double)(panel.y + 270.0f + panel.dragY) - 2.5, 0.5, Colors.getColor(20), Colors.getColor(10));
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float h = this.hue;
        float h2 = this.hue + 100.0f;
        float h3 = this.hue + 200.0f;
        if (h > 255.0f) {
            h = 0.0f;
        }
        if (h2 > 255.0f) {
            h2 -= 255.0f;
        }
        if (h3 > 255.0f) {
            h3 -= 255.0f;
        }
        java.awt.Color color33 = java.awt.Color.getHSBColor(h / 255.0f, 0.9f, 1.0f);
        java.awt.Color color332 = java.awt.Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
        java.awt.Color color333 = java.awt.Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
        int color = color33.getRGB();
        int color2 = color332.getRGB();
        int color3 = color333.getRGB();
        this.hue = (float)((double)this.hue + 0.15);
        RenderingUtil.drawGradientSideways(panel.x + panel.dragX + 3.0f, panel.y + panel.dragY + 3.0f, panel.x + 202.0f + panel.dragX - 3.0f, panel.dragY + panel.y + 4.0f, color, color2);
        RenderingUtil.drawGradientSideways(panel.x + panel.dragX + 199.0f, panel.y + panel.dragY + 3.0f, panel.x + 400.0f + panel.dragX - 3.0f, panel.dragY + panel.y + 4.0f, color2, color3);
        RenderingUtil.rectangle(panel.x + panel.dragX + 3.0f, (double)(panel.y + panel.dragY) + 3.3, panel.x + 400.0f + panel.dragX - 3.0f, panel.dragY + panel.y + 4.0f, Colors.getColor(0, 120));
        boolean isOff = false;
        for (float y = 5.0f; y < 268.0f; y += 4.0f) {
            int x;
            int n = x = isOff ? 6 : 4;
            while (x < 397) {
                RenderingUtil.rectangle(panel.x + panel.dragX + (float)x, panel.y + panel.dragY + y, (double)(panel.x + panel.dragX + (float)x) + 1.3, (double)(panel.y + panel.dragY + y) + 1.6, Colors.getColor(12));
                x += 4;
            }
            isOff = !isOff;
        }
        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 57.0f, panel.y + panel.dragY + 16.0f, panel.x + 390.0f + panel.dragX, panel.y + 250.0f + panel.dragY, 0.5, Colors.getColor(46), Colors.getColor(10));
        RenderingUtil.rectangle(panel.x + panel.dragX + 58.0f, panel.y + panel.dragY + 17.0f, panel.x + 390.0f + panel.dragX - 1.0f, panel.y + 250.0f + panel.dragY - 1.0f, Colors.getColor(17));
        for (SLButton button : panel.slButtons) {
            button.draw(p0, p1);
        }
        for (CategoryButton button : panel.typeButton) {
            button.draw(p0, p1);
        }
        if (panel.dragging) {
            panel.dragX = (float)p0 - panel.lastDragX;
            panel.dragY = (float)p1 - panel.lastDragY;
        }
    }

    @Override
    public void mainPanelKeyPress(MainPanel panel, int key) {
        for (CategoryButton cbutton : panel.typeButton) {
            for (Button button : cbutton.categoryPanel.buttons) {
                button.keyPressed(key);
            }
        }
    }

    @Override
    public void panelConstructor(MainPanel mainPanel, float x, float y) {
        int y1 = 16;
        for (ModuleData.Type types : ModuleData.Type.values()) {
            mainPanel.typeButton.add(new CategoryButton(mainPanel, types.name(), x, y + (float)y1));
            y += 14.0f;
        }
        mainPanel.typeButton.add(new CategoryButton(mainPanel, "Colors", x, y + (float)y1));
        mainPanel.typeButton.add(new CategoryButton(mainPanel, "Extra", x, (y += 14.0f) + (float)y1));
        mainPanel.slButtons.add(new SLButton(mainPanel, "Save", 5.0f, 201.0f, false));
        mainPanel.slButtons.add(new SLButton(mainPanel, "Load", 5.0f, 215.0f, true));
    }

    @Override
    public void panelMouseClicked(MainPanel mainPanel, int x, int y, int z) {
        if ((float)x >= mainPanel.x + mainPanel.dragX && (float)y >= mainPanel.dragY + mainPanel.y && (float)x <= mainPanel.dragX + mainPanel.x + 400.0f && (float)y <= mainPanel.dragY + mainPanel.y + 12.0f && z == 0) {
            mainPanel.dragging = true;
            mainPanel.lastDragX = (float)x - mainPanel.dragX;
            mainPanel.lastDragY = (float)y - mainPanel.dragY;
        }
        for (CategoryButton c : mainPanel.typeButton) {
            c.mouseClicked(x, y, z);
            c.categoryPanel.mouseClicked(x, y, z);
        }
        for (SLButton button : mainPanel.slButtons) {
            button.mouseClicked(x, y, z);
        }
    }

    @Override
    public void panelMouseMovedOrUp(MainPanel mainPanel, int x, int y, int z) {
        if (z == 0) {
            mainPanel.dragging = false;
        }
        for (CategoryButton button : mainPanel.typeButton) {
            button.mouseReleased(x, y, z);
        }
    }

    @Override
    public void categoryButtonConstructor(CategoryButton p0, MainPanel p1) {
        p0.categoryPanel = new CategoryPanel(p0.name, p0, 0.0f, 0.0f);
    }

    @Override
    public void categoryButtonMouseClicked(CategoryButton p0, MainPanel p1, int p2, int p3, int p4) {
        if ((float)p2 >= p0.x + p1.dragX && (float)p3 >= p1.dragY + p0.y && (float)p2 <= p1.dragX + p0.x + Client.f.getStringWidth(p0.name.toUpperCase()) + 5.0f && (float)p3 <= p1.dragY + p0.y + 12.0f && p4 == 0) {
            int i = 0;
            for (CategoryButton button : p1.typeButton) {
                if (button == p0) {
                    p0.enabled = true;
                    p0.categoryPanel.visible = true;
                } else {
                    button.enabled = false;
                    button.categoryPanel.visible = false;
                }
                ++i;
            }
        }
    }

    @Override
    public void categoryButtonDraw(CategoryButton p0, float p2, float p3) {
        int color;
        int n = color = p0.enabled ? -1 : Colors.getColor(75);
        if (p2 >= p0.x + p0.panel.dragX && p3 >= p0.y + p0.panel.dragY && p2 <= p0.x + p0.panel.dragX + Client.f.getStringWidth(p0.name.toUpperCase()) + 5.0f && p3 <= p0.y + p0.panel.dragY + 12.0f && !p0.enabled) {
            color = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255);
        }
        GlStateManager.pushMatrix();
        Client.f.drawStringWithShadow(p0.name.toUpperCase(), p0.x + 4.0f + p0.panel.dragX, p0.y + p0.panel.dragY, color);
        GlStateManager.popMatrix();
        p0.categoryPanel.draw(p2, p3);
    }

    private List<Setting> getSettings(Module mod) {
        ArrayList<Setting> settings = new ArrayList<Setting>();
        for (Setting set : mod.getSettings().values()) {
            settings.add(set);
        }
        if (settings.isEmpty()) {
            return null;
        }
        return settings;
    }

    @Override
    public void categoryPanelConstructor(CategoryPanel categoryPanel, CategoryButton categoryButton, float x, float y) {
        float biggestY;
        Options option4;
        ArrayList<Setting> sliders;
        float xOff = 62.0f + categoryButton.panel.x;
        float yOff = 20.0f + categoryButton.panel.y;
        if (categoryButton.name.equalsIgnoreCase("Combat")) {
            biggestY = yOff + 8.0f;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() != ModuleData.Type.Combat) continue;
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10.0f, module));
                y = 32.0f;
                if (this.getSettings(module) != null) {
                    y = 18.0f;
                    for (Setting setting32 : this.getSettings(module)) {
                        if (!(setting32.getValue() instanceof Boolean)) continue;
                        categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting32.getName(), xOff, yOff + y, setting32));
                        if (yOff + (y += 8.0f) < biggestY) continue;
                        biggestY = 20.0f + categoryButton.panel.y + 16.0f;
                    }
                    sliders = new ArrayList();
                    for (Setting setting4 : this.getSettings(module)) {
                        if (!(setting4.getValue() instanceof Number)) continue;
                        sliders.add(setting4);
                    }
                    sliders.sort(Comparator.comparing(Setting::getName));
                    for (Setting setting : sliders) {
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = 20.0f + categoryButton.panel.y + 16.0f;
                    }
                    ArrayList<Options> listToAdd = new ArrayList<Options>();
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Options)) continue;
                        option4 = (Options)setting.getValue();
                        listToAdd.add(option4);
                        if (yOff + y < biggestY) continue;
                        biggestY = 20.0f + categoryButton.panel.y + 16.0f;
                    }
                    y += (float)(6 + listToAdd.size() * 16);
                    if (!listToAdd.isEmpty()) {
                        for (Options option2 : listToAdd) {
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option2, xOff, yOff + (y -= 16.0f), categoryPanel));
                        }
                    }
                }
                if ((xOff += 45.0f) <= 20.0f + categoryButton.panel.y + 320.0f) continue;
                xOff = 62.0f + categoryButton.panel.x;
                yOff = biggestY + 16.0f;
            }
        }
        if (categoryButton.name == "Player") {
            biggestY = yOff + 8.0f;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() != ModuleData.Type.Player) continue;
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10.0f, module));
                y = 32.0f;
                if (this.getSettings(module) != null) {
                    y = 18.0f;
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Boolean)) continue;
                        categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                        if (yOff + (y += 8.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                    sliders = new ArrayList();
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Number)) continue;
                        sliders.add(setting);
                    }
                    sliders.sort(Comparator.comparing(Setting::getName));
                    for (Setting setting : sliders) {
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                    ArrayList<Options> listToAdd = new ArrayList<Options>();
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Options)) continue;
                        option4 = (Options)setting.getValue();
                        listToAdd.add(option4);
                        if (yOff + y < biggestY) continue;
                        biggestY = 20.0f + categoryButton.panel.y + 16.0f;
                    }
                    y += (float)(6 + listToAdd.size() * 16);
                    if (!listToAdd.isEmpty()) {
                        for (Options option2 : listToAdd) {
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option2, xOff, yOff + (y -= 16.0f), categoryPanel));
                        }
                    }
                }
                if ((xOff += 50.0f) <= 20.0f + categoryButton.panel.y + 315.0f) continue;
                xOff = 62.0f + categoryButton.panel.x;
                yOff = biggestY + 8.0f;
            }
        }
        if (categoryButton.name == "Movement") {
            biggestY = yOff + 8.0f;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() != ModuleData.Type.Movement) continue;
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10.0f, module));
                y = 32.0f;
                if (this.getSettings(module) != null) {
                    y = 18.0f;
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Boolean)) continue;
                        categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                        if (yOff + (y += 8.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                    sliders = new ArrayList<Setting>();
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Number)) continue;
                        sliders.add(setting);
                    }
                    sliders.sort(Comparator.comparing(Setting::getName));
                    for (Setting setting : sliders) {
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                    ArrayList<Options> listToAdd = new ArrayList<Options>();
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Options)) continue;
                        option4 = (Options)setting.getValue();
                        listToAdd.add(option4);
                        if (yOff + y < biggestY) continue;
                        biggestY = 20.0f + categoryButton.panel.y + 16.0f;
                    }
                    y += (float)(6 + listToAdd.size() * 16);
                    if (!listToAdd.isEmpty()) {
                        for (Options option3 : listToAdd) {
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option3, xOff, yOff + (y -= 16.0f), categoryPanel));
                        }
                    }
                }
                if ((xOff += 50.0f) <= 20.0f + categoryButton.panel.y + 315.0f) continue;
                xOff = 62.0f + categoryButton.panel.x;
                yOff = biggestY + 8.0f;
            }
        }
        if (categoryButton.name == "Visuals") {
            biggestY = yOff + 8.0f;
            int row = 0;
            Module[] arrmodule = Client.getModuleManager().getArray();
            int n = arrmodule.length;
            for (int module = 0; module < n; ++module) {
                Module module2 = arrmodule[module];
                if (module2.getType() != ModuleData.Type.Visuals) continue;
                categoryPanel.buttons.add(new Button(categoryPanel, module2.getName(), xOff, yOff + 10.0f, module2));
                y = 32.0f;
                if (this.getSettings(module2) != null) {
                    y = 18.0f;
                    for (Setting setting : this.getSettings(module2)) {
                        if (!(setting.getValue() instanceof Boolean)) continue;
                        categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                        if (yOff + (y += 8.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                    ArrayList<Setting> sliders2 = new ArrayList<Setting>();
                    for (Setting setting22 : this.getSettings(module2)) {
                        if (!(setting22.getValue() instanceof Number)) continue;
                        sliders2.add(setting22);
                    }
                    sliders2.sort(Comparator.comparing(Setting::getName));
                    for (Setting setting : sliders2) {
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                    ArrayList<Options> listToAdd2 = new ArrayList<Options>();
                    for (Setting setting5 : this.getSettings(module2)) {
                        if (!(setting5.getValue() instanceof Options)) continue;
                        Options option3 = (Options)setting5.getValue();
                        listToAdd2.add(option3);
                        if (yOff + y < biggestY) continue;
                        biggestY = 20.0f + categoryButton.panel.y + 16.0f;
                    }
                    y += (float)(6 + listToAdd2.size() * 16);
                    if (!listToAdd2.isEmpty()) {
                        for (Options option5 : listToAdd2) {
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option5, xOff, yOff + (y -= 16.0f), categoryPanel));
                        }
                    }
                }
                if ((xOff += 40.0f) <= 20.0f + categoryButton.panel.y + 320.0f) continue;
                xOff = 62.0f + categoryButton.panel.x;
                yOff = biggestY + 8.0f;
                if (++row != 2) continue;
                yOff = 20.0f + categoryButton.panel.y + 24.0f + 80.0f;
            }
        }
        if (categoryButton.name == "Other") {
            biggestY = yOff + 8.0f;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() != ModuleData.Type.Other) continue;
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff + 10.0f, module));
                y = 32.0f;
                if (this.getSettings(module) != null) {
                    y = 18.0f;
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Boolean)) continue;
                        categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                        if (yOff + (y += 8.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                    sliders = new ArrayList();
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Number)) continue;
                        sliders.add(setting);
                    }
                    sliders.sort(Comparator.comparing(Setting::getName));
                    for (Setting setting : sliders) {
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                    ArrayList<Options> listToAdd = new ArrayList<Options>();
                    for (Setting setting : this.getSettings(module)) {
                        if (!(setting.getValue() instanceof Options)) continue;
                        option4 = (Options)setting.getValue();
                        listToAdd.add(option4);
                        if (yOff + y < biggestY) continue;
                        biggestY = 20.0f + categoryButton.panel.y + 16.0f;
                    }
                    y += (float)(6 + listToAdd.size() * 16);
                    if (!listToAdd.isEmpty()) {
                        for (Options option5 : listToAdd) {
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option5, xOff, yOff + (y -= 16.0f), categoryPanel));
                        }
                    }
                }
                if ((xOff += 47.0f) <= 20.0f + categoryButton.panel.y + 320.0f) continue;
                xOff = 62.0f + categoryButton.panel.x;
                yOff = biggestY + 8.0f;
            }
        }
        if (categoryButton.name == "Colors") {
            biggestY = yOff + 8.0f;
            categoryPanel.colorPreviews.add(new ColorPreview(Client.cm.getHudColor(), "Hud Color", xOff + 300.0f, y + 5.0f, categoryButton));
        }
    }

    @Override
    public void categoryPanelMouseClicked(CategoryPanel categoryPanel, int p1, int p2, int p3) {
        boolean active = false;
        for (DropdownBox db : categoryPanel.dropdownBoxes) {
            if (!db.active) continue;
            db.mouseClicked(p1, p2, p3);
            active = true;
            break;
        }
        if (!active) {
            for (DropdownBox db : categoryPanel.dropdownBoxes) {
                db.mouseClicked(p1, p2, p3);
            }
            for (Button button : categoryPanel.buttons) {
                button.mouseClicked(p1, p2, p3);
            }
            for (Checkbox checkbox : categoryPanel.checkboxes) {
                checkbox.mouseClicked(p1, p2, p3);
            }
            for (Slider slider : categoryPanel.sliders) {
                slider.mouseClicked(p1, p2, p3);
            }
            for (ColorPreview cp : categoryPanel.colorPreviews) {
                for (RGBSlider slider : cp.sliders) {
                    slider.mouseClicked(p1, p2, p3);
                }
            }
        }
    }

    @Override
    public void categoryPanelDraw(CategoryPanel categoryPanel, float x, float y) {
        if (categoryPanel.visible) {
            RenderingUtil.rectangle(114.0f + categoryPanel.categoryButton.panel.dragX, 65.0f + categoryPanel.categoryButton.panel.dragY, 115.0f + categoryPanel.categoryButton.panel.dragX + Client.fs.getStringWidth(categoryPanel.headerString) + 2.0f, 65.0f + categoryPanel.categoryButton.panel.dragY + Client.fs.getStringHeight(categoryPanel.headerString), Colors.getColor(17));
            Client.fs.drawStringWithShadow(categoryPanel.headerString, 115.0f + categoryPanel.categoryButton.panel.dragX, 66.0f + categoryPanel.categoryButton.panel.dragY, -1);
        }
        for (ColorPreview cp : categoryPanel.colorPreviews) {
            cp.draw(x, y);
        }
        for (Button button : categoryPanel.buttons) {
            button.draw(x, y);
        }
        for (Checkbox checkbox : categoryPanel.checkboxes) {
            checkbox.draw(x, y);
        }
        for (Slider slider : categoryPanel.sliders) {
            slider.draw(x, y);
        }
        for (DropdownBox db : categoryPanel.dropdownBoxes) {
            db.draw(x, y);
        }
        for (DropdownBox db : categoryPanel.dropdownBoxes) {
            if (!db.active) continue;
            for (DropdownButton b : db.buttons) {
                b.draw(x, y);
            }
        }
    }

    @Override
    public void categoryPanelMouseMovedOrUp(CategoryPanel categoryPanel, int x, int y, int button) {
        for (Slider slider : categoryPanel.sliders) {
            slider.mouseReleased(x, y, button);
        }
        for (ColorPreview cp : categoryPanel.colorPreviews) {
            for (RGBSlider slider : cp.sliders) {
                slider.mouseReleased(x, y, button);
            }
        }
    }

    @Override
    public void buttonContructor(Button p0, CategoryPanel panel) {
    }

    @Override
    public void buttonMouseClicked(Button p0, int p2, int p3, int p4, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            boolean hovering;
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            boolean bl = hovering = (float)p2 >= p0.x + xOff && (float)p3 >= p0.y + yOff && (float)p2 <= p0.x + 35.0f + xOff && (float)p3 <= p0.y + 6.0f + yOff;
            if (hovering) {
                if (p4 == 0) {
                    p0.module.toggle();
                    p0.enabled = p0.module.isEnabled();
                } else if (p4 == 1) {
                    if (p0.isBinding) {
                        p0.module.setKeybind(new Keybind(p0.module, Keyboard.getKeyIndex((String)"NONE")));
                        p0.isBinding = false;
                    } else {
                        p0.isBinding = true;
                    }
                }
            } else if (p0.isBinding) {
                p0.isBinding = false;
            }
        }
    }

    @Override
    public void buttonDraw(Button p0, float p2, float p3, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            String meme;
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            RenderingUtil.rectangle((double)(p0.x + xOff) + 0.6, (double)(p0.y + yOff) + 0.6, (double)(p0.x + 6.0f + xOff) + -0.6, (double)(p0.y + 6.0f + yOff) + -0.6, Colors.getColor(10));
            RenderingUtil.drawGradient(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + 6.0f + xOff + -1.0f, p0.y + 6.0f + yOff + -1.0f, Colors.getColor(76), Colors.getColor(51));
            p0.enabled = p0.module.isEnabled();
            boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35.0f + xOff && p3 <= p0.y + 6.0f + yOff;
            GlStateManager.pushMatrix();
            Client.fs.drawStringWithShadow("", 0.0f, 0.0f, Colors.getColor(0, 0));
            Client.fs.drawString(p0.module.getName(), p0.x + xOff + 1.0f, p0.y + 2.5f + yOff - 7.0f, NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
            String string = meme = !p0.module.getKeybind().getKeyStr().equalsIgnoreCase("None") ? "Enable [" + p0.module.getKeybind().getKeyStr() + "]" : "Enable";
            Client.fss.drawString(meme, p0.x + 7.6f + xOff, p0.y + 2.7f + yOff, NharFont.FontType.OUTLINE_THIN, p0.isBinding ? Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255) : (p0.enabled ? -1 : Colors.getColor(180)), Colors.getColor(10));
            GlStateManager.popMatrix();
            if (p0.enabled) {
                RenderingUtil.drawGradient(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + xOff + 5.0f, p0.y + yOff + 5.0f, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
            }
            if (hovering && !p0.enabled) {
                RenderingUtil.rectangle(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + xOff + 5.0f, p0.y + yOff + 5.0f, Colors.getColor(255, 40));
            }
            if (hovering) {
                Client.fs.drawStringWithShadow("Desc: ", panel.categoryButton.panel.x + 4.0f + panel.categoryButton.panel.dragX + 55.0f, panel.categoryButton.panel.y + 8.0f + panel.categoryButton.panel.dragY, -1);
                float width = Client.fs.getStringWidth("Desc: ");
                Client.fss.drawStringWithShadow(p0.module.getDescription(), panel.categoryButton.panel.x + 4.0f + panel.categoryButton.panel.dragX + 55.0f + width, panel.categoryButton.panel.y + 8.0f + panel.categoryButton.panel.dragY, -1);
            }
        }
    }

    @Override
    public void buttonKeyPressed(Button button, int key) {
        if (button.isBinding && key != 0) {
            int keyToBind = key;
            if (key == 1) {
                keyToBind = Keyboard.getKeyIndex((String)"NONE");
            }
            Keybind keybind = new Keybind(button.module, keyToBind);
            button.module.setKeybind(keybind);
            button.isBinding = false;
        }
    }

    @Override
    public void checkBoxMouseClicked(Checkbox p0, int p2, int p3, int p4, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            boolean hovering;
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            boolean bl = hovering = (float)p2 >= p0.x + xOff && (float)p3 >= p0.y + yOff && (float)p2 <= p0.x + 35.0f + xOff && (float)p3 <= p0.y + 6.0f + yOff;
            if (hovering && p4 == 0) {
                boolean xd = (Boolean)p0.setting.getValue();
                p0.setting.setValue(!xd);
                Module.saveSettings();
            }
        }
    }

    @Override
    public void checkBoxDraw(Checkbox p0, float p2, float p3, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            RenderingUtil.rectangle((double)(p0.x + xOff) + 0.6, (double)(p0.y + yOff) + 0.6, (double)(p0.x + 6.0f + xOff) + -0.6, (double)(p0.y + 6.0f + yOff) + -0.6, Colors.getColor(10));
            RenderingUtil.drawGradient(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + 6.0f + xOff + -1.0f, p0.y + 6.0f + yOff + -1.0f, Colors.getColor(76), Colors.getColor(51));
            p0.enabled = (Boolean)p0.setting.getValue();
            boolean hovering = p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 35.0f + xOff && p3 <= p0.y + 6.0f + yOff;
            GlStateManager.pushMatrix();
            String xd = "" + p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1);
            Client.fs.drawStringWithShadow("", 0.0f, 0.0f, Colors.getColor(0, 0));
            Client.fss.drawString(xd, p0.x + 7.5f + xOff, p0.y + 2.6f + yOff, NharFont.FontType.OUTLINE_THIN, hovering ? Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255) : (p0.enabled ? -1 : Colors.getColor(180)), Colors.getColor(10));
            GlStateManager.popMatrix();
            if (p0.enabled) {
                RenderingUtil.drawGradient(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + xOff + 5.0f, p0.y + yOff + 5.0f, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
            }
            if (hovering && !p0.enabled) {
                RenderingUtil.rectangle(p0.x + xOff + 1.0f, p0.y + yOff + 1.0f, p0.x + xOff + 5.0f, p0.y + yOff + 5.0f, Colors.getColor(255, 40));
            }
            if (hovering) {
                Client.fs.drawStringWithShadow("Desc: ", panel.categoryButton.panel.x + 4.0f + panel.categoryButton.panel.dragX + 55.0f, panel.categoryButton.panel.y + 8.0f + panel.categoryButton.panel.dragY, -1);
                float width = Client.fs.getStringWidth("Desc: ");
                Client.fss.drawStringWithShadow(p0.setting.getDesc(), panel.categoryButton.panel.x + 4.0f + panel.categoryButton.panel.dragX + 55.0f + width, panel.categoryButton.panel.y + 8.0f + panel.categoryButton.panel.dragY, -1);
            }
        }
    }

    @Override
    public void dropDownContructor(DropdownBox p0, float p2, float p3, CategoryPanel panel) {
        int y = 7;
        for (String value : p0.option.getOptions()) {
            p0.buttons.add(new DropdownButton(value, p2, p3 + (float)y, p0));
            y += 6;
        }
    }

    @Override
    public void dropDownMouseClicked(DropdownBox dropDown, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        for (DropdownButton db : dropDown.buttons) {
            if (!dropDown.active || !dropDown.panel.visible) continue;
            db.mouseClicked(mouseX, mouseY, mouse);
        }
        dropDown.active = (float)mouseX >= panel.categoryButton.panel.dragX + dropDown.x && (float)mouseY >= panel.categoryButton.panel.dragY + dropDown.y && (float)mouseX <= panel.categoryButton.panel.dragX + dropDown.x + 40.0f && (float)mouseY <= panel.categoryButton.panel.dragY + dropDown.y + 8.0f && mouse == 0 && dropDown.panel.visible ? !dropDown.active : false;
    }

    @Override
    public void dropDownDraw(DropdownBox p0, float p2, float p3, CategoryPanel panel) {
        float xOff = panel.categoryButton.panel.dragX;
        float yOff = panel.categoryButton.panel.dragY;
        boolean hovering = p2 >= panel.categoryButton.panel.dragX + p0.x && p3 >= panel.categoryButton.panel.dragY + p0.y && p2 <= panel.categoryButton.panel.dragX + p0.x + 40.0f && p3 <= panel.categoryButton.panel.dragY + p0.y + 6.0f;
        RenderingUtil.rectangle((double)(p0.x + xOff) - 0.3, (double)(p0.y + yOff) - 0.3, (double)(p0.x + xOff + 40.0f) + 0.3, (double)(p0.y + yOff + 6.0f) + 0.3, Colors.getColor(10));
        RenderingUtil.drawGradient(p0.x + xOff, p0.y + yOff, p0.x + xOff + 40.0f, p0.y + yOff + 6.0f, Colors.getColor(31), Colors.getColor(36));
        if (hovering) {
            RenderingUtil.rectangleBordered(p0.x + xOff, p0.y + yOff, p0.x + xOff + 40.0f, p0.y + yOff + 6.0f, 0.3, Colors.getColor(0, 0), Colors.getColor(90));
        }
        Client.fs.drawStringWithShadow("", 0.0f, 0.0f, Colors.getColor(0, 0));
        Client.fss.drawString(p0.option.getName(), p0.x + xOff, p0.y - 5.0f + yOff, NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)(p0.x + xOff + 36.0f), (double)p0.y + (p0.active ? 3.5 : 2.5) + (double)yOff, 0.0);
        GlStateManager.rotate(p0.active ? 270.0f : 90.0f, 0.0f, 0.0f, 90.0f);
        RenderingUtil.drawCircle(0.0f, 0.0f, 2.5f, 3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255));
        RenderingUtil.drawCircle(0.0f, 0.0f, 1.5f, 3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255));
        RenderingUtil.drawCircle(0.0f, 0.0f, 1.0f, 3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255));
        GlStateManager.popMatrix();
        Client.fss.drawString(p0.option.getSelected(), p0.x + 2.0f + xOff - 1.0f, p0.y + 2.5f + yOff, NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
        if (p0.active) {
            int i = p0.buttons.size();
            RenderingUtil.rectangle((double)(p0.x + xOff) - 0.3, (double)(p0.y + 7.0f + yOff) - 0.3, (double)(p0.x + xOff + 40.0f) + 0.3, (double)(p0.y + yOff + 7.0f + (float)(6 * i)) + 0.3, Colors.getColor(10));
            RenderingUtil.drawGradient(p0.x + xOff, p0.y + yOff + 7.0f, p0.x + xOff + 40.0f, p0.y + yOff + 7.0f + (float)(6 * i), Colors.getColor(31), Colors.getColor(36));
        }
    }

    @Override
    public void dropDownButtonMouseClicked(DropdownButton p0, DropdownBox p1, int x, int y, int mouse) {
        if ((float)x >= p1.panel.categoryButton.panel.dragX + p0.x && (float)y >= p1.panel.categoryButton.panel.dragY + p0.y && (float)x <= p1.panel.categoryButton.panel.dragX + p0.x + 40.0f && (double)y <= (double)(p1.panel.categoryButton.panel.dragY + p0.y) + 5.5 && mouse == 0) {
            p1.option.setSelected(p0.name);
            p1.active = false;
        }
    }

    @Override
    public void dropDownButtonDraw(DropdownButton p0, DropdownBox p1, float x, float y) {
        float xOff = p1.panel.categoryButton.panel.dragX;
        float yOff = p1.panel.categoryButton.panel.dragY;
        boolean hovering = x >= xOff + p0.x && y >= yOff + p0.y && x <= xOff + p0.x + 40.0f && (double)y <= (double)(yOff + p0.y) + 5.5;
        GlStateManager.pushMatrix();
        Client.fs.drawStringWithShadow("", 0.0f, 0.0f, Colors.getColor(0, 0));
        Client.fss.drawString(p0.name, p0.x + 1.0f + xOff, p0.y + 2.5f + yOff, NharFont.FontType.OUTLINE_THIN, hovering ? Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255) : -1, Colors.getColor(10));
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    @Override
    public void SliderContructor(Slider p0, CategoryPanel panel) {
        p0.dragX = ((Number)p0.setting.getValue()).doubleValue() * 40.0 / p0.setting.getMax();
    }

    @Override
    public void categoryButtonMouseReleased(CategoryButton categoryButton, int x, int y, int button) {
        categoryButton.categoryPanel.mouseReleased(x, y, button);
    }

    @Override
    public void slButtonDraw(SLButton slButton, float x, float y, MainPanel panel) {
        float xOff = panel.dragX;
        float yOff = panel.dragY + 75.0f;
        boolean hovering = x >= 55.0f + slButton.x + xOff && y >= slButton.y + yOff - 2.0f && x <= 55.0f + slButton.x + xOff + 40.0f && y <= slButton.y + 8.0f + yOff + 2.0f;
        RenderingUtil.rectangleBordered((double)(slButton.x + xOff + 55.0f) - 0.3, (double)(slButton.y + yOff) - 0.3 - 2.0, (double)(slButton.x + xOff + 40.0f + 55.0f) + 0.3, (double)(slButton.y + 8.0f + yOff) + 0.3 + 2.0, 0.3, Colors.getColor(10), Colors.getColor(10));
        RenderingUtil.drawGradient(slButton.x + xOff + 55.0f, slButton.y + yOff - 2.0f, slButton.x + xOff + 40.0f + 55.0f, slButton.y + 8.0f + yOff + 2.0f, Colors.getColor(46), Colors.getColor(27));
        if (hovering) {
            RenderingUtil.rectangleBordered(slButton.x + xOff + 55.0f, slButton.y + yOff - 2.0f, slButton.x + xOff + 40.0f + 55.0f, slButton.y + 8.0f + yOff + 2.0f, 0.6, Colors.getColor(0, 0), Colors.getColor(90));
        }
        float xOffset = Client.fs.getStringWidth(slButton.name) / 2.0f;
        Client.fs.drawStringWithShadow(slButton.name, xOff + 25.0f + 55.0f - xOffset, slButton.y + yOff + 3.5f, -1);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void slButtonMouseClicked(SLButton slButton, float x, float y, int button, MainPanel panel) {
        float xOff = panel.dragX;
        float yOff = panel.dragY + 75.0f;
        if (button != 0 || x < 55.0f + slButton.x + xOff || y < slButton.y + yOff - 2.0f || x > 55.0f + slButton.x + xOff + 40.0f || y > slButton.y + 8.0f + yOff + 2.0f) return;
        if (slButton.load) {
            ChatUtil.printChat("Settings have been loaded.");
            Module.loadSettings();
            Color.loadStatus();
            for (CategoryPanel xd : panel.typePanel) {
                for (Slider slider : xd.sliders) {
                    slider.dragX = slider.lastDragX = ((Number)slider.setting.getValue()).doubleValue() * 40.0 / slider.setting.getMax();
                }
            }
            return;
        } else {
            ChatUtil.printChat("Settings have been saved.");
            Color.saveStatus();
            Module.saveSettings();
            for (CategoryPanel xd : panel.typePanel) {
                for (Slider slider : xd.sliders) {
                    slider.dragX = slider.lastDragX = ((Number)slider.setting.getValue()).doubleValue() * 40.0 / slider.setting.getMax();
                }
            }
        }
    }

    @Override
    public void colorConstructor(ColorPreview colorPreview, float x, float y) {
        int i = 0;
        for (RGBSlider.Colors xd : RGBSlider.Colors.values()) {
            colorPreview.sliders.add(new RGBSlider(x + 10.0f, y + (float)i, colorPreview, xd));
            i += 12;
        }
    }

    @Override
    public void colorPrewviewDraw(ColorPreview colorPreview, float x, float y) {
        float xOff = colorPreview.x + colorPreview.categoryPanel.panel.dragX;
        float yOff = colorPreview.y + colorPreview.categoryPanel.panel.dragY + 75.0f;
        RenderingUtil.rectangleBordered(xOff - 80.0f, yOff - 6.0f, xOff + 1.0f, yOff + 46.0f, 0.3, Colors.getColor(48), Colors.getColor(10));
        RenderingUtil.rectangle(xOff - 79.0f, yOff - 5.0f, xOff, yOff + 45.0f, Colors.getColor(17));
        RenderingUtil.rectangle(xOff - 74.0f, yOff - 6.0f, xOff - 73.0f + Client.fs.getStringWidth(colorPreview.colorName) + 1.0f, yOff - 4.0f, Colors.getColor(17));
        Client.fs.drawStringWithShadow(colorPreview.colorName, xOff - 73.0f, yOff - 6.0f, -1);
        for (RGBSlider slider : colorPreview.sliders) {
            slider.draw(x, y);
        }
    }

    @Override
    public void rgbSliderDraw(RGBSlider slider, float x, float y) {
        float xOff = slider.x + slider.colorPreview.categoryPanel.panel.dragX - 75.0f;
        float yOff = slider.y + slider.colorPreview.categoryPanel.panel.dragY + 74.0f;
        double fraction = slider.dragX / 60.0;
        double value = MathUtils.getIncremental(fraction * 255.0, 1.0);
        ColorObject cO = slider.colorPreview.colorObject;
        int faggotNiggerColor = Colors.getColor(cO.red, cO.green, cO.blue, 255);
        int faggotNiggerColor2 = Colors.getColor(cO.red, cO.green, cO.blue, 120);
        RenderingUtil.rectangle(xOff, yOff, xOff + 60.0f, yOff + 6.0f, Colors.getColor(32));
        switch (slider.rgba) {
            case ALPHA: {
                faggotNiggerColor = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255);
                faggotNiggerColor2 = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120);
            }
        }
        this.mc.fontRendererObj.drawString("", 100.0f, 100.0f, -1);
        RenderingUtil.rectangle(xOff, yOff, (double)xOff + 60.0 * fraction, yOff + 6.0f, Colors.getColor(0));
        RenderingUtil.drawGradient(xOff, yOff, (double)xOff + 60.0 * fraction, yOff + 6.0f, faggotNiggerColor, faggotNiggerColor2);
        Client.fss.drawStringWithShadow("", 0.0f, 0.0f, Colors.getColor(0, 0));
        String current = "R";
        switch (slider.rgba) {
            case BLUE: {
                current = "B";
                break;
            }
            case GREEN: {
                current = "G";
                break;
            }
            case ALPHA: {
                current = "A";
            }
        }
        Client.fs.drawString(current, xOff - 7.0f, yOff + 3.0f, NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
        float textX = xOff + 30.0f - Client.fs.getStringWidth(Integer.toString((int)value)) / 2.0f;
        Client.fs.drawString(Integer.toString((int)value), textX, yOff + 5.0f, NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
        double newValue = 0.0;
        if (slider.dragging) {
            slider.dragX = (double)x - slider.lastDragX;
            if (value <= 255.0 && value >= 0.0) {
                newValue = value;
            }
            switch (slider.rgba) {
                case RED: {
                    slider.colorPreview.colorObject.setRed((int)newValue);
                    break;
                }
                case GREEN: {
                    slider.colorPreview.colorObject.setGreen((int)newValue);
                    break;
                }
                case BLUE: {
                    slider.colorPreview.colorObject.setBlue((int)newValue);
                    break;
                }
                case ALPHA: {
                    slider.colorPreview.colorObject.setAlpha((int)newValue);
                }
            }
        }
        if (slider.dragX <= 0.0) {
            slider.dragX = 0.0;
        }
        if (slider.dragX >= 60.0) {
            slider.dragX = 60.0;
        }
    }

    @Override
    public void rgbSliderClick(RGBSlider slider, float x, float y, int mouse) {
        float xOff = slider.x + slider.colorPreview.categoryPanel.panel.dragX - 75.0f;
        float yOff = slider.y + slider.colorPreview.categoryPanel.panel.dragY + 74.0f;
        if (slider.colorPreview.categoryPanel.enabled && x >= xOff && y >= yOff && x <= xOff + 60.0f && y <= yOff + 6.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = (double)x - slider.dragX;
        }
    }

    @Override
    public void rgbSliderMovedOrUp(RGBSlider slider, float x, float y, int mouse) {
        if (mouse == 0) {
            Color.saveStatus();
            slider.dragging = false;
        }
    }

    @Override
    public void SliderMouseClicked(Slider slider, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        float xOff = panel.categoryButton.panel.dragX;
        float yOff = panel.categoryButton.panel.dragY;
        if (panel.visible && (float)mouseX >= panel.x + xOff + slider.x && (float)mouseY >= yOff + panel.y + slider.y - 6.0f && (float)mouseX <= xOff + panel.x + slider.x + 40.0f && (float)mouseY <= yOff + panel.y + slider.y + 4.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = (double)mouseX - slider.dragX;
        }
    }

    @Override
    public void SliderMouseMovedOrUp(Slider slider, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        if (mouse == 0) {
            slider.dragging = false;
        }
    }

    @Override
    public void SliderDraw(Slider slider, float x, float y, CategoryPanel panel) {
        if (panel.visible) {
            Object newValue;
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            double fraction = slider.dragX / 40.0;
            double value = MathUtils.getIncremental(fraction * slider.setting.getMax(), slider.setting.getInc());
            float sliderX = (float)(((Number)slider.setting.getValue()).doubleValue() * 38.0 / slider.setting.getMax());
            RenderingUtil.rectangle((double)(slider.x + xOff) - 0.3, (double)(slider.y + yOff) - 0.3, (double)(slider.x + xOff + 38.0f) + 0.3, (double)(slider.y + yOff + 3.0f) + 0.3, Colors.getColor(10));
            RenderingUtil.drawGradient(slider.x + xOff, slider.y + yOff, slider.x + xOff + 38.0f, slider.y + yOff + 3.0f, Colors.getColor(46), Colors.getColor(27));
            RenderingUtil.drawGradient(slider.x + xOff, slider.y + yOff, slider.x + xOff + sliderX, slider.y + yOff + 3.0f, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
            Client.fs.drawStringWithShadow("", slider.x + xOff, slider.y - 5.0f + yOff, -1);
            String xd = "" + slider.setting.getName().charAt(0) + slider.setting.getName().toLowerCase().substring(1);
            double setting = ((Number)slider.setting.getValue()).doubleValue();
            GlStateManager.pushMatrix();
            String valu2e = MathUtils.isInteger(setting) ? "" + (int)setting + "" : "" + setting + "";
            float strWidth = Client.fs.getStringWidth(valu2e);
            float textX = sliderX + strWidth > 42.0f ? sliderX - strWidth : sliderX - strWidth / 2.0f;
            Client.fs.drawString(valu2e, slider.x + xOff + textX, slider.y + yOff + 2.6f, NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
            Client.fss.drawString(xd, slider.x + xOff, slider.y - 5.0f + yOff, NharFont.FontType.OUTLINE_THIN, -1, Colors.getColor(10));
            if (slider.dragging) {
                slider.dragX = (double)x - slider.lastDragX;
                newValue = StringConversions.castNumber(Double.toString(value), slider.setting.getInc());
                slider.setting.setValue(newValue);
            }
            if (((Number)slider.setting.getValue()).doubleValue() <= slider.setting.getMin()) {
                newValue = StringConversions.castNumber(Double.toString(slider.setting.getMin()), slider.setting.getInc());
                slider.setting.setValue(newValue);
            }
            if (((Number)slider.setting.getValue()).doubleValue() >= slider.setting.getMax()) {
                newValue = StringConversions.castNumber(Double.toString(slider.setting.getMax()), slider.setting.getInc());
                slider.setting.setValue(newValue);
            }
            if (slider.dragX <= 0.0) {
                slider.dragX = 0.0;
            }
            if (slider.dragX >= 40.0) {
                slider.dragX = 40.0;
            }
            if (x >= xOff + slider.x && y >= yOff + slider.y - 6.0f && x <= xOff + slider.x + 38.0f && y <= yOff + slider.y + 3.0f || slider.dragging) {
                Client.fs.drawStringWithShadow("Desc: ", panel.categoryButton.panel.x + 4.0f + panel.categoryButton.panel.dragX + 55.0f, panel.categoryButton.panel.y + 8.0f + panel.categoryButton.panel.dragY, -1);
                float width = Client.fs.getStringWidth("Desc: ");
                Client.fss.drawStringWithShadow(slider.setting.getDesc() + " Min: " + slider.setting.getMin() + " Max: " + slider.setting.getMax(), panel.categoryButton.panel.x + 4.0f + panel.categoryButton.panel.dragX + 55.0f + width, panel.categoryButton.panel.y + 8.0f + panel.categoryButton.panel.dragY, -1);
            }
        }
    }

}

