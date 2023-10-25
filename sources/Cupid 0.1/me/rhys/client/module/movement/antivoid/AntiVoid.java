package me.rhys.client.module.movement.antivoid;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.movement.antivoid.impl.MineBox;
import net.minecraft.util.BlockPos;

public class AntiVoid extends Module {
  public long lastCatch;
  
  public AntiVoid(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((ModuleMode)new MineBox("MineBox", this));
  }
  
  public boolean isBlockUnder(int offset) {
    for (int i = (int)(this.mc.thePlayer.posY - offset); i > 0; i--) {
      BlockPos pos = new BlockPos(this.mc.thePlayer.posX, i, this.mc.thePlayer.posZ);
      if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockAir))
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\antivoid\AntiVoid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */