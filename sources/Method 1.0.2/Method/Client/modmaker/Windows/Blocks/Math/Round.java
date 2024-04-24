package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Round extends Block {

    public Round() {
        super("Round", MainBlockType.Numbers, Tabs.Math, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        ddOptions.add("Round");
        ddOptions.add("Ceiling");
        ddOptions.add("Floor");
        ddOptions.add("Truncate");
        this.description = "Rounds the number to the nearest integer. " +
                "Also Celings, Floors, and Truncates the number.";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Round":
                return Math.round(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
            case "Ceiling":
                return Math.ceil(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
            case "Floor":
                return Math.floor(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
            case "Truncate":
                return Math.floor(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event)*100)/100;
        }
        return 0;
    }

}
