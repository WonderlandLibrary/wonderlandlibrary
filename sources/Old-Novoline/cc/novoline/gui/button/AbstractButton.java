package cc.novoline.gui.button;

import cc.novoline.gui.AbstractElementWithBody;
import cc.novoline.gui.label.Label;
import cc.novoline.utils.java.Checks;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

import static cc.novoline.utils.RenderUtils.drawRoundedRect;

/**
 * @author xDelsy
 */
public abstract class AbstractButton extends AbstractElementWithBody implements Button {

    /* fields */
    @Nullable
    protected Label name;

    /* constructors */
    public AbstractButton(@Nullable Label name, int x, int y, int width, int height) {
        super(x, y, width, height);
        setName(name);
    }

    public AbstractButton(@Nullable Label name, int x, int y) {
        super(x, y, 200, 20);
        setName(name);
    }

    /* methods */
    @Override
    public void onDraw(int mouseX, int mouseY) {
        if (this.visible) {
            final int alpha = !isHovered(mouseX, mouseY) ? 210 : 125;

            drawRoundedRect(this.x, this.y, this.width, this.height, 15, 0x303136 | alpha << 24);

            if (this.name != null) {
                this.name.draw(mouseX, mouseY);
            }
        }
    }

    public void updateNamePosition() throws NullPointerException {
        Checks.notNull(this.name, "name");

        final int x = (int) (this.x + (this.width - this.name.getWidth()) / 2F), // @off
                y = (int) (this.y + (this.height - this.name.getFontRenderer().getHeight()) / 2F); // @on

        this.name.setPosition(x, y);
    }

    //region Lombok
    @Override
    @Nullable
    public Label getName() {
        return this.name;
    }

    @Override
    public void setName(@Nullable Label name) {
        if (name != null) {
            this.name = name;
            updateNamePosition();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractButton)) return false;
        if (!super.equals(o)) return false;
        final AbstractButton that = (AbstractButton) o;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.name);
    }

    @Override
    public String toString() {
        return "AbstractButton{" + "name=" + this.name + '}';
    }
    //endregion

}
