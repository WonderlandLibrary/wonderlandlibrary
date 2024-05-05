package team.massacre.api.ui.main.components.tab;

import team.massacre.api.ui.framework.component.Component;

public class TabComponent extends Component {
   public static final float MARGIN = 10.0F;
   private final Tab tab;

   public TabComponent(Component parent, Tab tab, float x, float y, float width, float height) {
      super(parent, x, y, width, height);
      this.tab = tab;
   }
}
