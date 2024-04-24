package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;

public class pSPacketSpawnGlobalEntity extends Block {
    public pSPacketSpawnGlobalEntity() {
        super( "SpawnGlobalEntity", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSpawnGlobalEntity);
        this.packetclasses.add(SPacketSpawnGlobalEntity.class);
    }

    public static class getEntityIdgid extends Block {
        public getEntityIdgid() {
            super( "getEntityIdgid", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnGlobalEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnGlobalEntity) event).getEntityId();
        }
    }
    public static class getXgid extends Block {
        public getXgid() {
            super( "getXgid", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnGlobalEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnGlobalEntity) event).getX();
        }
    }
    public static class getYgid extends Block {
        public getYgid() {
            super( "getYgid", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnGlobalEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnGlobalEntity) event).getY();
        }
    }
    public static class getZgid extends Block {
        public getZgid() {
            super( "getZgid", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnGlobalEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnGlobalEntity) event).getZ();
        }
    }
    public static class getTypegid extends Block {
        public getTypegid() {
            super( "getTypegid", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnGlobalEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnGlobalEntity) event).getType();
        }
    }
    public static class setentityIdgid extends Block {
        public setentityIdgid() {
            super( "setentityIdgid", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnGlobalEntity, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnGlobalEntity) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setTypegid extends Block {
        public setTypegid() {
            super( "setTypegid", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnGlobalEntity, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnGlobalEntity) event).type = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setXgid extends Block {
        public setXgid() {
            super( "setXgid", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnGlobalEntity, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnGlobalEntity) event).x =  dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setYgid extends Block {
        public setYgid() {
            super( "setYgid", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnGlobalEntity, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnGlobalEntity) event).y = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setZgid extends Block {
        public setZgid() {
            super( "setZgid", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnGlobalEntity, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnGlobalEntity) event).z = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
