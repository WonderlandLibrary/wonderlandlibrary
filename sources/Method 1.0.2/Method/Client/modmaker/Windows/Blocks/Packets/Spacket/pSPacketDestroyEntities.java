package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketDestroyEntities;

import java.util.ArrayList;
import java.util.Collections;

public class pSPacketDestroyEntities extends Block {
    public pSPacketDestroyEntities() {
        super( "DestroyEntities", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketDestroyEntities);
        this.packetclasses.add(SPacketDestroyEntities.class);
    }

    public static class EntityIDDes extends Block {
        public EntityIDDes() {
            super( "EntityIDDes", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.SPacketDestroyEntities);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return new ArrayList<>(Collections.singletonList(((SPacketDestroyEntities) event).getEntityIDs()));
        }
    }


}
