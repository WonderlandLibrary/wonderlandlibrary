package graphite.impl.gui.click.component.tabcontrol.page.groupbox.components;

import graphite.api.config.Config;
import graphite.api.util.render.RenderUtil;
import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;
import graphite.impl.gui.click.component.Theme;
import graphite.impl.gui.click.framework.Component;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.Collection;
import java.util.function.Supplier;

public class ListBoxComponent extends Component {
    private final Supplier<Collection<?>> getter;

    private boolean elementHovered, elementSelected;

    public ListBoxComponent(Component parent, Supplier<Collection<?>> getter) {
        super(parent, 0, 0, 0, 0);

        this.getter = getter;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        final TTFFontRenderer fontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("VERDANA_9");

        final double valuesXPos = x + 1.5;
        double valuesYPos = y + 4.5;

        {
            RenderUtil.drawBorderedRect(x, y, x + width, y + height, .5, new Color(40, 40, 50).getRGB(), new Color(18, 18, 24).getRGB());
        }

        {
            for (Object obj : getter.get()) {
                Config config = (Config) obj;
                elementHovered = mouseWithinBounds(mouseX, mouseY, valuesXPos, valuesYPos - 3, valuesXPos + width - 3, valuesYPos + 4);

                if (elementHovered)
                    RenderUtil.drawRect(valuesXPos, valuesYPos - 3, valuesXPos + width - 3, valuesYPos + 4, new Color(54, 54, 64).getRGB());

                fontRenderer.drawString(config.getName(), (float) valuesXPos + 1.5F, (float) valuesYPos, elementSelected ? Theme.getUIColor() : new Color(201, 201, 218).getRGB());
                valuesYPos += 7;
            }
        }
    }

    public static  boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= width) && (mouseY >= y && mouseY <= height);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && elementHovered) {
            elementSelected = true;
        }
    }
}
