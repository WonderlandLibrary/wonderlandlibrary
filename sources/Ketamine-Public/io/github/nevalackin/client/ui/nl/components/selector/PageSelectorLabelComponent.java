package io.github.nevalackin.client.ui.nl.components.selector;

import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.core.KetamineClient;
import net.minecraft.client.gui.ScaledResolution;

public final class PageSelectorLabelComponent extends Component implements PageSelector<PageSelectorComponent> {

    private final String label;

    public PageSelectorLabelComponent(Component parent, final String label, double x, double y, double width, double height) {
        super(parent, x, y, width, height);

        this.label = label;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        // Draw label
        {
            final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

            fontRenderer.draw(this.label, x + 4, y + height / 2.0 - fontRenderer.getHeight(this.label) / 2.0, 0.225, this.getTheme().getPageSelectorLabelColour());
        }

        super.onDraw(scaledResolution, mouseX, mouseY);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {

    }

    @Override
    public void onMouseRelease(int button) {

    }

    @Override
    public void onKeyPress(int keyCode) {

    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return false;
    }
}
