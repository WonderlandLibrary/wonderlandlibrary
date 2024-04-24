package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.Main;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class HoleRun extends Block {


    public HoleRun() {
        super("HoleRun", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Runs the Hole Finder code. Finds Holes in a radius";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        double radius = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
        Main.holeManager.detectHoles((int) radius);
        super.runCode(dragableBlock, event);
    }


}
