package net.smoothboot.client.setting;

import net.minecraft.client.gui.DrawContext;
import net.smoothboot.client.hud.modbutton;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.Setting;

import java.awt.*;

public class checkbox extends Component {

    public Setting setting;

    private BooleanSetting boolSet = (BooleanSetting) setting;

    public checkbox(Setting setting, modbutton parent, int offset) {
        super(setting, parent, offset);
        this.boolSet = (BooleanSetting) setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, new Color(0, 0, 0, 160).getRGB());
        int textOffset = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
        context.drawText(mc.textRenderer, "> " + boolSet.getName() + ": " + (boolSet.isEnabled() ? "On" : "Off"), parent.parent.x + textOffset, parent.parent.y + parent.offset + offset + textOffset, new Color(255, 255, 255, 220).getRGB(), true);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (parent.extended) {
            if (isHovered(mouseX, mouseY) && button == 0) {
                boolSet.toggle();
            }
            super.mouseClicked(mouseX, mouseY, button);
        }
    }
}