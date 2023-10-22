package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventRenderBlock;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.RenderUtil;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class CaveFinder extends Mod {
   ArrayList list = new ArrayList();

   public CaveFinder() {
      super("CaveFinder", Category.RENDER);
   }

   public void onEnable() {
      mc.renderGlobal.loadRenderers();
      this.list.clear();
   }

   @EventTarget
   public void onRenderBlock(EventRenderBlock e) {
      BlockPos pos = new BlockPos(e.x, e.y, e.z);
      if (!this.list.contains(pos) && e.block instanceof BlockLiquid && e.y <= 40) {
         this.list.add(pos);
      }

   }

   @EventTarget
   public void onRender(EventRender e) {
      BlockPos pos;
      IBlockState state;
      for(Iterator var2 = this.list.iterator(); var2.hasNext(); RenderUtil.drawSolidBlockESP((double)pos.getX() - ((IRenderManager)mc.getRenderManager()).getRenderPosX(), (double)pos.getY() - ((IRenderManager)mc.getRenderManager()).getRenderPosY(), (double)pos.getZ() - ((IRenderManager)mc.getRenderManager()).getRenderPosZ(), state.getBlock().getMaterial() == Material.lava ? 1.0F : 0.0F, 0.0F, state.getBlock().getMaterial() == Material.water ? 1.0F : 0.0F, 0.2F)) {
         pos = (BlockPos)var2.next();
         state = mc.theWorld.getBlockState(pos);
         if (!(state.getBlock() instanceof BlockLiquid)) {
            this.list.remove(pos);
         }
      }

   }
}
