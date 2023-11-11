package me.nyan.flush.clickgui.flush.component.components.settings;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.flush.FlushPanel;
import me.nyan.flush.clickgui.flush.component.Component;
import me.nyan.flush.module.impl.render.ModuleClickGui;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class BooleanComponent extends Component {
    private final BooleanSetting setting;
    private final int i;
    private int color = 0xFF1E1E1E;
    private int fontColor = 0xFFF2F2F2;

    public BooleanComponent(BooleanSetting setting, int i) {
        this.setting = setting;
        this.i = i;
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        int targetColor = setting.getValue() ?
                Flush.getInstance().getModuleManager().getModule(ModuleClickGui.class).getClickGUIColor(i)
                : 0xFF1E1E1E;
        color = ColorUtils.animateColor(color, targetColor, 16);

        //int targetFontColor = setting.getValue() ? 0xFF1E1E1E : 0xFFF2F2F2;
        //fontColor = ColorUtils.animateColor(fontColor, targetFontColor, 16);
        fontColor = ColorUtils.contrast(color);

        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 18);

        Gui.drawRect(x, y, x + getWidth(), y + getFullHeight(), color);
        font.drawXYCenteredString(setting.getName(), x + getWidth() / 2F, y + getFullHeight() / 2F, fontColor);
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y + 0.5, x + getWidth(), y + getFullHeight() - 0.5) && button == 0) {
            setting.setValue(!setting.getValue());
        }
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
