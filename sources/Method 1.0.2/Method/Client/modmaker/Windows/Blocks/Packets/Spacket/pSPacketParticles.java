package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.EnumParticleTypes;

public class pSPacketParticles extends Block {
    public pSPacketParticles() {
        super( "Particles", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketParticles);
        this.packetclasses.add(SPacketParticles.class);
    }

    public static class getParticleType extends Block {
        public getParticleType() {
            super( "getParticleType", MainBlockType.ParticleTypes, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).getParticleType();

        }
    }

    public static class isLongDistance extends Block {
        public isLongDistance() {
            super( "isLongDistance", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).isLongDistance();

        }
    }

    public static class getXOffset extends Block {
        public getXOffset() {
            super( "getXOffset", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).getXOffset();

        }
    }

    public static class getYOffset extends Block {
        public getYOffset() {
            super( "getYOffset", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).getYOffset();

        }
    }

    public static class getZOffset extends Block {
        public getZOffset() {
            super( "getZOffset", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).getZOffset();

        }
    }

    public static class getXCoordinate extends Block {
        public getXCoordinate() {
            super( "getXCoordinate", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).getXCoordinate();

        }
    }

    public static class getYCoordinate extends Block {
        public getYCoordinate() {
            super( "getYCoordinate", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).getYCoordinate();

        }
    }

    public static class getZCoordinate extends Block {
        public getZCoordinate() {
            super( "getZCoordinate", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).getZCoordinate();

        }
    }

    public static class getParticleSpeed extends Block {
        public getParticleSpeed() {
            super( "getParticleSpeed", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).getParticleSpeed();

        }
    }

    public static class getParticleCount extends Block {
        public getParticleCount() {
            super( "getParticleCount", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketParticles);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketParticles) event).getParticleCount();

        }
    }

    public static class ParticleTypes extends Block {
        public ParticleTypes() {
            super( "ParticleTypes", MainBlockType.ParticleTypes, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.DropDown);
            for (EnumParticleTypes value : EnumParticleTypes.values()) {
                this.ddOptions.add(value.toString());
            }
        }
    }

    public static class setParticleType extends Block {
        public setParticleType() {
            super( "setParticleType", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.ParticleTypes));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).particleType = (EnumParticleTypes) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setxCoord extends Block {
        public setxCoord() {
            super( "setxCoord", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).xCoord = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setyCoord extends Block {
        public setyCoord() {
            super( "setyCoord", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).yCoord = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setzCoord extends Block {
        public setzCoord() {
            super( "setzCoord", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).zCoord = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setxOffset extends Block {
        public setxOffset() {
            super( "setxOffset", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).xOffset = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setyOffset extends Block {
        public setyOffset() {
            super( "setyOffset", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).yOffset = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setzOffset extends Block {
        public setzOffset() {
            super( "setzOffset", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).zOffset = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setparticleSpeed extends Block {
        public setparticleSpeed() {
            super( "setparticleSpeed", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).particleSpeed = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setparticleCount extends Block {
        public setparticleCount() {
            super( "setparticleCount", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).particleCount = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setlongDistance extends Block {
        public setlongDistance() {
            super( "setlongDistance", MainBlockType.Default, Tabs.Sub, Headers.SPacketParticles, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketParticles) event).longDistance = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}

