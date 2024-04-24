package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.TimerUtils;

public class IsTimerPassed extends Block {

    public IsTimerPassed() {
        super("IsTimerPassed", MainBlockType.Boolean, Tabs.VarHelper, BlockObjects.Name,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Timer));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "Timer";
        this.words[1] = "Delay";
        this.description = "Returns true if the timer is passed the delay";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((TimerUtils) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isDelay((long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event));
    }

}
