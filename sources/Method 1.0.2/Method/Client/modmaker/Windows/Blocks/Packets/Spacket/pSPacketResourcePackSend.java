package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketResourcePackSend;

public class pSPacketResourcePackSend extends Block {
    public pSPacketResourcePackSend() {
        super( "ResourcePackSend", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketResourcePackSend);
        this.packetclasses.add(SPacketResourcePackSend.class);

    }

    public static class getURL extends Block {
        public getURL() {
            super( "getURL", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketResourcePackSend);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketResourcePackSend) event).getURL();
        }
    }

    public static class getHash extends Block {
        public getHash() {
            super( "getHash", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketResourcePackSend);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketResourcePackSend) event).getHash();
        }
    }

    public static class setURL extends Block {
        public setURL() {
            super( "setURL", MainBlockType.Default, Tabs.Sub, Headers.SPacketResourcePackSend, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketResourcePackSend) event).url = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setHash extends Block {
        public setHash() {
            super( "setHash", MainBlockType.Default, Tabs.Sub, Headers.SPacketResourcePackSend, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketResourcePackSend) event).hash = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
