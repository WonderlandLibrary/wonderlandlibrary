package cn.hanabi.utils;

import cn.hanabi.events.EventRenderBlock;
import cn.hanabi.events.EventUpdate;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import net.minecraft.block.BlockBed;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class NukerUtil {
   public static ArrayList list = new ArrayList();

   public static void update() {
      Minecraft.getMinecraft().renderGlobal.loadRenderers();
      list.clear();
   }

   @EventTarget
   public void onRenderBlock(EventRenderBlock e) {
      BlockPos pos = new BlockPos(e.x, e.y, e.z);
      if (!list.contains(pos) && e.block instanceof BlockBed) {
         list.add(pos);
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      list.removeIf((pos) -> {
         return !(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockBed);
      });
   }
}
