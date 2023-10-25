package club.async.clickgui.dropdown;

import club.async.Async;
import club.async.clickgui.dropdown.components.BooleanComp;
import club.async.clickgui.dropdown.components.ModeComp;
import club.async.clickgui.dropdown.components.NumberComp;
import club.async.module.Module;
import club.async.module.setting.Setting;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.ModeSetting;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.ColorUtil;
import club.async.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class MButton {

    private final Panel parent;
    public static boolean binding;
    private Module bindingModule;
    public double scrollOffset;

    public MButton(Panel parent) {
        this.parent = parent;
    }

    public void drawScreen(int mouseX, int mouseY) {
        int offset = 25;
        for (Module module : Async.INSTANCE.getModuleManager().getModules(parent.category))
        {
            if (module.isEnabled())
                Gui.drawRect(parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + 21, ColorUtil.getMainColor());

            Async.INSTANCE.getFontManager().getFont("Arial 23").drawString((binding && bindingModule == module) ? "Waiting..." : module.getName(), parent.x + 10, parent.y + offset + 4, -1);
            if (module.gotSettings())
            Async.INSTANCE.getFontManager().getFont("Arial 23").drawString("+", parent.x + parent.width - 13, parent.y + offset + 5, -1);

            offset += 21;
        }

        if (parent.parent.renderingSettings)
            handleScrolling(mouseX, mouseY);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int offset = 25;
        for (Module module : Async.INSTANCE.getModuleManager().getModules(parent.category))
        {
            if (parent.parent.isInside(mouseX, mouseY, parent.x, parent.y + offset, parent.width, 21)) {
                if (mouseButton == 0 && !parent.parent.renderingSettings)
                module.toggle();
                if (mouseButton == 1 && !parent.parent.renderingSettings) {
                    if (module.gotSettings()) {
                        parent.parent.selectedModule = module;
                        parent.parent.components.clear();
                        int sOffset = 110;
                        int sOffset2 = 110;
                        for (Setting setting : module.getSettings()) {
                            if (setting instanceof ModeSetting) {
                                parent.parent.components.add(new ModeComp((ModeSetting) setting, sOffset, this));
                                sOffset += 21;
                            } else if (setting instanceof BooleanSetting) {
                                parent.parent.components.add(new BooleanComp((BooleanSetting) setting, sOffset, this));
                                sOffset += 21;
                            } else if(setting instanceof NumberSetting) {
                                parent.parent.components.add(new NumberComp((NumberSetting) setting, sOffset2, this));
                                sOffset2 += 38;
                            }
                        }
                        parent.parent.renderingSettings = true;
                    }
                }
                if (mouseButton == 2) {
                    bindingModule = module;
                    binding = true;
                }
            }
            offset += 21;
        }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (binding && bindingModule != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                binding = false;
                bindingModule = null;
                return;
            }
            bindingModule.setKey(keyCode);
            binding = false;
            bindingModule = null;
        }
    }

    private void handleScrolling(int mouseX, int mouseY) {
        if (Mouse.hasWheel() && parent.parent.isInside(mouseX, mouseY, RenderUtil.getScaledResolution().getScaledWidth() / 2 - 150, 105, 300, RenderUtil.getScaledResolution().getScaledHeight() - 200)) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.scrollOffset += 10;
                if (this.scrollOffset < 0) {
                    this.scrollOffset = 0;
                }
            } else if (wheel > 0) {
                this.scrollOffset -= 10;
                if (this.scrollOffset < 0) {
                    this.scrollOffset = 0;
                }
            }
        }
    }

}
