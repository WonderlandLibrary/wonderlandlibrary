package team.massacre.impl.module.render;

import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventUpdate;

public class Animations extends Module {
   public EnumProperty<Animations.AnimationsMode> mode;

   public Animations() {
      super("Animations", 0, Category.RENDER);
      this.mode = new EnumProperty("Mode", Animations.AnimationsMode.ONEPOINTSEVEN);
      this.addValues(new Property[]{this.mode});
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      this.setSuffix(((Animations.AnimationsMode)this.mode.getValue()).name());
   }

   public static enum AnimationsMode {
      GRAVITY,
      ONEPOINTSEVEN,
      JELLO;
   }
}
