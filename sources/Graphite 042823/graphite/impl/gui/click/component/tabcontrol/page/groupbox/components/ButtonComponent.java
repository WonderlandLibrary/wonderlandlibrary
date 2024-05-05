package graphite.impl.gui.click.component.tabcontrol.page.groupbox.components;

import graphite.api.util.render.RenderUtil;
import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;
import graphite.impl.gui.click.framework.Component;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public abstract class ButtonComponent extends Component {
    private final String label;

    public ButtonComponent(Component parent, String label, double width, double height) {
        super(parent, 0, 0, width, height);

        this.label = label;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        final TTFFontRenderer fontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("VERDANA_9");

        {
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(18, 18, 24).getRGB());
            RenderUtil.drawGradientRect(x + .5, y + .5, x + width - .5, y + height - .5, false, new Color(41, 41, 49).getRGB(), new Color(36, 36, 43).getRGB());
        }

        {
            fontRenderer.drawString(this.label, (float) (x - (fontRenderer.getWidth(label) / 2) + width / 2), (float) (y - (fontRenderer.getHeight(label) / 2) + height / 2) + 1F, -1);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0)
            onPress();
    }

    public abstract void onPress();
}
