package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.login.client.CPacketLoginStart;

public class pCPacketLoginStart extends Block {
    public pCPacketLoginStart() {
        super( "LoginStart", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketLoginStart);
        this.packetclasses.add(CPacketLoginStart.class);
        this.description = "Sends the login start packet";
    }

    public static class getProfile extends Block {
        public getProfile() {
            super( "getProfile", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketLoginStart);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketLoginStart) event).getProfile();

        }
    }

}
