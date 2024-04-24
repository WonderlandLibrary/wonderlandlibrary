package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;

public class DirectionSpeed extends Block {

    public DirectionSpeed() {
        super("DirectionSpeed", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        ddOptions.add("X");
        ddOptions.add("Z");
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Returns the direction and speed of an entity";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X":
                return Utils.directionSpeed(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event))[0];
            case "Z":
                return Utils.directionSpeed(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event))[1];
        }
        return 0;
    }

}
