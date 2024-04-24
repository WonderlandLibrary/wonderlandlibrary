package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Min extends Block {

    public Min() {
        super("Min/Max", MainBlockType.Numbers, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        ddOptions.add("Min");
        ddOptions.add("Max");
        this.description = "Returns the minimum of two numbers or " + "\n" + "the maximum of two numbers";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Min":
                return Math.min(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event));
            case "Max":
                return Math.max(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event));

        }
        return 0;
    }
}
