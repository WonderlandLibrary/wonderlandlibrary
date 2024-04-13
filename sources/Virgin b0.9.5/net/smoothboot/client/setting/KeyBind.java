package net.smoothboot.client.setting;

import net.minecraft.client.gui.DrawContext;
import net.smoothboot.client.hud.modbutton;
import net.smoothboot.client.module.settings.KeyBindSetting;
import net.smoothboot.client.module.settings.Setting;

import java.awt.*;

public class KeyBind extends Component {

    private KeyBindSetting binding = (KeyBindSetting)setting;
    public boolean isBinding = false;

    public KeyBind(Setting setting, modbutton parent, int offset) {
        super(setting, parent, offset);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0 && parent.extended) {
            binding.toggle();
            isBinding = true;
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int key) {
        if (isBinding) {
            parent.module.setKey(key);
            binding.setKey(key);
            isBinding = false;
        }
        if ((binding.getKey() == 256)) {
            parent.module.setKey(0);
            binding.setKey(0);
            isBinding = false;
        }
        if ((binding.getKey() == 259)) {
            parent.module.setKey(0);
            binding.setKey(0);
            isBinding = false;
        }
        super.keyPressed(key);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        context.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, new Color(0, 0, 0, 160).getRGB());

        int offsetY = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);

        if (!isBinding && binding.getKey() == 0) context.drawText(mc.textRenderer, "> Keybind: None", parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, new Color(255, 255, 255, 220).getRGB(), true);
        else if (!isBinding && binding.getKey() != 342 && binding.getKey() != 343 && binding.getKey() != 344 && binding.getKey() != 345) context.drawText(mc.textRenderer, "> Keybind: " + (char) binding.getKey(), parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, new Color(255, 255, 255, 220).getRGB(), true);
        if (!isBinding && binding.getKey() == 342) context.drawText(mc.textRenderer, "> Keybind: LALT", parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, new Color(255, 255, 255, 220).getRGB(), true);
        if (!isBinding && binding.getKey() == 344) context.drawText(mc.textRenderer, "> Keybind: RSHIFT", parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, new Color(255, 255, 255, 220).getRGB(), true);
        if (!isBinding && binding.getKey() == 345) context.drawText(mc.textRenderer, "> Keybind: RCTRL", parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, new Color(255, 255, 255, 220).getRGB(), true);
        if (isBinding) context.drawText(mc.textRenderer, "> Press key: ", parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, new Color(255, 255, 255, 220).getRGB(), true);

        super.render(context, mouseX, mouseY, delta);
    }
}