package io.github.nevalackin.client.ui.nl.components.selector;

import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.ui.cfont.StaticallySizedImage;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;

public final class PageButtonComponent extends Component implements PageSelector<PageSelectorLabelComponent> {

    private final String label;
    private final StaticallySizedImage icon;

    private double selectedFadeInProgress;
    private final int idx;

    public PageButtonComponent(Component parent, String label, StaticallySizedImage icon, final int idx, double x, double y, double width, double height) {
        super(parent, x, y, width, height);

        this.label = label;
        this.icon = icon;
        this.idx = idx;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        this.selectedFadeInProgress = DrawUtil.animateProgress(this.selectedFadeInProgress, this.idx == this.getSelectedIdx() ? 1.0 : 0.0, 4);

        // Draw selector box
        {
            DrawUtil.glDrawRoundedRect(x, y, width, height, DrawUtil.RoundingMode.FULL, 4.f, scaledResolution.getScaleFactor(),
                                       // TODO :: Get alpha from colour
                                       ColourUtil.overwriteAlphaComponent(this.getTheme().getPageSelectorSelectedPageColour(), (int) (0xFF * DrawUtil.bezierBlendAnimation(this.selectedFadeInProgress))));
        }

        // Draw label & icon
        {
            // Note :: Must be an even number
            final int iconSize = 12;
            this.icon.draw(x + 4, y + height / 2.0 - iconSize / 2.0, iconSize, iconSize, this.getTheme().getMainColour());

            final CustomFontRenderer cFontRenderer = KetamineClient.getInstance().getFontRenderer();

            cFontRenderer.draw(this.label, x + 4 + iconSize + 4, y + height / 2.0 - cFontRenderer.getHeight(this.label) / 2.0, this.getTheme().getHighlightedTextColour());
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && this.isHovered(mouseX, mouseY)) {
            this.onPageSelect(this.idx, this.getY() + this.getHeight() / 2.0);
        }
    }
}
