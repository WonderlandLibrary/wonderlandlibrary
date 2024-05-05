package graphite.impl.gui.click.framework;

public interface Expandable {

    void setExpanded(boolean expanded);

    boolean isExpanded();

    double getX();
    double getY();

    double getExpandedX();
    double getExpandedY();
    double getExpandedWidth();
    double calculateExpandedHeight();

    default double getExpandedHeight() {
        return this.calculateExpandedHeight();
    }

    default boolean isHoveredExpanded(final int mouseX, final int mouseY) {
        final double ex, ey;
        return this.isExpanded() &&
            mouseX > (ex = this.getExpandedX()) &&
            mouseX < ex + this.getExpandedWidth() &&
            mouseY > (ey = this.getExpandedY()) &&
            mouseY < ey + this.getExpandedHeight();
    }
}