package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Replace extends Block {

    public Replace() {
        super("Replace", MainBlockType.String, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "Replace";
        this.words[1] = "With";
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Replaces a string with another string";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).replace((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event), (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,2, event));
    }


}
