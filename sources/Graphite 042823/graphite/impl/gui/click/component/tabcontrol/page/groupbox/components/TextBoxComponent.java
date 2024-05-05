package graphite.impl.gui.click.component.tabcontrol.page.groupbox.components;

import graphite.api.util.render.RenderUtil;
import graphite.impl.gui.click.framework.Component;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class TextBoxComponent extends Component {
    private final Consumer<String> setter;
    private final Supplier<String> getter;

    public TextBoxComponent(Component parent, Supplier<String> getter, Consumer<String> setter, double width, double height) {
        super(parent, 0, 0, width, height);

        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        {
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(18, 18, 24).getRGB());
            RenderUtil.drawGradientRect(x + .5, y + .5, x + width - .5, y + height - .5, false, new Color(41, 41, 51).getRGB(), new Color(36, 36, 44).getRGB());
        }
    }
}
