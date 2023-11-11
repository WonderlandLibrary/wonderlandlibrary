package me.nyan.flush.clickgui.sigma.component.components.settings;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.sigma.component.Component;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class BooleanComponent extends Component {
    private final BooleanSetting setting;
    private float animation;

    public BooleanComponent(BooleanSetting setting) {
        this.setting = setting;
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 26);
        animation += (setting.getValue() ? 1 : -1) * 0.01 * Flush.getFrameTime();
        animation = Math.min(Math.max(animation, 0), 1);

        font.drawString(setting.getName(), x, y + getFullHeight() / 2F - font.getFontHeight() / 2F, 0xFF000000);
        float rectX = x + getWidth() - 12;
        float rectY = y + getHeight() / 2 - 6;
        boolean hovered = MouseUtils.hovered(mouseX, mouseY, x + getWidth() - 10, y + getHeight() / 2 - 5, x + getWidth(), y + getHeight() / 2 + 5) && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1));
        RenderUtils.fillRoundRect(rectX, rectY, 12, 12, 5, hovered ? 0xFFD9D9D9 : 0xFFE4E4E4);
        if (animation > 0) {
            RenderUtils.fillRoundRect(rectX, rectY, 12, 12, 5,
                    ColorUtils.alpha(hovered ? 0xFF2495E5 : 0xFF29A6FF, (int) (255 * animation)));

            GL11.glLineWidth(2);
            RenderUtils.drawLine(rectX + 3, rectY + 6, rectX + 5, rectY + 8.5F, -1);
            RenderUtils.drawLine(rectX + 5, rectY + 8.5F, rectX + 9, rectY + 4, -1);
        }
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x + getWidth() - 12, y + getHeight() / 2 - 6, x + getWidth(), y + getHeight() / 2 + 6) && (button == 0 || button == 1)) {
            setting.setValue(!setting.getValue());
        }
    }

    @Override
    public float getWidth() {
        return 250 - 30;
    }

    @Override
    public float getHeight() {
        return 16;
    }
}
