package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketCustomPayload;

public class pSPacketCustomPayload extends Block {
    public pSPacketCustomPayload() {
        super( "SCustomPayload", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketCustomPayload);
        this.packetclasses.add(SPacketCustomPayload.class);
    }

    public static class getChannelNameoad extends Block {
        public getChannelNameoad() {
            super( "getChannelNameoad", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketCustomPayload);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketCustomPayload) event).getChannelName();
        }
    }

    public static class setChannel extends Block {
        public setChannel() {
            super( "setChannel", MainBlockType.Default, Tabs.Sub, Headers.SPacketCustomPayload, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCustomPayload) event).channel = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
