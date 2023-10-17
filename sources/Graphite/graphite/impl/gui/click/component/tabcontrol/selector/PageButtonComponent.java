package graphite.impl.gui.click.component.tabcontrol.selector;

import graphite.api.util.render.RenderUtil;
import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;
import graphite.impl.gui.click.component.Theme;
import graphite.impl.gui.click.framework.Component;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public final class PageButtonComponent extends Component implements PageSelector<PageSelectorComponent> {
    private final String label;
    private final int idx;

    public PageButtonComponent(Component parent, String label, final int idx, double x, double y, double width, double height) {
        super(parent, x, y, width, height);

        this.label = label;
        this.idx = idx;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();
        
        final boolean selected = idx == getSelectorParent().getSelectedIdx();

        final TTFFontRenderer fontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("VERDANA_9");

        {
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(56, 56, 69).getRGB());
            RenderUtil.drawGradientRect(x + .5, y + .5, x + width - .5, y + height - .5, false, selected ? new Color(55, 55, 64).getRGB() : new Color(47, 47, 56).getRGB(), selected ? new Color(36, 36, 43).getRGB() : new Color(24, 24, 28).getRGB());
            
            if (selected)
                RenderUtil.drawRect(x + .5, y + height - 1, x + width - .5, y + height - .5, Theme.getUIColor());
        }

        {
            fontRenderer.drawString(this.label, (float) (x - (fontRenderer.getWidth(label) / 2) + width / 2), (float) (y - (fontRenderer.getHeight(label) / 2) + height / 2), new Color(205, 205, 205).getRGB());
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && this.isHovered(mouseX, mouseY)) {
            this.onPageSelect(this.idx);
        }
    }
}