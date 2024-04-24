package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketMoveVehicle;

public class pSPacketMoveVehicle extends Block {
    public pSPacketMoveVehicle() {
        super( "MoveVehicle", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketMoveVehicle);
        this.description = "Packet from server to move a vehicle.";
        this.packetclasses.add(SPacketMoveVehicle.class);
    }

    public static class getPitchcle extends Block {
        public getPitchcle() {
            super( "getPitchcle", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMoveVehicle);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMoveVehicle) event).getPitch();

        }
    }

    public static class getYawcle extends Block {
        public getYawcle() {
            super( "getYawcle", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMoveVehicle);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMoveVehicle) event).getYaw();

        }
    }

    public static class getXcle extends Block {
        public getXcle() {
            super( "getXcle", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMoveVehicle);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMoveVehicle) event).getX();

        }
    }

    public static class getYcle extends Block {
        public getYcle() {
            super( "getYcle", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMoveVehicle);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMoveVehicle) event).getY();

        }
    }

    public static class getZcle extends Block {
        public getZcle() {
            super( "getZcle", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketMoveVehicle);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketMoveVehicle) event).getZ();

        }
    }


    public static class setPitchcle extends Block {
        public setPitchcle() {
            super( "setPitchcle", MainBlockType.Default, Tabs.Sub, Headers.SPacketMoveVehicle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMoveVehicle) event).pitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYawcle extends Block {
        public setYawcle() {
            super( "setYawcle", MainBlockType.Default, Tabs.Sub, Headers.SPacketMoveVehicle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMoveVehicle) event).yaw = ((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setXcle extends Block {
        public setXcle() {
            super( "setXcle", MainBlockType.Default, Tabs.Sub, Headers.SPacketMoveVehicle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMoveVehicle) event).x = ((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYcle extends Block {
        public setYcle() {
            super( "setYcle", MainBlockType.Default, Tabs.Sub, Headers.SPacketMoveVehicle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMoveVehicle) event).y = ((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZcle extends Block {
        public setZcle() {
            super( "setZcle", MainBlockType.Default, Tabs.Sub, Headers.SPacketMoveVehicle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketMoveVehicle) event).z = ((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

}
