package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.renderer.GlStateManager;

public class GlClear extends Block {

    public GlClear() {
        super("GlClear", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.description = "Call to clear Gl flag" + "\n" + "GlStateManager.clear(int)";
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        GlStateManager.clear((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
        super.runCode(dragableBlock, event);
    }


}
