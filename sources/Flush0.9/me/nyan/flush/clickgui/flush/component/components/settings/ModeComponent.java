package me.nyan.flush.clickgui.flush.component.components.settings;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.flush.FlushPanel;
import me.nyan.flush.clickgui.flush.component.Component;
import me.nyan.flush.clickgui.flush.component.components.settings.mode.ModeOptionComponent;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;

public class ModeComponent extends Component {
    private final ModeSetting setting;
    private final ArrayList<ModeOptionComponent> components = new ArrayList<>();
    private boolean expanded;
    private float expand;

    public ModeComponent(ModeSetting setting) {
        this.setting = setting;

        for (String option : this.setting.getOptions()) {
            components.add(new ModeOptionComponent(this.setting, option));
        }
    }

    @Override
    public void init() {
        for (Component component : components) {
            component.init();
        }
    }

    @Override
    public void update() {
        if (!expanded) {
            expand -= expand / 100F * Flush.getFrameTime();
            if (expand < 0.01) {
                expand = 0;
            }
        } else {
            expand += ((1 - expand) / 100F) * Flush.getFrameTime();
            if (expand > 0.99) {
                expand = 1;
            }
        }
        expand = Math.max(Math.min(expand, 1), 0);
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 18);

        Gui.drawRect(x, y, x + getWidth(), y + getHeight(), 0xFF1D1D1D);
        font.drawXYCenteredString(setting.getName(), x + getWidth() / 2F, y + getHeight() / 2F, 0xFFE8E8E8);

        if (expand == 0) {
            return;
        }

        float optionY = y + getHeight();
        for (Component component : components) {
            component.draw(x, optionY, mouseX, mouseY, partialTicks);
            optionY += component.getFullHeight();
        }
        RenderUtils.drawGradientRect(x, optionY - 10, x + getWidth(), optionY, 0x00000000, 0x88000000);
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y + 0.5, x + getWidth(), y + getHeight() - 0.5) &&
                button == 1) {
            expanded = !expanded;
        }

        if (expand != 1) {
            return;
        }

        float optionY = y + getHeight();
        for (Component component : components) {
            component.mouseClicked(x, optionY, mouseX, mouseY, button);
            optionY += component.getFullHeight();
        }
    }

    @Override
    public float getHeight() {
        return FlushPanel.TITLE_HEIGHT - 4;
    }

    @Override
    public float getFullHeight() {
        float height = 0;
        for (Component component : components) {
            height += component.getFullHeight();
        }
        return getHeight() + height * expand;
    }

    @Override
    public boolean show() {
        return setting.shouldShow();
    }
}
