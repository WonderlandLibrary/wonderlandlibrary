package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventRender;
import cn.hanabi.injection.interfaces.IMinecraft;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemBlock;

public class AutoPlace extends Mod {
   public AutoPlace() {
      super("Auto Place", Category.GHOST);
   }

   @EventTarget
   private void onRender(EventRender render) {
      if (this.isHoldingBlock()) {
         ((IMinecraft)mc).runRightMouse();
      }

   }

   private boolean isHoldingBlock() {
      return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().stackSize > 0 && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock;
   }
}
