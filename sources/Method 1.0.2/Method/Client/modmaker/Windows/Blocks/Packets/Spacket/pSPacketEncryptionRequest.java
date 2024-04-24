package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.login.server.SPacketEncryptionRequest;

public class pSPacketEncryptionRequest extends Block {
    public pSPacketEncryptionRequest() {
        super( "EncryptionRequest", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEncryptionRequest);
        this.packetclasses.add(SPacketEncryptionRequest.class);
    }

    public static class getServerId extends Block {
        public getServerId() {
            super( "getServerId", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketEncryptionRequest);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketEncryptionRequest) event).getServerId();
        }
    }

    public static class sethashedServerId extends Block {
        public sethashedServerId() {
            super( "sethashedServerId", MainBlockType.Default, Tabs.Sub, Headers.SPacketEncryptionRequest, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEncryptionRequest) event).hashedServerId = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


}
