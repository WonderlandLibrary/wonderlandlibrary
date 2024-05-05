package team.massacre.api.ui.main.components.tab.impl;

import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.main.components.groupBox.GroupBoxComponent;
import team.massacre.api.ui.main.components.groupBox.GroupBoxPosition;
import team.massacre.api.ui.main.components.groupBox.GroupBoxSide;
import team.massacre.api.ui.main.components.groupBox.GroupBoxType;
import team.massacre.api.ui.main.components.tab.Tab;
import team.massacre.api.ui.main.components.tab.TabComponent;

public final class VisualsTabComponent extends TabComponent {
   public VisualsTabComponent(Component parent, float x, float y, float width, float height) {
      super(parent, Tab.VISUALS, x, y, width, height);
      this.addChild(new GroupBoxComponent(this, "ESP", GroupBoxPosition.TOP, GroupBoxSide.LEFT, GroupBoxType.BIG) {
         public void initComponents() {
         }
      });
      this.addChild(new GroupBoxComponent(this, "Colored models", GroupBoxPosition.THIRD_X2, GroupBoxSide.LEFT, GroupBoxType.SMALL) {
         public void initComponents() {
         }
      });
      this.addChild(new GroupBoxComponent(this, "Other ESP", GroupBoxPosition.TOP, GroupBoxSide.RIGHT, GroupBoxType.HALF) {
         public void initComponents() {
         }
      });
      this.addChild(new GroupBoxComponent(this, "Effects", GroupBoxPosition.HALF, GroupBoxSide.RIGHT, GroupBoxType.HALF) {
         public void initComponents() {
         }
      });
   }
}
