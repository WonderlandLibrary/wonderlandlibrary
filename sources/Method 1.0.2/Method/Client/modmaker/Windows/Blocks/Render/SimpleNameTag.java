package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.util.math.Vec3d;

public class SimpleNameTag extends Block {

    public SimpleNameTag() {
        super("SimpleNameTag", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));

        this.words[0] = "Pos:";
        this.words[1] = "Text:";
        this.description = "Renders a simple name tag";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        RenderUtils.SimpleNametag((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
        super.runCode(dragableBlock, event);
    }


}
