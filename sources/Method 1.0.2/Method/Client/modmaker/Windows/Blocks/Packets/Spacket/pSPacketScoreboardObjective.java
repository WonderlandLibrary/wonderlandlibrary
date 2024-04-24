package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketScoreboardObjective;

public class pSPacketScoreboardObjective extends Block {
    public pSPacketScoreboardObjective() {
        super( "ScoreboardObjective", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketScoreboardObjective);
        this.packetclasses.add(SPacketScoreboardObjective.class);
    }

    public static class getObjectiveName extends Block {
        public getObjectiveName() {
            super( "getObjectiveName", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketScoreboardObjective);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketScoreboardObjective) event).getObjectiveName();

        }
    }
    public static class getObjectiveValue extends Block {
        public getObjectiveValue() {
            super( "getObjectiveValue", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketScoreboardObjective);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketScoreboardObjective) event).getObjectiveValue();

        }
    }
    public static class setobjectiveName extends Block {
        public setobjectiveName() {
            super( "setobjectiveName", MainBlockType.Default, Tabs.Sub,Headers.SPacketScoreboardObjective, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketScoreboardObjective) event).objectiveName = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setobjectiveValue extends Block {
        public setobjectiveValue() {
            super( "setobjectiveValue", MainBlockType.Default, Tabs.Sub,Headers.SPacketScoreboardObjective, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketScoreboardObjective) event).objectiveValue = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }



}
