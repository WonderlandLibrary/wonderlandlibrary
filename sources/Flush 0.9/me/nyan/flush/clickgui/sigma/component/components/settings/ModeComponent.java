package me.nyan.flush.clickgui.sigma.component.components.settings;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.sigma.SigmaPanel;
import me.nyan.flush.clickgui.sigma.component.Component;
import me.nyan.flush.clickgui.sigma.component.components.settings.mode.ModeOptionComponent;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.Stencil;
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
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        expand += (expanded ? 1 : -1) * 0.005F * Flush.getFrameTime();
        expand = Math.max(Math.min(expand, 1), 0);

        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 18);

        Gui.drawRect(x, y, x + getWidth(), y + getHeight(), 0xFF1D1D1D);
        font.drawXYCenteredString(setting.getName(), x + getWidth() / 2F, y + getHeight() / 2F, 0xFFE8E8E8);

        if (expand == 0) {
            return;
        }

        float optionY = y + getHeight();
        float height = 0;
        for (Component component : components) {
            height += component.getFullHeight();
        }

        Stencil.setup();
        Gui.drawRect(x, optionY - 1, x + getWidth(), optionY + (height + 1) * expand, 0);
        Stencil.draw();
        for (Component component : components) {
            component.draw(x, optionY, mouseX, mouseY, partialTicks);
            optionY += component.getFullHeight();
        }
        Stencil.finish();
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y + 0.5, x + getWidth(), y + getHeight() - 0.5) &&
                button == 1) {
            expanded = !expanded;
        }

        if (!expanded || expand != 1) {
            return;
        }

        float optionY = y + getHeight();
        for (Component component : components) {
            component.mouseClicked(x, optionY, mouseX, mouseY, button);
            optionY += component.getFullHeight();
        }
    }

    @Override
    public float getWidth() {
        return SigmaPanel.WIDTH;
    }

    @Override
    public float getHeight() {
        return 16;
    }

    @Override
    public float getFullHeight() {
        float height = 0;
        for (Component component : components) {
            height += component.getFullHeight();
        }
        return getHeight() + height * expand;
    }
}
