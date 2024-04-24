package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketEntityAttach;

public class pSPacketEntityAttach extends Block {
    public pSPacketEntityAttach() {
        super( "EntityAttach", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntityAttach);
        this.packetclasses.add(SPacketEntityAttach.class);
    }

    public static class getEntityId extends Block {
        public getEntityId() {
            super( "getEntityId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityAttach);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityAttach) event).getEntityId();

        }
    }

    public static class getVehicleEntityId extends Block {
        public getVehicleEntityId() {
            super( "getVehicleEntityId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityAttach);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityAttach) event).getVehicleEntityId();

        }
    }

    public static class setVehicleEntityId extends Block {
        public setVehicleEntityId() {
            super( "setVehicleEntityId", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityAttach, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityAttach) event).vehicleEntityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setEntityIdach extends Block {
        public setEntityIdach() {
            super( "setEntityIdach", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityAttach, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityAttach) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
