package cc.novoline.gui.group2;

import cc.novoline.gui.AbstractElementWithBody;

import java.util.Objects;

/**
 * @author xDelsy
 */
public abstract class AbstractGroup extends AbstractElementWithBody implements Group {

    /* fields */
    protected int color;

    /* constructors */
    public AbstractGroup(int color, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.color = color;
    }

    /* methods */
    //region Lombok
    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractGroup)) return false;
        if (!super.equals(o)) return false;
        final AbstractGroup that = (AbstractGroup) o;
        return this.color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.color);
    }

    @Override
    public String toString() {
        return "AbstractGroup{" + "color=" + this.color + '}';
    }
    //endregion

}
