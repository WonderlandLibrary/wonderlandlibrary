package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketEntityTeleport;

public class pSPacketEntityTeleport extends Block {
    public pSPacketEntityTeleport() {
        super( "EntityTeleport", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntityTeleport);
        this.packetclasses.add(SPacketEntityTeleport.class);
    }

    public static class getEntityIdort extends Block {
        public getEntityIdort() {
            super( "getEntityIdort", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityTeleport);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityTeleport) event).getEntityId();
        }
    }

    public static class getPitchort extends Block {
        public getPitchort() {
            super( "getPitchort", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityTeleport);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityTeleport) event).getPitch();
        }
    }

    public static class getYawort extends Block {
        public getYawort() {
            super( "getYawort", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityTeleport);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityTeleport) event).getYaw();
        }
    }

    public static class getXort extends Block {
        public getXort() {
            super( "getXort", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityTeleport);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityTeleport) event).getX();
        }
    }

    public static class getYort extends Block {
        public getYort() {
            super( "getYort", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityTeleport);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityTeleport) event).getY();
        }
    }

    public static class getZort extends Block {
        public getZort() {
            super( "getZort", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityTeleport);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityTeleport) event).getZ();
        }
    }

    public static class getOnGroundort extends Block {
        public getOnGroundort() {
            super( "getOnGroundort", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityTeleport);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityTeleport) event).getOnGround();
        }
    }

    public static class setEntityIdort extends Block {
        public setEntityIdort() {
            super( "setEntityIdort", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityTeleport, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityTeleport) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPitchort extends Block {
        public setPitchort() {
            super( "setPitchort", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityTeleport, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityTeleport) event).pitch = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYawort extends Block {
        public setYawort() {
            super( "setYawort", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityTeleport, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityTeleport) event).yaw = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setXort extends Block {
        public setXort() {
            super( "setXort", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityTeleport, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityTeleport) event).posX = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYort extends Block {
        public setYort() {
            super( "setYort", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityTeleport, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityTeleport) event).posY = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZort extends Block {
        public setZort() {
            super( "setZort", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityTeleport, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityTeleport) event).posZ = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setOnGround extends Block {
        public setOnGround() {
            super( "setOnGround", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityTeleport, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityTeleport) event).onGround = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


}
