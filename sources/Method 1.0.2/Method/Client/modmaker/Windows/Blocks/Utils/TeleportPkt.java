package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;
import net.minecraft.util.math.Vec3d;

public class TeleportPkt extends Block {

    public TeleportPkt() {
        super("TeleportPkt", MainBlockType.Default, Tabs.Utils, BlockObjects.Name,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.description = "Teleport to a position";
        this.words[0] = "EndPos";


        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));


    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Utils.teleportToPosition((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
        super.runCode(dragableBlock, event);
    }

}
