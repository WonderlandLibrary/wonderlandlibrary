package cn.hanabi.modules.modules.world;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.modules.movement.Fly.Fly;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;

@ObfuscationClass
public class AntiFall extends Mod {
   public static Value falldistance = new Value("AntiFall", "FallDistance", 10.0, 5.0, 30.0, 0.1);
   public static Value delay = new Value("AntiFall", "Delay", 800.0, 200.0, 2000.0, 100.0);
   public Value onlyvoid = new Value("AntiFall", "OnlyVoid", true);
   public Value nodmg = new Value("AntiFall", "0 DMG", true);
   TimeHelper timer = new TimeHelper();
   boolean falling;

   public AntiFall() {
      super("AntiFall", Category.WORLD);
   }

   @EventTarget
   public void onUpdate(EventPreMotion e) {
      boolean canFall = !mc.thePlayer.onGround;
      boolean aboveVoid = !(Boolean)this.onlyvoid.getValue() || PlayerUtil.isBlockUnder();
      if (canFall && PlayerUtil.isBlockUnder() && (Boolean)this.nodmg.getValue()) {
         e.setOnGround(true);
      }

      if ((double)mc.thePlayer.fallDistance >= (Double)falldistance.getValue() && aboveVoid) {
         e.setX(e.getX() + Fly.getRandomInRange(0.19, 0.49));
         e.setZ(e.getZ() + Fly.getRandomInRange(0.19, 0.49));
      }

   }
}
