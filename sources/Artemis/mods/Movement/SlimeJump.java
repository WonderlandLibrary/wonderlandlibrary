package artemis.mods.Movement;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;
import net.minecraft.block.BlockSlime;
import net.minecraft.util.BlockPos;


public class SlimeJump extends module {


  public SlimeJump() {
    super("SlimeJump", "Jumps on slime blocks", Keyboard.KEY_NONE, 77, Category.MOVEMENT);
  }
  
  public void onDisable() {
   
    super.onDisable();
  }
  
  public void onUpdate() {
    if (isToggled()) {
    	
    BlockPos pos = new BlockPos(Math.floor(mc.thePlayer.posX), Math.ceil(mc.thePlayer.posY), Math.floor(mc.thePlayer.posZ));
    if(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockSlime && mc.thePlayer.onGround) {
    	mc.thePlayer.motionY = 1.5;
    }
      super.onUpdate();
    } 
  }
}
