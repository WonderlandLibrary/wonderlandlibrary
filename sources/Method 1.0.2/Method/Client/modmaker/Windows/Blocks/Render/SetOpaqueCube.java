package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.proxy.Overrides.VisGraphOverride;

public class SetOpaqueCube extends Block {

    public SetOpaqueCube() {
        super("SetOpaqueCube", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.description = "If set to true, cube will be cancelled.";

        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        VisGraphOverride.stop = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
        super.runCode(dragableBlock, event);
    }


}
