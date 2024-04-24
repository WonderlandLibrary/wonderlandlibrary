package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSpawnObject;

public class pSPacketSpawnObject extends Block {
    public pSPacketSpawnObject() {
        super( "SpawnObject", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSpawnObject);
        this.packetclasses.add(SPacketSpawnObject.class);
    }

    public static class getEntityIDpso extends Block {
        public getEntityIDpso() {
            super( "getEntityIDpso", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getEntityID();

        }
    }

    public static class getXpso extends Block {
        public getXpso() {
            super( "getXpso", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getX();

        }
    }

    public static class getYpso extends Block {
        public getYpso() {
            super( "getYpso", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getY();

        }
    }

    public static class getZpso extends Block {
        public getZpso() {
            super( "getZpso", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getZ();

        }
    }

    public static class getSpeedX extends Block {
        public getSpeedX() {
            super( "getSpeedX", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getSpeedX();

        }
    }

    public static class getSpeedY extends Block {
        public getSpeedY() {
            super( "getSpeedY", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getSpeedY();

        }
    }

    public static class getSpeedZ extends Block {
        public getSpeedZ() {
            super( "getSpeedZ", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getSpeedZ();

        }
    }

    public static class getYawpso extends Block {
        public getYawpso() {
            super( "getYawpso", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getYaw();

        }
    }

    public static class getPitchpso extends Block {
        public getPitchpso() {
            super( "getPitchpso", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getPitch();

        }
    }

    public static class getTypepso extends Block {
        public getTypepso() {
            super( "getTypepso", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getType();

        }
    }

    public static class getData extends Block {
        public getData() {
            super( "getData", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnObject);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSpawnObject) event).getData();

        }
    }

    public static class setXpso extends Block {
        public setXpso() {
            super( "setXpso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).x = (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYpso extends Block {
        public setYpso() {
            super( "setYpso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).y = (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZpso extends Block {
        public setZpso() {
            super( "setZpso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).z = (dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYawpso extends Block {
        public setYawpso() {
            super( "setYawpso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).yaw = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPitchpso extends Block {
        public setPitchpso() {
            super( "setPitchpso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).pitch = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class setMotionXpso extends Block {
        public setMotionXpso() {
            super( "setMotionXpso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).speedX = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMotionYpso extends Block {
        public setMotionYpso() {
            super( "setMotionYpso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).speedY = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMotionZpso extends Block {
        public setMotionZpso() {
            super( "setMotionZpso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).speedZ = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setentityIdpso extends Block {
        public setentityIdpso() {
            super( "setentityIdpso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class settypepso extends Block {
        public settypepso() {
            super( "settypepso", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).type = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setdata extends Block {
        public setdata() {
            super( "setdata", MainBlockType.Default, Tabs.Sub, Headers.SPacketSpawnObject, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnObject) event).data = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
