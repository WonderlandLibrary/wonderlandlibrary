package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class pSPacketRespawn extends Block {
    public pSPacketRespawn() {
        super( "PacketRespawn", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketRespawn);
        this.packetclasses.add(SPacketRespawn.class);
    }

    public static class getDimensionID extends Block {
        public getDimensionID() {
            super( "getDimensionID", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketRespawn);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketRespawn) event).getDimensionID();
        }
    }

    public static class getDifficultyawn extends Block {
        public getDifficultyawn() {
            super( "getDifficultyawn", MainBlockType.EnumDifficulty, Tabs.Sub, BlockObjects.Name, Headers.SPacketRespawn);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketRespawn) event).getDifficulty();
        }
    }

    public static class getGameTypeawn extends Block {
        public getGameTypeawn() {
            super( "getGameTypeawn", MainBlockType.GameType, Tabs.Sub, BlockObjects.Name, Headers.SPacketRespawn);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketRespawn) event).getGameType();
        }
    }

    public static class getWorldTypeawn extends Block {
        public getWorldTypeawn() {
            super( "getWorldTypeawn", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketRespawn);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketRespawn) event).getWorldType().toString();
        }
    }

    public static class setGameTypeawn extends Block {
        public setGameTypeawn() {
            super( "setGameTypeawn", MainBlockType.Default, Tabs.Sub, Headers.SPacketRespawn, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.GameType));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketRespawn) event).gameType = (GameType) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setDimensionID extends Block {
        public setDimensionID() {
            super( "setDimensionID", MainBlockType.Default, Tabs.Sub, Headers.SPacketRespawn, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketRespawn) event).dimensionID = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setDifficultyawn extends Block {
        public setDifficultyawn() {
            super( "setDifficultyawn", MainBlockType.Default, Tabs.Sub, Headers.SPacketRespawn, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.EnumDifficulty));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketRespawn) event).difficulty = (EnumDifficulty) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setWorldTypeawn extends Block {
        public setWorldTypeawn() {
            super( "setWorldTypeawn", MainBlockType.Default, Tabs.Sub, Headers.SPacketRespawn, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketRespawn) event).worldType = WorldType.byName((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

}
