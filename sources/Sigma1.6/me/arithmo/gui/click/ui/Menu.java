/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.click.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.MathUtils;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.StringConversions;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class Menu
extends UI {
    Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void mainConstructor(ClickGui p0, MainPanel panel) {
    }

    @Override
    public void onClose() {
    }

    @Override
    public void mainPanelDraw(MainPanel panel, int p0, int p1) {
        RenderingUtil.rectangleBordered(panel.x + panel.dragX, panel.y + panel.dragY, panel.x + 400.0f + panel.dragX, panel.y + 230.0f + panel.dragY, 3.0, Colors.getColor(14, 14, 14), Colors.getColor(28, 28, 28));
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        this.mc.fontRendererObj.drawStringWithShadow(panel.headerString + " Hook | " + dateFormat.format(date), (panel.x + 8.0f + panel.dragX) / 0.5f, (panel.y + 8.0f + panel.dragY) / 0.5f, -1);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        for (CategoryButton button : panel.typeButton) {
            button.draw(p0, p1);
        }
        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 57.0f, panel.y + panel.dragY + 16.0f, panel.x + 390.0f + panel.dragX, panel.y + 220.0f + panel.dragY, 0.5, Colors.getColor(0, 0, 0, 0), Colors.getColor(28, 28, 28));
        if (panel.dragging) {
            panel.dragX = (float)p0 - panel.lastDragX;
            panel.dragY = (float)p1 - panel.lastDragY;
        }
        RenderingUtil.rectangle(p0 - 1, p1 - 1, p0 + 2, (double)p1 - 0.5, -1);
        RenderingUtil.rectangle(p0 - 1, p1 - 1, (double)p0 - 0.5, p1 + 2, -1);
    }

    @Override
    public void mainPanelKeyPress(MainPanel panel, int key) {
    }

    @Override
    public void panelConstructor(MainPanel mainPanel, float x, float y) {
        int y1 = 16;
        for (ModuleData.Type types : ModuleData.Type.values()) {
            mainPanel.typeButton.add(new CategoryButton(mainPanel, types.name(), x, y + (float)y1));
            y += 14.0f;
        }
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
        if ((float)p2 >= p0.x + p1.dragX && (float)p3 >= p1.dragY + p0.y && (float)p2 <= p1.dragX + p0.x + 50.0f && (float)p3 <= p1.dragY + p0.y + 12.0f && p4 == 0) {
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
        RenderingUtil.rectangle(p0.x + p0.panel.dragX, p0.y + p0.panel.dragY, p0.x + 50.0f + p0.panel.dragX, p0.y + 12.0f + p0.panel.dragY, Colors.getColor(28, 28, 28));
        if (p2 >= p0.x + p0.panel.dragX && p3 >= p0.y + p0.panel.dragY && p2 <= p0.x + p0.panel.dragX + 50.0f && p3 <= p0.y + p0.panel.dragY + 12.0f) {
            RenderingUtil.rectangle(p0.x + 3.0f + p0.panel.dragX, p0.y + p0.panel.dragY, p0.x + 50.0f + p0.panel.dragX, p0.y + 12.0f + p0.panel.dragY, Colors.getColor(255, 255, 255, 50));
        }
        if (p0.enabled) {
            RenderingUtil.rectangle(p0.x + 3.0f + p0.panel.dragX, p0.y + p0.panel.dragY, p0.x + 6.0f + p0.panel.dragX, p0.y + 12.0f + p0.panel.dragY, Colors.getColor(165, 241, 165));
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        this.mc.fontRendererObj.drawStringWithShadow(p0.name, (p0.x + 10.0f + p0.panel.dragX) * 2.0f, (p0.y + 4.0f + p0.panel.dragY) * 2.0f, -1);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
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
        float xOff = 62.0f + categoryButton.panel.x;
        float yOff = 20.0f + categoryButton.panel.y;
        if (categoryButton.name.equalsIgnoreCase("Combat")) {
            biggestY = yOff + 8.0f;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() != ModuleData.Type.Combat) continue;
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                y = 8.0f;
                if (this.getSettings(module) != null) {
                    for (Setting setting : this.getSettings(module)) {
                        if (setting.getValue() instanceof Boolean) {
                            categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                            if (yOff + (y += 8.0f) >= biggestY) {
                                biggestY = yOff + y;
                            }
                        }
                        if (!(setting.getValue() instanceof Number)) continue;
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                }
                if ((xOff += 40.0f) <= 20.0f + categoryButton.panel.y + 360.0f) continue;
                xOff = 62.0f + categoryButton.panel.x;
                yOff = biggestY + 8.0f;
            }
        }
        if (categoryButton.name == "Player") {
            biggestY = yOff + 8.0f;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() != ModuleData.Type.Player) continue;
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                y = 8.0f;
                if (this.getSettings(module) != null) {
                    for (Setting setting : this.getSettings(module)) {
                        if (setting.getValue() instanceof Boolean) {
                            categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                            if (yOff + (y += 8.0f) >= biggestY) {
                                biggestY = yOff + y;
                            }
                        }
                        if (!(setting.getValue() instanceof Number)) continue;
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                }
                if ((xOff += 45.0f) <= 20.0f + categoryButton.panel.y + 315.0f) continue;
                xOff = 62.0f + categoryButton.panel.x;
                yOff = biggestY + 8.0f;
            }
        }
        if (categoryButton.name == "Movement") {
            biggestY = yOff + 8.0f;
            for (Module module : Client.getModuleManager().getArray()) {
                if (module.getType() != ModuleData.Type.Movement) continue;
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                y = 8.0f;
                if (this.getSettings(module) != null) {
                    for (Setting setting : this.getSettings(module)) {
                        if (setting.getValue() instanceof Boolean) {
                            categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                            if (yOff + (y += 8.0f) >= biggestY) {
                                biggestY = yOff + y;
                            }
                        }
                        if (!(setting.getValue() instanceof Number)) continue;
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                }
                if ((xOff += 45.0f) <= 20.0f + categoryButton.panel.y + 315.0f) continue;
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
                categoryPanel.buttons.add(new Button(categoryPanel, module2.getName(), xOff, yOff, module2));
                y = 8.0f;
                if (this.getSettings(module2) != null) {
                    for (Setting setting : this.getSettings(module2)) {
                        if (setting.getValue() instanceof Boolean) {
                            categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                            if (yOff + (y += 8.0f) >= biggestY) {
                                biggestY = yOff + y;
                            }
                        }
                        if (!(setting.getValue() instanceof Number)) continue;
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                }
                if ((xOff += 40.0f) <= 20.0f + categoryButton.panel.y + 360.0f) continue;
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
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                y = 8.0f;
                if (this.getSettings(module) != null) {
                    for (Setting setting : this.getSettings(module)) {
                        if (setting.getValue() instanceof Boolean) {
                            categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                            if (yOff + (y += 8.0f) >= biggestY) {
                                biggestY = yOff + y;
                            }
                        }
                        if (!(setting.getValue() instanceof Number)) continue;
                        categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                        if (yOff + (y += 12.0f) < biggestY) continue;
                        biggestY = yOff + y;
                    }
                }
                if ((xOff += 40.0f) <= 20.0f + categoryButton.panel.y + 360.0f) continue;
                xOff = 62.0f + categoryButton.panel.x;
                yOff = biggestY + 8.0f;
            }
        }
    }

    @Override
    public void categoryPanelMouseClicked(CategoryPanel categoryPanel, int p1, int p2, int p3) {
        for (Button button : categoryPanel.buttons) {
            button.mouseClicked(p1, p2, p3);
        }
        for (Checkbox checkbox : categoryPanel.checkboxes) {
            checkbox.mouseClicked(p1, p2, p3);
        }
        for (Slider slider : categoryPanel.sliders) {
            slider.mouseClicked(p1, p2, p3);
        }
        for (DropdownBox db : categoryPanel.dropdownBoxes) {
            db.mouseClicked(p1, p2, p3);
        }
    }

    @Override
    public void categoryPanelDraw(CategoryPanel categoryPanel, float x, float y) {
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
    }

    @Override
    public void categoryPanelMouseMovedOrUp(CategoryPanel categoryPanel, int x, int y, int button) {
        for (Slider slider : categoryPanel.sliders) {
            slider.mouseReleased(x, y, button);
        }
    }

    @Override
    public void buttonContructor(Button p0, CategoryPanel panel) {
    }

    @Override
    public void buttonMouseClicked(Button p0, int p2, int p3, int p4, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            if ((float)p2 >= p0.x + xOff && (float)p3 >= p0.y + yOff && (float)p2 <= p0.x + xOff + 6.0f && (float)p3 <= p0.y + yOff + 6.0f && p4 == 0) {
                p0.module.toggle();
                p0.enabled = p0.module.isEnabled();
            }
        }
    }

    @Override
    public void buttonDraw(Button p0, float p2, float p3, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            RenderingUtil.drawRect(p0.x + xOff, p0.y + yOff, p0.x + 6.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(28, 28, 28));
            p0.enabled = p0.module.isEnabled();
            if (p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 6.0f + xOff && p3 <= p0.y + 6.0f + yOff) {
                RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + xOff + 6.0f, p0.y + yOff + 6.0f, Colors.getColor(255, 255, 255, 50));
            }
            if (p0.enabled) {
                RenderingUtil.rectangleBordered(p0.x + xOff, p0.y + yOff, p0.x + 6.0f + xOff, p0.y + 6.0f + yOff, 0.3, Colors.getColor(0, 0, 0, 0), Colors.getColor(165, 241, 165));
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            this.mc.fontRendererObj.drawStringWithShadow(p0.module.getName(), (p0.x + 8.0f + xOff) * 2.0f, (p0.y + 1.0f + yOff) * 2.0f, -1);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void buttonKeyPressed(Button button, int key) {
    }

    @Override
    public void checkBoxMouseClicked(Checkbox p0, int p2, int p3, int p4, CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            if ((float)p2 >= p0.x + xOff && (float)p3 >= p0.y + yOff && (float)p2 <= p0.x + xOff + 6.0f && (float)p3 <= p0.y + yOff + 6.0f && p4 == 0) {
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
            RenderingUtil.drawRect(p0.x + xOff, p0.y + yOff, p0.x + 6.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(28, 28, 28));
            p0.enabled = (Boolean)p0.setting.getValue();
            if (p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 6.0f + xOff && p3 <= p0.y + 6.0f + yOff) {
                RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + xOff + 6.0f, p0.y + yOff + 6.0f, Colors.getColor(255, 255, 255, 50));
            }
            RenderingUtil.rectangleBordered(p0.x + xOff, p0.y + yOff, p0.x + 6.0f + xOff, p0.y + 6.0f + yOff, 0.5, Colors.getColor(0, 0, 0, 0), p0.enabled ? Colors.getColor(165, 241, 165) : Colors.getColor(55, 55, 55));
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            String xd = "" + p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1);
            this.mc.fontRendererObj.drawStringWithShadow(xd, (p0.x + 8.0f + xOff) * 2.0f, (p0.y + 1.0f + yOff) * 2.0f, -1);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void dropDownContructor(DropdownBox p0, float p2, float p3, CategoryPanel panel) {
    }

    @Override
    public void dropDownMouseClicked(DropdownBox dropDown, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        if (dropDown.active) {
            for (DropdownButton b : dropDown.buttons) {
                b.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        if ((float)mouseX >= panel.categoryButton.panel.dragX + dropDown.x && (float)mouseY >= panel.categoryButton.panel.dragY + dropDown.y && (float)mouseX <= panel.categoryButton.panel.dragX + dropDown.x + 40.0f && (float)mouseY <= panel.categoryButton.panel.dragY + dropDown.y + 8.0f && mouse == 0) {
            dropDown.active = !dropDown.active;
        }
    }

    @Override
    public void dropDownDraw(DropdownBox p0, float p2, float p3, CategoryPanel panel) {
        boolean hovering;
        float xOff = panel.categoryButton.panel.dragX;
        float yOff = panel.categoryButton.panel.dragY;
        RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(28, 28, 28));
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        this.mc.fontRendererObj.drawStringWithShadow("Target Mode", (p0.x + xOff) * 2.0f, (p0.y - 7.0f + yOff) * 2.0f, -1);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        boolean bl = hovering = p2 >= panel.categoryButton.panel.dragX + p0.x && p3 >= panel.categoryButton.panel.dragY + p0.y && p2 <= panel.categoryButton.panel.dragX + p0.x + 40.0f && p3 <= panel.categoryButton.panel.dragY + p0.y + 6.0f;
        if (hovering) {
            RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(255, 255, 255, 50));
        }
        if (p0.active) {
            for (DropdownButton buttons : p0.buttons) {
                buttons.draw(p2, p3);
            }
        }
    }

    @Override
    public void dropDownButtonMouseClicked(DropdownButton p0, DropdownBox p1, int x, int y, int mouse) {
        if ((float)x < p1.panel.categoryButton.panel.dragX + p0.x || (float)y < p1.panel.categoryButton.panel.dragY + p0.y || (float)x > p1.panel.categoryButton.panel.dragX + p0.x + 40.0f || (float)y > p1.panel.categoryButton.panel.dragY + p0.y + 8.0f || mouse == 0) {
            // empty if block
        }
    }

    @Override
    public void dropDownButtonDraw(DropdownButton p0, DropdownBox p1, float x, float y) {
        boolean hovering;
        float xOff = p1.panel.categoryButton.panel.dragX;
        float yOff = p1.panel.categoryButton.panel.dragY;
        RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(28, 28, 28));
        boolean bl = hovering = x >= xOff + p0.x && y >= yOff + p0.y && x <= xOff + p0.x + 40.0f && y <= yOff + p0.y + 6.0f;
        if (hovering) {
            RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(255, 255, 255, 50));
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        int offSet = this.mc.fontRendererObj.getStringWidth(p0.name) / 2;
        this.mc.fontRendererObj.drawStringWithShadow(p0.name, (p0.x + 20.0f - (float)(offSet / 2) + xOff) * 2.0f, (p0.y + 1.0f + yOff) * 2.0f, -1);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    @Override
    public void SliderContructor(Slider p0, CategoryPanel panel) {
        p0.dragX = ((Number)p0.setting.getValue()).doubleValue() * 40.0 / p0.setting.getMax();
    }

    @Override
    public void SliderMouseClicked(Slider slider, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        float xOff = panel.categoryButton.panel.dragX;
        float yOff = panel.categoryButton.panel.dragY;
        if (panel.visible && (float)mouseX >= panel.x + xOff + slider.x && (float)mouseY >= yOff + panel.y + slider.y && (float)mouseX <= xOff + panel.x + slider.x + 40.0f && (float)mouseY <= yOff + panel.y + slider.y + 4.0f && mouse == 0) {
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
            float xOff = panel.categoryButton.panel.dragX;
            float yOff = panel.categoryButton.panel.dragY;
            double fraction = slider.dragX / 40.0;
            double value = MathUtils.roundToPlace(fraction * slider.setting.getMax(), 2);
            RenderingUtil.rectangle(slider.x + xOff, slider.y + yOff, slider.x + xOff + 38.0f, slider.y + yOff + 3.0f, Colors.getColor(28, 28, 28));
            RenderingUtil.rectangle(slider.x + xOff, slider.y + yOff, (double)(slider.x + xOff) + 38.0 * fraction, slider.y + yOff + 3.0f, Colors.getColor(165, 241, 165));
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            String xd = "" + slider.setting.getName().charAt(0) + slider.setting.getName().toLowerCase().substring(1);
            this.mc.fontRendererObj.drawStringWithShadow(xd, (slider.x + xOff) * 2.0f, (slider.y - 6.0f + yOff) * 2.0f, -1);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
            if (x >= xOff + slider.x && y >= yOff + slider.y && x <= xOff + slider.x + 38.0f && y <= yOff + slider.y + 3.0f || slider.dragging) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5, 0.5, 0.5);
                int strWidth = this.mc.fontRendererObj.getStringWidth(xd + " ");
                this.mc.fontRendererObj.drawStringWithShadow(Double.toString(((Number)slider.setting.getValue()).doubleValue()), (slider.x + (float)(strWidth / 2) + xOff) * 2.0f, (slider.y - 6.0f + yOff) * 2.0f, -1);
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
            }
            if (slider.dragging) {
                slider.dragX = (double)x - slider.lastDragX;
                Object newValue = StringConversions.castNumber(Double.toString(value), slider.setting.getValue());
                slider.setting.setValue(newValue);
            }
            if (((Number)slider.setting.getValue()).doubleValue() <= slider.setting.getMin()) {
                Object newValue = StringConversions.castNumber(Double.toString(slider.setting.getMin()), slider.setting.getValue());
                slider.setting.setValue(newValue);
            }
            if (((Number)slider.setting.getValue()).doubleValue() >= slider.setting.getMax()) {
                Object newValue = StringConversions.castNumber(Double.toString(slider.setting.getMax()), slider.setting.getValue());
                slider.setting.setValue(newValue);
            }
            if (slider.dragX <= 0.0) {
                slider.dragX = 0.0;
            }
            if (slider.dragX >= 40.0) {
                slider.dragX = 40.0;
            }
        }
    }

    @Override
    public void categoryButtonMouseReleased(CategoryButton categoryButton, int x, int y, int button) {
        categoryButton.categoryPanel.mouseReleased(x, y, button);
    }

    @Override
    public void slButtonDraw(SLButton slButton, float x, float y, MainPanel panel) {
    }

    @Override
    public void slButtonMouseClicked(SLButton slButton, float x, float y, int button, MainPanel panel) {
    }

    @Override
    public void colorConstructor(ColorPreview colorPreview, float x, float y) {
    }

    @Override
    public void colorPrewviewDraw(ColorPreview colorPreview, float x, float y) {
    }

    @Override
    public void rgbSliderDraw(RGBSlider slider, float x, float y) {
    }

    @Override
    public void rgbSliderClick(RGBSlider slider, float x, float y, int mouse) {
    }

    @Override
    public void rgbSliderMovedOrUp(RGBSlider slider, float x, float y, int mouse) {
    }
}

