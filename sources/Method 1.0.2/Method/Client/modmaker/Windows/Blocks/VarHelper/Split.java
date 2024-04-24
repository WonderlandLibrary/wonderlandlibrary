package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Split extends Block {

    public Split() {
        super("Split", MainBlockType.String, Tabs.VarHelper, BlockObjects.NumericalTextEnter,BlockObjects.Words,BlockObjects.NumericalTextEnter);
        this.words[0] = " Split at ";
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Splits the string at the given string";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).split((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
    }

}
