package io.github.nevalackin.client.ui.nl.components;

import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.ui.nl.components.buttons.SaveConfigButtonComponent;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;

public final class HeaderComponent extends Component {

    public HeaderComponent(Component parent) {
        super(parent, 0, 0, 0, 40);

        final double margin = this.getHeight() / 2.0 - 16 / 2.0;

        this.addChild(new SaveConfigButtonComponent(this, null, margin, margin, 75, 16));
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        // TODO :: Header background

        // Draw bottom line
        {
            final double lineThickness = 1;

            DrawUtil.glDrawFilledQuad(x, y + height - lineThickness, width, lineThickness, this.getTheme().getHeaderPageSeparatorColour());
        }

        super.onDraw(scaledResolution, mouseX, mouseY);
    }
}
