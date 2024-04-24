package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketEntityProperties;

public class pSPacketEntityProperties extends Block {
    public pSPacketEntityProperties() {
        super( "EntityProperties", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntityProperties);
        this.packetclasses.add(SPacketEntityProperties.class);
    }

    public static class getEntityIdies extends Block {
        public getEntityIdies() {
            super( "getEntityIdies", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityProperties);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityProperties) event).getEntityId();
        }
    }
    public static class getSnapshots extends Block {
        public getSnapshots() {
            super( "getSnapshots", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityProperties);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event){
            return ((SPacketEntityProperties) event).getSnapshots();
        }
    }

    public static class setentityIdies extends Block {
        public setentityIdies() {
            super( "setentityIdies", MainBlockType.Default, Tabs.Sub,Headers.SPacketEntityProperties, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityProperties) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
