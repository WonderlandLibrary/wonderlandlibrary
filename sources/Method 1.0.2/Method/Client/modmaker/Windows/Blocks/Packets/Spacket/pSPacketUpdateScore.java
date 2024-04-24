package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketUpdateScore;

public class pSPacketUpdateScore extends Block {
    public pSPacketUpdateScore() {
        super( "UpdateScore", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketUpdateScore);
        this.packetclasses.add(SPacketUpdateScore.class);
    }

    public static class getScoreAction extends Block {
        public getScoreAction() {
            super( "getScoreAction", MainBlockType.ScoreAction, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateScore);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateScore) event).getScoreAction();

        }
    }

    public static class getObjectiveNameore extends Block {
        public getObjectiveNameore() {
            super( "getObjectiveNameore", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateScore);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateScore) event).getObjectiveName();

        }
    }

    public static class getPlayerName extends Block {
        public getPlayerName() {
            super( "getPlayerName", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateScore);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateScore) event).getPlayerName();

        }
    }

    public static class getScoreValue extends Block {
        public getScoreValue() {
            super( "getScoreValue", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateScore);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateScore) event).getScoreValue();

        }
    }

    public static class ScoreAction extends Block {
        public ScoreAction() {
            super( "ScoreAction", MainBlockType.ScoreAction, Tabs.Sub, Headers.SPacketUpdateScore, BlockObjects.Name, BlockObjects.DropDown);
            for (SPacketUpdateScore.Action value : SPacketUpdateScore.Action.values()) {
                this.ddOptions.add(value.toString());
            }
        }
    }

    public static class setScoreAction extends Block {
        public setScoreAction() {
            super( "setScoreAction", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateScore, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.ScoreAction));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateScore) event).action = (SPacketUpdateScore.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setObjectiveName extends Block {
        public setObjectiveName() {
            super( "setObjectiveName", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateScore, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateScore) event).name = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setobjective extends Block {
        public setobjective() {
            super( "setobjective", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateScore, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateScore) event).objective = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setScoreValue extends Block {
        public setScoreValue() {
            super( "setScoreValue", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateScore, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateScore) event).value = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
