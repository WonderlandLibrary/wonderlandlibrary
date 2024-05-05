package vip.radium.gui.csgo.component;

public interface ExpandableComponent {

    float getExpandedX();

    float getExpandedY();

    float getExpandedWidth();

    float getExpandedHeight();

    void setExpanded(boolean expanded);

    boolean isExpanded();

}
