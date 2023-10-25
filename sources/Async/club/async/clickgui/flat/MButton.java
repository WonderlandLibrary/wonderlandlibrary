package club.async.clickgui.flat;

import club.async.Async;
import club.async.clickgui.flat.components.impl.Combo;
import club.async.clickgui.flat.components.impl.Slider;
import club.async.module.Module;
import club.async.module.setting.Setting;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.ModeSetting;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.ColorUtil;
import club.async.util.RenderUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MButton {

    private final double width;
    private final double height;
    private final ClickGUI parent;
    private double offset;
    private double offset2;
    public boolean binding;
    private Module bindingModule;

    public MButton(double width, double height, ClickGUI parent) {
        this.width = width;
        this.height = height;
        this.parent = parent;
        parent.expand = false;
        binding = false;
        bindingModule = null;
    }

    public void drawScreen() {
        if (parent.selectedCategory != null) {
            offset2 = parent.y + 50 - parent.scrollOffset;
            if (parent.searchField.getText().isEmpty())
                for (Module module : Async.INSTANCE.getModuleManager().getModules(parent.selectedCategory))
                    renderButton(module);
                else
                    for (Module module : Async.INSTANCE.getModuleManager().getModules(parent.searchField.getText()))
                        renderButton(module);
        }
    }

    public void keyTyped(int keyCode) throws IOException {
        if (binding) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                bindingModule.setKey(Keyboard.KEY_NONE);
                binding = false;
                return;
            }
            bindingModule.setKey(keyCode);
            binding = false;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (parent.selectedCategory != null) {
            offset = parent.y + 50 - parent.scrollOffset;
            if (parent.searchField.getText().isEmpty())
                for (Module module : Async.INSTANCE.getModuleManager().getModules(parent.selectedCategory))
                    handleClicking(mouseX, mouseY, mouseButton, module);
             else
                for (Module module : Async.INSTANCE.getModuleManager().getModules(parent.searchField.getText()))
                    handleClicking(mouseX, mouseY, mouseButton, module);
        }
    }

    private void handleClicking(int mouseX, int mouseY, int mouseButton, Module module) {
        if (parent.isInside(mouseX, mouseY, parent.x + 140, offset - 8, width - 140, 28) && mouseY >= parent.y + 42 && mouseY <= parent.y + parent.height) {
            if (mouseButton == 0 && !parent.expand)
                module.toggle();
            if (mouseButton == MouseEvent.BUTTON2) {
                binding = true;
                bindingModule = module;
            }
            if (mouseButton == 1) {
                if (module.getSettings() != null && !module.getSettings().isEmpty()) {
                    parent.expand = !parent.expand;
                    parent.selectedModule = module;
                    parent.components.clear();
                    parent.width = 420;
                    double sOffset = 50;
                    double sOffset2 = 50;
                    double sOffset3 = 65;
                    for (Setting setting : module.getSettings()) {
                      //  if (setting.isVisible()) {
                            if (setting instanceof ModeSetting) {
                                parent.components.add(new Combo((ModeSetting) setting, parent,130, sOffset));
                                sOffset += 30;
                            }
                            if (setting instanceof BooleanSetting) {
                                parent.components.add(new club.async.clickgui.flat.components.impl.Checkbox((BooleanSetting) setting, parent,130, sOffset2));
                                sOffset2 += 30;
                            }
                            if (setting instanceof NumberSetting) {
                                parent.components.add(new Slider((NumberSetting) setting, parent,130, sOffset3));
                                sOffset3 += 30;
                            }
                      //  }
                    }
                }
            }
        }
        offset += 30;
    }

    private void renderButton(Module module) {
        GL11.glPushMatrix();
        RenderUtil.prepareScissorBox(parent.x + 130, parent.y + 42, parent.x + width, parent.y + height);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        Gui.drawRect(parent.x + 140, offset2 - 8, parent.x + width, offset2 + 20, new Color(20, 20, 20).getRGB());
        String name = module.getName();
        if (binding && bindingModule == module)
            name = "Waiting...";
        Async.INSTANCE.getFontManager().getFont("Arial 25").drawString(name, (float) parent.x + 150, (float) offset2, new Color(140,140,140).getRGB());
        Gui.drawRect(parent.x + width - 20, offset2 - 8, parent.x + width, offset2 + 20, module.isEnabled() ? ColorUtil.getMainColor().getRGB() : new Color(28, 28, 28).getRGB());
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        if (!Keyboard.getKeyName(module.getKey()).toLowerCase().contains("none")) {
            GL11.glPushMatrix();
            RenderUtil.prepareScissorBox(parent.x + width - 20, parent.y + 42, parent.x + width, parent.y + height);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            Async.INSTANCE.getFontManager().getFont("Arial 20").drawCenteredString(Keyboard.getKeyName(module.getKey()).toLowerCase(), (float) parent.x + 260, (float) offset2, new Color(140,140,140).getRGB());
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }
        offset2 += 30;
    }

    public void reloadSettings(Module module) {

        if (module.getSettings() != null && !module.getSettings().isEmpty()) {
            parent.components.clear();
            parent.width = 420;
            double sOffset = 50;
            double sOffset2 = 50;
            double sOffset3 = 65;
            for (Setting setting : module.getSettings()) {
                if (setting.isVisible()) {
                    if (setting instanceof ModeSetting) {
                        parent.components.add(new Combo((ModeSetting) setting, parent,130, sOffset));
                        sOffset += 30;
                    }
                    if (setting instanceof BooleanSetting) {
                        parent.components.add(new club.async.clickgui.flat.components.impl.Checkbox((BooleanSetting) setting, parent,130, sOffset2));
                        sOffset2 += 30;
                    }
                    if (setting instanceof NumberSetting) {
                        parent.components.add(new Slider((NumberSetting) setting, parent,130, sOffset3));
                        sOffset3 += 30;
                    }
                }
            }
        }
    }

}
