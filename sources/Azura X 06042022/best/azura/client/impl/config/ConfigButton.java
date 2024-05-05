package best.azura.client.impl.config;

import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.util.render.RenderUtil;

import java.awt.*;

public class ConfigButton extends ButtonImpl {

    public boolean lastClickedState;
    public Config config;

    public ConfigButton(String text, int x, int y, int width, int height) {
        super(text, x, y, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        this.hovered = RenderUtil.INSTANCE.isHoveredScaled(x, y, width, height, mouseX, mouseY, 1.0);
        Color color = lastClickedState ? hovered ? hoverColor.brighter().brighter() : hoverColor.brighter() : hovered ? hoverColor : normalColor;
        RenderUtil.INSTANCE.drawRoundedRect(x, y, width*animation, height * animation, roundness*animation, selected ? RenderUtil.INSTANCE.modifiedAlpha(color, 100) : color);
        Fonts.INSTANCE.arial15bold.drawString(text, x, y + height / 2.0 - Fonts.INSTANCE.arial15bold.FONT_HEIGHT / 2.0 - Fonts.INSTANCE.arial12.FONT_HEIGHT / 3.0 - 2,
                new Color(255, 255, 255, (int) (255 * animation)).getRGB());
        Fonts.INSTANCE.arial12.drawString(description, x, y + height / 2.0 + Fonts.INSTANCE.arial12.FONT_HEIGHT / 3.0 - 2, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
    }

    @Override
    public void mouseClicked() {
        if (!hovered) {
            lastClickedState = false;
            return;
        }
        if (lastClickedState) {
            if (config != null) {
                if (config.load()) {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Loaded config " + config.getName() + ".", 3000, Type.INFO));
                } else {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Config System", "Couldn't load config " + config.getName() + ".", 3000, Type.ERROR));
                }
                lastClickedState = false;
            }
        } else lastClickedState = true;
    }
}
