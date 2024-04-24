package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.util.concurrent.ThreadLocalRandom;

public class Random extends Block {

    public Random() {
        super("Random", MainBlockType.Numbers, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Returns a random number between the two numbers you enter.";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ThreadLocalRandom.current().nextDouble(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event));
    }

}
