package team.massacre.impl.module.playr;

import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.MathUtil;
import team.massacre.utils.TimerUtil;

public class AutoClicker extends Module {
   public TimerUtil leftTimer = new TimerUtil();
   public TimerUtil rightTimer = new TimerUtil();
   public Property<Boolean> rightClick = new Property("Right Click", false);
   public DoubleProperty maxCPS = new DoubleProperty("Max CPS", 12.0D, 1.0D, 20.0D, 1.0D);
   public DoubleProperty minCPS = new DoubleProperty("Min CPS", 10.0D, 1.0D, 20.0D, 1.0D);

   public AutoClicker() {
      super("AutoClicker", 0, Category.PLAYER);
      this.addValues(new Property[]{this.rightClick, this.maxCPS, this.minCPS});
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      if ((Double)this.minCPS.getValue() > (Double)this.maxCPS.getValue()) {
         this.minCPS.setValue((Double)this.maxCPS.getValue());
      }

      if (this.mc.gameSettings.keyBindAttack.pressed && this.leftTimer.hasHit(1000.0D / MathUtil.getRandomInRange((double)((Double)this.minCPS.getValue()).floatValue(), (double)((Double)this.maxCPS.getValue()).floatValue()))) {
         this.mc.leftClickCounter = 0;
         this.mc.clickMouse();
         this.leftTimer.reset();
      }

      if (this.mc.gameSettings.keyBindUseItem.pressed && (Boolean)this.rightClick.getValue() && this.rightTimer.hasHit(1000.0D / MathUtil.getRandomInRange((Double)this.minCPS.getValue(), (Double)this.maxCPS.getValue()))) {
         this.mc.rightClickMouse();
         this.rightTimer.reset();
      }

   }
}
