package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import net.minecraft.network.play.client.CPacketPlaceRecipe;

public class pCPacketPlaceRecipe extends Block {
    public pCPacketPlaceRecipe() {
        super( "PlaceRecipe", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketPlaceRecipe);
        this.packetclasses.add(CPacketPlaceRecipe.class);
        this.description = "Sends a packet to the server to place a recipe";

    }


}
