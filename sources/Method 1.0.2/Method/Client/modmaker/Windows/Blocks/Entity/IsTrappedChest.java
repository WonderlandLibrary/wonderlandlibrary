package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;

public class IsTrappedChest extends Block {

    public IsTrappedChest() {
        super("GetChestType", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns true if the chest is trapped.";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        if( dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof TileEntityChest){
            return ((TileEntityChest) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getChestType() == BlockChest.Type.TRAP;
        }
        return false;
    }

}
