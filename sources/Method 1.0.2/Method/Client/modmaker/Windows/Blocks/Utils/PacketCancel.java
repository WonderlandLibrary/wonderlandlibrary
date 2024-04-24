package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class PacketCancel extends Block {

    public PacketCancel() {
        super("PacketCancel", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "Cancels the current Packet.";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {

            dragableBlock.local.cancelPacket = true;
        super.runCode(dragableBlock, event);
    }

}
