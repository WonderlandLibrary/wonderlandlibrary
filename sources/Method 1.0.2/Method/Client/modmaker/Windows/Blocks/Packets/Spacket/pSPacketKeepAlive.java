package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketKeepAlive;

public class pSPacketKeepAlive extends Block {
    public pSPacketKeepAlive() {
        super( "ServerKeepAlive", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketKeepAlive);
        this.packetclasses.add(SPacketKeepAlive.class);
    }

    public static class getId extends Block {
        public getId() {
            super( "getId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketKeepAlive);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketKeepAlive) event).getId();

        }
    }

    public static class setid extends Block {
        public setid() {
            super( "setid", MainBlockType.Default, Tabs.Sub,Headers.SPacketKeepAlive, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketKeepAlive) event).id = (long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
