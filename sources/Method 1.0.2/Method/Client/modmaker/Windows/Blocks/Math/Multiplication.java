package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Multiplication extends Block {

    public Multiplication() {
        super("Multiplication", MainBlockType.Numbers, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = " X ";
        this.description = "Multiplies two numbers";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event) * dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event);
    }

}
