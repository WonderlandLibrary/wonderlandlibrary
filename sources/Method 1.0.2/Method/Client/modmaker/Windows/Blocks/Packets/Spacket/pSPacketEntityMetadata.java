package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketEntityMetadata;

public class pSPacketEntityMetadata extends Block {
    public pSPacketEntityMetadata() {
        super( "EntityMetadata", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntityMetadata);
        this.packetclasses.add(SPacketEntityMetadata.class);
    }

    public static class getEntityIdata extends Block {
        public getEntityIdata() {
            super( "getEntityIdata", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityMetadata);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityMetadata) event).getEntityId();
        }
    }
    public static class setentityIdata extends Block {
        public setentityIdata() {
            super( "setentityIdata", MainBlockType.Default, Tabs.Sub,Headers.SPacketEntityMetadata, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityMetadata) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
