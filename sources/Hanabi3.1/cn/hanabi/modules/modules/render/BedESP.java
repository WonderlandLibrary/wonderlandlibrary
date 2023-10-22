package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EventRender;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.NukerUtil;
import cn.hanabi.utils.RenderUtil;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.util.BlockPos;

public class BedESP extends Mod {
   public BedESP() {
      super("BedESP", Category.RENDER);
   }

   @EventTarget
   public void onRender(EventRender e) {
      Iterator var2 = NukerUtil.list.iterator();

      while(var2.hasNext()) {
         BlockPos pos = (BlockPos)var2.next();
         RenderUtil.drawSolidBlockESP((double)pos.getX() - ((IRenderManager)mc.getRenderManager()).getRenderPosX(), (double)pos.getY() - ((IRenderManager)mc.getRenderManager()).getRenderPosY(), (double)pos.getZ() - ((IRenderManager)mc.getRenderManager()).getRenderPosZ(), 1.0F, 1.0F, 1.0F, 0.2F);
      }

   }
}
