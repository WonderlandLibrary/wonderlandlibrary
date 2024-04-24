package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSetExperience;

public class pSPacketSetExperience extends Block {
    public pSPacketSetExperience() {
        super( "SetExperience", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSetExperience);
        this.packetclasses.add(SPacketSetExperience.class);
    }

    public static class getExperienceBar extends Block {
        public getExperienceBar() {
            super( "getExperienceBar", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSetExperience);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetExperience) event).getExperienceBar();

        }
    }

    public static class getLevel extends Block {
        public getLevel() {
            super( "getLevel", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSetExperience);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetExperience) event).getLevel();

        }
    }

    public static class getTotalExperience extends Block {
        public getTotalExperience() {
            super( "getTotalExperience", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSetExperience);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetExperience) event).getTotalExperience();

        }
    }

    public static class setExperienceBar extends Block {
        public setExperienceBar() {
            super( "setExperienceBar", MainBlockType.Default, Tabs.Sub, Headers.SPacketSetExperience, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetExperience) event).experienceBar = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);

        }
    }

    public static class setLevel extends Block {
        public setLevel() {
            super( "setLevel", MainBlockType.Default, Tabs.Sub, Headers.SPacketSetExperience, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetExperience) event).level = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);

        }
    }

    public static class setTotalExperience extends Block {
        public setTotalExperience() {
            super( "setTotalExperience", MainBlockType.Default, Tabs.Sub, Headers.SPacketSetExperience, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetExperience) event).totalExperience = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);

        }
    }

}
