package net.smoothboot.client.setting;

import net.minecraft.client.gui.DrawContext;
import net.smoothboot.client.hud.modbutton;
import net.smoothboot.client.module.settings.NumberSetting;
import net.smoothboot.client.module.settings.Setting;

import java.awt.*;

import static net.smoothboot.client.hud.frame.*;

public class slider extends Component {

    public NumberSetting numSet = (NumberSetting)setting;

    private boolean sliding = false;

    public slider(Setting setting, modbutton parent, int offset) {
        super(setting, parent, offset);
        this.numSet = (NumberSetting)setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + parent.parent.height + offset, new Color(0, 0, 0, 130).getRGB());
        double diff = Math.min(parent.parent.width, Math.max(0, mouseX - parent.parent.x));
        int renderWidth = (int)(parent.parent.width * (numSet.getValue()- numSet.getMin()) / (numSet.getMax() - numSet.getMin()));
        context.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + renderWidth, parent.parent.y + parent.offset + parent.parent.height + offset, new Color(menured, menugreen, menublue, 130).getRGB());
        if (sliding) {
            if (diff == 0) {
                numSet.setValue(numSet.getMin());
            } else {
                numSet.setValue(((diff / parent.parent.width) * (numSet.getMax() - numSet.getMin()) + numSet.getMin()));
            }
        }
        int textOffset = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
        context.drawText(mc.textRenderer, "> " + numSet.getName() + ": " + numSet.getValue(), parent.parent.x + textOffset, parent.parent.y + parent.offset + offset + textOffset, new Color(255, 255, 255, 220).getRGB(), true);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0) {
            sliding = true;
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        sliding = false;
        super.mouseReleased(mouseX, mouseY, button);
    }
}