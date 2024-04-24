package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.ColorUtils;

public class ColorCalc extends Block {

    public ColorCalc() {
        super("ColorCalc", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        ddOptions.add("Red");
        ddOptions.add("Green");
        ddOptions.add("Blue");
        ddOptions.add("Alpha");

        this.description = "Returns the color value of the selected color";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Red":
                return ColorUtils.colorcalc((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), 16);
            case "Green":
                return ColorUtils.colorcalc((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), 8);
            case "Blue":
                return ColorUtils.colorcalc((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), 0);
            case "Alpha":
                return ColorUtils.colorcalc((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), 24);
        }
        return 0;
    }


}
