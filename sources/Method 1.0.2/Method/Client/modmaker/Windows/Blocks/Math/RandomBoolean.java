package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.util.concurrent.ThreadLocalRandom;

public class RandomBoolean extends Block {

    public RandomBoolean() {
        super("RandomBoolean", MainBlockType.Boolean, Tabs.Math, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Returns true input % of the time. out of 100";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        int num = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
        // return true if random number % of the time
        // ex if num = 50, return true 50% of the time
        return ThreadLocalRandom.current().nextInt(0, 100) < num;
    }

}
