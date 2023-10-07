package cc.novoline.gui;

import java.util.Objects;

/**
 * @author xDelsy
 */
public abstract class AbstractElementWithBody extends AbstractElement implements ElementWithBody {

    /* fields */
    protected int width, height;

    /* constructors */
    public AbstractElementWithBody(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    /* methods */
    @Override
    public boolean isHovered(int mouseX, int mouseY) { // @off
        // noinspection UnnecessaryLocalVariable
        final boolean b = mouseX >= this.x && mouseX <= this.x + this.width &&
                mouseY >= this.y && mouseY <= this.y + this.height; // @on

        // if(b) RenderUtils.drawBorderedRect(x, y, x + width, y + height, 1, 0xFFFF0000, 0);

        return b;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    //region Lombok
    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractElementWithBody)) return false;
        if (!super.equals(o)) return false;
        final AbstractElementWithBody that = (AbstractElementWithBody) o;
        return this.width == that.width && this.height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.width, this.height);
    }

    @Override
    public String toString() {
        return "AbstractDrawableWithBody{" + "width=" + this.width + ", height=" + this.height + ", visible=" + this.visible + ", x=" + this.x + ", y=" + this.y + '}';
    }
    //endregion

}
