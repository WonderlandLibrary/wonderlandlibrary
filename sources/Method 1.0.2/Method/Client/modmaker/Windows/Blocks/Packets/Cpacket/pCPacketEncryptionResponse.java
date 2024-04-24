package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import net.minecraft.network.login.client.CPacketEncryptionResponse;

public class pCPacketEncryptionResponse extends Block {
    public pCPacketEncryptionResponse() {
        super( "EncryptionResponse", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketEncryptionResponse);
        this.packetclasses.add(CPacketEncryptionResponse.class);
    }
    // I don't know if the user should need access to this.
    // But we will let them at least Cancel packet and Reads its ID.
}
