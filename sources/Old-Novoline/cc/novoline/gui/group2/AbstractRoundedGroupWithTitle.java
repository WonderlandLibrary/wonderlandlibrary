package cc.novoline.gui.group2;

import cc.novoline.gui.label.Label;
import cc.novoline.utils.java.Checks;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

import static cc.novoline.utils.RenderUtils.drawRoundedRect;

/**
 * @author xDelsy
 */
public abstract class AbstractRoundedGroupWithTitle extends AbstractGroup implements RoundedGroupWithTitle {

    /* fields */
    @Nullable
    protected Label title;
    protected int radius;

    /* constructors */
    public AbstractRoundedGroupWithTitle(@Nullable Label title, int radius, int color, int x, int y, int width,
                                         int height) {
        super(color, x, y, width, height);
        setTitle(title);
        this.radius = radius;
    }

    /* methods */
    @Override
    public void onDraw(int mouseX, int mouseY) {
        drawRoundedRect(this.x, this.y, this.width, this.height, this.radius, this.color);

        if (this.title != null) {
            this.title.draw(mouseX, mouseY);
        }
    }

    public void updateTitlePosition() throws NullPointerException {
        Checks.notNull(this.title, "title");

        final int x = (int) (this.x + (this.width - this.title.getWidth()) / 2.0F), // @off
                y = this.y + 4; // @on

        this.title.setPosition(x, y);
    }

    //region Lombok
    @Nullable
    public Label getTitle() {
        return this.title;
    }

    public void setTitle(@Nullable Label title) {
        if (title != null) {
            this.title = title;
            updateTitlePosition();
        }
    }

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
        if (!(o instanceof AbstractRoundedGroupWithTitle)) return false;
        if (!super.equals(o)) return false;
        final AbstractRoundedGroupWithTitle that = (AbstractRoundedGroupWithTitle) o;
        return this.radius == that.radius && Objects.equals(this.title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.title, this.radius);
    }

    @Override
    public String toString() {
        return "AbstractRoundedGroupWithTitle{" + "title=" + this.title + ", radius=" + this.radius + ", color=" + this.color + ", width=" + this.width + ", height=" + this.height + ", visible=" + this.visible + ", x=" + this.x + ", y=" + this.y + '}';
    }
    //endregion

}
