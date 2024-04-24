package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Algebra extends Block {

    public Algebra() {
        super("Algebra", MainBlockType.Numbers, Tabs.Math, BlockObjects.DropDown, BlockObjects.NumericalTextEnter, BlockObjects.Name);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        ddOptions.add("Sqr");
        ddOptions.add("Sin");
        ddOptions.add("Cos");
        ddOptions.add("Tan");
        ddOptions.add("Abs");
        this.description = "Solves algebraic equations like sin, cos, tan, sqrt,abs";

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Sqr":
                return Math.sqrt(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            case "Sin":
                return Math.sin(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            case "Cos":
                return Math.cos(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            case "Tan":
                return Math.tan(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            case "Abs":
                return Math.abs(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));

        }
        return 0;
    }
}
