package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.tileentity.TileEntity;

public class GetBlockType extends Block {

    public GetBlockType() {
        super("GetBlockType", MainBlockType.Blocks, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.TileEntity));
        this.description = "Given tileEntity return block " + "\n" + "(TileEntity).getBlockType()";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((TileEntity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getBlockType();
    }

}
