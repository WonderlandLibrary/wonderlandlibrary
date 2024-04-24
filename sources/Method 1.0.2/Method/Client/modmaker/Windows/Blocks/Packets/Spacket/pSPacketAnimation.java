package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketAnimation;

public class pSPacketAnimation extends Block {
    public pSPacketAnimation() {
        super( "ServerAnimation", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketAnimation);
        this.packetclasses.add(SPacketAnimation.class);
    }

    public static class getAnimationType extends Block {
        public getAnimationType() {
            super( "getAnimationType", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketAnimation);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketAnimation) event).getAnimationType();

        }
    }

    public static class getEntityID extends Block {
        public getEntityID() {
            super( "getEntityID", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketAnimation);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketAnimation) event).getEntityID();

        }
    }

    public static class setAnimationType extends Block {
        public setAnimationType() {
            super( "setAnimationType", MainBlockType.Default, Tabs.Sub,Headers.SPacketAnimation, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketAnimation) event).type = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setEntityIDAni extends Block {
        public setEntityIDAni() {
            super( "setEntityIDAni", MainBlockType.Default, Tabs.Sub,Headers.SPacketAnimation, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketAnimation) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
