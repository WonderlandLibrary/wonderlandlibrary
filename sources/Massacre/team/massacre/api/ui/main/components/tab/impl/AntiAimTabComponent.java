package team.massacre.api.ui.main.components.tab.impl;

import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.main.components.groupBox.GroupBoxComponent;
import team.massacre.api.ui.main.components.groupBox.GroupBoxPosition;
import team.massacre.api.ui.main.components.groupBox.GroupBoxSide;
import team.massacre.api.ui.main.components.groupBox.GroupBoxType;
import team.massacre.api.ui.main.components.tab.Tab;
import team.massacre.api.ui.main.components.tab.TabComponent;

public final class AntiAimTabComponent extends TabComponent {
   public AntiAimTabComponent(Component parent, float x, float y, float width, float height) {
      super(parent, Tab.ANTI_AIM, x, y, width, height);
      this.addChild(new GroupBoxComponent(this, "Target strafe", GroupBoxPosition.TOP, GroupBoxSide.LEFT, GroupBoxType.FULL) {
         public void initComponents() {
         }
      });
      this.addChild(new GroupBoxComponent(this, "Fake lag", GroupBoxPosition.TOP, GroupBoxSide.RIGHT, GroupBoxType.HALF) {
         public void initComponents() {
         }
      });
      this.addChild(new GroupBoxComponent(this, "Velocity", GroupBoxPosition.HALF, GroupBoxSide.RIGHT, GroupBoxType.HALF) {
         public void initComponents() {
         }
      });
   }
}
