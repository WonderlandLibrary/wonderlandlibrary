package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.world.EnumDifficulty;

public class pSPacketServerDifficulty extends Block {
    public pSPacketServerDifficulty() {
        super( "ServerDifficulty", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketServerDifficulty);
        this.packetclasses.add(SPacketServerDifficulty.class);
    }

    public static class isDifficultyLocked extends Block {
        public isDifficultyLocked() {
            super( "isDifficultyLocked", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketServerDifficulty);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketServerDifficulty) event).isDifficultyLocked();
        }
    }

    public static class getDifficultylty extends Block {
        public getDifficultylty() {
            super( "getDifficultylty", MainBlockType.EnumDifficulty, Tabs.Sub, BlockObjects.Name, Headers.SPacketServerDifficulty);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketServerDifficulty) event).getDifficulty();
        }
    }


    public static class setIsLocked extends Block {
        public setIsLocked() {
            super( "setIsLocked", MainBlockType.Default, Tabs.Sub, Headers.SPacketServerDifficulty, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketServerDifficulty) event).difficultyLocked = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setDifficultylty extends Block {
        public setDifficultylty() {
            super( "setDifficultylty", MainBlockType.Default, Tabs.Sub, Headers.SPacketServerDifficulty, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.EnumDifficulty));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketServerDifficulty) event).difficulty = (EnumDifficulty) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
