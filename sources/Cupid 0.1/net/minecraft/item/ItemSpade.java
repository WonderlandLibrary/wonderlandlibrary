package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class ItemSpade extends ItemTool {
  private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet((Object[])new Block[] { Blocks.clay, Blocks.dirt, Blocks.farmland, (Block)Blocks.grass, Blocks.gravel, (Block)Blocks.mycelium, (Block)Blocks.sand, Blocks.snow, Blocks.snow_layer, Blocks.soul_sand });
  
  public ItemSpade(Item.ToolMaterial material) {
    super(1.0F, material, EFFECTIVE_ON);
  }
  
  public boolean canHarvestBlock(Block blockIn) {
    return (blockIn == Blocks.snow_layer) ? true : ((blockIn == Blocks.snow));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemSpade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */