package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Shift extends Block {

    public Shift() {
        super("Shift", MainBlockType.Numbers, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.DropDown, BlockObjects.Name,
                BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));


        ddOptions.add(">>");
        ddOptions.add("<<");


        this.description = "Solves a number with a shift << or >>";

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case ">>":
                return (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) >> ((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event));
            case "<<":
                return (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) << ((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event));

        }
        return 0;
    }
}
