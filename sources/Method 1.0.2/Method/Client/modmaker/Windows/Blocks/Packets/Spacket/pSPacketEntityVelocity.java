package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public class pSPacketEntityVelocity extends Block {
    public pSPacketEntityVelocity() {
        super( "EntityVelocity", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntityVelocity);
        this.packetclasses.add(SPacketEntityVelocity.class);
    }

    public static class getEntityIDity extends Block {
        public getEntityIDity() {
            super( "getEntityIDity", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityVelocity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityVelocity) event).getEntityID();
        }
    }

    public static class getMotionX extends Block {
        public getMotionX() {
            super( "getMotionX", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityVelocity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityVelocity) event).getMotionX();
        }
    }

    public static class getMotionY extends Block {
        public getMotionY() {
            super( "getMotionY", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityVelocity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityVelocity) event).getMotionY();
        }
    }

    public static class getMotionZ extends Block {
        public getMotionZ() {
            super( "getMotionZ", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityVelocity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityVelocity) event).getMotionZ();
        }
    }

    public static class setEntityIDity extends Block {
        public setEntityIDity() {
            super( "setEntityIDity", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityVelocity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityVelocity) event).entityID = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMotionX extends Block {
        public setMotionX() {
            super( "setMotionX", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityVelocity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityVelocity) event).motionX = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMotionY extends Block {
        public setMotionY() {
            super( "setMotionY", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityVelocity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityVelocity) event).motionY = ((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMotionZ extends Block {
        public setMotionZ() {
            super( "setMotionZ", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityVelocity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityVelocity) event).motionZ = ((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }


}
