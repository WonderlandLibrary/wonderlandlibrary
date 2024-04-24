package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.login.server.SPacketEnableCompression;

public class pSPacketEnableCompression extends Block {
    public pSPacketEnableCompression() {
        super( "EnableCompression", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEnableCompression);
        this.packetclasses.add(SPacketEnableCompression.class);
    }

    public static class getCompressionThreshold extends Block {
        public getCompressionThreshold() {
            super( "getCompressionThreshold", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEnableCompression);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEnableCompression) event).getCompressionThreshold();
        }
    }

    public static class setcompressionThreshold extends Block {
        public setcompressionThreshold() {
            super( "setcompressionThreshold", MainBlockType.Default, Tabs.Sub, Headers.SPacketEnableCompression, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEnableCompression) event).compressionThreshold = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
