package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class SubString extends Block {

    public SubString() {
        super("SubString", MainBlockType.String, Tabs.VarHelper, BlockObjects.NumericalTextEnter, BlockObjects.Words,
                BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = " SubString from";
        this.words[1] = " to";
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Returns a substring of the string";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).substring((int) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event), (int) dragableBlock.local.codeExecuter.solveObject(dragableBlock,2, event));
    }

}
