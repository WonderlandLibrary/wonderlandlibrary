package me.nyan.flush.clickgui.sigma.component.components.settings;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.sigma.SigmaPanel;
import me.nyan.flush.clickgui.sigma.component.Component;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import net.minecraft.client.gui.Gui;

import java.awt.Color;

public class NumberComponent extends Component {
    private final NumberSetting setting;
    private boolean sliding;
    private double aPercentage;

    public NumberComponent(NumberSetting setting) {
        this.setting = setting;
    }

    @Override
    public void init() {
        sliding = false;
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        double percentage = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());

        if (aPercentage != percentage) {
            if (aPercentage < percentage) {
                aPercentage += 0.005F * Flush.getFrameTime();
                if (aPercentage > percentage) {
                    aPercentage = percentage;
                }
            }

            if (aPercentage > percentage) {
                aPercentage -= 0.005F * Flush.getFrameTime();
                if (aPercentage < percentage) {
                    aPercentage = percentage;
                }
            }

            aPercentage = Math.max(Math.min(aPercentage, 1), 0);
        }

        Gui.drawRect(x, y, x + getWidth(), y + getFullHeight(), 0xFF121212);
        Gui.drawRect(x, y, x + getWidth() * aPercentage, y + getFullHeight(), 0xFFBB86FC);

        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 16);
        int stringWidth = font.getStringWidth(setting.getName());
        int currentWidth = 0;
        for (char ch : setting.getName().toCharArray()) {
            font.drawString(
                    String.valueOf(ch),
                    x + getWidth() / 2F - stringWidth / 2F + currentWidth,
                    y + getFullHeight() / 2F - font.getFontHeight() + 1,
                    (x + getWidth() / 2F - stringWidth / 2F + currentWidth + font.getStringWidth(String.valueOf(ch))
                            < x + getWidth() * aPercentage ? 0xFF121212 : 0xFFF2F2F2)
            );
            currentWidth += font.getStringWidth(String.valueOf(ch));
        }

        String stringValue = (setting.getValue() % 1.0 == 0.0 ? String.valueOf(setting.getValue()).replace(".0", "") :
                String.valueOf(setting.getValue()));
        int stringWidth2 = font.getStringWidth(stringValue);
        int currentWidth2 = 0;
        for (char ch : stringValue.toCharArray()) {
            font.drawString(
                    String.valueOf(ch),
                    x + getWidth() / 2F - stringWidth2 / 2F + currentWidth2,
                    y + getFullHeight() / 2F - 1,
                    (x + getWidth() / 2F - stringWidth2 / 2F + currentWidth2 + font.getStringWidth(String.valueOf(ch))
                            < x + getWidth() * aPercentage ? 0xFF121212 : 0xFFF2F2F2)
            );
            currentWidth2 += font.getStringWidth(String.valueOf(ch));
        }

        if (sliding) {
            setting.setValue(setting.getMin() + ((mouseX - x) / getWidth() * (setting.getMax() - setting.getMin())));
        }
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y, x + getWidth(), y + getHeight()) && button == 0) {
            sliding = true;
        }
    }

    @Override
    public void mouseReleased(float x, float y, int mouseX, int mouseY, int button) {
        sliding = false;
    }

    @Override
    public float getWidth() {
        return SigmaPanel.WIDTH;
    }

    @Override
    public float getHeight() {
        return 16;
    }
}
