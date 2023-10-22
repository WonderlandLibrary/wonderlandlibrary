package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;

public class HitAnimation extends Mod {
   public Value mode = new Value("HitAnimation", "Mode", 0);
   public Value swingSpeed = new Value("HitAnimation", "SwingSpeed", 1.0, 0.5, 5.0);
   public Value posX = new Value("HitAnimation", "ItemPosX", 0.0, -1.0, 1.0, 0.05);
   public Value posY = new Value("HitAnimation", "ItemPosY", 0.0, -1.0, 1.0, 0.05);
   public Value posZ = new Value("HitAnimation", "ItemPosZ", 0.0, -1.0, 1.0, 0.05);
   public Value itemScale = new Value("HitAnimation", "ItemScale", 0.7, 0.0, 2.0, 0.05);
   public Value equipProgMultProperty = new Value("HitAnimation", "E-Prog", 2.0, 0.5, 3.0, 0.1);
   public Value equipProgressProperty = new Value("HitAnimation", "Equip Prog", false);

   public HitAnimation() {
      super("HitAnimation", Category.RENDER);
      this.mode.addValue("Vanilla");
      this.mode.addValue("1.7");
      this.mode.addValue("Swang");
      this.mode.addValue("Swank");
      this.mode.addValue("Swong");
      this.mode.addValue("Sigma");
      this.mode.addValue("Jello");
      this.mode.addValue("Slide");
      this.mode.addValue("Ohare");
      this.mode.addValue("Wizzard");
      this.mode.addValue("Lennox");
      this.mode.addValue("Leaked");
      this.mode.addValue("Butter");
      this.mode.addValue("Lucky");
      this.mode.addValue("Long Hit");
      this.mode.addValue("Tiny Whack");
      this.mode.addValue("Skid");
      this.mode.addValue("Slide2");
      this.mode.addValue("Mix");
      this.mode.addValue("SlideT");
      this.mode.addValue("SlideA");
      this.mode.addValue("Epic");
      this.mode.addValue("Punch");
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.setDisplayName((String)this.mode.getValueState());
   }
}
