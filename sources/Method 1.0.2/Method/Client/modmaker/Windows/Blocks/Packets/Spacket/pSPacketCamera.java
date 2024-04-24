package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketCamera;

public class pSPacketCamera extends Block {
    public pSPacketCamera() {
        super( "Camera", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketCamera);
        this.packetclasses.add(SPacketCamera.class);
    }

    public static class entityId extends Block {
        public entityId() {
            super( "entityId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCamera);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCamera) event).entityId;

        }
    }

    public static class setentityIdCam extends Block {
        public setentityIdCam() {
            super( "setentityIdCam", MainBlockType.Default, Tabs.Sub,Headers.SPacketCamera, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }
        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCamera) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
