package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.util.math.BlockPos;

public class pSPacketSignEditorOpen extends Block {
    public pSPacketSignEditorOpen() {
        super( "SignEditorOpen", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSignEditorOpen);
        this.packetclasses.add(SPacketSignEditorOpen.class);
    }

    public static class getSignPosition extends Block {
        public getSignPosition() {
            super( "getSignPosition", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.SPacketSignEditorOpen);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketSignEditorOpen) event).getSignPosition();
        }
    }

    public static class setsignPosition extends Block {
        public setsignPosition() {
            super( "setsignPosition", MainBlockType.Default, Tabs.Sub,Headers.SPacketSignEditorOpen, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSignEditorOpen) event).signPosition = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
