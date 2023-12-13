package me.nyan.flush.clickgui.sigma.component.components.settings.mode;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.sigma.SigmaPanel;
import me.nyan.flush.clickgui.sigma.component.Component;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.client.gui.Gui;

import java.awt.Color;

public class ModeOptionComponent extends Component {
    private final ModeSetting setting;
    private int color = 0xFF121212;
    private int fontColor = 0xFFF2F2F2;
    private final String option;

    public ModeOptionComponent(ModeSetting setting, String option) {
        this.setting = setting;
        this.option = option;
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        int targetColor = setting.getValue().equals(option) ? 0xFFBB86FC : 0xFF121212;
        color = ColorUtils.animateColor(color, targetColor, 16);

        int targetFontColor = setting.getValue().equals(option) ? 0xFF121212 : 0xFFF2F2F2;
        fontColor = ColorUtils.animateColor(fontColor, targetFontColor, 16);

        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 16);

        Gui.drawRect(x, y, x + getWidth(), y + getFullHeight(), color);
        font.drawXYCenteredString(option, x + getWidth() / 2F, y + getFullHeight() / 2F, fontColor);
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y + 0.5, x + getWidth(), y + getFullHeight() - 0.5) && button == 0) {
            setting.setValue(option);
        }
    }

    @Override
    public float getWidth() {
        return SigmaPanel.WIDTH;
    }

    @Override
    public float getHeight() {
        return 14;
    }
}
