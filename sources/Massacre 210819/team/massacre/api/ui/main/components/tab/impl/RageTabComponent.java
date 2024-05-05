package team.massacre.api.ui.main.components.tab.impl;

import team.massacre.Massacre;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.main.components.groupBox.GroupBoxComponent;
import team.massacre.api.ui.main.components.groupBox.GroupBoxPosition;
import team.massacre.api.ui.main.components.groupBox.GroupBoxSide;
import team.massacre.api.ui.main.components.groupBox.GroupBoxType;
import team.massacre.api.ui.main.components.tab.Tab;
import team.massacre.api.ui.main.components.tab.TabComponent;
import team.massacre.impl.module.combat.KillAura2;

public final class RageTabComponent extends TabComponent {
   public RageTabComponent(Component parent, float x, float y, float width, float height) {
      super(parent, Tab.RAGE, x, y, width, height);
      this.addChild(new GroupBoxComponent(this, "Rage bot", GroupBoxPosition.TOP, GroupBoxSide.LEFT, GroupBoxType.FULL) {
         public void initComponents() {
            KillAura2 killaura = (KillAura2)Massacre.INSTANCE.getModuleManager().getModule(KillAura2.class);
            this.addComponentsFromModule(killaura);
         }
      });
      this.addChild(new GroupBoxComponent(this, "Auto heal", GroupBoxPosition.TOP, GroupBoxSide.RIGHT, GroupBoxType.HALF) {
         public void initComponents() {
         }
      });
      this.addChild(new GroupBoxComponent(this, "Other", GroupBoxPosition.HALF, GroupBoxSide.RIGHT, GroupBoxType.HALF) {
         public void initComponents() {
         }
      });
   }
}
