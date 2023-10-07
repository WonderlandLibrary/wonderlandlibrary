package cc.novoline.gui.label;

import cc.novoline.gui.AbstractElement;
import cc.novoline.utils.fonts.api.FontRenderer;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
public abstract class AbstractLabel extends AbstractElement implements Label {

    /* fields */
    @Nullable
    protected String text;
    protected int color;
    @NonNull
    protected FontRenderer fontRenderer;

    /* constructors */
    public AbstractLabel(@Nullable String text, int color, @NonNull FontRenderer fontRenderer, int x, int y) {
        super(x, y);
        this.text = text;
        this.color = color;
        this.fontRenderer = fontRenderer;
    }

    public AbstractLabel(@Nullable String text, int color, @NonNull FontRenderer fontRenderer) {
        this(text, color, fontRenderer, 0, 0);
    }

    /* methods */
    @Override
    public void onDraw(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.text, this.x, this.y, this.color);
    }

    //region Lombok
    @Override
    @Nullable
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(@Nullable String text) {
        this.text = text;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    @NonNull
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    @Override
    public void setFontRenderer(@NonNull FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractLabel)) return false;
        final AbstractLabel that = (AbstractLabel) o;
        return Objects.equals(this.text, that.text) && this.fontRenderer.equals(that.fontRenderer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.fontRenderer);
    }

    @Override
    public String toString() {
        return "AbstractLabel{" + "text='" + this.text + '\'' + ", color=" + this.color + ", fontRenderer=" + this.fontRenderer + '}';
    }
    //endregion

}
