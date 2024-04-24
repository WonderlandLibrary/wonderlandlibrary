package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.player.EntityPlayer;


public class GetCurrentItem extends Block {

    public GetCurrentItem() {
        super("GetCurrentItem", MainBlockType.ItemStack, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Get Current Item + \n" + "EntityPlayer.inventory.getCurrentItem()";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return   ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).inventory.getCurrentItem();
    }

}
