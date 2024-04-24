package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSpawnPlayer;

public class pSPacketSpawnPlayer extends Block {
    public pSPacketSpawnPlayer() {
        super( "SpawnPlayer", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSpawnPlayer);
        this.packetclasses.add(SPacketSpawnPlayer.class);
    }

    public static class getEntityIDyer extends Block {
        public getEntityIDyer() {
            super( "getEntityIDyer", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPlayer) event).getEntityID();

        }
    }

    public static class getPitchyer extends Block {
        public getPitchyer() {
            super( "getPitchyer", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPlayer) event).getPitch();

        }
    }

    public static class getYawyer extends Block {
        public getYawyer() {
            super( "getYawyer", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPlayer) event).getYaw();

        }
    }

    public static class getXyer extends Block {
        public getXyer() {
            super( "getXyer", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPlayer) event).getX();

        }
    }

    public static class getYyer extends Block {
        public getYyer() {
            super( "getYyer", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPlayer) event).getY();

        }
    }

    public static class getZyer extends Block {
        public getZyer() {
            super( "getZyer", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnPlayer) event).getZ();

        }
    }

    public static class setentityIdyer extends Block {
        public setentityIdyer() {
            super( "setentityIdyer", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPlayer) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setXyer extends Block {
        public setXyer() {
            super( "setXyer", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPlayer) event).x = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYyer extends Block {
        public setYyer() {
            super( "setYyer", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPlayer) event).y = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZyer extends Block {
        public setZyer() {
            super( "setZyer", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPlayer) event).z = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYawyer extends Block {
        public setYawyer() {
            super( "setYawyer", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPlayer) event).yaw = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPitchyer extends Block {
        public setPitchyer() {
            super( "setPitchyer", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnPlayer) event).pitch = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
