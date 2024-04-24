package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Equal extends Block {

    public Equal() {
        super("Equal", MainBlockType.Boolean, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        ddOptions.add("=");
        ddOptions.add("!=");
        ddOptions.add(">");
        ddOptions.add("<");
        ddOptions.add(">=");
        ddOptions.add("<=");
        this.description = "Returns true if the first number is equal to the second number + \n + " +
                "Also has Greater than, Less than, Greater than or equal to, and Less than or equal to and not equal to";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "=":
                return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) == dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
            case "!=":
                return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) != dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
            case ">":
                return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) > dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
            case "<":
                return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) < dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
            case ">=":
                return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) >= dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
            case "<=":
                return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) <= dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);

        }
        return false;
    }
}
