// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.ui;

import java.util.HashMap;
import exhibition.gui.click.components.RGBSlider;
import exhibition.gui.click.components.ColorPreview;
import exhibition.gui.click.components.SLButton;
import exhibition.util.StringConversions;
import exhibition.util.MathUtils;
import exhibition.gui.click.components.DropdownButton;
import exhibition.gui.click.components.GroupBox;
import exhibition.gui.click.components.DropdownBox;
import exhibition.gui.click.components.Slider;
import exhibition.gui.click.components.Checkbox;
import exhibition.gui.click.components.Button;
import exhibition.Client;
import java.util.ArrayList;
import exhibition.module.data.Setting;
import java.util.List;
import exhibition.module.Module;
import exhibition.gui.click.components.CategoryPanel;
import exhibition.module.data.ModuleData;
import java.util.Iterator;
import java.text.DateFormat;
import exhibition.gui.click.components.CategoryButton;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import exhibition.gui.click.components.MainPanel;
import exhibition.gui.click.ClickGui;
import net.minecraft.client.Minecraft;

public class Menu extends UI
{
    Minecraft mc;
    
    public Menu() {
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void mainConstructor(final ClickGui p0) {
    }
    
    @Override
    public void mainPanelDraw(final MainPanel panel, final int p0, final int p1) {
        RenderingUtil.rectangleBordered(panel.x + panel.dragX, panel.y + panel.dragY, panel.x + 400.0f + panel.dragX, panel.y + 230.0f + panel.dragY, 3.0, Colors.getColor(14, 14, 14), Colors.getColor(28, 28, 28));
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        final DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        final Date date = new Date();
        this.mc.fontRendererObj.drawStringWithShadow(panel.headerString + " Hook | " + dateFormat.format(date), (panel.x + 8.0f + panel.dragX) / 0.5f, (panel.y + 8.0f + panel.dragY) / 0.5f, -1);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        for (final CategoryButton button : panel.typeButton) {
            button.draw(p0, p1);
        }
        RenderingUtil.rectangleBordered(panel.x + panel.dragX + 57.0f, panel.y + panel.dragY + 16.0f, panel.x + 390.0f + panel.dragX, panel.y + 220.0f + panel.dragY, 0.5, Colors.getColor(0, 0, 0, 0), Colors.getColor(28, 28, 28));
        if (panel.dragging) {
            panel.dragX = p0 - panel.lastDragX;
            panel.dragY = p1 - panel.lastDragY;
        }
        RenderingUtil.rectangle(p0 - 1, p1 - 1, p0 + 2, p1 - 0.5, -1);
        RenderingUtil.rectangle(p0 - 1, p1 - 1, p0 - 0.5, p1 + 2, -1);
    }
    
    @Override
    public void mainPanelKeyPress(final MainPanel panel, final int key) {
    }
    
    @Override
    public void panelConstructor(final MainPanel mainPanel, final float x, float y) {
        final int y2 = 16;
        for (final ModuleData.Type types : ModuleData.Type.values()) {
            mainPanel.typeButton.add(new CategoryButton(mainPanel, types.name(), x, y + y2));
            y += 14.0f;
        }
    }
    
    @Override
    public void panelMouseClicked(final MainPanel mainPanel, final int x, final int y, final int z) {
        if (x >= mainPanel.x + mainPanel.dragX && y >= mainPanel.dragY + mainPanel.y && x <= mainPanel.dragX + mainPanel.x + 400.0f && y <= mainPanel.dragY + mainPanel.y + 12.0f && z == 0) {
            mainPanel.dragging = true;
            mainPanel.lastDragX = x - mainPanel.dragX;
            mainPanel.lastDragY = y - mainPanel.dragY;
        }
        for (final CategoryButton c : mainPanel.typeButton) {
            c.mouseClicked(x, y, z);
            c.categoryPanel.mouseClicked(x, y, z);
        }
    }
    
    @Override
    public void panelMouseMovedOrUp(final MainPanel mainPanel, final int x, final int y, final int z) {
        if (z == 0) {
            mainPanel.dragging = false;
        }
        for (final CategoryButton button : mainPanel.typeButton) {
            button.mouseReleased(x, y, z);
        }
    }
    
    @Override
    public void categoryButtonConstructor(final CategoryButton p0, final MainPanel p1) {
        p0.categoryPanel = new CategoryPanel(p0.name, p0, 0.0f, 0.0f);
    }
    
    @Override
    public void categoryButtonMouseClicked(final CategoryButton p0, final MainPanel p1, final int p2, final int p3, final int p4) {
        if (p2 >= p0.x + p1.dragX && p3 >= p1.dragY + p0.y && p2 <= p1.dragX + p0.x + 50.0f && p3 <= p1.dragY + p0.y + 12.0f && p4 == 0) {
            int i = 0;
            for (final CategoryButton button : p1.typeButton) {
                if (button == p0) {
                    p0.enabled = true;
                    p0.categoryPanel.visible = true;
                }
                else {
                    button.enabled = false;
                    button.categoryPanel.visible = false;
                }
                ++i;
            }
        }
    }
    
    @Override
    public void categoryButtonDraw(final CategoryButton p0, final float p2, final float p3) {
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
    
    private List<Setting> getSettings(final Module mod) {
        final List<Setting> settings = new ArrayList<Setting>();
        for (final Setting set : ((HashMap<K, Setting>)mod.getSettings()).values()) {
            settings.add(set);
        }
        if (settings.isEmpty()) {
            return null;
        }
        return settings;
    }
    
    @Override
    public void categoryPanelConstructor(final CategoryPanel categoryPanel, final CategoryButton categoryButton, final float x, float y) {
        float xOff = 62.0f + categoryButton.panel.x;
        float yOff = 20.0f + categoryButton.panel.y;
        if (categoryButton.name.equalsIgnoreCase("Combat")) {
            float biggestY = yOff + 8.0f;
            for (final Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Combat) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                    y = 8.0f;
                    if (this.getSettings(module) != null) {
                        for (final Setting setting : this.getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                                y += 8.0f;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                                y += 12.0f;
                                if (yOff + y < biggestY) {
                                    continue;
                                }
                                biggestY = yOff + y;
                            }
                        }
                    }
                    xOff += 40.0f;
                    if (xOff > 20.0f + categoryButton.panel.y + 360.0f) {
                        xOff = 62.0f + categoryButton.panel.x;
                        yOff = biggestY + 8.0f;
                    }
                }
            }
        }
        if (categoryButton.name == "Player") {
            float biggestY = yOff + 8.0f;
            for (final Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Player) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                    y = 8.0f;
                    if (this.getSettings(module) != null) {
                        for (final Setting setting : this.getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                                y += 8.0f;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                                y += 12.0f;
                                if (yOff + y < biggestY) {
                                    continue;
                                }
                                biggestY = yOff + y;
                            }
                        }
                    }
                    xOff += 45.0f;
                    if (xOff > 20.0f + categoryButton.panel.y + 315.0f) {
                        xOff = 62.0f + categoryButton.panel.x;
                        yOff = biggestY + 8.0f;
                    }
                }
            }
        }
        if (categoryButton.name == "Movement") {
            float biggestY = yOff + 8.0f;
            for (final Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Movement) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                    y = 8.0f;
                    if (this.getSettings(module) != null) {
                        for (final Setting setting : this.getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                                y += 8.0f;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                                y += 12.0f;
                                if (yOff + y < biggestY) {
                                    continue;
                                }
                                biggestY = yOff + y;
                            }
                        }
                    }
                    xOff += 45.0f;
                    if (xOff > 20.0f + categoryButton.panel.y + 315.0f) {
                        xOff = 62.0f + categoryButton.panel.x;
                        yOff = biggestY + 8.0f;
                    }
                }
            }
        }
        if (categoryButton.name == "Visuals") {
            float biggestY = yOff + 8.0f;
            int row = 0;
            for (final Module module2 : Client.getModuleManager().getArray()) {
                if (module2.getType() == ModuleData.Type.Visuals) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module2.getName(), xOff, yOff, module2));
                    y = 8.0f;
                    if (this.getSettings(module2) != null) {
                        for (final Setting setting2 : this.getSettings(module2)) {
                            if (setting2.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting2.getName(), xOff, yOff + y, setting2));
                                y += 8.0f;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting2.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting2));
                                y += 12.0f;
                                if (yOff + y < biggestY) {
                                    continue;
                                }
                                biggestY = yOff + y;
                            }
                        }
                    }
                    xOff += 40.0f;
                    if (xOff > 20.0f + categoryButton.panel.y + 360.0f) {
                        ++row;
                        xOff = 62.0f + categoryButton.panel.x;
                        yOff = biggestY + 8.0f;
                        if (row == 2) {
                            yOff = 20.0f + categoryButton.panel.y + 24.0f + 80.0f;
                        }
                    }
                }
            }
        }
        if (categoryButton.name == "Other") {
            float biggestY = yOff + 8.0f;
            for (final Module module : Client.getModuleManager().getArray()) {
                if (module.getType() == ModuleData.Type.Other) {
                    categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), xOff, yOff, module));
                    y = 8.0f;
                    if (this.getSettings(module) != null) {
                        for (final Setting setting : this.getSettings(module)) {
                            if (setting.getValue() instanceof Boolean) {
                                categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), xOff, yOff + y, setting));
                                y += 8.0f;
                                if (yOff + y >= biggestY) {
                                    biggestY = yOff + y;
                                }
                            }
                            if (setting.getValue() instanceof Number) {
                                categoryPanel.sliders.add(new Slider(categoryPanel, xOff, yOff + y + 6.0f, setting));
                                y += 12.0f;
                                if (yOff + y < biggestY) {
                                    continue;
                                }
                                biggestY = yOff + y;
                            }
                        }
                    }
                    xOff += 40.0f;
                    if (xOff > 20.0f + categoryButton.panel.y + 360.0f) {
                        xOff = 62.0f + categoryButton.panel.x;
                        yOff = biggestY + 8.0f;
                    }
                }
            }
        }
    }
    
    @Override
    public void categoryPanelMouseClicked(final CategoryPanel categoryPanel, final int p1, final int p2, final int p3) {
        for (final Button button : categoryPanel.buttons) {
            button.mouseClicked(p1, p2, p3);
        }
        for (final Checkbox checkbox : categoryPanel.checkboxes) {
            checkbox.mouseClicked(p1, p2, p3);
        }
        for (final Slider slider : categoryPanel.sliders) {
            slider.mouseClicked(p1, p2, p3);
        }
        for (final DropdownBox db : categoryPanel.dropdownBoxes) {
            db.mouseClicked(p1, p2, p3);
        }
    }
    
    @Override
    public void categoryPanelDraw(final CategoryPanel categoryPanel, final float x, final float y) {
        for (final Button button : categoryPanel.buttons) {
            button.draw(x, y);
        }
        for (final Checkbox checkbox : categoryPanel.checkboxes) {
            checkbox.draw(x, y);
        }
        for (final Slider slider : categoryPanel.sliders) {
            slider.draw(x, y);
        }
        for (final DropdownBox db : categoryPanel.dropdownBoxes) {
            db.draw(x, y);
        }
    }
    
    @Override
    public void categoryPanelMouseMovedOrUp(final CategoryPanel categoryPanel, final int x, final int y, final int button) {
        for (final Slider slider : categoryPanel.sliders) {
            slider.mouseReleased(x, y, button);
        }
    }
    
    @Override
    public void groupBoxConstructor(final GroupBox groupBox, final float x, final float y) {
    }
    
    @Override
    public void groupBoxMouseClicked(final GroupBox groupBox, final int p1, final int p2, final int p3) {
    }
    
    @Override
    public void groupBoxDraw(final GroupBox groupBox, final float x, final float y) {
    }
    
    @Override
    public void groupBoxMouseMovedOrUp(final GroupBox groupBox, final int x, final int y, final int button) {
    }
    
    @Override
    public void buttonContructor(final Button p0, final CategoryPanel panel) {
    }
    
    @Override
    public void buttonMouseClicked(final Button p0, final int p2, final int p3, final int p4, final CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
            if (p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + xOff + 6.0f && p3 <= p0.y + yOff + 6.0f && p4 == 0) {
                p0.module.toggle();
                p0.enabled = p0.module.isEnabled();
            }
        }
    }
    
    @Override
    public void buttonDraw(final Button p0, final float p2, final float p3, final CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
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
    public void buttonKeyPressed(final Button button, final int key) {
    }
    
    @Override
    public void checkBoxMouseClicked(final Checkbox p0, final int p2, final int p3, final int p4, final CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
            if (p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + xOff + 6.0f && p3 <= p0.y + yOff + 6.0f && p4 == 0) {
                final boolean xd = p0.setting.getValue();
                p0.setting.setValue(!xd);
                Module.saveSettings();
            }
        }
    }
    
    @Override
    public void checkBoxDraw(final Checkbox p0, final float p2, final float p3, final CategoryPanel panel) {
        if (panel.categoryButton.enabled) {
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
            RenderingUtil.drawRect(p0.x + xOff, p0.y + yOff, p0.x + 6.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(28, 28, 28));
            p0.enabled = p0.setting.getValue();
            if (p2 >= p0.x + xOff && p3 >= p0.y + yOff && p2 <= p0.x + 6.0f + xOff && p3 <= p0.y + 6.0f + yOff) {
                RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + xOff + 6.0f, p0.y + yOff + 6.0f, Colors.getColor(255, 255, 255, 50));
            }
            RenderingUtil.rectangleBordered(p0.x + xOff, p0.y + yOff, p0.x + 6.0f + xOff, p0.y + 6.0f + yOff, 0.5, Colors.getColor(0, 0, 0, 0), p0.enabled ? Colors.getColor(165, 241, 165) : Colors.getColor(55, 55, 55));
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            final String xd = p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1);
            this.mc.fontRendererObj.drawStringWithShadow(xd, (p0.x + 8.0f + xOff) * 2.0f, (p0.y + 1.0f + yOff) * 2.0f, -1);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void dropDownContructor(final DropdownBox p0, final float p2, final float p3, final CategoryPanel panel) {
    }
    
    @Override
    public void dropDownMouseClicked(final DropdownBox dropDown, final int mouseX, final int mouseY, final int mouse, final CategoryPanel panel) {
        if (dropDown.active) {
            for (final DropdownButton b : dropDown.buttons) {
                b.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        if (mouseX >= panel.categoryButton.panel.dragX + dropDown.x && mouseY >= panel.categoryButton.panel.dragY + dropDown.y && mouseX <= panel.categoryButton.panel.dragX + dropDown.x + 40.0f && mouseY <= panel.categoryButton.panel.dragY + dropDown.y + 8.0f && mouse == 0) {
            dropDown.active = !dropDown.active;
        }
    }
    
    @Override
    public void dropDownDraw(final DropdownBox p0, final float p2, final float p3, final CategoryPanel panel) {
        final float xOff = panel.categoryButton.panel.dragX;
        final float yOff = panel.categoryButton.panel.dragY;
        RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(28, 28, 28));
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        this.mc.fontRendererObj.drawStringWithShadow("Target Mode", (p0.x + xOff) * 2.0f, (p0.y - 7.0f + yOff) * 2.0f, -1);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        final boolean hovering = p2 >= panel.categoryButton.panel.dragX + p0.x && p3 >= panel.categoryButton.panel.dragY + p0.y && p2 <= panel.categoryButton.panel.dragX + p0.x + 40.0f && p3 <= panel.categoryButton.panel.dragY + p0.y + 6.0f;
        if (hovering) {
            RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(255, 255, 255, 50));
        }
        if (p0.active) {
            for (final DropdownButton buttons : p0.buttons) {
                buttons.draw(p2, p3);
            }
        }
    }
    
    @Override
    public void dropDownButtonMouseClicked(final DropdownButton p0, final DropdownBox p1, final int x, final int y, final int mouse) {
        if (x < p1.panel.categoryButton.panel.dragX + p0.x || y < p1.panel.categoryButton.panel.dragY + p0.y || x > p1.panel.categoryButton.panel.dragX + p0.x + 40.0f || y > p1.panel.categoryButton.panel.dragY + p0.y + 8.0f || mouse == 0) {}
    }
    
    @Override
    public void dropDownButtonDraw(final DropdownButton p0, final DropdownBox p1, final float x, final float y) {
        final float xOff = p1.panel.categoryButton.panel.dragX;
        final float yOff = p1.panel.categoryButton.panel.dragY;
        RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(28, 28, 28));
        final boolean hovering = x >= xOff + p0.x && y >= yOff + p0.y && x <= xOff + p0.x + 40.0f && y <= yOff + p0.y + 6.0f;
        if (hovering) {
            RenderingUtil.rectangle(p0.x + xOff, p0.y + yOff, p0.x + 40.0f + xOff, p0.y + 6.0f + yOff, Colors.getColor(255, 255, 255, 50));
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        final int offSet = this.mc.fontRendererObj.getStringWidth(p0.name) / 2;
        this.mc.fontRendererObj.drawStringWithShadow(p0.name, (p0.x + 20.0f - offSet / 2 + xOff) * 2.0f, (p0.y + 1.0f + yOff) * 2.0f, -1);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void SliderContructor(final Slider p0, final CategoryPanel panel) {
        p0.dragX = p0.setting.getValue().doubleValue() * 40.0 / p0.setting.getMax();
    }
    
    @Override
    public void SliderMouseClicked(final Slider slider, final int mouseX, final int mouseY, final int mouse, final CategoryPanel panel) {
        final float xOff = panel.categoryButton.panel.dragX;
        final float yOff = panel.categoryButton.panel.dragY;
        if (panel.visible && mouseX >= panel.x + xOff + slider.x && mouseY >= yOff + panel.y + slider.y && mouseX <= xOff + panel.x + slider.x + 40.0f && mouseY <= yOff + panel.y + slider.y + 4.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = mouseX - slider.dragX;
        }
    }
    
    @Override
    public void SliderMouseMovedOrUp(final Slider slider, final int mouseX, final int mouseY, final int mouse, final CategoryPanel panel) {
        if (mouse == 0) {
            slider.dragging = false;
        }
    }
    
    @Override
    public void SliderDraw(final Slider slider, final float x, final float y, final CategoryPanel panel) {
        if (panel.visible) {
            final float xOff = panel.categoryButton.panel.dragX;
            final float yOff = panel.categoryButton.panel.dragY;
            final double fraction = slider.dragX / 40.0;
            final double value = MathUtils.roundToPlace(fraction * slider.setting.getMax(), 2);
            RenderingUtil.rectangle(slider.x + xOff, slider.y + yOff, slider.x + xOff + 38.0f, slider.y + yOff + 3.0f, Colors.getColor(28, 28, 28));
            RenderingUtil.rectangle(slider.x + xOff, slider.y + yOff, slider.x + xOff + 38.0 * fraction, slider.y + yOff + 3.0f, Colors.getColor(165, 241, 165));
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            final String xd = slider.setting.getName().charAt(0) + slider.setting.getName().toLowerCase().substring(1);
            this.mc.fontRendererObj.drawStringWithShadow(xd, (slider.x + xOff) * 2.0f, (slider.y - 6.0f + yOff) * 2.0f, -1);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
            if ((x >= xOff + slider.x && y >= yOff + slider.y && x <= xOff + slider.x + 38.0f && y <= yOff + slider.y + 3.0f) || slider.dragging) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5, 0.5, 0.5);
                final int strWidth = this.mc.fontRendererObj.getStringWidth(xd + " ");
                this.mc.fontRendererObj.drawStringWithShadow(Double.toString(slider.setting.getValue().doubleValue()), (slider.x + strWidth / 2 + xOff) * 2.0f, (slider.y - 6.0f + yOff) * 2.0f, -1);
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
            }
            if (slider.dragging) {
                slider.dragX = x - slider.lastDragX;
                final Object newValue = StringConversions.castNumber(Double.toString(value), slider.setting.getValue());
                slider.setting.setValue(newValue);
            }
            if (slider.setting.getValue().doubleValue() <= slider.setting.getMin()) {
                final Object newValue = StringConversions.castNumber(Double.toString(slider.setting.getMin()), slider.setting.getValue());
                slider.setting.setValue(newValue);
            }
            if (slider.setting.getValue().doubleValue() >= slider.setting.getMax()) {
                final Object newValue = StringConversions.castNumber(Double.toString(slider.setting.getMax()), slider.setting.getValue());
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
    public void categoryButtonMouseReleased(final CategoryButton categoryButton, final int x, final int y, final int button) {
        categoryButton.categoryPanel.mouseReleased(x, y, button);
    }
    
    @Override
    public void slButtonDraw(final SLButton slButton, final float x, final float y, final MainPanel panel) {
    }
    
    @Override
    public void slButtonMouseClicked(final SLButton slButton, final float x, final float y, final int button, final MainPanel panel) {
    }
    
    @Override
    public void colorConstructor(final ColorPreview colorPreview, final float x, final float y) {
    }
    
    @Override
    public void colorPrewviewDraw(final ColorPreview colorPreview, final float x, final float y) {
    }
    
    @Override
    public void rgbSliderDraw(final RGBSlider slider, final float x, final float y) {
    }
    
    @Override
    public void rgbSliderClick(final RGBSlider slider, final float x, final float y, final int mouse) {
    }
    
    @Override
    public void rgbSliderMovedOrUp(final RGBSlider slider, final float x, final float y, final int mouse) {
    }
}
