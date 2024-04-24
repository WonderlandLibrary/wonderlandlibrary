package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketEntityHeadLook;

public class pSPacketEntityHeadLook extends Block {
    public pSPacketEntityHeadLook() {
        super( "EntityHeadLook", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntityHeadLook);
        this.packetclasses.add(SPacketEntityHeadLook.class);
    }

    public static class getEntityook extends Block {
        public getEntityook() {
            super( "getEntityook", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityHeadLook);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityHeadLook) event).getEntity(Minecraft.getMinecraft().world);
        }
    }
    public static class getYawook extends Block {
        public getYawook() {
            super( "getYawook", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityHeadLook);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityHeadLook) event).getYaw();
        }
    }
    public static class setentityIdook extends Block {
        public setentityIdook() {
            super( "setentityIdook", MainBlockType.Default, Tabs.Sub,Headers.SPacketEntityHeadLook, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityHeadLook) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setYawook extends Block {
        public setYawook() {
            super( "setYawook", MainBlockType.Default, Tabs.Sub,Headers.SPacketEntityHeadLook, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityHeadLook) event).yaw = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


}
