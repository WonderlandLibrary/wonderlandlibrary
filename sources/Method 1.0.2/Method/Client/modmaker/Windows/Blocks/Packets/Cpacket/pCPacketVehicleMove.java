package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketVehicleMove;

public class pCPacketVehicleMove extends Block {
    public pCPacketVehicleMove() {
        super("VehicleMove", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketVehicleMove);
        this.packetclasses.add(CPacketVehicleMove.class);
        this.description = "Sends a vehicle move packet to the server.";
    }

    public static class getXove extends Block {
        public getXove() {
            super("getXove", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketVehicleMove);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketVehicleMove) event).getX();
        }
    }

    public static class setXove extends Block {
        public setXove() {
            super("setXove", MainBlockType.Default, Tabs.Sub, Headers.CPacketVehicleMove, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketVehicleMove) event).x = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class getYove extends Block {
        public getYove() {
            super("getYove", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketVehicleMove);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketVehicleMove) event).getY();
        }
    }
    public static class setYove extends Block {
        public setYove() {
            super("setYove", MainBlockType.Default, Tabs.Sub, Headers.CPacketVehicleMove, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketVehicleMove) event).y = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class getZove extends Block {
        public getZove() {
            super("getZove", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketVehicleMove);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketVehicleMove) event).getZ();
        }
    }

    public static class setZove extends Block {
        public setZove() {
            super("setZove", MainBlockType.Default, Tabs.Sub, Headers.CPacketVehicleMove, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketVehicleMove) event).z = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class getYawove extends Block {
        public getYawove() {
            super("getYawove", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketVehicleMove);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketVehicleMove) event).getYaw();
        }
    }

    public static class setYawove extends Block {
        public setYawove() {
            super("setYawove", MainBlockType.Default, Tabs.Sub, Headers.CPacketVehicleMove, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketVehicleMove) event).yaw = (float)dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class getPitchove extends Block {
        public getPitchove() {
            super("getPitchove", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketVehicleMove);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketVehicleMove) event).getPitch();
        }
    }

    public static class setPitchove extends Block {
        public setPitchove() {
            super("setPitchove", MainBlockType.Default, Tabs.Sub, Headers.CPacketVehicleMove, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketVehicleMove) event).pitch = (float)dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class SendVehicleMove extends Block {
        public SendVehicleMove() {
            super("SendVehicleMove", MainBlockType.Default, Tabs.Sub, Headers.CPacketVehicleMove, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send VehicleMove Entity:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // Entity vehicle
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketVehicleMove((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }

}
