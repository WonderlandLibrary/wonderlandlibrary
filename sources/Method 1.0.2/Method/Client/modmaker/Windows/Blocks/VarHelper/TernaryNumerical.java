package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class TernaryNumerical extends Block {

    public TernaryNumerical() {
        super("TernaryNumerical", MainBlockType.Numbers, Tabs.VarHelper, BlockObjects.Name,
                BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = ":";
        this.words[1] = "?";
        this.description = "Ternary Numerical also known by : ? : ";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event))
            return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
        else
            return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 2, event);
    }

}
