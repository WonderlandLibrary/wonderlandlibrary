package team.massacre.impl.module.combat;

import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.Representation;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.impl.event.EventUpdate;

public class Reach extends Module {
   public DoubleProperty maxReach;
   public DoubleProperty minReach;

   public Reach() {
      super("Reach", 0, Category.COMBAT);
      this.maxReach = new DoubleProperty("Max Reach", 3.4D, 3.0D, 6.0D, 0.1D, Representation.DISTANCE);
      this.minReach = new DoubleProperty("Min Reach", 3.0D, 3.0D, 6.0D, 0.1D, Representation.DISTANCE);
      this.addValues(new Property[]{this.maxReach, this.minReach});
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      if ((Double)this.minReach.getValue() > (Double)this.maxReach.getValue()) {
         this.minReach.setValue((Double)this.maxReach.getValue());
      }

   }
}
