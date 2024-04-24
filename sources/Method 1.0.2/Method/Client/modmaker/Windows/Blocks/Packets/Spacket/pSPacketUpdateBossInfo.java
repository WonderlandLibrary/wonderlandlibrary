package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.text.TextComponentString;

public class pSPacketUpdateBossInfo extends Block {
    public pSPacketUpdateBossInfo() {
        super( "UpdateBossInfo", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketUpdateBossInfo);
        this.packetclasses.add(SPacketUpdateBossInfo.class);
    }

    public static class getPercent extends Block {
        public getPercent() {
            super( "getPercent", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateBossInfo);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateBossInfo) event).getPercent();
        }
    }

    public static class getNamenfo extends Block {
        public getNamenfo() {
            super( "getNamenfo", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateBossInfo);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateBossInfo) event).getName();
        }
    }

    public static class shouldCreateFog extends Block {
        public shouldCreateFog() {
            super( "shouldCreateFog", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateBossInfo);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateBossInfo) event).shouldCreateFog();
        }
    }

    public static class shouldDarkenSky extends Block {
        public shouldDarkenSky() {
            super( "shouldDarkenSky", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateBossInfo);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateBossInfo) event).shouldDarkenSky();
        }
    }

    public static class shouldPlayEndBossMusic extends Block {
        public shouldPlayEndBossMusic() {
            super( "shouldPlayEndBossMusic", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateBossInfo);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateBossInfo) event).shouldPlayEndBossMusic();
        }
    }

    public static class setPercent extends Block {
        public setPercent() {
            super( "setPercent", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateBossInfo, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateBossInfo) event).percent = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setDarkenSky extends Block {
        public setDarkenSky() {
            super( "setDarkenSky", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateBossInfo, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateBossInfo) event).darkenSky = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPlayEndBossMusic extends Block {
        public setPlayEndBossMusic() {
            super( "setPlayEndBossMusic", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateBossInfo, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateBossInfo) event).playEndBossMusic = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setCreateFog extends Block {
        public setCreateFog() {
            super( "setCreateFog", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateBossInfo, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateBossInfo) event).createFog = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setname extends Block {
        public setname() {
            super( "setname", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateBossInfo, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateBossInfo) event).name = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

}
