package graphite.impl.gui.click.component.tabcontrol.page.groupbox.components;

import graphite.api.util.render.ColourUtil;
import graphite.api.util.render.RenderUtil;
import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;
import graphite.impl.gui.click.framework.Component;
import graphite.impl.gui.click.framework.Predicated;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ColorPickerComponent extends Component implements Predicated {
    private final String label;

    private final Consumer<Integer> setter;
    private final Supplier<Integer> getter;

    private final Supplier<Boolean> dependency;

    private boolean expanded;

    public ColorPickerComponent(Component parent, String label, Supplier<Integer> getter, Consumer<Integer> setter, Supplier<Boolean> dependency) {
        super(parent, 0, 0, 0, 4.5);

        this.label = label;
        this.getter = getter;
        this.setter = setter;
        this.dependency = dependency;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        final TTFFontRenderer fontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("VERDANA_9");

        //final int colorAlpha = getter.get() >> 24 & 0xFF;

        {
            RenderUtil.drawRect(x + width - 8, y, x + width, y + height, new Color(18, 18, 26).getRGB());
            RenderUtil.drawGradientRect(x + width - 7.5, y + .5, x + width - .5, y + height - .5, false, getter.get(), ColourUtil.darker(getter.get(), 0.65));
        }

        {
            fontRenderer.drawStringWithShadow(this.label, (float) x - .5F, (float) y + 1.5F, new Color(205, 205, 205).getRGB());
        }
    }

    @Override
    public boolean isVisible() {
        return this.dependency != null ? this.dependency.get() : true;
    }
}
