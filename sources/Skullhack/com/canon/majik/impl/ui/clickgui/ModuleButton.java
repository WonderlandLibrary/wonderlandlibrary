package com.canon.majik.impl.ui.clickgui;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.api.utils.render.animate.Animation;
import com.canon.majik.api.utils.render.animate.Easing;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.modules.impl.client.ClickGui;
import com.canon.majik.impl.setting.Setting;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.ColorSetting;
import com.canon.majik.impl.setting.settings.ModeSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;
import com.canon.majik.impl.ui.clickgui.item.Item;
import com.canon.majik.impl.ui.clickgui.item.items.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.List;

public class ModuleButton {
    private final Module module;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Minecraft mc;
    private int offset;
    private boolean open;
    private final List<Item<?>> items = new ArrayList<>();
    Animation animation = new Animation(500, false, Easing.LINEAR);

    public ModuleButton(Module module, int x, int y, int width, int height, Minecraft mc) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mc = mc;

        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof BooleanSetting)
                items.add(new ItemBoolean((BooleanSetting) setting, x, y, width, height));
            else if (setting instanceof NumberSetting)
                items.add(new ItemNumber((NumberSetting) setting, x, y, width, height));
            else if (setting instanceof ColorSetting)
                items.add(new ItemColor((ColorSetting) setting, x, y, width, height));
            else if (setting instanceof ModeSetting)
                items.add(new ItemMode((ModeSetting) setting, x, y, width, height));
        }

        items.add(new ItemKeyBind(module, x, y, width, height));
    }

    public int drawScreen(int mouseX, int mouseY, float partialTicks, int offset) {
        this.offset = offset;
        int y = this.y + offset;
        RenderUtils.rect(x, y, width, height, this.module.isEnabled() ? ClickGui.instance.color.getValue().getRGB() : 0x80000000);
        if(ClickGui.instance.cfont.getValue()){
            Initializer.CFont.drawStringWithShadow(module.getName(), x + 3, y + (height / 2f - Initializer.CFont.getHeight() / 2f), -1);
        }else {
            mc.fontRenderer.drawStringWithShadow(module.getName(), x + 3, y + (height / 2f - mc.fontRenderer.FONT_HEIGHT / 2f), -1);
        }

        animation.setState(open);
        int offsets = (int) (height);
        if(open || animation.getAnimationFactor() > 0) {
            for (Item<?> item : items) {
                offsets += item.drawScreen(mouseX, mouseY, partialTicks, offsets + offset) * animation.getAnimationFactor();
            }
        }
        return offsets;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (bounding(mouseX, mouseY)){
            if (mouseButton == 0) {
                this.module.toggle();
            } else if (mouseButton == 1) {
                this.open = !this.open;
            }
        }

        if (open)
            items.forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (open)
            items.forEach(item -> item.keyTyped(typedChar, keyCode));
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (open)
            items.forEach(item -> item.mouseReleased(mouseX, mouseY, state));
    }

    public boolean bounding(int mouseX, int mouseY) {
        if (mouseX < this.x) return false;
        if (mouseX > this.x + this.width) return false;
        if (mouseY < this.y + this.offset) return false;
        return mouseY <= this.y + this.offset + this.height;
    }
}
