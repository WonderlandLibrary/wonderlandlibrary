package team.massacre.api.ui.main.components.tab.impl;

import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.main.components.groupBox.GroupBoxComponent;
import team.massacre.api.ui.main.components.groupBox.GroupBoxPosition;
import team.massacre.api.ui.main.components.groupBox.GroupBoxSide;
import team.massacre.api.ui.main.components.groupBox.GroupBoxType;
import team.massacre.api.ui.main.components.tab.Tab;
import team.massacre.api.ui.main.components.tab.TabComponent;

public final class LegitTabComponent extends TabComponent {
   public LegitTabComponent(Component parent, float x, float y, float width, float height) {
      super(parent, Tab.LEGIT, x, y, width, height);
      this.addChild(new GroupBoxComponent(this, "Auto clicker", GroupBoxPosition.TOP, GroupBoxSide.LEFT, GroupBoxType.HALF) {
         public void initComponents() {
         }
      });
      this.addChild(new GroupBoxComponent(this, "Reach", GroupBoxPosition.HALF, GroupBoxSide.LEFT, GroupBoxType.HALF) {
         public void initComponents() {
         }
      });
      this.addChild(new GroupBoxComponent(this, "Other", GroupBoxPosition.TOP, GroupBoxSide.RIGHT, GroupBoxType.FULL) {
         public void initComponents() {
         }
      });
   }
}
