package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketEntityEffect;

public class pSPacketEntityEffect extends Block {
    public pSPacketEntityEffect() {
        super( "EntityEffect", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntityEffect);
        this.packetclasses.add(SPacketEntityEffect.class);
    }

    public static class isMaxDuration extends Block {
        public isMaxDuration() {
            super( "isMaxDuration", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityEffect);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityEffect) event).isMaxDuration();

        }
    }

    public static class getIsAmbient extends Block {
        public getIsAmbient() {
            super( "getIsAmbient", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityEffect);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityEffect) event).getIsAmbient();

        }
    }

    public static class getEntityIdect extends Block {
        public getEntityIdect() {
            super( "getEntityIdect", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityEffect) event).getEntityId();

        }
    }

    public static class getEffectId extends Block {
        public getEffectId() {
            super( "getEffectId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityEffect) event).getEffectId();

        }
    }

    public static class getAmplifier extends Block {
        public getAmplifier() {
            super( "getAmplifier", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityEffect) event).getAmplifier();

        }
    }

    public static class getDuration extends Block {
        public getDuration() {
            super( "getDuration", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityEffect);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityEffect) event).getDuration();

        }
    }


    public static class setEntityIdect extends Block {
        public setEntityIdect() {
            super( "setEntityIdect", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityEffect) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setEffectId extends Block {
        public setEffectId() {
            super( "setEffectId", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityEffect) event).effectId = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setAmplifier extends Block {
        public setAmplifier() {
            super( "setAmplifier", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityEffect) event).amplifier = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setDurationect extends Block {
        public setDurationect() {
            super( "setDurationect", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityEffect) event).duration = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setflags extends Block {
        public setflags() {
            super( "setflags", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityEffect) event).flags = (byte) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
