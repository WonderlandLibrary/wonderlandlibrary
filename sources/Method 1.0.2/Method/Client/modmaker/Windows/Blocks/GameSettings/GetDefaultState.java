package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class GetDefaultState extends Block {

    public GetDefaultState() {
        super("GetDefaultState", MainBlockType.IBlockState, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Blocks));
        this.description = "Given Block get Default State " + "\n" + "(Block).getDefaultState() IblockState";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return    ( (net.minecraft.block.Block)dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getDefaultState();
    }

}
