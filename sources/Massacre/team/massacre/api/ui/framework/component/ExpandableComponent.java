package team.massacre.api.ui.framework.component;

public interface ExpandableComponent {
   float getExpandedX();

   float getExpandedY();

   float getExpandedWidth();

   float getExpandedHeight();

   void setExpanded(boolean var1);

   boolean isExpanded();
}
