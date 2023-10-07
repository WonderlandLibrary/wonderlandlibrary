package cc.novoline.gui.group2;

import java.util.Objects;

import static cc.novoline.utils.RenderUtils.drawRoundedRect;

/**
 * @author xDelsy
 */
public abstract class AbstractRoundedGroup extends AbstractGroup implements RoundedGroup {

    /* fields */
    protected int radius;

    /* constructors */
    public AbstractRoundedGroup(int radius, int color, int x, int y, int width, int height) {
        super(color, x, y, width, height);
        this.radius = radius;
    }

    /* methods */
    @Override
    public void onDraw(int mouseX, int mouseY) {
        drawRoundedRect(this.x, this.y, this.width, this.height, this.radius, this.color);
    }

    //region Lombok
    @Override
    public int getRadius() {
        return this.radius;
    }

    @Override
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractRoundedGroup)) return false;
        if (!super.equals(o)) return false;
        final AbstractRoundedGroup that = (AbstractRoundedGroup) o;
        return this.radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.radius);
    }

    @Override
    public String toString() {
        return "AbstractRoundedGroup{" + "radius=" + this.radius + ", color=" + this.color + ", width=" + this.width + ", height=" + this.height + ", visible=" + this.visible + ", x=" + this.x + ", y=" + this.y + '}';
    }
    //endregion

}
