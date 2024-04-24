package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class pSPacketJoinGame extends Block {
    public pSPacketJoinGame() {
        super( "JoinGame", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketJoinGame);
        this.packetclasses.add(SPacketJoinGame.class);
    }

    public static class getPlayerId extends Block {
        public getPlayerId() {
            super( "getPlayerId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketJoinGame);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketJoinGame) event).getPlayerId();

        }
    }

    public static class getDimension extends Block {
        public getDimension() {
            super( "getDimension", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketJoinGame);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketJoinGame) event).getDimension();

        }
    }

    public static class getMaxPlayers extends Block {
        public getMaxPlayers() {
            super( "getMaxPlayers", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketJoinGame);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketJoinGame) event).getMaxPlayers();

        }
    }

    public static class getGameType extends Block {
        public getGameType() {
            super( "getGameType", MainBlockType.GameType, Tabs.Sub, BlockObjects.Name, Headers.SPacketJoinGame);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketJoinGame) event).getGameType();

        }
    }

    public static class getWorldType extends Block {
        public getWorldType() {
            super( "getWorldType", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketJoinGame);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketJoinGame) event).getWorldType();

        }
    }

    public static class getDifficulty extends Block {
        public getDifficulty() {
            super( "getDifficulty", MainBlockType.EnumDifficulty, Tabs.Sub, BlockObjects.Name, Headers.SPacketJoinGame);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketJoinGame) event).getDifficulty();

        }
    }

    public static class setPlayerIdame extends Block {
        public setPlayerIdame() {
            super( "setPlayerIdame", MainBlockType.Default, Tabs.Sub, Headers.SPacketJoinGame, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketJoinGame) event).playerId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setDimension extends Block {
        public setDimension() {
            super( "setDimension", MainBlockType.Default, Tabs.Sub, Headers.SPacketJoinGame, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketJoinGame) event).dimension = ((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMaxPlayers extends Block {
        public setMaxPlayers() {
            super( "setMaxPlayers", MainBlockType.Default, Tabs.Sub, Headers.SPacketJoinGame, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketJoinGame) event).maxPlayers = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setGameType extends Block {
        public setGameType() {
            super( "setGameType", MainBlockType.Default, Tabs.Sub, Headers.SPacketJoinGame, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.GameType));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketJoinGame) event).gameType = (GameType) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setWorldType extends Block {
        public setWorldType() {
            super( "setWorldType", MainBlockType.Default, Tabs.Sub, Headers.SPacketJoinGame, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketJoinGame) event).worldType = WorldType.byName((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setDifficulty extends Block {
        public setDifficulty() {
            super( "setDifficulty", MainBlockType.Default, Tabs.Sub, Headers.SPacketJoinGame, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.EnumDifficulty));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketJoinGame) event).difficulty = (EnumDifficulty) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setReducedDebugInfo extends Block {
        public setReducedDebugInfo() {
            super( "setReducedDebugInfo", MainBlockType.Default, Tabs.Sub, Headers.SPacketJoinGame, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketJoinGame) event).reducedDebugInfo = (dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class sethardcoreMode extends Block {
        public sethardcoreMode() {
            super( "sethardcoreMode", MainBlockType.Default, Tabs.Sub, Headers.SPacketJoinGame, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketJoinGame) event).hardcoreMode = (dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }
}
