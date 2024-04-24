package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.tileentity.TileEntityChest;


public class numplayersChest extends Block {

    public numplayersChest() {
        super("numplayersChest", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns the number of players in the chest";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
     return    ((TileEntityChest) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).numPlayersUsing;
    }

}
