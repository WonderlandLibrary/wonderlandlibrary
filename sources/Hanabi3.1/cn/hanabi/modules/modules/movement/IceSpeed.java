package cn.hanabi.modules.modules.movement;

import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import net.minecraft.init.Blocks;

public class IceSpeed extends Mod {
   public IceSpeed() {
      super("IceSpeed", Category.MOVEMENT);
   }

   public void onEnable() {
      Blocks.ice.slipperiness = 0.39F;
      Blocks.packed_ice.slipperiness = 0.39F;
      super.onEnable();
   }

   public void onDisable() {
      Blocks.ice.slipperiness = 0.98F;
      Blocks.packed_ice.slipperiness = 0.98F;
      super.onDisable();
   }
}
