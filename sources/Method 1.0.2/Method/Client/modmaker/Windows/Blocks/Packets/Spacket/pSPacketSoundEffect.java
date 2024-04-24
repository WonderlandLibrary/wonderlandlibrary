package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;

public class pSPacketSoundEffect extends Block {
    public pSPacketSoundEffect() {
        super( "SoundEffect", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSoundEffect);
        this.packetclasses.add(SPacketSoundEffect.class);
    }


    public static class getCategoryect extends Block {
        public getCategoryect() {
            super( "getCategoryect", MainBlockType.SoundCategory, Tabs.Sub, BlockObjects.Name, Headers.SPacketSoundEffect);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketSoundEffect) event).getCategory();

        }
    }

    public static class getSoundEventect extends Block {
        public getSoundEventect() {
            super( "getSoundEventect", MainBlockType.SoundEvent, Tabs.Sub, BlockObjects.Name, Headers.SPacketSoundEffect);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketSoundEffect) event).getSound();
        }
    }

    public static class getPitchect extends Block {
        public getPitchect() {
            super( "getPitchect", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSoundEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSoundEffect) event).getPitch();

        }
    }

    public static class getVolumeect extends Block {
        public getVolumeect() {
            super( "getVolumeect", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSoundEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSoundEffect) event).getVolume();

        }
    }

    public static class getXect extends Block {
        public getXect() {
            super( "getXect", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSoundEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSoundEffect) event).getX();

        }
    }

    public static class getYect extends Block {
        public getYect() {
            super( "getYect", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSoundEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSoundEffect) event).getY();

        }
    }

    public static class getZect extends Block {
        public getZect() {
            super( "getZect", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSoundEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSoundEffect) event).getZ();

        }
    }

    public static class setCategoryect extends Block {
        public setCategoryect() {
            super( "setCategoryect", MainBlockType.Default, Tabs.Sub, Headers.SPacketSoundEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.SoundCategory));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSoundEffect) event).category = (SoundCategory) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPitchect extends Block {
        public setPitchect() {
            super( "setPitchect", MainBlockType.Default, Tabs.Sub, Headers.SPacketSoundEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSoundEffect) event).soundPitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setVolumeect extends Block {
        public setVolumeect() {
            super( "setVolumeect", MainBlockType.Default, Tabs.Sub, Headers.SPacketSoundEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSoundEffect) event).soundVolume = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setXect extends Block {
        public setXect() {
            super( "setXect", MainBlockType.Default, Tabs.Sub, Headers.SPacketSoundEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSoundEffect) event).posX = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYect extends Block {
        public setYect() {
            super( "setYect", MainBlockType.Default, Tabs.Sub, Headers.SPacketSoundEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSoundEffect) event).posY = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZect extends Block {
        public setZect() {
            super( "setZect", MainBlockType.Default, Tabs.Sub, Headers.SPacketSoundEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSoundEffect) event).posZ = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
