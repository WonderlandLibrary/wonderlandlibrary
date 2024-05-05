package io.github.nevalackin.client.ui.nl.components.page.groupBox;

import io.github.nevalackin.client.ui.framework.Animated;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.ui.framework.Predicated;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.glTranslated;

public abstract class GroupBoxComponent extends Component implements Animated {

    protected final double xMargin = 3;
    protected final double yMargin = 2;

    public double maxHeight;
    public int pageColumn;

    private double scrollVelocity;
    public int scrollOffset;

    public GroupBoxComponent(Component parent) {
        super(parent, 0, 0, 0, 0);

        this.onInit();
    }

    @Override
    public double getHeight() {
        double height = this.yMargin * 2.0;

        for (final Component component : this.getChildren()) {
            if (component instanceof Predicated) {
                final Predicated Predicated = (Predicated) component;
                if (!Predicated.isVisible()) continue;
            }

            height += component.getHeight();
        }

        return height;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double scrollableHeight = this.getHeight();
        final double viewableHeight = Math.min(scrollableHeight, this.maxHeight);

        final int groupBoxBackground = this.getTheme().getGroupBoxBackgroundColour();

        // Draw background
        {
            DrawUtil.glDrawRoundedRect(x, y, width, viewableHeight, DrawUtil.RoundingMode.FULL,
                                       4.f, scaledResolution.getScaleFactor(), groupBoxBackground);
        }

        double childOffset = this.yMargin;

        final boolean scrollable = scrollableHeight > viewableHeight;

        if (scrollable) {
            DrawUtil.glScissorBox(x, y, width, viewableHeight, scaledResolution);

            if (this.isHovered(mouseX, mouseY)) {
                final double scrollAmount = -Mouse.getDWheel() / 50.0;

                if (scrollAmount != 0) {
                    this.scrollVelocity += scrollAmount;
                }
            }

            this.scrollVelocity *= 0.98;

            this.scrollOffset = (int) Math.round(Math.max(0, Math.min(scrollableHeight - viewableHeight, this.scrollOffset + this.scrollVelocity)));

            glTranslated(0, -this.scrollOffset, 0);
        } else {
            this.scrollOffset = 0;
            this.scrollVelocity = 0;
        }

        for (Component child : this.getChildren()) {
            if (child instanceof Predicated) {
                final Predicated Predicated = (Predicated) child;
                if (!Predicated.isVisible()) continue;
            }

            child.setX(this.xMargin);
            child.setY(childOffset);
            child.setWidth(width - this.xMargin * 2.0);

            child.onDraw(scaledResolution, mouseX, mouseY + this.scrollOffset);

            childOffset += child.getHeight();
        }

        if (scrollable) {
            DrawUtil.glEndScissor();

            if (this.scrollOffset < scrollableHeight - viewableHeight) {
                DrawUtil.glDrawFilledQuad(x, y + viewableHeight - 20, width, 20,
                                          ColourUtil.removeAlphaComponent(groupBoxBackground),
                                          groupBoxBackground);
            }

            glTranslated(0, this.scrollOffset, 0);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        for (Component child : this.getChildren()) {
            if (child instanceof Predicated) {
                final Predicated Predicated = (Predicated) child;
                if (!Predicated.isVisible()) continue;
            }

            if (child.isHovered(mouseX, mouseY + this.scrollOffset)) {
                child.onMouseClick(mouseX, mouseY + this.scrollOffset, button);
                return;
            }
        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        if (super.isHovered(mouseX, mouseY + this.scrollOffset)) return true;

        for (Component child : this.getChildren()) {
            if (child instanceof Predicated) {
                final Predicated Predicated = (Predicated) child;
                if (!Predicated.isVisible()) continue;
            }

            if (child.isHovered(mouseX, mouseY + this.scrollOffset)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void resetAnimationState() {
        this.resetChildrenAnimations();
    }

    public abstract void onInit();
}
