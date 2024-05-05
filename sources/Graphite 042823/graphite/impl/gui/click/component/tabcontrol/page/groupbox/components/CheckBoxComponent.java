package graphite.impl.gui.click.component.tabcontrol.page.groupbox.components;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

import graphite.api.util.render.ColourUtil;
import graphite.api.util.render.RenderUtil;
import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;
import graphite.impl.gui.click.component.Theme;
import graphite.impl.gui.click.framework.Component;
import graphite.impl.gui.click.framework.Predicated;
import net.minecraft.client.gui.ScaledResolution;

public final class CheckBoxComponent extends Component implements Predicated {

    private final String label;

    private final Consumer<Boolean> setter;
    private final Supplier<Boolean> getter;
    private final Supplier<Boolean> dependency;

    private final double boxSize = 4.0;
    private final double textXOffset = 3.5;

    public CheckBoxComponent(Component parent, String label, Supplier<Boolean> getter, Consumer<Boolean> setter, Supplier<Boolean> dependency) {
        super(parent, 0, 0, 0, 4);

        this.label = label;

        this.getter = getter;
        this.setter = setter;
        this.dependency = dependency;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double height = this.getHeight();

        final TTFFontRenderer fontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("VERDANA_9");
        
        {
        	RenderUtil.drawBorderedRect(x, y, x + boxSize, y + height, .5, new Color(75, 75, 86).getRGB(), new Color(18, 18, 24).getRGB());
        	
        	if (getter.get())
            	RenderUtil.drawGradientRect(x + .5, y + .5, x + boxSize - .5, y + height - .5, false, Theme.getUIColor(), ColourUtil.darker(Theme.getUIColor(), 0.65));
        }
        
        {
        	fontRenderer.drawStringWithShadow(this.label, (float) (x + boxSize + textXOffset), (float) y + 1, new Color(205, 205, 205).getRGB());
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0) this.setter.accept(!this.getter.get());
    }

    @Override
    public boolean isVisible() {
        return this.dependency != null ? this.dependency.get() : true;
    }
}