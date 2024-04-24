package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.block.state.IBlockState;

public class getBlock extends Block {

    public getBlock() {
        super("getBlock", MainBlockType.Blocks, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.IBlockState));
        this.description = "Given block state return block" + "\n" + "IBlockState getBlock()";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getBlock();
    }

}
