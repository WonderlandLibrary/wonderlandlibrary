package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketEntityStatus;

public class pSPacketEntityStatus extends Block {
    public pSPacketEntityStatus() {
        super( "EntityStatus", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntityStatus);
        this.packetclasses.add(SPacketEntityStatus.class);
    }

    public static class getEntitytus extends Block {
        public getEntitytus() {
            super( "getEntitytus", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityStatus);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityStatus) event).getEntity(Minecraft.getMinecraft().world);
        }
    }

    public static class getOpCode extends Block {
        public getOpCode() {
            super( "getOpCode", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityStatus);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityStatus) event).getOpCode();
        }
    }

    public static class setOpCode extends Block {
        public setOpCode() {
            super( "setOpCode", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityStatus, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityStatus) event).logicOpcode = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }

    }

    public static class setentityIdtus extends Block {
        public setentityIdtus() {
            super( "setentityIdtus", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityStatus, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityStatus) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
