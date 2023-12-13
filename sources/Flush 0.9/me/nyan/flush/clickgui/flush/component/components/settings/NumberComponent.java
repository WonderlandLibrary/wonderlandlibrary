package me.nyan.flush.clickgui.flush.component.components.settings;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.flush.FlushPanel;
import me.nyan.flush.clickgui.flush.component.Component;
import me.nyan.flush.module.impl.render.ModuleClickGui;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.client.gui.Gui;

public class NumberComponent extends Component {
    private final NumberSetting setting;
    private final int i;
    private boolean sliding;
    private double aPercentage;

    public NumberComponent(NumberSetting setting, int i) {
        this.setting = setting;
        this.i = i;
    }

    @Override
    public void init() {
        sliding = false;
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        double percentage = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());

        aPercentage -= (aPercentage - percentage) / 10F;

        int color = Flush.getInstance().getModuleManager().getModule(ModuleClickGui.class).getClickGUIColor(i);
        Gui.drawRect(x, y, x + getWidth(), y + getFullHeight(), 0xFF121212);
        Gui.drawRect(x, y, x + getWidth() * aPercentage, y + getFullHeight(), color);

        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 16);
        int stringWidth = font.getStringWidth(setting.getName());
        int currentWidth = 0;
        for (char ch : setting.getName().toCharArray()) {
            font.drawString(
                    String.valueOf(ch),
                    x + getWidth() / 2F - stringWidth / 2F + currentWidth,
                    y + getFullHeight() / 2F - font.getFontHeight() + 1,
                    (x + getWidth() / 2F - stringWidth / 2F + currentWidth + font.getStringWidth(String.valueOf(ch))
                            < x + getWidth() * aPercentage ? ColorUtils.contrast(color) : 0xFFF2F2F2)
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
                            < x + getWidth() * aPercentage ? ColorUtils.contrast(color) : 0xFFF2F2F2)
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
    public float getHeight() {
        return FlushPanel.TITLE_HEIGHT - 4;
    }

    @Override
    public boolean show() {
        return setting.shouldShow();
    }
}
