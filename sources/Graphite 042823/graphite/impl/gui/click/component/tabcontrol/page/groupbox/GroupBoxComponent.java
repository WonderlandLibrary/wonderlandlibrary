package graphite.impl.gui.click.component.tabcontrol.page.groupbox;

import java.awt.Color;
import graphite.api.util.render.RenderUtil;
import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;
import graphite.impl.gui.click.framework.Component;
import graphite.impl.gui.click.framework.Predicated;
import net.minecraft.client.gui.ScaledResolution;

public abstract class GroupBoxComponent extends Component {
    protected final double xMargin = getMarginX();
    protected final double yMargin = 6;
    
    private final String label;

    public double maxHeight;
    public int pageColumn;

    public GroupBoxComponent(Component parent, String label) {
        super(parent, 0, 0, 0, 0);
        
        this.label = label;
        
        this.onInit();
    }
    
    @Override
    public double getHeight() {
        double height = this.yMargin * 1.4;

        for (final Component component : this.getChildren()) {
            if (component instanceof Predicated) {
                final Predicated Predicated = (Predicated) component;
                if (!Predicated.isVisible()) continue;
            }

            height += component.getHeight() + 4;
        }

        return height;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        final TTFFontRenderer fontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("VERDANA_BOLD_9");
        
        final float length = fontRenderer.getWidth(this.label);

        {
        	RenderUtil.drawRect(x, y, x + width, y + height, new Color(18, 18, 26).getRGB());
            RenderUtil.drawBorderedRect(x + .5, y + .5, x + width - .5, y + height - .5, .5, new Color(27, 27, 34).getRGB(), new Color(51, 51, 58).getRGB());
        
            RenderUtil.drawRect(x + 5, y, x + 10 + length, y + 1, new Color(32, 32, 38).getRGB());
        }
        
        {
        	fontRenderer.drawStringWithShadow(this.label, (float) x + 7.5F, (float) y, new Color(205, 205, 205).getRGB());
        }

        double childOffsetY = this.yMargin;

        for (Component child : this.getChildren()) {
            if (child instanceof Predicated) {
                final Predicated Predicated = (Predicated) child;
                if (!Predicated.isVisible()) continue;
            }

            child.setX(this.xMargin);
            child.setY(childOffsetY);
            child.setWidth(width - this.xMargin * 2.0);

            child.onDraw(scaledResolution, mouseX, mouseY);

            childOffsetY += child.getHeight() + 4;
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        for (Component child : this.getChildren()) {
            if (child instanceof Predicated) {
                final Predicated Predicated = (Predicated) child;
                if (!Predicated.isVisible()) continue;
            }

            if (child.isHovered(mouseX, mouseY)) {
                child.onMouseClick(mouseX, mouseY , button);
                return;
            }
        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        if (super.isHovered(mouseX, mouseY)) return true;

        for (Component child : this.getChildren()) {
            if (child instanceof Predicated) {
                final Predicated Predicated = (Predicated) child;
                if (!Predicated.isVisible()) continue;
            }

            if (child.isHovered(mouseX, mouseY)) {
                return true;
            }
        }

        return false;
    }

    public abstract void onInit();

    public abstract double getMarginX();
}
