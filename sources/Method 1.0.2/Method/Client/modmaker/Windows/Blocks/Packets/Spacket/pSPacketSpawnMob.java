package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSpawnMob;

public class pSPacketSpawnMob extends Block {
    public pSPacketSpawnMob() {
        super( "SpawnMob", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSpawnMob);
        this.packetclasses.add(SPacketSpawnMob.class);
    }

    public static class getEntityIDMob extends Block {
        public getEntityIDMob() {
            super( "getEntityIDMob", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getEntityID();

        }
    }
    public static class getEntityType extends Block {
        public getEntityType() {
            super( "getEntityType", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getEntityType();

        }
    }
    public static class getXMob extends Block {
        public getXMob() {
            super( "getXMob", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getX();

        }
    }
    public static class getYMob extends Block {
        public getYMob() {
            super( "getYMob", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getY();

        }
    }
    public static class getZMob extends Block {
        public getZMob() {
            super( "getZMob", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getZ();

        }
    }
    public static class getVelocityX extends Block {
        public getVelocityX() {
            super( "getVelocityX", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getVelocityX();

        }
    }
    public static class getVelocityY extends Block {
        public getVelocityY() {
            super( "getVelocityY", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getVelocityY();

        }
    }
    public static class getVelocityZ extends Block {
        public getVelocityZ() {
            super( "getVelocityZ", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getVelocityZ();

        }
    }
    public static class getYawMob extends Block {
        public getYawMob() {
            super( "getYawMob", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getYaw();

        }
    }
    public static class getPitchMob extends Block {
        public getPitchMob() {
            super( "getPitchMob", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getPitch();

        }
    }
    public static class getHeadPitch extends Block {
        public getHeadPitch() {
            super( "getHeadPitch", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSpawnMob);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketSpawnMob) event).getHeadPitch();

        }
    }
    public static class setXMob extends Block {
        public setXMob() {
            super( "setXMob", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).x = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setYMob extends Block {
        public setYMob() {
            super( "setYMob", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).y = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setZMob extends Block {
        public setZMob() {
            super( "setZMob", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).z = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setYawMob extends Block {
        public setYawMob() {
            super( "setYawMob", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).yaw = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setPitchMob extends Block {
        public setPitchMob() {
            super( "setPitchMob", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).pitch = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setHeadPitch extends Block {
        public setHeadPitch() {
            super( "setHeadPitch", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).headPitch = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setVelX extends Block {
        public setVelX() {
            super( "setVelX", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).velocityX = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setVelY extends Block {
        public setVelY() {
            super( "setVelY", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).velocityY = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setVelZ extends Block {
        public setVelZ() {
            super( "setVelZ", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).velocityZ = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class settypeMob extends Block {
        public settypeMob() {
            super( "settypeMob", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).type = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setentityIdMob extends Block {
        public setentityIdMob() {
            super( "setentityIdMob", MainBlockType.Default, Tabs.Sub,Headers.SPacketSpawnMob, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSpawnMob) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
