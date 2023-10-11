package markgg.ui.click.astolfo.buttons;

import markgg.modules.Module;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

public class ModuleButton extends Button{

    public ArrayList<Button> buttons = new ArrayList<>();

    public Module module;
    public Color color;

    public boolean hovered;

    public float finalHeight;

    public ModuleButton(float x, float y, float width, float height, Module mod, Color color) {
        super(x, y, width, height);

        module = mod;
        this.color = color;

        final float startModuleHeight = y + height;

        int count = 0;
        for(Setting setting : mod.getSettings()) {
            if(setting instanceof ModeSetting)
                buttons.add(new ModeButton(x, startModuleHeight + 18 * count, width, 9f, (ModeSetting) setting, color));
            if(setting instanceof BooleanSetting)
                buttons.add(new BooleanButton(x, startModuleHeight + 18 * count, width, 9f, (BooleanSetting) setting, color));
            if(setting instanceof NumberSetting)
                buttons.add(new NumberButton(x, startModuleHeight + 18 * count, width, 9f, (NumberSetting) setting, color));
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        Gui.drawRect(x, y, x + width, y + height, 0xff181A17);

        if(module.expanded)
            Gui.drawRect(x + 2, y, x + width - 2, y + height, 0xff181A17);
        else
            Gui.drawRect(x + 2, y, x + width - 2, y + height, module.toggled ? hovered ? color.darker().getRGB() : color.getRGB() : hovered ? new Color(0xff232623).darker().getRGB() : 0xff232623);

        Minecraft.getMinecraft().fontRendererObj.drawHeightCenteredString(module.getName().toLowerCase(Locale.ROOT),
                x + width - Minecraft.getMinecraft().fontRendererObj.getStringWidth(module.getName().toLowerCase(Locale.ROOT)) - 4, y + height / 2 + 1.5f, module.expanded ? module.isEnabled() ? color.getRGB() : 0xffffffff : 0xffffffff);

        if(module.keyCode != 0 && !module.listening) {
            Minecraft.getMinecraft().fontRendererObj.drawHeightCenteredString("[" + Keyboard.getKeyName(module.getKey()) + "]".toUpperCase(), x + width - 95, y + height / 2 + 1.5f, new Color(73, 75, 85).getRGB());
        }else if(module.listening) {
            Minecraft.getMinecraft().fontRendererObj.drawHeightCenteredString("[...]".toUpperCase(), x + width - 95, y + height / 2 + 1.5f, new Color(73, 75, 85).getRGB());
        }

        float sexyMethod = 0;
        int count = 0;

        if(module.expanded) {
            final float startY = y + height;
            for(Button button : buttons) {
                button.x = x;
                button.y = startY + button.height * count;
                button.draw(mouseX, mouseY);
                count++;
                sexyMethod = button.height;
            }
        }

        finalHeight = sexyMethod * count + height;
    }

    @Override
    public void action(int mouseX, int mouseY, boolean click, int button) {
        if(isHovered(mouseX, mouseY) && click) {
            if(button == 0) {
                module.toggle();
            }else if(button == 1) {
                module.expanded = !module.expanded;
            }else {
                module.listening = !module.listening;
            }
        }
    }

    @Override
    public void key(char typedChar, int key) {
        if (module.listening) {
            this.module.keyCode = key;

            if(key == 1){
                module.keyCode = 0;
            }

            module.listening = false;
        }
    }
}
