package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketHeldItemChange;

public class pSPacketHeldItemChange extends Block {
    public pSPacketHeldItemChange() {
        super( "ServerHeldItemChange", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketHeldItemChange);
        this.packetclasses.add(SPacketHeldItemChange.class);
    }

    public static class HotbarIndex extends Block {
        public HotbarIndex() {
            super( "HotbarIndex", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketHeldItemChange);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketHeldItemChange) event).getHeldItemHotbarIndex();
        }
    }
    public static class setheldItem extends Block {
        public setheldItem() {
            super( "setheldItem", MainBlockType.Default, Tabs.Sub,Headers.SPacketHeldItemChange, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketHeldItemChange) event).heldItemHotbarIndex = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
