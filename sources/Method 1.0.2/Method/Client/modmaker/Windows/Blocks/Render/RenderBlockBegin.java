package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.proxy.Overrides.ForgeBlockModelRendererOverride;

public class RenderBlockBegin extends Block {

    public RenderBlockBegin() {
        super("RenderBlockBegin", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.description = "Setting this to true will intercept the rendering of blocks." + "\n" +
                "Done this way to avoid lag";

        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ForgeBlockModelRendererOverride.runevent = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
        super.runCode(dragableBlock, event);
    }


}
