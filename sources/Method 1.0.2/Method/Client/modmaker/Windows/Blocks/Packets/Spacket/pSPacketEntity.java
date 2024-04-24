package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketEntity;

public class pSPacketEntity extends Block {
    public pSPacketEntity() {
        super( "SPacketEntity", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntity);
        this.packetclasses.add(SPacketEntity.S15PacketEntityRelMove.class);
        this.packetclasses.add(SPacketEntity.S16PacketEntityLook.class);
        this.packetclasses.add(SPacketEntity.S17PacketEntityLookMove.class);
        this.packetclasses.add(SPacketEntity.class);
    }

    public static class getOnGround extends Block {
        public getOnGround() {
            super( "getOnGround", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntity);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntity) event).getOnGround();

        }
    }
    public static class getisRotating extends Block {
        public getisRotating() {
            super( "getisRotating", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntity);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntity) event).isRotating();

        }
    }
    public static class getPitchity extends Block {
        public getPitchity() {
            super( "getPitchity", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntity) event).getPitch();

        }
    }

    public static class getYawity extends Block {
        public getYawity() {
            super( "getYawity", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntity) event).getYaw();

        }
    }

    public static class getXity extends Block {
        public getXity() {
            super( "getXity", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntity) event).getX();

        }
    }

    public static class getYity extends Block {
        public getYity() {
            super( "getYity", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntity) event).getY();

        }
    }

    public static class getZity extends Block {
        public getZity() {
            super( "getZity", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntity) event).getZ();

        }
    }

    public static class getEntityity extends Block {
        public getEntityity() {
            super( "getEntityity", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntity);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntity) event).getEntity(Minecraft.getMinecraft().world);

        }
    }

    public static class setPitchity extends Block {
        public setPitchity() {
            super( "setPitchity", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntity) event).pitch = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYaw extends Block {
        public setYaw() {
            super( "setYaw", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntity) event).yaw = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setXity extends Block {
        public setXity() {
            super( "setXity", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntity) event).posX = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYity extends Block {
        public setYity() {
            super( "setYity", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntity) event).posY = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZity extends Block {
        public setZity() {
            super( "setZity", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntity) event).posZ = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setEntityIdity extends Block {
        public setEntityIdity() {
            super( "setEntityIdity", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntity) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setonGround extends Block {
        public setonGround() {
            super( "setonGround", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntity, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntity) event).onGround = (dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setrotating extends Block {
        public setrotating() {
            super( "setrotating", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntity, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntity) event).rotating = (dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }


}
