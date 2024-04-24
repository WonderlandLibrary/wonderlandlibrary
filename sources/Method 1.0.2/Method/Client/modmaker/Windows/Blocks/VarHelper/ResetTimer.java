package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.TimerUtils;

public class ResetTimer extends Block {

    public ResetTimer() {
        super("ResetTimer", MainBlockType.Default, Tabs.VarHelper, BlockObjects.Name,  BlockObjects.NumericalTextEnter);
        this.description = "Resets the timer";
        this.typesAccepted.add(typesCollapse(MainBlockType.Timer));


    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ( (TimerUtils) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).setLastMS();
        super.runCode(dragableBlock, event);
    }

}
