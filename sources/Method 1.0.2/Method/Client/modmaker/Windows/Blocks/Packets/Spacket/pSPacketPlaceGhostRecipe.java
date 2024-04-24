package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;

public class pSPacketPlaceGhostRecipe extends Block {
    public pSPacketPlaceGhostRecipe() {
        super( "PlaceGhostRecipe", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketPlaceGhostRecipe);
        this.packetclasses.add(SPacketPlaceGhostRecipe.class);
    }


}
