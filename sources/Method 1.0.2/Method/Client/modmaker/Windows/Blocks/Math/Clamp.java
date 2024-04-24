package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Clamp extends Block {

    public Clamp() {
        super( "Clamp", MainBlockType.Numbers, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "N2: ";
        this.words[1] = "N3: ";
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Clamp! N1: Number to clamp, N2: Min, N3: Max";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) < dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event)) {
            return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
        } else {
            return Math.min(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event));
        }
    }


}
