package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Mod extends Block {

    public Mod() {
        super("Mod", MainBlockType.Numbers, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Returns modulus of number one by number two.";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event) % dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event);
    }
}
